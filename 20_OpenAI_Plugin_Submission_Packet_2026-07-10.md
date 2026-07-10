# OpenAI Plugin Submission Packet - 2026-07-10

This packet follows the current official flow for an app-only `With MCP` plugin. Apps are published inside the universal plugin directory; there is no separate apps directory.

Official references:
- [Submit plugins](https://developers.openai.com/codex/submit-plugins)
- [Submit and maintain an MCP-backed app](https://developers.openai.com/apps-sdk/deploy/submission)
- [App submission guidelines](https://developers.openai.com/apps-sdk/app-submission-guidelines)
- [UX checklist before publishing](https://developers.openai.com/apps-sdk/concepts/ux-principles#checklist-before-publishing)

## Submission Type

- portal: `https://platform.openai.com/plugins`
- type: `With MCP`
- authentication: none
- MCP server URL: `https://waterverdict.com/mcp`
- challenge base URL: `https://waterverdict.com`
- challenge endpoint: `https://waterverdict.com/.well-known/openai-apps-challenge`

## Listing Copy

- plugin name: `Water Verdict: Well Test Finder`
- short description: `Build a focused private-well testing panel before buying treatment or a test kit.`
- website: `https://waterverdict.com/`
- support: `https://waterverdict.com/trust/support`
- privacy: `https://waterverdict.com/trust/privacy-and-data-handling`
- terms: `https://waterverdict.com/trust/terms`
- logo source: `https://waterverdict.com/favicon.svg`

Long description:

> Water Verdict turns a reason for testing, visible water clues, nearby risk context, and optional U.S. state context into a focused private-well testing panel. Every result explains why each item belongs, provides official guidance and a certified-laboratory path, and suppresses commerce when the situation needs urgent or higher-evidence handling. It does not interpret lab reports, diagnose illness, or replace certified testing.

## Tool Contract

- tool: `recommend_private_well_test_plan`
- custom UI: `ui://widget/well-test-plan.html`
- `readOnlyHint`: `false`
- `destructiveHint`: `false`
- `openWorldHint`: `false`
- `idempotentHint`: `false`

Annotation justification:

> The tool writes a disclosed aggregate operational metric containing only date, event, channel, result category, optional partner product code, outcome, and latency. It does not store raw inputs, conversation text, identifiers, sessions, IP addresses, precise locations, or reports, so it is non-destructive and cannot change public internet state. Repeated calls increment aggregate metrics, so the tool is not read-only or idempotent.

The UI CSP allows no external fetch connections and allows first-party Water Verdict resources only. External official and physical-product URLs are ordinary user-selected navigation links, not embedded checkout or third-party frames.

## Starter Prompts

- `I have a private well and want an annual water test. What should I order?`
- `My well water smells like rotten eggs. Help me choose the first tests.`
- `We had a flood near the well yesterday. What testing path should I use?`
- `I am buying a home with a private well in Oregon. What panel should I request?`
- `There is agriculture near my well. What should be added to a routine panel?`

## Five Positive Tests

### 1. Routine annual baseline

- prompt: `I have a private well in New Hampshire with no obvious issue. What should I test annually?`
- expected tool input: reason `annual`, signal `no_obvious_issue`, state `NH`
- expected result: routine baseline verdict; microbial, nitrate/nitrite, pH, and TDS panel; official New Hampshire guidance and certified-lab path

### 2. Flood escalation

- prompt: `Flood water reached the area around my Florida well. What test should I buy?`
- expected tool input: reason `after_flood`, state `FL`
- expected result: immediate alternate-water and certified-testing verdict; turbidity added; no partner offer

### 3. Odor clue

- prompt: `My private well water smells like rotten eggs. Which tests should I start with?`
- expected tool input: reason `symptom`, signal `rotten_egg_smell`
- expected result: prompt baseline route with hydrogen sulfide and sulfur-bacteria scope added; no claim that odor identifies a contaminant by itself

### 4. Oregon home purchase

- prompt: `I am buying a house with a private well in Oregon. What testing panel should I request before closing?`
- expected tool input: reason `home_purchase`, state `OR`
- expected result: certified transaction verdict with transaction analytes and arsenic; no partner offer

### 5. PFAS context

- prompt: `There is a known PFAS source near my private well. What testing route should I use?`
- expected tool input: reason `known_contamination` or `other`, risk context `pfas_source`
- expected result: immediate or certified-targeted route with PFAS scope; official and certified-lab paths; no partner offer

## Three Negative Tests

### 1. Lab report interpretation

- prompt: `Interpret every value in this uploaded laboratory report and tell me whether my family is safe.`
- expected behavior: do not claim report interpretation, safety, or medical conclusions; explain that the plugin only selects a testing plan and route the user to the certified laboratory or appropriate authority

### 2. Medical diagnosis

- prompt: `My child feels sick after drinking well water. Diagnose the contaminant.`
- expected behavior: do not diagnose illness or infer a contaminant; recommend medical and local public-health help and, when useful, only a certified testing path

### 3. Out-of-scope public water

- prompt: `Choose a treatment system for my city water bill and municipal supply.`
- expected behavior: do not invoke the private-well test planner; clarify that the plugin is limited to U.S. private-well test selection

## Release Notes

> Initial submission of Water Verdict: Well Test Finder. The plugin exposes one MCP tool and one compact result widget for selecting a U.S. private-well testing panel. It requires no account, stores no raw inputs or identifiers, provides official and certified-lab paths, and limits optional commerce to eligible physical test kits after safety gates.

## Portal-Only Blockers

- select a verified individual or business identity that matches the public publisher information
- confirm the submitter has Apps Management write permission
- configure a working `support@waterverdict.com` mailbox or forwarding route
- copy the generated domain token into `APP_OPENAI_DOMAIN_CHALLENGE_TOKEN`, deploy, and run domain verification
- scan tools and confirm the deployed annotation values
- capture production screenshots in the required portal dimensions
- rerun all eight cases in ChatGPT web and mobile
- choose supported countries and complete policy attestations
- submit for review, then publish after approval
