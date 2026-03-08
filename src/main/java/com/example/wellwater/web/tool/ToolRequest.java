package com.example.wellwater.web.tool;

import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.DecisionReportLine;
import com.example.wellwater.decision.model.EntryMode;

import java.util.ArrayList;
import java.util.List;

public class ToolRequest {

    private String entryMode;
    private String analyteName;
    private String resultValue;
    private String unit;
    private String qualifier;
    private String qualifierValue;
    private String sampleDate;
    private String sampleSource;
    private String labCertified;
    private String state;
    private String useScope;
    private String existingTreatment;
    private List<String> existingTreatments = new ArrayList<>();
    private List<String> supportingSignals = new ArrayList<>();
    private List<CompanionReportLineForm> companionLines = new ArrayList<>();
    private String symptomFlag;
    private String triggerFlag;
    private String labName;
    private String householdSize;
    private String smellType;
    private String stainType;
    private String tasteType;
    private String locationScope;
    private String changeTiming;
    private boolean infantPresent;
    private boolean pregnancyPresent;
    private boolean immunocompromisedPresent;
    private String slugHint;

    public DecisionInput toDecisionInput() {
        String primaryExistingTreatment = resolvePrimaryExistingTreatment();
        return new DecisionInput(
                EntryMode.fromWire(entryMode),
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
                primaryExistingTreatment,
                List.copyOf(existingTreatments),
                List.copyOf(supportingSignals),
                buildCompanionReportLines(),
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

    public List<CompanionReportLineForm> getCompanionLinesForView() {
        List<CompanionReportLineForm> forms = sanitizedCompanionLines();
        while (forms.size() < 2) {
            forms.add(new CompanionReportLineForm());
        }
        return forms;
    }

    private String resolvePrimaryExistingTreatment() {
        if (existingTreatment != null && !existingTreatment.isBlank()) {
            return existingTreatment;
        }
        if (!existingTreatments.isEmpty()) {
            return existingTreatments.get(0);
        }
        return "";
    }

    private List<DecisionReportLine> buildCompanionReportLines() {
        return sanitizedCompanionLines().stream()
                .map(CompanionReportLineForm::toDecisionReportLine)
                .filter(line -> !line.isBlank())
                .toList();
    }

    private List<CompanionReportLineForm> sanitizedCompanionLines() {
        List<CompanionReportLineForm> forms = new ArrayList<>();
        for (CompanionReportLineForm line : companionLines) {
            if (line == null) {
                continue;
            }
            forms.add(line);
        }
        return forms;
    }

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    public String getAnalyteName() {
        return analyteName;
    }

    public void setAnalyteName(String analyteName) {
        this.analyteName = analyteName;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getQualifierValue() {
        return qualifierValue;
    }

    public void setQualifierValue(String qualifierValue) {
        this.qualifierValue = qualifierValue;
    }

    public String getSampleDate() {
        return sampleDate;
    }

    public void setSampleDate(String sampleDate) {
        this.sampleDate = sampleDate;
    }

    public String getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(String sampleSource) {
        this.sampleSource = sampleSource;
    }

    public String getLabCertified() {
        return labCertified;
    }

    public void setLabCertified(String labCertified) {
        this.labCertified = labCertified;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUseScope() {
        return useScope;
    }

    public void setUseScope(String useScope) {
        this.useScope = useScope;
    }

    public String getExistingTreatment() {
        return existingTreatment;
    }

    public void setExistingTreatment(String existingTreatment) {
        this.existingTreatment = existingTreatment;
    }

    public List<String> getExistingTreatments() {
        return existingTreatments;
    }

    public void setExistingTreatments(List<String> existingTreatments) {
        this.existingTreatments = existingTreatments == null ? new ArrayList<>() : new ArrayList<>(existingTreatments);
    }

    public boolean hasExistingTreatment(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            return false;
        }
        if (candidate.equalsIgnoreCase(existingTreatment)) {
            return true;
        }
        return existingTreatments.stream().anyMatch(value -> candidate.equalsIgnoreCase(value));
    }

    public List<String> getSupportingSignals() {
        return supportingSignals;
    }

    public void setSupportingSignals(List<String> supportingSignals) {
        this.supportingSignals = supportingSignals == null ? new ArrayList<>() : new ArrayList<>(supportingSignals);
    }

    public boolean hasSupportingSignal(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            return false;
        }
        return supportingSignals.stream().anyMatch(value -> candidate.equalsIgnoreCase(value));
    }

    public List<CompanionReportLineForm> getCompanionLines() {
        return companionLines;
    }

    public void setCompanionLines(List<CompanionReportLineForm> companionLines) {
        this.companionLines = companionLines == null ? new ArrayList<>() : new ArrayList<>(companionLines);
    }

    public String getSymptomFlag() {
        return symptomFlag;
    }

    public void setSymptomFlag(String symptomFlag) {
        this.symptomFlag = symptomFlag;
    }

    public String getTriggerFlag() {
        return triggerFlag;
    }

    public void setTriggerFlag(String triggerFlag) {
        this.triggerFlag = triggerFlag;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getHouseholdSize() {
        return householdSize;
    }

    public void setHouseholdSize(String householdSize) {
        this.householdSize = householdSize;
    }

    public String getSmellType() {
        return smellType;
    }

    public void setSmellType(String smellType) {
        this.smellType = smellType;
    }

    public String getStainType() {
        return stainType;
    }

    public void setStainType(String stainType) {
        this.stainType = stainType;
    }

    public String getTasteType() {
        return tasteType;
    }

    public void setTasteType(String tasteType) {
        this.tasteType = tasteType;
    }

    public String getLocationScope() {
        return locationScope;
    }

    public void setLocationScope(String locationScope) {
        this.locationScope = locationScope;
    }

    public String getChangeTiming() {
        return changeTiming;
    }

    public void setChangeTiming(String changeTiming) {
        this.changeTiming = changeTiming;
    }

    public boolean isInfantPresent() {
        return infantPresent;
    }

    public void setInfantPresent(boolean infantPresent) {
        this.infantPresent = infantPresent;
    }

    public boolean isPregnancyPresent() {
        return pregnancyPresent;
    }

    public void setPregnancyPresent(boolean pregnancyPresent) {
        this.pregnancyPresent = pregnancyPresent;
    }

    public boolean isImmunocompromisedPresent() {
        return immunocompromisedPresent;
    }

    public void setImmunocompromisedPresent(boolean immunocompromisedPresent) {
        this.immunocompromisedPresent = immunocompromisedPresent;
    }

    public String getSlugHint() {
        return slugHint;
    }

    public void setSlugHint(String slugHint) {
        this.slugHint = slugHint;
    }
}
