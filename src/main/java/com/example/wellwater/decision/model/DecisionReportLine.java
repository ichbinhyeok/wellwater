package com.example.wellwater.decision.model;

public record DecisionReportLine(
        String analyteName,
        String resultValue,
        String unit,
        String qualifier,
        String qualifierValue
) {
    public boolean isBlank() {
        return normalize(analyteName).isBlank()
                && normalize(resultValue).isBlank()
                && normalize(unit).isBlank()
                && normalize(qualifier).isBlank()
                && normalize(qualifierValue).isBlank();
    }

    public String normalizedAnalyteName() {
        return normalize(analyteName);
    }

    public String normalizedResultValue() {
        return normalize(resultValue);
    }

    public String normalizedUnit() {
        return normalize(unit);
    }

    public String normalizedQualifier() {
        return normalize(qualifier);
    }

    public String normalizedQualifierValue() {
        return normalize(qualifierValue);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }
}
