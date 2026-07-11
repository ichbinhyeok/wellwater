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

## Entry: 2026-04-01

### Scope

- Property reviewed: `sc-domain:waterverdict.com`
- Comparison window reviewed:
- current: `2026-03-02` to `2026-03-29`
- previous: `2026-02-02` to `2026-03-01`
- Previous tracked window for reference:
- prior log: `2026-02-21` to `2026-03-20`
- Infra context:
- Cloudflare still sits in front of the site
- live `robots.txt` and `sitemap.xml` now emit `https://waterverdict.com/...` only

### Data

#### Search Performance

- Current 28-day window:
- clicks: `0`
- impressions: `495`
- average position: `42.9`
- direct previous 28-day window in Search Console comparison:
- clicks: `0`
- impressions: `0`
- average position: `0`
- compared with the prior tracked window ending `2026-03-20`:
- impressions rose from `277` to `495`
- that is about `+78.7%`
- average position worsened from `36.1` to `42.9`

Interpretation:
- discovery and recall expanded
- ranking quality did not improve yet
- Google is testing more URLs and queries, but not promoting them upward

#### Query Pattern

Top query themes now:
- New Hampshire arsenic remains the strongest cluster
- New Jersey PWTA and private well testing queries emerged as the second real cluster
- metallic taste still appears, but deeper in the rankings
- Oregon private well testing is still present, but not as broad as New Hampshire or New Jersey

Representative queries from the current window:
- `arsenic in well water new hampshire` -> `80` impressions, average position `52.3`
- `arsenic water test new hampshire` -> `70` impressions, average position `56.2`
- `nj pwta water testing` -> `5` impressions, average position `29.6`
- `private well testing new jersey` -> `5` impressions, average position `31.6`

Interpretation:
- the site still has signal in the exact clusters identified in the `2026-03-23` review
- this is not a random-query drift problem
- the bigger issue is that most cluster pages are still ranking too low to earn clicks

#### Page Pattern

Current focus-page totals from raw query-page aggregation:
- `new-hampshire-arsenic-well-water` -> `153` impressions, average position `54.1`
- `new-jersey-pwta-private-well-testing` -> `89` impressions, average position `46.2`
- `metallic-taste` -> `69` impressions, average position `77.0`
- `oregon-private-well-testing-recommendations` -> `46` impressions, average position `44.4`
- `home-purchase-test` -> `1` impression, average position `8.0`

Interpretation:
- New Hampshire arsenic is still the main organic wedge
- New Jersey PWTA has become a real second wedge
- metallic taste is visible but not competitive yet
- Oregon still matters, but its support cluster has not been deployed live

#### Device Pattern

- desktop: `352` impressions, average position `39.9`
- mobile: `142` impressions, average position `50.5`
- tablet: `1` impression, average position `9.0`

Interpretation:
- the mobile weakness noted on `2026-03-23` still exists
- the relative gap is still large enough to matter, but it is still secondary to overall rank entry

#### Search Appearance / Opportunity Read

- search appearance rows are still empty
- low-hanging-fruit queries are effectively absent
- striking-distance coverage is effectively absent except for one low-volume long-tail query on `home-purchase-test`
- meaningful cannibalization was not detected

Interpretation:
- the site still does not have enough page-1/page-2 footholds to make CTR optimization the main job
- this remains a ranking-entry problem, not a snippet-optimization problem

### Inspection Findings

#### Live Output Check

Live production checks on `2026-04-01` show:
- `https://waterverdict.com/robots.txt` points only to `https://waterverdict.com/sitemap.xml`
- `https://waterverdict.com/sitemap.xml` contains `https` URLs only
- home page canonical and `og:url` are both `https`

Interpretation:
- the protocol-generation bug is not visibly present in current live output anymore

#### Canonical Residue Check

Search Console URL inspection still shows duplicate `http` URLs indexed separately:
- `http://waterverdict.com/well-water/metallic-taste`
- `http://waterverdict.com/well-water/new-jersey-pwta-private-well-testing`
- `http://waterverdict.com/well-water/oregon-private-well-testing-recommendations`
- `http://waterverdict.com/well-water/hardness`

In these cases:
- `user canonical = https`
- `google canonical = http`

Current 28-day `http://waterverdict.com/...` page totals:
- total impressions: `65`
- share of total impressions: about `13.1%`

Interpretation:
- the current live sitemap is not generating `http` URLs now
- Search Console is still carrying older `http` duplicates that were discovered earlier
- protocol cleanup is deployed, but Google has not consolidated the duplicates yet

#### Support Page Deployment Check

The three support pages added after the `2026-03-23` review are not live in production:
- `new-hampshire-arsenic-testing-order` -> `404`
- `metallic-taste-plumbing-vs-source-water` -> `404`
- `oregon-private-well-homebuyer-testing` -> `404`

Search Console inspection status for all three:
- `URL is unknown to Google`

Current Search Console impressions for those three URLs:
- `0`
- `0`
- `0`

Interpretation:
- zero visibility on these three support pages is not an indexing-quality problem yet
- it is a deployment gap

#### Sitemap Status

Search Console sitemap status on `2026-04-01`:
- sitemap path: `https://waterverdict.com/sitemap.xml`
- last submitted: `2026-03-23`
- last downloaded: `2026-04-01`
- warnings: `0`
- errors: `0`
- submitted URLs: `120`
- indexed URLs reported in sitemap summary: `0`

Interpretation:
- the sitemap summary is still not a trustworthy read of live URL indexing
- URL inspection results are more useful than the sitemap indexed count for this project right now

### Changes Shipped

Observed live state since the prior review:
- the `https` robots/sitemap output fix appears live
- the three new support pages do not appear live
- production content is therefore behind the current repo state

### Insights

#### Primary Read

- the site still has real topic fit in:
- New Hampshire arsenic
- New Jersey PWTA
- Oregon private well testing
- metallic taste
- this is not a topic-selection failure

#### Main Constraint

The main bottlenecks are now:
- persistent indexed `http` duplicates
- missing deployment of the new support pages
- lack of page-1/page-2 footholds in the winner clusters

#### What Changed Since 2026-03-23

- impressions increased materially
- the New Jersey cluster is more visible than before
- the protocol fix appears live at the output layer
- but the support pages intended to reinforce winner clusters are still not on production

#### What This Means

- the site is still moving forward
- but the repo improvements and the live site are not fully aligned
- until the support pages are actually deployed, the last round of cluster reinforcement cannot affect Search Console

### Next Check

Immediate priorities:
1. deploy the three support pages to production
2. verify those three URLs return `200`
3. verify those three URLs appear in the live sitemap
4. request indexing for those three `https` URLs only after they are live
5. re-inspect the known `http` duplicates after another crawl cycle

Questions for the next review:
- did `http` duplicate impressions fall below the current `65`?
- did `new-jersey-pwta-private-well-testing` continue to expand?
- did `new-hampshire-arsenic-well-water` improve from the current `54.1` average position?
- did any of the three support pages start receiving impressions after deployment?

Target follow-up window:
- `2026-04-08` to `2026-04-10`

## Entry: 2026-04-04

### Scope

- Property reviewed: `sc-domain:waterverdict.com`
- Work reviewed on `2026-04-04`: repo-side search-surface narrowing and indexing-policy changes
- Search Console context carried forward from the `2026-04-01` review:
- strongest visible clusters were still:
- `new-hampshire-arsenic-well-water`
- `new-jersey-pwta-private-well-testing`
- `oregon-private-well-testing-recommendations`
- `metallic-taste`
- `home-purchase-test`

### Data

#### Search Performance

- no new Search Console export was pulled on `2026-04-04`
- this entry records a strategy and implementation change, not a new traffic read
- most recent tracked live window remains:
- `2026-03-02` to `2026-03-29`
- impressions: `495`
- clicks: `0`
- average position: `42.9`

Interpretation:
- the site still had query discovery but weak ranking depth
- there was not enough evidence to justify broadening the public search surface further

#### Query Pattern

- no fresh query table was pulled on `2026-04-04`
- working assumption carried forward from `2026-04-01`:
- New Hampshire arsenic is still the clearest wedge
- New Jersey PWTA is the second real wedge
- Oregon private well testing still matters
- metallic taste remains visible but weak
- home-purchase intent is strategically important even though current volume is still small

Interpretation:
- the repo should now bias the public surface toward state-specific testing, transaction timing, and testing-order intent
- broad symptom, contaminant, and compare expansion should not remain the default growth posture

#### Page Pattern

Current repo-side page inventory reviewed on `2026-04-04`:
- total pSEO pages in `data/pseo/pages.csv`: `107`
- role split after narrowing:
- `CORE`: `21`
- `SUPPORT`: `23`
- `HOLD`: `50`
- `CONVERSION`: `13`

Interpretation:
- the site had become too broad for its current proof level
- the new role split intentionally reduces what acts like a primary search asset

#### Inspection / Indexing Policy Read

Repo-side indexing policy after the `2026-04-04` change:
- family hubs still indexable:
- `regional`
- `authority`
- `triggers`
- family hubs now public but `noindex,follow`:
- `contaminants`
- `symptoms`
- `compares`
- compare pages now default to public but `noindex,follow`
- held-back pages are excluded from `sitemap.xml`

Interpretation:
- this is a controlled narrowing, not a content deletion
- public navigation and internal linking still exist
- search focus is now much tighter than the full public inventory

### Changes Shipped

#### Search Surface Narrowing

Goal:
- stop treating the entire pSEO inventory as equal-weight acquisition surface
- align the public search surface with the clusters already showing early traction

Files changed:
- `src/main/java/com/example/wellwater/pseo/PseoSearchRole.java`
- `src/main/java/com/example/wellwater/pseo/PseoSearchStrategy.java`
- `src/main/java/com/example/wellwater/pseo/PseoPage.java`
- `src/main/java/com/example/wellwater/pseo/PseoCatalogService.java`
- `src/main/java/com/example/wellwater/pseo/PseoExperienceService.java`
- `src/main/java/com/example/wellwater/web/page/SeoMetadata.java`
- `src/main/java/com/example/wellwater/web/page/SeoMetadataService.java`
- `src/main/java/com/example/wellwater/web/page/PageController.java`

What changed:
- every pSEO page now resolves to one of four search roles:
- `CORE`
- `SUPPORT`
- `HOLD`
- `CONVERSION`
- role now controls whether the page is treated as indexable or held back
- sitemap generation now includes only indexable families and pages

#### Public Positioning Rewrite

Goal:
- make the home page and hub layer communicate a narrower product wedge

Files changed:
- `src/main/jte/pages/home.jte`
- `src/main/jte/pages/pseo/list.jte`
- `src/main/jte/pages/pseo/detail.jte`
- `src/main/jte/pages/trust/list.jte`
- `src/main/jte/pages/trust/view.jte`

What changed:
- home page now leads with:
- home sale and transaction testing
- state-specific testing paths
- report interpretation
- testing-order support
- broad compare behavior is now framed as internal conversion support instead of equal-weight acquisition intent
- noindexed detail and family pages now visibly explain that they remain public but are not primary search assets

#### Strategy Documentation

Goal:
- make the narrowing decision explicit so the site does not drift broader again by accident

Files added:
- `15_Search_Surface_Narrowing_2026-04-04.md`
- `16_Search_Asset_Classification_2026-04-04.md`

What changed:
- the repo now contains a written rationale for narrowing
- the repo now contains a slug-by-slug search-asset classification snapshot

### Verification

Commands run on `2026-04-04`:

```powershell
.\gradlew.bat test
```

Additional focused test run executed during implementation:

```powershell
.\gradlew.bat test --tests com.example.wellwater.pseo.PseoExperienceServiceTest --tests com.example.wellwater.web.page.SeoMetadataServiceTest --tests com.example.wellwater.web.page.PageControllerTest --tests com.example.wellwater.web.RenderingSmokeTest
```

Result:
- passed on `2026-04-04`

### Insights

#### Primary Read

- the site is still best understood as a narrow decision asset, not a broad well-water content site
- the right reaction to weak early rankings was not to add more breadth
- the right reaction was to narrow what the site asks Google to understand first

#### What This Change Tries To Fix

- weak topical focus caused by too many public families competing at once
- a long monetization path where compare content was too visible too early
- a mismatch between actual traction clusters and the visible public information architecture

#### What Did Not Change

- the underlying product thesis is still:
- private well
- problem-first
- verification before treatment shopping
- noindex personalized outputs
- the protocol/canonical cleanup from the earlier review still matters
- support pages still need to be deployed to production if they are not live yet

#### Current Strategic Read

- the strongest public wedge is now explicitly:
- state-specific testing paths
- home-sale and transaction timing
- testing-order and sampling discipline
- a few interpretation pages that reinforce those decisions

### Next Check

After the next deploy, answer these questions:
1. did the live sitemap shrink to the new indexable surface only?
2. do noindexed family hubs and compare pages emit the expected robots directive on production?
3. did the previously missing support pages finally return `200` on production?
4. did Search Console start reflecting fewer broad/weak surfaces and stronger testing-oriented clustering?
5. did core pages like `new-jersey-pwta-private-well-testing`, `private-well-home-sale-testing-by-state`, `home-purchase-test`, `new-hampshire-arsenic-well-water`, and `oregon-private-well-testing-recommendations` gain broader query coverage?

Target follow-up window:
- first deployment verification: `2026-04-04` to `2026-04-06`
- Search Console follow-up window: `2026-04-11` to `2026-04-15`

## Implementation Note: 2026-04-06

### Changes shipped

- tightened the homepage hierarchy again so compare exploration is no longer shown as an equal-weight deeper-research card
- added tracked internal redirect links for:
- homepage primary and secondary CTAs
- homepage start-path and signal-path cards
- detail-page tool transitions
- detail-page primary action clicks
- detail-page compare transitions
- detail-page related support-link clicks
- changed public page-view logging so `tier` now records the public search role (`core`, `support`, `hold`, `conversion`, `family-hub`, `home`, `trust`) and `branch` records the visible surface or family
- reduced broad-family leakage in related reads by preferring stronger search roles and pushing compare families later in the related-family order

### Contradiction resolved

- the repo already had a narrow search policy, but measurement still described pages mostly by legacy family or tier conventions
- after this change, the same role system that controls indexability is also visible in click and page-view analytics
- the homepage also now matches the intended wedge more closely by routing deeper research toward state, interpretation, triggers, and suspicious-result follow-up before treatment comparison

### Measure next

- which `home_primary_cta`, `home_secondary_cta`, `home_start_path`, and `home_signal_path` targets get the most clicks
- whether `detail_tool_cta` beats `detail_compare_cta` on winner pages
- whether `detail_support_link` traffic still reaches `HOLD` pages in a meaningful amount
- whether winner pages send visitors into verification paths before compare paths

## Entry: 2026-04-12

### Scope

- Property reviewed: `sc-domain:waterverdict.com`
- Comparison window reviewed:
- current: `2026-03-13` to `2026-04-09`
- previous: `2026-02-13` to `2026-03-12`
- Review context:
- this review was performed after the `2026-04-04` narrowing and the `2026-04-06` tracking alignment changes
- live production verification and GitHub Actions deploy status were checked alongside Search Console

### Data

#### Search Performance

- Current 28-day window:
- clicks: `0`
- impressions: `790`
- average position: `40.1`
- Previous 28-day window:
- clicks: `0`
- impressions: `39`
- average position: `22.4`
- Delta vs previous window:
- impressions: `+751` (`+1925.6%`)
- clicks: unchanged at `0`
- average position: worsened by `17.7`
- Delta vs prior tracked window on `2026-04-01`:
- impressions: `495` -> `790`
- average position: `42.9` -> `40.1`

Interpretation:
- Google is testing the site far more broadly than before
- this is still an impressions-first phase, not a clicks phase
- ranking breadth expanded faster than ranking quality

#### Daily Pattern

- the largest single-day spike was `2026-04-05`
- that day recorded `105` impressions and average position `5.84`
- the spike was spread across many pages rather than one durable winner

Interpretation:
- this looked like a broad test burst, not a stable breakthrough
- the site was sampled across multiple surfaces, but the ranking did not hold at that level afterward

#### Query Pattern

Top query clusters in the current window:
- `arsenic in well water new hampshire` -> `122` impressions, position `54.9`
- `arsenic water test new hampshire` -> `113` impressions, position `57.8`
- New Jersey PWTA variants combined on the main page -> about `124` impressions across many query forms
- metallic-taste variants on the main page -> about `83` impressions, mostly positions in the `60s` to `80s`
- `judith is buying a home with a well...` -> `3` impressions, position `8.0`

Interpretation:
- the market signal is still strongest around:
- New Hampshire arsenic
- New Jersey PWTA and sale-path intent
- metallic taste / corrosion diagnosis
- this is not a "topic failure" read
- it is a "Google has found the wedge, but the pages still rank too low to win clicks" read

#### Page Pattern

Top pages in the current window:
- `https://waterverdict.com/well-water/new-hampshire-arsenic-well-water` -> `308` impressions, position `45.2`
- `https://waterverdict.com/well-water/metallic-taste` -> `143` impressions, position `48.4`
- `https://waterverdict.com/well-water/home-purchase-test` -> `69` impressions, position `4.5`
- `https://waterverdict.com/` -> `67` impressions, position `2.6`
- `http://waterverdict.com/well-water/new-jersey-pwta-private-well-testing` -> `55` impressions, position `13.8`
- `https://waterverdict.com/well-water/cloudy-water` -> `40` impressions, position `24.1`
- `http://waterverdict.com/well-water/oregon-private-well-testing-recommendations` -> `30` impressions, position `7.6`

Interpretation:
- core traction is still concentrated in a small number of decision pages
- broad informational pages still appear in testing bursts, but they are not yet the main acquisition engine
- `home-purchase-test` remains the strongest page-quality signal even at low impression volume

#### Device Pattern

- desktop: `581` impressions, average position `36.2`
- mobile: `207` impressions, average position `51.1`
- tablet: `2` impressions, average position `44.5`

Interpretation:
- mobile remains materially weaker than desktop
- mobile underperformance still matters, but it is not the main reason clicks remain at zero
- the main issue is still overall ranking depth

#### Protocol / Inspection Findings

Live checks on `2026-04-12`:
- `http://waterverdict.com/well-water/new-jersey-pwta-private-well-testing` -> `301` to `https://...`
- `http://waterverdict.com/well-water/metallic-taste` -> `301` to `https://...`
- live edge behavior is now consistent with HTTPS

Search Console still shows residual protocol split:
- HTTP pages still earned `144` impressions in the current window
- HTTP share of total impressions: about `18.2%`
- this is worse than the roughly `13.1%` share recorded on `2026-04-01`
- inspected HTTP URLs were still shown as indexed with `google canonical = http` and `user canonical = https`
- those HTTP crawls were old, mostly around `2026-03-09` to `2026-03-14`

Interpretation:
- the live redirect is correct
- Search Console protocol consolidation is still lagging
- the technical issue is no longer "missing redirect"
- it is "old HTTP selections are still consuming impressions in Google"

#### Support Page / Sitemap Findings

Inspected support URLs:
- `new-hampshire-arsenic-testing-order` -> Search Console `URL is unknown to Google`, live `404`
- `metallic-taste-plumbing-vs-source-water` -> Search Console `URL is unknown to Google`, live `404`
- `oregon-private-well-homebuyer-testing` -> Search Console `URL is unknown to Google`, live `404`
- `private-well-home-sale-testing-by-state` -> live `200`, present in sitemap, but still `URL is unknown to Google`

Sitemap status in Search Console:
- sitemap: `https://waterverdict.com/sitemap.xml`
- last submitted: `2026-04-04`
- last downloaded: `2026-04-04`
- warnings: `0`
- errors: `0`
- submitted pages: `120`
- indexed pages summary: `0`

Interpretation:
- the support-page problem is not an indexing lag only
- three intended support pages are not actually present on production
- the sitemap summary is still too stale or too coarse to use as the source of truth

### Changes Shipped

- no new repo-side search changes were shipped during this review
- operational finding recorded during review:
- production deploys after `2026-04-01` did not complete successfully

#### Deployment Finding

GitHub Actions deploy status:
- `2026-04-01` run `23850750651` -> failed
- `2026-04-04` runs `23979081537`, `23980024462`, `23980570955` -> failed
- `2026-04-06` run `24026794704` -> failed

Failure mode:
- the deploy job passed tests
- the job failed during `Sync content data to OCI`
- remote command `rm -rf ~/deploy/waterverdict/data` hit `Permission denied` on files inside the mounted data directory

Interpretation:
- repository state moved ahead
- production content data did not
- the missing support pages on production are explained by deploy failure first, not by Search Console first

### Verification

Commands run on `2026-04-12`:

```powershell
gh run list --workflow deploy.yml --limit 10
gh run view 24026794704 --log-failed
gh run view 23850750651 --log-failed
./gradlew --no-daemon test --tests com.example.wellwater.decision.DecisionEngineServiceTest.nitrateWithInfantRoutesToImmediateRed
```

Live checks performed:
- `curl` checks against `sitemap.xml`, target support URLs, and known HTTP duplicates

Result:
- the targeted local test passed
- recent deploy workflow runs were confirmed failed
- live support pages were confirmed missing where Search Console also reported "unknown"

### Insights

#### Primary Read

- impressions are up sharply, so Google interest is increasing
- clicks remain at zero because most meaningful query clusters still sit too low
- the growth story is still "discovery expanding", not "conversion starting"

#### Most Important Constraint

- the biggest blocker is no longer content selection alone
- the biggest blocker is deployment drift between repository and production
- as long as production stays behind, Search Console interpretation of support-page strategy will stay partially invalid

#### What The Data Does And Does Not Mean

- it does mean:
- New Hampshire arsenic is the strongest current acquisition wedge
- New Jersey PWTA is gaining breadth
- metallic taste has discovery but weak ranking quality
- it does not mean:
- the topic strategy failed
- the site needs a broad topic rewrite
- support content strategy failed on its own

#### Current Priority Order

1. fix the OCI deploy/data-permission issue so production can actually receive current `data/`
2. confirm the three missing support pages return `200` on production
3. let Google recrawl the HTTPS versions and monitor whether HTTP share falls
4. re-check whether `private-well-home-sale-testing-by-state` moves from "unknown" to indexed
5. only after deploy parity is restored, judge whether the narrowed search surface is working as intended

### Next Check

Target follow-up window:
- deployment verification: immediately after the next successful deploy
- Search Console recheck: `2026-04-19` to `2026-04-23`

Exact questions to answer next time:
1. did production finally ship the current `data/pseo/pages.csv` state?
2. do `new-hampshire-arsenic-testing-order`, `metallic-taste-plumbing-vs-source-water`, and `oregon-private-well-homebuyer-testing` return `200` on production?
3. did HTTP impression share fall below the `18.2%` seen in this review?
4. did `new-hampshire-arsenic-well-water` or `new-jersey-pwta-private-well-testing` move materially closer to page 2?
5. did `private-well-home-sale-testing-by-state` move from "unknown" into indexed status?

## Implementation Note: 2026-04-12

### Deploy remediation shipped

- file changed:
- `.github/workflows/deploy.yml`

What changed:
- deploy sync now ships only `data/pseo` and `data/registry`
- runtime directories such as `data/analytics`, `data/leads`, and `data/results` are no longer replaced during deploy
- the remote sync step now stops the running container, normalizes ownership of the bind-mounted `data/` directory through Docker, and then refreshes only the seed-data directories

Why this matters:
- the earlier deploy strategy tried to replace the full mounted `data/` directory
- that both failed on root-owned runtime files and carried unnecessary risk of wiping live operational data
- the new deploy path matches the actual app design: static seed data should update, runtime data should persist

### Verification after fix

- commit pushed: `1c23d8a`
- GitHub Actions deploy run: `24303455892`
- deploy result: `success`

Live production verification after the successful deploy:
- `https://waterverdict.com/well-water/new-hampshire-arsenic-testing-order` -> `200`
- `https://waterverdict.com/well-water/metallic-taste-plumbing-vs-source-water` -> `200`
- `https://waterverdict.com/well-water/oregon-private-well-homebuyer-testing` -> `200`
- `https://waterverdict.com/well-water/private-well-home-sale-testing-by-state` -> `200`
- `https://waterverdict.com/sitemap.xml` now includes all four target URLs

Interpretation:
- the repository and production are back in sync for the tracked support pages
- future Search Console analysis can now evaluate the narrowed support strategy on real live URLs instead of stale deploy state

## Entry: 2026-04-22

### Scope

- Property reviewed: `sc-domain:waterverdict.com`
- Comparison window reviewed:
- current: `2026-03-23` to `2026-04-19`
- previous: `2026-02-23` to `2026-03-22`
- Review context:
- this review was performed against the latest final 28-day Search Console window available on `2026-04-22`
- the tracking documents from `2026-04-12` were used as the decision frame:
- keep the narrowed wedge in place
- do not re-expand until clicks start and protocol drift is controlled
- verify whether `contaminants` and `symptoms` family hubs are truly `noindex,follow` on production

### Data

#### Search Performance

- Current 28-day window:
- clicks: `0`
- impressions: `809`
- average position: `42.6`
- Previous 28-day window:
- clicks: `0`
- impressions: `326`
- average position: `39.2`
- Delta vs previous window:
- impressions: `+483` (`+148.2%`)
- clicks: unchanged at `0`
- average position: worsened by `3.4`

Interpretation:
- discovery is still expanding
- ranking quality is not holding with that growth yet
- this remains an impressions-phase site, not a clicks-phase site

#### Page Pattern

Most visible pages in the current window:
- `https://waterverdict.com/well-water/new-hampshire-arsenic-well-water` -> `280` impressions, position `53.6`
- `https://waterverdict.com/well-water/new-jersey-pwta-private-well-testing` -> `121` impressions, position `43.9`
- `http://waterverdict.com/well-water/new-jersey-pwta-private-well-testing` -> `61` impressions, position `20.6`
- `https://waterverdict.com/well-water/oregon-private-well-testing-recommendations` -> `123` impressions, position `29.6`
- `http://waterverdict.com/well-water/oregon-private-well-testing-recommendations` -> `26` impressions, position `7.8`
- `https://waterverdict.com/well-water/metallic-taste` -> `92` impressions, position `46.2`
- `https://waterverdict.com/well-water/cloudy-water` -> `81` impressions, position `42.9`
- `https://waterverdict.com/well-water/home-purchase-test` -> `41` impressions, position `5.2`

Additional surface that should be watched carefully:
- `https://waterverdict.com/well-water/family/contaminants` -> `20` impressions, position `3.7`
- `https://waterverdict.com/well-water/family/symptoms` -> `9` impressions, position `3.9`
- `http://waterverdict.com/well-water/family/symptoms` -> `19` impressions, position `2.0`

Interpretation:
- the New Hampshire, New Jersey, and Oregon wedge still owns most meaningful visibility
- but broad symptom and family-hub surface is still leaking into Search
- that means the narrowed strategy cannot yet be judged as fully live on production

#### Device Pattern

- desktop: `589` impressions, average position `38.8`
- mobile: `215` impressions, average position `52.0`
- tablet: `5` impressions, average position `77.4`

Interpretation:
- mobile still trails desktop materially
- but the bigger blocker is still weak rank depth and production parity, not CTR tuning

#### Protocol Findings

Current protocol split from page-level Search Console data:
- HTTP pages still earned `125` impressions in the current window
- HTTP share of total impressions: about `15.5%`
- previous comparison window HTTP share: about `12.0%` (`39` of `326`)
- compared with the `18.2%` share logged on `2026-04-12`, this is better than that read but still far above the `<5%` consolidation gate

Live checks on `2026-04-22`:
- `http://waterverdict.com/well-water/new-jersey-pwta-private-well-testing` -> `301` to `https://...`
- `http://waterverdict.com/well-water/oregon-private-well-testing-recommendations` -> `301` to `https://...`

Interpretation:
- edge redirects are working
- protocol cleanup is still incomplete inside Google selection, so re-expansion remains blocked

#### Family-Hub / Production Parity Findings

Live HTML checks on `2026-04-22` showed:
- `https://waterverdict.com/well-water/family/symptoms` -> `meta robots = index,follow`
- `https://waterverdict.com/well-water/family/contaminants` -> `meta robots = index,follow`
- both responses returned `cf-cache-status: DYNAMIC`

Repository-side checks on the same date showed:
- `src/main/java/com/example/wellwater/pseo/PseoSearchStrategy.java` currently marks only `regional`, `authority`, and `triggers` family hubs as indexable
- `src/test/java/com/example/wellwater/web/page/SeoMetadataServiceTest.java` and `src/test/java/com/example/wellwater/web/page/PageControllerTest.java` both expect `contaminants` family hubs to emit `noindex,follow`

Interpretation:
- this is not a stale Cloudflare HTML cache read
- production app code is behind the repository search-strategy state
- Search Console family-hub impressions are therefore partly a deployment-parity problem, not just a Google-lag problem

#### Support Page Findings

Live checks on `2026-04-22`:
- `https://waterverdict.com/well-water/new-hampshire-arsenic-testing-order` -> present in sitemap
- `https://waterverdict.com/well-water/metallic-taste-plumbing-vs-source-water` -> present in sitemap
- `https://waterverdict.com/well-water/oregon-private-well-homebuyer-testing` -> present in sitemap
- `https://waterverdict.com/well-water/private-well-home-sale-testing-by-state` -> present in sitemap

Search Console page reads for `2026-04-13` to `2026-04-19`:
- `new-hampshire-arsenic-testing-order` -> `4` impressions, position `57.5`
- `metallic-taste-plumbing-vs-source-water` -> `0`
- `oregon-private-well-homebuyer-testing` -> `0`
- `private-well-home-sale-testing-by-state` -> `0`

Interpretation:
- support-page deploy parity for data is restored
- but only one of the four tracked support pages has started to register impressions
- the support layer still needs crawl time after the `2026-04-12` deploy recovery

### Changes Shipped

- no new production search changes were shipped during this review

#### Deploy-Gate Fix Prepared Locally

Files changed:
- `src/test/java/com/example/wellwater/pseo/PseoCatalogServiceTest.java`

What changed:
- updated the outdated sitemap expectation that still assumed `/well-water/family/contaminants` should remain indexable
- added a positive assertion that `/well-water/family/regional` stays in the sitemap under the narrowed strategy

Why this matters:
- the latest strategy commit `7f73f32` (`Narrow search focus and add re-expansion triggers`) failed deploy run `24305628425`
- failure cause: `PseoCatalogServiceTest > loadsPagesAndCountsFamilies()` still expected the old family-hub sitemap policy
- until that stale test is corrected, the production app cannot ship the family-hub `noindex` behavior described in the tracking documents

#### Deployment Finding

GitHub Actions deploy status checked on `2026-04-22`:
- `24305628425` for commit `7f73f32` -> failed
- latest successful deploy runs remain:
- `24303500116`
- `24303455892`

Failure mode in `24305628425`:
- deploy stopped in the test phase
- the failing test was `PseoCatalogServiceTest > loadsPagesAndCountsFamilies()`
- no Docker image build or OCI deploy steps ran after that failure

Interpretation:
- production currently reflects the last successful pre-fix app code, not the narrowed family-hub strategy commit
- that explains why support-page data can be live while family hubs still emit `index,follow`

### Verification

Commands run on `2026-04-22`:

```powershell
gh run list --workflow deploy.yml --limit 8
gh run view 24305628425 --log-failed
./gradlew --no-daemon test
curl.exe -I -s http://waterverdict.com/well-water/new-jersey-pwta-private-well-testing
curl.exe -I -s http://waterverdict.com/well-water/oregon-private-well-testing-recommendations
curl.exe -I -s https://waterverdict.com/well-water/family/symptoms
curl.exe -I -s https://waterverdict.com/well-water/family/contaminants
```

Result:
- local full test suite passed on `2026-04-22` after the stale catalog test was corrected
- live HTTP redirects were confirmed
- live family-hub HTML was confirmed dynamic and still indexable

### Insights

#### Primary Read

- the narrowed search wedge still looks directionally right
- but production is not fully serving the narrowed strategy yet
- that means Search Console is still partly measuring an old app state

#### Re-Expansion Gate Read

The `2026-04-12` re-expansion triggers are still not met:
- site-wide clicks are still `0`
- HTTP share is still `15.5%`, not below `5%`
- family hubs that should be held back are still indexable on production

Interpretation:
- re-expansion remains off the table
- do not promote new `SUPPORT` pages to `CORE`
- do not reopen any held family

#### Most Important Constraint

- the biggest blocker is now deployment parity for app code, not lack of content
- until the successful post-`7f73f32` app build is live, Search Console cannot fairly evaluate the `noindex` family-hub strategy

#### Current Priority Order

1. ship a successful deploy for the narrowed strategy app code
2. re-check live `meta robots` on `family/symptoms` and `family/contaminants`
3. only after production shows `noindex,follow`, watch whether family-hub impressions decay
4. keep the current wedge closed and avoid search-surface expansion
5. re-check support-page impressions after another `7` to `14` days of post-fix crawl time

### Next Check

Target follow-up window:
- first check: immediately after the next successful deploy of the narrowed strategy code
- second check: `2026-04-29` to `2026-05-03`

Exact questions to answer next time:
1. do `family/symptoms` and `family/contaminants` now emit `noindex,follow` on production?
2. does HTTP impression share fall below the current `15.5%`?
3. do the New Hampshire, New Jersey, and Oregon clusters move closer to page 2 without broad family-hub leakage?
4. do `metallic-taste-plumbing-vs-source-water`, `oregon-private-well-homebuyer-testing`, and `private-well-home-sale-testing-by-state` start receiving impressions?
5. does the site produce its first multi-page organic clicks, or does it remain in pure discovery mode?

## Implementation Note: 2026-04-22

### Deploy completion

- follow-up commit pushed: `ce3c77e`
- GitHub Actions deploy run: `24761957623`
- deploy result: `success`

Why this follow-up note exists:
- the main `2026-04-22` entry was recorded while production still reflected the last successful pre-fix app code
- this note records the point at which the narrowed family-hub strategy was actually shipped live

### Live production verification after successful deploy

Live checks on `2026-04-22` after run `24761957623` completed:
- `https://waterverdict.com/well-water/family/symptoms` -> `meta robots = noindex,follow`
- `https://waterverdict.com/well-water/family/contaminants` -> `meta robots = noindex,follow`
- `https://waterverdict.com/well-water/family/regional` -> `meta robots = index,follow`
- `https://waterverdict.com/well-water/new-hampshire-arsenic-well-water` -> `meta robots = index,follow`
- checked family-hub responses still returned `cf-cache-status: DYNAMIC`

Interpretation:
- the robots directives now match the repository search-surface policy
- this confirms the production app is now serving the narrowed family-hub strategy, not the earlier pre-fix app state
- future Search Console reads can evaluate family-hub decay and wedge performance on a valid live baseline

### What changes in the operating read

- the immediate deploy-parity blocker for family hubs is resolved
- this does not mean the site is healthy yet
- it means the next Search Console measurement window will be the first one that can fairly judge the narrowed strategy end to end

### What to watch from here

Use `2026-04-22` as the operational baseline for the live family-hub `noindex` change.

Priorities for the next review remain:
1. confirm whether family-hub impressions begin to decay after recrawl
2. confirm whether HTTP impression share keeps falling from `15.5%`
3. confirm whether the New Hampshire, New Jersey, and Oregon wedge gains stronger page-2 footholds
4. confirm whether the support pages that only just went live begin to attract impressions
5. confirm whether the site remains at `0` clicks or starts producing first-click evidence across more than one page

### Verification

Commands run after deploy success on `2026-04-22`:

```powershell
gh run watch 24761957623 --exit-status
curl.exe -I -s https://waterverdict.com/well-water/family/symptoms
curl.exe -I -s https://waterverdict.com/well-water/family/contaminants
curl.exe -s https://waterverdict.com/well-water/family/symptoms
curl.exe -s https://waterverdict.com/well-water/family/contaminants
```

Result:
- deploy completed successfully
- family-hub robots directives were verified live on production immediately after deploy
- no new Search Console performance read was taken in this follow-up note; this note is strictly deployment-state verification

## Implementation Note: 2026-04-22

### Surface simplification and CTA clarity

Why this note exists:
- the live search-surface narrowing deploy was one half of the problem
- the other half was product comprehension on first view
- home and detail pages were still asking users to read too much before they could understand where the tool starts
- header height also made the first actionable section feel farther away than it should

### What changed

Homepage changes shipped locally:
- hero reframed around one direct promise: decide the next test before product shopping starts
- primary CTA kept tool-first and the four start paths were grouped into one obvious "pick the closest starting point" block
- longer browse sections were pushed later so the reading layer supports the tool instead of competing with it
- lead capture stayed below trust instead of appearing earlier in the decision path

Detail page changes shipped locally:
- first section now explains the page in two layers: tool first, public guide second
- added a short "quick start" block that says what the page means, what to do first, and when to use the tool
- removed the later duplicate tool-prompt block so the action path is clearer near the top

Layout changes shipped locally:
- reduced header padding, logo block size, nav spacing, and mobile header copy
- mobile header now consumes less vertical space before the main decision surface starts

### Why this matters

Interpretation:
- this is not a ranking fix by itself
- it is a conversion and comprehension fix for the search traffic the site already earns
- the working hypothesis is that a newer domain still stuck around positions `30-60` should prioritize clear orientation and obvious tool entry over additional above-the-fold reading density

### Verification

Commands run on `2026-04-22`:

```powershell
.\gradlew.bat --no-daemon test
"C:\Program Files\Google\Chrome\Application\chrome.exe" --headless=new --screenshot=...
```

Local verification performed:
- JTE templates compiled and tests passed
- local desktop and mobile screenshots were checked for `/` and `/well-water/nitrate`
- header height was reduced and first-view CTA visibility improved in both checks

## Implementation Note: 2026-04-22

### Indexable tool-surface added

Why this note exists:
- the narrowed wedge and CTA simplification solved only part of the product-surface problem
- Google could see the public guide and router layer, but the actual intake routes under `/tool/*` and saved results under `/result/saved/*` were still intentionally blocked from indexation
- that created a real risk that the site would still read like an information shell with a hidden tool behind it
- the target strategy is now explicit:
- narrow wedge
- wide public surface
- public surface should still read as a tool, not as a publisher-first guide layer

### What changed

Files changed:
- `src/main/java/com/example/wellwater/web/page/PageController.java`
- `src/main/java/com/example/wellwater/web/page/SeoMetadataService.java`
- `src/main/java/com/example/wellwater/web/page/PublicTrackingLinkService.java`
- `src/main/jte/layout/main.jte`
- `src/main/jte/pages/home.jte`
- `src/main/jte/pages/tool/landing.jte`
- `src/test/java/com/example/wellwater/web/RenderingSmokeTest.java`
- `src/test/java/com/example/wellwater/web/page/PageControllerTest.java`
- `src/test/java/com/example/wellwater/web/page/SeoMetadataServiceTest.java`

What changed:
- added a new public, indexable `/tool` landing page
- added `/tool` to the generated sitemap
- changed the main header "Open Tool" path to point to `/tool`, not directly to a noindexed intake route
- added a home-page bridge into the public tool surface so the site can explain the tool before the user enters a private flow
- the new `/tool` page now explains:
- the four valid starting inputs:
- lab result
- symptom
- recent change
- state or sale context
- the expected output shape:
- decision call
- recommended tests
- treatment hold logic
- public reasoning and support links
- `/tool/*` intake routes and `/result/saved/*` personalized outputs remain intentionally non-indexed
- structured data on `/tool` now describes a private-well decision tool surface in crawlable public HTML instead of leaving all tool identity buried behind blocked routes

Interpretation:
- this is the first repo-side change that turns the public surface itself into an indexable tool explanation layer
- Google still cannot fully evaluate the private intake and result engine directly
- but Google can now crawl a page whose main job is to say:
- this site accepts problem inputs
- this site routes users into a decision engine
- the guide layer exists after the route, not before it

### Why this matters

Interpretation:
- this change better matches the intended strategy than a guide-first homepage plus hidden tool CTA
- it should reduce the risk that Google classifies the broad public surface as a generic well-water publisher shell
- it does not guarantee rich-result treatment or automatic "tool" classification
- it does give Google a crawlable public page that can support a stronger product-identity read than the prior structure

### Verification

Commands run on `2026-04-22`:

```powershell
.\gradlew.bat --no-daemon test
```

Result:
- local full test suite passed after the `/tool` landing, sitemap inclusion, and metadata changes
- the repo now contains an indexable tool landing page that can be used as the next URL-inspection target after deploy

### What to watch next

Use the next live review to answer:
1. does `https://waterverdict.com/tool` return `200` and `meta robots = index,follow` on production?
2. does `https://waterverdict.com/sitemap.xml` include `/tool` live?
3. does Search Console start showing impressions for `/tool` after recrawl?
4. do queries around `tool`, `test`, `decision`, `lab result`, `home sale`, or `symptom` begin attaching to `/tool` or to the newly tool-routed home surface?
5. does the site identity look less article-led in Search Console page distribution over the next `2` to `4` weeks?

## Implementation Note: 2026-04-22

### Live verification after tool-surface deploy

Why this note exists:
- the earlier `2026-04-22` implementation note recorded the repo-side product-surface change
- this follow-up note records that the new public tool surface is now actually live on production

### Deploy status

- commit pushed for the tool-surface change: `dc868cf`
- GitHub Actions deploy run: `24765132058`
- deploy result: `success`

Interpretation:
- the new `/tool` public surface is no longer only a repository state
- Google can now actually crawl the live page, not just the repo-local version

### Live production verification

Live checks run on `2026-04-22` after deploy success:
- `https://waterverdict.com/tool` -> `200 OK`
- `/tool` live HTML now emits:
- `meta robots = index,follow`
- canonical `https://waterverdict.com/tool`
- JSON-LD describing a public private-well decision tool surface
- `https://waterverdict.com/sitemap.xml` now includes `https://waterverdict.com/tool`
- live `/tool` page copy confirms:
- public tool surface
- four valid starting inputs
- private wedge boundary between indexable public routing and non-indexed personalized flows

Interpretation:
- the key structural change for this cycle is now fully live
- today is therefore a valid baseline date for measuring whether Google starts attaching impressions and identity signals to `/tool`

### Verification

Commands run on `2026-04-22`:

```powershell
gh run list --limit 5
curl.exe -I -s https://waterverdict.com/tool
curl.exe -s https://waterverdict.com/sitemap.xml
curl.exe -s https://waterverdict.com/tool
```

Result:
- deploy success was confirmed in GitHub Actions
- live `/tool` returned `200`
- live `/tool` was confirmed indexable
- live sitemap inclusion of `/tool` was confirmed

### Operating read

- no additional product or SEO implementation work is required on `2026-04-22`
- the project should now switch from build mode to observation mode
- next meaningful work should be based on recrawl and Search Console response, not another same-day surface rewrite

## Entry: 2026-07-10

### Scope

- Property reviewed: `sc-domain:waterverdict.com`
- Decision scope: determine whether to keep optimizing the publisher surface or pivot to an automated product-distribution model
- Operating constraint: no early outbound, no manual service, and no recurring work outside development

### Data

#### Search Performance

- `180`-day total: `1` click, `1,568` impressions, average position `40.3`
- latest `28` days: `0` clicks, `13` impressions, average position `22.6`
- successive impression blocks reviewed: `102 -> 850 -> 417 -> 186 -> 13`
- latest block decline from peak block: about `98.5%`

#### Index And Surface Pattern

- sitemap surface: about `75` URLs
- submitted-page report reviewed: `74` URLs
- dependable submitted-and-indexed growth signal: `0` in the reviewed report
- interpretation: URL count created more eligible surface, but did not produce durable distribution or clicks

### Decision

- stop treating another observation period as the strategy
- keep the private-well decision engine and narrow test-selection wedge
- replace publisher-first acquisition with tool-first web and OpenAI plugin distribution
- freeze the existing pSEO inventory during the plugin test instead of expanding or immediately deleting it
- permit only automated, safety-gated physical test-kit affiliate routing

### Changes Shipped

- replaced the home surface with an immediate private-well test-plan input
- added a deterministic test-plan domain service that reuses the existing decision registries
- added one MCP tool, `recommend_private_well_test_plan`, plus its MCP App widget resource
- added official guidance and certified-lab routes to every successful output
- added allowlisted Tap Score redirects that fail closed and suppress commerce on high-risk paths
- retired new lead capture with HTTP `410 Gone`
- added aggregate-only pivot metrics and protected `/admin/pivot-metrics` summary
- added `/health/app`, OpenAI domain challenge handling, deployment verification, and a daily MCP health workflow
- removed hardcoded admin credentials from Compose and GitHub Actions
- updated privacy, commercial disclosure, and support surfaces to match the new product model

### Verification

- `123` automated tests passed with `0` failures and `0` skips
- `clean test bootJar` completed successfully
- the packaged JAR returned `200` for the tool-first home page and `ok` from `/health/app` under the production profile
- MCP `tools/list` returned only `recommend_private_well_test_plan`
- malformed MCP JSON returned a sanitized `400` response without Java stack frames
- desktop and mobile browser QA passed for the first-input surface and immediate annual-test result

### Operating Read

- this is a distribution-model pivot, not another page-level SEO experiment
- the site remains broad through engine inputs, result families, official state routes, and ChatGPT conversations
- no new SEO content, outbound, lead handling, or manual interpretation work is authorized during the 90-day plugin test
- the KPI clock starts only after the approved plugin is publicly published

### Next Check

- publication date: add after OpenAI plugin approval and publication
- Day 30: distribution sanity and failure-rate gate
- Day 60: utility and partner-intent gate
- Day 90: keep, iterate once, or stop according to `19_Automated_Distribution_Pivot_2026-07-10.md`

## Entry: 2026-07-10 - ChatGPT Distribution Experiment Hardening

### Research Read

- the product pivot remains `publisher-first -> automated private-well test-selection tool`
- the ChatGPT app is now classified as a capped distribution experiment, not the product identity or a proven replacement for search
- official discovery depends on direct mention, directory browsing, conversation context, metadata, linking state, and past usage; listing alone does not guarantee traffic
- public evidence supports the practical-guidance and complex-decision fit, but no dependable independent-app install or revenue benchmark was found
- broad ChatGPT reach must not be reported as Water Verdict reach

### Changes Shipped

- expanded discovery metadata across direct and indirect private-well testing intents
- mapped general private-well testing requests to `reason=other` without forcing optional clarification
- versioned the widget URI as `ui://widget/well-test-plan-v2.html`
- reordered the widget to verdict, first three actions, official paths, panel, avoid-now guidance, and explanation
- added host-aware external navigation, theme adaptation, tool invocation status copy, widget description, and submission CSP metadata
- added allowlisted aggregate tracking for official-guidance and certified-lab clicks
- added `APP_PIVOT_EXPERIMENT_START_DATE` so review and QA calls are excluded from the KPI window
- added ChatGPT-specific failure, external-action, and partner-intent ratios to the protected metric summary
- removed commerce from the ChatGPT tool schema and widget; web commerce remains independently configurable and any future app commerce requires policy confirmation plus review
- added a direct, indirect, and negative discovery evaluation set in `21_ChatGPT_App_Distribution_Research_2026-07-10.md`

### Decision Gates

- fewer than `100` ChatGPT completions by Day 90 means automatic distribution failed
- `250+` completions and at least `10%` external-action ratio keep the channel in low-touch operation
- the initial ChatGPT experiment validates automatic distribution and utility, not monetization
- utility without revenue permits maintenance-only operation, not expansion
- no new SEO pages, outbound, or paid acquisition may contaminate the experiment window

### Current Status

- implementation and local automated verification are complete
- the experiment has not started because the plugin is not publicly published
- publication date and `APP_PIVOT_EXPERIMENT_START_DATE` remain blank until approval and public release

### Verification

- `127` automated tests passed with `0` failures and `0` skips
- `clean test bootJar` completed successfully
- the production-profile JAR returned the v2 widget, action-first output, and tracked official/certified links
- the scanned MCP output schema and annual result contained no commerce field
- an allowlisted official-resource action returned `302` to the expected New Hampshire government URL and incremented only the aggregate action counter
- desktop and `390px` mobile widget QA passed; the only browser console error was the temporary QA server's missing favicon

## Entry: 2026-07-11 - NJ Transaction Distribution Experiment Implemented

### Decision

- the failed model is classified as general-guide publishing followed by passive Google acquisition
- ChatGPT distribution is not classified as failed because it was never published; it is no longer the active experiment
- the active product is `NJ Private-Well Transaction Preflight`
- distribution is split into a borrowed partner route and a bounded local-data search route
- the same engine and result surface serve both routes so channel quality can be compared without product drift

### Changes Shipped

- added `/nj-well-preflight` and privacy-safe `POST /nj-well-preflight/result`
- added 24 indexable municipality routers selected from the official 564-row NJDEP municipality snapshot
- added address-to-grid matching against the versioned 1,676-row NJDEP 2-mile grid snapshot
- added current PWTA sale, lease, certified-sampling, mercury-county, and uranium-county routing
- added inactive-by-default configuration for five laboratory and five home-inspector prospects
- added noindex co-branded partner routes and fixed allowlisted redirects
- added independent `direct`, `organic_local`, and `partner` aggregate metrics at `/admin/nj-distribution-metrics`
- excluded addresses, coordinates, sessions, contacts, and raw form answers from the NJ metric contract
- added the main NJ tool and exactly 24 pilot municipality routes to `sitemap.xml`
- retained non-pilot municipalities only as form fallbacks; public non-pilot page routes return `404`

### Verification

- `145` automated tests passed with `0` failures
- snapshot tests verified `1,676` grid rows, `564` municipality rows, and both reviewed SHA-256 checksums
- Census response parsing, out-of-state handling, grid matching, and geocoder fallback passed
- Ocean County mercury and Morris County uranium rule tests passed
- raw submitted addresses were absent from rendered results, result URLs, and aggregate metrics
- unknown partner routes returned `404`; certified-lab redirects returned only the fixed NJDEP destination
- browser QA passed on desktop and `390px` mobile for landing, municipality selection, result generation, and action links
- browser console reported `0` errors and `0` warnings

### Measurement Gates

- partner Day 30: at least 2 of 10 proposed partners publish a link
- partner Day 60: 100 landing views, 40 completions, and 15 booking or partner clicks
- local search Day 90: 80% indexed, 1,000 non-brand impressions, 10 clicks across at least 3 pages, 20 starts, and 8 completions
- no municipality expansion, generic guide publishing, or routine CTR work before the local-search gates pass
- if both routes fail, move the well-water project to maintenance mode rather than adding another distribution hypothesis

### External Steps Still Required

- verify the current certification or license status of each prospect before outreach
- obtain written permission before setting any partner row to `active=true`
- send the 10 one-time proposals and record the first live-link date
- deploy, verify the production sitemap, and submit the new sitemap in Search Console
- record the deployment and sitemap-submission dates; those dates start the 30/60/90-day clocks

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
