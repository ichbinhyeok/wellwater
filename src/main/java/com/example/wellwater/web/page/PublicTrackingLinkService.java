package com.example.wellwater.web.page;

import com.example.wellwater.pseo.PseoPage;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;

@Service
public class PublicTrackingLinkService {

    public String home(String ctaType, String targetUrl) {
        return trackedHref("home", "", "home", "home", ctaType, targetUrl);
    }

    public String family(String family, String ctaType, String targetUrl) {
        return trackedHref("family", family, "family-hub", family, ctaType, targetUrl);
    }

    public String detail(PseoPage sourcePage, String ctaType, String targetUrl) {
        if (sourcePage == null) {
            return trackedHref("detail", "", "unknown", "unknown", ctaType, targetUrl);
        }
        return trackedHref(
                "detail",
                sourcePage.slug(),
                sourcePage.searchRole().name().toLowerCase(Locale.ROOT),
                sourcePage.family(),
                ctaType,
                targetUrl
        );
    }

    private String trackedHref(
            String entryMode,
            String slug,
            String role,
            String family,
            String ctaType,
            String targetUrl
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/tool/out")
                .queryParam("target", targetUrl)
                .queryParam("ctaType", ctaType)
                .queryParam("entryMode", entryMode)
                .queryParam("sessionId", "");

        if (role != null && !role.isBlank()) {
            builder.queryParam("tier", role);
        }
        if (family != null && !family.isBlank()) {
            builder.queryParam("branch", family);
        }
        if (slug != null && !slug.isBlank()) {
            builder.queryParam("slug", slug);
        }

        return builder.build().encode().toUriString();
    }
}
