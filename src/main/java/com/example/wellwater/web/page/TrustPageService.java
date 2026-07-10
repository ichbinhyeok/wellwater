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
                    "How Water Verdict Handles Tool Inputs And Analytics",
                    "Understand what the web and ChatGPT tools process, what is retained, and what happens after an optional partner click.",
                    "Privacy and data",
                    "Water Verdict is designed to build a test plan without creating an account, collecting contact details, or storing the household inputs used to produce it.",
                    "The current tool minimizes collection by separating short-lived decision inputs from non-identifying operational counts.",
                    "2026-07-10",
                    section(
                            "What the test finder processes",
                            "The test finder processes only the options needed to build a plan, such as the reason for testing, visible clues, nearby risk context, state code, existing treatment, and intended water use.",
                            "Tool inputs are evaluated in application memory and are not written to the pivot-metrics file.",
                            "Do not submit names, email addresses, street addresses, medical histories, lab reports, or other sensitive personal information.",
                            "The ChatGPT app receives tool arguments from OpenAI; OpenAI handles the surrounding conversation under its own terms and privacy controls."
                    ),
                    section(
                            "What is retained",
                            "The pivot measures only aggregate operational fields needed to judge whether the automated product works.",
                            "Recorded fields are date, event type, web or ChatGPT channel, result family, optional partner product code, outcome, and latency.",
                            "No raw answers, account identifiers, session identifiers, IP addresses, contact details, or precise locations are written to that metric file.",
                            "Aggregate operational metrics may be retained for up to 13 months, then deleted or reduced to longer-term totals."
                    ),
                    section(
                            "External services and legacy data",
                            "A partner receives normal browser request data only after a visitor chooses an external affiliate link; Water Verdict does not run the partner checkout.",
                            "Google Analytics, when enabled, is limited to public route behavior and must not receive tool answers or result content.",
                            "Older saved-result and lead files may remain from the previous product model, but new follow-up submissions are disabled and the new test finder does not create saved snapshots.",
                            "Administrative metric summaries remain credential-protected, noindex, and unavailable through the public tool."
                    )
            ),
            page(
                    "commercial-disclosure",
                    "Commercial Disclosure",
                    "How Optional Test-Kit Links Work On Water Verdict",
                    "Read when a physical test-kit link may appear, when it is suppressed, and how an affiliate relationship affects the site.",
                    "Commercial disclosure",
                    "Water Verdict may earn a commission when a visitor buys an eligible physical test kit after following a clearly labeled partner link.",
                    "The recommendation engine chooses the testing scope first. Affiliate availability cannot add tests, lower urgency, or override official guidance.",
                    "2026-07-10",
                    section(
                            "What an affiliate link means",
                            "The current commercial route is limited to optional Tap Score physical well-water test kits configured by the operator.",
                            "The visitor completes any purchase, sample registration, payment, and fulfillment on the partner site.",
                            "Water Verdict does not increase the price and does not receive the visitor's checkout or laboratory result data.",
                            "If approved partner URLs are not configured, no commercial offer is shown."
                    ),
                    section(
                            "When the offer is suppressed",
                            "The engine blocks affiliate output when the situation needs a higher-evidence or urgent path.",
                            "No offer appears for flood, wildfire, known contamination, home-purchase, PFAS, fuel, radionuclide, or immediate-escalation paths.",
                            "Official state guidance and certified-laboratory directories remain available whether or not an affiliate relationship exists.",
                            "No physical kit is presented as a medical diagnosis, legal clearance, or guarantee that treatment is unnecessary."
                    ),
                    section(
                            "How to read the recommendation",
                            "A kit link is an optional way to purchase the panel, not the evidence behind the panel selection.",
                            "Users may take the same recommended panel to another appropriately certified laboratory.",
                            "The recommendation explains why each panel item was included before presenting any commercial route.",
                            "Questions about the recommendation or disclosure can be sent to support@waterverdict.com."
                    )
            ),
            page(
                    "support",
                    "Support",
                    "Water Verdict Support",
                    "Get support for the Water Verdict private-well test finder, privacy questions, or affiliate disclosures.",
                    "Support",
                    "For product, privacy, or commercial-disclosure questions, email support@waterverdict.com.",
                    "Support is asynchronous and does not provide emergency, medical, legal, or laboratory services.",
                    "2026-07-10",
                    section(
                            "What support can help with",
                            "Support covers the automated test finder and its published operating rules.",
                            "Report a broken tool flow, inaccessible page, or incorrect official-resource link.",
                            "Ask how aggregate metrics, ChatGPT tool inputs, or affiliate links are handled.",
                            "Include the page URL and a short description, but do not send lab reports, medical details, account credentials, or other sensitive data."
                    ),
                    section(
                            "What support cannot do",
                            "Water Verdict does not operate a manual consulting or interpretation service.",
                            "For urgent health or contamination concerns, use local public-health or environmental authorities.",
                            "For sampling, analysis, or result questions, contact an appropriately certified drinking-water laboratory.",
                            "For orders, refunds, kit fulfillment, or laboratory status, contact the external seller shown at checkout."
                    )
            ),
            page(
                    "terms",
                    "Terms Of Use",
                    "Water Verdict Terms Of Use",
                    "Read the terms that apply to the Water Verdict website, private-well test finder, MCP app, and external partner links.",
                    "Terms of use",
                    "By using Water Verdict, you agree to use it as educational testing decision support and not as emergency, medical, legal, laboratory, or regulatory advice.",
                    "These terms cover the public website and the MCP-backed app distributed through an OpenAI plugin.",
                    "2026-07-10",
                    section(
                            "Permitted use and product limits",
                            "Water Verdict helps users choose a private-well testing scope from limited situation inputs.",
                            "You must not rely on a result as proof that water is safe, compliant, or suitable for a particular person.",
                            "Use certified laboratories and applicable state or local authorities for sampling, analysis, transactions, and urgent contamination events.",
                            "Do not submit credentials, payment data, government identifiers, medical records, lab reports, or other restricted or sensitive information."
                    ),
                    section(
                            "External resources and purchases",
                            "Results may link to government resources, certified-laboratory directories, or an optional physical test kit sold by a third party.",
                            "External sites control their own content, availability, checkout, fulfillment, refunds, testing, and privacy practices.",
                            "Water Verdict may earn a commission from a clearly disclosed eligible physical-kit link, but that relationship does not change the testing logic.",
                            "A partner link is optional and does not create a laboratory, professional-services, or sales contract with Water Verdict."
                    ),
                    section(
                            "Availability, warranties, and contact",
                            "The service is provided as available and may change, pause, or remove unsupported functionality.",
                            "To the extent permitted by law, Water Verdict disclaims implied warranties and is not liable for decisions made in place of certified testing or official guidance.",
                            "You remain responsible for checking the recommendation against current local requirements and qualified professional advice.",
                            "Questions about these terms can be sent to support@waterverdict.com."
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
