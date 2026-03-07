package com.example.wellwater.web.page;

import com.example.wellwater.pseo.PseoCatalogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class PageController {

    private final PseoCatalogService pseoCatalogService;

    public PageController(PseoCatalogService pseoCatalogService) {
        this.pseoCatalogService = pseoCatalogService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("familyCounts", pseoCatalogService.familyCounts());
        model.addAttribute("featuredPages", pseoCatalogService.featured(16));
        return "pages/home";
    }

    @GetMapping("/well-water/family/{family}")
    public String family(@PathVariable String family, Model model, HttpServletResponse response) {
        var pages = pseoCatalogService.byFamily(family);
        if (pages.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/well-water/family/" + family);
            return "pages/not-found";
        }
        model.addAttribute("family", family);
        model.addAttribute("pages", pages);
        return "pages/pseo/list";
    }

    @GetMapping("/well-water/{slug}")
    public String detail(@PathVariable String slug, Model model, HttpServletResponse response) {
        var maybePage = pseoCatalogService.findBySlug(slug);
        if (maybePage.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/well-water/" + slug);
            return "pages/not-found";
        }
        model.addAttribute("page", maybePage.get());
        return "pages/pseo/detail";
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        return pseoCatalogService.sitemapXml(baseUrl);
    }
}
