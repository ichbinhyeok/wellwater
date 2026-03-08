package com.example.wellwater.decision.model;

import java.util.List;

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
        List<String> existingTreatments,
        List<String> supportingSignals,
        List<DecisionReportLine> companionReportLines,
        String symptomFlag,
        String triggerFlag,
        String labName,
        String householdSize,
        String smellType,
        String stainType,
        String tasteType,
        String locationScope,
        String changeTiming,
        boolean infantPresent,
        boolean pregnancyPresent,
        boolean immunocompromisedPresent,
        String slugHint
) {
    public DecisionInput {
        existingTreatments = existingTreatments == null ? List.of() : List.copyOf(existingTreatments);
        supportingSignals = supportingSignals == null ? List.of() : List.copyOf(supportingSignals);
        companionReportLines = companionReportLines == null ? List.of() : List.copyOf(companionReportLines);
    }

    public DecisionInput(
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
            List<String> existingTreatments,
            List<String> supportingSignals,
            String symptomFlag,
            String triggerFlag,
            String labName,
            String householdSize,
            String smellType,
            String stainType,
            String tasteType,
            String locationScope,
            String changeTiming,
            boolean infantPresent,
            boolean pregnancyPresent,
            boolean immunocompromisedPresent,
            String slugHint
    ) {
        this(
                entryMode,
                analyteName,
                resultValue,
                unit,
                qualifier,
                qualifierValue,
                sampleDate,
                sampleSource,
                labCertified,
                state,
                useScope,
                existingTreatment,
                existingTreatments,
                supportingSignals,
                List.of(),
                symptomFlag,
                triggerFlag,
                labName,
                householdSize,
                smellType,
                stainType,
                tasteType,
                locationScope,
                changeTiming,
                infantPresent,
                pregnancyPresent,
                immunocompromisedPresent,
                slugHint
        );
    }

    public DecisionInput(
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
            List<String> existingTreatments,
            String symptomFlag,
            String triggerFlag,
            String labName,
            String householdSize,
            String smellType,
            String stainType,
            String tasteType,
            String locationScope,
            String changeTiming,
            boolean infantPresent,
            boolean pregnancyPresent,
            boolean immunocompromisedPresent,
            String slugHint
    ) {
        this(
                entryMode,
                analyteName,
                resultValue,
                unit,
                qualifier,
                qualifierValue,
                sampleDate,
                sampleSource,
                labCertified,
                state,
                useScope,
                existingTreatment,
                existingTreatments,
                List.of(),
                List.of(),
                symptomFlag,
                triggerFlag,
                labName,
                householdSize,
                smellType,
                stainType,
                tasteType,
                locationScope,
                changeTiming,
                infantPresent,
                pregnancyPresent,
                immunocompromisedPresent,
                slugHint
        );
    }

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
        String primary = normalize(existingTreatment);
        if (!primary.isBlank()) {
            return primary;
        }
        return normalizedExistingTreatments().stream().findFirst().orElse("");
    }

    public List<String> normalizedExistingTreatments() {
        return normalizeList(existingTreatments);
    }

    public List<String> normalizedSupportingSignals() {
        return normalizeList(supportingSignals);
    }

    public List<DecisionReportLine> companionReportLines() {
        return companionReportLines;
    }

    public String normalizedLabName() {
        return normalize(labName);
    }

    public String normalizedHouseholdSize() {
        return normalize(householdSize);
    }

    public String normalizedSmellType() {
        return normalize(smellType);
    }

    public String normalizedStainType() {
        return normalize(stainType);
    }

    public String normalizedTasteType() {
        return normalize(tasteType);
    }

    public String normalizedLocationScope() {
        return normalize(locationScope);
    }

    public String normalizedChangeTiming() {
        return normalize(changeTiming);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    private List<String> normalizeList(List<String> values) {
        return values.stream()
                .map(this::normalize)
                .filter(value -> !value.isBlank())
                .toList();
    }
}
