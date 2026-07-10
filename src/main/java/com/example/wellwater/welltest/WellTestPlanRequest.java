package com.example.wellwater.welltest;

import java.util.List;

public record WellTestPlanRequest(
        String reason,
        List<String> signals,
        List<String> riskContexts,
        String stateCode,
        String existingTreatment,
        String useScope
) {
    public WellTestPlanRequest {
        signals = signals == null ? List.of() : List.copyOf(signals);
        riskContexts = riskContexts == null ? List.of() : List.copyOf(riskContexts);
    }
}
