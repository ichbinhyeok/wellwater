package com.example.wellwater.web.page;

import java.util.List;

public record TrustPage(
        String slug,
        String title,
        String h1,
        String metaDescription,
        String eyebrow,
        String lead,
        String operatorNote,
        String updatedAt,
        List<TrustSection> sections
) {
}
