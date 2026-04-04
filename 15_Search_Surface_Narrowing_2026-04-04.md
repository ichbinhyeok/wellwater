# Search Surface Narrowing

Date: `2026-04-04`

## Why this exists

The project direction is still valid:
- private well
- problem-first
- verification before shopping
- public indexable pages plus noindex personalized outputs

The problem is that the public search surface grew into too many families too early.

Recent Search Console reads in `14_Search_Console_Tracking_Log.md` show the site is not getting tested evenly.
The strongest early clusters are:
- New Hampshire arsenic
- New Jersey PWTA
- Oregon private well testing
- home purchase testing
- metallic taste / corrosion

That means the site should not behave like a broad well-water encyclopedia right now.
It should behave like a narrower decision site for:
- state-specific testing paths
- home sale and transaction timing
- testing order and sampling discipline
- a few result-interpretation pages that directly reinforce those decisions

## Product position

The core public wedge is now:

`Decide what to test next for a private well, especially during home purchase, state-specific testing, and suspicious result follow-up.`

This is intentionally narrower than:
- generic contaminant encyclopedia
- generic symptom encyclopedia
- generic treatment comparison site

## Public search policy

The repo now uses four search roles:

### Core

Primary acquisition pages that should stay indexable and appear in the sitemap.

Current core set:
- `new-jersey-pwta-private-well-testing`
- `private-well-home-sale-testing-by-state`
- `home-purchase-test`
- `home-sale-private-well-testing-checklist`
- `new-hampshire-arsenic-well-water`
- `new-hampshire-arsenic-testing-order`
- `arsenic`
- `oregon-private-well-testing-recommendations`
- `oregon-private-well-homebuyer-testing`
- `metallic-taste`
- `metallic-taste-plumbing-vs-source-water`
- `ph`
- `low-ph-copper-corrosion-testing-order`
- `how-to-read-a-well-water-lab-report`
- `test-kit-vs-certified-lab`
- `private-well-sampling-mistakes-that-break-results`
- `new-jersey-pwta-vs-full-household-panel`
- `nitrate`
- `nitrate-baby-pregnancy-well-water-checklist`
- `after-flood`
- `coliform`

### Support

Still indexable, but secondary.
These pages exist to reinforce the core wedge, not to act like equal-weight acquisition clusters.

Examples:
- `after-heavy-rain`
- `after-repair`
- `retest-after-treatment`
- `new-baby-at-home`
- `pregnancy-in-home`
- `arsenic-bedrock-testing-checklist`
- `mail-in-lab-vs-local-certified-lab`
- `private-well-testing-schedule-by-household`
- `connecticut-low-ph-blue-green-stains`
- `pennsylvania-private-well-radon`
- `new-york-pfas-private-wells`

### Hold

Publicly reachable, but noindexed on purpose.
These pages can still support internal linking and user navigation without acting like a broad public search bet.

Typical hold pages:
- long-tail contaminants with weak current signal
- broad state owner guides with no clear winner evidence yet
- symptom pages that do not clearly reinforce a current state/testing wedge

### Conversion

Compare pages that are useful only after the problem class is narrower.
These remain public but noindexed.

Small exception:
- a few compare-shaped pages with real testing intent can stay indexable
- current examples: `test-kit-vs-certified-lab`, `mail-in-lab-vs-local-certified-lab`

Examples:
- `uv-vs-ro`
- `uv-vs-chlorination`
- `whole-house-vs-under-sink-ro`
- `ro-vs-adsorptive-media-for-arsenic`

## Family policy

### Keep indexable
- `regional`
- `authority`
- `triggers`

These are the families that best match the current wedge.

### Keep public but noindex
- `contaminants`
- `symptoms`
- `compares`

Reason:
- contaminants and symptoms still matter, but their family hubs make the site look broader than it should right now
- compares are better as internal conversion surfaces than as public acquisition hubs

## What changed in code

The implementation now does four things:

1. Adds a search-role layer:
- `CORE`
- `SUPPORT`
- `HOLD`
- `CONVERSION`

2. Drives `meta robots` from that policy:
- indexable pages -> `index,follow`
- held-back or compare pages -> `noindex,follow`

3. Excludes held-back and compare pages from `sitemap.xml`

4. Repositions the home page around:
- home sale and transaction testing
- state-specific testing paths
- testing-order articles
- a few result-interpretation support pages

## Why noindex instead of deleting

The site still needs these pages for:
- internal linking
- user navigation
- compare handoff
- future cluster expansion if Search Console starts validating them

For now the problem is not that the site has too few pages.
The problem is that too many pages are competing to define what the site is about.

## Google guidance note

This narrowing approach follows Google Search Central guidance:
- `noindex` should be implemented with a robots meta tag or `X-Robots-Tag`, not `robots.txt`
- pages must remain crawlable for Google to see the `noindex`
- canonical signals should stay aligned across sitemap and rel=canonical

References:
- [Block Search indexing with noindex](https://developers.google.com/search/docs/crawling-indexing/block-indexing)
- [How to specify a canonical URL with rel="canonical" and other methods](https://developers.google.com/search/docs/crawling-indexing/consolidate-duplicate-urls)

## What to watch next

After deploy, measure:
- whether `http` duplicate impressions decline
- whether the core regional pages move closer to page 2
- whether home-sale and state-testing pages gain more query breadth
- whether noindexed compare pages disappear from Search over time
- whether the core wedge starts earning clicks before broader families are reopened

## Re-expansion rule

Do not reopen broad families just because a page exists.

Re-expand only when:
- a held-back page family begins showing consistent impressions through internal support links
- a state or symptom cluster demonstrates repeat visibility across multiple related queries
- the site has at least a few page-2 footholds in the core wedge

Until then, keep the brand broad and the public search core narrow.
