package com.example.wellwater.web.tool;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.decision.DecisionEngineService;
import com.example.wellwater.decision.normalize.DecisionInputNormalizationService;
import com.example.wellwater.decision.registry.CostRegistryService;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import com.example.wellwater.web.result.ResultCtaService;
import com.example.wellwater.web.result.ResultSnapshotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolControllerTest {

    @TempDir
    Path tempDir;

    private DecisionEngineService newDecisionEngine() {
        DecisionRegistryService registryService = new DecisionRegistryService(
                "./data/registry/contaminant_registry.csv",
                "./data/registry/symptom_registry.csv",
                "./data/registry/trigger_registry.csv"
        );
        return new DecisionEngineService(
                registryService,
                new DecisionInputNormalizationService(registryService),
                new CostRegistryService("./data/registry/cost_registry.csv"),
                new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
        );
    }

    private StateResourceRegistryService newStateResourceRegistry() {
        return new StateResourceRegistryService("./data/registry/state_resource_registry.csv");
    }

    private ResultSnapshotService newSnapshotService() {
        return new ResultSnapshotService(
                tempDir.resolve("snapshots").toString(),
                30
        );
    }

    @Test
    void resultFirstViewLoads() {
        DecisionRegistryService registryService = new DecisionRegistryService(
                "./data/registry/contaminant_registry.csv",
                "./data/registry/symptom_registry.csv",
                "./data/registry/trigger_registry.csv"
        );
        ToolController controller = new ToolController(
                newDecisionEngine(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString()),
                registryService,
                newStateResourceRegistry(),
                new ResultCtaService(),
                newSnapshotService()
        );
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.resultFirst("nitrate", "PA", "nitrate", model, response);

        assertEquals("pages/intake/result-first", viewName);
        assertNotNull(model.getAttribute("request"));
        assertNotNull(model.getAttribute("analytes"));
        assertNotNull(model.getAttribute("states"));
        assertNotNull(model.getAttribute("reportSupportSignals"));
        assertEquals("noindex, nofollow, noarchive", response.getHeader("X-Robots-Tag"));
        assertTrue(((java.util.List<?>) model.getAttribute("analytes")).contains("radon"));
        assertEquals("PA", ((ToolRequest) model.getAttribute("request")).getState());
        assertEquals(2, ((ToolRequest) model.getAttribute("request")).getCompanionLinesForView().size());
    }

    @Test
    void postResultProducesViewWithResultAndCtas() {
        DecisionRegistryService registryService = new DecisionRegistryService(
                "./data/registry/contaminant_registry.csv",
                "./data/registry/symptom_registry.csv",
                "./data/registry/trigger_registry.csv"
        );
        ToolController controller = new ToolController(
                newDecisionEngine(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString()),
                registryService,
                newStateResourceRegistry(),
                new ResultCtaService(),
                newSnapshotService()
        );
        Model model = new ExtendedModelMap();
        ToolRequest request = new ToolRequest();
        request.setEntryMode("result-first");
        request.setAnalyteName("nitrate");
        request.setResultValue("14");
        request.setUnit("mg/L");
        request.setQualifier("none");
        request.setSampleDate("2026-02-20");
        request.setSampleSource("raw well");
        request.setLabCertified("yes");
        request.setState("TX");
        request.setUseScope("drinking-only");
        request.setExistingTreatment("none");
        request.setInfantPresent(true);
        CompanionReportLineForm line = new CompanionReportLineForm();
        line.setAnalyteName("total coliform");
        line.setResultValue("positive");
        line.setUnit("presence/absence");
        line.setQualifier("positive");
        request.setCompanionLines(List.of(line));
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.decisionResult(request, model, response);

        assertEquals("pages/result/view", viewName);
        assertNotNull(model.getAttribute("result"));
        assertNotNull(model.getAttribute("sessionId"));
        assertNotNull(model.getAttribute("ctaLinks"));
        assertNotNull(model.getAttribute("leadContext"));
        assertEquals("noindex, nofollow, noarchive", response.getHeader("X-Robots-Tag"));
        assertTrue(((String) model.getAttribute("savedResultUrl")).startsWith("/result/saved/"));
        assertTrue(((String) model.getAttribute("pdfUrl")).endsWith(".pdf"));
    }

    @Test
    void outboundRejectsUnsafeTarget() {
        DecisionRegistryService registryService = new DecisionRegistryService(
                "./data/registry/contaminant_registry.csv",
                "./data/registry/symptom_registry.csv",
                "./data/registry/trigger_registry.csv"
        );
        ToolController controller = new ToolController(
                newDecisionEngine(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString()),
                registryService,
                newStateResourceRegistry(),
                new ResultCtaService(),
                newSnapshotService()
        );

        RedirectView view = controller.outbound(
                "javascript:alert(1)",
                "category_compare",
                "result-first",
                "session-1",
                "Tier A",
                "Green",
                "nitrate"
        );

        assertEquals("/", view.getUrl());
    }

    @Test
    void outboundRejectsArbitraryExternalTarget() {
        DecisionRegistryService registryService = new DecisionRegistryService(
                "./data/registry/contaminant_registry.csv",
                "./data/registry/symptom_registry.csv",
                "./data/registry/trigger_registry.csv"
        );
        ToolController controller = new ToolController(
                newDecisionEngine(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString()),
                registryService,
                newStateResourceRegistry(),
                new ResultCtaService(),
                newSnapshotService()
        );

        RedirectView view = controller.outbound(
                "https://example.com/not-allowed",
                "local_guidance",
                "result-first",
                "session-2",
                "Tier A",
                "Green",
                "nitrate"
        );

        assertEquals("/", view.getUrl());
    }

    @Test
    void outboundRejectsProtocolRelativeTarget() {
        DecisionRegistryService registryService = new DecisionRegistryService(
                "./data/registry/contaminant_registry.csv",
                "./data/registry/symptom_registry.csv",
                "./data/registry/trigger_registry.csv"
        );
        ToolController controller = new ToolController(
                newDecisionEngine(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString()),
                registryService,
                newStateResourceRegistry(),
                new ResultCtaService(),
                newSnapshotService()
        );

        RedirectView view = controller.outbound(
                "//example.com/not-allowed",
                "local_guidance",
                "result-first",
                "session-3",
                "Tier A",
                "Green",
                "nitrate"
        );

        assertEquals("/", view.getUrl());
    }

    @Test
    void outboundAllowsKnownStateGuidanceUrl() {
        DecisionRegistryService registryService = new DecisionRegistryService(
                "./data/registry/contaminant_registry.csv",
                "./data/registry/symptom_registry.csv",
                "./data/registry/trigger_registry.csv"
        );
        ToolController controller = new ToolController(
                newDecisionEngine(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString()),
                registryService,
                newStateResourceRegistry(),
                new ResultCtaService(),
                newSnapshotService()
        );
        String target = "https://www.pa.gov/agencies/dep/residents/my-water/private-wells/water-testing";

        RedirectView view = controller.outbound(
                target,
                "local_guidance",
                "result-first",
                "session-4",
                "Tier A",
                "Green",
                "nitrate"
        );

        assertEquals(target, view.getUrl());
    }
}
