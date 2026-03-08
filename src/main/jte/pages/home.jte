@param java.util.Map<String, Long> familyCounts
@param Integer totalPageCount
@param java.util.List<com.example.wellwater.pseo.PseoPage> priorityPages
@param java.util.List<com.example.wellwater.pseo.PseoPage> featuredRegionalPages
@param java.util.List<com.example.wellwater.web.page.TrustPage> trustPages
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "Water Verdict | Results, Symptoms, and Next Steps", robots = "index,follow", description = "Use test results, water symptoms, or recent changes to decide what to verify next for a private well.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Private well decision engine</span>
                    <h1>Figure out what changed in your well water and what to test next.</h1>
                    <p class="hero-lead">Start with a lab result, a smell or stain, or a recent event like flooding or repairs. The engine turns that clue into a test plan, interpretation path, compare gate, and trust trail.</p>
                    <div class="hero-actions">
                        <a class="button-link" href="/tool/result-first">Start From Lab Results</a>
                        <a class="button-link-secondary" href="/tool/trigger-first">Start From Recent Changes</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>What this product gives you</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">The goal is not generic water content. It is to help a well owner decide what to verify, what to test, and when not to buy yet.</p>
                    <div class="hero-metrics">
                        <div class="metric">
                            <strong>Test</strong>
                            <span>recommendation</span>
                        </div>
                        <div class="metric">
                            <strong>Result</strong>
                            <span>interpretation</span>
                        </div>
                        <div class="metric">
                            <strong>Compare</strong>
                            <span>gate before shopping</span>
                        </div>
                        <div class="metric">
                            <strong>Trust</strong>
                            <span>review, privacy, disclosure</span>
                        </div>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Start here</p>
                    <h2>Choose the signal you already have</h2>
                </div>
                <p class="section-copy">The intake paths match how private well owners actually arrive: a report, a symptom, or a sudden change.</p>
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
                    <h2>How the engine moves you toward a safer next step</h2>
                </div>
                <p class="section-copy">The product is most useful when it helps you avoid shopping too early and keeps the reasoning visible.</p>
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
                    <h2>Start from the questions most likely to become a real decision</h2>
                </div>
                <p class="section-copy">These pages are the strongest organic entry points because they usually lead into testing, interpretation, or a constrained compare path.</p>
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
                <a class="chip" href="/well-water/family/contaminants">Contaminants</a>
                <a class="chip" href="/well-water/family/symptoms">Symptoms</a>
                <a class="chip" href="/well-water/family/triggers">Triggers</a>
                <a class="chip" href="/well-water/family/compares">Comparisons</a>
            </div>
        </section>

        @if (!featuredRegionalPages.isEmpty())
            <section class="panel panel-accent">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">State-specific paths</p>
                        <h2>Featured regional guides with official lab and guidance handoff</h2>
                    </div>
                    <p class="section-copy">These are the strongest local-entry pages right now because they change the next step based on state guidance, certified lab routing, and event context.</p>
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
                    <p class="brand-kicker">Research surface</p>
                    <h2>Browse by question type when you need a wider map</h2>
                </div>
                <p class="section-copy">Use the hub layer when you are still scoping the problem or want to move sideways into state nuance, compare pages, or authority content.</p>
            </div>
            <div class="grid grid-compact">
                <a class="card-link" href="/well-water/family/contaminants">
                    <span class="card-kicker">Family</span>
                    <h3>Contaminants</h3>
                    <p class="card-meta">${familyCounts.get("contaminants")} pages covering named analytes and contamination concerns.</p>
                </a>
                <a class="card-link" href="/well-water/family/symptoms">
                    <span class="card-kicker">Family</span>
                    <h3>Water Symptoms</h3>
                    <p class="card-meta">${familyCounts.get("symptoms")} pages for signs like stains, odor, taste, and visible changes.</p>
                </a>
                <a class="card-link" href="/well-water/family/triggers">
                    <span class="card-kicker">Family</span>
                    <h3>Recent Triggers</h3>
                    <p class="card-meta">${familyCounts.get("triggers")} pages for event-driven changes such as flooding or post-install retesting.</p>
                </a>
                <a class="card-link" href="/well-water/family/compares">
                    <span class="card-kicker">Family</span>
                    <h3>Treatment Comparisons</h3>
                    <p class="card-meta">${familyCounts.get("compares")} pages comparing categories without forcing a premature product choice.</p>
                </a>
                <a class="card-link" href="/well-water/family/regional">
                    <span class="card-kicker">Family</span>
                    <h3>Regional Guides</h3>
                    <p class="card-meta">${familyCounts.get("regional")} pages where state geology, rules, and testing pathways change the answer.</p>
                </a>
                <a class="card-link" href="/well-water/family/authority">
                    <span class="card-kicker">Family</span>
                    <h3>Authority Articles</h3>
                    <p class="card-meta">${familyCounts.get("authority")} pages that strengthen trust, method, and internal support for money pages.</p>
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
