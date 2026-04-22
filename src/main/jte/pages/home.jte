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
                    <h1>Choose your starting clue.</h1>
                    <p class="hero-lead">Pick one input and get the next safe step before you read anything deeper. This surface should behave like a routing tool first, not a guide directory.</p>
                    <div class="hero-actions">
                        <a class="button-link" href="${trackingLinks.home("home_primary_cta", "/tool/result-first")}">Start With A Lab Result</a>
                        <a class="button-link-secondary" href="${trackingLinks.home("home_secondary_cta", "/tool/symptom-first")}">I Only Have A Symptom</a>
                    </div>
                    <div class="chip-row" style="margin-top: 16px;">
                        <a class="chip" href="/tool">See the public tool page</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>Pick one path now</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">You do not need to browse first. Choose the clue you already have and let the tool narrow the path.</p>
                    <div class="hero-metrics">
                        <a class="metric" href="${trackingLinks.home("home_hero_route", "/tool/result-first")}">
                            <strong>Lab result</strong>
                            <span>Use one report line to start.</span>
                        </a>
                        <a class="metric" href="${trackingLinks.home("home_hero_route", "/tool/symptom-first")}">
                            <strong>Smell or stain</strong>
                            <span>Start from what you can see or smell.</span>
                        </a>
                        <a class="metric" href="${trackingLinks.home("home_hero_route", "/tool/trigger-first")}">
                            <strong>Recent change</strong>
                            <span>Use a flood, repair, or sudden shift.</span>
                        </a>
                        <a class="metric" href="${trackingLinks.home("home_hero_route", "/well-water/private-well-home-sale-testing-by-state")}">
                            <strong>State or sale</strong>
                            <span>Use rules and location when they change the answer.</span>
                        </a>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">What happens after you start</p>
                    <h2>Use the tool first, then read only if the first pass is not enough</h2>
                </div>
                <p class="section-copy">The first job is not browsing. The first job is to pick the input you already have, get oriented, and only then open the longer public guide layer if something is still unclear.</p>
            </div>
            <div class="grid grid-compact">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Start</span>
                    <h3>Pick the clue you already have</h3>
                    <p class="card-meta">Lab result, symptom, recent change, or state and home-sale context.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Orient</span>
                    <h3>Get the next safe step</h3>
                    <p class="card-meta">Let the tool narrow scope before treatment research gets ahead of the evidence.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Verify</span>
                    <h3>Check method or state context</h3>
                    <p class="card-meta">Use the reading layer only when you need the reasoning, source trail, or a local rule check.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Read</span>
                    <h3>Open the longer guide if needed</h3>
                    <p class="card-meta">Read only after the first pass tells you which branch, household, or test question still matters.</p>
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
                    <p class="brand-kicker">Guide layer</p>
                    <h2>High-signal pages worth opening only after you start the tool</h2>
                </div>
                <p class="section-copy">These guides exist to support the routing tool. They should not force you to learn the whole site before you can act.</p>
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
                    <p class="brand-kicker">Longer reading layer</p>
                    <h2>Only open these when the first routing pass still left you uncertain</h2>
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
                    <p class="brand-kicker">Trust layer</p>
                    <h2>Method, review, sources, and safety limits</h2>
                </div>
                <p class="section-copy">These pages are evidence and review support for the decision surface. They should sit behind the first action, not in front of it.</p>
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
