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
import com.example.wellwater.decision.normalize.DecisionInputNormalizationService;
import com.example.wellwater.decision.normalize.DecisionNormalizedInput;
import com.example.wellwater.decision.normalize.QualifierType;
import com.example.wellwater.decision.normalize.SampleFreshness;
import com.example.wellwater.decision.registry.CostProfile;
import com.example.wellwater.decision.registry.CostRegistryService;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.RuleSignal;
import com.example.wellwater.decision.registry.StateResource;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class DecisionEngineService {

    private final DecisionRegistryService registryService;
    private final DecisionInputNormalizationService normalizationService;
    private final CostRegistryService costRegistryService;
    private final StateResourceRegistryService stateResourceRegistryService;

    public DecisionEngineService(
            DecisionRegistryService registryService,
            DecisionInputNormalizationService normalizationService,
            CostRegistryService costRegistryService,
            StateResourceRegistryService stateResourceRegistryService
    ) {
        this.registryService = registryService;
        this.normalizationService = normalizationService;
        this.costRegistryService = costRegistryService;
        this.stateResourceRegistryService = stateResourceRegistryService;
    }

    public DecisionResult decide(DecisionInput input) {
        DecisionNormalizedInput normalized = normalizationService.normalize(input);
        SignalMatch signal = resolveSignal(normalized);

        Tier tier = resolveTier(input, normalized, signal);
        ProblemType problemType = classifyProblemType(input, tier, signal);
        Confidence confidence = scoreConfidence(input, tier, normalized);
        Urgency urgency = classifyUrgency(input, problemType, signal, normalized);
        Scope scope = classifyScope(input, problemType, signal);
        ActionMode actionMode = classifyActionMode(input, urgency, problemType, signal);
        Branch branch = routeBranch(urgency, confidence, problemType, tier);

        Optional<StateResource> stateResource = stateResourceRegistryService.findByState(normalized.stateKey());
        Optional<CostProfile> costProfile = costRegistryService.find(problemType, scope);
        String installRange = costProfile.map(CostProfile::installRange).orElse("N/A");
        String maintenanceRange = costProfile.map(CostProfile::maintenanceRange).orElse("N/A");
        String costNote = buildCostNote(costProfile);

        List<String> keyReasons = buildKeyReasons(input, tier, problemType, urgency, signal, normalized);
        List<String> qualityNotes = buildQualityNotes(input, confidence, tier, normalized);
        List<String> today = buildTodayActions(branch, actionMode);
        List<String> thisWeek = buildThisWeekActions(branch, problemType, stateResource);
        List<String> later = buildLaterActions(problemType);
        List<ScenarioOption> scenarios = buildScenarios(branch, problemType, scope, tier, signal, installRange, maintenanceRange);
        List<String> assumptions = buildAssumptions(tier, confidence);
        List<String> sources = buildSources(problemType, signal, stateResource, costProfile);
        List<CtaLink> ctas = routeCtas(branch, stateResource);

        String localGuidanceUrl = stateResource.map(StateResource::localGuidanceUrl)
                .filter(url -> !url.isBlank())
                .orElse("https://www.epa.gov/privatewells/protect-your-homes-water");
        String certifiedLabUrl = stateResource.map(StateResource::certifiedLabUrl)
                .filter(url -> !url.isBlank())
                .orElse("https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html");

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
                normalized.sampleFreshness().wireValue(),
                normalized.completenessScore(),
                costNote,
                installRange,
                maintenanceRange,
                localGuidanceUrl,
                certifiedLabUrl,
                assumptions,
                sources,
                "Affiliate links may generate revenue after editorial review. Claims and fit must be verified before purchase.",
                branch != Branch.GREEN,
                ctas
        );
    }

    private SignalMatch resolveSignal(DecisionNormalizedInput normalized) {
        if (!normalized.analyteKey().isBlank()) {
            var maybe = registryService.findContaminant(normalized.analyteKey());
            if (maybe.isPresent()) {
                return new SignalMatch("contaminant", maybe.get());
            }
        }
        if (!normalized.symptomKey().isBlank()) {
            var maybe = registryService.findSymptom(normalized.symptomKey());
            if (maybe.isPresent()) {
                return new SignalMatch("symptom", maybe.get());
            }
        }
        if (!normalized.triggerKey().isBlank()) {
            var maybe = registryService.findTrigger(normalized.triggerKey());
            if (maybe.isPresent()) {
                return new SignalMatch("trigger", maybe.get());
            }
        }
        return new SignalMatch("fallback", null);
    }

    private Tier resolveTier(DecisionInput input, DecisionNormalizedInput normalized, SignalMatch signal) {
        if (input.entryMode().wireValue().equals("result-first")) {
            if (!normalized.unitSupported()) {
                return Tier.C;
            }
            if (normalized.sampleFreshness() == SampleFreshness.STALE) {
                return Tier.C;
            }
        }
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

    private Confidence scoreConfidence(DecisionInput input, Tier tier, DecisionNormalizedInput normalized) {
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
        if (normalized.sampleFreshness() == SampleFreshness.AGING) {
            score -= 1;
        } else if (normalized.sampleFreshness() == SampleFreshness.STALE) {
            score -= 2;
        }
        if (!normalized.unitSupported()) {
            score -= 2;
        }
        if (normalized.qualifierType() == QualifierType.UNKNOWN) {
            score -= 1;
        }
        if (normalized.completenessScore() < 70) {
            score -= 1;
        }
        if (tier == Tier.C) {
            score -= 1;
        }
        if (input.normalizedTrigger().contains("flood") || input.normalizedTrigger().contains("wildfire")) {
            score -= 1;
        }
        if ("treated tap".equals(input.normalizedSampleSource()) && !input.normalizedExistingTreatment().isBlank()) {
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

    private Urgency classifyUrgency(DecisionInput input, ProblemType problemType, SignalMatch signal, DecisionNormalizedInput normalized) {
        String analyte = normalized.analyteKey();
        String trigger = normalized.triggerKey();

        if ((analyte.contains("coliform") || analyte.contains("e. coli") || analyte.contains("e coli"))
                && normalized.qualifierType() == QualifierType.POSITIVE) {
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
        if (normalized.sampleFreshness() == SampleFreshness.STALE) {
            return Urgency.PROMPT;
        }
        return Urgency.ROUTINE;
    }

    private Scope classifyScope(DecisionInput input, ProblemType problemType, SignalMatch signal) {
        if (!input.normalizedUseScope().isBlank()) {
            return switch (input.normalizedUseScope()) {
                case "drinking-only", "drinking only" -> Scope.DRINKING_ONLY;
                case "whole-house", "whole house" -> Scope.WHOLE_HOUSE;
                case "both" -> Scope.BOTH;
                default -> Scope.UNCLEAR;
            };
        }
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

    private List<String> buildKeyReasons(
            DecisionInput input,
            Tier tier,
            ProblemType type,
            Urgency urgency,
            SignalMatch signal,
            DecisionNormalizedInput normalized
    ) {
        List<String> reasons = new ArrayList<>();
        if (signal.signal() != null) {
            reasons.add("Registry-matched " + signal.sourceType() + " signal: " + signal.signal().key() + ".");
        } else {
            reasons.add("No direct registry match. Conservative fallback routing was applied.");
        }
        reasons.add("Support level resolved as " + tier.label() + " with completeness score " + normalized.completenessScore() + ".");
        reasons.add("Problem type classified as " + type.wireValue() + " with urgency set to " + urgency.wireValue() + ".");
        if (input.infantPresent() || input.pregnancyPresent()) {
            reasons.add("Vulnerable household flag raises priority for drinking-water safety actions.");
        }
        return reasons;
    }

    private List<String> buildQualityNotes(
            DecisionInput input,
            Confidence confidence,
            Tier tier,
            DecisionNormalizedInput normalized
    ) {
        List<String> notes = new ArrayList<>();
        if (!normalized.unitSupported() && input.entryMode().wireValue().equals("result-first")) {
            notes.add("Unit is missing or unsupported for this analyte. Tier is downgraded.");
        }
        if (normalized.sampleFreshness() == SampleFreshness.STALE) {
            notes.add("Sample is stale. Retest is required before strong recommendations.");
        } else if (normalized.sampleFreshness() == SampleFreshness.AGING) {
            notes.add("Sample is aging. Confirm with a newer sample if risk context is high.");
        }
        if (!"yes".equals(input.normalizedLabCertified())) {
            notes.add("Certified lab status is not confirmed. Confidence is reduced.");
        }
        if (normalized.qualifierType() == QualifierType.UNKNOWN) {
            notes.add("Result qualifier is unclear. Interpret with caution.");
        }
        if (tier == Tier.C) {
            notes.add("This signal is outside strong support coverage. Refer-out or assisted guidance is safer.");
        }
        if (confidence == Confidence.LOW) {
            notes.add("Low confidence: prioritize retest and local guidance before product decisions.");
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

    private List<String> buildThisWeekActions(Branch branch, ProblemType type, Optional<StateResource> stateResource) {
        List<String> actions = new ArrayList<>();
        actions.add("Run or schedule certified testing for confirmation.");
        if (type == ProblemType.CORROSION) {
            actions.add("Inspect source and plumbing factors that can shift corrosion conditions.");
        } else if (type == ProblemType.AESTHETIC_OPERATIONAL) {
            actions.add("Compare whole-house versus drinking-only scope based on daily discomfort.");
        } else {
            actions.add("Coordinate local guidance where event or health context is involved.");
        }
        stateResource.ifPresent(resource -> {
            if (!resource.localGuidanceUrl().isBlank()) {
                actions.add("State guidance available: " + resource.localGuidanceUrl());
            }
        });
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

    private List<ScenarioOption> buildScenarios(
            Branch branch,
            ProblemType type,
            Scope scope,
            Tier tier,
            SignalMatch signal,
            String installRange,
            String maintenanceRange
    ) {
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

        out.add(new ScenarioOption(
                scope == Scope.WHOLE_HOUSE ? "whole-house" : "drinking-only",
                scope == Scope.WHOLE_HOUSE ? "Whole-House Treatment Path" : "Drinking-Only Protection",
                scope == Scope.WHOLE_HOUSE ? "whole-house-treatment" : "drinking-protection",
                "Use claim-based selection tied to your current problem type and support level.",
                "Do not skip verification and local guidance when confidence is low.",
                scope.wireValue(),
                claims,
                installRange,
                maintenanceRange,
                "Compare categories by claim and maintenance profile.",
                "category_compare"
        ));

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
        return List.of(
                "This output is educational decision support and not legal/medical advice.",
                "Support level is currently " + tier.label() + " and confidence is " + confidence.label() + ".",
                "Model/claim verification is required before any final purchase."
        );
    }

    private String buildCostNote(Optional<CostProfile> profile) {
        if (profile.isPresent()) {
            return "Directional estimate from registry. Install: " + profile.get().installRange()
                    + ", maintenance: " + profile.get().maintenanceRange()
                    + ". Local labor, permit, and system complexity can change actual pricing.";
        }
        return "Costs are directional only. Local plumbing, electrical, permit, and labor can change actual pricing.";
    }

    private List<String> buildSources(
            ProblemType type,
            SignalMatch signal,
            Optional<StateResource> stateResource,
            Optional<CostProfile> costProfile
    ) {
        LinkedHashSet<String> sources = new LinkedHashSet<>();
        if (signal.signal() != null) {
            sources.addAll(signal.signal().sources());
        }
        stateResource.map(StateResource::sourceUrl)
                .filter(url -> !url.isBlank())
                .ifPresent(sources::add);
        costProfile.map(CostProfile::sourceUrl)
                .filter(url -> !url.isBlank())
                .ifPresent(sources::add);

        sources.add("https://www.epa.gov/privatewells");
        sources.add("https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html");
        if (type == ProblemType.CHEMICAL_HEALTH) {
            sources.add("https://www.epa.gov/sdwa/drinking-water-regulations-and-contaminants");
        }
        return new ArrayList<>(sources);
    }

    private List<CtaLink> routeCtas(Branch branch, Optional<StateResource> stateResource) {
        String localGuidanceUrl = stateResource.map(StateResource::localGuidanceUrl)
                .filter(url -> !url.isBlank())
                .orElse("https://www.epa.gov/privatewells/protect-your-homes-water");
        String certifiedLabUrl = stateResource.map(StateResource::certifiedLabUrl)
                .filter(url -> !url.isBlank())
                .orElse("https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html");

        if (branch == Branch.RED) {
            return List.of(
                    new CtaLink("action_guide", "Review Immediate Action Guidance", "https://www.cdc.gov/water-emergency/about/drinking-water-advisories-an-overview.html"),
                    new CtaLink("local_guidance", "Check State / Local Guidance", localGuidanceUrl),
                    new CtaLink("certified_lab", "Find Certified Lab", certifiedLabUrl),
                    new CtaLink("category_compare", "Compare Treatment Categories (After Safety Steps)", "/well-water/family/compares")
            );
        }
        if (branch == Branch.AMBER) {
            return List.of(
                    new CtaLink("retest", "Get Better Data First", certifiedLabUrl),
                    new CtaLink("local_guidance", "Check State / Local Guidance", localGuidanceUrl),
                    new CtaLink("inspect_source", "Inspect Source / System Conditions", "/well-water/family/triggers"),
                    new CtaLink("category_compare", "Compare Categories (Secondary)", "/well-water/family/compares")
            );
        }
        return List.of(
                new CtaLink("category_compare", "Compare Treatment Categories", "/well-water/family/compares"),
                new CtaLink("local_quote", "Get Local Quote Context", "https://www.google.com/search?q=well+water+treatment+professional+near+me"),
                new CtaLink("save_report", "Retest and Verify Over Time", certifiedLabUrl)
        );
    }

    private record SignalMatch(
            String sourceType,
            RuleSignal signal
    ) {
    }
}

