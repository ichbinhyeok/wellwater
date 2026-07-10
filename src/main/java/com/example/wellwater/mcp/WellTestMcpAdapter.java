package com.example.wellwater.mcp;

import com.example.wellwater.welltest.PivotMetricService;
import com.example.wellwater.welltest.PlanResourceLink;
import com.example.wellwater.welltest.ResultResourceLinkService;
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
    static final String WIDGET_URI = "ui://widget/well-test-plan-v2.html";

    private final WellTestPlanService wellTestPlanService;
    private final PivotMetricService pivotMetricService;
    private final ResultResourceLinkService resultResourceLinkService;

    public WellTestMcpAdapter(
            WellTestPlanService wellTestPlanService,
            PivotMetricService pivotMetricService,
            ResultResourceLinkService resultResourceLinkService
    ) {
        this.wellTestPlanService = wellTestPlanService;
        this.pivotMetricService = pivotMetricService;
        this.resultResourceLinkService = resultResourceLinkService;
    }

    public McpStatelessServerFeatures.SyncToolSpecification toolSpecification() {
        McpSchema.Tool tool = McpSchema.Tool.builder()
                .name(TOOL_NAME)
                .title("Build a private-well test plan")
                .description("Use this when a U.S. private-well owner asks what water tests, testing panel, or certified lab path to choose before treatment. Match direct or indirect intent including an annual check, home purchase, flood or heavy rain, well repair, wildfire, a new well, odor, taste, staining, cloudiness, scale, agriculture, septic, industrial, mining, fuel, PFAS, or radionuclide context. If the well-testing request is general, use reason 'other' and do not delay for optional fields. Do not use it for municipal water, lab-report interpretation, medical diagnosis, treatment sizing, emergency response, legal clearance, or shopping. Each call records only a disclosed non-identifying aggregate outcome, result category, and latency metric; raw inputs and user identifiers are not stored.")
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
                .meta(toolMeta())
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
            pivotMetricService.tryRecord(
                    "tool_completed",
                    "chatgpt",
                    result.resultFamily(),
                    "",
                    "success",
                    elapsedMs(started)
            );
            return McpSchema.CallToolResult.builder()
                    .addTextContent(summary(result))
                    .structuredContent(structured(result))
                    .meta(toolMeta())
                    .isError(false)
                    .build();
        } catch (IllegalArgumentException e) {
            pivotMetricService.tryRecord("tool_failed", "chatgpt", "", "", "invalid_input", elapsedMs(started));
            return McpSchema.CallToolResult.builder()
                    .addTextContent("I could not build a test plan because the input was outside the supported private-well options. " + e.getMessage())
                    .isError(true)
                    .build();
        } catch (RuntimeException e) {
            pivotMetricService.tryRecord("tool_failed", "chatgpt", "", "", "server_error", elapsedMs(started));
            return McpSchema.CallToolResult.builder()
                    .addTextContent("The private-well test planner is temporarily unavailable. Use the certified-lab path at https://www.epa.gov/dwlabcert/contact-information-certification-programs-and-certified-laboratories-drinking-water")
                    .isError(true)
                    .build();
        }
    }

    private McpSchema.JsonSchema inputSchema() {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("reason", enumString(
                "Required route. Use symptom for an odor, taste, stain, cloudiness, or scale clue; use known_contamination for a confirmed nearby problem; use other for a general private-well testing request with no listed trigger.",
                List.of("annual", "home_purchase", "after_flood", "after_heavy_rain", "after_repair", "after_wildfire", "new_or_unused_well", "symptom", "known_contamination", "other")
        ));
        properties.put("signals", enumArray(
                "Optional visible, taste, odor, or scale clues explicitly mentioned by the user. Use no_obvious_issue only when no other clue is present.",
                List.of("rotten_egg_smell", "metallic_taste", "orange_stains", "blue_green_stains", "black_stains", "cloudy_water", "scale_buildup", "no_obvious_issue")
        ));
        properties.put("risk_contexts", enumArray(
                "Optional nearby land-use or contamination contexts explicitly mentioned by the user. Use unknown only when no specific context is present. Do not infer an address or precise location.",
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
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("verdict", Map.of("type", "string"));
        properties.put("urgency", Map.of("type", "string", "enum", List.of("immediate", "prompt", "routine")));
        properties.put("recommended_panel", Map.of("type", "array", "items", panelItem));
        properties.put("next_steps", Map.of("type", "array", "items", Map.of("type", "string"), "maxItems", 3));
        properties.put("avoid_for_now", Map.of("type", "string"));
        properties.put("reasons", Map.of("type", "array", "items", Map.of("type", "string")));
        properties.put("official_guidance", link);
        properties.put("certified_lab_path", link);
        properties.put("disclosure", Map.of("type", "string"));
        properties.put("source_version", Map.of("type", "string"));
        return Map.of(
                "type", "object",
                "properties", properties,
                "required", List.of("verdict", "urgency", "recommended_panel", "next_steps", "avoid_for_now", "reasons", "official_guidance", "certified_lab_path", "disclosure", "source_version"),
                "additionalProperties", false
        );
    }

    private Map<String, Object> structured(WellTestPlanResult result) {
        Map<String, Object> structured = new LinkedHashMap<>();
        structured.put("verdict", result.verdict());
        structured.put("urgency", result.urgency());
        structured.put("recommended_panel", result.recommendedPanel().stream().map(this::panelMap).toList());
        structured.put("next_steps", result.nextSteps());
        structured.put("avoid_for_now", result.avoidForNow());
        structured.put("reasons", result.reasons());
        PlanResourceLink guidance = resultResourceLinkService.tracked(
                result.officialGuidance(), "official_guidance", "chatgpt", result.resultFamily());
        PlanResourceLink lab = resultResourceLinkService.tracked(
                result.certifiedLabPath(), "certified_lab", "chatgpt", result.resultFamily());
        structured.put("official_guidance", Map.of("label", guidance.label(), "url", guidance.url()));
        structured.put("certified_lab_path", Map.of("label", lab.label(), "url", lab.url()));
        structured.put("disclosure", result.disclosure());
        structured.put("source_version", result.sourceVersion());
        return structured;
    }

    private Map<String, Object> panelMap(TestPanelItem item) {
        return Map.of("name", item.name(), "reason", item.reason());
    }

    private String summary(WellTestPlanResult result) {
        StringBuilder out = new StringBuilder(result.verdict());
        out.append("\nUrgency: ").append(result.urgency()).append("\nRecommended panel:");
        for (TestPanelItem item : result.recommendedPanel()) {
            out.append("\n- ").append(item.name()).append(": ").append(item.reason());
        }
        out.append("\nNext steps:");
        for (String step : result.nextSteps()) {
            out.append("\n- ").append(step);
        }
        out.append("\nAvoid for now: ").append(result.avoidForNow());
        PlanResourceLink guidance = resultResourceLinkService.tracked(
                result.officialGuidance(), "official_guidance", "chatgpt", result.resultFamily());
        PlanResourceLink lab = resultResourceLinkService.tracked(
                result.certifiedLabPath(), "certified_lab", "chatgpt", result.resultFamily());
        out.append("\nOfficial guidance: ").append(guidance.url());
        out.append("\nCertified lab path: ").append(lab.url());
        out.append("\n").append(result.disclosure());
        return out.toString();
    }

    private Map<String, Object> toolMeta() {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("ui", Map.of("resourceUri", WIDGET_URI));
        meta.put("openai/outputTemplate", WIDGET_URI);
        meta.put("openai/toolInvocation/invoking", "Building your private-well test plan");
        meta.put("openai/toolInvocation/invoked", "Your private-well test plan is ready");
        return Map.copyOf(meta);
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
