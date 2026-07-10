@param com.example.wellwater.welltest.WellTestPlanResult result
@param com.example.wellwater.web.welltest.WellTestPlanForm planForm
@param String error
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "Your Private-Well Test Plan | Water Verdict", robots = "noindex,nofollow", description = "A focused private-well testing panel and certified lab path.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = java.util.List.of(), structuredDataBlocks = java.util.List.of(), openGraphType = "website", socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = "", content = @`
    <main class="stack plan-result-page">
        <section class="hero plan-result-hero">
            @if(error != null && !error.isBlank())
                <span class="eyebrow">Input needs review</span>
                <h1>We could not build that plan.</h1>
                <p class="hero-lead">${error}</p>
                <div class="hero-actions"><a class="button-link" href="/#test-plan">Return to the test finder</a></div>
            @elseif(result != null)
                <span class="eyebrow">${result.urgency()} test path</span>
                <h1>${result.verdict()}</h1>
                <p class="hero-lead">This result is intentionally short: choose the panel, use the certified path, and only then consider a matching physical kit.</p>
            @endif
        </section>

        @if(result != null)
            <section class="panel">
                <div class="section-header">
                    <div><p class="brand-kicker">Your panel</p><h2>Test these first</h2></div>
                    <p class="section-copy">The list combines a private-well baseline with only the clues and nearby risks you selected.</p>
                </div>
                <div class="plan-panel-grid">
                    @for(var item : result.recommendedPanel())
                        <article class="plan-panel-item"><strong>${item.name()}</strong><p>${item.reason()}</p></article>
                    @endfor
                </div>
            </section>

            <section class="panel panel-accent">
                <div class="section-header"><div><p class="brand-kicker">Why this route</p><h2>What changed the recommendation</h2></div></div>
                <ul class="plan-reason-list">
                    @for(var reason : result.reasons())
                        <li>${reason}</li>
                    @endfor
                </ul>
            </section>

            <section class="panel">
                <div class="section-header"><div><p class="brand-kicker">Official handoff</p><h2>Use a certified path</h2></div></div>
                <div class="plan-links">
                    <a class="card-link" href="${result.officialGuidance().url()}" rel="noopener noreferrer" target="_blank"><span class="card-kicker">Guidance</span><h3>${result.officialGuidance().label()}</h3></a>
                    <a class="card-link" href="${result.certifiedLabPath().url()}" rel="noopener noreferrer" target="_blank"><span class="card-kicker">Certified lab</span><h3>${result.certifiedLabPath().label()}</h3></a>
                </div>
            </section>

            @if(result.hasPartnerOffer())
                <section class="panel plan-partner">
                    <div><p class="brand-kicker">Optional physical kit</p><h2>${result.partnerOffer().productName()}</h2><p>${result.partnerOffer().fitReason()}</p></div>
                    <a class="button-link" href="${result.partnerOffer().url()}" rel="sponsored noopener noreferrer">View matching test kit</a>
                    <p class="plan-disclosure">${result.partnerOffer().disclosure()}</p>
                </section>
            @endif

            <section class="panel plan-fine-print">
                <p>${result.disclosure()}</p>
                <a class="button-link-secondary" href="/#test-plan">Build another plan</a>
            </section>
        @endif
    </main>
    <style>
        .plan-result-hero { padding-bottom: 34px; }
        .plan-panel-grid, .plan-links { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
        .plan-panel-item { border: 1px solid var(--line); border-radius: 18px; padding: 18px; background: var(--paper); }
        .plan-panel-item strong { font-family: Georgia, serif; font-size: 1.06rem; }
        .plan-panel-item p { margin: 8px 0 0; color: var(--muted); line-height: 1.55; }
        .plan-reason-list { display: grid; gap: 10px; margin: 0; padding-left: 20px; }
        .plan-reason-list li { line-height: 1.55; }
        .plan-partner { display: grid; gap: 16px; background: #13271f; color: #fff; }
        .plan-partner p { color: rgba(255,255,255,.74); }
        .plan-partner .brand-kicker { color: #9ed1b7; }
        .plan-partner .button-link { width: fit-content; background: #fff; color: #13271f; }
        .plan-disclosure { font-size: .78rem; }
        .plan-fine-print { display: flex; justify-content: space-between; align-items: center; gap: 16px; }
        .plan-fine-print p { margin: 0; color: var(--muted); max-width: 720px; }
        @media (max-width: 700px) { .plan-panel-grid, .plan-links { grid-template-columns: 1fr; } .plan-fine-print { align-items: flex-start; flex-direction: column; } }
    </style>
`)
