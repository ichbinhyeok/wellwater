@param java.util.Map<String, Long> familyCounts
@param Integer totalPageCount
@param java.util.List<com.example.wellwater.pseo.PseoPage> priorityPages
@param java.util.List<com.example.wellwater.web.page.TrustPage> trustPages
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "Private Well Water Guide | Results, Symptoms, and Next Steps", robots = "index,follow", description = "Use test results, water symptoms, or recent changes to decide what to verify next for a private well.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Private well help</span>
                    <h1>Figure out what changed in your well water and what to test next.</h1>
                    <p class="hero-lead">Start with a lab result, a smell or stain, or a recent event like flooding or repairs. These guides help you act faster without jumping straight to treatment.</p>
                    <div class="hero-actions">
                        <a class="button-link" href="/tool/result-first">Start From Lab Results</a>
                        <a class="button-link-secondary" href="/well-water/family/symptoms">Start From Symptoms</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>Three ways in</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">Most visits start with one of three real questions: what a lab result means, why the water changed, or what to do after an event.</p>
                    <div class="hero-metrics">
                        <div class="metric">
                            <strong>Lab</strong>
                            <span>result questions</span>
                        </div>
                        <div class="metric">
                            <strong>Smell</strong>
                            <span>stain and taste clues</span>
                        </div>
                        <div class="metric">
                            <strong>Event</strong>
                            <span>flood, repair, or purchase</span>
                        </div>
                        <div class="metric">
                            <strong>${totalPageCount}</strong>
                            <span>guide pages</span>
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

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Public research</p>
                    <h2>Browse the main kinds of well water questions</h2>
                </div>
                <p class="section-copy">Use these hubs when you already know whether you are dealing with a test result, a symptom, a recent event, a state-specific issue, or a treatment comparison.</p>
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
                <a class="card-link" href="/well-water/family/compares">
                    <span class="card-kicker">Family</span>
                    <h3>Treatment Comparisons</h3>
                    <p class="card-meta">${familyCounts.get("compares")} pages comparing categories without forcing a premature product choice.</p>
                </a>
                <a class="card-link" href="/well-water/family/triggers">
                    <span class="card-kicker">Family</span>
                    <h3>Recent Triggers</h3>
                    <p class="card-meta">${familyCounts.get("triggers")} pages for event-driven changes such as flooding or post-install retesting.</p>
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

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Priority pages</p>
                    <h2>Start with the most common well water searches</h2>
                </div>
                <p class="section-copy">These pages match the questions people are most likely to search first and should lead naturally into testing, comparison, or the matching tool flow.</p>
            </div>
            <div class="grid">
                @for (var page : priorityPages)
                    <a class="card-link" href="/well-water/${page.slug()}">
                        <span class="card-kicker">${page.family()}</span>
                        <h3>${page.h1()}</h3>
                        <p class="card-meta">${page.metaDescription()}</p>
                    </a>
                @endfor
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
                @for (var page : trustPages)
                    <a class="card-link" href="/trust/${page.slug()}">
                        <span class="card-kicker">${page.eyebrow()}</span>
                        <h3>${page.h1()}</h3>
                        <p class="card-meta">${page.metaDescription()}</p>
                    </a>
                @endfor
            </div>
        </section>
    </main>
`)
