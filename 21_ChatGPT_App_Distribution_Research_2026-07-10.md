# ChatGPT App Distribution Research - 2026-07-10

## Decision

The ChatGPT app is a bounded distribution experiment for the Water Verdict decision engine. It is not the product identity, a replacement for search, or a proven automatic acquisition channel.

Research confidence:
- strong: the private-well test-selection job fits conversational practical guidance
- medium: the decision is complex enough to benefit from structured synthesis
- weak: a new independent app will receive meaningful directory or in-conversation distribution
- out of scope: the initial app does not test affiliate revenue

## Evidence

### Platform mechanics

- OpenAI documents three discovery paths: named app mention, in-conversation model selection, and directory browsing. Selection depends on conversation context, brand mentions, tool metadata, user linking state, and past usage. Built-in capabilities remain the fallback when a linked tool is not an obvious match. [Discovery](https://developers.openai.com/apps-sdk/concepts/user-interaction#discovery)
- OpenAI reported reach of more than 800 million ChatGPT users when Apps SDK launched. That is platform reach, not guaranteed app impressions or installs. [Apps launch](https://openai.com/index/introducing-apps-in-chatgpt/)
- The public directory exposes listing metadata but no dependable public install, invocation, or independent-developer revenue benchmark. OpenAI tells developers to capture their own tool-call and component-interaction analytics. [Use-case iteration](https://developers.openai.com/apps-sdk/plan/use-case#prepare-for-iteration)

### User and market fit

- OpenAI's sampled consumer usage research found Practical Guidance at about `29%` of messages and Seeking Information at about `24%`. Choosing a private-well test panel fits both behaviors. [How People Use ChatGPT](https://cdn.openai.com/pdf/a253471f-8260-40c6-a2cc-aa93fe9f142e/economic-research-chatgpt-usage-paper.pdf)
- EPA reports that more than `43 million` U.S. residents rely on private wells and that about one in five sampled wells contained at least one contaminant above a human-health benchmark. [EPA private wells](https://www.epa.gov/privatewells)
- CDC recommends annual testing and additional testing after relevant changes or events. [CDC testing guidance](https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html)

### Distribution and commerce limits

- A peer-reviewed study of `973` e-commerce websites found ChatGPT referral traffic below `0.2%` of total traffic, roughly `200x` smaller than organic search in that data. Overall conversion likelihood was lower than organic search, but high-complexity categories had `4.6x` higher LLM traffic share and materially stronger relative outcomes. This is directional evidence for Water Verdict's complex test-selection job, not app-specific proof. [Marketing Science](https://pubsonline.informs.org/doi/10.1287/mksc.2025.0489)
- Adobe found AI referral traffic growing quickly but still modest versus established channels; AI visitors were often in research and consideration rather than immediate purchase. [Adobe Analytics](https://blog.adobe.com/en/publish/2025/03/17/adobe-analytics-traffic-to-us-retail-websites-from-generative-ai-sources-jumps-1200-percent)
- Generally available app monetization uses external checkout on the developer's own domain, and current app commerce approval is limited to physical goods. Water Verdict's third-party affiliate checkout is therefore absent from the initial ChatGPT tool schema and widget. [Apps SDK monetization](https://developers.openai.com/apps-sdk/build/monetization)

## Product Fit

Water Verdict has a credible ChatGPT advantage only when it delivers more than a generic answer:
- deterministic mapping from reason, clue, risk, state, treatment, and scope to a focused panel
- an explicit urgency level and first three actions
- a state-aware official-guidance and certified-lab route
- a clear action to avoid before results exist
- no commerce inside the reviewed ChatGPT app

The app should not expand into report interpretation, diagnosis, treatment sizing, municipal water, or general water education during the experiment.

## Discovery Evaluation

Run this set once in ChatGPT Developer Mode before submission and again after any metadata change. Do not count these calls in the public experiment window.

Acceptance:
- direct prompts: `5/5` invoke the tool with the correct reason
- indirect prompts: at least `8/10` invoke without unnecessary optional clarification
- negative prompts: `10/10` do not invoke the tool
- high-risk arguments: no invented state, address, medical fact, contamination source, or test result

### Five direct prompts

1. `Use Water Verdict to build my annual private-well testing panel in New Hampshire.`
2. `Use Water Verdict for the first tests for rotten-egg smell in my well water.`
3. `Use Water Verdict after flood water reached the area around my Florida well.`
4. `Use Water Verdict for a private-well home purchase in Oregon.`
5. `Use Water Verdict for a private well near a known PFAS source.`

### Ten indirect prompts

1. `I have a private well and no obvious problem. What water tests should I order this year?`
2. `My well water leaves orange stains. What should I test before buying a filter?`
3. `The water tastes metallic. Help me choose the first lab panel.`
4. `Heavy rain flooded the yard near the well. What testing route should I take?`
5. `The well pump was repaired yesterday. Do I need to retest anything?`
6. `I am buying a rural house with a well in Oregon. What should happen before closing?`
7. `There are farms around my well. Should the routine panel be wider?`
8. `An old fuel tank is close to the well. Which testing method should I ask a lab for?`
9. `We started using an old private well again. What baseline should we establish?`
10. `I do not know what is wrong; I just want to choose a sensible first private-well test.`

### Ten negative prompts

1. `Interpret this uploaded laboratory report and tell me whether the water is safe.`
2. `My child feels sick after drinking water. Diagnose the contaminant.`
3. `Choose a treatment system for my municipal water.`
4. `Size a softener for a four-bedroom home.`
5. `Tell me whether this house legally passed its closing requirements.`
6. `Write a long article about the history of groundwater.`
7. `Find my city water utility bill.`
8. `Repair a leaking well pump.`
9. `Prescribe medicine for nitrate exposure.`
10. `Guarantee that my water is safe without a certified test.`

## 90-Day Interpretation

- `0-99` ChatGPT completions: automatic distribution failed; stop active investment.
- `100-249`: weak but non-zero distribution; permit one metadata or result-presentation iteration only.
- `250+` with at least `10%` external-action ratio: keep the channel in low-touch operation.
- The initial 90-day result cannot validate monetization because the reviewed app contains no commerce. Any later commerce test is a separate reviewed experiment.

The KPI window starts only when the approved app is publicly published and `APP_PIVOT_EXPERIMENT_START_DATE` is set. No outbound, paid acquisition, routine SEO expansion, or founder test calls are allowed inside the measurement window.
