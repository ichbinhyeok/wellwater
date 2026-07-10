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
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageControllerTest {

    @TempDir
    Path tempDir;

    private final PseoCatalogService catalogService = new PseoCatalogService("./data/pseo/pages.csv");
    private final PseoCitationRegistryService citationRegistryService = new PseoCitationRegistryService("./data/pseo/page_sources.csv");
    private final SeoMetadataService seoMetadataService = new SeoMetadataService("https://example.com", new MockEnvironment());
    private final TrustPageService trustPageService = new TrustPageService();

    private PseoExperienceService newExperienceService() {
        return new PseoExperienceService(
                catalogService,
                citationRegistryService,
                new PseoDecisionDocService(),
                new RegionalContextRegistryService("./data/registry/regional_context_registry.csv"),
                new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
        );
    }

    private PageController newController(Path analyticsPath) {
        return new PageController(
                catalogService,
                newExperienceService(),
                seoMetadataService,
                trustPageService,
                new AnalyticsEventService(analyticsPath.toString()),
                new PublicTrackingLinkService()
        );
    }

    @Test
    void homeRendersWithJteTemplate() {
        PageController controller = newController(tempDir.resolve("events-home.csv"));
        Model model = new ExtendedModelMap();
        String viewName = controller.home(model);

        assertEquals("pages/home", viewName);
        assertNotNull(model.getAttribute("seo"));
        assertEquals(1, model.asMap().size());
    }

    @Test
    void familyRendersListViewWhenDataExists() {
        PageController controller = newController(tempDir.resolve("events-family.csv"));
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
        SeoMetadata seo = (SeoMetadata) model.getAttribute("seo");
        assertEquals("noindex,follow", seo.robotsDirective());
    }

    @Test
    void legacyToolLandingRedirectsToTheSingleToolSurface() {
        PageController controller = newController(tempDir.resolve("events-tool.csv"));
        Model model = new ExtendedModelMap();

        String viewName = controller.toolLanding(null, model);

        assertEquals("redirect:/#test-plan", viewName);
    }

    @Test
    void detailRendersNotFoundWhenSlugMissing() {
        PageController controller = newController(tempDir.resolve("events-not-found.csv"));
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String viewName = controller.detail("missing-slug", null, model, response);

        assertEquals("pages/not-found", viewName);
        assertEquals(404, response.getStatus());
    }

    @Test
    void sitemapContainsWellWaterUrl() {
        PageController controller = newController(tempDir.resolve("events-sitemap.csv"));
        String xml = controller.sitemap();
        assertTrue(xml.contains("/well-water/"));
        assertTrue(xml.contains("/trust/methodology"));
        assertTrue(xml.contains("/well-water/new-jersey-pwta-private-well-testing"));
        assertTrue(xml.contains("/well-water/private-well-home-sale-testing-by-state"));
        assertTrue(xml.contains("/well-water/hardness"));
        assertTrue(xml.contains("/well-water/rotten-egg-smell"));
        assertTrue(!xml.contains("<loc>https://example.com/tool</loc>"));
        assertTrue(xml.contains("/well-water/family/regional"));
        assertTrue(xml.contains("/well-water/family/authority"));
        assertTrue(xml.contains("/well-water/family/triggers"));
        assertFalse(xml.contains("/well-water/family/contaminants"));
        assertFalse(xml.contains("/well-water/family/symptoms"));
        assertFalse(xml.contains("/well-water/uv-vs-ro"));
        assertFalse(xml.contains("/well-water/family/compares"));
    }

    @Test
    void robotsDisallowNonIndexableAreasAndPointToSitemap() {
        PageController controller = newController(tempDir.resolve("events-robots.csv"));
        String robots = controller.robots();

        assertTrue(robots.contains("Disallow: /admin"));
        assertTrue(robots.contains("Disallow: /tool/"));
        assertTrue(robots.contains("Disallow: /mcp"));
        assertTrue(robots.contains("Disallow: /partner/"));
        assertTrue(robots.contains("Sitemap: https://example.com/sitemap.xml"));
    }

    @Test
    void detailAddsPageViewModelWhenSlugExists() {
        PageController controller = newController(tempDir.resolve("events-detail.csv"));
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.detail("nitrate", null, model, response);

        assertEquals("pages/pseo/detail", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("page"));
        assertNotNull(model.getAttribute("pageView"));
        assertEquals(null, model.getAttribute("leadContext"));
        SeoMetadata seo = (SeoMetadata) model.getAttribute("seo");
        assertEquals("index,follow", seo.robotsDirective());
    }

    @Test
    void recoveredPagesCanStayIndexableWhileComparePagesStayNoindexed() {
        PageController controller = newController(tempDir.resolve("events-recovered.csv"));
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.detail("hardness", null, model, response);

        assertEquals("pages/pseo/detail", viewName);
        assertEquals(200, response.getStatus());
        SeoMetadata seo = (SeoMetadata) model.getAttribute("seo");
        assertEquals("index,follow", seo.robotsDirective());
    }

    @Test
    void compareDetailPagesStayPublicButNoindexed() {
        PageController controller = newController(tempDir.resolve("events-compare.csv"));
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.detail("uv-vs-ro", null, model, response);

        assertEquals("pages/pseo/detail", viewName);
        assertEquals(200, response.getStatus());
        SeoMetadata seo = (SeoMetadata) model.getAttribute("seo");
        assertEquals("noindex,follow", seo.robotsDirective());
    }

    @Test
    void detailRedirectsLegacyAliasSlugToCanonicalPage() {
        PageController controller = newController(tempDir.resolve("events-redirect.csv"));
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
                newExperienceService(),
                seoMetadataService,
                trustPageService,
                failingAnalytics,
                new PublicTrackingLinkService()
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
        PageController controller = newController(tempDir.resolve("events-trust-hub.csv"));
        Model model = new ExtendedModelMap();

        String viewName = controller.trustHub(null, model);

        assertEquals("pages/trust/list", viewName);
        assertNotNull(model.getAttribute("trustPages"));
        assertEquals(null, model.getAttribute("leadContext"));
        assertEquals("", model.getAttribute("leadStatus"));
        assertNotNull(model.getAttribute("seo"));
    }

    @Test
    void trustPageRendersWhenSlugExists() {
        PageController controller = newController(tempDir.resolve("events-trust-page.csv"));
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.trustPage("methodology", null, model, response);

        assertEquals("pages/trust/view", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("page"));
        assertEquals(null, model.getAttribute("leadContext"));
        assertEquals("", model.getAttribute("leadStatus"));
        assertNotNull(model.getAttribute("seo"));
    }

    @Test
    void detailPageViewAnalyticsUseSearchRoleAndFamilySurface() throws Exception {
        Path analyticsPath = tempDir.resolve("events-role.csv");
        PageController controller = newController(analyticsPath);
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        controller.detail("nitrate", null, model, response);

        String csv = Files.readString(analyticsPath);
        assertTrue(csv.contains("public_page_view"));
        assertTrue(csv.contains(",nitrate,support,contaminants,"));
        assertTrue(csv.contains("/well-water/nitrate"));
        assertTrue(csv.contains("index,follow"));
    }
}
