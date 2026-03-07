package com.example.wellwater.decision;

import com.example.wellwater.decision.model.Branch;
import com.example.wellwater.decision.model.Confidence;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Tier;
import com.example.wellwater.decision.model.Urgency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecisionEngineServiceTest {

    private final DecisionEngineService service = new DecisionEngineService();

    @Test
    void nitrateWithInfantRoutesToImmediateRed() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "nitrate",
                "12",
                "mg/L",
                "raw well",
                "yes",
                "",
                "",
                true,
                false,
                false,
                ""
        );

        var result = service.decide(input);

        assertEquals(Tier.A, result.tier());
        assertEquals(ProblemType.CHEMICAL_HEALTH, result.problemType());
        assertEquals(Urgency.IMMEDIATE, result.urgency());
        assertEquals(Branch.RED, result.branch());
    }

    @Test
    void unknownAnalyteFallsBackToTierCAmber() {
        DecisionInput input = new DecisionInput(
                EntryMode.RESULT_FIRST,
                "unknown-metal-x",
                "4.3",
                "mg/L",
                "unknown",
                "unknown",
                "",
                "",
                false,
                false,
                false,
                ""
        );

        var result = service.decide(input);

        assertEquals(Tier.C, result.tier());
        assertEquals(ProblemType.UNSUPPORTED, result.problemType());
        assertEquals(Branch.AMBER, result.branch());
        assertEquals(Confidence.LOW, result.confidence());
    }

    @Test
    void symptomWithStrongContextCanReachGreen() {
        DecisionInput input = new DecisionInput(
                EntryMode.SYMPTOM_FIRST,
                "",
                "",
                "",
                "raw well",
                "yes",
                "rotten-egg-smell",
                "",
                false,
                false,
                false,
                "rotten-egg-smell"
        );

        var result = service.decide(input);

        assertEquals(Tier.A, result.tier());
        assertEquals(ProblemType.AESTHETIC_OPERATIONAL, result.problemType());
        assertEquals(Branch.GREEN, result.branch());
    }
}

