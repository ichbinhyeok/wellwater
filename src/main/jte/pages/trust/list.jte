@param java.util.List<com.example.wellwater.web.page.TrustPage> trustPages
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "Trust And Method | Water Verdict", robots = "index,follow", description = "Read the methodology, review policy, sources policy, and safety limits behind this private-well decision surface.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
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
                    <textarea name="note" rows="4" placeholder="What are you trying to figure out before you use the tool?" class="form-textarea"></textarea>
                    <button type="submit" class="button-link" style="justify-content: center;">${leadContext.submitLabel()}</button>
                </form>
            </section>
        @endif

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
