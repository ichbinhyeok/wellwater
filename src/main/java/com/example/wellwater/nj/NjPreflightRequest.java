package com.example.wellwater.nj;

import java.util.List;

public record NjPreflightRequest(
        String transactionType,
        String waterSource,
        String address,
        String municipalitySlug,
        List<String> extraContexts,
        String channel,
        String source,
        String partnerSlug
) {
    public NjPreflightRequest {
        extraContexts = extraContexts == null ? List.of() : List.copyOf(extraContexts);
    }
}
