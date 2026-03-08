package com.example.wellwater.pseo;

import java.util.Locale;

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
        String fetchedAt,
        String tier
) {
    public String normalizedTier() {
        if (tier == null || tier.isBlank()) {
            return "B";
        }
        return tier.trim().toUpperCase(Locale.ROOT);
    }

    public boolean hasSupportedTier() {
        return switch (normalizedTier()) {
            case "A", "B", "C" -> true;
            default -> false;
        };
    }

    public int tierRank() {
        return switch (normalizedTier()) {
            case "A" -> 0;
            case "B" -> 1;
            default -> 2;
        };
    }
}
