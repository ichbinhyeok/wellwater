package com.example.wellwater.decision.model;

import java.util.List;

public record SoftenerSizingPreview(
        boolean eligible,
        String headline,
        String summary,
        String recommendedClass,
        String sizingVerdict,
        String hardnessLabel,
        String dailyGrainLoadLabel,
        String regenerationLabel,
        List<String> notes,
        String nextAction
) {
    public boolean hasRecommendedClass() {
        return recommendedClass != null && !recommendedClass.isBlank();
    }
}
