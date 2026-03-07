package com.example.wellwater.web.tool;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.decision.DecisionEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolControllerTest {

    @TempDir
    Path tempDir;

    @Test
    void resultFirstViewLoads() {
        ToolController controller = new ToolController(
                new DecisionEngineService(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString())
        );
        Model model = new ExtendedModelMap();

        String viewName = controller.resultFirst("nitrate", model);

        assertEquals("pages/intake/result-first", viewName);
        assertNotNull(model.getAttribute("request"));
        assertNotNull(model.getAttribute("analytes"));
    }

    @Test
    void postResultProducesViewWithResultAndCtas() {
        ToolController controller = new ToolController(
                new DecisionEngineService(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString())
        );
        Model model = new ExtendedModelMap();
        ToolRequest request = new ToolRequest();
        request.setEntryMode("result-first");
        request.setAnalyteName("nitrate");
        request.setResultValue("14");
        request.setUnit("mg/L");
        request.setSampleSource("raw well");
        request.setLabCertified("yes");
        request.setInfantPresent(true);

        String viewName = controller.decisionResult(request, model);

        assertEquals("pages/result/view", viewName);
        assertNotNull(model.getAttribute("result"));
        assertNotNull(model.getAttribute("sessionId"));
        assertNotNull(model.getAttribute("ctaLinks"));
    }

    @Test
    void outboundRejectsUnsafeTarget() {
        ToolController controller = new ToolController(
                new DecisionEngineService(),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString())
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

        assertTrue(view.getUrl().equals("/"));
    }
}

