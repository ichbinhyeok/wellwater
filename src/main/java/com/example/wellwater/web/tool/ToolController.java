package com.example.wellwater.web.tool;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.decision.DecisionEngineService;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.DecisionResult;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import com.example.wellwater.web.result.RenderableCta;
import com.example.wellwater.web.result.ResultCtaService;
import com.example.wellwater.web.result.ResultSnapshot;
import com.example.wellwater.web.result.ResultSnapshotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Controller
public class ToolController {

    private static final Set<String> STATIC_ALLOWED_OUTBOUND_HOSTS = Set.of(
            "www.epa.gov",
            "epa.gov",
            "www.cdc.gov",
            "cdc.gov",
            "www.google.com",
            "google.com"
    );

    private final DecisionEngineService decisionEngineService;
    private final AnalyticsEventService analyticsEventService;
    private final DecisionRegistryService decisionRegistryService;
    private final ResultCtaService resultCtaService;
    private final ResultSnapshotService resultSnapshotService;
    private final Set<String> allowedOutboundHosts;

    public ToolController(
            DecisionEngineService decisionEngineService,
            AnalyticsEventService analyticsEventService,
            DecisionRegistryService decisionRegistryService,
            StateResourceRegistryService stateResourceRegistryService,
            ResultCtaService resultCtaService,
            ResultSnapshotService resultSnapshotService
    ) {
        this.decisionEngineService = decisionEngineService;
        this.analyticsEventService = analyticsEventService;
        this.decisionRegistryService = decisionRegistryService;
        this.resultCtaService = resultCtaService;
        this.resultSnapshotService = resultSnapshotService;
        LinkedHashSet<String> hosts = new LinkedHashSet<>(STATIC_ALLOWED_OUTBOUND_HOSTS);
        hosts.addAll(stateResourceRegistryService.allowedOutboundHosts());
        this.allowedOutboundHosts = Set.copyOf(hosts);
    }

    @GetMapping("/tool/result-first")
    public String resultFirst(
            @RequestParam(required = false) String analyte,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String slug,
            Model model
    ) {
        ToolRequest request = new ToolRequest();
        request.setEntryMode("result-first");
        request.setAnalyteName(analyte);
        request.setState(state);
        request.setSlugHint(slug);
        addSharedOptions(model);
        model.addAttribute("request", request);
        analyticsEventService.logEvent("entry_mode_selected", "result-first", null, preferredSlug(slug, analyte), null, null, null, null, "landing");
        return "pages/intake/result-first";
    }

    @GetMapping("/tool/symptom-first")
    public String symptomFirst(
            @RequestParam(required = false) String symptom,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String slug,
            Model model
    ) {
        ToolRequest request = new ToolRequest();
        request.setEntryMode("symptom-first");
        request.setSymptomFlag(symptom);
        request.setState(state);
        request.setSlugHint(slug);
        addSharedOptions(model);
        model.addAttribute("request", request);
        analyticsEventService.logEvent("entry_mode_selected", "symptom-first", null, slug, null, null, null, null, "landing");
        return "pages/intake/symptom-first";
    }

    @GetMapping("/tool/trigger-first")
    public String triggerFirst(
            @RequestParam(required = false) String trigger,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String slug,
            Model model
    ) {
        ToolRequest request = new ToolRequest();
        request.setEntryMode("trigger-first");
        request.setTriggerFlag(trigger);
        request.setState(state);
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
        ResultSnapshot snapshot = resultSnapshotService.create(sessionId, input.slugHint(), result);

        List<RenderableCta> ctas = resultCtaService.renderableCtas(result, sessionId, input.slugHint());

        model.addAttribute("result", result);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("ctaLinks", ctas);
        model.addAttribute("savedResultUrl", "/result/saved/" + snapshot.id());
        model.addAttribute("shareUrl", "/result/saved/" + snapshot.id());
        model.addAttribute("pdfUrl", "/result/saved/" + snapshot.id() + ".pdf");
        model.addAttribute("savedAtLabel", "just now");
        model.addAttribute("expiresLabel", snapshot.expiresAt().substring(0, 10));
        model.addAttribute("sharedView", false);

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
        analyticsEventService.logEvent(
                "result_snapshot_created",
                result.entryMode().wireValue(),
                sessionId,
                input.slugHint(),
                result.tier().label(),
                result.branch().label(),
                null,
                "/result/saved/" + snapshot.id(),
                "auto-save"
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

    private String preferredSlug(String slug, String fallback) {
        if (slug != null && !slug.isBlank()) {
            return slug;
        }
        return fallback;
    }

    private boolean isAllowedTarget(String target) {
        if (target == null || target.isBlank()) {
            return false;
        }
        String candidate = target.trim();
        try {
            URI uri = URI.create(candidate);
            if (!uri.isAbsolute()) {
                return candidate.startsWith("/") && !candidate.startsWith("//") && uri.getScheme() == null && uri.getHost() == null;
            }
            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                return false;
            }
            String host = uri.getHost();
            if (host == null || host.isBlank()) {
                return false;
            }
            return allowedOutboundHosts.contains(host.toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private void addSharedOptions(Model model) {
        model.addAttribute("analytes", decisionRegistryService.contaminantKeys());
        model.addAttribute("symptoms", decisionRegistryService.symptomKeys());
        model.addAttribute("triggers", decisionRegistryService.triggerKeys());
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
                "ro", "uv", "softener", "iron filter", "carbon", "sediment", "unknown"
        ));
        model.addAttribute("smellTypes", List.of("rotten-egg", "chemical", "musty", "other"));
        model.addAttribute("stainTypes", List.of("orange", "black", "blue-green"));
        model.addAttribute("tasteTypes", List.of("metallic", "salty", "bitter"));
        model.addAttribute("locationScopes", List.of("one-fixture", "hot-only", "cold-only", "whole-house", "unknown"));
        model.addAttribute("changeTimings", List.of("after-rain", "after-repair", "gradual", "sudden", "unknown"));
    }
}
