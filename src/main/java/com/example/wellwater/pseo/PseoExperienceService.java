package com.example.wellwater.pseo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PseoExperienceService {

    private static final Set<String> HEALTH_CHEMICAL_SLUGS = Set.of(
            "nitrate", "arsenic", "lead", "pfas", "uranium", "radon", "nitrite", "chromium", "barium", "selenium", "fluoride"
    );
    private static final Set<String> MICROBIAL_SLUGS = Set.of("coliform", "e-coli");
    private static final Set<String> CORROSION_SLUGS = Set.of("ph", "copper", "metallic-taste", "blue-green-stains");
    private static final Set<String> NUISANCE_SLUGS = Set.of(
            "iron", "manganese", "hardness", "tds", "sulfate", "chloride", "sodium",
            "rotten-egg-smell", "sulfur-smell-hot-water", "orange-stains", "black-stains",
            "cloudy-water", "scale-buildup", "salty-taste", "bitter-taste", "slimy-residue",
            "musty-odor", "pink-stains", "low-water-pressure"
    );
    private static final Set<String> VULNERABLE_HOUSEHOLD_SLUGS = Set.of(
            "nitrate", "arsenic", "lead", "pfas", "coliform", "e-coli", "new-baby-at-home", "pregnancy-in-home"
    );
    private static final Set<String> STOP_WORDS = Set.of(
            "in", "well", "water", "for", "and", "the", "to", "what", "how", "from", "with", "after", "vs", "guide", "next", "steps", "first"
    );
    private static final Map<String, Set<String>> CLUSTER_COMPANIONS = Map.ofEntries(
            Map.entry("new-hampshire-arsenic-well-water", Set.of("arsenic", "radon", "uranium", "test-kit-vs-certified-lab", "mail-in-lab-vs-local-certified-lab", "ro-vs-adsorptive-media-for-arsenic", "arsenic-bedrock-testing-checklist", "private-well-home-sale-testing-by-state", "home-sale-private-well-testing-checklist")),
            Map.entry("new-jersey-pwta-private-well-testing", Set.of("home-purchase-test", "mail-in-lab-vs-local-certified-lab", "test-kit-vs-certified-lab", "pfas", "arsenic", "private-well-home-sale-testing-by-state", "new-jersey-pwta-vs-full-household-panel", "private-well-sampling-mistakes-that-break-results", "home-sale-private-well-testing-checklist")),
            Map.entry("florida-rotten-egg-smell-well-water", Set.of("rotten-egg-smell", "sulfur-smell-hot-water", "orange-stains", "point-of-entry-vs-point-of-use", "air-injection-vs-oxidizing-filter", "sulfur-smell-hot-water-vs-whole-house", "florida-sulfur-smell-staining-testing-order", "when-not-to-buy-treatment-yet")),
            Map.entry("iowa-nitrate-baby-well-water", Set.of("nitrate", "nitrite", "new-baby-at-home", "pregnancy-in-home", "carbon-vs-ro", "nitrate-baby-pregnancy-well-water-checklist", "private-well-testing-schedule-by-household")),
            Map.entry("connecticut-low-ph-blue-green-stains", Set.of("ph", "copper", "metallic-taste", "blue-green-stains", "lead", "acid-neutralizer-vs-soda-ash", "low-ph-copper-corrosion-testing-order", "how-to-read-a-well-water-lab-report")),
            Map.entry("pennsylvania-private-well-radon", Set.of("radon", "uranium", "home-purchase-test", "radon-aeration-vs-gac", "radon-radium-private-well-testing-order", "private-well-home-sale-testing-by-state", "home-sale-private-well-testing-checklist")),
            Map.entry("maine-bedrock-arsenic-private-well", Set.of("arsenic", "uranium", "radon", "test-kit-vs-certified-lab", "ro-vs-adsorptive-media-for-arsenic", "arsenic-bedrock-testing-checklist", "radon-radium-private-well-testing-order")),
            Map.entry("michigan-arsenic-private-well", Set.of("arsenic", "nitrate", "lead", "test-kit-vs-certified-lab", "ro-vs-adsorptive-media-for-arsenic", "arsenic-bedrock-testing-checklist")),
            Map.entry("wisconsin-nitrate-pregnancy-well-water", Set.of("nitrate", "nitrite", "pregnancy-in-home", "new-baby-at-home", "carbon-vs-ro", "nitrate-baby-pregnancy-well-water-checklist", "private-well-testing-schedule-by-household")),
            Map.entry("minnesota-nitrate-private-well", Set.of("nitrate", "nitrite", "new-baby-at-home", "pregnancy-in-home", "carbon-vs-ro", "nitrate-baby-pregnancy-well-water-checklist", "private-well-testing-schedule-by-household")),
            Map.entry("massachusetts-bedrock-arsenic-uranium-well", Set.of("arsenic", "uranium", "radon", "ro-vs-adsorptive-media-for-arsenic", "arsenic-bedrock-testing-checklist", "radon-radium-private-well-testing-order", "home-sale-private-well-testing-checklist")),
            Map.entry("vermont-new-well-arsenic-uranium-testing", Set.of("arsenic", "uranium", "ph", "test-kit-vs-certified-lab", "arsenic-bedrock-testing-checklist", "private-well-sampling-mistakes-that-break-results", "home-sale-private-well-testing-checklist")),
            Map.entry("california-private-well-owner-guide", Set.of("after-wildfire", "after-heavy-rain", "home-purchase-test", "california-after-wildfire-private-well-checklist", "wildfire-drought-private-well-risk-reset", "private-well-sampling-mistakes-that-break-results", "private-well-testing-schedule-by-household")),
            Map.entry("texas-private-well-sampling-testing", Set.of("test-kit-vs-certified-lab", "mail-in-lab-vs-local-certified-lab", "home-purchase-test", "nitrate", "coliform", "texas-private-well-sampling-chain-of-custody", "private-well-sampling-mistakes-that-break-results", "how-to-read-a-well-water-lab-report")),
            Map.entry("new-york-pfas-private-wells", Set.of("pfas", "mail-in-lab-vs-local-certified-lab", "point-of-entry-vs-point-of-use", "new-york-pfas-private-well-testing-order", "pfas-private-well-filter-claim-checklist", "how-to-verify-water-treatment-claims")),
            Map.entry("ro-vs-adsorptive-media-for-arsenic", Set.of("arsenic", "new-hampshire-arsenic-well-water", "maine-bedrock-arsenic-private-well", "massachusetts-bedrock-arsenic-uranium-well", "arsenic-bedrock-testing-checklist")),
            Map.entry("acid-neutralizer-vs-soda-ash", Set.of("ph", "copper", "metallic-taste", "connecticut-low-ph-blue-green-stains", "low-ph-copper-corrosion-testing-order")),
            Map.entry("air-injection-vs-oxidizing-filter", Set.of("rotten-egg-smell", "sulfur-smell-hot-water", "florida-rotten-egg-smell-well-water", "sulfur-smell-hot-water-vs-whole-house")),
            Map.entry("radon-aeration-vs-gac", Set.of("radon", "uranium", "pennsylvania-private-well-radon", "radon-radium-private-well-testing-order")),
            Map.entry("new-jersey-pwta-vs-full-household-panel", Set.of("new-jersey-pwta-private-well-testing", "home-purchase-test", "private-well-home-sale-testing-by-state", "home-sale-private-well-testing-checklist")),
            Map.entry("florida-sulfur-smell-staining-testing-order", Set.of("florida-rotten-egg-smell-well-water", "rotten-egg-smell", "orange-stains", "air-injection-vs-oxidizing-filter", "sulfur-smell-hot-water-vs-whole-house")),
            Map.entry("california-after-wildfire-private-well-checklist", Set.of("california-private-well-owner-guide", "after-wildfire", "after-heavy-rain", "wildfire-drought-private-well-risk-reset")),
            Map.entry("texas-private-well-sampling-chain-of-custody", Set.of("texas-private-well-sampling-testing", "test-kit-vs-certified-lab", "mail-in-lab-vs-local-certified-lab", "private-well-sampling-mistakes-that-break-results")),
            Map.entry("new-york-pfas-private-well-testing-order", Set.of("new-york-pfas-private-wells", "pfas", "point-of-entry-vs-point-of-use", "pfas-private-well-filter-claim-checklist"))
    );

    private final PseoCatalogService catalogService;
    private final PseoCitationRegistryService citationRegistryService;

    public PseoExperienceService(PseoCatalogService catalogService, PseoCitationRegistryService citationRegistryService) {
        this.catalogService = catalogService;
        this.citationRegistryService = citationRegistryService;
    }

    public Optional<PseoDetailView> detailView(String slug) {
        return catalogService.findBySlug(slug).map(page -> toDetailView(page, catalogService.allPages()));
    }

    public PseoFamilyView familyView(String family, List<PseoPage> pages) {
        int size = pages == null ? 0 : pages.size();
        return switch (family) {
            case "contaminants" -> new PseoFamilyView(
                    family,
                    "Contaminant response guides",
                    "Named-analyte pages for health, corrosion, and nuisance interpretation before the tool takes over.",
                    size + " pages aimed at result interpretation, confirmatory testing, and scope decisions.",
                    "Start From Lab Results",
                    "/tool/result-first"
            );
            case "symptoms" -> new PseoFamilyView(
                    family,
                    "Symptom-first troubleshooting guides",
                    "Pages for odor, stains, taste, residue, and other visible clues when clean lab data is missing.",
                    size + " pages aimed at diagnostic thinking before anyone buys the wrong category.",
                    "Start From Symptoms",
                    "/tool/symptom-first"
            );
            case "compares" -> new PseoFamilyView(
                    family,
                    "Treatment comparison guides",
                    "Category-vs-category pages built to slow premature buying and force better scope and claim checks.",
                    size + " pages comparing product paths without pretending every problem has the same fix.",
                    "Start From Lab Results",
                    "/tool/result-first"
            );
            case "triggers" -> new PseoFamilyView(
                    family,
                    "Recent trigger response guides",
                    "Event-driven pages for flood, repair, vacancy, wildfire, and other moments when retest timing matters.",
                    size + " pages aimed at response order, retest timing, and escalation logic.",
                    "Start From Recent Trigger",
                    "/tool/trigger-first"
            );
            case "regional" -> new PseoFamilyView(
                    family,
                    "Regional well-water guides",
                    "State-specific pages that connect geology, regulation, and local testing pathways to a practical next step.",
                    size + " pages aimed at location-shaped search intent rather than generic national answers.",
                    "Start With Your State Context",
                    "/tool/result-first"
            );
            case "authority" -> new PseoFamilyView(
                    family,
                    "Authority and methodology articles",
                    "Support articles that explain how to read reports, verify claims, and avoid bad testing and buying assumptions.",
                    size + " pages aimed at trust, method clarity, and stronger internal support for money pages.",
                    "Open Decision Tool",
                    "/tool/result-first"
            );
            default -> new PseoFamilyView(
                    family,
                    family,
                    "Indexable well-water guide family.",
                    size + " pages in this family.",
                    "Open Tool",
                    "/tool/result-first"
            );
        };
    }

    public PseoDetailView toDetailView(PseoPage page, List<PseoPage> allPages) {
        String riskLens = riskLens(page);
        PseoEntryHint entryHint = entryHint(page);
        return new PseoDetailView(
                page,
                archetypeLabel(page),
                intentLabel(page),
                riskLens,
                riskSummary(page, riskLens),
                householdLabel(page, riskLens),
                householdSummary(page, riskLens),
                doNotBuyYet(page, riskLens),
                entryHint,
                quickAnswers(page, riskLens, entryHint),
                relatedSections(page, allPages, riskLens),
                citations(page)
        );
    }

    private String archetypeLabel(PseoPage page) {
        return switch (page.family()) {
            case "contaminants" -> "Contaminant brief";
            case "symptoms" -> "Symptom diagnostic";
            case "compares" -> "Category comparison";
            case "triggers" -> "Event response guide";
            case "regional" -> "Regional guide";
            case "authority" -> "Authority article";
            default -> "Well-water guide";
        };
    }

    private String intentLabel(PseoPage page) {
        return switch (page.family()) {
            case "contaminants" -> "Interpret a named analyte and choose the next verification step";
            case "symptoms" -> "Translate a visible clue into a smarter test and scope plan";
            case "compares" -> "Slow down category shopping until scope and claims are clearer";
            case "triggers" -> "Decide what to retest, inspect, or escalate after a specific event";
            case "regional" -> "Translate state-specific risk patterns and rules into a practical testing path";
            case "authority" -> "Build trust and method clarity before the tool or product comparison takes over";
            default -> "Orient the problem before opening the personalized tool";
        };
    }

    private String riskLens(PseoPage page) {
        if ("triggers".equals(page.family())) {
            return "Event-driven";
        }
        if ("regional".equals(page.family())) {
            return "Regional / state-specific";
        }
        if ("authority".equals(page.family())) {
            return "Authority / methodology";
        }
        if (MICROBIAL_SLUGS.contains(page.slug())) {
            return "Health / microbial";
        }
        if (HEALTH_CHEMICAL_SLUGS.contains(page.slug())) {
            return "Health / chemical";
        }
        if (CORROSION_SLUGS.contains(page.slug())) {
            return "Corrosion / plumbing";
        }
        if (NUISANCE_SLUGS.contains(page.slug()) || "symptoms".equals(page.family())) {
            return "Nuisance / operational";
        }
        if ("compares".equals(page.family())) {
            return "Decision / comparison";
        }
        return "Mixed / verify first";
    }

    private String riskSummary(PseoPage page, String riskLens) {
        return switch (riskLens) {
            case "Health / microbial" -> "Treat this as exposure control plus confirmatory testing, not a shopping problem.";
            case "Health / chemical" -> "Use this page to reduce uncertainty first, especially before choosing equipment scope.";
            case "Corrosion / plumbing" -> "Separate source-water risk from plumbing interaction before locking into treatment.";
            case "Nuisance / operational" -> "Focus on symptom pattern, scope, and maintenance burden before comparing products.";
            case "Event-driven" -> "Recent events change retest timing and can invalidate older assumptions about the system.";
            case "Regional / state-specific" -> "Use this page when local geology, regulation, or state lab pathways change the right next step.";
            case "Authority / methodology" -> "Use this page to tighten your method and avoid sloppy interpretations before shopping.";
            default -> "Use this page for orientation, then hand off to a more specific tool flow.";
        };
    }

    private String householdLabel(PseoPage page, String riskLens) {
        if (VULNERABLE_HOUSEHOLD_SLUGS.contains(page.slug())) {
            return "Vulnerable household";
        }
        return switch (riskLens) {
            case "Health / microbial", "Health / chemical" -> "Health-first household";
            case "Corrosion / plumbing" -> "Plumbing-first household";
            case "Nuisance / operational" -> "Whole-house comfort household";
            case "Event-driven" -> "Incident-response household";
            case "Regional / state-specific" -> "State-context household";
            case "Authority / methodology" -> "Verification-first household";
            default -> "Verification-first household";
        };
    }

    private String householdSummary(PseoPage page, String riskLens) {
        if (VULNERABLE_HOUSEHOLD_SLUGS.contains(page.slug())) {
            return "Infants, pregnancy, or other sensitivity flags should push you toward faster drinking-water verification.";
        }
        return switch (riskLens) {
            case "Health / microbial" -> "Best for households trying to control exposure first and delay all product decisions until data is cleaner.";
            case "Health / chemical" -> "Best for households deciding whether the issue is drinking-only, whole-house, or a false sense of urgency.";
            case "Corrosion / plumbing" -> "Best for households seeing metallic taste, blue-green signs, or low-pH hints that may involve plumbing interaction.";
            case "Nuisance / operational" -> "Best for households annoyed by taste, odor, stains, or maintenance pain but still trying to avoid over-buying.";
            case "Event-driven" -> "Best for households reacting to a discrete event where timing and sequence matter more than a product shortlist.";
            case "Regional / state-specific" -> "Best for households where state guidance, geology, or home-sale rules change what you should test first.";
            case "Authority / methodology" -> "Best for households trying to make fewer mistakes with testing, interpretation, and product claims.";
            default -> "Best for households still narrowing the problem definition.";
        };
    }

    private String doNotBuyYet(PseoPage page, String riskLens) {
        if ("compares".equals(page.family())) {
            return "Do not treat the comparison page as a verdict. Lock down contaminant or symptom scope before spending.";
        }
        return switch (riskLens) {
            case "Health / microbial" -> "Do not jump to filters or disinfection hardware before confirmatory testing and immediate exposure steps.";
            case "Health / chemical" -> "Do not buy by contaminant name alone until scope, certified testing, and claim fit are clear.";
            case "Corrosion / plumbing" -> "Do not assume a treatment tank solves what might be a plumbing and corrosion interaction problem.";
            case "Nuisance / operational" -> "Do not buy the biggest whole-house system before mapping where the symptom appears and what maintenance it adds.";
            case "Event-driven" -> "Do not treat an event page like a permanent equipment answer. First reset the evidence after the event.";
            case "Regional / state-specific" -> "Do not assume a national answer fits local geology, rules, or lab pathways in your state.";
            case "Authority / methodology" -> "Do not treat a method article like a product verdict. Clean up the evidence first.";
            default -> "Do not shop from uncertainty. Narrow the problem first.";
        };
    }

    private PseoEntryHint entryHint(PseoPage page) {
        String stateQuery = stateHint(page).map(state -> "&state=" + state).orElse("");
        return switch (page.family()) {
            case "contaminants" -> new PseoEntryHint(
                    "Result-first handoff",
                    "/tool/result-first?analyte=" + page.slug() + "&slug=" + page.slug(),
                    "Best when you already have one named analyte from a lab report."
            );
            case "symptoms" -> new PseoEntryHint(
                    "Symptom-first handoff",
                    "/tool/symptom-first?symptom=" + page.slug() + "&slug=" + page.slug(),
                    "Best when the visible problem is stronger than the data you have."
            );
            case "triggers" -> new PseoEntryHint(
                    "Trigger-first handoff",
                    "/tool/trigger-first?trigger=" + page.slug() + "&slug=" + page.slug(),
                    "Best when a recent event changed how you should interpret risk or retest timing."
            );
            case "regional" -> new PseoEntryHint(
                    "State-aware handoff",
                    "/tool/result-first?slug=" + page.slug() + stateQuery,
                    "Best when the state context matters and you want the engine to combine that with your own result or symptom."
            );
            case "authority" -> new PseoEntryHint(
                    "Method-first handoff",
                    "/tool/result-first?slug=" + page.slug(),
                    "Best when you want to apply the method article to your own result instead of reading in the abstract."
            );
            default -> new PseoEntryHint(
                    "Result-first handoff",
                    "/tool/result-first?slug=" + page.slug(),
                    "Best when you want the engine to narrow the problem before shopping."
            );
        };
    }

    private List<PseoQuickAnswer> quickAnswers(PseoPage page, String riskLens, PseoEntryHint entryHint) {
        return List.of(
                new PseoQuickAnswer("Risk lens", riskSummary(page, riskLens)),
                new PseoQuickAnswer("Best-fit household", householdSummary(page, riskLens)),
                new PseoQuickAnswer("Do not buy yet", doNotBuyYet(page, riskLens)),
                new PseoQuickAnswer(entryHint.label(), entryHint.reason())
        );
    }

    private List<PseoRelatedSection> relatedSections(PseoPage page, List<PseoPage> allPages, String riskLens) {
        List<PseoRelatedSection> sections = new ArrayList<>();
        for (String family : preferredFamilies(page.family())) {
            List<PseoPage> matches = allPages.stream()
                    .filter(candidate -> !candidate.slug().equals(page.slug()))
                    .filter(candidate -> candidate.family().equals(family))
                    .sorted(Comparator.comparingInt((PseoPage candidate) -> relatedScore(page, candidate, riskLens)).reversed()
                            .thenComparing(PseoPage::slug))
                    .limit(2)
                    .toList();
            if (!matches.isEmpty()) {
                sections.add(new PseoRelatedSection(
                        relatedSectionTitle(family),
                        relatedSectionDescription(page.family(), family),
                        matches
                ));
            }
        }
        return sections;
    }

    private List<String> preferredFamilies(String family) {
        return switch (family) {
            case "contaminants" -> List.of("symptoms", "compares", "triggers", "regional", "authority");
            case "symptoms" -> List.of("contaminants", "compares", "triggers", "regional", "authority");
            case "compares" -> List.of("contaminants", "symptoms", "triggers", "authority", "regional");
            case "triggers" -> List.of("contaminants", "symptoms", "compares", "regional", "authority");
            case "regional" -> List.of("contaminants", "symptoms", "authority", "compares");
            case "authority" -> List.of("regional", "contaminants", "compares", "triggers");
            default -> List.of("contaminants", "symptoms", "compares", "regional");
        };
    }

    private String relatedSectionTitle(String family) {
        return switch (family) {
            case "contaminants" -> "Related contaminant reads";
            case "symptoms" -> "Related symptom reads";
            case "compares" -> "Related comparison reads";
            case "triggers" -> "Related trigger reads";
            case "regional" -> "Related regional reads";
            case "authority" -> "Related authority reads";
            default -> "Related pages";
        };
    }

    private String relatedSectionDescription(String sourceFamily, String targetFamily) {
        return switch (targetFamily) {
            case "contaminants" -> "Use named analyte pages to turn a clue or comparison into a clearer testing plan.";
            case "symptoms" -> "Use symptom pages when the issue is visible but your data quality is still weak.";
            case "compares" -> "Use compare pages only after you narrow the likely scope and claim requirements.";
            case "triggers" -> "Use trigger pages when timing or a recent event changes what the next action should be.";
            case "regional" -> "Use regional pages when geology, regulation, or state testing pathways change the answer.";
            case "authority" -> "Use authority pages to tighten your method, trust, and interpretation discipline.";
            default -> "Keep moving through the problem graph, not a flat list of pages.";
        };
    }

    private int relatedScore(PseoPage source, PseoPage candidate, String sourceRiskLens) {
        int score = 0;
        if (riskLens(candidate).equals(sourceRiskLens)) {
            score += 8;
        }
        score += tokenOverlap(source, candidate) * 5;
        score += manualClusterBoost(source, candidate);

        Set<String> sourceTokens = keywordTokens(source);
        if (sourceTokens.contains("baby") || sourceTokens.contains("pregnancy")) {
            if (Set.of("new-baby-at-home", "pregnancy-in-home", "test-kit-vs-certified-lab").contains(candidate.slug())) {
                score += 8;
            }
        }
        if (MICROBIAL_SLUGS.contains(source.slug()) || sourceTokens.contains("bacteria")) {
            if (Set.of("uv-vs-ro", "uv-vs-chlorination", "shock-vs-continuous-chlorination", "after-boil-water-advisory").contains(candidate.slug())) {
                score += 7;
            }
        }
        if (HEALTH_CHEMICAL_SLUGS.contains(source.slug())) {
            if (Set.of("carbon-vs-ro", "whole-house-vs-under-sink-ro", "test-kit-vs-certified-lab", "mail-in-lab-vs-local-certified-lab").contains(candidate.slug())) {
                score += 6;
            }
        }
        if (CORROSION_SLUGS.contains(source.slug())) {
            if (Set.of("metallic-taste", "blue-green-stains", "lead", "after-repair").contains(candidate.slug())) {
                score += 7;
            }
        }
        if (NUISANCE_SLUGS.contains(source.slug()) || "symptoms".equals(source.family())) {
            if (Set.of("softener-vs-iron-filter", "iron-filter-vs-softener", "spin-down-vs-cartridge-sediment-filter", "point-of-entry-vs-point-of-use").contains(candidate.slug())) {
                score += 6;
            }
        }
        if ("triggers".equals(source.family())) {
            if (Set.of("test-kit-vs-certified-lab", "mail-in-lab-vs-local-certified-lab").contains(candidate.slug())) {
                score += 4;
            }
        }
        return score;
    }

    private int manualClusterBoost(PseoPage source, PseoPage candidate) {
        if (source == null || candidate == null) {
            return 0;
        }
        int score = 0;
        Set<String> sourceCompanions = CLUSTER_COMPANIONS.getOrDefault(source.slug(), Set.of());
        if (sourceCompanions.contains(candidate.slug())) {
            score += 24;
        }
        Set<String> candidateCompanions = CLUSTER_COMPANIONS.getOrDefault(candidate.slug(), Set.of());
        if (candidateCompanions.contains(source.slug())) {
            score += 16;
        }
        return score;
    }

    private List<PseoCitation> citations(PseoPage page) {
        LinkedHashMap<String, PseoCitation> deduped = new LinkedHashMap<>();
        addCitation(deduped, new PseoCitation("Primary official source", page.sourceUrl()));
        for (PseoCitation citation : citationRegistryService.findBySlug(page.slug())) {
            addCitation(deduped, citation);
        }
        return List.copyOf(deduped.values());
    }

    private void addCitation(Map<String, PseoCitation> citations, PseoCitation citation) {
        if (citation == null || citation.url() == null || citation.url().isBlank()) {
            return;
        }
        citations.putIfAbsent(citation.url(), citation);
    }

    private Optional<String> stateHint(PseoPage page) {
        if (!"regional".equals(page.family())) {
            return Optional.empty();
        }
        return switch (page.slug()) {
            case "new-hampshire-arsenic-well-water" -> Optional.of("NH");
            case "new-jersey-pwta-private-well-testing" -> Optional.of("NJ");
            case "florida-rotten-egg-smell-well-water" -> Optional.of("FL");
            case "iowa-nitrate-baby-well-water" -> Optional.of("IA");
            case "connecticut-low-ph-blue-green-stains" -> Optional.of("CT");
            case "pennsylvania-private-well-radon" -> Optional.of("PA");
            case "maine-bedrock-arsenic-private-well" -> Optional.of("ME");
            case "michigan-arsenic-private-well" -> Optional.of("MI");
            case "wisconsin-nitrate-pregnancy-well-water" -> Optional.of("WI");
            case "minnesota-nitrate-private-well" -> Optional.of("MN");
            case "massachusetts-bedrock-arsenic-uranium-well" -> Optional.of("MA");
            case "vermont-new-well-arsenic-uranium-testing" -> Optional.of("VT");
            case "california-private-well-owner-guide" -> Optional.of("CA");
            case "texas-private-well-sampling-testing" -> Optional.of("TX");
            case "new-york-pfas-private-wells" -> Optional.of("NY");
            default -> Optional.empty();
        };
    }

    private int tokenOverlap(PseoPage source, PseoPage candidate) {
        Set<String> sourceTokens = keywordTokens(source);
        Set<String> candidateTokens = keywordTokens(candidate);
        sourceTokens.retainAll(candidateTokens);
        return sourceTokens.size();
    }

    private Set<String> keywordTokens(PseoPage page) {
        return tokenize(page.slug() + " " + page.h1() + " " + page.metaDescription() + " " + page.whatToTest());
    }

    private Set<String> tokenize(String text) {
        return java.util.Arrays.stream(text.toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
                .filter(token -> !token.isBlank())
                .filter(token -> !STOP_WORDS.contains(token))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
