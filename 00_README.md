# Well Water Verdict

Private well water decision engine for problem-first search traffic.

## Current Product Shape

This repo is no longer just a pSEO skeleton.

Current public surface:
- indexable home page
- family hubs for `contaminants`, `symptoms`, `compares`, `triggers`, `regional`, `authority`
- 98 pSEO detail pages backed by CSV
- trust hub and 4 trust pages at `/trust/*`
- structured data, canonical, and breadcrumb support on public pages

Current tool surface:
- `/tool/result-first`
- `/tool/symptom-first`
- `/tool/trigger-first`
- `/tool/result`
- personalized result pages rendered as `noindex,nofollow`

Current commercial surface:
- lead capture on regional, authority, and detail pages
- admin dashboard at `/admin`
- internal compare pages used as the active commercial bridge
- direct vendor or affiliate channels are not the primary surface yet

## Product Positioning

This project is not an Amazon-style review site.

The current sequence is:
1. organic search lands on an indexable page
2. the page frames risk, scope, and the next verification step
3. the user enters the tool or moves into a compare page
4. lead capture or future partner routing happens only after the page earns that transition

That means the moat is the decision framework, not a list of products.

## Current SEO Inventory

pSEO families:
- contaminants: 22
- symptoms: 14
- compares: 16
- triggers: 12
- regional: 15
- authority: 19

Trust pages:
- methodology
- review policy
- sources policy
- safety and scope

Core high-intent pages also have a deeper decision-doc layer with:
- one-line call
- health vs nuisance split
- retest timing
- three actions before buying
- common confusion block
- FAQ

## Commercial Readiness

The current site can support partner or lead routing for the right clusters.

Best current partner-ready clusters:
- arsenic
- nitrate
- sulfur smell
- iron and orange stains
- low pH and corrosion
- radon
- PFAS
- coliform or post-flood microbial follow-up

Current rule:
- testing, verification, and state guidance come before vendor links
- internal compare pages are the active commercial handoff until verified partner channels go live

## Running Locally

```powershell
.\gradlew.bat bootRun
```

Default app URL:
- `http://localhost:8080`

Admin credentials are configured via environment variables in production.

## Documentation Map

Use these files as the current source of truth:
- `01_Strategy_One_Pager.md`
- `02_Product_PRD.md`
- `03_Input_Output_Spec.md`
- `04_Decision_Engine_Spec.md`
- `05_Trust_Compliance_Guidelines.md`
- `06_Content_SEO_Architecture.md`
- `07_Monetization_Ops.md`
- `08_Data_Model_System_Architecture.md`
- `09_Execution_Roadmap_KPI.md`
- `10_Implementation_Plan_SpringBoot_JTE.md`
- `11_Source_Data_Collection_Policy.md`
- `12_PSEO_MVP_Playbook.md`

Session-specific handoff files have been removed so the active documentation set stays clean.
