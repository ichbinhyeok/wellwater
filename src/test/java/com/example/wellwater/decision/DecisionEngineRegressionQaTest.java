package com.example.wellwater.decision;

import com.example.wellwater.decision.model.Branch;
import com.example.wellwater.decision.model.Confidence;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Scope;
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

class DecisionEngineRegressionQaTest {

    private final DecisionRegistryService registry = new DecisionRegistryService(
            "./data/registry/contaminant_registry.csv",
            "./data/registry/symptom_registry.csv",
            "./data/registry/trigger_registry.csv"
    );

    private final DecisionEngineService service = new DecisionEngineService(
            registry,
            new DecisionInputNormalizationService(registry),
            new CostRegistryService("./data/registry/cost_registry.csv"),
            new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
    );

    @Test
    void qaCase1_nitrateInfantRawFresh() {
        var result = service.decide(new DecisionInput(
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
                "qa-1"
        ));

        assertEquals(Tier.A, result.tier());
        assertEquals(Urgency.IMMEDIATE, result.urgency());
        assertEquals(Branch.RED, result.branch());
    }

    @Test
    void qaCase2_coliformPositiveWithFlood() {
        var result = service.decide(new DecisionInput(
                EntryMode.RESULT_FIRST,
                "total coliform",
                "positive",
                "presence/absence",
                "positive",
                "",
                "2026-03-01",
                "raw well",
                "yes",
                "FL",
                "drinking-only",
                "none",
                List.of("none"),
                "",
                "after-flood",
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
                "qa-2"
        ));

        assertEquals(ProblemType.MICROBIAL, result.problemType());
        assertEquals(Urgency.IMMEDIATE, result.urgency());
        assertEquals(Branch.RED, result.branch());
    }

    @Test
    void qaCase3_ironOrangeStainsWholeHouse() {
        var result = service.decide(new DecisionInput(
                EntryMode.RESULT_FIRST,
                "iron",
                "1.2",
                "mg/L",
                "none",
                "",
                "2026-02-15",
                "raw well",
                "yes",
                "PA",
                "whole-house",
                "none",
                List.of("none"),
                "orange-stains",
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
                "qa-3"
        ));

        assertEquals(ProblemType.AESTHETIC_OPERATIONAL, result.problemType());
        assertEquals(Scope.WHOLE_HOUSE, result.scope());
        assertEquals(Branch.GREEN, result.branch());
    }

    @Test
    void qaCase4_lowPhCorrosionUnknownLeadStatus() {
        var result = service.decide(new DecisionInput(
                EntryMode.RESULT_FIRST,
                "ph",
                "6.0",
                "su",
                "unknown",
                "",
                "2025-10-01",
                "unknown",
                "unknown",
                "MA",
                "whole-house",
                "none",
                List.of("none"),
                "metallic-taste",
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
                "qa-4"
        ));

        assertEquals(ProblemType.CORROSION, result.problemType());
        assertEquals(Branch.AMBER, result.branch());
    }

    @Test
    void qaCase5_pfasUnknownLabStaleSample() {
        var result = service.decide(new DecisionInput(
                EntryMode.RESULT_FIRST,
                "pfas",
                "28",
                "ppt",
                "none",
                "",
                "2024-10-01",
                "raw well",
                "unknown",
                "NY",
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
                false,
                false,
                false,
                "qa-5"
        ));

        assertEquals(Tier.C, result.tier());
        assertEquals(Confidence.LOW, result.confidence());
        assertEquals(Branch.RED, result.branch());
    }

    @Test
    void qaCase6_treatedTapExistingRoConcernPersists() {
        var result = service.decide(new DecisionInput(
                EntryMode.SYMPTOM_FIRST,
                "",
                "",
                "",
                "none",
                "",
                "",
                "treated tap",
                "unknown",
                "CA",
                "drinking-only",
                "ro",
                List.of("ro"),
                "cloudy-water",
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
                "qa-6"
        ));

        assertTrue(result.confidence() == Confidence.MEDIUM || result.confidence() == Confidence.LOW);
        assertTrue(result.scenarios().stream().anyMatch(s -> s.scenarioId().equals("verify-first")));
    }
}
