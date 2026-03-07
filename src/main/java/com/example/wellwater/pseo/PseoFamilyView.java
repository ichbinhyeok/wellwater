package com.example.wellwater.pseo;

import java.util.List;

public record PseoFamilyView(
        String familyKey,
        String heroTitle,
        String heroLead,
        String operatorNote,
        String primaryToolLabel,
        String primaryToolHref,
        List<PseoPage> starterPages,
        List<String> commonMistakes,
        List<String> beforeToolChecks
) {
}
