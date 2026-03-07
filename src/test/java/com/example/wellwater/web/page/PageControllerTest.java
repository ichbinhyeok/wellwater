package com.example.wellwater.web.page;

import com.example.wellwater.pseo.PseoCatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageControllerTest {

    private final PageController controller = new PageController(
            new PseoCatalogService("./data/pseo/pages.csv")
    );

    @Test
    void homeRendersWithJteTemplate() {
        Model model = new ExtendedModelMap();
        String viewName = controller.home(model);

        assertEquals("pages/home", viewName);
        assertNotNull(model.getAttribute("familyCounts"));
        assertNotNull(model.getAttribute("featuredPages"));
    }

    @Test
    void familyRendersListViewWhenDataExists() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String viewName = controller.family("contaminants", model, response);

        assertEquals("pages/pseo/list", viewName);
        assertEquals(200, response.getStatus());
        assertNotNull(model.getAttribute("pages"));
    }

    @Test
    void detailRendersNotFoundWhenSlugMissing() {
        Model model = new ExtendedModelMap();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String viewName = controller.detail("missing-slug", model, response);

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
    }
}
