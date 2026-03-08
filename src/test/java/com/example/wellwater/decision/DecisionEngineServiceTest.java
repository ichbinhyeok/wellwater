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

    @Test
    void phHighRangeRoutesToAmberCorrosionFlow() {
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

        var result = service.decide(input);

        assertEquals(ProblemType.CORROSION, result.problemType());
        assertEquals(Branch.AMBER, result.branch());
        assertEquals(ActionMode.INSPECT_SOURCE, result.actionMode());
        assertTrue(result.thresholdSummary().contains("EPA SMCL"));
        assertTrue(result.scenarios().stream().anyMatch(s -> s.scenarioId().equals("verify-first")));
    }

    @Test
    void leadAboveThresholdRoutesToAmberVerifyFirstFlow() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "lead",
                "20",
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

        assertEquals(ProblemType.CHEMICAL_HEALTH, result.problemType());
        assertEquals(Branch.AMBER, result.branch());
        assertEquals(ActionMode.RETEST, result.actionMode());
        assertTrue(result.thresholdSummary().contains("EPA action level"));
        assertTrue(result.scenarios().stream().anyMatch(s -> s.scenarioId().equals("verify-first")));
    }

    @Test
    void radiumAboveThresholdRoutesToAmberVerifyFirstFlow() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "radium",
                "6",
                "pCi/L",
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

        assertEquals(ProblemType.CHEMICAL_HEALTH, result.problemType());
        assertEquals(Branch.AMBER, result.branch());
        assertEquals(ActionMode.RETEST, result.actionMode());
        assertTrue(result.thresholdSummary().contains("EPA MCL"));
        assertTrue(result.scenarios().stream().anyMatch(s -> s.scenarioId().equals("verify-first")));
    }

    @Test
    void hardnessWithCorrosionSupportSignalsDelaysSoftenerComparePath() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "hardness",
                "18",
                "grains/gal",
                "none",
                "",
                "2026-03-01",
                "raw well",
                "yes",
                "PA",
                "whole-house",
                "",
                List.of(),
                List.of("lead", "ph"),
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

        assertEquals(ProblemType.AESTHETIC_OPERATIONAL, result.problemType());
        assertTrue(result.compareReadinessChecks().stream().anyMatch(check -> check.contains("Do not open the softener path yet")));
        assertTrue(result.ctas().stream().anyMatch(cta -> cta.targetUrl().equals("/well-water/acid-neutralizer-vs-soda-ash")));
        assertTrue(result.ctas().stream().anyMatch(cta -> cta.targetUrl().equals("/well-water/low-ph-copper-corrosion-testing-order")));
        assertTrue(result.hasSoftenerSizingPreview());
        assertTrue(!result.softenerSizingPreview().eligible());
        assertTrue(result.consumerSupportingSignals().contains("Lead"));
        assertTrue(result.consumerSupportingSignals().contains("pH"));
    }

    @Test
    void nitrateWithBacteriaContextPushesSamplingMistakeAuthorityPath() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "nitrate",
                "9",
                "mg/L",
                "none",
                "",
                "2026-03-01",
                "raw well",
                "yes",
                "IA",
                "drinking-only",
                "",
                List.of(),
                List.of("total coliform", "after-heavy-rain"),
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

        assertTrue(result.hasRecommendedTestCards());
        assertTrue(result.recommendedTestCards().stream().anyMatch(test -> test.testName().equals("Broader contamination follow-up")));
        assertTrue(result.recommendedTestCards().stream().anyMatch(test -> "IA certified lab path".equals(test.resourceLabel())));
        assertTrue(result.ctas().stream().anyMatch(cta -> cta.targetUrl().equals("/well-water/private-well-sampling-mistakes-that-break-results")));
    }

    @Test
    void floodTriggerOrdersEventSensitiveTestingFirst() {
        DecisionInput input = new DecisionInput(
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
                "",
                List.of(),
                List.of(),
                List.of(),
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
                ""
        );

        var result = service.decide(input);

        assertTrue(result.hasRecommendedTestOrderNote());
        assertEquals("Post-flood certified bacteria retest", result.recommendedTestCards().get(0).testName());
        assertEquals("Recent untreated follow-up sample", result.recommendedTestCards().get(1).testName());
    }

    @Test
    void homePurchaseTriggerMovesCertifiedPanelAheadOfGenericChecks() {
        DecisionInput input = new DecisionInput(
                EntryMode.TRIGGER_FIRST,
                "",
                "",
                "",
                "none",
                "",
                "",
                "raw well",
                "yes",
                "NJ",
                "both",
                "",
                List.of(),
                List.of(),
                List.of(),
                "",
                "home-purchase-test",
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

        assertTrue(result.hasRecommendedTestOrderNote());
        assertEquals("Home-purchase certified panel", result.recommendedTestCards().get(0).testName());
        assertEquals("Untreated baseline confirmation", result.recommendedTestCards().get(1).testName());
    }

    @Test
    void companionReportLinesCanDriveSupportingSignalRouting() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "hardness",
                "18",
                "grains/gal",
                "none",
                "",
                "2026-03-01",
                "raw well",
                "yes",
                "PA",
                "whole-house",
                "",
                List.of(),
                List.of(),
                List.of(
                        new com.example.wellwater.decision.model.DecisionReportLine("lead", "20", "ppb", "none", ""),
                        new com.example.wellwater.decision.model.DecisionReportLine("ph", "6.0", "su", "none", "")
                ),
                "",
                "",
                "",
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

        assertTrue(result.consumerSupportingSignals().contains("Lead"));
        assertTrue(result.consumerSupportingSignals().contains("pH"));
        assertTrue(result.compareReadinessChecks().stream().anyMatch(check -> check.contains("Do not open the softener path yet")));
    }

    @Test
    void hardnessWithHouseholdSizeBuildsSoftenerSizingPreview() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "hardness",
                "20",
                "grains/gal",
                "none",
                "",
                "2026-03-01",
                "raw well",
                "yes",
                "PA",
                "whole-house",
                "",
                List.of(),
                List.of(),
                "",
                "",
                "",
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

        assertTrue(result.hasSoftenerSizingPreview());
        assertTrue(result.softenerSizingPreview().eligible());
        assertEquals("48k grain class", result.softenerSizingPreview().recommendedClass());
        assertTrue(result.softenerSizingPreview().dailyGrainLoadLabel().contains("6,000"));
    }
}
