package com.example.wellwater.decision;

import com.example.wellwater.decision.model.Branch;
import com.example.wellwater.decision.model.Confidence;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Tier;
import com.example.wellwater.decision.model.Urgency;
import com.example.wellwater.decision.normalize.DecisionInputNormalizationService;
import com.example.wellwater.decision.registry.CostRegistryService;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                "rotten-egg-smell",
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
    }
}
