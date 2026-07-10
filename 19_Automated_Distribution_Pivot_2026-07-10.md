# Automated Distribution Pivot - 2026-07-10

## Decision

Water Verdict is no longer operated as an SEO publisher with a tool attached. It is an automated private-well test-selection product with a first-party web surface and a capped 90-day OpenAI distribution experiment.

The wedge stays narrow: choose a private-well test panel before treatment or test-kit purchase. The reachable surface stays broad through structured reasons, signals, risk contexts, state resources, and ChatGPT conversations. Broadness comes from the engine's inputs and outputs, not from publishing more near-duplicate pages.

## Why The Model Changed

The Search Console evidence did not support another cycle of page-level CTR and internal-link optimization:
- `180`-day total: `1` click, `1,568` impressions, average position `40.3`
- latest `28` days: `0` clicks, `13` impressions, average position `22.6`
- successive impression blocks: `102 -> 850 -> 417 -> 186 -> 13`
- the latest block is down about `98.5%` from the peak block
- the sitemap exposes about `75` URLs while the submitted-page report covered `74` and showed no dependable indexed growth signal at review time

This does not prove that the domain is permanently dead. It does prove that organic search is not earning more development investment under the current low-touch operating constraint.

The OpenAI channel is not assumed to replace search. Public evidence shows strong platform reach and good fit for practical guidance, but no dependable install, invocation, or revenue benchmark for independent ChatGPT apps. The plugin is therefore a low-cost falsification test, not the product identity or a proven acquisition engine. See `21_ChatGPT_App_Distribution_Research_2026-07-10.md`.

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
- lab-result interpretation in the MCP app inside the plugin
- diagnosis, medical advice, legal clearance, or emergency response
- accounts, saved app results, direct payment, lead capture, or manual consulting
- names, email addresses, street addresses, medical histories, and uploaded reports

## Commercial Safety Gates

Only configured HTTPS URLs on `mytapscore.com` or `www.mytapscore.com` can receive a partner redirect. Missing or unsafe URLs suppress the offer.

Offers are suppressed for immediate escalation and for home purchase, known contamination, flood, wildfire, PFAS, fuel, and radionuclide paths. Official resources and certified-lab routes are always shown independently of commerce.

The initial ChatGPT tool schema and widget contain no commerce. Current OpenAI guidance describes generally available external checkout on the developer's own domain, while Water Verdict hands off to a third-party merchant. Web commerce remains independently configurable, but any future ChatGPT commerce requires explicit policy confirmation, a code change, and review before release.

## Measurement Contract

The application writes only these aggregate fields:

```text
date,event_name,channel,result_family,partner_product,outcome,latency_ms
```

No raw tool answers, account ID, session ID, IP address, contact detail, or precise location is stored in the pivot metric. The protected summary is available at `/admin/pivot-metrics`.

Primary counters:
- completed plans by `web` and `chatgpt`
- failed tool calls
- official-guidance and certified-lab clicks
- web partner-eligible completions and redirects, outside the initial ChatGPT KPI
- result-family mix
- average completion latency

The 90-day clock begins on the date the approved plugin is publicly published, not on the code deployment or approval date. Set that date as `APP_PIVOT_EXPERIMENT_START_DATE=YYYY-MM-DD`; the protected summary excludes review, QA, and pre-publication rows before that date.

`chatgptExternalActionRatePct` is an action-to-completion ratio, not a unique-user conversion rate. No user or session identifier is retained, so repeated actions can make the ratio exceed `100%`.

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
- ChatGPT external-action ratio of at least `10%` across official guidance and certified-lab paths
- tool failure rate remains below `5%`

If usage passes but clicks fail, revise offer fit and result presentation once. Do not widen the product scope.

### Day 90 - Keep, Iterate, Or Stop

- Keep the automatic distribution channel when completions are at least `250`, the ChatGPT external-action ratio is at least `10%`, and safety/quality gates pass.
- Keep the app in maintenance-only mode when utility passes; the initial experiment does not validate monetization.
- Run one final narrow metadata or result-presentation iteration when completions are `100-249` and safety/quality gates pass.
- Stop active investment when completions are below `100`, or when failure/safety gates repeatedly fail.

These thresholds are minimum pulse checks, not statistically conclusive proof of a durable business. They validate only whether automatic ChatGPT distribution produces useful actions. They do not validate a revenue model.

## Search Surface Policy

During the 90-day plugin test:
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
5. Verify the publisher identity, create a `With MCP` plugin draft, set the domain challenge token, scan `https://waterverdict.com/mcp`, and submit the plugin for review.
6. Run the direct, indirect, and negative discovery set in `21_ChatGPT_App_Distribution_Research_2026-07-10.md` on ChatGPT web and mobile.
7. Publish the approved plugin, set `APP_PIVOT_EXPERIMENT_START_DATE`, and record the date in `14_Search_Console_Tracking_Log.md`; that date starts the KPI clock.
8. Do not add ChatGPT commerce without OpenAI policy confirmation and a new review of the changed app surface.

## Automated Operations

- every push to `master` runs tests, builds the image, deploys, and verifies `/health/app`
- the scheduled `Production Health Check` workflow verifies the public health contract and MCP tool listing daily
- unsafe partner configuration fails closed by hiding commerce
- admin credentials are never stored in the repository
- ongoing outbound, lead handling, content publishing, and manual result interpretation are not part of this model
