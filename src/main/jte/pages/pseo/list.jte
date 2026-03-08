@param String family
@param java.util.List<com.example.wellwater.pseo.PseoPage> pages
@param com.example.wellwater.pseo.PseoFamilyView familyView
@param com.example.wellwater.web.page.SeoMetadata seo
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus

@template.layout.main(title = familyView.heroTitle(), robots = "index,follow", description = familyView.heroLead(), canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">Well water topic</span>
                    <h1>${familyView.heroTitle()}</h1>
                    <p class="hero-lead">${familyView.heroLead()}</p>
                    <div class="hero-actions">
                        <a class="button-link" href="${familyView.primaryToolHref()}">${familyView.primaryToolLabel()}</a>
                        <a class="button-link-secondary" href="/">Back to guide hub</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>${pages.size()} pages in this family</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">${familyView.operatorNote()}</p>
                    <div class="chip-row">
                        <span class="chip">Common searches</span>
                        <span class="chip">Testing path</span>
                        <span class="chip">Next-step guidance</span>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Priority reads</p>
                    <h2>Start with these pages in this family</h2>
                </div>
                <p class="section-copy">This keeps the hub from behaving like a flat index and moves visitors toward the pages most likely to answer their first question.</p>
            </div>
            <div class="grid">
                @for (var starter : familyView.starterPages())
                    <a class="card-link card-link-accent" href="/well-water/${starter.slug()}">
                        <span class="card-kicker">${starter.family()}</span>
                        <h3>${starter.h1()}</h3>
                        <p class="card-meta">${starter.metaDescription()}</p>
                    </a>
                @endfor
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Family guidance</p>
                    <h2>What this hub should help you avoid</h2>
                </div>
                <p class="section-copy">Good hub pages do more than list URLs. They explain the common mistake pattern and what to check before you open the tool.</p>
            </div>
            <div class="grid">
                <article class="card-link card-link-accent">
                    <span class="card-kicker">Common mistake</span>
                    <h3>What people get wrong first in this family</h3>
                    <div class="stack" style="gap: 10px;">
                        @for (var mistake : familyView.commonMistakes())
                            <p class="card-meta">${mistake}</p>
                        @endfor
                    </div>
                </article>
                <article class="card-link">
                    <span class="card-kicker">Before the tool</span>
                    <h3>Three checks before opening the decision flow</h3>
                    <div class="stack" style="gap: 10px;">
                        @for (var check : familyView.beforeToolChecks())
                            <p class="card-meta">${check}</p>
                        @endfor
                    </div>
                </article>
            </div>
        </section>

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Browse pages</p>
                    <h2>Practical guides in this family</h2>
                </div>
                <p class="section-copy">Each page should answer what this is, what to verify next, and when not to buy yet.</p>
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

        @if(leadContext != null)
            <section class="panel" id="lead-capture">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">Lead capture</p>
                        <h2>${leadContext.heading()}</h2>
                    </div>
                </div>
                <p class="section-copy">${leadContext.summary()}</p>
                @if("success".equals(leadStatus))
                    <p class="section-copy" style="color: #0f766e; font-weight: 700;">Lead captured. Check <code>/admin</code> for the new row.</p>
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
                    <input type="email" name="email" placeholder="you@example.com" required class="form-field">
                    <div class="form-grid two">
                        <input type="text" name="name" placeholder="Name (optional)" class="form-field">
                        <input type="text" name="state" placeholder="State (optional)" maxlength="12" class="form-field">
                    </div>
                    <textarea name="note" rows="4" placeholder="What follow-up do you want from this cluster?" class="form-textarea"></textarea>
                    <button type="submit" class="button-link" style="grid-column: 1 / -1; justify-content: center;">${leadContext.submitLabel()}</button>
                </form>
            </section>
        @endif
    </main>
`)
