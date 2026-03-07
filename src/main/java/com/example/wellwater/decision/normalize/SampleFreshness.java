package com.example.wellwater.decision.normalize;

public enum SampleFreshness {
    FRESH("fresh"),
    AGING("aging"),
    STALE("stale"),
    UNKNOWN("unknown");

    private final String wireValue;

    SampleFreshness(String wireValue) {
        this.wireValue = wireValue;
    }

    public String wireValue() {
        return wireValue;
    }
}

