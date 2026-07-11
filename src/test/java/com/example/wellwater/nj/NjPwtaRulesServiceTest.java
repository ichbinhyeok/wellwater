package com.example.wellwater.nj;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NjPwtaRulesServiceTest {

    private final NjPwtaRulesService service = new NjPwtaRulesService();

    @Test
    void appliesCurrentCountySpecificMercuryAndUraniumRules() {
        assertTrue(service.requiresMercury("Ocean County"));
        assertFalse(service.requiresUranium("Ocean County"));
        assertTrue(service.requiresUranium("Morris"));
        assertFalse(service.requiresMercury("Morris"));
    }

    @Test
    void distinguishesCoveredDrinkingWaterTransactionsFromIrrigation() {
        assertTrue(service.coverage("sale", "private_well").status().equals("likely_covered"));
        assertTrue(service.coverage("lease", "private_well").headline().contains("lease"));
        assertTrue(service.coverage("sale", "irrigation_only").status().equals("not_likely_covered"));
    }
}
