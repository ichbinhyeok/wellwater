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
        assertEquals("index,follow", metadata.robotsDirective());
        assertEquals(3, metadata.breadcrumbs().size());
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("BreadcrumbList")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"Article\"")));
    }

    @Test
    void decisionDocPagesEmitFaqStructuredData() {
        SeoMetadata metadata = seoMetadataService.detail(experienceService.detailView("nitrate").orElseThrow());

        assertEquals("index,follow", metadata.robotsDirective());
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"FAQPage\"")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"Question\"")));
    }

    @Test
    void comparePagesAndBroadFamilyHubsCanBeNoindexed() {
        SeoMetadata compareMetadata = seoMetadataService.detail(experienceService.detailView("ro-vs-adsorptive-media-for-arsenic").orElseThrow());
        SeoMetadata familyMetadata = seoMetadataService.family("compares", experienceService.familyView("compares", catalogService.byFamily("compares")));
        SeoMetadata contaminantsMetadata = seoMetadataService.family("contaminants", experienceService.familyView("contaminants", catalogService.byFamily("contaminants")));

        assertEquals("noindex,follow", compareMetadata.robotsDirective());
        assertEquals("noindex,follow", familyMetadata.robotsDirective());
        assertEquals("noindex,follow", contaminantsMetadata.robotsDirective());
    }

    @Test
    void trustMetadataBuildsCanonicalAndAboutPageJson() {
        SeoMetadata metadata = seoMetadataService.trustPage(trustPageService.findBySlug("methodology").orElseThrow());

        assertEquals("https://waterverdict.example/trust/methodology", metadata.canonicalUrl());
        assertEquals("index,follow", metadata.robotsDirective());
        assertEquals(3, metadata.breadcrumbs().size());
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("BreadcrumbList")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"AboutPage\"")));
    }

    @Test
    void toolLandingMetadataBuildsCanonicalAndToolEntityJson() {
        SeoMetadata metadata = seoMetadataService.toolLanding(
                "Private Well Water Decision Tool | Water Verdict",
                "Start with a lab result, symptom, recent change, or state and home-sale context and get the next testing path for a private well."
        );

        assertEquals("https://waterverdict.example/tool", metadata.canonicalUrl());
        assertEquals("index,follow", metadata.robotsDirective());
        assertEquals(2, metadata.breadcrumbs().size());
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"Private well decision tool\"")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("\"Well-water symptom triage\"")));
        assertTrue(metadata.structuredDataBlocks().stream().anyMatch(block -> block.contains("BreadcrumbList")));
    }
}
