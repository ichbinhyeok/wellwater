package com.example.wellwater.web.page;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.pseo.PseoCatalogService;
import com.example.wellwater.pseo.PseoExperienceService;
import com.example.wellwater.pseo.PseoPage;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Locale;

@Controller
public class PageController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    private final PseoCatalogService pseoCatalogService;
    private final PseoExperienceService pseoExperienceService;
    private final SeoMetadataService seoMetadataService;
    private final TrustPageService trustPageService;
    private final AnalyticsEventService analyticsEventService;
    private final PublicTrackingLinkService publicTrackingLinkService;

    public PageController(
            PseoCatalogService pseoCatalogService,
            PseoExperienceService pseoExperienceService,
            SeoMetadataService seoMetadataService,
            TrustPageService trustPageService,
            AnalyticsEventService analyticsEventService,
            PublicTrackingLinkService publicTrackingLinkService
    ) {
        this.pseoCatalogService = pseoCatalogService;
        this.pseoExperienceService = pseoExperienceService;
        this.seoMetadataService = seoMetadataService;
        this.trustPageService = trustPageService;
        this.analyticsEventService = analyticsEventService;
        this.publicTrackingLinkService = publicTrackingLinkService;
    }

    @ModelAttribute("trackingLinks")
    public PublicTrackingLinkService trackingLinks() {
        return publicTrackingLinkService;
    }

    @GetMapping("/")
    public String home(Model model) {
        trackPublicPageView("home", "", "home", "/", "home", "index,follow");
        model.addAttribute("seo", seoMetadataService.home(
                "Water Verdict | Find The Right Private-Well Test",
                "Choose the focused private-well testing panel and certified lab path for an annual check, visible clue, nearby risk, flood, or home purchase."
        ));
        return "pages/home";
    }

    @GetMapping("/tool")
    public String toolLanding(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model
    ) {
        return "redirect:/#test-plan";
    }

    @GetMapping("/well-water/family/{family}")
    public String family(
            @PathVariable String family,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model,
            HttpServletResponse response
    ) {
        var pages = pseoCatalogService.byFamily(family);
        if (pages.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/well-water/family/" + family);
            return "pages/not-found";
        }
        model.addAttribute("family", family);
        model.addAttribute("pages", pages);
        var familyView = pseoExperienceService.familyView(family, pages);
        model.addAttribute("familyView", familyView);
        var seo = seoMetadataService.family(family, familyView);
        trackPublicPageView("family", family, "family-hub", "/well-water/family/" + family, family, seo.robotsDirective());
        model.addAttribute("seo", seo);
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", null);
        return "pages/pseo/list";
    }

    @GetMapping("/well-water/{slug}")
    public String detail(
            @PathVariable String slug,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model,
            HttpServletResponse response
    ) {
        if (!pseoCatalogService.isCanonicalSlug(slug)) {
            return "redirect:/well-water/" + pseoCatalogService.canonicalSlug(slug);
        }
        var maybePageView = pseoExperienceService.detailView(slug);
        if (maybePageView.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/well-water/" + slug);
            return "pages/not-found";
        }
        var seo = seoMetadataService.detail(maybePageView.get());
        trackPublicPageView(
                "detail",
                maybePageView.get().page().slug(),
                detailSearchRole(maybePageView.get().page()),
                "/well-water/" + maybePageView.get().page().slug(),
                maybePageView.get().page().family(),
                seo.robotsDirective()
        );
        model.addAttribute("pageView", maybePageView.get());
        model.addAttribute("page", maybePageView.get().page());
        model.addAttribute("seo", seo);
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", null);
        return "pages/pseo/detail";
    }

    @GetMapping("/trust")
    public String trustHub(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model
    ) {
        trackPublicPageView("trust-hub", "trust", "trust", "/trust", "trust", "index,follow");
        model.addAttribute("trustPages", trustPageService.allPages());
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", null);
        model.addAttribute("seo", seoMetadataService.trustHub(
                "Trust And Method | Water Verdict",
                "Read the methodology, review policy, sources policy, and safety limits behind this private-well decision surface."
        ));
        return "pages/trust/list";
    }

    @GetMapping("/trust/{slug}")
    public String trustPage(
            @PathVariable String slug,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String lead,
            Model model,
            HttpServletResponse response
    ) {
        var maybePage = trustPageService.findBySlug(slug);
        if (maybePage.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/trust/" + slug);
            return "pages/not-found";
        }
        trackPublicPageView("trust-page", maybePage.get().slug(), "trust", "/trust/" + maybePage.get().slug(), "trust", "index,follow");
        model.addAttribute("page", maybePage.get());
        model.addAttribute("trustPages", trustPageService.allPages());
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", null);
        model.addAttribute("seo", seoMetadataService.trustPage(maybePage.get()));
        return "pages/trust/view";
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots() {
        String baseUrl = seoMetadataService.absolute("");
        return """
                User-agent: *
                Allow: /
                Disallow: /admin
                Disallow: /lead/
                Disallow: /result/
                Disallow: /tool/
                Disallow: /mcp
                Disallow: /partner/

                Sitemap: %s/sitemap.xml
                """.formatted(baseUrl);
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        String baseUrl = seoMetadataService.absolute("");
        ArrayList<String> extraPaths = new ArrayList<>(trustPageService.sitemapPaths());
        return pseoCatalogService.sitemapXml(baseUrl, extraPaths);
    }

    private String sanitizeLeadStatus(String lead) {
        if ("success".equalsIgnoreCase(lead)) {
            return "success";
        }
        if ("invalid".equalsIgnoreCase(lead)) {
            return "invalid";
        }
        return "";
    }

    private String detailSearchRole(PseoPage page) {
        return page.searchRole().name().toLowerCase(Locale.ROOT);
    }

    private void trackPublicPageView(String entryMode, String slug, String role, String targetUrl, String surface, String robotsDirective) {
        try {
            analyticsEventService.logEvent(
                    "public_page_view",
                    entryMode,
                    null,
                    slug,
                    role,
                    surface,
                    null,
                    targetUrl,
                    robotsDirective
            );
        } catch (RuntimeException e) {
            log.warn("Skipping public_page_view analytics for {} because event logging failed", targetUrl, e);
        }
    }
}
