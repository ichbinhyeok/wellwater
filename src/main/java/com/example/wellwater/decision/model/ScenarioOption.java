package com.example.wellwater.decision.model;

import java.util.List;

public record ScenarioOption(
        String scenarioId,
        String scenarioTitle,
        String scenarioType,
        String fitReason,
        String limitations,
        String recommendedScope,
        List<String> claimRequirements,
        String estimatedCostBand,
        String estimatedMaintenanceBand,
        String nextAction,
        String ctaType
) {
}

