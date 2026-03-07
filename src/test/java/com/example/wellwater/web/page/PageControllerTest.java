package com.example.wellwater.web.page;

import com.example.wellwater.pseo.PseoCatalogService;
import com.example.wellwater.pseo.PseoCitationRegistryService;
import com.example.wellwater.pseo.PseoExperienceService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageControllerTest {

    private final PseoCatalogService catalogService = new PseoCatalogService("./data/pseo/pages.csv");
    private final PseoCitationRegistryService citationRegistryService = new PseoCitationRegistryService("./data/pseo/page_sources.csv");
    private final SeoMetadataService seoMetadataService = new SeoMetadataService("https://example.com");
    private final TrustPageService trustPageService = new TrustPageService();
    private final PageController controller = new PageController(
            catalogService,
            new PseoExperienceService(catalogService, citationRegistryService),
            seoMetadataService,
            trustPageService
    );

    @Test
    void homeRendersWithJteTemplate() {
        Model model = new ExtendedModelMap();
        String viewName = controller.home(model);

        assertEquals("pages/home", viewName);
        assertNotNull(model.getAttribute("familyCounts"));
        assertNotNull(model.getAttribute("totalPageCount"));
        assertNotNull(model.getAttribute("featuredPages"));
        assertNotNull(model.getAttribute("trustPages"));
        assertNotNull(model.getAttribute("seo"));
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
    void trustHubRendersWithPages() {
        Model model = new ExtendedModelMap();

        String viewName = controller.trustHub(model);

        assertEquals("pages/trust/list", viewName);
        assertNotNull(model.getAttribute("trustPages"));
        assertNotNull(model.getAttribute("seo"));
    }

    @Test
    void trustPageRendersWhenSlugExists() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = controller.trustPage("methodology", model, response);

        assertEquals("pages/trust/view", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("page"));
        assertNotNull(model.getAttribute("seo"));
    }
}
