@param String path

@template.layout.main(title = "Not Found", robots = "noindex,nofollow", description = "Requested page was not found.", canonicalUrl = "", breadcrumbs = java.util.List.of(), structuredDataBlocks = java.util.List.of(), openGraphType = "website", socialImageUrl = "/og-card.svg", googleSiteVerification = "", googleAnalyticsId = "", content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Missing page</span>
                    <h1>Page not found.</h1>
                    <p class="hero-lead">No page data was found for <strong>${path}</strong>. Use the public guide hub or jump directly into the decision tool.</p>
                    <div class="hero-actions">
                        <a class="button-link" href="/">Back to Home</a>
                        <a class="button-link-secondary" href="/tool/result-first">Open Result-First Tool</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>Suggested recovery</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">Return to a family page or re-enter through one of the three tool modes.</p>
                </aside>
            </div>
        </section>
    </main>
`)
