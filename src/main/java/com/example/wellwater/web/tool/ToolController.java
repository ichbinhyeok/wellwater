package com.example.wellwater.web.tool;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.decision.DecisionEngineService;
import com.example.wellwater.decision.model.CtaLink;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.DecisionResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Controller
public class ToolController {

    private final DecisionEngineService decisionEngineService;
    private final AnalyticsEventService analyticsEventService;

    public ToolController(DecisionEngineService decisionEngineService, AnalyticsEventService analyticsEventService) {
        this.decisionEngineService = decisionEngineService;
        this.analyticsEventService = analyticsEventService;
    }

    @GetMapping("/tool/result-first")
    public String resultFirst(@RequestParam(required = false) String analyte, Model model) {
        ToolRequest request = new ToolRequest();
        request.setEntryMode("result-first");
        request.setAnalyteName(analyte);
        addSharedOptions(model);
        model.addAttribute("request", request);
        analyticsEventService.logEvent("entry_mode_selected", "result-first", null, analyte, null, null, null, null, "landing");
        return "pages/intake/result-first";
    }

    @GetMapping("/tool/symptom-first")
    public String symptomFirst(@RequestParam(required = false) String symptom, @RequestParam(required = false) String slug, Model model) {
        ToolRequest request = new ToolRequest();
        request.setEntryMode("symptom-first");
        request.setSymptomFlag(symptom);
        request.setSlugHint(slug);
        addSharedOptions(model);
        model.addAttribute("request", request);
        analyticsEventService.logEvent("entry_mode_selected", "symptom-first", null, slug, null, null, null, null, "landing");
        return "pages/intake/symptom-first";
    }

    @GetMapping("/tool/trigger-first")
    public String triggerFirst(@RequestParam(required = false) String trigger, @RequestParam(required = false) String slug, Model model) {
        ToolRequest request = new ToolRequest();
        request.setEntryMode("trigger-first");
        request.setTriggerFlag(trigger);
        request.setSlugHint(slug);
        addSharedOptions(model);
        model.addAttribute("request", request);
        analyticsEventService.logEvent("entry_mode_selected", "trigger-first", null, slug, null, null, null, null, "landing");
        return "pages/intake/trigger-first";
    }

    @PostMapping("/tool/result")
    public String decisionResult(@ModelAttribute("request") ToolRequest request, Model model) {
        addSharedOptions(model);

        DecisionInput input = request.toDecisionInput();
        DecisionResult result = decisionEngineService.decide(input);
        String sessionId = UUID.randomUUID().toString();

        List<RenderableCta> ctas = result.ctas().stream()
                .map(cta -> toRenderableCta(cta, result, sessionId, input.slugHint()))
                .toList();

        model.addAttribute("result", result);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("ctaLinks", ctas);

        analyticsEventService.logEvent(
                "test_completed",
                result.entryMode().wireValue(),
                sessionId,
                input.slugHint(),
                result.tier().label(),
                result.branch().label(),
                null,
                null,
                "submitted"
        );
        analyticsEventService.logEvent(
                "result_viewed",
                result.entryMode().wireValue(),
                sessionId,
                input.slugHint(),
                result.tier().label(),
                result.branch().label(),
                null,
                null,
                "rendered"
        );
        return "pages/result/view";
    }

    @GetMapping("/tool/out")
    public RedirectView outbound(
            @RequestParam String target,
            @RequestParam String ctaType,
            @RequestParam String entryMode,
            @RequestParam String sessionId,
            @RequestParam(required = false) String tier,
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) String slug
    ) {
        analyticsEventService.logEvent(
                "cta_clicked",
                entryMode,
                sessionId,
                slug,
                tier,
                branch,
                ctaType,
                target,
                "redirect"
        );
        String safeTarget = isAllowedTarget(target) ? target : "/";
        RedirectView view = new RedirectView(safeTarget);
        view.setExposeModelAttributes(false);
        return view;
    }

    private RenderableCta toRenderableCta(CtaLink cta, DecisionResult result, String sessionId, String slug) {
        String outboundUrl = UriComponentsBuilder.fromPath("/tool/out")
                .queryParam("target", cta.targetUrl())
                .queryParam("ctaType", cta.type())
                .queryParam("entryMode", result.entryMode().wireValue())
                .queryParam("sessionId", sessionId)
                .queryParam("tier", result.tier().label())
                .queryParam("branch", result.branch().label())
                .queryParam("slug", slug == null ? "" : slug)
                .build()
                .toUriString();
        return new RenderableCta(cta.type(), cta.label(), outboundUrl);
    }

    private boolean isAllowedTarget(String target) {
        if (target == null || target.isBlank()) {
            return false;
        }
        return target.startsWith("http://") || target.startsWith("https://") || target.startsWith("/");
    }

    private void addSharedOptions(Model model) {
        model.addAttribute("analytes", List.of(
                "nitrate", "arsenic", "total coliform", "e. coli", "iron",
                "manganese", "hardness", "ph", "tds", "sulfate", "lead", "pfas"
        ));
        model.addAttribute("symptoms", List.of(
                "rotten-egg-smell", "orange-stains", "black-stains", "metallic-taste",
                "cloudy-water", "scale-buildup", "blue-green-stains"
        ));
        model.addAttribute("triggers", List.of(
                "after-flood", "after-heavy-rain", "after-repair", "after-wildfire",
                "home-purchase-test", "retest-after-treatment"
        ));
        model.addAttribute("states", List.of(
                "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
                "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
                "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
        ));
        model.addAttribute("qualifiers", List.of(
                "none", "nd", "less_than", "estimated", "positive", "negative", "unknown"
        ));
        model.addAttribute("useScopes", List.of("drinking-only", "whole-house", "both"));
        model.addAttribute("existingTreatments", List.of(
                "none", "ro", "uv", "softener", "iron filter", "carbon", "sediment", "unknown"
        ));
    }

    public record RenderableCta(
            String type,
            String label,
            String outboundUrl
    ) {
    }
}
