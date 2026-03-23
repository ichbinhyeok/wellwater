# Search Console Tracking Log

Use this file as the ongoing operating log for organic search progress on `sc-domain:waterverdict.com`.

The goal is not to dump raw metrics.
The goal is to record:
- what date range was checked
- what the data actually said
- what was changed in the product or content
- what the current interpretation is
- what should be checked next

## Tracking Format

For each review, keep this order:
1. Date
2. Data
3. Changes shipped
4. Insights
5. Next check

Keep absolute dates in every entry.
Do not write "today", "yesterday", or "last week" without the real dates.

## Entry: 2026-03-23

### Scope

- Property reviewed: `sc-domain:waterverdict.com`
- Site context: user reported production deploy was about 2 weeks before `2026-03-23`
- Infra context: Cloudflare SSL mode remains `Flexible`
- Edge redirect context: `Always Use HTTPS` is enabled

### Data

#### Search Performance

- Comparison window reviewed:
- current: `2026-02-21` to `2026-03-20`
- previous: `2026-01-24` to `2026-02-20`
- Current period:
- impressions: `277`
- clicks: `0`
- average position: `36.1`
- Previous period:
- impressions: effectively near `0`
- clicks: `0`
- Interpretation:
- this is not a traffic drop pattern
- this is an early indexing and first-impressions pattern

#### Query Pattern

Top emerging query themes:
- `arsenic in well water new hampshire`
- `arsenic water test new hampshire`
- `well water testing oregon`
- `why does my water taste metallic`

Interpretation:
- search demand is currently clustering around a small number of high-intent regional and symptom queries
- query-page matching is beginning, but rankings are still too weak for clicks

#### Page Pattern

Top emerging pages:
- `new-hampshire-arsenic-well-water`
- `oregon-private-well-testing-recommendations`
- `/`
- `metallic-taste`
- `home-purchase-test`

Interpretation:
- Google is already finding the intended cluster leaders
- the site does not need a broad rewrite yet
- it needs stronger ranking support around the pages that are already getting impressions

#### Device Pattern

- desktop: `194` impressions, average position `31.1`
- mobile: `82` impressions, average position `48.4`

Interpretation:
- mobile visibility is materially weaker than desktop
- for now this is a secondary issue, because the bigger problem is still rank entry rather than CTR

#### Anomaly Read

- no meaningful crash or demand collapse was detected
- daily impressions started appearing around `2026-03-08`
- weekly impression pattern was rising, not falling

Interpretation:
- the site looks newly discoverable rather than newly penalized
- the stronger reading is "Google started testing pages" rather than "Google stopped trusting pages"

#### URL Inspection Findings

Search Console inspection found mixed protocol signals:
- some `http://waterverdict.com/...` URLs were indexed or selected as canonical
- examples checked:
- `http://waterverdict.com/well-water/metallic-taste`
- `http://waterverdict.com/well-water/new-jersey-pwta-private-well-testing`
- in those cases, Search Console showed `user canonical = https` but `google canonical = http`

Interpretation:
- canonical and sitemap signals were not fully aligned
- this was the main technical SEO issue found in this review

### Changes Shipped

#### Technical SEO Fixes

Goal:
- stop request-scheme leakage from generating `http` absolute URLs in `robots.txt` and `sitemap.xml`

Files changed:
- `src/main/java/com/example/wellwater/web/page/PageController.java`
- `src/main/resources/application.properties`
- `src/main/resources/application-prod.properties`
- `src/test/java/com/example/wellwater/web/page/PageControllerTest.java`

What changed:
- `robots.txt` and `sitemap.xml` now use the configured site base URL instead of request scheme reconstruction
- forwarded header handling was enabled with `server.forward-headers-strategy=framework`

Why this matters:
- with Cloudflare `Flexible`, the origin can still see HTTP
- `Always Use HTTPS` only fixes the browser-edge request path
- it does not stop the application from constructing `http` URLs if the app trusts the origin request scheme

#### Search-Led Content Improvements

Goal:
- strengthen the pages that were already earning impressions

Files changed:
- `data/pseo/pages.csv`
- `src/main/java/com/example/wellwater/pseo/PseoExperienceService.java`

What changed:
- sharpened query alignment for:
- `metallic-taste`
- `home-purchase-test`
- `new-hampshire-arsenic-well-water`
- `new-jersey-pwta-private-well-testing`
- `oregon-private-well-testing-recommendations`
- strengthened cluster companion links around:
- New Hampshire arsenic
- Oregon private well testing
- metallic taste / corrosion
- home purchase testing

#### Support Page Expansion

Goal:
- create authority support pages behind the clusters already showing Search Console traction

Files changed:
- `data/pseo/pages.csv`
- `data/pseo/page_sources.csv`
- `src/main/java/com/example/wellwater/pseo/PseoExperienceService.java`
- `src/main/java/com/example/wellwater/pseo/PseoDecisionDocService.java`
- `src/test/java/com/example/wellwater/pseo/PseoExperienceServiceTest.java`

New pages added:
- `new-hampshire-arsenic-testing-order`
- `metallic-taste-plumbing-vs-source-water`
- `oregon-private-well-homebuyer-testing`

Why these were added:
- they directly reinforce the three strongest emerging Search Console clusters:
- New Hampshire arsenic
- metallic taste / corrosion
- Oregon private well testing and homebuyer intent

### Verification

Tests run:

```powershell
./gradlew test --tests com.example.wellwater.pseo.PseoExperienceServiceTest --tests com.example.wellwater.pseo.PseoCatalogServiceTest --tests com.example.wellwater.web.page.PageControllerTest --tests com.example.wellwater.web.page.SeoMetadataServiceTest
```

Result:
- passed on `2026-03-23`

### Insights

#### Primary Read

- the site is in the "first visibility" phase, not the "traffic recovery" phase
- impressions matter more than clicks right now because most visible queries are still outside strong click positions

#### Main Constraint

- the biggest technical issue was mixed `http` and `https` canonical signaling
- the biggest product issue was not lack of page count
- it was weak support depth around the few clusters already being tested by Google

#### What Not To Overreact To Yet

- zero clicks
- sitemap summary inconsistencies in Search Console
- low CTR

Reason:
- the site still needs more time for indexing and ranking stabilization after the recent deploy and protocol cleanup

#### Current Priority Order

1. deploy the protocol fix and content changes
2. verify `robots.txt` and `sitemap.xml` no longer emit `http://waterverdict.com`
3. resubmit sitemap in Search Console
4. re-inspect the previously problematic `http` URLs
5. wait for Search Console to reflect the new canonical and cluster signals

### Next Check

Target follow-up window:
- first recheck: `2026-03-30` to `2026-04-02`
- second recheck: `2026-04-06` to `2026-04-09`

Questions for the next review:
- did `http` impressions disappear or materially decline?
- did `new-hampshire-arsenic-well-water` move closer to page 2 or page 1?
- did `oregon-private-well-testing-recommendations` gain broader query coverage?
- did `metallic-taste` begin earning clicks or improved average position?
- did the three new support pages start receiving impressions?

What to record next time:
- impressions, clicks, average position for the same 28-day comparison
- top 10 queries
- top 10 pages
- mobile vs desktop split
- whether `http` canonical contamination is still visible
- whether the three support pages have impressions

## Reusable Entry Template

Copy this block for the next review.

```md
## Entry: YYYY-MM-DD

### Scope
- Property reviewed:
- Context:

### Data
#### Search Performance
- current window:
- previous window:
- impressions:
- clicks:
- average position:

#### Query Pattern
- top queries:

#### Page Pattern
- top pages:

#### Device Pattern
- desktop:
- mobile:

#### Inspection Findings
- canonical:
- sitemap:
- rich result eligibility:

### Changes Shipped
- files changed:
- pages added or revised:
- infra changes:

### Verification
- commands run:
- result:

### Insights
- what changed:
- what likely caused it:
- what still matters most:

### Next Check
- target date:
- exact questions to answer:
```
