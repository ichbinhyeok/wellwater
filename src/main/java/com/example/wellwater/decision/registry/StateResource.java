package com.example.wellwater.decision.registry;

public record StateResource(
        String stateCode,
        String localGuidanceUrl,
        String certifiedLabUrl,
        String sourceUrl
) {
}

