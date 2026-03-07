package com.example.wellwater.decision.model;

public enum ActionMode {
    USE_ALTERNATE_WATER("use_alternate_water"),
    BOIL("boil"),
    DO_NOT_BOIL("do_not_boil"),
    RETEST("retest"),
    COMPARE_TREATMENT("compare_treatment"),
    INSPECT_SOURCE("inspect_source"),
    CONTACT_LOCAL_GUIDANCE("contact_local_guidance");

    private final String wireValue;

    ActionMode(String wireValue) {
        this.wireValue = wireValue;
    }

    public String wireValue() {
        return wireValue;
    }
}

