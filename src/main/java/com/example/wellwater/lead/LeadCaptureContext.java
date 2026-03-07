package com.example.wellwater.lead;

public record LeadCaptureContext(
        String heading,
        String summary,
        String submitLabel,
        String redirectPath,
        String sourceType,
        String sourceFamily,
        String sourceSlug,
        String sourceLabel
) {
}
