@param com.example.wellwater.pseo.PseoDetailView pageView
@param com.example.wellwater.web.page.SeoMetadata seo
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus

@template.layout.main(title = pageView.page().title(), robots = "index,follow", description = pageView.page().metaDescription(), canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), content = @`
    <main class="stack">
        <section class="hero">
            <div class="hero-grid">
                <div class="hero-copy">
                    <span class="eyebrow">${pageView.archetypeLabel()}</span>
                    <h1>${pageView.page().h1()}</h1>
                    <p class="hero-lead">${pageView.page().intro()}</p>
                    <div class="chip-row" style="margin-top: 18px;">
                        <span class="chip">${pageView.riskLabel()}</span>
                        <span class="chip">${pageView.householdLabel()}</span>
                        <span class="chip">${pageView.intentLabel()}</span>
                    </div>
                    <div class="hero-actions">
                        <a class="button-link" href="${pageView.entryHint().href()}">${pageView.entryHint().label()}</a>
                        <a class="button-link-secondary" href="${pageView.page().primaryCtaUrl()}">${pageView.page().primaryCtaLabel()}</a>
                    </div>
                </div>
                <aside class="hero-note">
                    <strong>What to figure out first</strong>
                    <p class="muted" style="color: rgba(246, 251, 249, 0.78);">${pageView.riskSummary()}</p>
                    <div class="chip-row">
                        <span class="chip">${pageView.page().family()}</span>
                        <span class="chip">Test before treatment</span>
                        <span class="chip">Compare after scope</span>
                    </div>
                </aside>
            </div>
        </section>

        <section class="panel panel-accent">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">First questions</p>
                    <h2>What it usually means, what to do today, and what to test next</h2>
                </div>
                <p class="section-copy">These are the answers most people want before they trust a treatment recommendation.</p>
            </div>
            <div class="grid">
                @for (var answer : pageView.quickAnswers())
                    <article class="card-link">
                        <span class="card-kicker">${pageView.archetypeLabel()}</span>
                        <h3>${answer.title()}</h3>
                        <p class="card-meta">${answer.body()}</p>
                    </article>
                @endfor
            </div>
        </section>

        @if(pageView.regionalContext() != null)
            <section class="panel">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">Regional delta</p>
                        <h2>Why ${pageView.regionalContext().stateName()} changes the answer</h2>
                    </div>
                    <p class="section-copy">This page earns its place only when the state context changes the testing path, evidence threshold, or the order of next steps.</p>
                </div>
                <div class="grid">
                    <article class="card-link card-link-accent">
                        <span class="card-kicker">Local difference</span>
                        <p class="card-meta">${pageView.regionalContext().localDelta()}</p>
                    </article>
                    <article class="card-link">
                        <span class="card-kicker">Use state guidance first</span>
                        <p class="card-meta">${pageView.regionalContext().decisionTrigger()}</p>
                    </article>
                    <article class="card-link">
                        <span class="card-kicker">Lab path</span>
                        <p class="card-meta">${pageView.regionalContext().labNote()}</p>
                    </article>
                </div>
                <div class="cta-row" style="margin-top: 18px;">
                    <a class="button-link" href="${pageView.regionalContext().guidanceUrl()}">Open ${pageView.regionalContext().stateCode()} guidance</a>
                    <a class="button-link-secondary" href="${pageView.regionalContext().certifiedLabUrl()}">Find ${pageView.regionalContext().stateCode()} certified lab</a>
                </div>
            </section>
        @endif

        @if(pageView.decisionDoc() != null)
            <section class="panel">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">Decision doc</p>
                        <h2>One-line call, scope split, and retest logic</h2>
                    </div>
                    <p class="section-copy">These deeper blocks only appear on the highest-intent pages where public search traffic is close to a real decision.</p>
                </div>
                <div class="grid">
                    <article class="card-link card-link-accent">
                        <span class="card-kicker">One-line call</span>
                        <p class="card-meta">${pageView.decisionDoc().oneLineVerdict()}</p>
                    </article>
                    <article class="card-link">
                        <span class="card-kicker">Health vs nuisance</span>
                        <p class="card-meta">${pageView.decisionDoc().healthVsNuisance()}</p>
                    </article>
                    <article class="card-link">
                        <span class="card-kicker">Retest timing</span>
                        <p class="card-meta">${pageView.decisionDoc().retestTiming()}</p>
                    </article>
                </div>
            </section>
        @endif

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
                        <p>${pageView.page().actionNow()}</p>
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
                        <p>${pageView.page().whatToTest()}</p>
                    </div>
                </section>

                @if(pageView.decisionDoc() != null)
                    <section class="panel">
                        <div class="section-header">
                            <div>
                                <p class="brand-kicker">Next moves</p>
                                <h2>Three actions before you buy anything</h2>
                            </div>
                        </div>
                        <div class="stack" style="gap: 12px;">
                            @for (var step : pageView.decisionDoc().nextSteps())
                                <article class="card-link">
                                    <span class="card-kicker">Action</span>
                                    <p class="card-meta">${step}</p>
                                </article>
                            @endfor
                        </div>
                    </section>

                    @if(!pageView.decisionDoc().decisionSplits().isEmpty())
                        <section class="panel panel-accent">
                            <div class="section-header">
                                <div>
                                    <p class="brand-kicker">Decision splits</p>
                                    <h2>What changes the decision fastest</h2>
                                </div>
                            </div>
                            <div class="stack" style="gap: 12px;">
                                @for (var split : pageView.decisionDoc().decisionSplits())
                                    <article class="card-link">
                                        <span class="card-kicker">${split.signal()}</span>
                                        <p class="card-meta">${split.meaning()}</p>
                                    </article>
                                @endfor
                            </div>
                        </section>
                    @endif

                    <section class="panel">
                        <div class="section-header">
                            <div>
                                <p class="brand-kicker">Common confusion</p>
                                <h2>What people usually get wrong here</h2>
                            </div>
                        </div>
                        <div class="stack" style="gap: 12px;">
                            @for (var confusion : pageView.decisionDoc().commonConfusions())
                                <article class="card-link">
                                    <span class="card-kicker">Avoid this mistake</span>
                                    <p class="card-meta">${confusion}</p>
                                </article>
                            @endfor
                        </div>
                    </section>

                    @if(!pageView.decisionDoc().escalationSignals().isEmpty())
                        <section class="panel">
                            <div class="section-header">
                                <div>
                                    <p class="brand-kicker">Escalation</p>
                                    <h2>Escalate now if</h2>
                                </div>
                            </div>
                            <div class="stack" style="gap: 12px;">
                                @for (var signal : pageView.decisionDoc().escalationSignals())
                                    <article class="card-link card-link-accent">
                                        <span class="card-kicker">Escalate</span>
                                        <p class="card-meta">${signal}</p>
                                    </article>
                                @endfor
                            </div>
                        </section>
                    @endif
                @endif

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Need a personalized next step?</p>
                            <h2>Use the matching tool</h2>
                        </div>
                        <p class="section-copy">Personalized results are rendered as noindex pages after the tool collects context.</p>
                    </div>
                    <div class="cta-row">
                        <a class="button-link" href="${pageView.entryHint().href()}">${pageView.entryHint().label()}</a>
                    </div>
                    <div class="prose" style="margin-top: 16px;">
                        <p>${pageView.entryHint().reason()}</p>
                    </div>
                </section>
            </div>

            <aside class="detail-stack">
                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Best-fit household</p>
                            <h2>${pageView.householdLabel()}</h2>
                        </div>
                    </div>
                    <p class="card-meta">${pageView.householdSummary()}</p>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Before you buy anything</p>
                            <h2>Do not buy yet</h2>
                        </div>
                    </div>
                    <p class="card-meta">${pageView.doNotBuyYet()}</p>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Next steps</p>
                            <h2>Primary action path</h2>
                        </div>
                    </div>
                    <a class="card-link card-link-accent" href="${pageView.page().primaryCtaUrl()}">
                        <span class="card-kicker">Primary</span>
                        <h3>${pageView.page().primaryCtaLabel()}</h3>
                        <p class="card-meta">Use this when you need the direct next action outside the tool.</p>
                    </a>
                </section>

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Commercial research</p>
                            <h2>Compare paths before vendor links</h2>
                        </div>
                    </div>
                    <a class="card-link" href="${pageView.page().moneyCtaUrl()}">
                        <span class="card-kicker">Internal compare path</span>
                        <h3>${pageView.page().moneyCtaLabel()}</h3>
                        <p class="card-meta">${pageView.page().disclosure()}</p>
                    </a>
                </section>

                @if(leadContext != null)
                    <section class="panel" id="lead-capture">
                        <div class="section-header">
                            <div>
                                <p class="brand-kicker">Lead capture</p>
                                <h2>${leadContext.heading()}</h2>
                            </div>
                        </div>
                        <p class="card-meta">${leadContext.summary()}</p>
                        @if("success".equals(leadStatus))
                            <p class="card-meta" style="margin-top: 14px; color: #0f766e; font-weight: 700;">Lead captured. Check <code>/admin</code> for the new row.</p>
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
                            <div class="form-grid two">
                                <input type="text" name="name" placeholder="Name (optional)" class="form-field">
                                <input type="text" name="state" placeholder="State (optional)" maxlength="12" class="form-field">
                            </div>
                            <textarea name="note" rows="4" placeholder="What follow-up do you want from this page?" class="form-textarea"></textarea>
                            <button type="submit" class="button-link" style="width: 100%; justify-content: center;">${leadContext.submitLabel()}</button>
                        </form>
                    </section>
                @endif

                <section class="panel">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Source trail</p>
                            <h2>Review and traceability</h2>
                        </div>
                    </div>
                    <div class="meta-list" style="margin-bottom: 16px;">
                        <div class="meta-item">
                            <span class="meta-label">Reviewed by</span>
                            <div class="meta-value">Private Well Editorial Desk</div>
                        </div>
                        <div class="meta-item">
                            <span class="meta-label">Last source refresh</span>
                            <div class="meta-value">${pageView.page().fetchedAt()}</div>
                        </div>
                        <div class="meta-item">
                            <span class="meta-label">Primary official sources</span>
                            <div class="meta-value">${pageView.citations().size()} linked sources on this page</div>
                        </div>
                    </div>
                    <div class="stack" style="gap: 12px;">
                        @for (var citation : pageView.citations())
                            <a class="card-link" href="${citation.url()}">
                                <span class="card-kicker">Official source</span>
                                <h3>${citation.label()}</h3>
                                <p class="card-meta">${citation.url()}</p>
                            </a>
                        @endfor
                    </div>
                    <div class="meta-list">
                        <div class="meta-item">
                            <span class="meta-label">Source dates</span>
                            <div class="meta-value">searched at ${pageView.page().searchPerformedAt()} / fetched at ${pageView.page().fetchedAt()}</div>
                        </div>
                        <div class="meta-item">
                            <span class="meta-label">What this page does not do</span>
                            <div class="meta-value">It does not replace emergency, medical, or state-certified instructions when the household risk is still unresolved.</div>
                        </div>
                    </div>
                </section>

                <section class="panel panel-accent">
                    <div class="section-header">
                        <div>
                            <p class="brand-kicker">Trust surface</p>
                            <h2>Method, review, and disclosure</h2>
                        </div>
                    </div>
                    <p class="card-meta">This page sits inside a larger trust layer. Use these public pages to inspect the rules behind review, evidence, saved results, and commercial gating.</p>
                    <div class="stack" style="gap: 12px; margin-top: 16px;">
                        <a class="card-link" href="/trust/methodology">
                            <span class="card-kicker">Methodology</span>
                            <h3>How the engine separates testing, interpretation, and compare paths</h3>
                            <p class="card-meta">Review the public evidence hierarchy and why personalized outputs stay noindex.</p>
                        </a>
                        <a class="card-link" href="/trust/reviewers-and-expertise">
                            <span class="card-kicker">Reviewers</span>
                            <h3>Who reviews the guidance and what the review does not claim</h3>
                            <p class="card-meta">Check reviewer visibility, scope limits, and claim-check responsibilities.</p>
                        </a>
                        <a class="card-link" href="/trust/commercial-disclosure">
                            <span class="card-kicker">Commercial disclosure</span>
                            <h3>How compare paths and follow-up routes are constrained</h3>
                            <p class="card-meta">Use this before treating any CTA like proof that a household problem is solved.</p>
                        </a>
                    </div>
                </section>
            </aside>
        </section>

        @if(pageView.decisionDoc() != null && !pageView.decisionDoc().faqs().isEmpty())
            <section class="panel">
                <div class="section-header">
                    <div>
                        <p class="brand-kicker">FAQ</p>
                        <h2>Questions that should be answered before a purchase</h2>
                    </div>
                </div>
                <div class="stack" style="gap: 14px;">
                    @for (var faq : pageView.decisionDoc().faqs())
                        <article class="card-link">
                            <span class="card-kicker">Question</span>
                            <h3>${faq.question()}</h3>
                            <p class="card-meta">${faq.answer()}</p>
                        </article>
                    @endfor
                </div>
            </section>
        @endif

        <section class="panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Internal link graph</p>
                    <h2>Related next reads</h2>
                </div>
                <p class="section-copy">This is where acquisition pages become a graph instead of a flat pile of long-tail pages.</p>
            </div>
            <div class="stack" style="gap: 18px;">
                @for (var section : pageView.relatedSections())
                    <section class="panel panel-accent" style="padding: 20px;">
                        <div class="section-header" style="margin-bottom: 16px;">
                            <div>
                                <p class="brand-kicker">${section.title()}</p>
                                <h3>${section.description()}</h3>
                            </div>
                        </div>
                        <div class="grid">
                            @for (var relatedPage : section.pages())
                                <a class="card-link" href="/well-water/${relatedPage.slug()}">
                                    <span class="card-kicker">${relatedPage.family()}</span>
                                    <h3>${relatedPage.h1()}</h3>
                                    <p class="card-meta">${relatedPage.metaDescription()}</p>
                                </a>
                            @endfor
                        </div>
                    </section>
                @endfor
            </div>
        </section>
    </main>
`)
