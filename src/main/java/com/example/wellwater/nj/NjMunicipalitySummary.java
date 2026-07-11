package com.example.wellwater.nj;

import java.util.List;

public record NjMunicipalitySummary(
        String slug,
        String name,
        String county,
        String municipalityCode,
        List<NjRiskSignal> signals,
        boolean pilot
) {
    public NjMunicipalitySummary {
        signals = signals == null ? List.of() : List.copyOf(signals);
    }
}
