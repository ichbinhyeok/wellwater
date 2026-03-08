package com.example.wellwater.decision;

import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.normalize.DecisionInputNormalizationService;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecisionInputNormalizationServiceTest {

    private final DecisionRegistryService registry = new DecisionRegistryService(
            "./data/registry/contaminant_registry.csv",
            "./data/registry/symptom_registry.csv",
            "./data/registry/trigger_registry.csv"
    );

    private final DecisionInputNormalizationService service = new DecisionInputNormalizationService(registry);

    @Test
    void derivesSymptomFromSecondaryContext() {
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

        var normalized = service.normalize(input);

        assertEquals("orange-stains", normalized.symptomKey());
    }

    @Test
    void derivesTriggerFromChangeTiming() {
        DecisionInput input = new DecisionInput(
                EntryMode.TRIGGER_FIRST,
                "",
                "",
                "",
                "none",
                "",
                "",
                "raw well",
                "unknown",
                "PA",
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
                "after-rain",
                false,
                false,
                false,
                ""
        );

        var normalized = service.normalize(input);

        assertEquals("after-heavy-rain", normalized.triggerKey());
    }

    @Test
    void convertsEquivalentUnitsToCanonicalAndChecksThreshold() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "arsenic",
                "10",
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

        var normalized = service.normalize(input);

        assertEquals("ug/L", normalized.canonicalUnit());
        assertEquals(10.0d, normalized.canonicalNumericResultValue());
        assertTrue(normalized.unitSupported());
        assertTrue(normalized.unitConverted());
        assertTrue(normalized.thresholdTriggered());
        assertTrue(normalized.thresholdSummary().contains("EPA MCL"));
    }

    @Test
    void phHighRangeThresholdIsTriggered() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "ph",
                "9.0",
                "su",
                "none",
                "",
                "2026-03-01",
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
                "",
                "",
                "",
                "",
                false,
                false,
                false,
                ""
        );

        var normalized = service.normalize(input);

        assertEquals("su", normalized.canonicalUnit());
        assertEquals(9.0d, normalized.canonicalNumericResultValue());
        assertTrue(normalized.thresholdTriggered());
        assertTrue(normalized.thresholdSummary().contains("EPA SMCL"));
        assertTrue(normalized.thresholdSummary().contains("8.500"));
    }

    @Test
    void microbialUnitsAcceptMpnPer100ml() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "total coliform",
                "12",
                "mpn/100ml",
                "",
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

        var normalized = service.normalize(input);

        assertEquals("presence/absence", normalized.canonicalUnit());
        assertTrue(normalized.unitSupported());
    }
}
