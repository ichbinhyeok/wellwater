@param com.example.wellwater.nj.NjPreflightResult result
@param String error
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "NJ Private-Well Preflight Result | Water Verdict", robots = "noindex,nofollow", description = "A private NJ PWTA transaction routing result.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = java.util.List.of(), structuredDataBlocks = java.util.List.of(), openGraphType = "website", socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = "", content = @`
    <main class="stack nj-preflight-page nj-result-page">
        @if(error != null && !error.isBlank())
            <section class="nj-result-hero nj-result-error">
                <span class="eyebrow">Input needs review</span>
                <h1>We could not build that preflight.</h1>
                <p class="hero-lead">${error}</p>
                <a class="button-link" href="/nj-well-preflight#start">Return to the NJ preflight</a>
            </section>
        @elseif(result != null)
            <section class="nj-result-hero">
                <div>
                    <span class="eyebrow">${result.locationResolution()} - ${result.locationLabel()}</span>
                    <h1>${result.coverage().headline()}</h1>
                    <p class="hero-lead">${result.coverage().explanation()}</p>
                    <div class="hero-actions">
                        <a class="button-link" href="/nj-well-preflight/out/certified-labs?channel=${result.channel()}&source=${result.source()}" rel="noopener noreferrer">Find NJDEP-certified labs</a>
                        <a class="button-link-secondary" href="/nj-well-preflight/out/pwta?channel=${result.channel()}&source=${result.source()}" rel="noopener noreferrer">Verify on NJDEP</a>
                    </div>
                </div>
                <aside class="nj-result-status">
                    <span>PWTA route</span>
                    <strong>${result.coverage().status().equals("likely_covered") ? "Likely applies" : (result.coverage().status().equals("verify") ? "Confirm coverage" : "Likely not triggered")}</strong>
                    <small>Decision support, not legal clearance</small>
                </aside>
            </section>

            <section class="nj-location-boundary">
                <strong>Your address was not retained.</strong>
                <p>${result.locationNotice()}</p>
            </section>

            <section class="panel nj-result-panel">
                <div class="section-header">
                    <div><p class="brand-kicker">Required route</p><h2>Ask one certified lab for this PWTA scope</h2></div>
                    <p class="section-copy">The exact order includes up to 43 analytes. County-specific additions are marked below.</p>
                </div>
                <div class="nj-requirement-grid">
                    @for(var requirement : result.requiredPanel())
                        <article class="${requirement.countySpecific() ? "county-specific" : ""}">
                            <strong>${requirement.name()}</strong>
                            <p>${requirement.scopeNote()}</p>
                            @if(requirement.countySpecific()) <span>${result.county()} County</span> @endif
                        </article>
                    @endfor
                </div>
            </section>

            <section class="panel nj-result-panel nj-data-panel">
                <div class="section-header">
                    <div><p class="brand-kicker">Public history, not a diagnosis</p><h2>What the surrounding aggregate makes worth discussing</h2></div>
                    <p class="section-copy">${result.localSignalScope()}</p>
                </div>
                @if(result.localSignals().isEmpty())
                    <p class="nj-empty-state">The selected public summary did not produce a reportable elevated range. That is not evidence that this property is clear; only certified property testing can answer that.</p>
                @else
                    <div class="nj-signal-grid">
                        @for(var signal : result.localSignals())
                            <article>
                                <span>${signal.healthRelated() ? "Health-related standard" : "Water-quality standard"}</span>
                                <strong>${signal.label()}</strong>
                                <p>${signal.observedRange()}</p>
                            </article>
                        @endfor
                    </div>
                @endif
            </section>

            <section class="panel nj-result-panel">
                <div class="section-header"><div><p class="brand-kicker">Lab conversation</p><h2>Bring these questions to the sampler</h2></div></div>
                <ul class="nj-action-list">
                    @for(var discussion : result.extraDiscussions())
                        <li>${discussion}</li>
                    @endfor
                </ul>
            </section>

            <section class="panel nj-result-panel nj-next-panel">
                <div class="section-header"><div><p class="brand-kicker">Order of operations</p><h2>Do these three things next</h2></div></div>
                <ol class="nj-next-steps">
                    @for(var step : result.nextSteps())
                        <li><span>${step}</span></li>
                    @endfor
                </ol>
                <p class="nj-hold-line"><strong>Do not use a home screening kit as PWTA compliance proof.</strong> ${result.basePlan().avoidForNow()}</p>
            </section>

            @if(result.hasPartner())
                <section class="nj-partner-result">
                    <div><p class="brand-kicker">Your referring ${result.partner().type()}</p><h2>Continue with ${result.partner().name()}</h2><p>${result.partner().disclosure()}</p></div>
                    <a class="button-link" href="/nj-well-preflight/out/partner/${result.partner().slug()}?channel=${result.channel()}&source=${result.source()}" rel="sponsored noopener noreferrer">Open scheduling</a>
                </section>
            @endif

            <section class="nj-result-footer">
                <p>This product uses NJDEP GIS aggregate data through December 2024. It has not been verified by NJDEP and is not state-authorized or endorsed. It does not identify or infer the result of an individual well.</p>
                <div>
                    <a href="/nj-well-preflight/out/testing?channel=${result.channel()}&source=${result.source()}" rel="noopener noreferrer">NJ well-testing guidance</a>
                    <a href="/nj-well-preflight#start">Run another preflight</a>
                </div>
            </section>
        @endif
    </main>
`)
