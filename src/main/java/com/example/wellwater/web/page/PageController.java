package com.example.wellwater.web.page;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.lead.LeadCaptureContext;
import com.example.wellwater.pseo.PseoCatalogService;
import com.example.wellwater.pseo.PseoExperienceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class PageController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    private final PseoCatalogService pseoCatalogService;
    private final PseoExperienceService pseoExperienceService;
    private final SeoMetadataService seoMetadataService;
    private final TrustPageService trustPageService;
    private final AnalyticsEventService analyticsEventService;

    public PageController(
            PseoCatalogService pseoCatalogService,
            PseoExperienceService pseoExperienceService,
            SeoMetadataService seoMetadataService,
            TrustPageService trustPageService,
            AnalyticsEventService analyticsEventService
    ) {
        this.pseoCatalogService = pseoCatalogService;
        this.pseoExperienceService = pseoExperienceService;
        this.seoMetadataService = seoMetadataService;
        this.trustPageService = trustPageService;
        this.analyticsEventService = analyticsEventService;
    }

    @GetMapping("/")
    public String home(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model
    ) {
        trackPublicPageView("home", "", "", "/", "indexable");
        model.addAttribute("familyCounts", pseoCatalogService.familyCounts());
        model.addAttribute("totalPageCount", pseoCatalogService.allPages().size());
        model.addAttribute("priorityPages", pseoExperienceService.priorityPages(16));
        model.addAttribute("featuredRegionalPages", pseoExperienceService.featuredRegionalPages());
        model.addAttribute("trustPages", trustPageService.allPages());
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", new LeadCaptureContext(
                "Not ready to enter lab data yet?",
                "Leave an email and a short note if you only have a smell, stain, recent change, or a general well-water concern.",
                "Request follow-up",
                "/",
                "home",
                "home",
                "",
                "Water Verdict home"
        ));
        model.addAttribute("seo", seoMetadataService.home(
                "Water Verdict | Results, Symptoms, and Next Steps",
                "Use test results, water symptoms, or recent changes to decide what to verify next for a private well."
        ));
        return "pages/home";
    }

    @GetMapping("/well-water/family/{family}")
    public String family(
            @PathVariable String family,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model,
            HttpServletResponse response
    ) {
        var pages = pseoCatalogService.byFamily(family);
        if (pages.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/well-water/family/" + family);
            return "pages/not-found";
        }
        trackPublicPageView("family", family, "", "/well-water/family/" + family, "indexable");
        model.addAttribute("family", family);
        model.addAttribute("pages", pages);
        var familyView = pseoExperienceService.familyView(family, pages);
        model.addAttribute("familyView", familyView);
        model.addAttribute("seo", seoMetadataService.family(family, familyView));
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", null);
        if ("regional".equals(family) || "authority".equals(family)) {
            model.addAttribute("leadContext", new LeadCaptureContext(
                    "Want follow-up on this " + family + " cluster?",
                    "Leave an email and the page you were reading. This closes the gap between organic research and a direct follow-up queue.",
                    "Request follow-up",
                    "/well-water/family/" + family,
                    "pseo-family",
                    family,
                    family,
                    familyView.heroTitle()
            ));
        }
        return "pages/pseo/list";
    }

    @GetMapping("/well-water/{slug}")
    public String detail(
            @PathVariable String slug,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model,
            HttpServletResponse response
    ) {
        if (!pseoCatalogService.isCanonicalSlug(slug)) {
            return "redirect:/well-water/" + pseoCatalogService.canonicalSlug(slug);
        }
        var maybePageView = pseoExperienceService.detailView(slug);
        if (maybePageView.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/well-water/" + slug);
            return "pages/not-found";
        }
        trackPublicPageView(
                "detail",
                maybePageView.get().page().slug(),
                maybePageView.get().page().tier(),
                "/well-water/" + maybePageView.get().page().slug(),
                maybePageView.get().page().family()
        );
        model.addAttribute("pageView", maybePageView.get());
        model.addAttribute("page", maybePageView.get().page());
        model.addAttribute("seo", seoMetadataService.detail(maybePageView.get()));
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", new LeadCaptureContext(
                maybePageView.get().page().family().equals("regional")
                        ? "Need a state-aware follow-up?"
                        : (maybePageView.get().page().family().equals("authority")
                        ? "Want this method applied to your own well?"
                        : "Want a private-well follow-up on this issue?"),
                maybePageView.get().page().family().equals("regional")
                        ? "Leave an email and your state context. This page already knows the cluster you came in on."
                        : "Leave an email and a short note so this page can become a direct lead path instead of dead-end research.",
                "Request follow-up",
                "/well-water/" + slug,
                "pseo-detail",
                maybePageView.get().page().family(),
                maybePageView.get().page().slug(),
                maybePageView.get().page().h1()
        ));
        return "pages/pseo/detail";
    }

    @GetMapping("/trust")
    public String trustHub(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model
    ) {
        trackPublicPageView("trust-hub", "trust", "", "/trust", "indexable");
        model.addAttribute("trustPages", trustPageService.allPages());
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", new LeadCaptureContext(
                "Need help before you use the tool?",
                "Leave an email if you have a well-water concern but are not ready to enter results. This keeps trust reading from becoming a dead end.",
                "Request follow-up",
                "/trust",
                "trust-hub",
                "trust",
                "trust",
                "Trust hub"
        ));
        model.addAttribute("seo", seoMetadataService.trustHub(
                "Trust And Method | Water Verdict",
                "Read the methodology, review policy, sources policy, and safety limits behind this private-well decision surface."
        ));
        return "pages/trust/list";
    }

    @GetMapping("/trust/{slug}")
    public String trustPage(
            @PathVariable String slug,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model,
            HttpServletResponse response
    ) {
        var maybePage = trustPageService.findBySlug(slug);
        if (maybePage.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/trust/" + slug);
            return "pages/not-found";
        }
        trackPublicPageView("trust-page", maybePage.get().slug(), "", "/trust/" + maybePage.get().slug(), "indexable");
        model.addAttribute("page", maybePage.get());
        model.addAttribute("trustPages", trustPageService.allPages());
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", new LeadCaptureContext(
                "Want a follow-up before you enter results?",
                "Leave an email and a short note if this trust page matches your situation but you are still in the suspicion or scoping phase.",
                "Request follow-up",
                "/trust/" + maybePage.get().slug(),
                "trust-page",
                "trust",
                maybePage.get().slug(),
                maybePage.get().h1()
        ));
        model.addAttribute("seo", seoMetadataService.trustPage(maybePage.get()));
        return "pages/trust/view";
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        return """
                User-agent: *
                Allow: /
                Disallow: /admin
                Disallow: /lead/
                Disallow: /result/
                Disallow: /tool/

                Sitemap: %s/sitemap.xml
                """.formatted(baseUrl);
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        return pseoCatalogService.sitemapXml(baseUrl, trustPageService.sitemapPaths());
    }

    private String sanitizeLeadStatus(String lead) {
        if ("success".equalsIgnoreCase(lead)) {
            return "success";
        }
        if ("invalid".equalsIgnoreCase(lead)) {
            return "invalid";
        }
        return "";
    }

    private void trackPublicPageView(String entryMode, String slug, String tier, String targetUrl, String note) {
        try {
            analyticsEventService.logEvent(
                    "public_page_view",
                    entryMode,
                    null,
                    slug,
                    tier,
                    null,
                    null,
                    targetUrl,
                    note
            );
        } catch (RuntimeException e) {
            log.warn("Skipping public_page_view analytics for {} because event logging failed", targetUrl, e);
        }
    }
}
