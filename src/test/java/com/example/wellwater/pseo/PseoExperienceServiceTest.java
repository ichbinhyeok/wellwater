package com.example.wellwater.pseo;

import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

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
        assertTrue(detailView.entryHint().label().contains("NH"));
        assertTrue(detailView.regionalContext() != null);
        assertEquals("New Hampshire", detailView.regionalContext().stateName());
        assertTrue(detailView.regionalContext().guidanceUrl().contains("des.nh.gov"));
        assertTrue(detailView.riskSummary().contains("New Hampshire"));
        assertTrue(detailView.doNotBuyYet().contains("New Hampshire"));
        assertTrue(detailView.quickAnswers().stream().anyMatch(answer -> answer.title().equals("What NH changes")));
        assertEquals("Well water testing and decision articles", familyView.heroTitle());
    }

    @Test
    void regionalFamilyViewHighlightsOfficialStateCoverage() {
        PseoFamilyView familyView = experienceService.familyView("regional", catalogService.byFamily("regional"));

        assertTrue(familyView.operatorNote().contains("official guidance"));
        assertTrue(familyView.operatorNote().contains("states"));
    }

    @Test
    void catalogNowSupportsExpandedRegionalPagesAndAuthorityCoverage() {
        assertEquals(22, catalogService.byFamily("regional").size());
        assertEquals(19, catalogService.byFamily("authority").size());
        assertTrue(catalogService.findBySlug("ro-vs-adsorptive-media-for-arsenic").isPresent());
        assertTrue(catalogService.findBySlug("radon-aeration-vs-gac").isPresent());
        assertTrue(catalogService.findBySlug("private-well-sampling-mistakes-that-break-results").isPresent());
        assertTrue(catalogService.findBySlug("new-jersey-pwta-vs-full-household-panel").isPresent());
        assertTrue(catalogService.findBySlug("new-york-pfas-private-well-testing-order").isPresent());
        assertTrue(catalogService.findBySlug("iron-filter-vs-softener").isPresent());
        assertTrue(catalogService.findBySlug("north-carolina-private-well-water-faqs").isPresent());
        assertTrue(catalogService.findBySlug("oregon-private-well-testing-recommendations").isPresent());
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
        assertTrue(detailView.regionalContext() != null);
        assertTrue(detailView.regionalContext().sourceUrl().contains("twdb.texas.gov"));
    }

    @Test
    void newStateRegionalPagesExposeDecisionDocsAndStateAwareCitations() {
        List<String> slugs = List.of(
                "north-carolina-private-well-water-faqs",
                "virginia-private-well-testing-program",
                "indiana-well-water-quality-testing",
                "georgia-private-well-water-guidance",
                "south-carolina-well-water-quality-testing",
                "oregon-private-well-testing-recommendations",
                "washington-private-well-water-testing"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertTrue(detailView.regionalContext() != null, slug);
            assertTrue(detailView.entryHint().href().contains("state=" + detailView.regionalContext().stateCode()), slug);
            assertTrue(detailView.citations().size() >= 2, slug);
            assertTrue(detailView.decisionDoc() != null, slug);
            assertFalse(detailView.decisionDoc().decisionSplits().isEmpty(), slug);
        }
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
    void featuredRegionalPagesPreferNewATierLaunchPages() {
        List<String> slugs = experienceService.featuredRegionalPages().stream()
                .map(PseoPage::slug)
                .collect(Collectors.toList());

        assertEquals(List.of(
                "north-carolina-private-well-water-faqs",
                "virginia-private-well-testing-program",
                "oregon-private-well-testing-recommendations",
                "washington-private-well-water-testing"
        ), slugs);
    }

    @Test
    void regionalFamilyViewNowStartsWithFourATierStatePages() {
        PseoFamilyView familyView = experienceService.familyView("regional", catalogService.byFamily("regional"));
        List<String> slugs = familyView.starterPages().stream()
                .map(PseoPage::slug)
                .collect(Collectors.toList());

        assertEquals(4, familyView.starterPages().size());
        assertEquals(List.of(
                "north-carolina-private-well-water-faqs",
                "virginia-private-well-testing-program",
                "oregon-private-well-testing-recommendations",
                "washington-private-well-water-testing"
        ), slugs);
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
                "new-york-pfas-private-wells",
                "how-to-read-a-well-water-lab-report",
                "private-well-home-sale-testing-by-state",
                "private-well-testing-schedule-by-household",
                "sulfur-smell-hot-water-vs-whole-house"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertTrue(detailView.decisionDoc() != null, slug);
            assertFalse(detailView.decisionDoc().faqs().isEmpty(), slug);
            assertFalse(detailView.decisionDoc().decisionSplits().isEmpty(), slug);
        }
    }

    @Test
    void allATierPagesNowCarryDecisionDocsAndMultipleOfficialCitations() {
        for (PseoPage page : catalogService.allPages()) {
            if (!"A".equals(page.normalizedTier())) {
                continue;
            }
            PseoDetailView detailView = experienceService.detailView(page.slug()).orElseThrow();
            assertTrue(detailView.decisionDoc() != null, page.slug());
            assertTrue(detailView.citations().size() >= 2, page.slug());
        }
    }

    @Test
    void selectedBTierPagesNowCarryDecisionDocsAndMultipleOfficialCitations() {
        List<String> slugs = List.of(
                "hardness",
                "uranium",
                "nitrite",
                "copper",
                "softener-vs-iron-filter",
                "whole-house-vs-under-sink-ro",
                "carbon-vs-ro",
                "uv-vs-chlorination",
                "point-of-entry-vs-point-of-use",
                "california-private-well-owner-guide",
                "texas-private-well-sampling-testing",
                "how-to-verify-water-treatment-claims"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertTrue(detailView.decisionDoc() != null, slug);
            assertTrue(detailView.citations().size() >= 2, slug);
        }
    }

    @Test
    void secondWaveBTierPagesNowCarryWinnerDocsAndMultipleOfficialCitations() {
        List<String> slugs = List.of(
                "acid-neutralizer-vs-soda-ash",
                "after-boil-water-advisory",
                "air-injection-vs-oxidizing-filter",
                "arsenic-bedrock-testing-checklist",
                "home-sale-private-well-testing-checklist",
                "low-ph-copper-corrosion-testing-order",
                "nitrate-baby-pregnancy-well-water-checklist",
                "pfas-private-well-filter-claim-checklist",
                "ro-vs-adsorptive-media-for-arsenic",
                "shock-vs-continuous-chlorination",
                "uv-vs-ro",
                "when-not-to-buy-treatment-yet"
        );

        for (String slug : slugs) {
            PseoDetailView detailView = experienceService.detailView(slug).orElseThrow();
            assertTrue(detailView.decisionDoc() != null, slug);
            assertTrue(detailView.citations().size() >= 2, slug);
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
