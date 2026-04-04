@param java.util.Map<String, Long> familyCounts
@param Integer totalPageCount
@param java.util.List<com.example.wellwater.pseo.PseoPage> priorityPages
@param java.util.List<com.example.wellwater.pseo.PseoPage> featuredRegionalPages
@param java.util.List<com.example.wellwater.web.page.TrustPage> trustPages
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "Water Verdict | Private Well Testing Decisions", robots = seo.robotsDirective(), description = "Decide what to test next for a private well, especially during home purchase, state-specific testing, and suspicious result follow-up.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Private well testing decisions</span>
                    <h1>Decide what to test next before a home purchase, state rule, or suspicious result turns into an expensive guess.</h1>
                    <p class="hero-lead">Start with state-specific testing guidance, home-sale timing, report interpretation, and the next safe verification step before equipment shopping takes over.</p>
                    <div class="hero-actions">
                        <a class="button-link" href="/well-water/private-well-home-sale-testing-by-state">Start With Home Sale Or State Rules</a>
                        <a class="button-link-secondary" href="/tool/result-first">Start From Lab Results</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>Where to start</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">Most people need one of four things first: local rules, home-sale timing, help reading a report, or a way to verify whether a symptom points to plumbing, source water, or both.</p>
                    <div class="hero-metrics">
                        <div class="metric">
                            <strong>State</strong>
                            <span>specific guidance</span>
                        </div>
                        <div class="metric">
                            <strong>Home sale</strong>
                            <span>and transfer timing</span>
                        </div>
                        <div class="metric">
                            <strong>Result</strong>
                            <span>interpretation support</span>
                        </div>
                        <div class="metric">
                            <strong>Testing</strong>
                            <span>order before shopping</span>
                        </div>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Start points</p>
                    <h2>Four reliable ways to narrow the problem</h2>
                </div>
                <p class="section-copy">These are the pages most likely to answer the first question clearly enough that you can choose the next test, not just browse treatment categories.</p>
            </div>
            <div class="grid">
                <a class="card-link card-link-accent" href="/well-water/private-well-home-sale-testing-by-state">
                    <span class="card-kicker">Transaction intent</span>
                    <h3>Home purchase and transfer testing</h3>
                    <p class="card-meta">Use state requirements, lender timing, and checklist logic to decide what a transfer panel does not answer.</p>
                </a>
                <a class="card-link" href="/well-water/family/regional">
                    <span class="card-kicker">Regional intent</span>
                    <h3>State-specific testing paths</h3>
                    <p class="card-meta">Focus on states where geology, certified-lab routing, or rules change the right next step.</p>
                </a>
                <a class="card-link" href="/well-water/how-to-read-a-well-water-lab-report">
                    <span class="card-kicker">Interpretation intent</span>
                    <h3>Read the report before you compare products</h3>
                    <p class="card-meta">Sharpen test scope, sample quality, and household risk before equipment research takes over.</p>
                </a>
                <a class="card-link" href="/well-water/metallic-taste">
                    <span class="card-kicker">Result support</span>
                    <h3>Interpret arsenic, nitrate, corrosion, and suspicious clues</h3>
                    <p class="card-meta">Keep interpretation pages alive where they reinforce the testing path instead of broadening into a generic encyclopedia.</p>
                </a>
            </div>
        </section>

        @if(leadContext != null)
            <section class="panel panel-accent" id="lead-capture">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">Early follow-up</p>
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

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Start here</p>
                    <h2>Choose the signal you already have</h2>
                </div>
                <p class="section-copy">You can still enter through lab results, symptoms, or a recent change. The difference is that the public guides now keep testing and verification ahead of treatment shopping.</p>
            </div>
            <div class="grid">
                <a class="card-link card-link-accent" href="/tool/result-first">
                    <span class="card-kicker">Result-first</span>
                    <h3>Lab Results</h3>
                    <p class="card-meta">Enter one analyte from a recent report and move straight into verification logic, scope, and next actions.</p>
                </a>
                <a class="card-link" href="/tool/symptom-first">
                    <span class="card-kicker">Symptom-first</span>
                    <h3>Symptoms</h3>
                    <p class="card-meta">Start from smell, stain, taste, cloudiness, or another visible pattern when you do not have clean lab data yet.</p>
                </a>
                <a class="card-link" href="/tool/trigger-first">
                    <span class="card-kicker">Trigger-first</span>
                    <h3>Recent Changes</h3>
                    <p class="card-meta">Use flooding, repairs, treatment changes, or wildfire context to decide whether to retest, inspect, or escalate.</p>
                </a>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Decision workflow</p>
                    <h2>How the engine moves you toward a testing decision instead of a premature purchase</h2>
                </div>
                <p class="section-copy">The goal is to shorten the path from a confusing clue to the next verified action, especially when a lab result, state rule, or home-sale timeline changes what matters first.</p>
            </div>
            <div class="grid grid-compact">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Interpret</span>
                    <h3>Read the clue in context</h3>
                    <p class="card-meta">A single analyte, smell, or event should not behave like a full household verdict until timing, sample point, and support signals are clear.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Test</span>
                    <h3>Choose the next verified test</h3>
                    <p class="card-meta">The engine should route toward certified testing, baseline confirmation, or state guidance before equipment research gets ahead of the evidence.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Compare</span>
                    <h3>Open only the right category</h3>
                    <p class="card-meta">Compare pages are useful only after the problem class is stable. Health paths stay verification-first. Nuisance paths can open equipment classes earlier.</p>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Trust</span>
                    <h3>Keep the paper trail visible</h3>
                    <p class="card-meta">Methodology, reviewers, disclosure, and data handling should stay close to the decision surface so a user can see why the routing works.</p>
                </article>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">High-signal starting points</p>
                    <h2>Start from the pages that can become real testing or follow-up decisions fastest</h2>
                </div>
                <p class="section-copy">These pages reflect the narrower public core: home purchase, state-specific testing, report interpretation, and a few support pages that sharpen the next test order.</p>
            </div>
            <div class="grid">
                @for (int i = 0; i < priorityPages.size(); i++)
                    !{var page = priorityPages.get(i);}
                    <a class="card-link ${i >= 6 ? "mobile-card-hidden" : ""}" href="/well-water/${page.slug()}">
                        <span class="card-kicker">${page.family()}</span>
                        <h3>${page.h1()}</h3>
                        <p class="card-meta">${page.metaDescription()}</p>
                    </a>
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
                        <a class="card-link card-link-accent" href="/well-water/${page.slug()}">
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
                    <p class="brand-kicker">More ways to narrow the issue</p>
                    <h2>Move from state rules and test scope into deeper research</h2>
                </div>
                <p class="section-copy">Start with the clearest decision pages, then move into deeper interpretation or treatment research once you know the likely problem and scope.</p>
            </div>
            <div class="grid grid-compact">
                <a class="card-link" href="/well-water/private-well-home-sale-testing-by-state">
                    <span class="card-kicker">Home sale guide</span>
                    <h3>Home sale testing by state</h3>
                    <p class="card-meta">Lead with transaction timing, required panels, and where a state checklist is still too thin.</p>
                </a>
                <a class="card-link" href="/well-water/family/regional">
                    <span class="card-kicker">State guide hub</span>
                    <h3>State-specific guides</h3>
                    <p class="card-meta">${familyCounts.get("regional")} pages where geology, rules, and certified-lab paths change the answer.</p>
                </a>
                <a class="card-link" href="/well-water/family/authority">
                    <span class="card-kicker">Testing guide hub</span>
                    <h3>Testing-order articles</h3>
                    <p class="card-meta">${familyCounts.get("authority")} articles covering sampling, lab-reading, and checklist logic before product research.</p>
                </a>
                <a class="card-link" href="/well-water/family/triggers">
                    <span class="card-kicker">Retest guide hub</span>
                    <h3>Trigger and retest guides</h3>
                    <p class="card-meta">${familyCounts.get("triggers")} pages for home purchase, flood response, repairs, and retest timing.</p>
                </a>
                <a class="card-link" href="/well-water/arsenic">
                    <span class="card-kicker">Result guide</span>
                    <h3>Named result interpretation</h3>
                    <p class="card-meta">Use result guides to clarify what a contaminant or symptom means before you compare products.</p>
                </a>
                <a class="card-link" href="/well-water/family/compares">
                    <span class="card-kicker">Treatment compare</span>
                    <h3>Compare pages</h3>
                    <p class="card-meta">${familyCounts.get("compares")} treatment comparison pages help once you have a recent result and a clearer sense of household scope.</p>
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
    </main>
`)
