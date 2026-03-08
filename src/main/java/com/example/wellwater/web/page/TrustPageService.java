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
            ),
            page(
                    "ymyl-and-legal-disclaimer",
                    "YMYL And Legal Disclaimer",
                    "Medical, Legal, and Emergency Limits of This Well-Water Guidance",
                    "Read the explicit medical, legal, and emergency disclaimer for this private-well decision product before relying on any public page or saved result.",
                    "YMYL disclaimer",
                    "This page makes the legal and health boundary explicit so public guidance is not mistaken for emergency, medical, or jurisdiction-specific professional advice.",
                    "A strong trust surface in this niche does not hide its limits. It names them before a visitor turns a public page into a household verdict.",
                    "2026-03-08",
                    section(
                            "No medical or emergency substitute",
                            "This product does not diagnose illness, confirm exposure, or replace emergency instructions.",
                            "If someone may already be sick, exposed, or drinking unsafe water, medical care, local public-health guidance, and safer temporary water come first.",
                            "Microbial positives, infant nitrate risk, flooding, wildfire damage, and other emergency contexts should not be downgraded into routine shopping behavior.",
                            "A public page can help frame the next test or escalation step, but it is not bedside or emergency advice."
                    ),
                    section(
                            "No legal, disclosure, or compliance guarantee",
                            "This product does not provide legal advice and should not be treated as a substitute for state sale rules, disclosure duties, lease obligations, or local compliance requirements.",
                            "Home sale, landlord, or contractor disputes should use the applicable state or local rule set, not only a public guide on this site.",
                            "A state-aware page may summarize local guidance, but it does not guarantee that the latest jurisdiction-specific requirement is fully captured.",
                            "Where legal or compliance stakes are high, certified labs and local agencies outrank generalized content."
                    ),
                    section(
                            "How to use the site safely",
                            "Use this site to narrow the next safe question: what to test, what to verify, what not to buy yet, and when to escalate.",
                            "Do not treat a symptom page, compare page, or saved result as proof that one product solves the whole problem.",
                            "When the evidence stack is weak, this product should push the user toward stronger testing and narrower claims rather than more confidence.",
                            "If a conflict exists between this site and a current official instruction from a state, county, or federal health authority, the official instruction wins."
                    )
            ),
            page(
                    "reviewers-and-expertise",
                    "Reviewers And Expertise",
                    "Who Reviews This Well-Water Guidance",
                    "See who reviews this site, what kind of expertise is represented, and what this editorial desk can and cannot verify.",
                    "Reviewers",
                    "This page makes the reviewer surface explicit so the site does not ask for trust through tone alone.",
                    "Trust gets stronger when visitors can see who is responsible for judgment, limits, refreshes, and claim-check discipline.",
                    "2026-03-08",
                    section(
                            "Editorial desk",
                            "This site is reviewed by a named editorial desk instead of pretending the content is anonymous or auto-generated.",
                            "Editorial lead: Private Well Editorial Desk.",
                            "Primary review focus: private-well testing logic, result interpretation, scope control, and claim-check discipline.",
                            "Commercial review focus: whether a page has earned a compare or partner handoff without skipping evidence."
                    ),
                    section(
                            "What the review actually covers",
                            "Reviewer visibility matters only if it is tied to concrete review responsibilities.",
                            "High-risk health pages should be reviewed for escalation logic, benchmark language, and unsupported claims.",
                            "Nuisance and equipment pages should still be reviewed for scope mistakes, maintenance burden, and over-buying risk.",
                            "State-aware pages should be checked when local guidance changes, not just when the copy feels old."
                    ),
                    section(
                            "What this expertise does not claim",
                            "This site is still a decision-support product, not a substitute for a state lab, a physician, or emergency instructions.",
                            "Reviewer visibility does not turn one page into a household-specific diagnosis.",
                            "Where evidence is weak, the review standard is to narrow the recommendation instead of sounding more certain.",
                            "If a page needs stronger local or lab input, the correct output is to route the user outward."
                    )
            ),
            page(
                    "privacy-and-data-handling",
                    "Privacy And Data Handling",
                    "How This Site Handles Saved Results, Follow-Up Requests, and Analytics",
                    "Understand what this site stores when you save a result, request follow-up, or click through a decision path.",
                    "Privacy and data",
                    "This page explains the minimum data stored by the current product and the limits of that storage model.",
                    "A diagnostic product that stores results and follow-up requests should explain retention, visibility, and limits in plain English.",
                    "2026-03-08",
                    section(
                            "What is stored today",
                            "The current product stores a limited set of operational data so results can be reopened and follow-up requests can be reviewed.",
                            "Saved results are stored as private snapshot files with a time limit instead of being published as indexable pages.",
                            "Analytics events record route behavior such as public-page views, tool entry, result views, and CTA clicks.",
                            "Lead requests store the contact details and page context the visitor explicitly submitted."
                    ),
                    section(
                            "What this storage model is for",
                            "The point of storage is to support decision continuity, not to silently create public profiles.",
                            "Saved results exist so a household can reopen, share, or export a private decision snapshot.",
                            "Analytics are used to understand which clusters lead to testing, compare clicks, and follow-up demand.",
                            "Follow-up requests exist so high-intent visitors do not hit a dead end after a result."
                    ),
                    section(
                            "Current limits and handling rules",
                            "The current repo uses a lightweight storage model, so this page is intentionally explicit about its limits.",
                            "Private result snapshots expire instead of staying available forever.",
                            "Admin access should stay credential-protected and noindex.",
                            "If Google Analytics or Search Console verification is configured, it should stay limited to low-sensitivity route metadata and avoid sending saved-result content, email fields, or follow-up notes into shared analytics surfaces.",
                            "This product should avoid collecting more personal detail than is required for the follow-up or saved-result flow."
                    )
            ),
            page(
                    "commercial-disclosure",
                    "Commercial Disclosure",
                    "How Commercial Routing Works On This Site",
                    "Read how testing referrals, compare pages, lead capture, and future affiliate routing are constrained by evidence and safety rules.",
                    "Commercial disclosure",
                    "This page explains how money can flow through the product without letting commerce outrun testing, scope, or trust.",
                    "A well-water decision product should make the commercial model explicit because hidden incentives are a trust risk in YMYL categories.",
                    "2026-03-08",
                    section(
                            "What the commercial layer is allowed to do",
                            "Commercial routing is intentionally downstream from evidence and safety.",
                            "Testing, interpretation, and state guidance can appear before any product or partner handoff.",
                            "Category compare pages can be commercial bridges only when the page already explains what the category cannot solve.",
                            "Lead capture is allowed where the page has enough intent and context to justify a follow-up."
                    ),
                    section(
                            "What the commercial layer is not allowed to do",
                            "Some commercial patterns are blocked on purpose because they damage trust and decision quality.",
                            "No page should jump from one weak clue straight to a product verdict.",
                            "No affiliate or partner path should outrank certified testing on red or low-confidence paths.",
                            "No nuisance equipment module should be used to imply broad health protection."
                    ),
                    section(
                            "How to read a commercial CTA on this site",
                            "A commercial CTA should be read as a next-step option, not as proof that the problem is fully understood.",
                            "Internal compare pages are often the first commercial bridge because they keep claim-checks visible.",
                            "Testing and follow-up routes can be monetized without pretending the user is already ready to buy equipment.",
                            "If a CTA appears, the page should still state what remains unknown."
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
