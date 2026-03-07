package com.example.wellwater.pseo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PseoExperienceServiceTest {

    private final PseoCatalogService catalogService = new PseoCatalogService("./data/pseo/pages.csv");
    private final PseoCitationRegistryService citationRegistryService = new PseoCitationRegistryService("./data/pseo/page_sources.csv");
    private final PseoExperienceService experienceService = new PseoExperienceService(catalogService, citationRegistryService);

    @Test
    void detailViewAddsDerivedMetadataAndRelatedSections() {
        PseoDetailView detailView = experienceService.detailView("nitrate").orElseThrow();

        assertEquals("Contaminant brief", detailView.archetypeLabel());
        assertEquals("Health / chemical", detailView.riskLabel());
        assertTrue(detailView.doNotBuyYet().contains("Do not buy"));
        assertFalse(detailView.quickAnswers().isEmpty());
        assertFalse(detailView.relatedSections().isEmpty());
        assertFalse(detailView.citations().isEmpty());
        assertTrue(detailView.relatedSections().stream().flatMap(section -> section.pages().stream()).anyMatch(page -> !page.slug().equals("nitrate")));
    }

    @Test
    void familyViewUsesHumanReadableHeroCopy() {
        PseoFamilyView familyView = experienceService.familyView("symptoms", catalogService.byFamily("symptoms"));

        assertEquals("Symptom-first troubleshooting guides", familyView.heroTitle());
        assertEquals("/tool/symptom-first", familyView.primaryToolHref());
    }

    @Test
    void regionalViewCarriesStateAwareEntryHintAndAuthorityFamilyCopy() {
        PseoDetailView detailView = experienceService.detailView("new-hampshire-arsenic-well-water").orElseThrow();
        PseoFamilyView familyView = experienceService.familyView("authority", catalogService.byFamily("authority"));

        assertEquals("Regional guide", detailView.archetypeLabel());
        assertTrue(detailView.entryHint().href().contains("state=NH"));
        assertEquals("Authority and methodology articles", familyView.heroTitle());
    }

    @Test
    void catalogNowSupportsFifteenRegionalPagesAndExpandedAuthorityCoverage() {
        assertEquals(15, catalogService.byFamily("regional").size());
        assertEquals(19, catalogService.byFamily("authority").size());
        assertTrue(catalogService.findBySlug("ro-vs-adsorptive-media-for-arsenic").isPresent());
        assertTrue(catalogService.findBySlug("radon-aeration-vs-gac").isPresent());
        assertTrue(catalogService.findBySlug("private-well-sampling-mistakes-that-break-results").isPresent());
        assertTrue(catalogService.findBySlug("new-jersey-pwta-vs-full-household-panel").isPresent());
        assertTrue(catalogService.findBySlug("new-york-pfas-private-well-testing-order").isPresent());
    }

    @Test
    void regionalPagesNowPullDedicatedAuthorityReads() {
        PseoDetailView california = experienceService.detailView("california-private-well-owner-guide").orElseThrow();
        PseoDetailView newJersey = experienceService.detailView("new-jersey-pwta-private-well-testing").orElseThrow();

        assertTrue(california.relatedSections().stream()
                .flatMap(section -> section.pages().stream())
                .anyMatch(page -> page.slug().equals("california-after-wildfire-private-well-checklist")));
        assertTrue(newJersey.relatedSections().stream()
                .flatMap(section -> section.pages().stream())
                .anyMatch(page -> page.slug().equals("new-jersey-pwta-vs-full-household-panel")));
    }
}
