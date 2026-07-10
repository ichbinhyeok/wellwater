package com.example.wellwater.web.welltest;

import com.example.wellwater.welltest.WellTestPlanRequest;

import java.util.ArrayList;
import java.util.List;

public class WellTestPlanForm {

    private String reason;
    private List<String> signals = new ArrayList<>();
    private List<String> riskContexts = new ArrayList<>();
    private String stateCode;
    private String existingTreatment;
    private String useScope;

    public WellTestPlanRequest toRequest() {
        return new WellTestPlanRequest(reason, signals, riskContexts, stateCode, existingTreatment, useScope);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getSignals() {
        return signals;
    }

    public void setSignals(List<String> signals) {
        this.signals = signals == null ? new ArrayList<>() : new ArrayList<>(signals);
    }

    public List<String> getRiskContexts() {
        return riskContexts;
    }

    public void setRiskContexts(List<String> riskContexts) {
        this.riskContexts = riskContexts == null ? new ArrayList<>() : new ArrayList<>(riskContexts);
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getExistingTreatment() {
        return existingTreatment;
    }

    public void setExistingTreatment(String existingTreatment) {
        this.existingTreatment = existingTreatment;
    }

    public String getUseScope() {
        return useScope;
    }

    public void setUseScope(String useScope) {
        this.useScope = useScope;
    }
}
