package com.example.wellwater.decision.registry;

import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CostRegistryServiceTest {

    private final CostRegistryService service = new CostRegistryService("./data/registry/cost_registry.csv");

    @Test
    void findsSpecificProblemTypeAndScope() {
        var maybe = service.find(ProblemType.CHEMICAL_HEALTH, Scope.DRINKING_ONLY);
        assertTrue(maybe.isPresent());
        assertTrue(maybe.get().installRange().contains("$"));
    }

    @Test
    void fallsBackToAnyAny() {
        var maybe = service.find(ProblemType.UNSUPPORTED, Scope.UNCLEAR);
        assertTrue(maybe.isPresent());
        assertTrue(maybe.get().sourceUrl().contains("homeguide.com"));
    }
}
