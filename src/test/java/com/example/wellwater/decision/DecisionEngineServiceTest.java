package com.example.wellwater.decision;

import com.example.wellwater.decision.model.Branch;
import com.example.wellwater.decision.model.Confidence;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.ActionMode;
import com.example.wellwater.decision.model.Tier;
import com.example.wellwater.decision.model.Urgency;
import com.example.wellwater.decision.normalize.DecisionInputNormalizationService;
import com.example.wellwater.decision.registry.CostRegistryService;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecisionEngineServiceTest {

    private final DecisionEngineService service = new DecisionEngineService(
            new DecisionRegistryService(
                    "./data/registry/contaminant_registry.csv",
                    "./data/registry/symptom_registry.csv",
                    "./data/registry/trigger_registry.csv"
            ),
            new DecisionInputNormalizationService(
                    new DecisionRegistryService(
                            "./data/registry/contaminant_registry.csv",
                            "./data/registry/symptom_registry.csv",
                            "./data/registry/trigger_registry.csv"
                    )
            ),
            new CostRegistryService("./data/registry/cost_registry.csv"),
            new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
    );

    @Test
    void nitrateWithInfantRoutesToImmediateRed() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "nitrate",
                "12",
                "mg/L",
                "none",
                "",
                "2026-02-20",
                "raw well",
                "yes",
                "TX",
                "drinking-only",
                "none",
                List.of("none"),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                true,
                false,
                false,
                ""
        );

        var result = service.decide(input);

        assertEquals(Tier.A, result.tier());
        assertEquals(ProblemType.CHEMICAL_HEALTH, result.problemType());
        assertEquals(Urgency.IMMEDIATE, result.urgency());
        assertEquals(Branch.RED, result.branch());
        assertEquals("fresh", result.sampleFreshness());
        assertEquals("certified_lab", result.recommendedScenarioCtaType());
    }

    @Test
    void unknownAnalyteFallsBackToTierCAmber() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "unknown-metal-x",
                "4.3",
                "mg/L",
                "none",
                "",
                "2025-01-01",
                "unknown",
                "unknown",
                "CA",
                "",
                "",
                List.of(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
                false,
                false,
                ""
        );

        var result = service.decide(input);

        assertEquals(Tier.C, result.tier());
        assertEquals(ProblemType.UNSUPPORTED, result.problemType());
        assertEquals(Branch.AMBER, result.branch());
        assertEquals(Confidence.LOW, result.confidence());
        assertEquals("stale", result.sampleFreshness());
        assertEquals("local_guidance", result.recommendedScenarioCtaType());
    }

    @Test
    void symptomWithStrongContextCanReachGreen() {
        DecisionInput input = new DecisionInput(
                EntryMode.SYMPTOM_FIRST,
                "",
                "",
                "",
                "none",
                "",
                "",
                "raw well",
                "yes",
                "PA",
                "whole-house",
                "none",
                List.of("none"),
                "rotten-egg-smell",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
                false,
                false,
                "rotten-egg-smell"
        );

        var result = service.decide(input);

        assertEquals(Tier.A, result.tier());
        assertEquals(ProblemType.AESTHETIC_OPERATIONAL, result.problemType());
        assertEquals(Branch.GREEN, result.branch());
        assertEquals("category_compare", result.recommendedScenarioCtaType());
    }

    @Test
    void derivedSymptomContextStillReachesSupportedSignal() {
        DecisionInput input = new DecisionInput(
                EntryMode.SYMPTOM_FIRST,
                "",
                "",
                "",
                "none",
                "",
                "",
                "raw well",
                "yes",
                "PA",
                "whole-house",
                "",
                List.of(),
                "",
                "",
                "",
                "",
                "",
                "orange",
                "",
                "whole-house",
                "",
                false,
                false,
                false,
                ""
        );

        var result = service.decide(input);

        assertEquals(Tier.A, result.tier());
        assertEquals(ProblemType.AESTHETIC_OPERATIONAL, result.problemType());
        assertEquals(Branch.GREEN, result.branch());
    }

    @Test
    void treatedTapWithMultipleTreatmentsReducesConfidence() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "nitrate",
                "9",
                "mg/L",
                "none",
                "",
                "2026-03-01",
                "treated tap",
                "yes",
                "PA",
                "drinking-only",
                "",
                List.of("ro", "uv"),
                "",
                "",
                "Acme Lab",
                "4",
                "",
                "",
                "",
                "",
                "",
                false,
                false,
                false,
                ""
        );

        var result = service.decide(input);

        assertEquals(Confidence.MEDIUM, result.confidence());
    }

    @Test
    void resultCarriesThresholdAndVersionMetadata() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "arsenic",
                "12",
                "ppb",
                "none",
                "",
                "2026-03-01",
                "raw well",
                "yes",
                "PA",
                "drinking-only",
                "",
                List.of(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
                false,
                false,
                ""
        );

        var result = service.decide(input);

        assertEquals(Branch.AMBER, result.branch());
        assertEquals(ActionMode.RETEST, result.actionMode());
        assertEquals("decision-engine-v0.3.0", result.decisionVersion());
        assertEquals("epa-2026-03-07", result.sourceVersion());
        assertEquals("local_guidance", result.recommendedScenarioCtaType());
        assertTrue(result.hasThresholdSummary());
        assertTrue(result.thresholdSummary().contains("EPA MCL"));
        assertTrue(result.keyReasons().stream().anyMatch(reason -> reason.contains("Registry threshold check")));
        assertTrue(result.dataQualityNotes().stream().anyMatch(note -> note.contains("Decision rules version")));
    }
}
