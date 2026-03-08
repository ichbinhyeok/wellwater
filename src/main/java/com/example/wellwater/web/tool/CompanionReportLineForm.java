package com.example.wellwater.web.tool;

import com.example.wellwater.decision.model.DecisionReportLine;

public class CompanionReportLineForm {

    private String analyteName;
    private String resultValue;
    private String unit;
    private String qualifier;
    private String qualifierValue;

    public DecisionReportLine toDecisionReportLine() {
        return new DecisionReportLine(analyteName, resultValue, unit, qualifier, qualifierValue);
    }

    public boolean isBlank() {
        return toDecisionReportLine().isBlank();
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
}
