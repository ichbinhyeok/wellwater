package com.example.wellwater.web.tool;

import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;

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
    private String symptomFlag;
    private String triggerFlag;
    private boolean infantPresent;
    private boolean pregnancyPresent;
    private boolean immunocompromisedPresent;
    private String slugHint;

    public DecisionInput toDecisionInput() {
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
                existingTreatment,
                symptomFlag,
                triggerFlag,
                infantPresent,
                pregnancyPresent,
                immunocompromisedPresent,
                slugHint
        );
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
