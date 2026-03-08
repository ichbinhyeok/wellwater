@param java.util.List<com.example.wellwater.web.page.TrustPage> trustPages
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "Trust And Method | Private Well Water Guide", robots = "index,follow", description = "Read the methodology, review policy, sources policy, and safety limits behind this private-well decision surface.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Trust pages</span>
                    <h1>How this site reviews well water advice, sources, and safety limits.</h1>
                    <p class="hero-lead">These pages explain how evidence is weighed, how sources are selected, what gets reviewed, and where testing should stay ahead of product advice.</p>
                    <div class="hero-actions">
                        <a class="button-link" href="/tool/result-first">Open Tool</a>
                        <a class="button-link-secondary" href="/well-water/family/authority">Read Authority Articles</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>Why this exists</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">A private well site should not ask for trust through tone alone. It should show how decisions, limits, sources, and corrections work in public.</p>
                    <div class="chip-row">
                        <span class="chip">Methodology</span>
                        <span class="chip">Review policy</span>
                        <span class="chip">Safety limits</span>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Public pages</p>
                    <h2>Trust and method documents</h2>
                </div>
                <p class="section-copy">These are indexable support assets, not buried footer notes.</p>
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
