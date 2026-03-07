package com.example.wellwater.pseo;

import java.util.List;

public record PseoDetailView(
        PseoPage page,
        String archetypeLabel,
        String intentLabel,
        String riskLabel,
        String riskSummary,
        String householdLabel,
        String householdSummary,
        String doNotBuyYet,
        PseoEntryHint entryHint,
        List<PseoQuickAnswer> quickAnswers,
        PseoDecisionDoc decisionDoc,
        List<PseoRelatedSection> relatedSections,
        List<PseoCitation> citations
) {
}
