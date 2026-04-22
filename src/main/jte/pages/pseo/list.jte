@param String family
@param java.util.List<com.example.wellwater.pseo.PseoPage> pages
@param com.example.wellwater.pseo.PseoFamilyView familyView
@param com.example.wellwater.web.page.SeoMetadata seo
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus

!{
    var isAuthority = "authority".equals(family);
    var isRegional = "regional".equals(family);
    var isTrigger = "triggers".equals(family);
    var isSymptom = "symptoms".equals(family);

    var heroEyebrow = isAuthority
            ? "Method router"
            : (isRegional
            ? "State context router"
            : (isTrigger ? "Event router" : "Decision router"));

    var heroLead = isAuthority
            ? "Use this family to choose the right tool entry before you open a long method article."
            : (isRegional
            ? "Use this family to decide whether state context, a lab result, or a recent change should drive the next step."
            : (isTrigger
            ? "Use this family to choose the right event-driven entry path before you browse every guide."
            : "Use this family to choose the right tool entry before it turns into a browse session."));

    var heroNoteTitle = isAuthority || isRegional
            ? "This is a router, not a reading destination"
            : "Pick the first path, not the first article";

    var heroNoteBody = isAuthority
            ? "The method layer exists to feed the decision engine. It should not replace the decision engine."
            : (isRegional
            ? "State pages should narrow the path, not become a directory you browse before acting."
            : "Choose the entry path first. Open a guide only if the first pass still leaves you uncertain.");

    var secondaryHeroLabel = isRegional
            ? "I Already Have A Lab Result"
            : (isAuthority
            ? "I Need State Or Sale Context"
            : (isSymptom ? "I Already Have A Lab Result" : (isTrigger ? "I Only Have A Symptom" : "I Only Have A Symptom")));

    var secondaryHeroHref = isRegional
            ? "/tool/result-first"
            : (isAuthority
            ? "/well-water/private-well-home-sale-testing-by-state"
            : (isSymptom ? "/tool/result-first" : (isTrigger ? "/tool/symptom-first" : "/tool/symptom-first")));

    var routeOneLabel = isRegional
            ? "I need state or home-sale context"
            : (isAuthority
            ? "I already have a lab result"
            : (isTrigger
            ? "Something changed recently"
            : (isSymptom ? "I have a smell, stain, or taste problem" : "I have a lab result")));
    var routeOneHref = isRegional
            ? "/well-water/private-well-home-sale-testing-by-state"
            : (isAuthority
            ? "/tool/result-first"
            : (isTrigger ? "/tool/trigger-first" : (isSymptom ? "/tool/symptom-first" : "/tool/result-first")));
    var routeOneBody = isRegional
            ? "Start here when rules, transaction timing, or local geology change what should happen first."
            : (isAuthority
            ? "Apply the method layer to your own well instead of reading in the abstract."
            : (isTrigger
            ? "Use the event itself to decide whether you should retest, inspect, or escalate."
            : (isSymptom
            ? "Start from what you can see or smell when the data is weak or missing."
            : "Use one analyte from a report to get the next test, scope, and verification path.")));

    var routeTwoLabel = isRegional
            ? "I already have a lab result"
            : (isAuthority ? "I only have a symptom or vague concern" : "I already have a lab result");
    var routeTwoHref = isRegional
            ? "/tool/result-first"
            : (isAuthority ? "/tool/symptom-first" : "/tool/result-first");
    var routeTwoBody = isRegional
            ? "Use the engine when state context matters but you already have one result line."
            : (isAuthority
            ? "Start with a symptom when you need triage before you have clean report data."
            : "Use the result flow when you already have evidence and do not need a long family browse.");

    var routeThreeLabel = isRegional
            ? "Something changed recently"
            : (isAuthority ? "I need state or home-sale context" : "I only have a symptom or recent change");
    var routeThreeHref = isRegional
            ? "/tool/trigger-first"
            : (isAuthority ? "/well-water/private-well-home-sale-testing-by-state" : "/tool/symptom-first");
    var routeThreeBody = isRegional
            ? "Use a recent change, flood, repair, or shift when the state context alone is not enough."
            : (isAuthority
            ? "Open the state and home-sale layer only when local rules or transfer timing shape the answer."
            : "Use the symptom path when you need triage before you have a clean report line.");
}

@template.layout.main(title = familyView.heroTitle(), robots = seo.robotsDirective(), description = familyView.heroLead(), canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">${heroEyebrow}</span>
                    <h1>${familyView.heroTitle()}</h1>
                    <p class="hero-lead">${heroLead}</p>
                    <div class="hero-actions">
                        <a class="button-link" href="${familyView.primaryToolHref()}">${familyView.primaryToolLabel()}</a>
                        <a class="button-link-secondary" href="${secondaryHeroHref}">${secondaryHeroLabel}</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>${heroNoteTitle}</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">${heroNoteBody}</p>
                    <div class="chip-row">
                        @if(isAuthority)
                            <span class="chip">Decision first</span>
                            <span class="chip">Method second</span>
                            <span class="chip">Do not browse blind</span>
                        @elseif(isRegional)
                            <span class="chip">State context</span>
                            <span class="chip">Lab result</span>
                            <span class="chip">Recent change</span>
                        @else
                            <span class="chip">Pick an input</span>
                            <span class="chip">Run the tool</span>
                            <span class="chip">Read only if needed</span>
                        @endif
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Start here</p>
                    <h2>Pick the first path, not the first article</h2>
                </div>
                <p class="section-copy">This family should route you into the right entry flow before it turns into a flat browse list.</p>
            </div>
            <div class="grid">
                <a class="card-link card-link-accent" href="${routeOneHref}">
                    <span class="card-kicker">Best first path</span>
                    <h3>${routeOneLabel}</h3>
                    <p class="card-meta">${routeOneBody}</p>
                </a>
                <a class="card-link" href="${routeTwoHref}">
                    <span class="card-kicker">Alternative path</span>
                    <h3>${routeTwoLabel}</h3>
                    <p class="card-meta">${routeTwoBody}</p>
                </a>
                <a class="card-link" href="${routeThreeHref}">
                    <span class="card-kicker">Alternative path</span>
                    <h3>${routeThreeLabel}</h3>
                    <p class="card-meta">${routeThreeBody}</p>
                </a>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Guide layer</p>
                    <h2>Use one of these pages only if the first pass was not enough</h2>
                </div>
                <p class="section-copy">These are the reading pages worth opening after you already know which branch or question still matters.</p>
            </div>
            <div class="grid">
                @for (var starter : familyView.starterPages())
                    <a class="card-link ${isAuthority ? "" : "card-link-accent"}" href="/well-water/${starter.slug()}">
                        <span class="card-kicker">${starter.family()}</span>
                        <h3>${starter.h1()}</h3>
                        <p class="card-meta">${starter.metaDescription()}</p>
                    </a>
                @endfor
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Before you open more guides</p>
                    <h2>What this family should stop you from doing</h2>
                </div>
                <p class="section-copy">The family layer is supposed to reduce browsing drift, not expand it. Use these checks to decide whether you still need the reading layer at all.</p>
            </div>
            <div class="grid">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Common mistake</span>
                    <h3>What people get wrong first in this family</h3>
                    <div class="stack" style="gap: 10px;">
                        @for (var mistake : familyView.commonMistakes())
                            <p class="card-meta">${mistake}</p>
                        @endfor
                    </div>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Before more reading</span>
                    <h3>Three checks before you open the longer guide layer</h3>
                    <div class="stack" style="gap: 10px;">
                        @for (var check : familyView.beforeToolChecks())
                            <p class="card-meta">${check}</p>
                        @endfor
                    </div>
                </article>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Longer reading layer</p>
                    <h2>Browse the full family only when the router still left a real question</h2>
                </div>
                <p class="section-copy">${"compares".equals(family)
                        ? "Use comparison pages after you confirm the likely problem and the part of the house you are trying to protect."
                        : "Each page should explain what the clue means, what to verify next, and when not to buy yet."}</p>
            </div>
            <div class="grid">
                @for (var page : pages)
                    <a class="card-link" href="/well-water/${page.slug()}">
                        <span class="card-kicker">${page.family()}</span>
                        <h3>${page.h1()}</h3>
                        <p class="card-meta">${page.metaDescription()}</p>
                    </a>
                @endfor
            </div>
        </section>

        @if(leadContext != null)
            <section class="panel" id="lead-capture">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">Lead capture</p>
                        <h2>${leadContext.heading()}</h2>
                    </div>
                </div>
                <p class="section-copy">${leadContext.summary()}</p>
                @if("success".equals(leadStatus))
                    <p class="section-copy" style="color: #0f766e; font-weight: 700;">Lead captured. Check <code>/admin</code> for the new row.</p>
                @elseif("invalid".equals(leadStatus))
                    <p class="section-copy" style="color: #c66a2b; font-weight: 700;">Enter a valid email address to submit this follow-up request.</p>
                @endif
                <form method="post" action="/lead/submit" class="form-grid" style="margin-top: 18px;">
                    <input type="hidden" name="redirectPath" value="${leadContext.redirectPath()}">
                    <input type="hidden" name="sourceType" value="${leadContext.sourceType()}">
                    <input type="hidden" name="sourceFamily" value="${leadContext.sourceFamily()}">
                    <input type="hidden" name="sourceSlug" value="${leadContext.sourceSlug()}">
                    <input type="hidden" name="sourceLabel" value="${leadContext.sourceLabel()}">
                    <input type="text" name="website" tabindex="-1" autocomplete="off" style="position:absolute;left:-9999px;width:1px;height:1px;opacity:0;" aria-hidden="true">
                    <input type="email" name="email" placeholder="you@example.com" required class="form-field">
                    <div class="form-grid two">
                        <input type="text" name="name" placeholder="Name (optional)" class="form-field">
                        <input type="text" name="state" placeholder="State (optional)" maxlength="12" class="form-field">
                    </div>
                    <textarea name="note" rows="4" placeholder="What follow-up do you want from this topic?" class="form-textarea"></textarea>
                    <button type="submit" class="button-link" style="grid-column: 1 / -1; justify-content: center;">${leadContext.submitLabel()}</button>
                </form>
            </section>
        @endif
    </main>
`)
