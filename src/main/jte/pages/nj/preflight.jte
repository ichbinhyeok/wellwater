@param com.example.wellwater.web.nj.NjPreflightForm form
@param java.util.List<com.example.wellwater.nj.NjMunicipalitySummary> municipalities
@param com.example.wellwater.nj.NjMunicipalitySummary preview
@param com.example.wellwater.nj.NjPartner partner
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = preview != null ? preview.name() + " Private-Well Sale Testing | Water Verdict" : "NJ Private-Well Transaction Preflight | Water Verdict", robots = seo.robotsDirective(), description = preview != null ? "Start the NJ PWTA transaction path and review official municipality-level private-well testing summaries." : "Check the New Jersey PWTA path, county-specific panel, and public aggregate well-testing context before a sale or lease.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack nj-preflight-page"
          data-nj-preflight-root="true"
          data-channel="${form.getChannel()}"
          data-source="${form.getSource()}"
          data-partner-slug="${form.getPartnerSlug() == null ? "" : form.getPartnerSlug()}"
          data-municipality-slug="${form.getMunicipalitySlug() == null ? "" : form.getMunicipalitySlug()}">
        <section class="nj-hero">
            <div class="nj-hero-copy">
                @if(partner != null)
                    <span class="eyebrow">Prepared with ${partner.name()}</span>
                @elseif(preview != null)
                    <span class="eyebrow">${preview.county()} County transaction route</span>
                @else
                    <span class="eyebrow">New Jersey sale + lease tool</span>
                @endif
                <h1>@if(preview != null) ${preview.name()} well-sale preflight @else Check the well before the closing clock starts. @endif</h1>
                <p class="hero-lead">See whether PWTA likely applies, what the county changes, and which certified path comes next. An address is optional and is never stored.</p>
                <div class="nj-hero-action"><a class="button-link" href="#start">Start the 60-second preflight</a><span>No signup required</span></div>
                <div class="nj-proof-row" aria-label="What this tool returns">
                    <span><strong>1</strong> coverage call</span>
                    <span><strong>43</strong> possible analytes</span>
                    <span><strong>0</strong> addresses retained</span>
                </div>
            </div>
            <aside class="nj-deadline-card">
                <p class="brand-kicker">Before title closing</p>
                <strong>Buyer and seller must receive and review the covered test results.</strong>
                <p>NJDEP estimates typical PWTA testing at about $1,200-$1,500. Start with the required certified route, not a mail-in screening kit.</p>
            </aside>
        </section>

        @if(preview != null)
            <section class="nj-local-preview" aria-labelledby="local-preview-title">
                <div>
                    <p class="brand-kicker">Official local aggregate</p>
                    <h2 id="local-preview-title">What has appeared in ${preview.name()}</h2>
                    <p>These are historical municipality-wide PWTA summaries. They do not describe any specific property or well.</p>
                </div>
                <div class="nj-signal-strip">
                    @for(var signal : preview.signals().stream().limit(4).toList())
                        <span><strong>${signal.label()}</strong>${signal.observedRange()}</span>
                    @endfor
                </div>
            </section>
        @endif

        <section class="nj-tool-shell" id="start">
            <div class="nj-tool-heading">
                <p class="brand-kicker">60-second preflight</p>
                <h2>Start with the transaction, not an article.</h2>
                <p>Address gives a 2-mile aggregate match. Municipality alone still produces the legal and county route.</p>
            </div>

            <form method="post" action="/nj-well-preflight/result" class="nj-preflight-form">
                <input type="hidden" name="channel" value="${form.getChannel()}">
                <input type="hidden" name="source" value="${form.getSource()}">
                <input type="hidden" name="partnerSlug" value="${form.getPartnerSlug() == null ? "" : form.getPartnerSlug()}">

                <fieldset class="nj-step">
                    <legend><span>01</span> What is happening?</legend>
                    <div class="nj-choice-grid">
                        <label><input type="radio" name="transactionType" value="sale" checked="${"sale".equals(form.getTransactionType())}"><span><strong>Buying or selling</strong><small>Closing is active or approaching</small></span></label>
                        <label><input type="radio" name="transactionType" value="lease" checked="${"lease".equals(form.getTransactionType())}"><span><strong>Leasing the property</strong><small>Landlord or tenant path</small></span></label>
                        <label><input type="radio" name="transactionType" value="planning" checked="${"planning".equals(form.getTransactionType())}"><span><strong>Planning ahead</strong><small>No active transaction yet</small></span></label>
                    </div>
                </fieldset>

                <fieldset class="nj-step">
                    <legend><span>02</span> How does the property get drinking water?</legend>
                    <select name="waterSource" required>
                        <option value="private_well" selected="${"private_well".equals(form.getWaterSource())}">Private well on the property</option>
                        <option value="shared_small_well" selected="${"shared_small_well".equals(form.getWaterSource())}">Small shared well</option>
                        <option value="irrigation_only" selected="${"irrigation_only".equals(form.getWaterSource())}">Well is not used for drinking</option>
                        <option value="unsure" selected="${"unsure".equals(form.getWaterSource())}">Not sure</option>
                    </select>
                </fieldset>

                <fieldset class="nj-step nj-location-step">
                    <legend><span>03</span> Locate the public aggregate data</legend>
                    <label class="nj-field">
                        <span>Street address <small>optional, processed in memory only</small></span>
                        <input type="text" name="address" maxlength="200" autocomplete="street-address" placeholder="24 Maple Ave, Chester, NJ 07930">
                    </label>
                    <div class="nj-or"><span>or use the fallback</span></div>
                    <label class="nj-field">
                        <span>New Jersey municipality</span>
                        <select name="municipalitySlug">
                            <option value="">Choose a municipality</option>
                            @for(var municipality : municipalities)
                                <option value="${municipality.slug()}" selected="${municipality.slug().equals(form.getMunicipalitySlug())}">${municipality.name()} - ${municipality.county()} County</option>
                            @endfor
                        </select>
                    </label>
                    <p class="nj-privacy-note"><strong>Privacy boundary:</strong> the address is sent to the U.S. Census geocoder, used to select one public aggregate grid, and discarded. It never enters analytics, logs, or the result URL.</p>
                </fieldset>

                <fieldset class="nj-step">
                    <legend><span>04</span> Anything the laboratory should know?</legend>
                    <div class="nj-check-grid">
                        <label><input type="checkbox" name="extraContexts" value="infant_or_pregnant"><span>Infant or pregnancy</span></label>
                        <label><input type="checkbox" name="extraContexts" value="recent_flood_or_repair"><span>Recent flood or well repair</span></label>
                        <label><input type="checkbox" name="extraContexts" value="older_plumbing"><span>Older plumbing</span></label>
                        <label><input type="checkbox" name="extraContexts" value="nearby_contamination"><span>Known nearby contamination</span></label>
                        <label><input type="checkbox" name="extraContexts" value="treatment_installed"><span>Treatment already installed</span></label>
                    </div>
                </fieldset>

                <div class="nj-submit-row">
                    <button type="submit">Build my NJ transaction path</button>
                    <p>No account. No contact details. No property-level contamination claim.</p>
                </div>
            </form>
        </section>

        <section class="nj-source-band">
            <strong>Built from official NJDEP data</strong>
            <p>PWTA rules and aggregate results through December 2024. This secondary product is not verified, authorized, or endorsed by NJDEP.</p>
            <a href="/nj-well-preflight/out/pwta?channel=${form.getChannel()}&source=${form.getSource()}" rel="noopener noreferrer">Open the official PWTA page</a>
        </section>
    </main>
`)
