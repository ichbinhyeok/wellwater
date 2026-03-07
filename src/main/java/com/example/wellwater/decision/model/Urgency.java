package com.example.wellwater.decision.model;

public enum Urgency {
    IMMEDIATE("immediate"),
    PROMPT("prompt"),
    ROUTINE("routine");

    private final String wireValue;

    Urgency(String wireValue) {
        this.wireValue = wireValue;
    }

    public String wireValue() {
        return wireValue;
    }
}

