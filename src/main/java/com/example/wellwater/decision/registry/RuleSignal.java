package com.example.wellwater.decision.registry;

import com.example.wellwater.decision.model.ActionMode;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Scope;
import com.example.wellwater.decision.model.Tier;
import com.example.wellwater.decision.model.Urgency;

import java.util.List;

public record RuleSignal(
        String key,
        Tier tier,
        ProblemType problemType,
        Urgency urgency,
        Scope scope,
        ActionMode actionMode,
        List<String> claimRequirements,
        List<String> sources
) {
}

