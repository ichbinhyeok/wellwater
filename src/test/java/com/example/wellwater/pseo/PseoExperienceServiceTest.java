package com.example.wellwater.pseo;

import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PseoExperienceServiceTest {

    private final PseoCatalogService catalogService = new PseoCatalogService("./data/pseo/pages.csv");
    private final PseoCitationRegistryService citationRegistryService = new PseoCitationRegistryService("./data/pseo/page_sources.csv");
    private final PseoDecisionDocService decisionDocService = new PseoDecisionDocService();
    private final PseoExperienceService experienceService = new PseoExperienceService(
            catalogService,
            citationRegistryService,
            decisionDocService,
            new RegionalContextRegistryService("./data/registry/regional_context_registry.csv"),
            new StateResourceRegistryService("./data/registry/state_resource_registry.csv")
    );

    @Test
    void detailViewAddsDerivedMetadataAndRelatedSections() {
        PseoDetailView detailView = experienceService.detailView("nitrate").orElseThrow();

        assertEquals("Contaminant brief", detailView.archetypeLabel());
        assertEquals("Health / chemical", detailView.riskLabel());
        assertTrue(detailView.doNotBuyYet().contains("Do not buy"));
        assertFalse(detailView.quickAnswers().isEmpty());
        assertTrue(detailView.decisionDoc() != null);
        assertFalse(detailView.relatedSections().isEmpty());
        assertFalse(detailView.citations().isEmpty());
        assertTrue(detailView.relatedSections().stream().flatMap(section -> section.pages().stream()).anyMatch(page -> !page.slug().equals("nitrate")));
    }

    @Test
    void familyViewUsesHumanReadableHeroCopy() {
        PseoFamilyView familyView = experienceService.familyView("symptoms", catalogService.byFamily("symptoms"));

        assertEquals("Well water smell, stain, and taste guides", familyView.heroTitle());
        assertEquals("/tool/symptom-first", familyView.primaryToolHref());
        assertEquals(3, familyView.starterPages().size());
        assertEquals(3, familyView.commonMistakes().size());
        assertEquals(3, familyView.beforeToolChecks().size());
    }

    @Test
    void regionalViewCarriesStateAwareEntryHintAndAuthorityFamilyCopy() {
        PseoDetailView detailView = experienceService.detailView("new-hampshire-arsenic-well-water").orElseThrow();
        PseoFamilyView familyView = experienceService.familyView("authority", catalogService.byFamily("authority"));

        assertEquals("Regional guide", detailView.archetypeLabel());
        assertTrue(detailView.entryHint().href().contains("state=NH"));
        assertTrue(detailView.regionalContext() != null);
        assertEquals("New Hampshire", detailView.regionalContext().stateName());
        assertTrue(detailView.regionalContext().guidanceUrl().contains("des.nh.gov"));
        assertEquals("Well water testing and decision articles", familyView.heroTitle());
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
        assertTrue(catalogService.findBySlug("iron-filter-vs-softener").isPresent());
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

    @Test
    void regionalPagesExposeStateGuidanceAndLabCitations() {
        PseoDetailView detailView = experienceService.detailView("texas-private-well-sampling-testing").orElseThrow();

        assertEquals(3, detailView.citations().size());
        assertTrue(detailView.citations().stream().anyMatch(citation -> citation.label().equals("TX testing guidance")));
        assertTrue(detailView.citations().stream().anyMatch(citation -> citation.label().equals("TX certified lab path")));
        assertFalse(detailView.citations().stream().anyMatch(citation -> citation.label().equals("Primary official source")));
    }

    @Test
    void coreDecisionDocsCoverTwelveHighIntentPages() {
        List<String> slugs = List.of(
                "rotten-egg-smell",
                "orange-stains",
                "black-stains",
                "cloudy-water",
                "metallic-taste",
                "after-flood",
                "after-repair",
                "home-purchase-test",
                "retest-after-treatment",
                "nitrate",
                "coliform",
                "e-coli",
                "arsenic"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertTrue(detailView.decisionDoc() != null, slug);
            assertFalse(detailView.decisionDoc().faqs().isEmpty(), slug);
            assertFalse(detailView.decisionDoc().commonConfusions().isEmpty(), slug);
        }
    }

    @Test
    void priorityPagesPreferManualHighIntentOrder() {
        List<PseoPage> priorityPages = experienceService.priorityPages(6);

        assertEquals(6, priorityPages.size());
        assertEquals("rotten-egg-smell", priorityPages.get(0).slug());
        assertEquals("nitrate", priorityPages.get(1).slug());
        assertEquals("coliform", priorityPages.get(2).slug());
    }

    @Test
    void secondWaveDecisionDocsCoverNextTwelvePages() {
        List<String> slugs = List.of(
                "lead",
                "pfas",
                "radon",
                "ph",
                "blue-green-stains",
                "sulfur-smell-hot-water",
                "after-heavy-rain",
                "new-baby-at-home",
                "pregnancy-in-home",
                "test-kit-vs-certified-lab",
                "mail-in-lab-vs-local-certified-lab",
                "private-well-sampling-mistakes-that-break-results"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertTrue(detailView.decisionDoc() != null, slug);
            assertFalse(detailView.decisionDoc().faqs().isEmpty(), slug);
            assertFalse(detailView.decisionDoc().nextSteps().isEmpty(), slug);
        }
    }

    @Test
    void authorityAndRegionalOrganicPagesNowExposeDecisionDocs() {
        List<String> slugs = List.of(
                "new-hampshire-arsenic-well-water",
                "florida-rotten-egg-smell-well-water",
                "iowa-nitrate-baby-well-water",
                "connecticut-low-ph-blue-green-stains",
                "pennsylvania-private-well-radon",
                "new-jersey-pwta-private-well-testing",
                "how-to-read-a-well-water-lab-report",
                "private-well-home-sale-testing-by-state"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertTrue(detailView.decisionDoc() != null, slug);
            assertFalse(detailView.decisionDoc().faqs().isEmpty(), slug);
            assertFalse(detailView.decisionDoc().decisionSplits().isEmpty(), slug);
        }
    }

    @Test
    void winnerDecisionDocsExposeDecisionSplitsAndEscalationSignals() {
        List<String> slugs = List.of(
                "rotten-egg-smell",
                "orange-stains",
                "black-stains",
                "cloudy-water",
                "metallic-taste",
                "after-flood",
                "after-repair",
                "home-purchase-test",
                "retest-after-treatment",
                "nitrate",
                "coliform",
                "e-coli",
                "arsenic",
                "lead",
                "pfas",
                "radon",
                "ph",
                "blue-green-stains",
                "sulfur-smell-hot-water",
                "after-heavy-rain",
                "new-baby-at-home",
                "pregnancy-in-home",
                "new-jersey-pwta-private-well-testing",
                "how-to-read-a-well-water-lab-report",
                "private-well-home-sale-testing-by-state",
                "test-kit-vs-certified-lab",
                "mail-in-lab-vs-local-certified-lab",
                "private-well-sampling-mistakes-that-break-results"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertFalse(detailView.decisionDoc().decisionSplits().isEmpty(), slug);
            assertFalse(detailView.decisionDoc().escalationSignals().isEmpty(), slug);
        }
    }
}
