package com.example.wellwater.decision.model;

public record DecisionInput(
        EntryMode entryMode,
        String analyteName,
        String resultValue,
        String unit,
        String sampleSource,
        String labCertified,
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

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }
}

