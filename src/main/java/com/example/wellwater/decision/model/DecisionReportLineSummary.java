package com.example.wellwater.decision.model;

public record DecisionReportLineSummary(
        String analyteLabel,
        String observedValueLabel,
        String interpretationNote,
        boolean primary
) {
}
