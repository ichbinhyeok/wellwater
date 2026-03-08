package com.example.wellwater.web.result;

import com.example.wellwater.decision.model.DecisionResult;

public record ResultSnapshot(
        String id,
        String sessionId,
        String sourceSlug,
        String createdAt,
        String expiresAt,
        DecisionResult result
) {
}
