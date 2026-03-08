@param com.example.wellwater.web.page.TrustPage page
@param java.util.List<com.example.wellwater.web.page.TrustPage> trustPages
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus
@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = page.title(), robots = "index,follow", description = page.metaDescription(), canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">${page.eyebrow()}</span>
                    <h1>${page.h1()}</h1>
                    <p class="hero-lead">${page.lead()}</p>
                    <div class="chip-row" style="margin-top: 18px;">
                        <span class="chip">Public trust asset</span>
                        <span class="chip">Indexable</span>
                        <span class="chip">Last updated ${page.updatedAt()}</span>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>Why this page matters</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">${page.operatorNote()}</p>
                </aside>
            </div>
        </section>

        <section class="detail-grid">
            <div class="detail-stack">
                @for (var section : page.sections())
                    <section class="panel">
                        <div class="section-header">
                            <div>
                                <p class="brand-kicker">${page.eyebrow()}</p>
                                <h2>${section.title()}</h2>
                            </div>
                        </div>
                        <div class="prose">
                            <p>${section.body()}</p>
                        </div>
                        <div class="stack" style="gap: 12px; margin-top: 18px;">
                            @for (var bullet : section.bullets())
                                <article class="card-link">
                                    <span class="card-kicker">Working rule</span>
                                    <p class="card-meta">${bullet}</p>
                                </article>
                            @endfor
                        </div>
                    </section>
                @endfor
            </div>

            <aside class="detail-stack">
                @if(leadContext != null)
                    <section class="panel" id="lead-capture">
                        <div class="section-header">
                            <div>
                                <p class="brand-kicker">Early follow-up</p>
                                <h2>${leadContext.heading()}</h2>
                            </div>
                        </div>
                        <p class="card-meta">${leadContext.summary()}</p>
                        @if("success".equals(leadStatus))
                            <p class="card-meta" style="margin-top: 14px; color: #0f766e; font-weight: 700;">Follow-up request captured. Check <code>/admin</code> for the new row.</p>
                        @elseif("invalid".equals(leadStatus))
                            <p class="card-meta" style="margin-top: 14px; color: #c66a2b; font-weight: 700;">Enter a valid email address to submit this follow-up request.</p>
                        @endif
                        <form method="post" action="/lead/submit" class="stack" style="gap: 12px; margin-top: 16px;">
                            <input type="hidden" name="redirectPath" value="${leadContext.redirectPath()}">
                            <input type="hidden" name="sourceType" value="${leadContext.sourceType()}">
                            <input type="hidden" name="sourceFamily" value="${leadContext.sourceFamily()}">
                            <input type="hidden" name="sourceSlug" value="${leadContext.sourceSlug()}">
                            <input type="hidden" name="sourceLabel" value="${leadContext.sourceLabel()}">
                            <input type="text" name="website" tabindex="-1" autocomplete="off" style="position:absolute;left:-9999px;width:1px;height:1px;opacity:0;" aria-hidden="true">
                            <input type="email" name="email" placeholder="you@example.com" required class="form-field">
                            <input type="text" name="state" placeholder="State (optional)" maxlength="12" class="form-field">
                            <textarea name="note" rows="4" placeholder="What are you trying to verify before you enter results?" class="form-textarea"></textarea>
                            <button type="submit" class="button-link" style="width: 100%; justify-content: center;">${leadContext.submitLabel()}</button>
                        </form>
                    </section>
                @endif

                <section class="panel panel-accent">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Use this with</p>
                            <h2>Decision engine and authority pages</h2>
                        </div>
                    </div>
                    <p class="card-meta">These trust pages exist to support the public acquisition layer and the tool, not to replace either one.</p>
                    <div class="cta-row" style="margin-top: 16px;">
                        <a class="button-link" href="/tool/result-first">Open Tool</a>
                    </div>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Trust hub</p>
                            <h2>Related trust pages</h2>
                        </div>
                    </div>
                    <div class="stack" style="gap: 12px;">
                        @for (var related : trustPages)
                            @if(!related.slug().equals(page.slug()))
                                <a class="card-link" href="/trust/${related.slug()}">
                                    <span class="card-kicker">${related.eyebrow()}</span>
                                    <h3>${related.h1()}</h3>
                                    <p class="card-meta">${related.metaDescription()}</p>
                                </a>
                            @endif
                        @endfor
                    </div>
                </section>
            </aside>
        </section>
    </main>
`)
