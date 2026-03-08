package com.example.wellwater.decision.model;

public record RecommendedTestCard(
        String testName,
        String whyNow,
        String samplePlan,
        String resourceLabel,
        String resourceUrl
) {
    public String summary() {
        StringBuilder out = new StringBuilder();
        if (testName != null && !testName.isBlank()) {
            out.append(testName.trim());
        }
        if (whyNow != null && !whyNow.isBlank()) {
            if (out.length() > 0) {
                out.append(": ");
            }
            out.append(whyNow.trim());
        }
        if (samplePlan != null && !samplePlan.isBlank()) {
            if (out.length() > 0) {
                out.append(" Sample plan: ");
            }
            out.append(samplePlan.trim());
        }
        return out.toString();
    }

    public boolean hasResourceLink() {
        return resourceUrl != null && !resourceUrl.isBlank();
    }
}
