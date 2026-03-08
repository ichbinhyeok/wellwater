# Content And SEO Architecture

## 1. Goal

The indexable surface exists to win problem-intent search and hand the user into a decision path.

This repo is not trying to rank broad product-review queries first.
It is trying to rank situation queries where the user needs the next safe step.

## 2. Surface Split

### Indexable public surface
- `/`
- `/well-water/family/{family}`
- `/well-water/{slug}`
- `/trust`
- `/trust/{slug}`

### Noindex decision surface
- `/tool/result-first`
- `/tool/symptom-first`
- `/tool/trigger-first`
- `/tool/result`

Public pages orient the situation.
Noindex result pages do the more personalized reasoning.

## 3. Current Page Families

### Contaminants
Use when the user already has a named analyte or lab line.
Examples:
- nitrate
- arsenic
- coliform
- PFAS
- radon
- pH

### Symptoms
Use when the user only has visible or sensory clues.
Examples:
- rotten egg smell
- orange stains
- black stains
- metallic taste
- cloudy water
- blue-green stains

### Triggers
Use when the decision changes because of an event.
Examples:
- after flood
- after repair
- home purchase test
- retest after treatment
- after wildfire
- after power outage

### Compares
Use when the user is close to a category choice, not a generic product search.
Examples:
- UV vs chlorination
- RO vs adsorptive media for arsenic
- acid neutralizer vs soda ash
- carbon vs RO
- point-of-entry vs point-of-use

### Regional
Use when geology, state testing pathways, or local rules change the correct next action.
Examples:
- New Hampshire arsenic
- New Jersey PWTA
- Florida sulfur smell
- Connecticut low pH
- New York PFAS

### Authority
Use when the user needs a trust-building or claim-check document.
Examples:
- how to read a well-water lab report
- when not to buy treatment yet
- how to verify water treatment claims
- private well sampling mistakes that break results

## 4. Current Inventory

Current pSEO inventory:
- contaminants: 22
- symptoms: 14
- compares: 15
- triggers: 12
- regional: 15
- authority: 19

Total pSEO pages: 97

## 5. Internal Link Logic

The site should behave like a graph, not a pile of isolated pages.

Current link directions:
- family hub -> detail page
- detail page -> tool handoff
- detail page -> related pages
- detail page -> internal compare path
- regional page -> matching contaminant, symptom, compare, and authority pages
- authority page -> matching compare pages or core cluster pages

## 6. Commercial Positioning

The commercial layer is currently internal-first.

That means:
- public pages do not jump straight to vendor pages
- money CTAs now point to internal compare or authority surfaces
- testing and verification come before vendor routing
- lead capture remains live where the page has enough intent and trust

This is intentional.
Until verified partners exist, internal compare pages are the correct commercial bridge.

## 7. Core Decision-Doc Layer

The highest-intent public pages now carry deeper answer blocks:
- one-line call
- health vs nuisance split
- retest timing
- three actions before buying
- common confusions
- FAQ

Current core set:
- rotten-egg-smell
- orange-stains
- black-stains
- cloudy-water
- metallic-taste
- after-flood
- after-repair
- home-purchase-test
- retest-after-treatment
- nitrate
- coliform
- arsenic

## 8. Structured Search Signals

Public pages currently include:
- canonical URL
- breadcrumb markup
- JSON-LD

Trust pages use static page markup.
Detail pages use structured data tied to the page context and breadcrumb hierarchy.

## 9. Content Rule

Do not grow by cloning state names or swapping nouns in thin templates.

A new page family or page variant is justified only when:
- the query intent is distinct
- the next action changes
- the evidence hierarchy changes
- the page can link into a meaningful cluster

## 10. Near-Term Growth Rule

The next content gains should come from:
- deepening high-intent pages
- adding scenario-specific compare pages
- expanding state clusters where the next action genuinely changes
- adding authority pages that unlock partner trust later

The next gains should not come from broad generic affiliate pages.
