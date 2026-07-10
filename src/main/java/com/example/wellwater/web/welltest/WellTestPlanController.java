package com.example.wellwater.web.welltest;

import com.example.wellwater.web.page.SeoMetadataService;
import com.example.wellwater.welltest.PivotMetricService;
import com.example.wellwater.welltest.WellTestPlanResult;
import com.example.wellwater.welltest.WellTestPlanService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WellTestPlanController {

    private final WellTestPlanService wellTestPlanService;
    private final PivotMetricService pivotMetricService;
    private final SeoMetadataService seoMetadataService;

    public WellTestPlanController(
            WellTestPlanService wellTestPlanService,
            PivotMetricService pivotMetricService,
            SeoMetadataService seoMetadataService
    ) {
        this.wellTestPlanService = wellTestPlanService;
        this.pivotMetricService = pivotMetricService;
        this.seoMetadataService = seoMetadataService;
    }

    @GetMapping("/tool/test-plan")
    public String form() {
        return "redirect:/#test-plan";
    }

    @PostMapping("/tool/test-plan")
    public String create(
            @ModelAttribute WellTestPlanForm planForm,
            Model model,
            HttpServletResponse response
    ) {
        response.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
        long started = System.nanoTime();
        try {
            WellTestPlanResult result = wellTestPlanService.create(planForm.toRequest(), "web");
            pivotMetricService.record(
                    "tool_completed",
                    "web",
                    result.resultFamily(),
                    result.hasPartnerOffer() ? result.partnerOffer().productCode() : "",
                    "success",
                    elapsedMs(started)
            );
            model.addAttribute("result", result);
            model.addAttribute("planForm", planForm);
            model.addAttribute("error", "");
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pivotMetricService.record("tool_failed", "web", "", "", "invalid_input", elapsedMs(started));
            model.addAttribute("result", null);
            model.addAttribute("planForm", planForm);
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("seo", seoMetadataService.toolLanding(
                "Your Private-Well Test Plan | Water Verdict",
                "Review the focused private-well testing panel and certified lab path generated from your situation."
        ));
        return "pages/tool/test-plan";
    }

    private long elapsedMs(long started) {
        return Math.max(0L, (System.nanoTime() - started) / 1_000_000L);
    }
}
