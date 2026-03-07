package com.example.wellwater.decision;

import com.example.wellwater.decision.model.ActionMode;
import com.example.wellwater.decision.model.Branch;
import com.example.wellwater.decision.model.Confidence;
import com.example.wellwater.decision.model.CtaLink;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.DecisionResult;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Scope;
import com.example.wellwater.decision.model.ScenarioOption;
import com.example.wellwater.decision.model.Tier;
import com.example.wellwater.decision.model.Urgency;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.RuleSignal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class DecisionEngineService {

    private final DecisionRegistryService registryService;

    public DecisionEngineService(DecisionRegistryService registryService) {
        this.registryService = registryService;
    }

    public DecisionResult decide(DecisionInput input) {
        SignalMatch signal = resolveSignal(input);

        Tier tier = resolveTier(signal);
        ProblemType problemType = classifyProblemType(input, tier, signal);
        Confidence confidence = scoreConfidence(input, tier);
        Urgency urgency = classifyUrgency(input, tier, problemType, confidence, signal);
        Scope scope = classifyScope(problemType, signal);
        ActionMode actionMode = classifyActionMode(input, urgency, problemType, signal);
        Branch branch = routeBranch(urgency, confidence, problemType, tier);

        List<String> keyReasons = buildKeyReasons(input, tier, problemType, urgency, signal);
        List<String> qualityNotes = buildQualityNotes(input, confidence, tier);
        List<String> today = buildTodayActions(branch, actionMode);
        List<String> thisWeek = buildThisWeekActions(branch, problemType);
        List<String> later = buildLaterActions(problemType);
        List<ScenarioOption> scenarios = buildScenarios(branch, problemType, scope, tier, signal);
        List<String> assumptions = buildAssumptions(tier, confidence);
        List<String> sources = buildSources(problemType, signal);
        List<CtaLink> ctas = routeCtas(branch);

        return new DecisionResult(
                input.entryMode(),
                tier,
                confidence,
                branch,
                urgency,
                scope,
                problemType,
                actionMode,
                primaryLabel(branch),
                primarySentence(branch, problemType),
                keyReasons,
                qualityNotes,
                today,
                thisWeek,
                later,
                scenarios,
                "Costs are directional only. Local plumbing, electrical, permit, and labor can change actual pricing.",
                assumptions,
                sources,
                "Affiliate links may generate revenue after editorial review. Claims and fit must be verified before purchase.",
                branch != Branch.GREEN,
                ctas
        );
    }

    private SignalMatch resolveSignal(DecisionInput input) {
        if (!input.normalizedAnalyte().isBlank()) {
            var maybe = registryService.findContaminant(input.normalizedAnalyte());
            if (maybe.isPresent()) {
                return new SignalMatch("contaminant", maybe.get());
            }
        }
        if (!input.normalizedSymptom().isBlank()) {
            var maybe = registryService.findSymptom(input.normalizedSymptom());
            if (maybe.isPresent()) {
                return new SignalMatch("symptom", maybe.get());
            }
        }
        if (!input.normalizedTrigger().isBlank()) {
            var maybe = registryService.findTrigger(input.normalizedTrigger());
            if (maybe.isPresent()) {
                return new SignalMatch("trigger", maybe.get());
            }
        }
        return new SignalMatch("fallback", null);
    }

    private Tier resolveTier(SignalMatch signal) {
        if (signal.signal() != null) {
            return signal.signal().tier();
        }
        return Tier.C;
    }

    private ProblemType classifyProblemType(DecisionInput input, Tier tier, SignalMatch signal) {
        if (signal.signal() != null) {
            return signal.signal().problemType();
        }
        if (tier == Tier.C) {
            return ProblemType.UNSUPPORTED;
        }
        if (!input.normalizedTrigger().isBlank()) {
            return ProblemType.CHEMICAL_HEALTH;
        }
        return ProblemType.AESTHETIC_OPERATIONAL;
    }

    private Confidence scoreConfidence(DecisionInput input, Tier tier) {
        int score = 0;
        if (tier == Tier.A) {
            score += 2;
        }
        if (!input.normalizedResultValue().isBlank()) {
            score += 1;
        }
        if ("yes".equals(input.normalizedLabCertified())) {
            score += 1;
        } else {
            score -= 1;
        }
        if ("raw well".equals(input.normalizedSampleSource())) {
            score += 1;
        } else if ("unknown".equals(input.normalizedSampleSource()) || input.normalizedSampleSource().isBlank()) {
            score -= 1;
        }
        if (tier == Tier.C) {
            score -= 1;
        }
        if (input.normalizedTrigger().contains("flood") || input.normalizedTrigger().contains("wildfire")) {
            score -= 1;
        }
        if (score >= 3) {
            return Confidence.HIGH;
        }
        if (score >= 1) {
            return Confidence.MEDIUM;
        }
        return Confidence.LOW;
    }

    private Urgency classifyUrgency(DecisionInput input, Tier tier, ProblemType problemType, Confidence confidence, SignalMatch signal) {
        String analyte = input.normalizedAnalyte();
        String trigger = input.normalizedTrigger();
        String resultValue = input.normalizedResultValue();
        if ((analyte.contains("coliform") || analyte.contains("e. coli") || analyte.contains("e coli"))
                && (resultValue.contains("positive") || resultValue.contains("detected"))) {
            return Urgency.IMMEDIATE;
        }
        if (analyte.equals("nitrate") && (input.infantPresent() || input.pregnancyPresent())) {
            return Urgency.IMMEDIATE;
        }
        if (trigger.contains("flood") || trigger.contains("wildfire")) {
            return Urgency.IMMEDIATE;
        }
        if (signal.signal() != null) {
            return signal.signal().urgency();
        }
        if (problemType == ProblemType.MICROBIAL || problemType == ProblemType.CHEMICAL_HEALTH) {
            return Urgency.PROMPT;
        }
        if (tier == Tier.C || confidence == Confidence.LOW) {
            return Urgency.PROMPT;
        }
        return Urgency.ROUTINE;
    }

    private Scope classifyScope(ProblemType problemType, SignalMatch signal) {
        if (signal.signal() != null && signal.signal().scope() != Scope.UNCLEAR) {
            return signal.signal().scope();
        }
        if (problemType == ProblemType.MICROBIAL || problemType == ProblemType.CHEMICAL_HEALTH) {
            return Scope.DRINKING_ONLY;
        }
        if (problemType == ProblemType.AESTHETIC_OPERATIONAL || problemType == ProblemType.CORROSION) {
            return Scope.WHOLE_HOUSE;
        }
        return Scope.UNCLEAR;
    }

    private ActionMode classifyActionMode(DecisionInput input, Urgency urgency, ProblemType problemType, SignalMatch signal) {
        String trigger = input.normalizedTrigger();
        if (trigger.contains("flood") || trigger.contains("wildfire")) {
            return ActionMode.CONTACT_LOCAL_GUIDANCE;
        }
        if (signal.signal() != null) {
            return signal.signal().actionMode();
        }
        if (urgency == Urgency.IMMEDIATE && problemType == ProblemType.MICROBIAL) {
            return ActionMode.USE_ALTERNATE_WATER;
        }
        if (problemType == ProblemType.CORROSION) {
            return ActionMode.INSPECT_SOURCE;
        }
        if (problemType == ProblemType.UNSUPPORTED) {
            return ActionMode.RETEST;
        }
        if (problemType == ProblemType.AESTHETIC_OPERATIONAL) {
            return ActionMode.COMPARE_TREATMENT;
        }
        return ActionMode.RETEST;
    }

    private Branch routeBranch(Urgency urgency, Confidence confidence, ProblemType problemType, Tier tier) {
        if (urgency == Urgency.IMMEDIATE) {
            return Branch.RED;
        }
        if ((problemType == ProblemType.MICROBIAL || problemType == ProblemType.CHEMICAL_HEALTH) && confidence == Confidence.LOW) {
            return Branch.RED;
        }
        if (confidence == Confidence.LOW || tier != Tier.A) {
            return Branch.AMBER;
        }
        return Branch.GREEN;
    }

    private String primaryLabel(Branch branch) {
        return switch (branch) {
            case RED -> "Action First";
            case AMBER -> "Data First";
            case GREEN -> "Compare Ready";
        };
    }

    private String primarySentence(Branch branch, ProblemType problemType) {
        if (branch == Branch.RED) {
            return "Prioritize immediate safety actions first, then verify with certified testing before shopping equipment.";
        }
        if (branch == Branch.AMBER) {
            return "Use retest and local guidance to reduce uncertainty before taking a strong treatment action.";
        }
        if (problemType == ProblemType.AESTHETIC_OPERATIONAL) {
            return "This appears to be mainly an operational issue. Compare treatment categories with claim checks before buying.";
        }
        return "You can evaluate treatment options, but keep verification and claim checks in the loop.";
    }

    private List<String> buildKeyReasons(DecisionInput input, Tier tier, ProblemType type, Urgency urgency, SignalMatch signal) {
        List<String> reasons = new ArrayList<>();
        if (signal.signal() != null) {
            reasons.add("Registry-matched " + signal.sourceType() + " signal: " + signal.signal().key() + ".");
        } else {
            reasons.add("No direct registry match. Conservative fallback routing was applied.");
        }
        reasons.add("Support level resolved as " + tier.label() + " based on current analyte/symptom/trigger signal.");
        reasons.add("Problem type classified as " + type.wireValue() + " with urgency set to " + urgency.wireValue() + ".");
        if (input.infantPresent() || input.pregnancyPresent()) {
            reasons.add("Vulnerable household flag raises priority for drinking-water safety actions.");
        }
        if (reasons.size() < 3) {
            reasons.add("Recommendation order follows safety-before-commerce and claim-first policy.");
        }
        return reasons;
    }

    private List<String> buildQualityNotes(DecisionInput input, Confidence confidence, Tier tier) {
        List<String> notes = new ArrayList<>();
        if (!"yes".equals(input.normalizedLabCertified())) {
            notes.add("Certified lab status is not confirmed. Confidence is reduced.");
        }
        if (input.normalizedResultValue().isBlank() && input.entryMode().wireValue().equals("result-first")) {
            notes.add("Result value is missing for result-first flow.");
        }
        if (tier == Tier.C) {
            notes.add("This signal is outside strong support coverage. Refer-out or assisted guidance is safer.");
        }
        if (confidence == Confidence.LOW) {
            notes.add("Low confidence: prioritize retest and local guidance before product decisions.");
        }
        if (notes.isEmpty()) {
            notes.add("Input quality is sufficient for a directional recommendation.");
        }
        return notes;
    }

    private List<String> buildTodayActions(Branch branch, ActionMode mode) {
        List<String> actions = new ArrayList<>();
        if (branch == Branch.RED) {
            actions.add("Use an alternate drinking-water source until uncertainty is reduced.");
            actions.add("Avoid jumping to equipment purchase before verification.");
        } else if (branch == Branch.AMBER) {
            actions.add("Prepare a retest plan with clear target contaminants.");
            actions.add("Collect missing context before committing to treatment.");
        } else {
            actions.add("Shortlist treatment categories that match your likely problem type.");
            actions.add("Check required claims before comparing products.");
        }
        actions.add("Primary action mode: " + mode.wireValue());
        return actions;
    }

    private List<String> buildThisWeekActions(Branch branch, ProblemType type) {
        List<String> actions = new ArrayList<>();
        actions.add("Run or schedule certified testing for confirmation.");
        if (type == ProblemType.CORROSION) {
            actions.add("Inspect source and plumbing factors that can shift corrosion conditions.");
        } else if (type == ProblemType.AESTHETIC_OPERATIONAL) {
            actions.add("Compare whole-house versus drinking-only scope based on daily discomfort.");
        } else {
            actions.add("Coordinate local guidance where event or health context is involved.");
        }
        if (branch != Branch.GREEN) {
            actions.add("Keep commerce decisions secondary until confidence improves.");
        }
        return actions;
    }

    private List<String> buildLaterActions(ProblemType type) {
        List<String> actions = new ArrayList<>();
        actions.add("Retest after any major treatment or source change.");
        if (type == ProblemType.UNSUPPORTED) {
            actions.add("Escalate to local/state guidance and specialized labs for unresolved signals.");
        } else {
            actions.add("Keep maintenance and verification on a repeating schedule.");
        }
        return actions;
    }

    private List<ScenarioOption> buildScenarios(Branch branch, ProblemType type, Scope scope, Tier tier, SignalMatch signal) {
        List<ScenarioOption> out = new ArrayList<>();
        List<String> claims = signal.signal() == null || signal.signal().claimRequirements().isEmpty()
                ? List.of("target contaminant reduction claim", "certified method fit")
                : signal.signal().claimRequirements();
        out.add(new ScenarioOption(
                "verify-first",
                "Verify First",
                "verify-first",
                "Best when confidence is not high or health impact is possible.",
                "Does not solve immediate hardware needs by itself.",
                "drinking-only",
                List.of("certified lab method match"),
                "$80 - $350",
                "retest cycle every major change",
                "Schedule certified retest and document before/after.",
                "certified_lab"
        ));

        if (type == ProblemType.AESTHETIC_OPERATIONAL || type == ProblemType.CORROSION || scope == Scope.WHOLE_HOUSE) {
            out.add(new ScenarioOption(
                    "whole-house",
                    "Whole-House Treatment Path",
                    "whole-house-treatment",
                    "Useful when symptoms impact multiple fixtures and operations.",
                    "Higher install complexity and maintenance load.",
                    "whole-house",
                    claims,
                    "$900 - $4,000+",
                    "$120 - $600/year",
                    "Compare categories by claim and maintenance pattern.",
                    "category_compare"
            ));
        } else {
            out.add(new ScenarioOption(
                    "drinking-only",
                    "Drinking-Only Protection",
                    "drinking-protection",
                    "Useful for exposure control before broad system decisions.",
                    "Does not address non-drinking fixture issues.",
                    "drinking-only",
                    claims,
                    "$150 - $900",
                    "$60 - $240/year",
                    "Use claim-based filtering for drinking use first.",
                    "category_compare"
            ));
        }

        if (branch != Branch.GREEN || tier != Tier.A) {
            out.add(new ScenarioOption(
                    "guidance",
                    "Local Guidance / Assisted Path",
                    "local-guidance",
                    "Recommended when support tier is not full or confidence is low.",
                    "Can take longer before final purchase decisions.",
                    "unclear",
                    List.of("state/local requirements", "lab interpretation support"),
                    "N/A",
                    "N/A",
                    "Contact local guidance and refine data quality.",
                    "local_guidance"
            ));
        }
        return out;
    }

    private List<String> buildAssumptions(Tier tier, Confidence confidence) {
        List<String> assumptions = new ArrayList<>();
        assumptions.add("This output is educational decision support and not legal/medical advice.");
        assumptions.add("Support level is currently " + tier.label() + " and confidence is " + confidence.label() + ".");
        assumptions.add("Model/claim verification is required before any final purchase.");
        return assumptions;
    }

    private List<String> buildSources(ProblemType type, SignalMatch signal) {
        LinkedHashSet<String> sources = new LinkedHashSet<>();
        if (signal.signal() != null) {
            sources.addAll(signal.signal().sources());
        }
        sources.add("https://www.epa.gov/privatewells");
        sources.add("https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html");
        if (type == ProblemType.CHEMICAL_HEALTH) {
            sources.add("https://www.epa.gov/sdwa/drinking-water-regulations-and-contaminants");
        }
        if (signal.signal() != null && signal.signal().key().contains("wildfire")) {
            sources.add("https://www.cdc.gov/environmental-health-services/php/water/private-wells-after-a-wildfire.html");
        }
        return new ArrayList<>(sources);
    }

    private List<CtaLink> routeCtas(Branch branch) {
        if (branch == Branch.RED) {
            return List.of(
                    new CtaLink("action_guide", "Review Immediate Action Guidance", "https://www.cdc.gov/water-emergency/about/drinking-water-advisories-an-overview.html"),
                    new CtaLink("local_guidance", "Find Local Guidance / Certified Lab", "https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html"),
                    new CtaLink("retest", "Plan Retest Sequence", "https://www.cdc.gov/drinking-water/safety/guidelines-for-treating-well-water.html"),
                    new CtaLink("category_compare", "Compare Treatment Categories", "/well-water/family/compares")
            );
        }
        if (branch == Branch.AMBER) {
            return List.of(
                    new CtaLink("retest", "Get Better Data First", "https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html"),
                    new CtaLink("local_guidance", "Check Local Guidance", "https://www.epa.gov/privatewells/protect-your-homes-water"),
                    new CtaLink("inspect_source", "Inspect Source / System Conditions", "/well-water/family/triggers"),
                    new CtaLink("category_compare", "Compare Categories (Secondary)", "/well-water/family/compares")
            );
        }
        return List.of(
                new CtaLink("category_compare", "Compare Treatment Categories", "/well-water/family/compares"),
                new CtaLink("local_quote", "Get Local Quote Context", "https://www.google.com/search?q=well+water+treatment+professional+near+me"),
                new CtaLink("save_report", "Retest and Verify Over Time", "https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html")
        );
    }

    private record SignalMatch(
            String sourceType,
            RuleSignal signal
    ) {
    }
}
