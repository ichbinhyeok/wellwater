# Automated Distribution Pivot - 2026-07-10

## Decision

Water Verdict is no longer operated as an SEO publisher with a tool attached. It is an automated private-well test-selection product distributed first through the web tool and the ChatGPT app directory.

The wedge stays narrow: choose a private-well test panel before treatment or test-kit purchase. The reachable surface stays broad through structured reasons, signals, risk contexts, state resources, and ChatGPT conversations. Broadness comes from the engine's inputs and outputs, not from publishing more near-duplicate pages.

## Why The Model Changed

The Search Console evidence did not support another cycle of page-level CTR and internal-link optimization:
- `180`-day total: `1` click, `1,568` impressions, average position `40.3`
- latest `28` days: `0` clicks, `13` impressions, average position `22.6`
- successive impression blocks: `102 -> 850 -> 417 -> 186 -> 13`
- the latest block is down about `98.5%` from the peak block
- the sitemap exposes about `75` URLs while the submitted-page report covered `74` and showed no dependable indexed growth signal at review time

This does not prove that the domain is permanently dead. It does prove that organic search is not earning more development investment under the current low-touch operating constraint.

## V1 Product Contract

The only promoted job is: build a focused U.S. private-well testing plan.

Supported inputs:
- reason for testing
- visible water clues
- nearby risk context
- optional two-letter state code
- optional existing treatment
- optional drinking or whole-house scope

Supported outputs:
- urgency
- recommended panel with a reason for every item
- official guidance
- certified-laboratory path
- optional physical-kit offer when eligible

Explicitly out of scope:
- lab-result interpretation in the ChatGPT app
- diagnosis, medical advice, legal clearance, or emergency response
- accounts, saved app results, direct payment, lead capture, or manual consulting
- names, email addresses, street addresses, medical histories, and uploaded reports

## Commercial Safety Gates

Only configured HTTPS URLs on `mytapscore.com` or `www.mytapscore.com` can receive a partner redirect. Missing or unsafe URLs suppress the offer.

Offers are suppressed for immediate escalation and for home purchase, known contamination, flood, wildfire, PFAS, fuel, and radionuclide paths. Official resources and certified-lab routes are always shown independently of commerce.

## Measurement Contract

The application writes only these aggregate fields:

```text
date,event_name,channel,result_family,partner_product,outcome,latency_ms
```

No raw tool answers, account ID, session ID, IP address, contact detail, or precise location is stored in the pivot metric. The protected summary is available at `/admin/pivot-metrics`.

Primary counters:
- completed plans by `web` and `chatgpt`
- failed tool calls
- partner-eligible completions
- partner clicks and successful redirects
- result-family mix
- average completion latency

The 90-day clock begins on the date the ChatGPT app is publicly approved, not on the code deployment date.

## 30 / 60 / 90 Day Gates

### Day 30 - Distribution Sanity

Continue only if all are true:
- at least `30` cumulative ChatGPT completions
- tool failure rate below `5%`
- average completion latency below `1,000 ms`
- no safety-gate regression or raw-input retention incident

If ChatGPT completions are below `30`, fix listing clarity or tool invocation metadata once. Do not add SEO pages or start outbound.

### Day 60 - Utility And Intent

Continue only if all are true:
- at least `100` cumulative ChatGPT completions
- at least `20` partner-eligible completions
- partner clicks are at least `5%` of partner-eligible completions
- tool failure rate remains below `5%`

If usage passes but clicks fail, revise offer fit and result presentation once. Do not widen the product scope.

### Day 90 - Keep, Iterate, Or Stop

- Scale maintenance-only operation when completions are at least `250`, eligible click-through is at least `5%`, and at least `10` partner redirects occurred.
- Run one final narrow iteration when completions are `100-249` and safety/quality gates pass.
- Stop active investment when completions are below `100`, or when failure/safety gates repeatedly fail.

Revenue confirmation remains in the partner's affiliate reporting because Water Verdict does not control checkout. Lack of an affiliate conversion API must not cause user-level tracking to be added.

## Search Surface Policy

During the 90-day app test:
- freeze the current sitemap inventory
- publish no new pSEO pages
- do not perform routine CTR-title or internal-link work
- preserve existing URLs so the product pivot is measured without a simultaneous index reset

After Day 90:
- retain pages that receive meaningful impressions, route users into the tool, or provide necessary trust/official context
- demote or remove from the sitemap pages that do none of those jobs
- expand only when a proven tool input or result family needs a public router, not to increase URL count by itself

## One-Time External Launch Checklist

These steps cannot be automated from this repository, but they are one-time setup rather than ongoing outreach:

1. Create GitHub Secrets for `APP_ADMIN_USERNAME` and `APP_ADMIN_PASSWORD` when dashboard access is needed. Until both exist, `/admin/**` stays disabled with `503`. Use URL-safe values because Docker Compose reads them from an env file.
2. Apply to the Tap Score affiliate program and set the approved Essential and Advanced product URLs in GitHub Secrets.
3. Configure `support@waterverdict.com` forwarding or a mailbox.
4. Set Cloudflare SSL/TLS to `Full (strict)` and confirm the origin certificate is valid.
5. Create the OpenAI organization/app listing, set the domain challenge token, verify `https://waterverdict.com/mcp`, and submit the app for directory review.
6. Record the public approval date in `14_Search_Console_Tracking_Log.md`; that date starts the KPI clock.

## Automated Operations

- every push to `master` runs tests, builds the image, deploys, and verifies `/health/app`
- the scheduled `Production Health Check` workflow verifies the public health contract and MCP tool listing daily
- unsafe partner configuration fails closed by hiding commerce
- admin credentials are never stored in the repository
- ongoing outbound, lead handling, content publishing, and manual result interpretation are not part of this model
