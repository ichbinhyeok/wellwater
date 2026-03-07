package com.example.wellwater.decision.registry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StateResourceRegistryServiceTest {

    private final StateResourceRegistryService service =
            new StateResourceRegistryService("./data/registry/state_resource_registry.csv");

    @Test
    void findsExactStateEntry() {
        var maybe = service.findByState("TX");
        assertTrue(maybe.isPresent());
        assertTrue(maybe.get().localGuidanceUrl().contains("twdb.texas.gov"));
    }

    @Test
    void fallsBackToUsEntryWhenUnknownState() {
        var maybe = service.findByState("ZZ");
        assertTrue(maybe.isPresent());
        assertTrue(maybe.get().stateCode().equalsIgnoreCase("US"));
    }
}
