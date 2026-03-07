package com.example.wellwater.decision.normalize;

public record DecisionNormalizedInput(
        String analyteKey,
        String symptomKey,
        String triggerKey,
        String stateKey,
        QualifierType qualifierType,
        Double numericResultValue,
        Double qualifierNumericValue,
        boolean unitSupported,
        SampleFreshness sampleFreshness,
        int completenessScore
) {
}

