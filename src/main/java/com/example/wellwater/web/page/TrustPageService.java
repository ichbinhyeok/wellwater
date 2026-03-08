package com.example.wellwater.web.page;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TrustPageService {

    private final List<TrustPage> pages = List.of(
            page(
                    "methodology",
                    "Private Well Decision Methodology",
                    "How This Site Turns Well-Water Clues Into Next Steps",
                    "Read the public methodology behind this well-water workflow, including how evidence, urgency, and buying gates are handled.",
                    "Methodology",
                    "This page explains how public pages, tool inputs, and noindex result pages fit together before any commercial recommendation appears.",
                    "The goal is not to predict everything from one clue. The goal is to narrow the next safe step with less fear, less guesswork, and fewer bad purchases.",
                    "2026-03-07",
                    section(
                            "Evidence hierarchy",
                            "This project treats evidence in layers instead of pretending every symptom or single lab line has the same strength.",
                            "Certified lab results outrank symptoms, symptoms outrank marketing claims, and official state or federal guidance outranks affiliate copy.",
                            "Recent floods, repairs, wildfires, and other triggers can downgrade confidence even when an older result exists.",
                            "Regional context matters because geology, radionuclides, agricultural runoff, and sale rules change what belongs in scope."
                    ),
                    section(
                            "Decision outputs",
                            "The engine is designed to answer what to do now, what to test next, and what not to buy yet.",
                            "Urgency is raised when vulnerable households, microbial signals, or timing-sensitive events are present.",
                            "Confidence is lowered when data quality is weak, sampling is suspect, or the issue is inferred from symptoms alone.",
                            "Commerce belongs after scope, verification, and safety notes, not before them."
                    ),
                    section(
                            "What public pages are for",
                            "Indexable pages are acquisition surfaces. They are supposed to orient the situation quickly and then route the user into the matching tool path.",
                            "Public pages should explain the likely problem shape, common misreads, and the next verification move.",
                            "Noindex result pages are where more personalized reasoning appears after the user gives context.",
                            "That split exists to keep public search pages useful without pretending they are individual household verdicts."
                    )
            ),
            page(
                    "review-policy",
                    "Editorial And Review Policy",
                    "Editorial and Review Policy for Private Well Water Content",
                    "See how this site reviews public pages, updates weak claims, and keeps commerce behind evidence and safety rules.",
                    "Review policy",
                    "This page explains what gets reviewed before a guide is published or refreshed, and what kinds of claims are not allowed.",
                    "A byline alone is not trust. The real trust signal is a stable review process with clear limits, corrections, and evidence rules.",
                    "2026-03-07",
                    section(
                            "What we review before publishing",
                            "Public pages are checked for scope, source fit, and whether the proposed next step is stronger than the available evidence.",
                            "Pages should separate health risk, nuisance issues, and corrosion clues instead of collapsing them into one alarmist answer.",
                            "A treatment comparison should not appear as a verdict when certified testing or scope clarification is still missing.",
                            "State-specific pages should show why the state context changes the next action instead of merely repeating a state name."
                    ),
                    section(
                            "What we do not allow",
                            "This project blocks a few common patterns because they create bad private-well decisions.",
                            "No fear-based copy that tries to force a purchase before testing or verification.",
                            "No pretending a home kit, product listing, or affiliate page is stronger than certified lab evidence.",
                            "No broad claims that a single category solves every odor, stain, radionuclide, or contaminant problem."
                    ),
                    section(
                            "Corrections and refreshes",
                            "If a page has weak sourcing, stale logic, or a misleading comparison, the page should be revised before more surface area is added.",
                            "Source-trail dates exist so older research can be spotted and refreshed.",
                            "High-risk health topics should be refreshed faster than low-stakes nuisance pages.",
                            "When the evidence is weak, the correct outcome is a narrower recommendation, not more confident copy."
                    )
            ),
            page(
                    "sources-policy",
                    "Sources And Update Policy",
                    "Sources and Update Policy for Well-Water Guides",
                    "Read which sources this site prefers, how citations are attached to pages, and when stale material should be replaced.",
                    "Sources policy",
                    "This page explains how official guidance, labs, and product standards should be used across public guides and decision outputs.",
                    "Trust is not just having links. Trust is choosing the right links, showing why they matter, and refreshing them when the context changes.",
                    "2026-03-07",
                    section(
                            "Preferred source tiers",
                            "Public guides should lean on primary or official sources whenever possible.",
                            "Federal and state public-health or environmental guidance is preferred for testing scope, safety, and escalation logic.",
                            "Certified laboratory directories and standards bodies are preferred for lab and treatment verification questions.",
                            "Commercial pages can support a comparison, but they should not be the main authority for health or scope decisions."
                    ),
                    section(
                            "How citations are used",
                            "Each page should keep a visible source trail and, where useful, more than one supporting citation.",
                            "Primary official source links are shown on the page instead of being hidden in a separate note.",
                            "Pages that depend on state context should cite that state guidance, not only national summaries.",
                            "Comparison pages should link to standards or claim-verification sources when product capability is part of the decision."
                    ),
                    section(
                            "When a page needs updating",
                            "Some triggers should force a refresh instead of letting a page drift.",
                            "If a state resource changes, the related regional page should be checked.",
                            "If the commercial route changes but the underlying safety logic does not, the safety logic still wins.",
                            "If the page cannot be refreshed with strong sources, it should stay narrower rather than becoming more speculative."
                    )
            ),
            page(
                    "safety-and-scope",
                    "Safety And Scope Limits",
                    "Safety and Scope Limits for This Well-Water Decision Tool",
                    "Understand when this site is useful, when certified labs or local agencies should take over, and when not to rely on product research.",
                    "Safety and scope",
                    "This page explains the limits of public guidance and why some private-well situations should stop at testing, escalation, or temporary alternative water.",
                    "The highest-risk mistake in this niche is turning uncertainty into shopping. This page exists to stop that.",
                    "2026-03-07",
                    section(
                            "What this site is not",
                            "This project is not a substitute for emergency instructions, medical care, or state-certified testing requirements.",
                            "It does not diagnose illness or confirm exposure from one symptom page.",
                            "It does not replace local public-health instructions after floods, wildfire damage, or microbial events.",
                            "It does not turn an unverified product claim into proof that the household risk is solved."
                    ),
                    section(
                            "When to escalate instead of shop",
                            "Some conditions should narrow buying decisions and raise the priority of verification or safer temporary water.",
                            "Infant or pregnancy nitrate questions should move toward safer drinking water and certified testing first.",
                            "Microbial positives, flood events, or suspect sampling should slow product comparison and increase verification.",
                            "Corrosion clues with possible lead or copper exposure should tighten the test sequence before any equipment verdict."
                    ),
                    section(
                            "How commerce is constrained",
                            "The commercial layer is intentionally downstream from evidence and safety.",
                            "The site can compare categories, kits, labs, or affiliate products only after the page explains what is still unknown.",
                            "Low-confidence paths should carry more caution, not more aggressive calls to action.",
                            "If a page cannot justify a product path from the available evidence, the correct output is to keep the user in verification mode."
                    )
            )
    );

    public List<TrustPage> allPages() {
        return pages;
    }

    public Optional<TrustPage> findBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return Optional.empty();
        }
        String normalized = slug.toLowerCase(Locale.ROOT);
        return pages.stream()
                .filter(page -> page.slug().equals(normalized))
                .findFirst();
    }

    public List<String> sitemapPaths() {
        List<String> paths = new ArrayList<>();
        paths.add("/trust");
        for (TrustPage page : pages) {
            paths.add("/trust/" + page.slug());
        }
        return List.copyOf(paths);
    }

    private TrustPage page(
            String slug,
            String title,
            String h1,
            String metaDescription,
            String eyebrow,
            String lead,
            String operatorNote,
            String updatedAt,
            TrustSection... sections
    ) {
        return new TrustPage(
                slug,
                title,
                h1,
                metaDescription,
                eyebrow,
                lead,
                operatorNote,
                updatedAt,
                List.of(sections)
        );
    }

    private TrustSection section(String title, String body, String... bullets) {
        return new TrustSection(title, body, List.of(bullets));
    }
}
