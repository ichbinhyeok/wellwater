package com.example.wellwater.decision.registry;

public record CostProfile(
        String problemType,
        String scope,
        String installRange,
        String maintenanceRange,
        String sourceUrl
) {
}

