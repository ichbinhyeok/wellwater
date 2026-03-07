# Home Handoff - 2026-03-07

## Why this file exists

This repo was being edited on a temporary PC, not the normal local machine.
Use this file as the single handoff anchor when resuming at home.

If you open this repo later, tell Codex:

`Continue from 15_HOME_HANDOFF_2026-03-07.md and verify on 8081, then leave default 8080.`

## Current branch and worktree state

- Branch: `master`
- There are uncommitted changes in the worktree.
- Do not assume the repo is clean.
- The previous session already completed several product and engine upgrades.

## What is already done

### Product and UX

- Public-facing pages were redesigned to look like a real product rather than an internal hub.
- Public layout and pages already updated:
  - `src/main/jte/layout/main.jte`
  - `src/main/jte/pages/home.jte`
  - `src/main/jte/pages/pseo/list.jte`
  - `src/main/jte/pages/pseo/detail.jte`
  - `src/main/jte/pages/not-found.jte`

### Engine and routing

- Input model was widened with richer context:
  - household size
  - lab name
  - issue location
  - change timing
  - smell / stain / taste
  - multi-select existing treatments
- Registry-driven intake options were wired in.
- Secondary context now strengthens symptom/trigger matching.
- Threshold-aware normalization was added:
  - canonical units
  - threshold refs
  - source version
  - decision version
- `chemical-health` threshold hits now route away from green compare flow.
- Outbound redirect was hardened to an allowlist-style policy.

### Tests and verification already completed earlier

- `gradlew.bat test` had passed after the major engine and public-page work.
- Playwright verification had been run on `8081`.
- The rule from now on is:
  - verify on `8081`
  - stop `8081`
  - leave default state as `8080`

## What is partially done and still open

Two tasks were being worked in parallel:

1. Engine-side design cleanup
2. Threshold rule coverage expansion

### 1. Engine-side design cleanup status

- `src/main/jte/layout/engine.jte` was already replaced with a newer visual system.
- The intake/result templates still need cleanup because some strings are broken or mojibake.
- These files are the active target:
  - `src/main/jte/pages/intake/result-first.jte`
  - `src/main/jte/pages/intake/symptom-first.jte`
  - `src/main/jte/pages/intake/trigger-first.jte`
  - `src/main/jte/pages/result/view.jte`

The intended cleanup is:

- convert broken Korean/mojibake strings to clear English
- keep the current logic
- align intake/result screens with the newer engine layout
- keep result page consumer-facing, not internal/debug-facing

### 2. Threshold rule coverage status

Already in code:

- normalization collects multiple threshold refs
- threshold summary prefers the triggered summaries
- corrosion thresholds can now push branch routing to amber

Still needed:

- add tests for upper/lower range cases like `pH`
- add more coverage for threshold-hit health analytes like `lead` and `radium`

## Exact next work

### Step 1

Replace or rewrite these templates cleanly:

- `src/main/jte/pages/intake/result-first.jte`
- `src/main/jte/pages/intake/symptom-first.jte`
- `src/main/jte/pages/intake/trigger-first.jte`
- `src/main/jte/pages/result/view.jte`

Use plain English labels such as:

- `Start with a lab result`
- `Start with a symptom`
- `Start with a recent trigger`
- `Decision result`
- `Enter new data`
- `Scenario compare`
- `Sample freshness`
- `Completeness`
- `State guidance`
- `Certified lab finder`
- `Need more help?`

### Step 2

Add threshold coverage tests:

- in `src/test/java/com/example/wellwater/decision/DecisionInputNormalizationServiceTest.java`
  - pH high-range threshold test
- in `src/test/java/com/example/wellwater/decision/DecisionEngineServiceTest.java`
  - pH high-range amber corrosion flow
  - lead above threshold amber verify-first flow
  - radium above threshold amber verify-first flow

### Step 3

Run:

```powershell
.\gradlew.bat test
```

### Step 4

Verify in browser on `8081`, not `8080`.

Minimum browser checks:

- `/tool/result-first`
- `/tool/symptom-first`
- `/tool/trigger-first`
- submit one threshold-hit result page, such as arsenic or pH

Then stop `8081` and leave the repo back in default `8080` state.

## Recommended resume prompt

When resuming at home, use this exact prompt:

`Continue from 15_HOME_HANDOFF_2026-03-07.md. Finish engine intake/result template cleanup, add pH/lead/radium threshold coverage tests, verify on 8081 with Playwright, then stop 8081 and leave default 8080.`

## Practical ways to carry this home

Best option:

- take the whole repo folder with the current worktree
- or commit and push the branch before leaving

Minimum safe option:

- keep this file
- keep the repo with current uncommitted changes
- resume from the prompt above

If you cannot take the full repo, at least preserve:

- this file
- `git diff`
- the modified files under `src/main/jte/`, `src/main/java/`, and `src/test/java/`

