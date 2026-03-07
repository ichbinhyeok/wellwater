package com.example.wellwater.decision.registry;

import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Tier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecisionRegistryServiceTest {

    private final DecisionRegistryService service = new DecisionRegistryService(
            "./data/registry/contaminant_registry.csv",
            "./data/registry/symptom_registry.csv",
            "./data/registry/trigger_registry.csv"
    );

    @Test
    void loadsContaminantRule() {
        var maybe = service.findContaminant("nitrate");
        assertTrue(maybe.isPresent());
        assertEquals(Tier.A, maybe.get().tier());
        assertEquals(ProblemType.CHEMICAL_HEALTH, maybe.get().problemType());
    }

    @Test
    void loadsSymptomRule() {
        var maybe = service.findSymptom("metallic-taste");
        assertTrue(maybe.isPresent());
        assertEquals(ProblemType.CORROSION, maybe.get().problemType());
    }

    @Test
    void loadsTriggerRule() {
        var maybe = service.findTrigger("after-flood");
        assertTrue(maybe.isPresent());
        assertEquals(Tier.B, maybe.get().tier());
    }
}

