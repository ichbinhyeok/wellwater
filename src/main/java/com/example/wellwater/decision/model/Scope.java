package com.example.wellwater.decision.model;

public enum Scope {
    DRINKING_ONLY("drinking-only"),
    WHOLE_HOUSE("whole-house"),
    BOTH("both"),
    UNCLEAR("unclear");

    private final String wireValue;

    Scope(String wireValue) {
        this.wireValue = wireValue;
    }

    public String wireValue() {
        return wireValue;
    }
}

