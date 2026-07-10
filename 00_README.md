# Water Verdict

Automated private-well test finder with a first-party web surface and a bounded OpenAI distribution experiment.

## Current Product Shape

The product is tool-first. It turns a reason for testing, visible clues, and nearby risk context into a focused test panel and certified-lab path before any optional commercial handoff.

Current public surface:
- tool-first home page with the first input above the fold
- immediate, noindex web results at `POST /tool/test-plan`
- trust, privacy, disclosure, and support pages at `/trust/*`
- the existing pSEO inventory remains available but is frozen; it is not the active growth model

Current tool surface:
- web entry at `/#test-plan`
- MCP endpoint at `POST /mcp`
- one MCP tool for the OpenAI plugin: `recommend_private_well_test_plan`
- MCP App widget resource at `ui://widget/well-test-plan-v2.html`
- official guidance and certified-lab links in every successful result
- legacy result-first flows remain in the codebase but are not promoted

Current commercial surface:
- optional allowlisted Tap Score physical-kit links for eligible routine paths
- no offer on urgent, home-purchase, known-contamination, flood, wildfire, PFAS, fuel, or radionuclide paths
- no new lead capture, account, direct payment, or manual consulting flow
- aggregate completion, official-resource, certified-lab, and partner metrics at the credential-protected `/admin/pivot-metrics`

## Product Positioning

This project is not an Amazon-style review site.

The current sequence is:
1. the user states why they need a well-water test
2. the engine selects a focused panel and urgency level
3. the result shows official guidance and a certified-lab path
4. an optional physical-kit link appears only when the safety gates allow it

The engine is the product. Public information explains and supports its output; it is not the main experience.

## Search Inventory Policy

The existing sitemap is a frozen acquisition bench, not a mandate to publish more pages. No new SEO inventory is added during the 90-day ChatGPT distribution test. Existing URLs are retained initially to avoid mixing a product pivot with a destructive index reset. Any later pruning must follow the gates in `19_Automated_Distribution_Pivot_2026-07-10.md`.

## Commercial Readiness

Commerce is configuration-driven. If an approved HTTPS Tap Score URL is absent or does not use an allowlisted Tap Score host, the offer is suppressed. Water Verdict never handles checkout or laboratory results.

## Running Locally

```powershell
.\gradlew.bat bootRun
```

Run the packaged JAR with the production profile so JTE uses the precompiled templates:

```powershell
java -jar build/libs/waterverdict-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

Default app URL:
- `http://localhost:8080`

Admin credentials are configured via environment variables in production.

Required production variable:
- `APP_SITE_BASE_URL`

Admin variables; when either is blank, `/admin/**` fails closed with `503`:
- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`

Optional launch variables:
- `APP_TAP_SCORE_ESSENTIAL_URL`
- `APP_TAP_SCORE_ADVANCED_URL`
- `APP_OPENAI_DOMAIN_CHALLENGE_TOKEN`
- `APP_PIVOT_EXPERIMENT_START_DATE` in `YYYY-MM-DD` format after public plugin publication
- `APP_SUPPORT_EMAIL`

Operational endpoints:
- public health: `GET /health/app`
- MCP transport: `POST /mcp`
- OpenAI domain challenge: `GET /.well-known/openai-apps-challenge`
- protected aggregate metrics: `GET /admin/pivot-metrics`

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
- `13_Current_SEO_Implementation_Notes.md`
- `14_Search_Console_Tracking_Log.md`
- `17_Search_Focus_Revision_2026-04-12.md`
- `18_Search_ReExpansion_Triggers_2026-04-12.md`
- `19_Automated_Distribution_Pivot_2026-07-10.md`
- `20_OpenAI_Plugin_Submission_Packet_2026-07-10.md`

Session-specific handoff files have been removed so the active documentation set stays clean.

For current SEO implementation truth, also check:
- `data/registry/regional_context_registry.csv`
- `src/main/java/com/example/wellwater/pseo/RegionalContextRegistryService.java`
- `src/main/jte/pages/pseo/detail.jte`
