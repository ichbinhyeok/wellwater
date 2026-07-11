package com.example.wellwater.nj;

import com.example.wellwater.welltest.WellTestPlanResult;

import java.util.List;

public record NjPreflightResult(
        NjCoverage coverage,
        String locationLabel,
        String locationResolution,
        String locationNotice,
        String county,
        String municipalitySlug,
        List<NjRequirement> requiredPanel,
        List<NjRiskSignal> localSignals,
        String localSignalScope,
        List<String> extraDiscussions,
        List<String> nextSteps,
        WellTestPlanResult basePlan,
        NjPartner partner,
        String channel,
        String source
) {
    public NjPreflightResult {
        requiredPanel = requiredPanel == null ? List.of() : List.copyOf(requiredPanel);
        localSignals = localSignals == null ? List.of() : List.copyOf(localSignals);
        extraDiscussions = extraDiscussions == null ? List.of() : List.copyOf(extraDiscussions);
        nextSteps = nextSteps == null ? List.of() : List.copyOf(nextSteps);
    }

    public boolean hasPartner() {
        return partner != null;
    }
}
