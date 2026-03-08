package com.example.wellwater.web.page;

import java.util.List;

public record SeoMetadata(
        String canonicalUrl,
        List<SeoBreadcrumb> breadcrumbs,
        List<String> structuredDataBlocks,
        String openGraphType,
        String socialImageUrl,
        String googleSiteVerification,
        String googleAnalyticsId
) {
}
