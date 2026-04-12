# Search Re-Expansion Triggers

Date: `2026-04-12`

This document defines when the narrowed search surface can widen again.

The purpose is to prevent random broadening.
Re-expansion should happen only when the current wedge proves it can rank, earn clicks, and hold together technically.

## First principle

Do not re-expand because a page exists.

Re-expand only when:
- the current wedge is producing evidence
- technical drift is under control
- the next page or family has a specific reason to exist

## Hard gates before any expansion

Do not reopen new search surface until all of these are true:

1. deployment parity is stable
- production is shipping the same `data/pseo` state as the repo
- no recent deploy failure is leaving support pages missing in production

2. protocol cleanup is mostly consolidated
- HTTP impression share falls below `5%` for two consecutive 28-day checks
- inspected winner URLs show `google canonical = https`

3. the narrowed wedge starts earning clicks
- site-wide organic clicks are greater than `0`
- and clicks appear in at least two different pages or clusters

## Trigger types

### 1. Promote `SUPPORT` to `CORE`

Promote one support page at a time when all of these are true over a 28-day window:
- the page earns at least `75` impressions
- the page or its main query set reaches average position `25` or better
- the page belongs to a cluster that already has one `CORE` page with clicks or page-2 foothold
- the page adds a distinct search intent instead of duplicating an existing core page

Examples:
- a state-specific arsenic companion page
- a sale-path extension page
- a testing-order page that narrows a proven cluster

### 2. Reopen one `HOLD` detail page as `SUPPORT`

Because `HOLD` pages are noindexed, use cluster evidence instead of page evidence.

Reopen one held page when:
- a related cluster already has at least two indexable pages with repeat impressions
- one of those pages reaches average position `20` or better
- the held page answers a missing query intent that the live cluster does not already cover
- the held page has a clear internal-link path from a live core or support page

Examples:
- reopen a held state page only after the same region or problem class proves repeat demand
- reopen a held symptom variant only after the parent symptom cluster shows real traction

### 3. Reopen a family hub

Reopen a family hub from `noindex,follow` to `index,follow` only when the family itself is proven.

Use this rule:
- at least `3` pages in that family have average position `20` or better over the same 28-day window
- the family contributes at least `10` organic clicks in that window
- the family has one clear user-intent story, not a mixed bag of unrelated pages

Default family order for reconsideration:
1. `contaminants`
2. `symptoms`
3. `compares`

`compares` should stay last because it is the most downstream and easiest to overexpose too early.

### 4. Reopen more compare pages

Do not expand compare search surface until upstream problem-definition pages are working.

Expand compare exposure only when:
- at least `2` upstream diagnosis or testing pages in the same problem class are already earning clicks
- internal analytics show users are moving from those pages into compare pages
- the compare page answers a real narrowed decision, not a generic product-vs-product curiosity query

## Trigger thresholds by layer

### Site-level health trigger

Treat the current wedge as proven enough to widen when all of these are true:
- site-wide organic clicks are consistently non-zero for two straight 28-day reads
- at least `3` pages have average position `20` or better
- at least `2` clusters are producing clicks, not just impressions

### Cluster-level trigger

A cluster is ready for one more page when:
- one page in the cluster is average position `15` or better
- a companion page in the same cluster is average position `25` or better
- the cluster has a clear next unanswered query intent

### Page-level trigger

A page deserves promotion when:
- it is already indexable
- it is getting repeated impressions from semantically consistent queries
- the query pattern is not just one odd long-tail or quiz-like artifact

## What does not count as a trigger

Do not broaden because of:
- one single-day spike
- one weird long-tail query
- family-hub impressions without page-level confirmation
- temporary rank bursts after a crawl
- internal enthusiasm without Search Console support

## Operating cadence

Review re-expansion only on a fixed rhythm:
- every `14` days in the current early stage
- page moves should happen at most once per review cycle
- expand one layer at a time:
  1. promote one page
  2. observe
  3. only then consider the next page or hub

## Default move order once triggers are met

When the site is ready to widen, use this order:
1. promote one adjacent support page to `CORE`
2. reopen one held detail page as `SUPPORT`
3. only after multiple page wins, reopen one family hub
4. leave broad compare exposure for last

## Decision log requirement

Every expansion should record:
- review date
- exact 28-day window
- pages or queries that met the trigger
- what moved
- what was explicitly not moved
- what will be checked next

This keeps re-expansion intentional instead of drifting back into broad, weak coverage.

