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
        List<String> recommendedTests,
        List<RecommendedTestCard> recommendedTestCards,
        String recommendedTestOrderNote,
        List<String> compareReadinessChecks,
        List<DecisionReportLineSummary> reportLinesReviewed,
        List<String> supportingSignalsUsed,
        List<ScenarioOption> scenarios,
        SoftenerSizingPreview softenerSizingPreview,
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
    public DecisionResult {
        keyReasons = safeList(keyReasons);
        dataQualityNotes = safeList(dataQualityNotes);
        todayActions = safeList(todayActions);
        thisWeekActions = safeList(thisWeekActions);
        laterActions = safeList(laterActions);
        recommendedTests = safeList(recommendedTests);
        recommendedTestCards = recommendedTestCards == null ? List.of() : List.copyOf(recommendedTestCards);
        recommendedTestOrderNote = recommendedTestOrderNote == null ? "" : recommendedTestOrderNote;
        compareReadinessChecks = safeList(compareReadinessChecks);
        reportLinesReviewed = reportLinesReviewed == null ? List.of() : List.copyOf(reportLinesReviewed);
        supportingSignalsUsed = safeList(supportingSignalsUsed);
        scenarios = scenarios == null ? List.of() : List.copyOf(scenarios);
        assumptions = safeList(assumptions);
        sourcesUsed = safeList(sourcesUsed);
        ctas = ctas == null ? List.of() : List.copyOf(ctas);
    }

    public DecisionResult(
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
            List<String> recommendedTests,
            List<String> compareReadinessChecks,
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
        this(
                entryMode,
                tier,
                confidence,
                branch,
                urgency,
                scope,
                problemType,
                actionMode,
                primaryVerdictLabel,
                primaryVerdictSentence,
                keyReasons,
                dataQualityNotes,
                todayActions,
                thisWeekActions,
                laterActions,
                recommendedTests,
                List.of(),
                "",
                compareReadinessChecks,
                List.of(),
                List.of(),
                scenarios,
                null,
                sampleFreshness,
                completenessScore,
                costNote,
                installRange,
                maintenanceRange,
                localGuidanceUrl,
                certifiedLabUrl,
                decisionVersion,
                sourceVersion,
                thresholdSummary,
                assumptions,
                sourcesUsed,
                disclosureText,
                localGuidanceNeeded,
                ctas
        );
    }

    public boolean hasThresholdSummary() {
        return thresholdSummary != null && !thresholdSummary.isBlank();
    }

    public boolean hasRecommendedTestCards() {
        return recommendedTestCards != null && !recommendedTestCards.isEmpty();
    }

    public boolean hasRecommendedTestOrderNote() {
        return recommendedTestOrderNote != null && !recommendedTestOrderNote.isBlank();
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

    public String browserTitle() {
        return primaryVerdictLabel + " | " + primaryTopicLabel() + " | Water Verdict";
    }

    public String primaryTopicLabel() {
        return primaryReportAnalyteLabel()
                .orElseGet(this::fallbackTopicLabel);
    }

    public String resultIntroSentence() {
        return switch (branch) {
            case RED -> "Start with safety steps and certified testing before you compare equipment.";
            case AMBER -> "Use the next steps below to reduce uncertainty before you choose treatment.";
            case GREEN -> "Use the next steps below to compare the right category without skipping verification.";
        };
    }

    public String mobileAnalysisSummary() {
        return switch (branch) {
            case RED -> "Treat this as a verification-first safety path. Keep purchases secondary until the test trail is clean.";
            case AMBER -> "This is still a data-first path. Tighten the testing plan before you choose a treatment class.";
            case GREEN -> "This looks compare-ready, but sampling fit and claim checks still come before buying.";
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

    public List<String> consumerSupportingSignals() {
        if (supportingSignalsUsed == null || supportingSignalsUsed.isEmpty()) {
            return List.of();
        }
        return supportingSignalsUsed.stream()
                .map(this::humanizeSignal)
                .toList();
    }

    public boolean hasReportLinesReviewed() {
        return reportLinesReviewed != null && !reportLinesReviewed.isEmpty();
    }

    public boolean hasSoftenerSizingPreview() {
        return softenerSizingPreview != null;
    }

    private boolean hasScenarioCtaType(String ctaType) {
        return scenarios.stream().anyMatch(scenario -> scenario.ctaType().equals(ctaType));
    }

    private static List<String> safeList(List<String> values) {
        return values == null ? List.of() : List.copyOf(values);
    }

    private java.util.Optional<String> primaryReportAnalyteLabel() {
        if (!hasReportLinesReviewed()) {
            return java.util.Optional.empty();
        }
        return reportLinesReviewed.stream()
                .filter(DecisionReportLineSummary::primary)
                .map(DecisionReportLineSummary::analyteLabel)
                .filter(label -> label != null && !label.isBlank())
                .filter(label -> !"Unknown line".equalsIgnoreCase(label))
                .findFirst();
    }

    private String fallbackTopicLabel() {
        return switch (entryMode) {
            case RESULT_FIRST -> switch (problemType) {
                case MICROBIAL -> "Microbial water result";
                case CHEMICAL_HEALTH -> "Health-focused water result";
                case AESTHETIC_OPERATIONAL -> "Nuisance water result";
                case CORROSION -> "Corrosion-focused water result";
                case UNSUPPORTED -> "Well water result";
            };
            case SYMPTOM_FIRST -> "Symptom-driven water result";
            case TRIGGER_FIRST -> "Event-driven water result";
        };
    }

    private String humanizeSignal(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return switch (value) {
            case "e. coli" -> "E. coli";
            case "ph" -> "pH";
            case "total coliform" -> "Total coliform";
            default -> {
                String normalized = value.replace('-', ' ');
                yield Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
            }
        };
    }
}
