# Search Asset Classification

Date: `2026-04-04`

This is the explicit slug-level search classification for the current public site.
It exists so the site does not drift back into "everything is a money page" or "everything should be indexed."

Role meanings:
- `CORE`: primary acquisition pages, indexable, in sitemap
- `SUPPORT`: secondary but still indexable reinforcement pages
- `HOLD`: public but `noindex,follow`
- `CONVERSION`: public compare or conversion surfaces, `noindex,follow`

Current snapshot:
- `CORE`: `21`
- `SUPPORT`: `39`
- `HOLD`: `34`
- `CONVERSION`: `13`

## Core

- `authority` `home-sale-private-well-testing-checklist`
- `authority` `how-to-read-a-well-water-lab-report`
- `authority` `low-ph-copper-corrosion-testing-order`
- `authority` `metallic-taste-plumbing-vs-source-water`
- `authority` `new-hampshire-arsenic-testing-order`
- `authority` `new-jersey-pwta-vs-full-household-panel`
- `authority` `nitrate-baby-pregnancy-well-water-checklist`
- `authority` `oregon-private-well-homebuyer-testing`
- `authority` `private-well-home-sale-testing-by-state`
- `authority` `private-well-sampling-mistakes-that-break-results`
- `compares` `test-kit-vs-certified-lab`
- `contaminants` `arsenic`
- `contaminants` `coliform`
- `contaminants` `nitrate`
- `contaminants` `ph`
- `regional` `new-hampshire-arsenic-well-water`
- `regional` `new-jersey-pwta-private-well-testing`
- `regional` `oregon-private-well-testing-recommendations`
- `symptoms` `metallic-taste`
- `triggers` `after-flood`
- `triggers` `home-purchase-test`

## Support

- `authority` `arsenic-bedrock-testing-checklist`
- `authority` `new-york-pfas-private-well-testing-order`
- `authority` `pfas-private-well-filter-claim-checklist`
- `authority` `private-well-testing-schedule-by-household`
- `authority` `radon-radium-private-well-testing-order`
- `authority` `sulfur-smell-hot-water-vs-whole-house`
- `authority` `texas-private-well-sampling-chain-of-custody`
- `compares` `mail-in-lab-vs-local-certified-lab`
- `contaminants` `e-coli`
- `contaminants` `hardness`
- `contaminants` `iron`
- `contaminants` `lead`
- `contaminants` `manganese`
- `contaminants` `pfas`
- `contaminants` `radon`
- `contaminants` `uranium`
- `regional` `connecticut-low-ph-blue-green-stains`
- `regional` `florida-rotten-egg-smell-well-water`
- `regional` `iowa-nitrate-baby-well-water`
- `regional` `maine-bedrock-arsenic-private-well`
- `regional` `massachusetts-bedrock-arsenic-uranium-well`
- `regional` `minnesota-nitrate-private-well`
- `regional` `new-york-pfas-private-wells`
- `regional` `pennsylvania-private-well-radon`
- `regional` `vermont-new-well-arsenic-uranium-testing`
- `regional` `virginia-private-well-testing-program`
- `regional` `washington-private-well-water-testing`
- `regional` `wisconsin-nitrate-pregnancy-well-water`
- `symptoms` `black-stains`
- `symptoms` `blue-green-stains`
- `symptoms` `cloudy-water`
- `symptoms` `orange-stains`
- `symptoms` `rotten-egg-smell`
- `symptoms` `sulfur-smell-hot-water`
- `triggers` `after-heavy-rain`
- `triggers` `after-repair`
- `triggers` `new-baby-at-home`
- `triggers` `pregnancy-in-home`
- `triggers` `retest-after-treatment`

## Hold

- `authority` `california-after-wildfire-private-well-checklist`
- `authority` `florida-sulfur-smell-staining-testing-order`
- `authority` `how-to-verify-water-treatment-claims`
- `authority` `when-not-to-buy-treatment-yet`
- `authority` `wildfire-drought-private-well-risk-reset`
- `contaminants` `barium`
- `contaminants` `chloride`
- `contaminants` `chromium`
- `contaminants` `copper`
- `contaminants` `fluoride`
- `contaminants` `nitrite`
- `contaminants` `selenium`
- `contaminants` `sodium`
- `contaminants` `sulfate`
- `contaminants` `tds`
- `regional` `california-private-well-owner-guide`
- `regional` `georgia-private-well-water-guidance`
- `regional` `indiana-well-water-quality-testing`
- `regional` `michigan-arsenic-private-well`
- `regional` `north-carolina-private-well-water-faqs`
- `regional` `south-carolina-well-water-quality-testing`
- `regional` `texas-private-well-sampling-testing`
- `symptoms` `bitter-taste`
- `symptoms` `low-water-pressure`
- `symptoms` `musty-odor`
- `symptoms` `pink-stains`
- `symptoms` `salty-taste`
- `symptoms` `scale-buildup`
- `symptoms` `slimy-residue`
- `triggers` `after-boil-water-advisory`
- `triggers` `after-long-vacancy`
- `triggers` `after-power-outage`
- `triggers` `after-wildfire`
- `triggers` `neighbor-contamination-alert`

## Conversion

- `compares` `acid-neutralizer-vs-soda-ash`
- `compares` `air-injection-vs-oxidizing-filter`
- `compares` `bottled-water-vs-well-treatment`
- `compares` `carbon-vs-ro`
- `compares` `point-of-entry-vs-point-of-use`
- `compares` `radon-aeration-vs-gac`
- `compares` `ro-vs-adsorptive-media-for-arsenic`
- `compares` `shock-vs-continuous-chlorination`
- `compares` `softener-vs-iron-filter`
- `compares` `spin-down-vs-cartridge-sediment-filter`
- `compares` `uv-vs-chlorination`
- `compares` `uv-vs-ro`
- `compares` `whole-house-vs-under-sink-ro`

## Operating rule

When a new slug is added, it must be placed in one of these buckets on purpose.
If that cannot be justified clearly, default to `HOLD`, not `CORE`.
