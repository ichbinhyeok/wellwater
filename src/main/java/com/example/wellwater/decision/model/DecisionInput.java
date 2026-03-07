package com.example.wellwater.decision.model;

public record DecisionInput(
        EntryMode entryMode,
        String analyteName,
        String resultValue,
        String unit,
        String qualifier,
        String qualifierValue,
        String sampleDate,
        String sampleSource,
        String labCertified,
        String state,
        String useScope,
        String existingTreatment,
        String symptomFlag,
        String triggerFlag,
        boolean infantPresent,
        boolean pregnancyPresent,
        boolean immunocompromisedPresent,
        String slugHint
) {
    public String normalizedAnalyte() {
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

    public String normalizedSampleDate() {
        return normalize(sampleDate);
    }

    public String normalizedSymptom() {
        return normalize(symptomFlag);
    }

    public String normalizedTrigger() {
        return normalize(triggerFlag);
    }

    public String normalizedLabCertified() {
        return normalize(labCertified);
    }

    public String normalizedSampleSource() {
        return normalize(sampleSource);
    }

    public String normalizedState() {
        return normalize(state);
    }

    public String normalizedUseScope() {
        return normalize(useScope);
    }

    public String normalizedExistingTreatment() {
        return normalize(existingTreatment);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }
}
