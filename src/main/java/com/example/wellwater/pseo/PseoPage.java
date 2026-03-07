package com.example.wellwater.pseo;

public record PseoPage(
        String family,
        String slug,
        String title,
        String h1,
        String metaDescription,
        String intro,
        String actionNow,
        String whatToTest,
        String primaryCtaLabel,
        String primaryCtaUrl,
        String moneyCtaLabel,
        String moneyCtaUrl,
        String disclosure,
        String sourceUrl,
        String searchQuery,
        String searchPerformedAt,
        String fetchedAt
) {
}

