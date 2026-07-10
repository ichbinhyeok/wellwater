package com.example.wellwater.mcp;

import com.example.wellwater.welltest.PartnerOffer;
import com.example.wellwater.welltest.PivotMetricService;
import com.example.wellwater.welltest.TestPanelItem;
import com.example.wellwater.welltest.WellTestPlanRequest;
import com.example.wellwater.welltest.WellTestPlanResult;
import com.example.wellwater.welltest.WellTestPlanService;
import io.modelcontextprotocol.server.McpStatelessServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class WellTestMcpAdapter {

    static final String TOOL_NAME = "recommend_private_well_test_plan";
    static final String WIDGET_URI = "ui://widget/well-test-plan.html";

    private final WellTestPlanService wellTestPlanService;
    private final PivotMetricService pivotMetricService;

    public WellTestMcpAdapter(WellTestPlanService wellTestPlanService, PivotMetricService pivotMetricService) {
        this.wellTestPlanService = wellTestPlanService;
        this.pivotMetricService = pivotMetricService;
    }

    public McpStatelessServerFeatures.SyncToolSpecification toolSpecification() {
        McpSchema.Tool tool = McpSchema.Tool.builder()
                .name(TOOL_NAME)
                .title("Recommend a private-well test plan")
                .description("Builds a focused U.S. private-well testing plan from the user's reason for testing, visible water clues, nearby risk context, optional state, existing treatment, and use scope. Use only when the user wants to decide what to test before buying treatment or a water test. It does not interpret lab results, diagnose illness, or guarantee legal compliance. Each call records only a disclosed non-identifying aggregate outcome, result category, partner eligibility, and latency metric; raw inputs and user identifiers are not stored.")
                .inputSchema(inputSchema())
                .outputSchema(outputSchema())
                .annotations(new McpSchema.ToolAnnotations(
                        "Recommend a private-well test plan",
                        false,
                        false,
                        false,
                        false,
                        false
                ))
                .meta(Map.of("ui", Map.of("resourceUri", WIDGET_URI)))
                .build();

        return McpStatelessServerFeatures.SyncToolSpecification.builder()
                .tool(tool)
                .callHandler((context, request) -> handle(request.arguments()))
                .build();
    }

    McpSchema.CallToolResult handle(Map<String, Object> arguments) {
        long started = System.nanoTime();
        try {
            WellTestPlanRequest request = new WellTestPlanRequest(
                    string(arguments, "reason"),
                    stringList(arguments, "signals", 8),
                    stringList(arguments, "risk_contexts", 8),
                    string(arguments, "state_code"),
                    string(arguments, "existing_treatment"),
                    string(arguments, "use_scope")
            );
            WellTestPlanResult result = wellTestPlanService.create(request, "chatgpt");
            pivotMetricService.record(
                    "tool_completed",
                    "chatgpt",
                    result.resultFamily(),
                    result.hasPartnerOffer() ? result.partnerOffer().productCode() : "",
                    "success",
                    elapsedMs(started)
            );
            return McpSchema.CallToolResult.builder()
                    .addTextContent(summary(result))
                    .structuredContent(structured(result))
                    .meta(Map.of("ui", Map.of("resourceUri", WIDGET_URI)))
                    .isError(false)
                    .build();
        } catch (IllegalArgumentException e) {
            pivotMetricService.record("tool_failed", "chatgpt", "", "", "invalid_input", elapsedMs(started));
            return McpSchema.CallToolResult.builder()
                    .addTextContent("I could not build a test plan because the input was outside the supported private-well options. " + e.getMessage())
                    .isError(true)
                    .build();
        } catch (RuntimeException e) {
            pivotMetricService.record("tool_failed", "chatgpt", "", "", "server_error", elapsedMs(started));
            return McpSchema.CallToolResult.builder()
                    .addTextContent("The private-well test planner is temporarily unavailable. Use the certified-lab path at https://www.epa.gov/dwlabcert/contact-information-certification-programs-and-certified-laboratories-drinking-water")
                    .isError(true)
                    .build();
        }
    }

    private McpSchema.JsonSchema inputSchema() {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("reason", enumString(
                "The explicit reason the user wants a private-well test plan.",
                List.of("annual", "home_purchase", "after_flood", "after_heavy_rain", "after_repair", "after_wildfire", "new_or_unused_well", "symptom", "known_contamination", "other")
        ));
        properties.put("signals", enumArray(
                "Visible, taste, odor, or scale clues explicitly mentioned by the user.",
                List.of("rotten_egg_smell", "metallic_taste", "orange_stains", "blue_green_stains", "black_stains", "cloudy_water", "scale_buildup", "no_obvious_issue")
        ));
        properties.put("risk_contexts", enumArray(
                "Nearby land-use or contamination contexts explicitly mentioned by the user. Do not infer an address or precise location.",
                List.of("agriculture", "industrial", "mining", "nearby_septic", "fuel_or_petroleum", "pfas_source", "radionuclides", "unknown")
        ));
        properties.put("state_code", Map.of(
                "type", "string",
                "pattern", "^[A-Z]{2}$",
                "description", "Optional two-letter U.S. state code only when the user explicitly provides it or coarse location is available."
        ));
        properties.put("existing_treatment", enumString(
                "Optional existing treatment explicitly mentioned by the user.",
                List.of("none", "ro", "uv", "softener", "iron_filter", "carbon", "sediment", "unknown")
        ));
        properties.put("use_scope", enumString(
                "Optional intended water-use scope.",
                List.of("drinking_only", "whole_house", "both", "unknown")
        ));
        return new McpSchema.JsonSchema("object", properties, List.of("reason"), false, null, null);
    }

    private Map<String, Object> outputSchema() {
        Map<String, Object> link = Map.of(
                "type", "object",
                "properties", Map.of("label", Map.of("type", "string"), "url", Map.of("type", "string")),
                "required", List.of("label", "url"),
                "additionalProperties", false
        );
        Map<String, Object> panelItem = Map.of(
                "type", "object",
                "properties", Map.of("name", Map.of("type", "string"), "reason", Map.of("type", "string")),
                "required", List.of("name", "reason"),
                "additionalProperties", false
        );
        Map<String, Object> partner = Map.of(
                "type", "object",
                "properties", Map.of(
                        "product_code", Map.of("type", "string"),
                        "product_name", Map.of("type", "string"),
                        "fit_reason", Map.of("type", "string"),
                        "url", Map.of("type", "string"),
                        "disclosure", Map.of("type", "string")
                ),
                "required", List.of("product_code", "product_name", "fit_reason", "url", "disclosure"),
                "additionalProperties", false
        );
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("verdict", Map.of("type", "string"));
        properties.put("urgency", Map.of("type", "string", "enum", List.of("immediate", "prompt", "routine")));
        properties.put("recommended_panel", Map.of("type", "array", "items", panelItem));
        properties.put("reasons", Map.of("type", "array", "items", Map.of("type", "string")));
        properties.put("official_guidance", link);
        properties.put("certified_lab_path", link);
        properties.put("partner_offer", partner);
        properties.put("disclosure", Map.of("type", "string"));
        properties.put("source_version", Map.of("type", "string"));
        return Map.of(
                "type", "object",
                "properties", properties,
                "required", List.of("verdict", "urgency", "recommended_panel", "reasons", "official_guidance", "certified_lab_path", "disclosure", "source_version"),
                "additionalProperties", false
        );
    }

    private Map<String, Object> structured(WellTestPlanResult result) {
        Map<String, Object> structured = new LinkedHashMap<>();
        structured.put("verdict", result.verdict());
        structured.put("urgency", result.urgency());
        structured.put("recommended_panel", result.recommendedPanel().stream().map(this::panelMap).toList());
        structured.put("reasons", result.reasons());
        structured.put("official_guidance", Map.of("label", result.officialGuidance().label(), "url", result.officialGuidance().url()));
        structured.put("certified_lab_path", Map.of("label", result.certifiedLabPath().label(), "url", result.certifiedLabPath().url()));
        if (result.hasPartnerOffer()) {
            structured.put("partner_offer", partnerMap(result.partnerOffer()));
        }
        structured.put("disclosure", result.disclosure());
        structured.put("source_version", result.sourceVersion());
        return structured;
    }

    private Map<String, Object> panelMap(TestPanelItem item) {
        return Map.of("name", item.name(), "reason", item.reason());
    }

    private Map<String, Object> partnerMap(PartnerOffer offer) {
        return Map.of(
                "product_code", offer.productCode(),
                "product_name", offer.productName(),
                "fit_reason", offer.fitReason(),
                "url", offer.url(),
                "disclosure", offer.disclosure()
        );
    }

    private String summary(WellTestPlanResult result) {
        StringBuilder out = new StringBuilder(result.verdict());
        out.append("\nUrgency: ").append(result.urgency()).append("\nRecommended panel:");
        for (TestPanelItem item : result.recommendedPanel()) {
            out.append("\n- ").append(item.name()).append(": ").append(item.reason());
        }
        out.append("\nOfficial guidance: ").append(result.officialGuidance().url());
        out.append("\nCertified lab path: ").append(result.certifiedLabPath().url());
        if (result.hasPartnerOffer()) {
            out.append("\nOptional physical test kit: ").append(result.partnerOffer().productName())
                    .append(" - ").append(result.partnerOffer().url())
                    .append("\n").append(result.partnerOffer().disclosure());
        }
        out.append("\n").append(result.disclosure());
        return out.toString();
    }

    private Map<String, Object> enumString(String description, List<String> values) {
        return Map.of("type", "string", "enum", values, "description", description);
    }

    private Map<String, Object> enumArray(String description, List<String> values) {
        return Map.of(
                "type", "array",
                "items", Map.of("type", "string", "enum", values),
                "maxItems", 8,
                "uniqueItems", true,
                "description", description
        );
    }

    private String string(Map<String, Object> arguments, String key) {
        if (arguments == null) {
            return "";
        }
        Object value = arguments.get(key);
        return value instanceof String string ? string : "";
    }

    private List<String> stringList(Map<String, Object> arguments, String key, int limit) {
        if (arguments == null || arguments.get(key) == null) {
            return List.of();
        }
        if (!(arguments.get(key) instanceof List<?> values) || values.size() > limit) {
            throw new IllegalArgumentException(key + " must be a short list.");
        }
        List<String> strings = new ArrayList<>();
        for (Object value : values) {
            if (!(value instanceof String string)) {
                throw new IllegalArgumentException(key + " must contain strings only.");
            }
            strings.add(string);
        }
        return List.copyOf(strings);
    }

    private long elapsedMs(long started) {
        return Math.max(0L, (System.nanoTime() - started) / 1_000_000L);
    }
}
