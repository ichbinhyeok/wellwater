package com.example.wellwater.decision.model;

public enum Confidence {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String label;

    Confidence(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}

