package com.example.wellwater.decision.model;

public enum Branch {
    RED("Red"),
    AMBER("Amber"),
    GREEN("Green");

    private final String label;

    Branch(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}

