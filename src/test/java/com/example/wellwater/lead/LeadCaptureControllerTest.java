package com.example.wellwater.lead;

import com.example.wellwater.analytics.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeadCaptureControllerTest {

    @TempDir
    Path tempDir;

    @Test
    void submitRedirectsBackToSourcePathOnSuccess() {
        LeadCaptureController controller = new LeadCaptureController(
                new LeadCaptureService(tempDir.resolve("leads.csv").toString()),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString())
        );
        LeadCaptureRequest request = new LeadCaptureRequest();
        request.setEmail("owner@example.com");
        request.setRedirectPath("/well-water/new-hampshire-arsenic-well-water");
        request.setSourceType("pseo-detail");
        request.setSourceFamily("regional");
        request.setSourceSlug("new-hampshire-arsenic-well-water");

        String redirect = controller.submit(request);

        assertEquals("redirect:/well-water/new-hampshire-arsenic-well-water?lead=success#lead-capture", redirect);
    }

    @Test
    void submitFallsBackToSafePathOnInvalidRedirectOrInput() {
        LeadCaptureController controller = new LeadCaptureController(
                new LeadCaptureService(tempDir.resolve("leads.csv").toString()),
                new AnalyticsEventService(tempDir.resolve("events.csv").toString())
        );
        LeadCaptureRequest request = new LeadCaptureRequest();
        request.setEmail("bad-email");
        request.setRedirectPath("https://evil.invalid");

        String redirect = controller.submit(request);

        assertEquals("redirect:/?lead=invalid#lead-capture", redirect);
    }
}
