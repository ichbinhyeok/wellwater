# Long-Term pSEO Optimization

## Decision

Water Verdict will enter a 90-day observation period with no new pSEO expansion.
The goal is to let the existing search surface accumulate crawl, ranking, and click evidence instead of repeatedly changing the page inventory.

The reference pattern is the useful part of Zapier's integration-page model, not its page count:

- each page maps a recognizable problem entity to a specific context
- each page exposes structured, page-specific facts rather than only swapped nouns
- the page gives the visitor an immediate action before the longer explanation
- related pages form a graph of next decisions, not a list of unrelated articles

For Water Verdict, the equivalent is:

`well-water signal -> decision lens -> next test or safety action -> related decision path`

This is a pattern adaptation, not a claim that the site has Zapier's authority or distribution.

## What Changed

Public pSEO detail pages now expose a compact decision router before the guide layer:

- `Input`: the signal or context the page is designed to accept
- `Engine signal`: the risk or decision lens applied by the engine
- `Output`: the next testing or decision path
- direct tool and source actions remain available without reading the full page

Guide language now says it is the explanation after the route. Related links are labelled as next decision paths instead of related reads. Short meta descriptions are automatically extended with the page's archetype and next-testing intent, capped at 160 characters.

No new URLs, broad family hubs, vendor links, or generic articles were added.

## 90-Day Operating Rule

The following are frozen until the observation window ends:

- pSEO inventory and sitemap expansion
- new state-name variants
- routine title or CTA experiments
- new affiliate or partner assumptions
- new distribution hypotheses

Allowed changes are limited to production bugs, broken official source links, safety corrections, and measurement failures.

## Review At Day 90

Review the same Search Console window against the pre-change baseline and record:

- total clicks and impressions
- pages with at least one click
- pages reaching average position 20 or better
- organic starts and completions of the decision tool
- which page clusters pass users to the tool before compare paths
- whether clicks are concentrated in the NJ transaction cluster, household triggers, or problem-signal pages

The site earns a further SEO investment only if multiple pages show both ranking movement and tool-start evidence. Impressions without clicks or tool use are discovery evidence, not product traction.

## Verification

- `./gradlew.bat --no-daemon test`
- all `145` tests passed after the router and metadata changes
- no new URLs were introduced
- noindex rules and the existing sitemap strategy remain unchanged
