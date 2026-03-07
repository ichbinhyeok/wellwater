package com.example.wellwater.decision.model;

import java.util.List;

public record DecisionResult(
        EntryMode entryMode,
        Tier tier,
        Confidence confidence,
        Branch branch,
        Urgency urgency,
        Scope scope,
        ProblemType problemType,
        ActionMode actionMode,
        String primaryVerdictLabel,
        String primaryVerdictSentence,
        List<String> keyReasons,
        List<String> dataQualityNotes,
        List<String> todayActions,
        List<String> thisWeekActions,
        List<String> laterActions,
        List<ScenarioOption> scenarios,
        String costNote,
        List<String> assumptions,
        List<String> sourcesUsed,
        String disclosureText,
        boolean localGuidanceNeeded,
        List<CtaLink> ctas
) {
}

