package com.example.wellwater.welltest;

import java.util.List;

public record WellTestPlanResult(
        String verdict,
        String urgency,
        List<TestPanelItem> recommendedPanel,
        List<String> nextSteps,
        String avoidForNow,
        List<String> reasons,
        PlanResourceLink officialGuidance,
        PlanResourceLink certifiedLabPath,
        PartnerOffer partnerOffer,
        String commerceNote,
        String disclosure,
        String sourceVersion,
        String resultFamily
) {
    public WellTestPlanResult {
        recommendedPanel = recommendedPanel == null ? List.of() : List.copyOf(recommendedPanel);
        nextSteps = nextSteps == null ? List.of() : List.copyOf(nextSteps);
        reasons = reasons == null ? List.of() : List.copyOf(reasons);
    }

    public boolean hasPartnerOffer() {
        return partnerOffer != null;
    }
}
