package com.example.wellwater.welltest;

import com.example.wellwater.decision.DecisionEngineService;
import com.example.wellwater.decision.normalize.DecisionInputNormalizationService;
import com.example.wellwater.decision.registry.CostRegistryService;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WellTestPlanServiceTest {

    private final WellTestPlanService service = service(
            "https://mytapscore.com/products/essential-well-water-test?ref=waterverdict",
            "https://mytapscore.com/products/advanced-well-water-test?ref=waterverdict"
    );

    @Test
    void annualBaselineRoutesToEssentialKitWhenConfigured() {
        WellTestPlanResult result = service.create(request("annual", List.of("no_obvious_issue"), List.of(), "NH"), "chatgpt");

        assertEquals("routine", result.urgency());
        assertEquals("baseline", result.resultFamily());
        assertTrue(result.hasPartnerOffer());
        assertEquals("essential", result.partnerOffer().productCode());
        assertEquals("https://waterverdict.com/partner/tap-score/essential?source=chatgpt", result.partnerOffer().url());
        assertTrue(result.recommendedPanel().stream().anyMatch(item -> item.name().contains("coliform")));
        assertEquals("NH private-well guidance", result.officialGuidance().label());
    }

    @Test
    void agriculturalContextExpandsPanelAndRoutesToAdvancedKit() {
        WellTestPlanResult result = service.create(request("annual", List.of(), List.of("agriculture"), "IA"), "web");

        assertEquals("prompt", result.urgency());
        assertEquals("expanded", result.resultFamily());
        assertEquals("advanced", result.partnerOffer().productCode());
        assertTrue(result.recommendedPanel().stream().anyMatch(item -> item.name().contains("Pesticide")));
    }

    @Test
    void floodSuppressesCommerceAndRequiresImmediateCertifiedTesting() {
        WellTestPlanResult result = service.create(request("after_flood", List.of("cloudy_water"), List.of(), "PA"), "chatgpt");

        assertEquals("immediate", result.urgency());
        assertEquals("certified_urgent", result.resultFamily());
        assertFalse(result.hasPartnerOffer());
        assertTrue(result.verdict().contains("alternate drinking-water source"));
        assertTrue(result.recommendedPanel().stream().anyMatch(item -> item.name().equals("Turbidity")));
    }

    @Test
    void oregonHomePurchaseUsesTransactionPanelWithoutAffiliateOffer() {
        WellTestPlanResult result = service.create(request("home_purchase", List.of(), List.of(), "OR"), "chatgpt");

        assertEquals("transaction", result.resultFamily());
        assertFalse(result.hasPartnerOffer());
        assertTrue(result.verdict().contains("before closing"));
        assertTrue(result.recommendedPanel().stream().anyMatch(item -> item.name().equals("Arsenic")));
        assertTrue(result.recommendedPanel().stream().anyMatch(item -> item.name().contains("transaction")));
    }

    @Test
    void specializedPfasRiskNeverRoutesToGenericKit() {
        WellTestPlanResult result = service.create(request("annual", List.of(), List.of("pfas_source"), "MI"), "chatgpt");

        assertEquals("certified_urgent", result.resultFamily());
        assertFalse(result.hasPartnerOffer());
        assertTrue(result.recommendedPanel().stream().anyMatch(item -> item.name().equals("PFAS")));
    }

    @Test
    void unsupportedInputIsRejectedInsteadOfSilentlyBroadened() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create(request("annual", List.of("medical_history"), List.of(), "NH"), "chatgpt"));
        assertThrows(IllegalArgumentException.class,
                () -> service.create(request("annual", List.of(), List.of(), "New Hampshire"), "chatgpt"));
    }

    @Test
    void unsafeAffiliateHostDisablesCommerce() {
        WellTestPlanResult result = service(
                "https://example.com/not-allowed",
                "https://example.com/not-allowed"
        ).create(request("annual", List.of(), List.of(), "US"), "chatgpt");

        assertFalse(result.hasPartnerOffer());
    }

    private WellTestPlanRequest request(String reason, List<String> signals, List<String> risks, String state) {
        return new WellTestPlanRequest(reason, signals, risks, state, "none", "drinking_only");
    }

    private WellTestPlanService service(String essentialUrl, String advancedUrl) {
        DecisionRegistryService registry = new DecisionRegistryService(
                "data/registry/contaminant_registry.csv",
                "data/registry/symptom_registry.csv",
                "data/registry/trigger_registry.csv"
        );
        StateResourceRegistryService states = new StateResourceRegistryService("data/registry/state_resource_registry.csv");
        DecisionEngineService engine = new DecisionEngineService(
                registry,
                new DecisionInputNormalizationService(registry),
                new CostRegistryService("data/registry/cost_registry.csv"),
                states
        );
        PartnerCatalogService partners = new PartnerCatalogService(
                "https://waterverdict.com",
                essentialUrl,
                advancedUrl
        );
        return new WellTestPlanService(engine, states, partners);
    }
}
