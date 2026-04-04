package com.example.wellwater.pseo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class PseoSearchStrategy {

    private static final Set<String> INDEXABLE_FAMILY_HUBS = Set.of("regional", "authority", "triggers");

    private static final List<String> CORE_SEARCH_SLUG_ORDER = List.of(
            "new-jersey-pwta-private-well-testing",
            "private-well-home-sale-testing-by-state",
            "home-purchase-test",
            "home-sale-private-well-testing-checklist",
            "new-hampshire-arsenic-well-water",
            "new-hampshire-arsenic-testing-order",
            "arsenic",
            "oregon-private-well-testing-recommendations",
            "oregon-private-well-homebuyer-testing",
            "metallic-taste",
            "metallic-taste-plumbing-vs-source-water",
            "ph",
            "low-ph-copper-corrosion-testing-order",
            "how-to-read-a-well-water-lab-report",
            "test-kit-vs-certified-lab",
            "private-well-sampling-mistakes-that-break-results",
            "new-jersey-pwta-vs-full-household-panel",
            "nitrate",
            "nitrate-baby-pregnancy-well-water-checklist",
            "after-flood",
            "coliform"
    );

    private static final Set<String> CORE_SEARCH_SLUGS = Set.copyOf(CORE_SEARCH_SLUG_ORDER);

    private static final Set<String> SUPPORT_SEARCH_SLUGS = Set.of(
            "after-heavy-rain",
            "after-repair",
            "retest-after-treatment",
            "new-baby-at-home",
            "pregnancy-in-home",
            "arsenic-bedrock-testing-checklist",
            "mail-in-lab-vs-local-certified-lab",
            "private-well-testing-schedule-by-household",
            "florida-rotten-egg-smell-well-water",
            "sulfur-smell-hot-water-vs-whole-house",
            "iowa-nitrate-baby-well-water",
            "connecticut-low-ph-blue-green-stains",
            "pennsylvania-private-well-radon",
            "radon-radium-private-well-testing-order",
            "new-york-pfas-private-wells",
            "new-york-pfas-private-well-testing-order",
            "pfas-private-well-filter-claim-checklist",
            "texas-private-well-sampling-chain-of-custody",
            "maine-bedrock-arsenic-private-well",
            "massachusetts-bedrock-arsenic-uranium-well",
            "minnesota-nitrate-private-well",
            "wisconsin-nitrate-pregnancy-well-water",
            "vermont-new-well-arsenic-uranium-testing"
    );

    private static final List<String> FEATURED_REGIONAL_SLUGS = List.of(
            "new-jersey-pwta-private-well-testing",
            "new-hampshire-arsenic-well-water",
            "oregon-private-well-testing-recommendations",
            "connecticut-low-ph-blue-green-stains"
    );

    private PseoSearchStrategy() {
    }

    public static PseoSearchRole roleFor(PseoPage page) {
        if (page == null) {
            return PseoSearchRole.HOLD;
        }
        if (CORE_SEARCH_SLUGS.contains(page.slug())) {
            return PseoSearchRole.CORE;
        }
        if (SUPPORT_SEARCH_SLUGS.contains(page.slug())) {
            return PseoSearchRole.SUPPORT;
        }
        if ("compares".equals(page.family())) {
            return PseoSearchRole.CONVERSION;
        }
        return PseoSearchRole.HOLD;
    }

    public static boolean isIndexable(PseoPage page) {
        return roleFor(page).isIndexable();
    }

    public static boolean isFamilyHubIndexable(String family) {
        return INDEXABLE_FAMILY_HUBS.contains(family);
    }

    public static String robotsForPage(PseoPage page) {
        return isIndexable(page) ? "index,follow" : "noindex,follow";
    }

    public static String robotsForFamily(String family) {
        return isFamilyHubIndexable(family) ? "index,follow" : "noindex,follow";
    }

    public static List<String> homePrioritySlugOrder() {
        return CORE_SEARCH_SLUG_ORDER;
    }

    public static List<String> featuredRegionalSlugOrder() {
        return FEATURED_REGIONAL_SLUGS;
    }

    public static Set<String> featuredRegionalSlugSet() {
        return Set.copyOf(FEATURED_REGIONAL_SLUGS);
    }

    public static List<String> coreAndSupportSlugOrder() {
        LinkedHashSet<String> ordered = new LinkedHashSet<>(CORE_SEARCH_SLUG_ORDER);
        ordered.addAll(SUPPORT_SEARCH_SLUGS);
        return List.copyOf(ordered);
    }
}
