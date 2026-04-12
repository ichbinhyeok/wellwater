# Search Focus Revision

Date: `2026-04-12`

## Why this revision exists

The project does not have a topic-selection failure.

Search Console data reviewed for `sc-domain:waterverdict.com` over `2026-03-12` to `2026-04-09` showed:
- `803` impressions
- `0` clicks
- average position `40.0`

That means the site is still in ranking-entry mode.
The main problem is not "too little seed."
The main problem is that Google still sees too many possible site identities at once.

The clearest repeat query wedges in the same window were:
- New Hampshire arsenic
- New Jersey PWTA / home-sale testing
- Oregon private well testing

The broad symptom pages still picked up discovery, but mostly far outside durable click positions.

## What changed

This revision narrows the active search focus without deleting content.

What changed in code:
- the `CORE` acquisition set was reduced from `21` pages to `12`
- `contaminants` and `symptoms` family hubs are now `noindex,follow`
- featured regional promotion was reduced to the three state pages with the clearest repeat demand signal

What did not change:
- detail pages in `contaminants` and `symptoms` can still remain indexable when they are in `CORE` or `SUPPORT`
- compare pages still stay public but `noindex,follow`
- no content was deleted

## Current role counts

Current search-role snapshot after the revision:
- `CORE`: `12`
- `SUPPORT`: `48`
- `HOLD`: `34`
- `CONVERSION`: `13`

Interpretation:
- the site still keeps a wide support bench
- the site now asks Google to understand a tighter public wedge first

## Current core wedge

The active acquisition wedge is now:

`private well home-sale, state-specific testing, and testing-order guidance`

Current `CORE` pages:
- `new-jersey-pwta-private-well-testing`
- `private-well-home-sale-testing-by-state`
- `home-purchase-test`
- `home-sale-private-well-testing-checklist`
- `new-hampshire-arsenic-well-water`
- `new-hampshire-arsenic-testing-order`
- `oregon-private-well-testing-recommendations`
- `oregon-private-well-homebuyer-testing`
- `how-to-read-a-well-water-lab-report`
- `test-kit-vs-certified-lab`
- `private-well-sampling-mistakes-that-break-results`
- `new-jersey-pwta-vs-full-household-panel`

## Support posture

Pages like these remain indexable, but they no longer define the primary site identity:
- `arsenic`
- `nitrate`
- `coliform`
- `after-flood`
- `metallic-taste`
- `metallic-taste-plumbing-vs-source-water`
- `ph`
- `low-ph-copper-corrosion-testing-order`
- `nitrate-baby-pregnancy-well-water-checklist`

Reason:
- these topics still matter
- but the current data does not justify letting them compete equally with the state-sale-testing wedge

## Family-hub policy

Current family-hub indexing policy:
- keep indexable:
  - `regional`
  - `authority`
  - `triggers`
- keep public but `noindex,follow`:
  - `contaminants`
  - `symptoms`
  - `compares`

Reason:
- the site's best current signal is not "generic well-water encyclopedia"
- the site's best current signal is "decision path for testing, transfer timing, and state-aware next steps"

## Homepage focus

Featured regional emphasis is now limited to:
- `new-jersey-pwta-private-well-testing`
- `new-hampshire-arsenic-well-water`
- `oregon-private-well-testing-recommendations`

This does not mean Connecticut or other regional pages are dead.
It means the homepage should reflect proven wedges first.

## What this revision is trying to improve

1. Stronger site identity for Google
2. Less internal-priority dilution across broad page families
3. Faster measurement of whether the sale-path and state-testing wedge can reach page 2 and page 1
4. Cleaner separation between "supported public pages" and "primary search bets"

## What to watch next

After this revision ships and recrawl catches up, check:
- whether the `regional`, `authority`, and `triggers` hubs become the only indexable family hubs
- whether HTTP impression share continues to decline
- whether the New Hampshire, New Jersey, and Oregon clusters move closer to page 2
- whether the home-sale and testing-order pages start producing the site's first stable organic clicks

