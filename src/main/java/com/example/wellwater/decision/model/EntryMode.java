package com.example.wellwater.decision.model;

public enum EntryMode {
    RESULT_FIRST("result-first"),
    SYMPTOM_FIRST("symptom-first"),
    TRIGGER_FIRST("trigger-first");

    private final String wireValue;

    EntryMode(String wireValue) {
        this.wireValue = wireValue;
    }

    public String wireValue() {
        return wireValue;
    }

    public static EntryMode fromWire(String value) {
        if (value == null) {
            return RESULT_FIRST;
        }
        return switch (value.trim().toLowerCase()) {
            case "symptom-first" -> SYMPTOM_FIRST;
            case "trigger-first" -> TRIGGER_FIRST;
            default -> RESULT_FIRST;
        };
    }
}

