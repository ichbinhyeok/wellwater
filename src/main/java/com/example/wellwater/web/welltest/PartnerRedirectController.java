package com.example.wellwater.web.welltest;

import com.example.wellwater.welltest.PartnerCatalogService;
import com.example.wellwater.welltest.PivotMetricService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PartnerRedirectController {

    private final PartnerCatalogService partnerCatalogService;
    private final PivotMetricService pivotMetricService;

    public PartnerRedirectController(PartnerCatalogService partnerCatalogService, PivotMetricService pivotMetricService) {
        this.partnerCatalogService = partnerCatalogService;
        this.pivotMetricService = pivotMetricService;
    }

    @GetMapping("/partner/tap-score/{product}")
    public RedirectView redirect(
            @PathVariable String product,
            @RequestParam(required = false) String source
    ) {
        String safeSource = "chatgpt".equalsIgnoreCase(source) ? "chatgpt" : ("web".equalsIgnoreCase(source) ? "web" : "tool");
        String target = partnerCatalogService.target(product).orElse("/tool/test-plan");
        String outcome = target.startsWith("https://") ? "redirect" : "unavailable";
        pivotMetricService.record("partner_clicked", safeSource, "", product, outcome, 0L);
        RedirectView view = new RedirectView(target);
        view.setExposeModelAttributes(false);
        return view;
    }
}
