package com.example.wellwater.decision.normalize;

public record DecisionNormalizedInput(
        String analyteKey,
        String symptomKey,
        String triggerKey,
        String stateKey,
        QualifierType qualifierType,
        Double numericResultValue,
        Double qualifierNumericValue,
        String canonicalUnit,
        Double canonicalNumericResultValue,
        boolean unitSupported,
        boolean unitConverted,
        boolean thresholdTriggered,
        String thresholdSummary,
        SampleFreshness sampleFreshness,
        int completenessScore
) {
}

