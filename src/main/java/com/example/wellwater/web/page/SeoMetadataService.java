package com.example.wellwater.web.page;

import com.example.wellwater.pseo.PseoDetailView;
import com.example.wellwater.pseo.PseoFaqItem;
import com.example.wellwater.pseo.PseoFamilyView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class SeoMetadataService {

    private final String baseUrl;
    private final String googleSiteVerification;
    private final String googleAnalyticsId;
    private final String socialImagePath;

    public SeoMetadataService(
            @Value("${app.site.base-url:}") String baseUrl,
            Environment environment
    ) {
        this.baseUrl = normalizeBaseUrl(baseUrl, isProduction(environment));
        this.googleSiteVerification = environment.getProperty("app.site.google-verification", "");
        this.googleAnalyticsId = environment.getProperty("app.site.google-analytics-id", "");
        this.socialImagePath = environment.getProperty("app.site.social-image-path", "/og-card.svg");
    }

    public SeoMetadata home(String title, String description) {
        String canonicalUrl = absolute("/");
        return new SeoMetadata(
                canonicalUrl,
                List.of(),
                List.of(webSiteJson(canonicalUrl, title, description)),
                "website",
                socialImageUrl(),
                googleSiteVerification,
                googleAnalyticsId
        );
    }

    public SeoMetadata family(String familyKey, PseoFamilyView familyView) {
        String canonicalUrl = absolute("/well-water/family/" + familyKey);
        List<SeoBreadcrumb> breadcrumbs = List.of(
                new SeoBreadcrumb("Home", absolute("/")),
                new SeoBreadcrumb(familyView.heroTitle(), canonicalUrl)
        );
        return new SeoMetadata(
                canonicalUrl,
                breadcrumbs,
                List.of(
                        collectionPageJson(canonicalUrl, familyView.heroTitle(), familyView.heroLead()),
                        breadcrumbJson(breadcrumbs)
                ),
                "website",
                socialImageUrl(),
                googleSiteVerification,
                googleAnalyticsId
        );
    }

    public SeoMetadata trustHub(String title, String description) {
        String canonicalUrl = absolute("/trust");
        List<SeoBreadcrumb> breadcrumbs = List.of(
                new SeoBreadcrumb("Home", absolute("/")),
                new SeoBreadcrumb("Trust", canonicalUrl)
        );
        return new SeoMetadata(
                canonicalUrl,
                breadcrumbs,
                List.of(
                        collectionPageJson(canonicalUrl, title, description),
                        breadcrumbJson(breadcrumbs)
                ),
                "website",
                socialImageUrl(),
                googleSiteVerification,
                googleAnalyticsId
        );
    }

    public SeoMetadata trustPage(TrustPage page) {
        String canonicalUrl = absolute("/trust/" + page.slug());
        List<SeoBreadcrumb> breadcrumbs = List.of(
                new SeoBreadcrumb("Home", absolute("/")),
                new SeoBreadcrumb("Trust", absolute("/trust")),
                new SeoBreadcrumb(page.h1(), canonicalUrl)
        );
        return new SeoMetadata(
                canonicalUrl,
                breadcrumbs,
                List.of(
                        staticPageJson("AboutPage", canonicalUrl, page.h1(), page.metaDescription(), page.updatedAt()),
                        breadcrumbJson(breadcrumbs)
                ),
                "article",
                socialImageUrl(),
                googleSiteVerification,
                googleAnalyticsId
        );
    }

    public SeoMetadata detail(PseoDetailView pageView) {
        String canonicalUrl = absolute("/well-water/" + pageView.page().slug());
        List<SeoBreadcrumb> breadcrumbs = List.of(
                new SeoBreadcrumb("Home", absolute("/")),
                new SeoBreadcrumb(familyLabel(pageView.page().family()), absolute("/well-water/family/" + pageView.page().family())),
                new SeoBreadcrumb(pageView.page().h1(), canonicalUrl)
        );

        List<String> blocks = new ArrayList<>();
        blocks.add(pageJson(pageView, canonicalUrl));
        if (pageView.decisionDoc() != null && !pageView.decisionDoc().faqs().isEmpty()) {
            blocks.add(faqJson(pageView.decisionDoc().faqs()));
        }
        blocks.add(breadcrumbJson(breadcrumbs));

        return new SeoMetadata(
                canonicalUrl,
                breadcrumbs,
                List.copyOf(blocks),
                pageView.page().family().equals("authority") || pageView.page().family().equals("regional") ? "article" : "website",
                socialImageUrl(),
                googleSiteVerification,
                googleAnalyticsId
        );
    }

    private String socialImageUrl() {
        return absolute(socialImagePath);
    }

    public String absolute(String path) {
        if (path == null || path.isBlank()) {
            return baseUrl;
        }
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        if (path.startsWith("/")) {
            return baseUrl + path;
        }
        return baseUrl + "/" + path;
    }

    private String webSiteJson(String canonicalUrl, String title, String description) {
        return object(Map.of(
                "@context", "https://schema.org",
                "@type", "WebSite",
                "url", canonicalUrl,
                "name", title,
                "description", description
        ));
    }

    private String collectionPageJson(String canonicalUrl, String title, String description) {
        return object(Map.of(
                "@context", "https://schema.org",
                "@type", "CollectionPage",
                "url", canonicalUrl,
                "name", title,
                "description", description
        ));
    }

    private String staticPageJson(String pageType, String canonicalUrl, String title, String description) {
        return staticPageJson(pageType, canonicalUrl, title, description, "");
    }

    private String staticPageJson(String pageType, String canonicalUrl, String title, String description, String updatedAt) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", pageType);
        payload.put("url", canonicalUrl);
        payload.put("name", title);
        payload.put("headline", title);
        payload.put("description", description);
        payload.put("inLanguage", "en");
        payload.put("author", editorialAuthor());
        payload.put("publisher", publisher());
        payload.put("dateModified", updatedAt == null ? "" : updatedAt);
        payload.put("isPartOf", Map.of(
                "@type", "WebSite",
                "name", "Private Well Water Guide",
                "url", absolute("/")
        ));
        return object(payload);
    }

    private String pageJson(PseoDetailView pageView, String canonicalUrl) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", pageType(pageView));
        payload.put("url", canonicalUrl);
        payload.put("headline", pageView.page().h1());
        payload.put("name", pageView.page().title());
        payload.put("description", pageView.page().metaDescription());
        payload.put("about", pageView.riskLabel());
        payload.put("inLanguage", "en");
        payload.put("author", editorialAuthor());
        payload.put("publisher", publisher());
        payload.put("dateModified", pageView.page().fetchedAt());
        payload.put("isPartOf", Map.of(
                "@type", "WebSite",
                "name", "Private Well Water Guide",
                "url", absolute("/")
        ));
        return object(payload);
    }

    private String breadcrumbJson(List<SeoBreadcrumb> breadcrumbs) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < breadcrumbs.size(); i++) {
            SeoBreadcrumb breadcrumb = breadcrumbs.get(i);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("@type", "ListItem");
            item.put("position", i + 1);
            item.put("name", breadcrumb.label());
            item.put("item", breadcrumb.absoluteUrl());
            items.add(object(item));
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("@context", "https://schema.org");
        payload.put("@type", "BreadcrumbList");
        payload.put("itemListElement", array(items));
        return object(payload);
    }

    private String faqJson(List<PseoFaqItem> faqs) {
        List<Map<String, Object>> questions = new ArrayList<>();
        for (PseoFaqItem faq : faqs) {
            questions.add(Map.of(
                    "@type", "Question",
                    "name", faq.question(),
                    "acceptedAnswer", Map.of(
                            "@type", "Answer",
                            "text", faq.answer()
                    )
            ));
        }

        return object(Map.of(
                "@context", "https://schema.org",
                "@type", "FAQPage",
                "mainEntity", questions
        ));
    }

    private String pageType(PseoDetailView pageView) {
        return switch (pageView.page().family()) {
            case "authority" -> "Article";
            case "regional" -> "Article";
            default -> "WebPage";
        };
    }

    private Map<String, Object> editorialAuthor() {
        return Map.of(
                "@type", "Organization",
                "name", "Private Well Editorial Desk",
                "url", absolute("/trust/reviewers-and-expertise")
        );
    }

    private Map<String, Object> publisher() {
        return Map.of(
                "@type", "Organization",
                "name", "Private Well Water Guide",
                "url", absolute("/"),
                "sameAs", List.of(
                        absolute("/trust/methodology"),
                        absolute("/trust/review-policy"),
                        absolute("/trust/reviewers-and-expertise")
                )
        );
    }

    private String familyLabel(String family) {
        return switch (family) {
            case "contaminants" -> "Contaminants";
            case "symptoms" -> "Symptoms";
            case "compares" -> "Comparisons";
            case "triggers" -> "Triggers";
            case "regional" -> "Regional";
            case "authority" -> "Articles";
            default -> family == null ? "" : family;
        };
    }

    private String object(Map<String, ?> values) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        boolean first = true;
        for (Map.Entry<String, ?> entry : values.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            json.append(quote(entry.getKey())).append(":").append(value(entry.getValue()));
        }
        json.append("}");
        return json.toString();
    }

    private String array(List<String> rawJsonValues) {
        return "[" + String.join(",", rawJsonValues) + "]";
    }

    @SuppressWarnings("unchecked")
    private String value(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map<?, ?> map) {
            return object((Map<String, ?>) map);
        }
        if (value instanceof List<?> list) {
            List<String> values = new ArrayList<>();
            for (Object item : list) {
                values.add(value(item));
            }
            return "[" + String.join(",", values) + "]";
        }
        String stringValue = value.toString();
        if (stringValue.startsWith("{") || stringValue.startsWith("[")) {
            return stringValue;
        }
        return quote(stringValue);
    }

    private String quote(String value) {
        StringBuilder escaped = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (ch < 0x20) {
                        escaped.append(String.format(Locale.ROOT, "\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
                }
            }
        }
        escaped.append("\"");
        return escaped.toString();
    }

    private String normalizeBaseUrl(String baseUrl, boolean production) {
        if (baseUrl == null || baseUrl.isBlank()) {
            if (production) {
                throw new IllegalStateException("app.site.base-url must be set outside localhost in production.");
            }
            return "http://localhost:8080";
        }
        String normalized = baseUrl.trim();
        if (normalized.endsWith("/")) {
            return normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private boolean isProduction(Environment environment) {
        for (String profile : environment.getActiveProfiles()) {
            if ("prod".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }
}
