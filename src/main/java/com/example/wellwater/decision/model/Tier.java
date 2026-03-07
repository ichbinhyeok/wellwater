package com.example.wellwater.decision.model;

public enum Tier {
    A("Tier A"),
    B("Tier B"),
    C("Tier C");

    private final String label;

    Tier(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}

