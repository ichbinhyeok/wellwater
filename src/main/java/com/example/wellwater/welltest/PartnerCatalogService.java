package com.example.wellwater.welltest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class PartnerCatalogService {

    private static final Set<String> ALLOWED_HOSTS = Set.of("mytapscore.com", "www.mytapscore.com");
    private static final String DISCLOSURE = "Affiliate link: Water Verdict may earn a commission if you buy this physical test kit.";

    private final String siteBaseUrl;
    private final String essentialUrl;
    private final String advancedUrl;

    public PartnerCatalogService(
            @Value("${app.site.base-url:}") String siteBaseUrl,
            @Value("${app.partner.tap-score.essential-url:}") String essentialUrl,
            @Value("${app.partner.tap-score.advanced-url:}") String advancedUrl
    ) {
        this.siteBaseUrl = trim(siteBaseUrl);
        this.essentialUrl = safeTarget(essentialUrl);
        this.advancedUrl = safeTarget(advancedUrl);
    }

    public Optional<PartnerOffer> offer(String productCode, String channel, String fitReason) {
        String normalizedProduct = normalize(productCode);
        if (target(normalizedProduct).isEmpty()) {
            return Optional.empty();
        }
        String productName = switch (normalizedProduct) {
            case "essential" -> "Tap Score Essential Well Water Test";
            case "advanced" -> "Tap Score Advanced Well Water Test";
            default -> "";
        };
        if (productName.isBlank()) {
            return Optional.empty();
        }
        String path = "/partner/tap-score/" + normalizedProduct;
        String redirectUrl = UriComponentsBuilder.fromUriString(siteBaseUrl.isBlank() ? "" : siteBaseUrl)
                .path(path)
                .queryParam("source", safeChannel(channel))
                .build()
                .toUriString();
        return Optional.of(new PartnerOffer(normalizedProduct, productName, fitReason, redirectUrl, DISCLOSURE));
    }

    public Optional<String> target(String productCode) {
        return switch (normalize(productCode)) {
            case "essential" -> optional(essentialUrl);
            case "advanced" -> optional(advancedUrl);
            default -> Optional.empty();
        };
    }

    private String safeTarget(String candidate) {
        String value = trim(candidate);
        if (value.isBlank()) {
            return "";
        }
        try {
            URI uri = URI.create(value);
            if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null) {
                return "";
            }
            return ALLOWED_HOSTS.contains(uri.getHost().toLowerCase(Locale.ROOT)) ? value : "";
        } catch (IllegalArgumentException ignored) {
            return "";
        }
    }

    private Optional<String> optional(String value) {
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    private String safeChannel(String channel) {
        return switch (normalize(channel)) {
            case "chatgpt" -> "chatgpt";
            case "web" -> "web";
            default -> "tool";
        };
    }

    private static String normalize(String value) {
        return trim(value).toLowerCase(Locale.ROOT);
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
