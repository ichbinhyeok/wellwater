package com.example.wellwater.nj;

public record NjRiskSignal(
        String code,
        String label,
        String observedRange,
        int upperPercent,
        boolean healthRelated
) {
}
