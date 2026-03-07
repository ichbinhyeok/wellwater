@param String family
@param java.util.List<com.example.wellwater.pseo.PseoPage> pages

@template.layout.main(title = "Well Water " + family, robots = "index,follow", description = "Browse well water " + family + " pages with practical next steps.", content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Guide family</span>
                    <h1>${family}</h1>
                    <p class="hero-lead">Indexable research pages for ${family}. Each page is designed to move from orientation into the right tool entry mode or next verification step.</p>
                </div>
                <aside class="hero-note">
                    <strong>${pages.size()} pages in this family</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">Use family pages to scan patterns quickly before opening a personalized result flow.</p>
                    <div class="chip-row">
                        <span class="chip">Public surface</span>
                        <span class="chip">Searchable</span>
                        <span class="chip">Tool handoff</span>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Browse pages</p>
                    <h2>Practical guides in this family</h2>
                </div>
                <p class="section-copy">Each page should answer “what is this” and “what should I do next” without pretending to be a personalized verdict.</p>
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
    </main>
`)
