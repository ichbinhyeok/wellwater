package com.example.wellwater.web.page;

import com.example.wellwater.decision.registry.StateResourceRegistryService;
import com.example.wellwater.pseo.PseoCatalogService;
import com.example.wellwater.pseo.PseoCitationRegistryService;
import com.example.wellwater.pseo.PseoDecisionDocService;
import com.example.wellwater.pseo.PseoExperienceService;
import com.example.wellwater.pseo.RegionalContextRegistryService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeoMetadataServiceTest {

    private final PseoCatalogService catalogService = new PseoCatalogService("./data/pseo/pages.csv");
    private final PseoExperienceService experienceService = new PseoExperienceService(
            catalogService,
            new PseoCitationRegistryService("./data/pseo/page_sources.csv"),
            new PseoDecisionDocService(),
            new RegionalContextRegistryService("./data/registry/regional_context_registry.csv"),
            new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
    );
    private final SeoMetadataService seoMetadataService = new SeoMetadataService("https://waterverdict.example", new MockEnvironment());
    private final TrustPageService trustPageService = new TrustPageService();

    @Test
    void detailMetadataBuildsCanonicalAndBreadcrumbJson() {
        SeoMetadata metadata = seoMetadataService.detail(experienceService.detailView("new-hampshire-arsenic-well-water").orElseThrow());

        assertEquals("https://waterverdict.example/well-water/new-hampshire-arsenic-well-water", metadata.canonicalUrl());
        assertEquals(3, metadata.breadcrumbs().size());
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("BreadcrumbList")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"Article\"")));
    }

    @Test
    void decisionDocPagesEmitFaqStructuredData() {
        SeoMetadata metadata = seoMetadataService.detail(experienceService.detailView("nitrate").orElseThrow());

        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"FAQPage\"")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"Question\"")));
    }

    @Test
    void trustMetadataBuildsCanonicalAndAboutPageJson() {
        SeoMetadata metadata = seoMetadataService.trustPage(trustPageService.findBySlug("methodology").orElseThrow());

        assertEquals("https://waterverdict.example/trust/methodology", metadata.canonicalUrl());
        assertEquals(3, metadata.breadcrumbs().size());
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("BreadcrumbList")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"AboutPage\"")));
    }
}
