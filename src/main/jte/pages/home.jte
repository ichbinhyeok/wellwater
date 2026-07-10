@param com.example.wellwater.web.page.SeoMetadata seo

@template.layout.main(title = "Water Verdict | Find The Right Private-Well Test", robots = seo.robotsDirective(), description = "Choose the focused private-well testing panel and certified lab path for an annual check, visible clue, nearby risk, flood, or home purchase.", canonicalUrl = seo.canonicalUrl(), breadcrumbs = seo.breadcrumbs(), structuredDataBlocks = seo.structuredDataBlocks(), openGraphType = seo.openGraphType(), socialImageUrl = seo.socialImageUrl(), googleSiteVerification = seo.googleSiteVerification(), googleAnalyticsId = seo.googleAnalyticsId(), content = @`
    <main class="stack home-tool-first">
        <section class="hero home-hero">
            <div class="home-hero-copy">
                <span class="eyebrow">Private-well test finder</span>
                <h1>Find the right water test before you buy the wrong one.</h1>
                <p class="hero-lead">Choose why you are testing, add any clues you already have, and get a focused panel plus a certified-lab path. No report, account, or email required.</p>
                <div class="home-proof-row">
                    <span>About 60 seconds</span>
                    <span>U.S. private wells</span>
                    <span>Official guidance first</span>
                </div>
            </div>
            <aside class="home-hero-aside">
                <p class="brand-kicker">One output</p>
                <strong>What to test next</strong>
                <p>Not another article list. The result names the panel, urgency, official source, and certified lab route.</p>
                <a href="#test-plan">Start with one choice</a>
            </aside>
        </section>

        <section id="test-plan" class="panel test-plan-panel">
            <div class="section-header">
                <div>
                    <p class="brand-kicker">Step 1 of 1</p>
                    <h2>Why are you testing your well?</h2>
                </div>
                <p class="section-copy">Pick the closest reason. Everything else is optional and only changes the panel when it matters.</p>
            </div>

            <form method="post" action="/tool/test-plan" class="test-plan-form">
                <fieldset class="choice-grid reason-grid">
                    <legend class="sr-only">Reason for testing</legend>
                    <label class="choice-card"><input type="radio" name="reason" value="annual" checked><span><strong>Annual check</strong><small>No obvious problem</small></span></label>
                    <label class="choice-card"><input type="radio" name="reason" value="symptom"><span><strong>Something changed</strong><small>Smell, taste, stain, or cloudiness</small></span></label>
                    <label class="choice-card"><input type="radio" name="reason" value="home_purchase"><span><strong>Buying or selling</strong><small>Transaction and state context</small></span></label>
                    <label class="choice-card choice-card-alert"><input type="radio" name="reason" value="after_flood"><span><strong>After a flood</strong><small>Urgent certified path</small></span></label>
                    <label class="choice-card"><input type="radio" name="reason" value="after_heavy_rain"><span><strong>After heavy rain</strong><small>Disturbance-sensitive retest</small></span></label>
                    <label class="choice-card"><input type="radio" name="reason" value="new_or_unused_well"><span><strong>New or unused well</strong><small>First baseline</small></span></label>
                </fieldset>

                <div class="form-split">
                    <fieldset>
                        <legend>Clues you can see, smell, or taste <span>optional</span></legend>
                        <div class="check-grid">
                            <label><input type="checkbox" name="signals" value="rotten_egg_smell"> Rotten-egg smell</label>
                            <label><input type="checkbox" name="signals" value="metallic_taste"> Metallic taste</label>
                            <label><input type="checkbox" name="signals" value="orange_stains"> Orange stains</label>
                            <label><input type="checkbox" name="signals" value="blue_green_stains"> Blue-green stains</label>
                            <label><input type="checkbox" name="signals" value="black_stains"> Black stains</label>
                            <label><input type="checkbox" name="signals" value="cloudy_water"> Cloudy water</label>
                            <label><input type="checkbox" name="signals" value="scale_buildup"> Scale buildup</label>
                        </div>
                    </fieldset>

                    <fieldset>
                        <legend>Nearby risk context <span>optional</span></legend>
                        <div class="check-grid">
                            <label><input type="checkbox" name="riskContexts" value="agriculture"> Agriculture</label>
                            <label><input type="checkbox" name="riskContexts" value="nearby_septic"> Nearby septic</label>
                            <label><input type="checkbox" name="riskContexts" value="industrial"> Industrial activity</label>
                            <label><input type="checkbox" name="riskContexts" value="mining"> Mining activity</label>
                            <label><input type="checkbox" name="riskContexts" value="fuel_or_petroleum"> Fuel or petroleum</label>
                            <label><input type="checkbox" name="riskContexts" value="pfas_source"> Known PFAS source</label>
                            <label><input type="checkbox" name="riskContexts" value="radionuclides"> Radionuclide concern</label>
                        </div>
                    </fieldset>
                </div>

                <details class="plan-details">
                    <summary>Add state or existing treatment</summary>
                    <div class="detail-fields">
                        <label>State code <span>optional</span><input name="stateCode" maxlength="2" pattern="[A-Za-z]{2}" placeholder="OR" autocomplete="address-level1"></label>
                        <label>Existing treatment
                            <select name="existingTreatment">
                                <option value="unknown">Unknown</option><option value="none">None</option><option value="ro">Reverse osmosis</option><option value="uv">UV</option><option value="softener">Softener</option><option value="iron_filter">Iron filter</option><option value="carbon">Carbon</option><option value="sediment">Sediment filter</option>
                            </select>
                        </label>
                        <label>Use scope
                            <select name="useScope"><option value="drinking_only">Drinking water</option><option value="whole_house">Whole house</option><option value="both">Both</option><option value="unknown">Not sure</option></select>
                        </label>
                    </div>
                </details>

                <div class="plan-submit-row">
                    <button class="button-link" type="submit">Build my test plan</button>
                    <p>No contact details. Inputs are not stored in the pivot metrics.</p>
                </div>
            </form>
        </section>

        <section class="home-trust-strip">
            <div><span>01</span><strong>Diagnose</strong><p>Your reason and clues define the panel.</p></div>
            <div><span>02</span><strong>Choose</strong><p>Get a certified path before any product.</p></div>
            <div><span>03</span><strong>Act</strong><p>Use the official link or matching physical kit.</p></div>
            <a href="/trust/methodology">Read the method only if you need it</a>
        </section>
    </main>

    <style>
        .home-tool-first { gap: 26px; }
        .home-hero { min-height: 430px; display: grid; grid-template-columns: minmax(0, 1.4fr) minmax(260px, .6fr); gap: 30px; align-items: end; background:
            radial-gradient(circle at 92% 15%, rgba(157, 205, 179, .2), transparent 34%),
            linear-gradient(135deg, #10271e, #1e4c3a); }
        .home-hero h1 { max-width: 820px; font-size: clamp(3rem, 7vw, 6.2rem); line-height: .93; letter-spacing: -.055em; }
        .home-hero-copy { align-self: center; }
        .home-proof-row { display: flex; flex-wrap: wrap; gap: 8px; margin-top: 24px; }
        .home-proof-row span { border: 1px solid rgba(255,255,255,.2); border-radius: 999px; padding: 8px 11px; color: rgba(255,255,255,.82); font-size: .78rem; font-weight: 700; }
        .home-hero-aside { border: 1px solid rgba(255,255,255,.18); border-radius: 22px; padding: 22px; background: rgba(4, 19, 13, .24); }
        .home-hero-aside strong { display: block; font: 700 1.6rem/1.05 Georgia, serif; }
        .home-hero-aside p { color: rgba(255,255,255,.72); line-height: 1.55; }
        .home-hero-aside a { color: #d9f1e4; font-weight: 800; text-decoration: none; }
        .test-plan-panel { padding: clamp(22px, 4vw, 44px); }
        .test-plan-form { display: grid; gap: 28px; margin-top: 26px; }
        fieldset { min-width: 0; margin: 0; padding: 0; border: 0; }
        legend { margin-bottom: 12px; font-weight: 800; }
        legend span, .detail-fields label > span { color: var(--muted); font-size: .72rem; font-weight: 600; text-transform: uppercase; }
        .choice-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 10px; }
        .choice-card { cursor: pointer; }
        .choice-card input { position: absolute; opacity: 0; pointer-events: none; }
        .choice-card span { display: grid; min-height: 104px; align-content: center; gap: 6px; border: 1px solid var(--line); border-radius: 17px; padding: 16px; background: var(--paper); transition: border-color .16s ease, transform .16s ease, box-shadow .16s ease; }
        .choice-card strong { font: 700 1.04rem/1.1 Georgia, serif; }
        .choice-card small { color: var(--muted); line-height: 1.35; }
        .choice-card input:checked + span { border-color: #246b50; box-shadow: 0 0 0 3px rgba(36,107,80,.13); transform: translateY(-2px); }
        .choice-card-alert input:checked + span { border-color: #a73f35; box-shadow: 0 0 0 3px rgba(167,63,53,.12); }
        .form-split { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 22px; }
        .check-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 8px; }
        .check-grid label { border: 1px solid var(--line); border-radius: 12px; padding: 11px 12px; color: #293a32; font-size: .88rem; background: #fff; }
        .check-grid input { accent-color: #246b50; }
        .plan-details { border-top: 1px solid var(--line); border-bottom: 1px solid var(--line); padding: 16px 0; }
        .plan-details summary { cursor: pointer; font-weight: 800; }
        .detail-fields { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; margin-top: 16px; }
        .detail-fields label { display: grid; gap: 7px; font-size: .84rem; font-weight: 800; }
        .detail-fields input, .detail-fields select { width: 100%; border: 1px solid var(--line); border-radius: 12px; padding: 12px; background: #fff; color: var(--ink); }
        .plan-submit-row { display: flex; align-items: center; gap: 16px; }
        .plan-submit-row button { border: 0; cursor: pointer; font-size: 1rem; }
        .plan-submit-row p { margin: 0; color: var(--muted); font-size: .78rem; }
        .home-trust-strip { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)) auto; gap: 10px; align-items: stretch; }
        .home-trust-strip > * { border: 1px solid var(--line); border-radius: 16px; padding: 16px; background: rgba(255,255,255,.72); }
        .home-trust-strip span { color: #246b50; font-size: .72rem; font-weight: 900; }
        .home-trust-strip strong { display: block; margin-top: 6px; font-family: Georgia, serif; }
        .home-trust-strip p { margin: 6px 0 0; color: var(--muted); font-size: .78rem; line-height: 1.4; }
        .home-trust-strip > a { display: grid; place-items: center; max-width: 220px; color: var(--ink); font-weight: 800; text-align: center; text-decoration: none; }
        .sr-only { position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip: rect(0,0,0,0); white-space: nowrap; border: 0; }
        @media (max-width: 900px) { .home-hero { grid-template-columns: 1fr; min-height: auto; } .home-hero h1 { font-size: clamp(3rem, 12vw, 5rem); } .choice-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); } .home-trust-strip { grid-template-columns: repeat(2, minmax(0, 1fr)); } .home-trust-strip > a { max-width: none; } }
        @media (max-width: 640px) { .choice-grid, .form-split, .check-grid, .detail-fields, .home-trust-strip { grid-template-columns: 1fr; } .choice-card span { min-height: 84px; } .plan-submit-row { align-items: stretch; flex-direction: column; } .plan-submit-row .button-link { justify-content: center; } }
    </style>
`)
