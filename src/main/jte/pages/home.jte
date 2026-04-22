@param java.util.Map<String, Long> familyCounts
@param Integer totalPageCount
@param java.util.List<com.example.wellwater.pseo.PseoPage> priorityPages
@param java.util.List<com.example.wellwater.pseo.PseoPage> featuredRegionalPages
@param java.util.List<com.example.wellwater.web.page.TrustPage> trustPages
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus
@param com.example.wellwater.web.page.SeoMetadata seo
@param com.example.wellwater.web.page.PublicTrackingLinkService trackingLinks

@template.layout.main(title = "Water Verdict | Private Well Testing Decisions", robots = seo.robotsDirective(), description = "Decide what to test next for a private well, especially during home purchase, state-specific testing, and suspicious result follow-up.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Private well decision tool</span>
                    <h1>Check your well water before you buy anything.</h1>
                    <p class="hero-lead">Start from one clue you already have: a lab result, a smell or stain, a recent change, or a state or home-sale requirement. The goal is to decide the next test before product shopping takes over.</p>
                    <div class="chip-row" style="margin-top: 18px;">
                        <span class="chip">Lab result</span>
                        <span class="chip">Smell or stain</span>
                        <span class="chip">Recent change</span>
                        <span class="chip">State or home sale</span>
                    </div>
                    <div class="hero-actions">
                        <a class="button-link" href="${trackingLinks.home("home_primary_cta", "/tool/result-first")}">Open The Tool</a>
                        <a class="button-link-secondary" href="${trackingLinks.home("home_secondary_cta", "/well-water/private-well-home-sale-testing-by-state")}">Start With State Or Home Sale</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>Start in 10 seconds</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">You do not need to read the whole site first. Pick the closest starting point, get oriented, and only then go deeper.</p>
                    <div class="hero-metrics">
                        <div class="metric">
                            <strong>1</strong>
                            <span>Pick the clue you already have</span>
                        </div>
                        <div class="metric">
                            <strong>2</strong>
                            <span>Get the next safe step</span>
                        </div>
                        <div class="metric">
                            <strong>3</strong>
                            <span>Read more only if you need it</span>
                        </div>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Start here</p>
                    <h2>Pick the closest starting point</h2>
                </div>
                <p class="section-copy">First choose the signal you already have. The longer reading can wait until after the problem is narrowed.</p>
            </div>
            <div class="grid">
                <a class="card-link card-link-accent" href="${trackingLinks.home("home_start_path", "/tool/result-first")}">
                    <span class="card-kicker">Tool</span>
                    <h3>I have a lab result</h3>
                    <p class="card-meta">Enter one analyte from a report and get the next test, scope, and verification path.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_start_path", "/tool/symptom-first")}">
                    <span class="card-kicker">Tool</span>
                    <h3>I have a smell, stain, or taste problem</h3>
                    <p class="card-meta">Start from what you can see or smell when the data is weak or missing.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_start_path", "/tool/trigger-first")}">
                    <span class="card-kicker">Tool</span>
                    <h3>I have a recent change</h3>
                    <p class="card-meta">Use a flood, repair, treatment change, or sudden shift to decide whether to retest or escalate.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_start_path", "/well-water/private-well-home-sale-testing-by-state")}">
                    <span class="card-kicker">Guide</span>
                    <h3>I need state or home-sale guidance</h3>
                    <p class="card-meta">Start here when rules, transfer timing, or state context shape what should happen first.</p>
                </a>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">What happens next</p>
                    <h2>Use the tool first, then read deeper only if you need it</h2>
                </div>
                <p class="section-copy">The site is supposed to work in two layers: first get oriented, then open the longer reading only when the next step is still unclear.</p>
            </div>
            <div class="grid grid-compact">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Start</span>
                    <h3>Pick the closest input</h3>
                    <p class="card-meta">Lab result, symptom, recent change, or state and home-sale context.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Orient</span>
                    <h3>Get the next safe step</h3>
                    <p class="card-meta">The tool should narrow scope before treatment research gets ahead of the evidence.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Read</span>
                    <h3>Open the public guide if needed</h3>
                    <p class="card-meta">Use the article layer only when you want the reasoning, source trail, or a deeper branch explanation.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Compare</span>
                    <h3>Shop only after scope is clear</h3>
                    <p class="card-meta">Compare paths and follow-up CTAs should stay downstream from verification, not replace it.</p>
                </article>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Browse if you want more context</p>
                    <h2>High-signal pages worth reading after you know your starting point</h2>
                </div>
                <p class="section-copy">This is the longer reading layer. It should support the tool, not force you to learn the whole site before you can act.</p>
            </div>
            <div class="grid">
                @for (int i = 0; i < priorityPages.size(); i++)
                    !{var page = priorityPages.get(i);}
                    @if (i < 8)
                        <a class="card-link ${i >= 6 ? "mobile-card-hidden" : ""}" href="${trackingLinks.home("home_priority_page", "/well-water/" + page.slug())}">
                            <span class="card-kicker">${page.family()}</span>
                            <h3>${page.h1()}</h3>
                            <p class="card-meta">${page.metaDescription()}</p>
                        </a>
                    @endif
                @endfor
            </div>
            <div class="chip-row mobile-only" style="margin-top: 18px;">
                <a class="chip" href="/well-water/private-well-home-sale-testing-by-state">Home sale</a>
                <a class="chip" href="/well-water/family/regional">State guides</a>
                <a class="chip" href="/well-water/family/triggers">Triggers</a>
                <a class="chip" href="/well-water/family/authority">Testing order</a>
            </div>
        </section>

        @if (!featuredRegionalPages.isEmpty())
            <section class="panel panel-accent">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">State-specific paths</p>
                        <h2>State pages with official guidance and lab handoff</h2>
                    </div>
                    <p class="section-copy">These state pages are most useful when local rules, geology, or certified-lab routing change what you should do next.</p>
                </div>
                <div class="grid">
                    @for (var page : featuredRegionalPages)
                        <a class="card-link card-link-accent" href="${trackingLinks.home("home_featured_regional", "/well-water/" + page.slug())}">
                            <span class="card-kicker">${page.family()}</span>
                            <h3>${page.h1()}</h3>
                            <p class="card-meta">${page.metaDescription()}</p>
                        </a>
                    @endfor
                </div>
            </section>
        @endif

        <section class="panel desktop-only">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Deeper browsing</p>
                    <h2>Open these only when the first pass was not enough</h2>
                </div>
                <p class="section-copy">These hubs are useful for people who already know the broad direction and want the longer reading path.</p>
            </div>
            <div class="grid grid-compact">
                <a class="card-link" href="${trackingLinks.home("home_deeper_path", "/well-water/private-well-home-sale-testing-by-state")}">
                    <span class="card-kicker">Home sale guide</span>
                    <h3>Home sale testing by state</h3>
                    <p class="card-meta">Lead with transaction timing, required panels, and where a state checklist is still too thin.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_deeper_path", "/well-water/family/regional")}">
                    <span class="card-kicker">State guide hub</span>
                    <h3>State-specific guides</h3>
                    <p class="card-meta">${familyCounts.get("regional")} pages where geology, rules, and certified-lab paths change the answer.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_deeper_path", "/well-water/family/authority")}">
                    <span class="card-kicker">Testing guide hub</span>
                    <h3>Testing-order articles</h3>
                    <p class="card-meta">${familyCounts.get("authority")} articles covering sampling, lab-reading, and checklist logic before product research.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_deeper_path", "/well-water/family/triggers")}">
                    <span class="card-kicker">Retest guide hub</span>
                    <h3>Trigger and retest guides</h3>
                    <p class="card-meta">${familyCounts.get("triggers")} pages for home purchase, flood response, repairs, and retest timing.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_deeper_path", "/well-water/arsenic")}">
                    <span class="card-kicker">Result guide</span>
                    <h3>Named result interpretation</h3>
                    <p class="card-meta">Use result guides to clarify what a contaminant or symptom means before you compare products.</p>
                </a>
                <a class="card-link" href="${trackingLinks.home("home_deeper_path", "/well-water/metallic-taste-plumbing-vs-source-water")}">
                    <span class="card-kicker">Suspicious clue follow-up</span>
                    <h3>Plumbing vs source-water splits</h3>
                    <p class="card-meta">Use a proven corrosion cluster to separate fixture clues from source-water risk before you open treatment comparisons.</p>
                </a>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Trust surface</p>
                    <h2>Method, review, sources, and safety limits</h2>
                </div>
                <p class="section-copy">These pages explain how advice is reviewed, where the source trail comes from, and when testing should stay ahead of product research.</p>
            </div>
            <div class="grid">
                @for (int i = 0; i < trustPages.size(); i++)
                    !{var page = trustPages.get(i);}
                    <a class="card-link ${i >= 3 ? "mobile-card-hidden" : ""}" href="/trust/${page.slug()}">
                        <span class="card-kicker">${page.eyebrow()}</span>
                        <h3>${page.h1()}</h3>
                        <p class="card-meta">${page.metaDescription()}</p>
                    </a>
                @endfor
                <a class="card-link mobile-only" href="/trust">
                    <span class="card-kicker">Trust hub</span>
                    <h3>Open the full trust surface</h3>
                    <p class="card-meta">Review methodology, reviewers, privacy, and disclosure without scrolling through the entire directory.</p>
                </a>
            </div>
        </section>

        @if(leadContext != null)
            <section class="panel" id="lead-capture">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">Still unsure?</p>
                        <h2>${leadContext.heading()}</h2>
                    </div>
                    <p class="section-copy">${leadContext.summary()}</p>
                </div>
                @if("success".equals(leadStatus))
                    <p class="section-copy" style="color: #0f766e; font-weight: 700;">Follow-up request captured. Check <code>/admin</code> for the new row.</p>
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
                    <div class="form-grid two">
                        <input type="email" name="email" placeholder="you@example.com" required class="form-field">
                        <input type="text" name="state" placeholder="State (optional)" maxlength="12" class="form-field">
                    </div>
                    <textarea name="note" rows="4" placeholder="What seems off? Example: sulfur smell, stains, home purchase, or a result you do not trust yet." class="form-textarea"></textarea>
                    <button type="submit" class="button-link" style="justify-content: center;">${leadContext.submitLabel()}</button>
                </form>
            </section>
        @endif
    </main>
`)
