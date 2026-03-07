package com.example.wellwater.decision.model;

import java.util.List;

public record DecisionResult(
        EntryMode entryMode,
        Tier tier,
        Confidence confidence,
        Branch branch,
        Urgency urgency,
        Scope scope,
        ProblemType problemType,
        ActionMode actionMode,
        String primaryVerdictLabel,
        String primaryVerdictSentence,
        List<String> keyReasons,
        List<String> dataQualityNotes,
        List<String> todayActions,
        List<String> thisWeekActions,
        List<String> laterActions,
        List<ScenarioOption> scenarios,
        String sampleFreshness,
        int completenessScore,
        String costNote,
        String installRange,
        String maintenanceRange,
        String localGuidanceUrl,
        String certifiedLabUrl,
        String decisionVersion,
        String sourceVersion,
        String thresholdSummary,
        List<String> assumptions,
        List<String> sourcesUsed,
        String disclosureText,
        boolean localGuidanceNeeded,
        List<CtaLink> ctas
) {
    public boolean hasThresholdSummary() {
        return thresholdSummary != null && !thresholdSummary.isBlank();
    }

    public String consumerPriorityLabel() {
        return switch (branch) {
            case RED -> "Act now";
            case AMBER -> "Verify first";
            case GREEN -> "Compare options";
        };
    }

    public String consumerSupportLabel() {
        return switch (tier) {
            case A -> "Strong support";
            case B -> "Guided support";
            case C -> "Limited support";
        };
    }

    public String consumerScopeLabel() {
        return switch (scope) {
            case DRINKING_ONLY -> "Drinking only";
            case WHOLE_HOUSE -> "Whole house";
            case BOTH -> "Drinking + whole house";
            case UNCLEAR -> "Scope needs review";
        };
    }

    public String recommendedScenarioCtaType() {
        if (branch == Branch.RED) {
            if ((tier != Tier.A || confidence == Confidence.LOW) && hasScenarioCtaType("local_guidance")) {
                return "local_guidance";
            }
            if (hasScenarioCtaType("certified_lab")) {
                return "certified_lab";
            }
        }
        if (branch == Branch.AMBER) {
            if (hasScenarioCtaType("local_guidance")) {
                return "local_guidance";
            }
            if (hasScenarioCtaType("certified_lab")) {
                return "certified_lab";
            }
        }
        if (hasScenarioCtaType("category_compare")) {
            return "category_compare";
        }
        if (hasScenarioCtaType("certified_lab")) {
            return "certified_lab";
        }
        return scenarios.isEmpty() ? "" : scenarios.get(0).ctaType();
    }

    public boolean isRecommendedScenario(ScenarioOption scenario) {
        return scenario != null && scenario.ctaType().equals(recommendedScenarioCtaType());
    }

    private boolean hasScenarioCtaType(String ctaType) {
        return scenarios.stream().anyMatch(scenario -> scenario.ctaType().equals(ctaType));
    }
}
