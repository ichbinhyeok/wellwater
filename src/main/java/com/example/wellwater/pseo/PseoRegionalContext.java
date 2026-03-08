package com.example.wellwater.pseo;

public record PseoRegionalContext(
        String stateCode,
        String stateName,
        String localDelta,
        String decisionTrigger,
        String labNote,
        String guidanceUrl,
        String certifiedLabUrl,
        String sourceUrl
) {
}
