package com.example.wellwater.pseo;

import java.util.List;

public record PseoDecisionDoc(
        String oneLineVerdict,
        String healthVsNuisance,
        List<String> nextSteps,
        List<String> commonConfusions,
        String retestTiming,
        List<PseoDecisionSplit> decisionSplits,
        List<String> escalationSignals,
        List<PseoFaqItem> faqs
) {
}
