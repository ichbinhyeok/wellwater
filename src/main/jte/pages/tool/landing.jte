@param com.example.wellwater.web.page.SeoMetadata seo
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus
@param com.example.wellwater.web.page.PublicTrackingLinkService trackingLinks

@template.layout.main(title = "Private Well Water Decision Tool | Water Verdict", robots = seo.robotsDirective(), description = "Start with a lab result, symptom, recent change, or state and home-sale context and get the next testing path for a private well.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Public tool surface</span>
                    <h1>Private Well Water Decision Tool</h1>
                    <p class="hero-lead">Wide public surface, narrow decision wedge. Start from one clue, get the next safe path, and use longer reading only when the first routing pass is not enough.</p>
                    <div class="hero-actions">
                        <a class="button-link" href="${trackingLinks.toolLanding("tool_hub_primary_cta", "/tool/result-first")}">Start With A Lab Result</a>
                        <a class="button-link-secondary" href="${trackingLinks.toolLanding("tool_hub_secondary_cta", "/tool/symptom-first")}">I Only Have A Symptom</a>
                    </div>
                    <div class="chip-row" style="margin-top: 16px;">
                        <span class="chip">Tool first</span>
                        <span class="chip">Guide second</span>
                        <span class="chip">Private result wedge</span>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>What this page does</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">This page is the indexable tool surface. It explains the inputs, outputs, and route choices in public, while personalized results and saved views stay out of search.</p>
                    <div class="hero-metrics">
                        <a class="metric" href="${trackingLinks.toolLanding("tool_hub_route", "/tool/result-first")}">
                            <strong>Lab result</strong>
                            <span>Use one report line to start the engine.</span>
                        </a>
                        <a class="metric" href="${trackingLinks.toolLanding("tool_hub_route", "/tool/symptom-first")}">
                            <strong>Symptom</strong>
                            <span>Start from smell, stain, or taste.</span>
                        </a>
                        <a class="metric" href="${trackingLinks.toolLanding("tool_hub_route", "/tool/trigger-first")}">
                            <strong>Recent change</strong>
                            <span>Use flood, repair, or sudden shift.</span>
                        </a>
                        <a class="metric" href="/well-water/private-well-home-sale-testing-by-state">
                            <strong>State or sale</strong>
                            <span>Use local rules and transaction timing.</span>
                        </a>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Inputs</p>
                    <h2>Pick the clue you already have</h2>
                </div>
                <p class="section-copy">The first move should be choosing an input, not opening a long article. Each route narrows the problem before product research or treatment comparisons begin.</p>
            </div>
            <div class="grid">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Lab result</span>
                    <h3>One analyte line is enough to start</h3>
                    <p class="card-meta">Enter analyte, value, and unit. Add qualifier, state, household, or companion lines only when they materially change the next step.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Symptom</span>
                    <h3>Start from what you can observe</h3>
                    <p class="card-meta">Smell, stain, taste, or nuisance signals can still route to safer testing steps even when the data is incomplete.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Trigger</span>
                    <h3>Use the event that changed the water</h3>
                    <p class="card-meta">Floods, repairs, drought shifts, or transfer timing are often strong routing signals before a clean report exists.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">State context</span>
                    <h3>Apply local rules only when they change the answer</h3>
                    <p class="card-meta">State guidance and home-sale timing belong in the decision path when they alter what should happen first.</p>
                </article>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Outputs</p>
                    <h2>What the engine gives back</h2>
                </div>
                <p class="section-copy">The output is not a generic article. The engine narrows the next move, the follow-up tests, and the places where you should slow down before buying anything.</p>
            </div>
            <div class="grid">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Decision call</span>
                    <h3>What matters first</h3>
                    <p class="card-meta">The result surface separates health-risk routing from nuisance routing so one symptom or one report line does not drive the wrong purchase.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Recommended tests</span>
                    <h3>What to verify next</h3>
                    <p class="card-meta">You get the ordered next tests, retest logic, and state-aware handoff when local programs or certified labs matter.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Treatment hold</span>
                    <h3>What not to buy first</h3>
                    <p class="card-meta">The tool deliberately slows category shopping until scope, claims, and evidence are clear enough to compare real options.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Context links</span>
                    <h3>Why the routing happened</h3>
                    <p class="card-meta">Method pages, state pages, and public guides sit behind the output to explain the reasoning after the route is already clear.</p>
                </article>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Wide Surface</p>
                    <h2>Public pages should feed the tool, not replace it</h2>
                </div>
                <p class="section-copy">This is the broad surface strategy: public pages stay indexable where they earn it, but they should route into the wedge instead of becoming the product by accident.</p>
            </div>
            <div class="grid">
                <a class="card-link card-link-accent" href="/well-water/family/regional">
                    <span class="card-kicker">State router</span>
                    <h3>Regional family</h3>
                    <p class="card-meta">Use state pages when geology, required panels, or certified-lab paths change what should happen first.</p>
                </a>
                <a class="card-link" href="/well-water/family/authority">
                    <span class="card-kicker">Method router</span>
                    <h3>Authority family</h3>
                    <p class="card-meta">Use method pages to choose the right decision path, not to browse in the abstract before starting.</p>
                </a>
                <a class="card-link" href="/well-water/private-well-home-sale-testing-by-state">
                    <span class="card-kicker">Transaction route</span>
                    <h3>Home-sale by state</h3>
                    <p class="card-meta">Lead with transfer timing and state context when they materially change testing scope and urgency.</p>
                </a>
                <a class="card-link" href="/well-water/nitrate">
                    <span class="card-kicker">Decision page</span>
                    <h3>Nitrate example page</h3>
                    <p class="card-meta">A strong public page should still push the user toward diagnosis, scope, and result-specific next steps.</p>
                </a>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Boundary</p>
                    <h2>What stays public and what stays private</h2>
                </div>
                <p class="section-copy">The public surface should be crawlable enough to explain the product. The result wedge should stay private enough to keep personalized outputs and saved views out of search.</p>
            </div>
            <div class="grid">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Public</span>
                    <h3>This indexable tool page</h3>
                    <p class="card-meta">Search engines and first-time visitors can understand the inputs, output shape, and the decision logic without needing a personal result page.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Public</span>
                    <h3>Router pages and high-intent guides</h3>
                    <p class="card-meta">Regional, authority, and selected decision pages remain the wide surface as long as they keep routing behavior ahead of long reading.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Private</span>
                    <h3>Entry flows under <code>/tool/*</code></h3>
                    <p class="card-meta">The actual intake routes remain non-indexed so Google sees the product shape, but not user-specific form states as the main crawl surface.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Private</span>
                    <h3>Saved results under <code>/result/saved/*</code></h3>
                    <p class="card-meta">Personalized output, saved views, and downloadable result artifacts stay out of search and out of the public content layer.</p>
                </article>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Start now</p>
                    <h2>Use the narrow wedge directly</h2>
                </div>
                <p class="section-copy">These routes go straight into the decision wedge. Use them when you already know which clue is strongest.</p>
            </div>
            <div class="grid">
                <a class="card-link card-link-accent" href="${trackingLinks.toolLanding("tool_hub_start_card", "/tool/result-first")}">
                    <span class="card-kicker">Start</span>
                    <h3>Lab result entry</h3>
                    <p class="card-meta">Best when you have one report line and want the next test or treatment hold decision fast.</p>
                </a>
                <a class="card-link" href="${trackingLinks.toolLanding("tool_hub_start_card", "/tool/symptom-first")}">
                    <span class="card-kicker">Start</span>
                    <h3>Symptom entry</h3>
                    <p class="card-meta">Best when the problem is visible or obvious but the lab data is partial, stale, or missing.</p>
                </a>
                <a class="card-link" href="${trackingLinks.toolLanding("tool_hub_start_card", "/tool/trigger-first")}">
                    <span class="card-kicker">Start</span>
                    <h3>Recent change entry</h3>
                    <p class="card-meta">Best when the event itself changed what should happen first, even before a new certified report is in hand.</p>
                </a>
                <a class="card-link" href="/well-water/private-well-home-sale-testing-by-state">
                    <span class="card-kicker">Route</span>
                    <h3>State and home-sale path</h3>
                    <p class="card-meta">Best when local requirements, program guidance, or real-estate timing should define the testing sequence.</p>
                </a>
            </div>
        </section>

        @if(leadContext != null)
            <section class="panel" id="lead-capture">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">Lead capture</p>
                        <h2>${leadContext.heading()}</h2>
                    </div>
                    <p class="section-copy">${leadContext.summary()}</p>
                </div>
                @if("success".equals(leadStatus))
                    <p class="section-copy" style="color: #0f766e; font-weight: 700;">Lead captured. Check <code>/admin</code> for the new row.</p>
                @elseif("invalid".equals(leadStatus))
                    <p class="section-copy warn">Enter a valid email before submitting the follow-up request.</p>
                @endif
                <form method="post" action="/lead/submit" class="form-grid" style="margin-top: 18px;">
                    <input type="hidden" name="redirectPath" value="${leadContext.redirectPath()}">
                    <input type="hidden" name="sourceType" value="${leadContext.sourceType()}">
                    <input type="hidden" name="sourceFamily" value="${leadContext.sourceFamily()}">
                    <input type="hidden" name="sourceSlug" value="${leadContext.sourceSlug()}">
                    <input type="hidden" name="sourceLabel" value="${leadContext.sourceLabel()}">
                    <input class="form-field" type="email" name="email" placeholder="you@example.com" required>
                    <input class="form-field" type="text" name="state" maxlength="2" placeholder="State (optional)">
                    <textarea class="form-textarea" name="note" placeholder="What clue do you have: lab line, symptom, recent change, or home-sale context?"></textarea>
                    <button type="submit" class="button-link" style="grid-column: 1 / -1; justify-content: center;">${leadContext.submitLabel()}</button>
                </form>
            </section>
        @endif
    </main>
`)
