package com.example.wellwater.web.page;

import java.util.List;

public record SeoMetadata(
        String canonicalUrl,
        List<SeoBreadcrumb> breadcrumbs,
        List<String> structuredDataBlocks
) {
}
