@param com.example.wellwater.pseo.PseoPage page

@template.layout.main(title = page.title(), robots = "index,follow", description = page.metaDescription(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">${page.family()}</span>
                    <h1>${page.h1()}</h1>
                    <p class="hero-lead">${page.intro()}</p>
                    <div class="hero-actions">
                        @if (page.family().equals("symptoms"))
                            <a class="button-link" href="/tool/symptom-first?symptom=${page.slug()}&slug=${page.slug()}">Open Symptom-First Tool</a>
                        @elseif (page.family().equals("triggers"))
                            <a class="button-link" href="/tool/trigger-first?trigger=${page.slug()}&slug=${page.slug()}">Open Trigger-First Tool</a>
                        @elseif (page.family().equals("contaminants"))
                            <a class="button-link" href="/tool/result-first?analyte=${page.slug()}&slug=${page.slug()}">Open Result-First Tool</a>
                        @else
                            <a class="button-link" href="/tool/result-first?slug=${page.slug()}">Open Result-First Tool</a>
                        @endif
                        <a class="button-link-secondary" href="${page.primaryCtaUrl()}">${page.primaryCtaLabel()}</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>How to use this page</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">Public page first for context. Personalized noindex result second for actual decision routing.</p>
                    <div class="chip-row">
                        <span class="chip">${page.family()}</span>
                        <span class="chip">Noindex result handoff</span>
                        <span class="chip">Claim-based next step</span>
                    </div>
                </aside>
            </div>
        </section>

        <section class="detail-grid">
            <div class="detail-stack">
                <section class="panel panel-accent">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Immediate orientation</p>
                            <h2>What to do now</h2>
                        </div>
                    </div>
                    <div class="prose">
                        <p>${page.actionNow()}</p>
                    </div>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Verification path</p>
                            <h2>What to test or compare next</h2>
                        </div>
                    </div>
                    <div class="prose">
                        <p>${page.whatToTest()}</p>
                    </div>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Decision engine handoff</p>
                            <h2>Run the matching tool flow</h2>
                        </div>
                        <p class="section-copy">Personalized results are rendered as noindex pages after the tool collects context.</p>
                    </div>
                    <div class="cta-row">
                        @if (page.family().equals("symptoms"))
                            <a class="button-link" href="/tool/symptom-first?symptom=${page.slug()}&slug=${page.slug()}">Start Symptom-First Tool</a>
                        @elseif (page.family().equals("triggers"))
                            <a class="button-link" href="/tool/trigger-first?trigger=${page.slug()}&slug=${page.slug()}">Start Trigger-First Tool</a>
                        @elseif (page.family().equals("contaminants"))
                            <a class="button-link" href="/tool/result-first?analyte=${page.slug()}&slug=${page.slug()}">Start Result-First Tool</a>
                        @else
                            <a class="button-link" href="/tool/result-first?slug=${page.slug()}">Start Result-First Tool</a>
                        @endif
                    </div>
                </section>
            </div>

            <aside class="detail-stack">
                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Next steps</p>
                            <h2>Primary action path</h2>
                        </div>
                    </div>
                    <a class="card-link card-link-accent" href="${page.primaryCtaUrl()}">
                        <span class="card-kicker">Primary</span>
                        <h3>${page.primaryCtaLabel()}</h3>
                        <p class="card-meta">Use this when you need the direct next action outside the tool.</p>
                    </a>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Research</p>
                            <h2>Options and pricing</h2>
                        </div>
                    </div>
                    <a class="card-link" href="${page.moneyCtaUrl()}">
                        <span class="card-kicker">Commercial follow-up</span>
                        <h3>${page.moneyCtaLabel()}</h3>
                        <p class="card-meta">${page.disclosure()}</p>
                    </a>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Source trail</p>
                            <h2>Traceability</h2>
                        </div>
                    </div>
                    <div class="meta-list">
                        <div class="meta-item">
                            <span class="meta-label">Source URL</span>
                            <div class="meta-value"><a href="${page.sourceUrl()}">${page.sourceUrl()}</a></div>
                        </div>
                        <div class="meta-item">
                            <span class="meta-label">Search query</span>
                            <div class="meta-value">${page.searchQuery()}</div>
                        </div>
                        <div class="meta-item">
                            <span class="meta-label">Fetched</span>
                            <div class="meta-value">searched at ${page.searchPerformedAt()} / fetched at ${page.fetchedAt()}</div>
                        </div>
                    </div>
                </section>
            </aside>
        </section>
    </main>
`)
