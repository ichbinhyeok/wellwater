# NJ Transaction Distribution Experiment - 2026-07-11

## Decision

The active experiment is no longer "publish general guides and wait for Google" or "submit a ChatGPT app and wait for discovery." The product is `NJ Private-Well Transaction Preflight`, and one engine is distributed through two independently measured routes:

1. co-branded links inserted into a laboratory or home-inspector transaction flow
2. 24 data-qualified NJ municipality pages that route directly into the same tool

The ChatGPT implementation remains available but unpublished and is not counted as an active experiment.

## Product Contract

Public routes:
- `/nj-well-preflight`: indexable product surface
- `/nj/private-well/{municipality-slug}`: indexable only for the 24 fixed pilot municipalities
- `/partners/{partner-slug}/nj-well-preflight`: noindex and available only after explicit partner activation
- `POST /nj-well-preflight/result`: noindex, no-store result

The output provides:
- likely PWTA coverage status, not legal clearance
- the statewide required panel and county-specific mercury or uranium additions
- a certified sampling and closing sequence
- historical NJDEP aggregate signals for the matched 2-mile grid or municipality
- a fixed NJDEP certified-laboratory handoff

It never claims that a property or individual well is contaminated.

## Data And Privacy Boundary

Bundled official snapshots:
- `1,676` NJDEP 2-mile grid polygons
- `564` NJDEP municipality summaries
- source period: September 2002 through December 2024
- snapshot date: `2026-07-11`

Startup fails when the snapshot row counts or SHA-256 checksums differ from the reviewed metadata. Full source URLs, checksums, and the required NJDEP secondary-product disclaimer are stored in `src/main/resources/data/nj/SOURCE-METADATA.md`.

Address handling:
- address is optional
- address is sent to the U.S. Census geocoder only during the request
- only the returned coordinates are used in memory for point-in-polygon matching
- raw address and coordinates are absent from logs, metrics, result URLs, and saved files
- failed geocoding falls back to the selected municipality

## Partner Track

The bundled catalog contains exactly five laboratory and five home-inspector prospects. Every prospect is inactive. This avoids false co-branding or implying a relationship before approval.

One-time activation procedure:
1. verify current laboratory certification or inspector license and sampling scope
2. obtain written permission to publish the co-branded route and booking link
3. copy the complete 10-row catalog to the persistent data volume
4. set the approved row to `active=true`
5. prefix `verification_status` with `verified:` and record the verification date
6. keep an absolute HTTPS booking URL and set `NJ_PARTNER_CSV_PATH` to the mounted file
7. verify the co-branded route and outbound redirect before sending the partner its link

Success gate:
- 10 one-time proposals
- at least 2 live partner links within 30 days
- by Day 60: 100 partner landing views, 40 completions, and 15 partner or booking clicks

Failure diagnosis:
- fewer than 2 live partners: partner proposition failed
- 2 live partners but fewer than 100 views: borrowed reach failed
- completion below 40%: landing or form failed
- outbound clicks below 15% of completions: handoff or partner fit failed

## Local Search Track

The sitemap contains one NJ tool route and 24 municipality routes. The pilot set was selected from the official municipality layer using non-suppressed parameter coverage and differentiated aggregate detections. All 564 municipalities remain available as non-indexed form fallbacks, but non-pilot public page requests return `404`.

Day-90 success gate:
- at least 80% of the 24 municipality URLs indexed
- at least 1,000 non-brand impressions
- at least 10 organic clicks across 3 or more municipality pages
- at least 20 `organic_local` tool starts
- at least 8 completed preflights

Do not add another municipality until these gates pass. Impression without click means the SERP proposition failed; click without tool start means the page-to-tool route failed; starts without completion mean the product flow failed.

## Measurement

NJ events use a separate CSV contract:

```text
date,event_name,channel,distribution_source,result_family,destination_type,outcome,latency_ms
```

Allowed channels are `direct`, `organic_local`, and `partner`. The system records landing view, tool start, completion or failure, official click, certified-lab click, and partner click. It stores no session ID, address, coordinate, contact detail, or raw form answer.

Protected aggregate summary:
- `GET /admin/nj-distribution-metrics`

## Stop Rule

If both routes miss their gates, treat the result as a distribution failure under the free, low-maintenance operating constraint. Move Water Verdict to maintenance mode instead of adding features, publishing more pSEO pages, or starting ongoing manual outreach. If only one route passes, invest only in that route.
