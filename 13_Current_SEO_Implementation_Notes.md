# Current SEO Implementation Notes

Use this file as the fast handoff for the current public SEO surface.

## Current Truth

- The public acquisition surface is 97 pSEO detail pages plus family hubs, trust pages, and the home page.
- Regional pages are not justified by a state-name swap alone.
- Regional pages must show a real state delta that changes the next action.
- State-aware deltas are backed by `data/registry/regional_context_registry.csv`.
- Public detail pages now show a visible review and traceability block in the page body.
- Regional detail pages can surface a state guidance path and a certified lab path.
- Winner regional pages can carry the same decision-doc depth model used on core high-intent pages.

## Current Registry And Rendering Files

- `data/pseo/pages.csv`
- `data/pseo/page_sources.csv`
- `data/registry/regional_context_registry.csv`
- `src/main/java/com/example/wellwater/pseo/RegionalContextRegistryService.java`
- `src/main/java/com/example/wellwater/pseo/PseoExperienceService.java`
- `src/main/jte/pages/pseo/detail.jte`

## Current Regional Rules

- Add or deepen a regional page only when the state changes the decision sequence.
- A strong regional page should support:
- state guidance
- certified lab routing
- cluster links into authority, compare, or tool flows
- If the page cannot support those routes with primary sources, narrow the scope or do not expand the page.

## Current Depth Model

Winner pages should answer:
- what this likely is
- whether it is a health, nuisance, corrosion, or mixed issue
- what to do now
- what to test next
- what not to buy yet

Regional winner pages should also answer:
- why this state changes the answer
- which state guidance should shape the next step
- when a certified lab path matters before compare logic

## Current Maintenance Rule

- Refresh registry truth before widening page count.
- Prefer stronger state deltas over new state-name variants.
- Keep visible trust copy aligned with what the page can actually cite and route to.
