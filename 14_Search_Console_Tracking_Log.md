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
