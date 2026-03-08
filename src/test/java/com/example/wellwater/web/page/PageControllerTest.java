package com.example.wellwater.web.page;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import com.example.wellwater.pseo.PseoCatalogService;
import com.example.wellwater.pseo.PseoCitationRegistryService;
import com.example.wellwater.pseo.PseoDecisionDocService;
import com.example.wellwater.pseo.PseoExperienceService;
import com.example.wellwater.pseo.PseoFamilyView;
import com.example.wellwater.pseo.RegionalContextRegistryService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageControllerTest {

    private final PseoCatalogService catalogService = new PseoCatalogService("./data/pseo/pages.csv");
    private final PseoCitationRegistryService citationRegistryService = new PseoCitationRegistryService("./data/pseo/page_sources.csv");
    private final SeoMetadataService seoMetadataService = new SeoMetadataService("https://example.com", new MockEnvironment());
    private final TrustPageService trustPageService = new TrustPageService();
    private final AnalyticsEventService analyticsEventService = new AnalyticsEventService("./build/test-analytics/page-controller-events.csv");
    private final PageController controller = new PageController(
            catalogService,
            new PseoExperienceService(
                    catalogService,
                    citationRegistryService,
                    new PseoDecisionDocService(),
                    new RegionalContextRegistryService("./data/registry/regional_context_registry.csv"),
                    new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
            ),
            seoMetadataService,
            trustPageService,
            analyticsEventService
    );

    @Test
    void homeRendersWithJteTemplate() {
        Model model = new ExtendedModelMap();
        String viewName = controller.home(null, model);

        assertEquals("pages/home", viewName);
        assertNotNull(model.getAttribute("familyCounts"));
        assertNotNull(model.getAttribute("totalPageCount"));
        assertNotNull(model.getAttribute("priorityPages"));
        assertNotNull(model.getAttribute("featuredRegionalPages"));
        assertNotNull(model.getAttribute("trustPages"));
        assertNotNull(model.getAttribute("leadContext"));
        assertEquals("", model.getAttribute("leadStatus"));
        assertNotNull(model.getAttribute("seo"));
        @SuppressWarnings("unchecked")
        List<com.example.wellwater.pseo.PseoPage> featuredRegionalPages =
                (List<com.example.wellwater.pseo.PseoPage>) model.getAttribute("featuredRegionalPages");
        assertNotNull(featuredRegionalPages);
        assertEquals(4, featuredRegionalPages.size());
        assertEquals("north-carolina-private-well-water-faqs", featuredRegionalPages.get(0).slug());
    }

    @Test
    void familyRendersListViewWhenDataExists() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String viewName = controller.family("contaminants", null, model, response);

        assertEquals("pages/pseo/list", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("pages"));
        assertNotNull(model.getAttribute("familyView"));
        PseoFamilyView familyView = (PseoFamilyView) model.getAttribute("familyView");
        assertNotNull(familyView);
        assertEquals(3, familyView.starterPages().size());
    }

    @Test
    void detailRendersNotFoundWhenSlugMissing() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String viewName = controller.detail("missing-slug", null, model, response);

        assertEquals("pages/not-found", viewName);
        assertEquals(404, response.getStatus());
    }

    @Test
    void sitemapContainsWellWaterUrl() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("https");
        request.setServerName("example.com");
        request.setServerPort(443);
        request.setRequestURI("/sitemap.xml");

        String xml = controller.sitemap(request);
        assertTrue(xml.contains("/well-water/"));
        assertTrue(xml.contains("/trust/methodology"));
    }

    @Test
    void robotsDisallowNonIndexableAreasAndPointToSitemap() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("https");
        request.setServerName("example.com");
        request.setServerPort(443);
        request.setRequestURI("/robots.txt");

        String robots = controller.robots(request);

        assertTrue(robots.contains("Disallow: /admin"));
        assertTrue(robots.contains("Disallow: /tool/"));
        assertTrue(robots.contains("Sitemap: https://example.com/sitemap.xml"));
    }

    @Test
    void detailAddsPageViewModelWhenSlugExists() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.detail("nitrate", null, model, response);

        assertEquals("pages/pseo/detail", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("page"));
        assertNotNull(model.getAttribute("pageView"));
        assertNotNull(model.getAttribute("leadContext"));
    }

    @Test
    void detailRedirectsLegacyAliasSlugToCanonicalPage() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.detail("iron-filter-vs-softener", null, model, response);

        assertEquals("redirect:/well-water/softener-vs-iron-filter", viewName);
    }

    @Test
    void detailStillRendersWhenAnalyticsLoggingThrows() {
        AnalyticsEventService failingAnalytics = new AnalyticsEventService("./build/test-analytics/page-controller-events.csv") {
            @Override
            public synchronized void logEvent(
                    String eventName,
                    String entryMode,
                    String sessionId,
                    String slug,
                    String tier,
                    String branch,
                    String ctaType,
                    String targetUrl,
                    String note
            ) {
                throw new IllegalStateException("analytics write failed");
            }
        };

        PageController controllerWithFailingAnalytics = new PageController(
                catalogService,
                new PseoExperienceService(
                        catalogService,
                        citationRegistryService,
                        new PseoDecisionDocService(),
                        new RegionalContextRegistryService("./data/registry/regional_context_registry.csv"),
                        new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
                ),
                seoMetadataService,
                trustPageService,
                failingAnalytics
        );

        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = assertDoesNotThrow(
                () -> controllerWithFailingAnalytics.detail("rotten-egg-smell", null, model, response)
        );

        assertEquals("pages/pseo/detail", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("pageView"));
        assertNotNull(model.getAttribute("seo"));
    }

    @Test
    void trustHubRendersWithPages() {
        Model model = new ExtendedModelMap();

        String viewName = controller.trustHub(null, model);

        assertEquals("pages/trust/list", viewName);
        assertNotNull(model.getAttribute("trustPages"));
        assertNotNull(model.getAttribute("leadContext"));
        assertEquals("", model.getAttribute("leadStatus"));
        assertNotNull(model.getAttribute("seo"));
    }

    @Test
    void trustPageRendersWhenSlugExists() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.trustPage("methodology", null, model, response);

        assertEquals("pages/trust/view", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("page"));
        assertNotNull(model.getAttribute("leadContext"));
        assertEquals("", model.getAttribute("leadStatus"));
        assertNotNull(model.getAttribute("seo"));
    }
}
