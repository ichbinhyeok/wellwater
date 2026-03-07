package com.example.wellwater.lead;

import com.example.wellwater.analytics.AnalyticsEventService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LeadCaptureController {

    private final LeadCaptureService leadCaptureService;
    private final AnalyticsEventService analyticsEventService;

    public LeadCaptureController(LeadCaptureService leadCaptureService, AnalyticsEventService analyticsEventService) {
        this.leadCaptureService = leadCaptureService;
        this.analyticsEventService = analyticsEventService;
    }

    @PostMapping("/lead/submit")
    public String submit(@ModelAttribute("leadRequest") LeadCaptureRequest request) {
        Optional<LeadCaptureService.LeadRecord> record = leadCaptureService.submit(request);
        if (record.isPresent()) {
            analyticsEventService.logEvent(
                    "lead_submitted",
                    request.getSourceType(),
                    null,
                    request.getSourceSlug(),
                    null,
                    null,
                    "lead_capture",
                    "",
                    request.getSourceFamily()
            );
            return "redirect:" + redirectPath(request.getRedirectPath(), "success");
        }
        return "redirect:" + redirectPath(request.getRedirectPath(), "invalid");
    }

    private String redirectPath(String candidate, String status) {
        String path = (candidate == null || candidate.isBlank()) ? "/" : candidate.trim();
        if (!path.startsWith("/") || path.startsWith("//")) {
            path = "/";
        }
        String separator = path.contains("?") ? "&" : "?";
        return path + separator + "lead=" + status + "#lead-capture";
    }
}
