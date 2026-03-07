package com.example.wellwater.decision.model;

public enum ProblemType {
    MICROBIAL("microbial"),
    CHEMICAL_HEALTH("chemical-health"),
    AESTHETIC_OPERATIONAL("aesthetic-operational"),
    CORROSION("corrosion"),
    UNSUPPORTED("unsupported");

    private final String wireValue;

    ProblemType(String wireValue) {
        this.wireValue = wireValue;
    }

    public String wireValue() {
        return wireValue;
    }
}

