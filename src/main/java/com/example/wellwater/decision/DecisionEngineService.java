package com.example.wellwater.decision;

import com.example.wellwater.decision.model.ActionMode;
import com.example.wellwater.decision.model.Branch;
import com.example.wellwater.decision.model.Confidence;
import com.example.wellwater.decision.model.CtaLink;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.DecisionReportLine;
import com.example.wellwater.decision.model.DecisionReportLineSummary;
import com.example.wellwater.decision.model.DecisionResult;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.RecommendedTestCard;
import com.example.wellwater.decision.model.Scope;
import com.example.wellwater.decision.model.ScenarioOption;
import com.example.wellwater.decision.model.SoftenerSizingPreview;
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

    private static final String DECISION_VERSION = "decision-engine-v0.3.0";

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
        Confidence confidence = scoreConfidence(input, tier, normalized, signal);
        Urgency urgency = classifyUrgency(input, problemType, signal, normalized);
        Scope scope = classifyScope(input, problemType, signal);
        ActionMode actionMode = classifyActionMode(input, urgency, problemType, signal, normalized);
        Branch branch = routeBranch(urgency, confidence, problemType, tier, normalized);

        Optional<StateResource> stateResource = stateResourceRegistryService.findByState(normalized.stateKey());
        Optional<CostProfile> costProfile = costRegistryService.find(problemType, scope);
        String installRange = costProfile.map(CostProfile::installRange).orElse("N/A");
        String maintenanceRange = costProfile.map(CostProfile::maintenanceRange).orElse("N/A");
        String costNote = buildCostNote(costProfile);

        List<String> supportingSignalsUsed = supportingSignalsUsed(input, normalized, signal);
        List<String> keyReasons = buildKeyReasons(input, tier, problemType, urgency, signal, normalized, supportingSignalsUsed);
        List<String> qualityNotes = buildQualityNotes(input, confidence, tier, signal, normalized);
        List<String> today = buildTodayActions(branch, actionMode);
        List<String> thisWeek = buildThisWeekActions(branch, problemType, stateResource);
        List<String> later = buildLaterActions(problemType);
        List<RecommendedTestCard> recommendedTestCards = buildRecommendedTestCards(input, problemType, signal, normalized, stateResource, supportingSignalsUsed);
        List<String> recommendedTests = recommendedTestCards.stream()
                .map(RecommendedTestCard::summary)
                .toList();
        String recommendedTestOrderNote = buildRecommendedTestOrderNote(normalized.triggerKey(), supportingSignalsUsed);
        List<String> compareReadinessChecks = buildCompareReadinessChecks(input, problemType, signal, normalized, branch, supportingSignalsUsed);
        List<DecisionReportLineSummary> reportLinesReviewed = buildReportLineSummaries(input, normalized, signal);
        List<ScenarioOption> scenarios = buildScenarios(branch, problemType, scope, tier, signal, installRange, maintenanceRange);
        SoftenerSizingPreview softenerSizingPreview = buildSoftenerSizingPreview(input, problemType, signal, normalized, branch, supportingSignalsUsed);
        List<String> assumptions = buildAssumptions(tier, confidence);
        List<String> sources = buildSources(problemType, signal, stateResource, costProfile);
        List<CtaLink> ctas = routeCtas(branch, stateResource, problemType, signal, normalized, input, supportingSignalsUsed);

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
                recommendedTests,
                recommendedTestCards,
                recommendedTestOrderNote,
                compareReadinessChecks,
                reportLinesReviewed,
                supportingSignalsUsed,
                scenarios,
                softenerSizingPreview,
                normalized.sampleFreshness().wireValue(),
                normalized.completenessScore(),
                costNote,
                installRange,
                maintenanceRange,
                localGuidanceUrl,
                certifiedLabUrl,
                DECISION_VERSION,
                signal.signal() == null ? "" : signal.signal().sourceVersion(),
                normalized.thresholdSummary(),
                assumptions,
                sources,
                "Commercial links stay behind testing, verification, and editorial review. Claims and fit must be verified before any purchase path.",
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
        if ("chemical".equals(input.normalizedSmellType())) {
            return ProblemType.CHEMICAL_HEALTH;
        }
        if ("metallic".equals(input.normalizedTasteType()) || "blue-green".equals(input.normalizedStainType())) {
            return ProblemType.CORROSION;
        }
        if ("rotten-egg".equals(input.normalizedSmellType())
                || "orange".equals(input.normalizedStainType())
                || "black".equals(input.normalizedStainType())
                || "salty".equals(input.normalizedTasteType())
                || "bitter".equals(input.normalizedTasteType())) {
            return ProblemType.AESTHETIC_OPERATIONAL;
        }
        if (tier == Tier.C) {
            return ProblemType.UNSUPPORTED;
        }
        if (!input.normalizedTrigger().isBlank()) {
            return ProblemType.CHEMICAL_HEALTH;
        }
        return ProblemType.AESTHETIC_OPERATIONAL;
    }

    private Confidence scoreConfidence(DecisionInput input, Tier tier, DecisionNormalizedInput normalized, SignalMatch signal) {
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
        if (normalized.canonicalNumericResultValue() != null
                && normalized.unitSupported()
                && !"treated tap".equals(input.normalizedSampleSource())) {
            score += 1;
        }
        if ("raw well".equals(input.normalizedSampleSource())) {
            score += 1;
        } else if ("unknown".equals(input.normalizedSampleSource()) || input.normalizedSampleSource().isBlank()) {
            score -= 1;
        }
        if (hasSecondaryContext(input)) {
            score += 1;
        }
        if (usesDerivedSignal(input, normalized, signal)) {
            score += 1;
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
        if ("treated tap".equals(input.normalizedSampleSource()) && input.normalizedExistingTreatments().size() > 1) {
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
        if ("chemical".equals(input.normalizedSmellType()) && "sudden".equals(input.normalizedChangeTiming())) {
            return Urgency.PROMPT;
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
        if ("whole-house".equals(input.normalizedLocationScope()) || "whole house".equals(input.normalizedLocationScope())) {
            return Scope.WHOLE_HOUSE;
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

    private ActionMode classifyActionMode(
            DecisionInput input,
            Urgency urgency,
            ProblemType problemType,
            SignalMatch signal,
            DecisionNormalizedInput normalized
    ) {
        String trigger = input.normalizedTrigger();
        if (trigger.contains("flood") || trigger.contains("wildfire")) {
            return ActionMode.CONTACT_LOCAL_GUIDANCE;
        }
        if (trigger.contains("retest-after-treatment")) {
            return ActionMode.RETEST;
        }
        if (normalized.thresholdTriggered() && problemType == ProblemType.CHEMICAL_HEALTH) {
            return ActionMode.RETEST;
        }
        if ("treated tap".equals(input.normalizedSampleSource()) && !input.normalizedExistingTreatments().isEmpty()) {
            return ActionMode.RETEST;
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

    private Branch routeBranch(
            Urgency urgency,
            Confidence confidence,
            ProblemType problemType,
            Tier tier,
            DecisionNormalizedInput normalized
    ) {
        if (urgency == Urgency.IMMEDIATE) {
            return Branch.RED;
        }
        if ((problemType == ProblemType.MICROBIAL || problemType == ProblemType.CHEMICAL_HEALTH) && confidence == Confidence.LOW) {
            return Branch.RED;
        }
        if (normalized.thresholdTriggered()
                && (problemType == ProblemType.CHEMICAL_HEALTH || problemType == ProblemType.CORROSION)) {
            return Branch.AMBER;
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
            DecisionNormalizedInput normalized,
            List<String> supportingSignalsUsed
    ) {
        List<String> reasons = new ArrayList<>();
        if (signal.signal() != null) {
            reasons.add("Registry-matched " + signal.sourceType() + " signal: " + signal.signal().key() + ".");
        } else {
            reasons.add("No direct registry match. Conservative fallback routing was applied.");
        }
        if (!supportingSignalsUsed.isEmpty()) {
            reasons.add("Supporting report or context signals used: " + joinHumanizedSignals(supportingSignalsUsed) + ".");
        }
        reasons.add("Support level resolved as " + tier.label() + " with completeness score " + normalized.completenessScore() + ".");
        reasons.add("Problem type classified as " + type.wireValue() + " with urgency set to " + urgency.wireValue() + ".");
        if (normalized.thresholdTriggered() && !normalized.thresholdSummary().isBlank()) {
            reasons.add("Registry threshold check: " + normalized.thresholdSummary() + ".");
        }
        if (usesDerivedSignal(input, normalized, signal)) {
            reasons.add("Secondary context from smell, stain, taste, or timing was used to strengthen signal matching.");
        }
        if (!input.normalizedLocationScope().isBlank() && scopeHintSupportsWholeHouse(input, type)) {
            reasons.add("Issue location context suggests a broader whole-house pattern.");
        }
        if (input.infantPresent() || input.pregnancyPresent()) {
            reasons.add("Vulnerable household flag raises priority for drinking-water safety actions.");
        }
        return reasons;
    }

    private List<String> buildQualityNotes(
            DecisionInput input,
            Confidence confidence,
            Tier tier,
            SignalMatch signal,
            DecisionNormalizedInput normalized
    ) {
        List<String> notes = new ArrayList<>();
        if (!normalized.unitSupported() && input.entryMode().wireValue().equals("result-first")) {
            notes.add("Unit is missing or unsupported for this analyte. Tier is downgraded.");
        }
        if (normalized.unitConverted() && normalized.canonicalNumericResultValue() != null) {
            notes.add("Entered unit was converted to canonical registry unit " + normalized.canonicalUnit() + " for threshold comparison.");
        }
        if (!normalized.thresholdTriggered() && !normalized.thresholdSummary().isBlank()) {
            notes.add("Threshold check completed: " + normalized.thresholdSummary() + ".");
        }
        if (normalized.sampleFreshness() == SampleFreshness.STALE) {
            notes.add("Sample is stale. Retest is required before strong recommendations.");
        } else if (normalized.sampleFreshness() == SampleFreshness.AGING) {
            notes.add("Sample is aging. Confirm with a newer sample if risk context is high.");
        }
        if (!"yes".equals(input.normalizedLabCertified())) {
            notes.add("Certified lab status is not confirmed. Confidence is reduced.");
        }
        if ("treated tap".equals(input.normalizedSampleSource()) && input.normalizedExistingTreatments().size() > 1) {
            notes.add("Multiple active treatments are already in place on a treated tap sample. Verify raw-vs-treated context before strong conclusions.");
        }
        if ("yes".equals(input.normalizedLabCertified()) && input.normalizedLabName().isBlank()) {
            notes.add("Lab is marked certified, but lab identity is missing. Keep the paper trail for later verification.");
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
        if (signal.signal() != null && !signal.signal().sourceVersion().isBlank()) {
            notes.add("Registry source version: " + signal.signal().sourceVersion() + ".");
        }
        notes.add("Decision rules version: " + DECISION_VERSION + ".");
        return notes;
    }

    private boolean hasSecondaryContext(DecisionInput input) {
        return !input.normalizedSmellType().isBlank()
                || !input.normalizedStainType().isBlank()
                || !input.normalizedTasteType().isBlank()
                || !input.normalizedLocationScope().isBlank()
                || !input.normalizedChangeTiming().isBlank();
    }

    private boolean usesDerivedSignal(DecisionInput input, DecisionNormalizedInput normalized, SignalMatch signal) {
        if (signal.signal() == null) {
            return false;
        }
        boolean derivedSymptom = input.normalizedSymptom().isBlank() && !normalized.symptomKey().isBlank();
        boolean derivedTrigger = input.normalizedTrigger().isBlank() && !normalized.triggerKey().isBlank();
        return derivedSymptom || derivedTrigger;
    }

    private boolean scopeHintSupportsWholeHouse(DecisionInput input, ProblemType type) {
        String locationScope = input.normalizedLocationScope();
        return ("whole-house".equals(locationScope) || "whole house".equals(locationScope))
                && (type == ProblemType.AESTHETIC_OPERATIONAL || type == ProblemType.CORROSION);
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
                actions.add("Check the matching state or local guidance in the links section before you compare or retest.");
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

    private List<RecommendedTestCard> buildRecommendedTestCards(
            DecisionInput input,
            ProblemType type,
            SignalMatch signal,
            DecisionNormalizedInput normalized,
            Optional<StateResource> stateResource,
            List<String> supportingSignalsUsed
    ) {
        LinkedHashSet<RecommendedTestCard> tests = new LinkedHashSet<>();
        String signalKey = resolvedSignalKey(input, normalized, signal);
        String triggerKey = normalized.triggerKey();

        if (input.entryMode() == com.example.wellwater.decision.model.EntryMode.RESULT_FIRST) {
            tests.add(testCard(
                    "Report-context confirmation",
                    "One analyte should not be treated like a full household verdict until the sample date, sampling point, and unit are all clear.",
                    "Confirm where the sample came from, whether it was raw or treated water, and whether the unit matches the lab report."
            ));
        }

        if (triggerKey.contains("flood")) {
            tests.add(testCard(
                    "Post-flood certified bacteria retest",
                    "Flood response changes the risk profile fast enough that pre-flood baselines stop being decision-grade.",
                    "Use a recent untreated sample after flood-response steps and follow the state/local disinfection and retest order.",
                    stateAwareGuidanceLabel(stateResource),
                    stateAwareGuidanceUrl(stateResource)
            ));
        }
        if (triggerKey.contains("repair") || triggerKey.contains("heavy-rain")) {
            tests.add(testCard(
                    "Event-window retest",
                    "Rain and repair events can move bacteria and disturbance-sensitive results enough to make the earlier baseline misleading.",
                    "Repeat key bacteria or disturbance-sensitive lines on a recent untreated sample after the event window closes.",
                    stateAwareLabLabel(stateResource),
                    stateAwareLabUrl(stateResource)
            ));
        }
        if (triggerKey.contains("home-purchase")) {
            tests.add(testCard(
                    "Home-purchase certified panel",
                    "A sale decision needs a broader panel than a single symptom or analyte because liability and timing are higher.",
                    "Order a certified panel matched to state requirements, local geology, and any infant or pregnancy exposure concerns.",
                    stateAwareGuidanceLabel(stateResource),
                    stateAwareGuidanceUrl(stateResource)
            ));
        }
        if (isFloodDriven(triggerKey, supportingSignalsUsed) || isRainOrRepairDriven(triggerKey, supportingSignalsUsed)) {
            tests.add(testCard(
                    "Recent untreated follow-up sample",
                    "Event context means the report is timing-sensitive and should not be treated as a stable basis for equipment choice.",
                    "Get a fresh untreated sample before comparing treatment classes or sizing nuisance equipment."
            ));
        }

        switch (type) {
            case MICROBIAL -> {
                tests.add(testCard(
                        "Certified total coliform / E. coli retest",
                        "Microbial signals need verification before they are treated like maintenance or odor-only problems.",
                        "Use a certified untreated sample and document any recent shock, repair, or disinfection steps.",
                        stateAwareLabLabel(stateResource),
                        stateAwareLabUrl(stateResource)
                ));
                tests.add(testCard(
                        "Sampling and wellhead review",
                        "Sampling mistakes and recent well work can change microbial interpretation enough to alter the action path.",
                        "Check the sampling sequence, faucet prep, and recent repair or wellhead disturbance before comparing UV or chlorination.",
                        stateAwareGuidanceLabel(stateResource),
                        stateAwareGuidanceUrl(stateResource)
                ));
            }
            case CHEMICAL_HEALTH -> {
                if (signalKey.contains("nitrate") || signalKey.contains("nitrite")) {
                    tests.add(testCard(
                            "Certified nitrate confirmation",
                            "Nitrate is a drinking-water protection issue and can move with rain, flooding, or treatment changes.",
                            "Use a certified sample from the untreated drinking-water point and repeat after major weather or treatment changes.",
                            stateAwareLabLabel(stateResource),
                            stateAwareLabUrl(stateResource)
                    ));
                    if (input.infantPresent() || input.pregnancyPresent()) {
                        tests.add(testCard(
                                "Infant / pregnancy exposure check",
                                "Vulnerable-household context keeps the decision in drinking-water protection mode instead of nuisance or convenience mode.",
                                "Keep the test plan focused on the drinking-water point of use until infant or pregnancy exposure questions are resolved."
                        ));
                    }
                    if (hasAnySignal(supportingSignalsUsed, "total coliform", "e. coli", "after-heavy-rain", "after-flood")) {
                        tests.add(testCard(
                                "Broader contamination follow-up",
                                "Nitrate plus bacteria or event signals means the problem may not be isolated to a single chemical line.",
                                "Pair nitrate confirmation with bacteria-focused follow-up and recent-event context on the same untreated source."
                        ));
                    }
                } else if (signalKey.contains("arsenic")) {
                    tests.add(testCard(
                            "Certified arsenic confirmation",
                            "Arsenic decisions should stay on verified untreated data before narrowing into treatment class comparisons.",
                            "Use a certified untreated sample and keep raw-vs-treated sampling explicit in the lab order.",
                            stateAwareLabLabel(stateResource),
                            stateAwareLabUrl(stateResource)
                    ));
                    tests.add(testCard(
                            "Bedrock follow-up panel check",
                            "Bedrock context can make radionuclide or uranium follow-up part of the same decision, not a separate later surprise.",
                            "Ask whether uranium or radionuclide follow-up belongs in the same test plan for your state and geology.",
                            stateAwareGuidanceLabel(stateResource),
                            stateAwareGuidanceUrl(stateResource)
                    ));
                    if (hasAnySignal(supportingSignalsUsed, "radium", "radon")) {
                        tests.add(testCard(
                                "Combined arsenic + radionuclide panel review",
                                "Radionuclide context means a single-analyte arsenic retest may under-scope the next decision.",
                                "Ask the lab or state guidance whether the next panel should cover arsenic plus radionuclide follow-up together."
                        ));
                    }
                } else if (signalKey.contains("pfas")) {
                    tests.add(testCard(
                            "Certified PFAS panel",
                            "PFAS treatment claims are easy to misread when the sampling point is unclear or not certified.",
                            "Use a certified PFAS panel and document whether the sample is raw well water or post-treatment water.",
                            stateAwareLabLabel(stateResource),
                            stateAwareLabUrl(stateResource)
                    ));
                } else if (signalKey.contains("radon") || signalKey.contains("radium")) {
                    tests.add(testCard(
                            "State-aware radionuclide lab path",
                            "Radionuclide scope and interpretation vary enough that state guidance should shape the next panel.",
                            "Use the state-certified radionuclide path before comparing aeration or GAC treatment classes.",
                            stateAwareGuidanceLabel(stateResource),
                            stateAwareGuidanceUrl(stateResource)
                    ));
                } else {
                    tests.add(testCard(
                            "Certified analyte-specific follow-up",
                            "This is still a health-oriented contaminant path, so the next test should match the analyte and benchmark exactly.",
                            "Verify that the lab method, unit, and benchmark line up with the contaminant you entered.",
                            stateAwareLabLabel(stateResource),
                            stateAwareLabUrl(stateResource)
                    ));
                }
            }
            case CORROSION -> {
                tests.add(testCard(
                        "Corrosion-focused follow-up set",
                        "pH by itself is not enough when lead, copper, staining, or metallic-taste clues suggest plumbing interaction.",
                        "Check low pH, copper, and lead follow-up together before treating this like a taste-only issue.",
                        stateAwareLabLabel(stateResource),
                        stateAwareLabUrl(stateResource)
                ));
                tests.add(testCard(
                        "Raw-water vs plumbing split",
                        "You need to know whether the problem starts in the source water or appears after plumbing contact.",
                        "Separate untreated source conditions from first-draw or plumbing-contact samples before comparing neutralizers or point-of-use fixes."
                ));
                if (hasAnySignal(supportingSignalsUsed, "lead", "copper", "blue-green-stains", "metallic-taste")) {
                    tests.add(testCard(
                            "Do-not-shop-from-pH-alone check",
                            "Corrosion-supporting clues mean the pH number alone is too weak for equipment selection.",
                            "Keep the next test set corrosion-focused until lead, copper, and plumbing interaction are clearer."
                    ));
                }
            }
            case AESTHETIC_OPERATIONAL -> {
                if (signalKey.contains("hardness") || signalKey.contains("scale") || signalKey.contains("orange") || signalKey.contains("iron") || signalKey.contains("manganese")) {
                    tests.add(testCard(
                            "Hardness / iron / manganese confirmation",
                            "Sizing or compare decisions are weak when nuisance equipment is chosen from a single hardness or staining clue.",
                            "Confirm hardness, iron, and manganese on an untreated sample before choosing softener vs iron-filter scope.",
                            stateAwareLabLabel(stateResource),
                            stateAwareLabUrl(stateResource)
                    ));
                    if (hasAnySignal(supportingSignalsUsed, "ph", "lead", "copper", "blue-green-stains", "metallic-taste")) {
                        tests.add(testCard(
                                "Corrosion split before softener sizing",
                                "Corrosion clues can mimic or distort nuisance patterns enough to break the softener decision.",
                                "Do not size nuisance equipment until corrosion clues are separated from hardness or staining assumptions."
                        ));
                    }
                } else if (signalKey.contains("rotten-egg") || signalKey.contains("sulfur")) {
                    tests.add(testCard(
                            "Hot-only vs whole-house sulfur check",
                            "Sulfur odor decisions change depending on whether the smell is in hot water only or across the house.",
                            "Map the odor pattern first, then test sulfur-related nuisance signals before buying a sulfur system."
                    ));
                } else if (signalKey.contains("cloudy") || signalKey.contains("sediment")) {
                    tests.add(testCard(
                            "Sediment / disturbance follow-up",
                            "Cloudiness can be a temporary disturbance problem rather than a stable equipment-sizing problem.",
                            "Check whether the issue clears after standing and retest if it tracks rain, repair, or other disturbance."
                    ));
                } else {
                    tests.add(testCard(
                            "Nuisance-focused raw-water follow-up",
                            "Symptom-only nuisance decisions are weak if no raw-water follow-up exists.",
                            "Use nuisance-focused untreated-water testing before comparing whole-house equipment by symptom alone.",
                            stateAwareLabLabel(stateResource),
                            stateAwareLabUrl(stateResource)
                    ));
                }
            }
            case UNSUPPORTED -> tests.add(testCard(
                    "Certified lab escalation",
                    "This signal sits outside the stronger support coverage in the current engine, so conservative escalation is safer.",
                    "Use a certified lab and local/state guidance before interpreting this like a supported problem class.",
                    stateAwareLabLabel(stateResource),
                    stateAwareLabUrl(stateResource)
            ));
        }

        tests.add(testCard(
                "Untreated baseline confirmation",
                "Treatment-stage samples can hide or distort the real source-water decision.",
                "If the reported sample came from treated water, confirm the untreated baseline before locking into a system class."
        ));
        return orderRecommendedTestCards(new ArrayList<>(tests), triggerKey, supportingSignalsUsed);
    }

    private RecommendedTestCard testCard(String testName, String whyNow, String samplePlan) {
        return new RecommendedTestCard(testName, whyNow, samplePlan, "", "");
    }

    private RecommendedTestCard testCard(String testName, String whyNow, String samplePlan, String resourceLabel, String resourceUrl) {
        return new RecommendedTestCard(testName, whyNow, samplePlan, resourceLabel, resourceUrl);
    }

    private String stateAwareGuidanceLabel(Optional<StateResource> stateResource) {
        return stateResource
                .filter(resource -> !resource.localGuidanceUrl().isBlank())
                .map(resource -> "US".equalsIgnoreCase(resource.stateCode())
                        ? "National testing guidance"
                        : resource.stateCode() + " testing guidance")
                .orElse("");
    }

    private String stateAwareGuidanceUrl(Optional<StateResource> stateResource) {
        return stateResource
                .map(StateResource::localGuidanceUrl)
                .filter(url -> !url.isBlank())
                .orElse("");
    }

    private String stateAwareLabLabel(Optional<StateResource> stateResource) {
        return stateResource
                .filter(resource -> !resource.certifiedLabUrl().isBlank())
                .map(resource -> "US".equalsIgnoreCase(resource.stateCode())
                        ? "National certified lab path"
                        : resource.stateCode() + " certified lab path")
                .orElse("");
    }

    private String stateAwareLabUrl(Optional<StateResource> stateResource) {
        return stateResource
                .map(StateResource::certifiedLabUrl)
                .filter(url -> !url.isBlank())
                .orElse("");
    }

    private List<RecommendedTestCard> orderRecommendedTestCards(
            List<RecommendedTestCard> cards,
            String triggerKey,
            List<String> supportingSignalsUsed
    ) {
        return cards.stream()
                .sorted((left, right) -> Integer.compare(
                        recommendedTestPriority(left, triggerKey, supportingSignalsUsed),
                        recommendedTestPriority(right, triggerKey, supportingSignalsUsed)
                ))
                .toList();
    }

    private int recommendedTestPriority(
            RecommendedTestCard card,
            String triggerKey,
            List<String> supportingSignalsUsed
    ) {
        String name = card.testName() == null ? "" : card.testName().trim().toLowerCase();
        if (isFloodDriven(triggerKey, supportingSignalsUsed)) {
            return switch (name) {
                case "post-flood certified bacteria retest" -> 10;
                case "recent untreated follow-up sample" -> 20;
                case "sampling and wellhead review" -> 30;
                case "report-context confirmation" -> 40;
                case "untreated baseline confirmation" -> 50;
                default -> 60;
            };
        }
        if (isRainOrRepairDriven(triggerKey, supportingSignalsUsed)) {
            return switch (name) {
                case "event-window retest" -> 10;
                case "recent untreated follow-up sample" -> 20;
                case "sampling and wellhead review" -> 30;
                case "report-context confirmation" -> 40;
                case "untreated baseline confirmation" -> 50;
                default -> 60;
            };
        }
        if (isHomePurchaseDriven(triggerKey)) {
            return switch (name) {
                case "home-purchase certified panel" -> 10;
                case "report-context confirmation" -> 20;
                case "untreated baseline confirmation" -> 30;
                default -> 40;
            };
        }
        return switch (name) {
            case "report-context confirmation" -> 10;
            case "untreated baseline confirmation" -> 90;
            default -> 30;
        };
    }

    private String buildRecommendedTestOrderNote(String triggerKey, List<String> supportingSignalsUsed) {
        if (isFloodDriven(triggerKey, supportingSignalsUsed)) {
            return "Start with flood-sensitive retesting and source review before you widen the panel or compare treatment classes.";
        }
        if (isRainOrRepairDriven(triggerKey, supportingSignalsUsed)) {
            return "Keep the order tight: re-test the disturbance window first, then decide whether broader follow-up is still needed.";
        }
        if (isHomePurchaseDriven(triggerKey)) {
            return "Keep this in due-diligence order: define the certified panel first, then use the result to narrow scope instead of shopping early.";
        }
        return "";
    }

    private boolean isFloodDriven(String triggerKey, List<String> supportingSignalsUsed) {
        return triggerKey.contains("flood") || hasAnySignal(supportingSignalsUsed, "after-flood");
    }

    private boolean isRainOrRepairDriven(String triggerKey, List<String> supportingSignalsUsed) {
        return triggerKey.contains("heavy-rain")
                || triggerKey.contains("repair")
                || hasAnySignal(supportingSignalsUsed, "after-heavy-rain", "after-repair");
    }

    private boolean isHomePurchaseDriven(String triggerKey) {
        return triggerKey.contains("home-purchase");
    }

    private List<String> buildCompareReadinessChecks(
            DecisionInput input,
            ProblemType type,
            SignalMatch signal,
            DecisionNormalizedInput normalized,
            Branch branch,
            List<String> supportingSignalsUsed
    ) {
        LinkedHashSet<String> checks = new LinkedHashSet<>();
        String signalKey = resolvedSignalKey(input, normalized, signal);

        checks.add("Know whether the decision is drinking-only, whole-house, or still unresolved.");
        checks.add("Know whether the strongest evidence came from a recent untreated sample or from a weaker symptom/event pattern.");
        if (!supportingSignalsUsed.isEmpty()) {
            checks.add("Supporting report or context signals in play: " + joinHumanizedSignals(supportingSignalsUsed) + ".");
        }

        if (branch != Branch.GREEN) {
            checks.add("Treat compare pages as secondary research until confidence improves. This path is still verification-first.");
        } else {
            checks.add("Compare only the category that matches the current problem class instead of browsing every treatment type.");
        }

        if (type == ProblemType.AESTHETIC_OPERATIONAL
                && (signalKey.contains("hardness") || signalKey.contains("scale") || signalKey.contains("iron") || signalKey.contains("orange"))) {
            checks.add("Open the softener path only when hardness, scale, soap performance, or nuisance iron are the real problem.");
            if (hasAnySignal(supportingSignalsUsed, "ph", "lead", "copper", "blue-green-stains", "metallic-taste")) {
                checks.add("Do not open the softener path yet when corrosion clues still need to be separated from staining or scaling.");
            }
            if (hasAnySignal(supportingSignalsUsed, "total coliform", "e. coli", "after-flood", "after-heavy-rain", "nitrate", "arsenic", "pfas")) {
                checks.add("Secondary health or contamination flags mean nuisance equipment should stay behind testing and interpretation.");
            }
        }
        if (type == ProblemType.CHEMICAL_HEALTH || type == ProblemType.MICROBIAL) {
            checks.add("Do not treat a compare page like proof that the health risk is solved. Claim-check and retest logic still matter.");
            if (hasAnySignal(supportingSignalsUsed, "hardness", "iron", "manganese", "scale-buildup")) {
                checks.add("Nuisance signals do not downgrade the drinking-water protection problem. Keep health protection as the lead scope.");
            }
        }
        if (type == ProblemType.CORROSION) {
            checks.add("Do not let a treatment tank comparison replace the need to separate source-water issues from plumbing interaction.");
        }

        return new ArrayList<>(checks);
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

    private SoftenerSizingPreview buildSoftenerSizingPreview(
            DecisionInput input,
            ProblemType type,
            SignalMatch signal,
            DecisionNormalizedInput normalized,
            Branch branch,
            List<String> supportingSignalsUsed
    ) {
        String signalKey = resolvedSignalKey(input, normalized, signal);
        boolean nuisanceSoftenerContext = type == ProblemType.AESTHETIC_OPERATIONAL
                && (signalKey.contains("hardness")
                || signalKey.contains("scale")
                || signalKey.contains("iron")
                || signalKey.contains("manganese")
                || signalKey.contains("orange"));
        if (!nuisanceSoftenerContext) {
            return null;
        }
        if (branch != Branch.GREEN) {
            return notRecommendedSoftenerPreview(
                    "Softener not recommended yet",
                    "This path is still verification-first, so equipment sizing would be premature.",
                    List.of(
                            "Wait until the nuisance path is strong enough to compare equipment.",
                            "Use recent untreated data before treating this as a sizing decision."
                    ),
                    "Retest or tighten the interpretation path before using a softener class as a buying proxy."
            );
        }
        if (hasAnySignal(supportingSignalsUsed, "ph", "lead", "copper", "blue-green-stains", "metallic-taste")) {
            return notRecommendedSoftenerPreview(
                    "Softener not recommended yet",
                    "Corrosion clues are still present, so this does not look like a clean nuisance-only softener decision.",
                    List.of(
                            "Separate pH, lead, copper, or corrosion flags before opening a softener buying path.",
                            "A softener class estimate is weaker than the corrosion interpretation problem still on the page."
                    ),
                    "Use the corrosion path first, then come back to sizing only if hardness remains the real issue."
            );
        }
        if (hasAnySignal(supportingSignalsUsed, "total coliform", "e. coli", "after-flood", "after-heavy-rain", "nitrate", "arsenic", "pfas")) {
            return notRecommendedSoftenerPreview(
                    "Softener not recommended yet",
                    "Health or contamination signals are still in play, so nuisance equipment should stay secondary.",
                    List.of(
                            "Do not treat a softener class like a solution to microbial or drinking-water protection problems.",
                            "Keep testing and interpretation ahead of equipment sizing on this path."
                    ),
                    "Resolve the health or contamination path first."
            );
        }

        Integer householdSize = parsePositiveInt(input.normalizedHouseholdSize());
        if (householdSize == null) {
            return notRecommendedSoftenerPreview(
                    "Softener sizing needs more data",
                    "A class estimate is possible only when household size is known.",
                    List.of("Enter household size to turn nuisance context into a directional class estimate."),
                    "Add household size and rerun the result."
            );
        }

        Double hardnessGpg = resolveSoftenerHardnessGpg(signalKey, normalized);
        if (hardnessGpg == null) {
            return notRecommendedSoftenerPreview(
                    "Softener sizing needs measured hardness",
                    "This path looks like a nuisance issue, but the current input does not include a usable hardness number.",
                    List.of(
                            "Use a hardness result in mg/L as CaCO3, ppm, or grains/gal before class sizing.",
                            "Iron, orange staining, or scale alone are not enough to size a softener well."
                    ),
                    "Measure raw-water hardness before comparing 24k, 32k, 48k, or 64k classes."
            );
        }

        double dailyWaterUse = householdSize * 75d;
        double dailyGrainLoad = dailyWaterUse * hardnessGpg;
        int recommendedClass = recommendedSoftenerClass(Math.round(dailyGrainLoad * 7d));
        double regenerationDays = recommendedClass * 1000d / Math.max(dailyGrainLoad, 1d);
        String sizingVerdict = regenerationDays < 4d
                ? "Likely undersized at the current load"
                : (regenerationDays > 10d ? "Likely oversized for the current load" : "Reasonable starting class");

        List<String> notes = new ArrayList<>();
        notes.add("Directional preview only. This uses the entered household size and a planning assumption of 75 gallons per person per day.");
        if (hasAnySignal(supportingSignalsUsed, "iron", "manganese", "orange-stains")) {
            notes.add("Iron or manganese context can push the final class upward once raw-water iron is verified.");
        }
        if (normalized.sampleFreshness() != SampleFreshness.FRESH) {
            notes.add("Because the sample is not fresh, use this as planning context rather than a final purchase call.");
        }

        return new SoftenerSizingPreview(
                true,
                "Softener sizing preview",
                "This looks like a nuisance-focused path, so a directional class estimate is reasonable before you compare equipment classes.",
                recommendedClass + "k grain class",
                sizingVerdict,
                "%.1f gpg hardness basis".formatted(hardnessGpg),
                "~%,d grains/day".formatted(Math.round(dailyGrainLoad)),
                "About %.1f days between regenerations at the current planning load".formatted(regenerationDays),
                List.copyOf(notes),
                hasAnySignal(supportingSignalsUsed, "iron", "manganese", "orange-stains")
                        ? "Verify raw-water iron before you treat this as the final class."
                        : "Use this to compare class sizes, maintenance cadence, and space tradeoffs rather than shopping brand pages."
        );
    }

    private SoftenerSizingPreview notRecommendedSoftenerPreview(
            String headline,
            String summary,
            List<String> notes,
            String nextAction
    ) {
        return new SoftenerSizingPreview(
                false,
                headline,
                summary,
                "",
                "Do not size yet",
                "",
                "",
                "",
                notes,
                nextAction
        );
    }

    private Double resolveSoftenerHardnessGpg(String signalKey, DecisionNormalizedInput normalized) {
        if (!(signalKey.contains("hardness") || signalKey.contains("scale"))) {
            return null;
        }
        if (normalized.canonicalNumericResultValue() == null) {
            return null;
        }
        return normalized.canonicalNumericResultValue() / 17.1d;
    }

    private Integer parsePositiveInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(digits);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int recommendedSoftenerClass(long weeklyGrainLoad) {
        if (weeklyGrainLoad <= 24000L) {
            return 24;
        }
        if (weeklyGrainLoad <= 32000L) {
            return 32;
        }
        if (weeklyGrainLoad <= 48000L) {
            return 48;
        }
        return 64;
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

    private List<DecisionReportLineSummary> buildReportLineSummaries(
            DecisionInput input,
            DecisionNormalizedInput normalized,
            SignalMatch signal
    ) {
        List<DecisionReportLineSummary> lines = new ArrayList<>();
        if (input.entryMode() == com.example.wellwater.decision.model.EntryMode.RESULT_FIRST
                && (!input.normalizedAnalyte().isBlank() || !input.normalizedResultValue().isBlank())) {
            lines.add(new DecisionReportLineSummary(
                    labelForSignal(input.analyteName(), normalized.analyteKey(), signal),
                    observedValueLabel(input.resultValue(), input.unit(), input.qualifier(), input.qualifierValue()),
                    reportLineNote(normalized, signal, true),
                    true
            ));
        }
        for (DecisionReportLine line : input.companionReportLines()) {
            if (line == null || line.isBlank()) {
                continue;
            }
            DecisionInput companionInput = companionDecisionInput(input, line);
            DecisionNormalizedInput companionNormalized = normalizationService.normalize(companionInput);
            SignalMatch companionSignal = resolveSignal(companionNormalized);
            lines.add(new DecisionReportLineSummary(
                    labelForSignal(line.analyteName(), companionNormalized.analyteKey(), companionSignal),
                    observedValueLabel(line.resultValue(), line.unit(), line.qualifier(), line.qualifierValue()),
                    reportLineNote(companionNormalized, companionSignal, false),
                    false
            ));
        }
        return List.copyOf(lines);
    }

    private List<String> supportingSignalsUsed(
            DecisionInput input,
            DecisionNormalizedInput normalized,
            SignalMatch signal
    ) {
        LinkedHashSet<String> signals = new LinkedHashSet<>();
        String primaryKey = resolvedSignalKey(input, normalized, signal);
        for (String candidate : input.normalizedSupportingSignals()) {
            if (candidate.isBlank() || candidate.equals(primaryKey)) {
                continue;
            }
            signals.add(candidate);
        }
        for (String candidate : derivedSupportingSignalsFromCompanionLines(input)) {
            if (candidate.isBlank() || candidate.equals(primaryKey)) {
                continue;
            }
            signals.add(candidate);
        }
        if (!normalized.triggerKey().isBlank()
                && (!normalized.triggerKey().equals(primaryKey)
                || input.entryMode() == com.example.wellwater.decision.model.EntryMode.TRIGGER_FIRST)) {
            signals.add(normalized.triggerKey());
        }
        return new ArrayList<>(signals);
    }

    private List<String> derivedSupportingSignalsFromCompanionLines(DecisionInput input) {
        LinkedHashSet<String> signals = new LinkedHashSet<>();
        for (DecisionReportLine line : input.companionReportLines()) {
            if (line == null || line.isBlank()) {
                continue;
            }
            DecisionInput companionInput = companionDecisionInput(input, line);
            DecisionNormalizedInput companionNormalized = normalizationService.normalize(companionInput);
            SignalMatch companionSignal = resolveSignal(companionNormalized);
            if (companionSignal.signal() == null) {
                continue;
            }
            if (shouldUseCompanionSignal(companionInput, companionNormalized, companionSignal)) {
                signals.add(companionSignal.signal().key());
            }
        }
        return new ArrayList<>(signals);
    }

    private DecisionInput companionDecisionInput(DecisionInput input, DecisionReportLine line) {
        return new DecisionInput(
                com.example.wellwater.decision.model.EntryMode.RESULT_FIRST,
                line.analyteName(),
                line.resultValue(),
                line.unit(),
                line.qualifier(),
                line.qualifierValue(),
                input.sampleDate(),
                input.sampleSource(),
                input.labCertified(),
                input.state(),
                input.useScope(),
                input.existingTreatment(),
                input.existingTreatments(),
                List.of(),
                List.of(),
                "",
                "",
                input.labName(),
                input.householdSize(),
                input.smellType(),
                input.stainType(),
                input.tasteType(),
                input.locationScope(),
                input.changeTiming(),
                input.infantPresent(),
                input.pregnancyPresent(),
                input.immunocompromisedPresent(),
                input.slugHint()
        );
    }

    private boolean shouldUseCompanionSignal(
            DecisionInput companionInput,
            DecisionNormalizedInput companionNormalized,
            SignalMatch companionSignal
    ) {
        if (companionSignal.signal() == null) {
            return false;
        }
        if (companionNormalized.thresholdTriggered()) {
            return true;
        }
        if (companionNormalized.qualifierType() == QualifierType.POSITIVE) {
            return true;
        }
        if (companionSignal.signal().problemType() == ProblemType.MICROBIAL) {
            return companionInput.normalizedResultValue().contains("positive")
                    || companionInput.normalizedResultValue().contains("detected");
        }
        if (companionSignal.signal().problemType() == ProblemType.CHEMICAL_HEALTH) {
            return companionNormalized.canonicalNumericResultValue() != null
                    || companionNormalized.qualifierType() == QualifierType.LESS_THAN;
        }
        if (companionSignal.signal().problemType() == ProblemType.AESTHETIC_OPERATIONAL) {
            return companionNormalized.canonicalNumericResultValue() != null;
        }
        return false;
    }

    private boolean hasAnySignal(List<String> supportingSignalsUsed, String... candidates) {
        for (String candidate : candidates) {
            if (supportingSignalsUsed.stream().anyMatch(value -> value.equals(candidate))) {
                return true;
            }
        }
        return false;
    }

    private String joinHumanizedSignals(List<String> supportingSignalsUsed) {
        return String.join(", ", supportingSignalsUsed.stream()
                .map(this::humanizeSignal)
                .toList());
    }

    private String humanizeSignal(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return switch (value) {
            case "e. coli" -> "E. coli";
            case "ph" -> "pH";
            case "total coliform" -> "Total coliform";
            default -> {
                String normalized = value.replace('-', ' ');
                yield Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
            }
        };
    }

    private String labelForSignal(String fallback, String normalizedKey, SignalMatch signal) {
        if (signal != null && signal.signal() != null) {
            return humanizeSignal(signal.signal().key());
        }
        if (normalizedKey != null && !normalizedKey.isBlank()) {
            return humanizeSignal(normalizedKey);
        }
        if (fallback == null || fallback.isBlank()) {
            return "Unknown line";
        }
        return fallback.trim();
    }

    private String observedValueLabel(String value, String unit, String qualifier, String qualifierValue) {
        String rawValue = value == null ? "" : value.trim();
        String rawUnit = unit == null ? "" : unit.trim();
        String rawQualifier = qualifier == null ? "" : qualifier.trim();
        String rawQualifierValue = qualifierValue == null ? "" : qualifierValue.trim();
        StringBuilder label = new StringBuilder();
        if (!rawValue.isBlank()) {
            label.append(rawValue);
        }
        if (!rawUnit.isBlank()) {
            if (label.length() > 0) {
                label.append(" ");
            }
            label.append(rawUnit);
        }
        if (!rawQualifier.isBlank() && !"none".equalsIgnoreCase(rawQualifier)) {
            if (label.length() > 0) {
                label.append(" ");
            }
            label.append("(").append(rawQualifier);
            if (!rawQualifierValue.isBlank()) {
                label.append(": ").append(rawQualifierValue);
            }
            label.append(")");
        }
        return label.length() == 0 ? "Context only" : label.toString();
    }

    private String reportLineNote(DecisionNormalizedInput normalized, SignalMatch signal, boolean primary) {
        if (!normalized.unitSupported()) {
            return primary ? "Primary line used cautiously because the unit is missing or unsupported." : "Supporting line used cautiously because the unit is missing or unsupported.";
        }
        if (normalized.thresholdTriggered()) {
            return primary ? "Primary line crossed or matched a benchmark used by the engine." : "Supporting line also crossed a benchmark and changed the routing.";
        }
        if (normalized.qualifierType() == QualifierType.POSITIVE) {
            return primary ? "Primary line is a positive result." : "Supporting line is a positive result and changes the interpretation.";
        }
        if (signal != null && signal.signal() != null) {
            return primary
                    ? "Primary line matched a supported registry signal."
                    : "Supporting line was read as additional context from the same report.";
        }
        return primary ? "Primary line was used as the main report input." : "Supporting line was kept as report context.";
    }

    private List<CtaLink> routeCtas(
            Branch branch,
            Optional<StateResource> stateResource,
            ProblemType type,
            SignalMatch signal,
            DecisionNormalizedInput normalized,
            DecisionInput input,
            List<String> supportingSignalsUsed
    ) {
        String localGuidanceUrl = stateResource.map(StateResource::localGuidanceUrl)
                .filter(url -> !url.isBlank())
                .orElse("https://www.epa.gov/privatewells/protect-your-homes-water");
        String certifiedLabUrl = stateResource.map(StateResource::certifiedLabUrl)
                .filter(url -> !url.isBlank())
                .orElse("https://www.cdc.gov/drinking-water/safety/guidelines-for-testing-well-water.html");
        String compareTarget = compareTarget(type, signal, normalized, input, supportingSignalsUsed);
        String authorityTarget = authorityTarget(type, signal, normalized, input, supportingSignalsUsed);

        if (branch == Branch.RED) {
            return List.of(
                    new CtaLink("action_guide", "Review Immediate Action Guidance", "https://www.cdc.gov/water-emergency/about/drinking-water-advisories-an-overview.html"),
                    new CtaLink("local_guidance", "Check State / Local Guidance", localGuidanceUrl),
                    new CtaLink("certified_lab", "Find Certified Lab", certifiedLabUrl),
                    new CtaLink("category_compare", "Open The Right Compare Path After Safety Steps", compareTarget)
            );
        }
        if (branch == Branch.AMBER) {
            return List.of(
                    new CtaLink("retest", "Get Better Data First", certifiedLabUrl),
                    new CtaLink("local_guidance", "Check State / Local Guidance", localGuidanceUrl),
                    new CtaLink("interpretation", "Review The Matching Interpretation Page", authorityTarget),
                    new CtaLink("category_compare", "Open The Best-Fit Compare Page", compareTarget)
            );
        }
        return List.of(
                new CtaLink("category_compare", "Compare The Best-Fit Category", compareTarget),
                new CtaLink("claim_check", "Check Claims, Scope, And Sampling Fit", authorityTarget),
                new CtaLink("save_report", "Retest and Verify Over Time", certifiedLabUrl)
        );
    }

    private String compareTarget(
            ProblemType type,
            SignalMatch signal,
            DecisionNormalizedInput normalized,
            DecisionInput input,
            List<String> supportingSignalsUsed
    ) {
        String signalKey = resolvedSignalKey(normalized, signal);
        String triggerKey = normalized.triggerKey();

        if (triggerKey.contains("home-purchase")) {
            return "/well-water/test-kit-vs-certified-lab";
        }
        if (triggerKey.contains("flood")) {
            return "/well-water/uv-vs-chlorination";
        }
        if (triggerKey.contains("repair") || triggerKey.contains("heavy-rain")) {
            return "/well-water/test-kit-vs-certified-lab";
        }
        if (signalKey.contains("arsenic")) {
            return "/well-water/ro-vs-adsorptive-media-for-arsenic";
        }
        if (signalKey.contains("pfas") || signalKey.contains("nitrate") || signalKey.contains("nitrite")) {
            return "/well-water/carbon-vs-ro";
        }
        if (signalKey.contains("radon") || signalKey.contains("radium")) {
            return "/well-water/radon-aeration-vs-gac";
        }
        if (type == ProblemType.MICROBIAL) {
            return "/well-water/uv-vs-chlorination";
        }
        if (type == ProblemType.CORROSION) {
            return "/well-water/acid-neutralizer-vs-soda-ash";
        }
        if (signalKey.contains("hardness") || signalKey.contains("scale") || signalKey.contains("iron") || signalKey.contains("manganese") || signalKey.contains("orange")) {
            if (hasAnySignal(supportingSignalsUsed, "ph", "lead", "copper", "blue-green-stains", "metallic-taste")) {
                return "/well-water/acid-neutralizer-vs-soda-ash";
            }
            if (hasAnySignal(supportingSignalsUsed, "total coliform", "e. coli", "after-flood", "after-heavy-rain")
                    || input.infantPresent()
                    || input.pregnancyPresent()) {
                return "/well-water/when-not-to-buy-treatment-yet";
            }
            return "/well-water/softener-vs-iron-filter";
        }
        if (signalKey.contains("rotten-egg") || signalKey.contains("sulfur")) {
            return "/well-water/air-injection-vs-oxidizing-filter";
        }
        if (signalKey.contains("cloudy") || signalKey.contains("sediment")) {
            return "/well-water/spin-down-vs-cartridge-sediment-filter";
        }
        if (type == ProblemType.CHEMICAL_HEALTH) {
            return "/well-water/whole-house-vs-under-sink-ro";
        }
        return "/well-water/family/compares";
    }

    private String authorityTarget(
            ProblemType type,
            SignalMatch signal,
            DecisionNormalizedInput normalized,
            DecisionInput input,
            List<String> supportingSignalsUsed
    ) {
        String signalKey = resolvedSignalKey(normalized, signal);
        String triggerKey = normalized.triggerKey();

        if (triggerKey.contains("home-purchase")) {
            return "/well-water/private-well-home-sale-testing-by-state";
        }
        if (triggerKey.contains("flood") || type == ProblemType.MICROBIAL) {
            return "/well-water/private-well-sampling-mistakes-that-break-results";
        }
        if (signalKey.contains("arsenic")) {
            return "/well-water/arsenic-bedrock-testing-checklist";
        }
        if (signalKey.contains("pfas")) {
            return "/well-water/pfas-private-well-filter-claim-checklist";
        }
        if (signalKey.contains("nitrate") || signalKey.contains("nitrite")) {
            if (hasAnySignal(supportingSignalsUsed, "total coliform", "e. coli", "after-flood", "after-heavy-rain")) {
                return "/well-water/private-well-sampling-mistakes-that-break-results";
            }
            return "/well-water/nitrate-baby-pregnancy-well-water-checklist";
        }
        if (type == ProblemType.CORROSION) {
            return "/well-water/low-ph-copper-corrosion-testing-order";
        }
        if ((signalKey.contains("hardness") || signalKey.contains("scale") || signalKey.contains("iron") || signalKey.contains("orange"))
                && hasAnySignal(supportingSignalsUsed, "ph", "lead", "copper", "blue-green-stains", "metallic-taste")) {
            return "/well-water/low-ph-copper-corrosion-testing-order";
        }
        if ((signalKey.contains("hardness") || signalKey.contains("scale") || signalKey.contains("iron") || signalKey.contains("orange"))
                && (hasAnySignal(supportingSignalsUsed, "total coliform", "e. coli", "after-flood", "after-heavy-rain")
                || input.infantPresent()
                || input.pregnancyPresent())) {
            return "/well-water/when-not-to-buy-treatment-yet";
        }
        if (signalKey.contains("rotten-egg") || signalKey.contains("sulfur")) {
            return "/well-water/sulfur-smell-hot-water-vs-whole-house";
        }
        if (type == ProblemType.AESTHETIC_OPERATIONAL) {
            return "/well-water/when-not-to-buy-treatment-yet";
        }
        return "/well-water/how-to-read-a-well-water-lab-report";
    }

    private String resolvedSignalKey(DecisionInput input, DecisionNormalizedInput normalized, SignalMatch signal) {
        if (signal.signal() != null) {
            return signal.signal().key();
        }
        if (!normalized.analyteKey().isBlank()) {
            return normalized.analyteKey();
        }
        if (!normalized.symptomKey().isBlank()) {
            return normalized.symptomKey();
        }
        if (!normalized.triggerKey().isBlank()) {
            return normalized.triggerKey();
        }
        if (!input.normalizedAnalyte().isBlank()) {
            return input.normalizedAnalyte();
        }
        if (!input.normalizedSymptom().isBlank()) {
            return input.normalizedSymptom();
        }
        return input.normalizedTrigger();
    }

    private String resolvedSignalKey(DecisionNormalizedInput normalized, SignalMatch signal) {
        if (signal.signal() != null) {
            return signal.signal().key();
        }
        if (!normalized.analyteKey().isBlank()) {
            return normalized.analyteKey();
        }
        if (!normalized.symptomKey().isBlank()) {
            return normalized.symptomKey();
        }
        return normalized.triggerKey();
    }

    private record SignalMatch(
            String sourceType,
            RuleSignal signal
    ) {
    }
}

