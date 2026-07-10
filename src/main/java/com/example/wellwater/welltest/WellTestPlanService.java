package com.example.wellwater.welltest;

import com.example.wellwater.decision.DecisionEngineService;
import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.DecisionResult;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.registry.StateResource;
import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class WellTestPlanService {

    public static final String PLAN_VERSION = "well-test-plan-2026-07-10-v2";
    private static final String DISCLOSURE = "This is testing decision support, not a safety guarantee, legal opinion, or substitute for a certified laboratory.";
    private static final Set<String> ALLOWED_REASONS = Set.of(
            "annual", "home_purchase", "after_flood", "after_heavy_rain", "after_repair",
            "after_wildfire", "new_or_unused_well", "symptom", "known_contamination", "other"
    );
    private static final Set<String> ALLOWED_SIGNALS = Set.of(
            "rotten_egg_smell", "metallic_taste", "orange_stains", "blue_green_stains",
            "black_stains", "cloudy_water", "scale_buildup", "no_obvious_issue"
    );
    private static final Set<String> ALLOWED_RISKS = Set.of(
            "agriculture", "industrial", "mining", "nearby_septic", "fuel_or_petroleum",
            "pfas_source", "radionuclides", "unknown"
    );
    private static final Set<String> EXPANDED_RISKS = Set.of("agriculture", "industrial", "mining", "nearby_septic");
    private static final Set<String> SPECIALIZED_RISKS = Set.of("fuel_or_petroleum", "pfas_source", "radionuclides");

    private final DecisionEngineService decisionEngineService;
    private final StateResourceRegistryService stateResourceRegistryService;
    private final PartnerCatalogService partnerCatalogService;

    public WellTestPlanService(
            DecisionEngineService decisionEngineService,
            StateResourceRegistryService stateResourceRegistryService,
            PartnerCatalogService partnerCatalogService
    ) {
        this.decisionEngineService = decisionEngineService;
        this.stateResourceRegistryService = stateResourceRegistryService;
        this.partnerCatalogService = partnerCatalogService;
    }

    public WellTestPlanResult create(WellTestPlanRequest request, String channel) {
        NormalizedRequest normalized = normalize(request);
        DecisionResult engineResult = engineResult(normalized).orElse(null);
        StateResource stateResource = stateResourceRegistryService.findByState(normalized.stateCode())
                .orElse(new StateResource("US", "https://www.epa.gov/privatewells", "https://www.epa.gov/dwlabcert/contact-information-certification-programs-and-certified-laboratories-drinking-water", "https://www.epa.gov/privatewells"));

        LinkedHashMap<String, TestPanelItem> panel = baselinePanel();
        applyReasonPanel(panel, normalized);
        applySignalPanel(panel, normalized.signals());
        applyRiskPanel(panel, normalized.riskContexts());

        String urgency = urgency(normalized, engineResult);
        String family = resultFamily(normalized, urgency);
        String verdict = verdict(normalized, urgency, family);
        List<String> reasons = reasons(normalized, engineResult, stateResource);
        PartnerOffer offer = partnerOffer(normalized, urgency, channel).orElse(null);

        return new WellTestPlanResult(
                verdict,
                urgency,
                new ArrayList<>(panel.values()),
                nextSteps(normalized, urgency),
                avoidForNow(normalized, urgency, family),
                reasons,
                new PlanResourceLink(guidanceLabel(stateResource), stateResource.localGuidanceUrl()),
                new PlanResourceLink(labLabel(stateResource), stateResource.certifiedLabUrl()),
                offer,
                commerceNote(normalized, urgency, offer),
                DISCLOSURE,
                PLAN_VERSION,
                family
        );
    }

    private NormalizedRequest normalize(WellTestPlanRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("A test-plan request is required.");
        }
        String reason = token(request.reason());
        if (!ALLOWED_REASONS.contains(reason)) {
            throw new IllegalArgumentException("Unsupported reason.");
        }
        List<String> signals = removePlaceholderWhenSpecific(
                normalizedList(request.signals(), ALLOWED_SIGNALS, "signal"),
                "no_obvious_issue"
        );
        List<String> risks = removePlaceholderWhenSpecific(
                normalizedList(request.riskContexts(), ALLOWED_RISKS, "risk context"),
                "unknown"
        );
        String stateCode = normalizeState(request.stateCode());
        String treatment = optionalToken(request.existingTreatment(), Set.of("none", "ro", "uv", "softener", "iron_filter", "carbon", "sediment", "unknown"), "existing treatment");
        String useScope = optionalToken(request.useScope(), Set.of("drinking_only", "whole_house", "both", "unknown"), "use scope");
        return new NormalizedRequest(reason, signals, risks, stateCode, treatment, useScope);
    }

    private Optional<DecisionResult> engineResult(NormalizedRequest request) {
        String trigger = switch (request.reason()) {
            case "home_purchase" -> "home-purchase-test";
            case "after_flood" -> "after-flood";
            case "after_heavy_rain" -> "after-heavy-rain";
            case "after_repair" -> "after-repair";
            case "after_wildfire" -> "after-wildfire";
            default -> "";
        };
        String symptom = trigger.isBlank() ? primarySymptom(request.signals()) : "";
        if (trigger.isBlank() && symptom.isBlank()) {
            return Optional.empty();
        }
        EntryMode entryMode = trigger.isBlank() ? EntryMode.SYMPTOM_FIRST : EntryMode.TRIGGER_FIRST;
        DecisionInput input = new DecisionInput(
                entryMode, "", "", "", "", "", "", "untreated", "unknown",
                request.stateCode(), request.useScope().replace('_', '-'), request.existingTreatment().replace('_', ' '),
                request.existingTreatment().isBlank() ? List.of() : List.of(request.existingTreatment().replace('_', ' ')),
                supportingSignals(request), List.of(), symptom, trigger, "", "", "", "", "", "", "",
                false, false, false, "chatgpt-well-test-plan"
        );
        return Optional.of(decisionEngineService.decide(input));
    }

    private LinkedHashMap<String, TestPanelItem> baselinePanel() {
        LinkedHashMap<String, TestPanelItem> panel = new LinkedHashMap<>();
        add(panel, "total-coliform-e-coli", "Total coliform and E. coli", "Core microbial screen for a private well.");
        add(panel, "nitrate-nitrite", "Nitrate and nitrite", "Core groundwater and septic or agricultural contamination screen.");
        add(panel, "ph", "pH", "Shows corrosivity or scale conditions that change follow-up decisions.");
        add(panel, "tds", "Total dissolved solids", "Provides a general baseline for mineral loading and change over time.");
        return panel;
    }

    private void applyReasonPanel(Map<String, TestPanelItem> panel, NormalizedRequest request) {
        switch (request.reason()) {
            case "home_purchase" -> {
                add(panel, "transaction-panel", "State-required transaction analytes", "A property transfer can require a certified method and state-specific panel.");
                if ("OR".equals(request.stateCode())) {
                    add(panel, "arsenic", "Arsenic", "Oregon domestic-well property transfer testing includes arsenic.");
                }
            }
            case "after_flood", "after_heavy_rain", "after_repair" ->
                    add(panel, "turbidity", "Turbidity", "Disturbance can move sediment and compromise microbial sampling confidence.");
            case "after_wildfire" ->
                    add(panel, "vocs", "Volatile organic compounds", "Wildfire damage can introduce chemical contamination that needs local protocol.");
            case "new_or_unused_well" -> {
                add(panel, "arsenic", "Arsenic", "A first baseline should cover common naturally occurring health contaminants.");
                add(panel, "iron-manganese", "Iron and manganese", "Establishes nuisance and operational baseline before equipment decisions.");
            }
            default -> {
            }
        }
    }

    private void applySignalPanel(Map<String, TestPanelItem> panel, List<String> signals) {
        for (String signal : signals) {
            switch (signal) {
                case "rotten_egg_smell" -> add(panel, "hydrogen-sulfide", "Hydrogen sulfide and sulfur bacteria", "Separates a sulfur source from a generic odor assumption.");
                case "metallic_taste" -> {
                    add(panel, "lead-copper", "Lead and copper", "Metallic taste can reflect plumbing corrosion rather than source water alone.");
                    add(panel, "iron-manganese", "Iron and manganese", "Checks common source-water metals that can create metallic taste.");
                }
                case "orange_stains" -> add(panel, "iron-manganese", "Iron and manganese", "Orange staining commonly needs an untreated-water metals check.");
                case "blue_green_stains" -> add(panel, "lead-copper", "Lead and copper", "Blue-green staining is a corrosion clue that warrants plumbing metals testing.");
                case "black_stains" -> add(panel, "iron-manganese", "Iron and manganese", "Black staining can be associated with manganese or mixed nuisance metals.");
                case "cloudy_water" -> add(panel, "turbidity", "Turbidity", "Cloudiness should be measured before it is treated as an equipment problem.");
                case "scale_buildup" -> add(panel, "hardness", "Hardness", "Confirms whether scale is actually a hardness-loading problem.");
                default -> {
                }
            }
        }
    }

    private void applyRiskPanel(Map<String, TestPanelItem> panel, List<String> risks) {
        for (String risk : risks) {
            switch (risk) {
                case "agriculture" -> add(panel, "pesticides", "Pesticide and herbicide screen", "Agricultural activity can widen the panel beyond a routine baseline.");
                case "industrial" -> {
                    add(panel, "vocs", "Volatile organic compounds", "Industrial activity can introduce solvent and fuel-related compounds.");
                    add(panel, "pfas", "PFAS", "Industrial context can justify a targeted PFAS discussion with a certified lab.");
                }
                case "mining" -> {
                    add(panel, "arsenic", "Arsenic", "Mining and geologic context can elevate naturally occurring metals risk.");
                    add(panel, "radionuclides", "Radium, radon, and uranium discussion", "A certified lab should determine the locally appropriate radionuclide scope.");
                }
                case "nearby_septic" -> add(panel, "septic-follow-up", "Septic-sensitive microbial and nitrate panel", "A nearby septic system strengthens the case for microbial and nitrate testing.");
                case "fuel_or_petroleum" -> add(panel, "petroleum", "Petroleum compounds and VOCs", "Fuel storage or spills require a certified targeted chemical panel.");
                case "pfas_source" -> add(panel, "pfas", "PFAS", "A known nearby PFAS source requires a certified targeted panel rather than a generic kit.");
                case "radionuclides" -> add(panel, "radionuclides", "Radium, radon, and uranium discussion", "Local geology and state guidance should determine the radionuclide method.");
                default -> {
                }
            }
        }
    }

    private String urgency(NormalizedRequest request, DecisionResult engineResult) {
        if (request.reason().equals("known_contamination")) {
            return "immediate";
        }
        if (engineResult != null) {
            return engineResult.urgency().wireValue();
        }
        if (!request.riskContexts().stream().filter(value -> !value.equals("unknown")).toList().isEmpty()) {
            return "prompt";
        }
        return request.signals().stream().anyMatch(value -> !value.equals("no_obvious_issue")) ? "prompt" : "routine";
    }

    private String resultFamily(NormalizedRequest request, String urgency) {
        if (request.reason().equals("home_purchase")) {
            return "transaction";
        }
        if (urgency.equals("immediate") || request.riskContexts().stream().anyMatch(SPECIALIZED_RISKS::contains)) {
            return "certified_urgent";
        }
        if (request.riskContexts().stream().anyMatch(EXPANDED_RISKS::contains)) {
            return "expanded";
        }
        return "baseline";
    }

    private String verdict(NormalizedRequest request, String urgency, String family) {
        if (request.reason().equals("home_purchase")) {
            return "Order the state-aware certified transaction panel before closing.";
        }
        if (urgency.equals("immediate")) {
            return "Use an alternate drinking-water source and arrange certified testing now.";
        }
        return switch (family) {
            case "certified_urgent" -> "Use a certified targeted panel before comparing any treatment.";
            case "expanded" -> "Start with an expanded certified well-water panel.";
            default -> "Start with a certified baseline well-water panel.";
        };
    }

    private List<String> nextSteps(NormalizedRequest request, String urgency) {
        if (urgency.equals("immediate")) {
            return List.of(
                    "Use an alternate source for drinking and cooking now.",
                    "Review the official response guidance for the event or contamination context.",
                    "Contact a certified drinking-water laboratory before collecting a sample."
            );
        }
        if (request.reason().equals("home_purchase")) {
            return List.of(
                    "Confirm the state's property-transfer testing requirements before closing.",
                    "Ask a certified laboratory for the transaction panel and sampling protocol.",
                    "Use the certified report, not a screening kit, for the closing decision."
            );
        }
        return List.of(
                "Send this panel list to a certified drinking-water laboratory.",
                "Get the laboratory's containers and sampling instructions before collecting water.",
                "Use the certified results before choosing treatment or expanding the panel."
        );
    }

    private String avoidForNow(NormalizedRequest request, String urgency, String family) {
        if (urgency.equals("immediate")) {
            return "Do not keep drinking the water or rely on a home kit while the urgent route is unresolved.";
        }
        if (request.reason().equals("home_purchase")) {
            return "Do not treat a mail-in screening kit as proof that property-transfer requirements are satisfied.";
        }
        if (family.equals("certified_urgent")) {
            return "Do not choose treatment from a smell, taste, stain, or nearby risk alone.";
        }
        return "Do not buy treatment before the recommended panel confirms what is actually present.";
    }

    private List<String> reasons(NormalizedRequest request, DecisionResult engineResult, StateResource stateResource) {
        LinkedHashSet<String> reasons = new LinkedHashSet<>();
        if (request.reason().equals("home_purchase")) {
            reasons.add("Property-transfer testing can depend on state rules and certified sampling methods.");
        }
        if (!request.signals().isEmpty() && !request.signals().equals(List.of("no_obvious_issue"))) {
            reasons.add("Visible, taste, or odor clues narrow the panel but do not identify a contaminant by themselves.");
        }
        if (request.riskContexts().stream().anyMatch(EXPANDED_RISKS::contains)) {
            reasons.add("Nearby land use expands the panel beyond routine annual indicators.");
        }
        if (request.riskContexts().stream().anyMatch(SPECIALIZED_RISKS::contains)) {
            reasons.add("Specialized chemical or radionuclide risks need a certified lab to choose the correct method.");
        }
        if (engineResult != null && !engineResult.primaryVerdictSentence().isBlank()) {
            reasons.add(engineResult.primaryVerdictSentence());
        }
        if (!"US".equalsIgnoreCase(stateResource.stateCode())) {
            reasons.add(stateResource.stateCode() + " guidance is available for the lab and sampling path.");
        }
        if (reasons.isEmpty()) {
            reasons.add("A compact baseline establishes what changed before equipment or broader testing is considered.");
        }
        return reasons.stream().limit(4).toList();
    }

    private Optional<PartnerOffer> partnerOffer(NormalizedRequest request, String urgency, String channel) {
        if ("chatgpt".equalsIgnoreCase(channel)) {
            return Optional.empty();
        }
        if (commerceSuppressed(request, urgency)) {
            return Optional.empty();
        }
        if (request.riskContexts().stream().anyMatch(EXPANDED_RISKS::contains)) {
            return partnerCatalogService.offer("advanced", channel,
                    "Matches a broader private-well screen when nearby land use or septic context expands the baseline.");
        }
        return partnerCatalogService.offer("essential", channel,
                "Matches a routine private-well baseline when no specialized or transaction-specific method is required.");
    }

    private String commerceNote(NormalizedRequest request, String urgency, PartnerOffer offer) {
        if (offer != null) {
            return "The optional physical kit is shown only after the testing scope is selected; the certified-lab path remains available.";
        }
        if (commerceSuppressed(request, urgency)) {
            return "No physical kit is shown because this route needs urgent, transaction-specific, or specialized certified testing.";
        }
        return "No partner kit is currently available for this route; use the certified-laboratory path.";
    }

    private boolean commerceSuppressed(NormalizedRequest request, String urgency) {
        return urgency.equals("immediate")
                || request.reason().equals("home_purchase")
                || request.reason().equals("known_contamination")
                || request.reason().equals("after_flood")
                || request.reason().equals("after_wildfire")
                || request.riskContexts().stream().anyMatch(SPECIALIZED_RISKS::contains);
    }

    private List<String> supportingSignals(NormalizedRequest request) {
        LinkedHashSet<String> signals = new LinkedHashSet<>();
        request.signals().stream().map(this::engineSignal).filter(value -> !value.isBlank()).forEach(signals::add);
        for (String risk : request.riskContexts()) {
            switch (risk) {
                case "agriculture" -> signals.addAll(List.of("nitrate", "pesticides"));
                case "industrial" -> signals.addAll(List.of("vocs", "pfas"));
                case "mining" -> signals.addAll(List.of("arsenic", "manganese", "radium"));
                case "nearby_septic" -> signals.addAll(List.of("total coliform", "e. coli", "nitrate"));
                case "fuel_or_petroleum" -> signals.add("vocs");
                case "pfas_source" -> signals.add("pfas");
                case "radionuclides" -> signals.addAll(List.of("radium", "radon"));
                default -> {
                }
            }
        }
        return List.copyOf(signals);
    }

    private String primarySymptom(List<String> signals) {
        return signals.stream().map(this::engineSignal).filter(value -> !value.isBlank()).findFirst().orElse("");
    }

    private String engineSignal(String signal) {
        return switch (signal) {
            case "rotten_egg_smell" -> "rotten-egg-smell";
            case "metallic_taste" -> "metallic-taste";
            case "orange_stains" -> "orange-stains";
            case "blue_green_stains" -> "blue-green-stains";
            case "black_stains" -> "black-stains";
            case "cloudy_water" -> "cloudy-water";
            case "scale_buildup" -> "scale-buildup";
            default -> "";
        };
    }

    private String guidanceLabel(StateResource resource) {
        return "US".equalsIgnoreCase(resource.stateCode()) ? "EPA private-well guidance" : resource.stateCode() + " private-well guidance";
    }

    private String labLabel(StateResource resource) {
        return "US".equalsIgnoreCase(resource.stateCode()) ? "Find a certified drinking-water lab" : "Find a certified lab for " + resource.stateCode();
    }

    private void add(Map<String, TestPanelItem> panel, String key, String name, String reason) {
        panel.putIfAbsent(key, new TestPanelItem(name, reason));
    }

    private List<String> normalizedList(List<String> values, Set<String> allowed, String label) {
        if (values == null) {
            return List.of();
        }
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String value : values) {
            String token = token(value);
            if (token.isBlank()) {
                continue;
            }
            if (!allowed.contains(token)) {
                throw new IllegalArgumentException("Unsupported " + label + ".");
            }
            normalized.add(token);
        }
        return List.copyOf(normalized);
    }

    private List<String> removePlaceholderWhenSpecific(List<String> values, String placeholder) {
        if (values.size() <= 1 || !values.contains(placeholder)) {
            return values;
        }
        return values.stream().filter(value -> !placeholder.equals(value)).toList();
    }

    private String optionalToken(String value, Set<String> allowed, String label) {
        String normalized = token(value);
        if (normalized.isBlank()) {
            return "";
        }
        if (!allowed.contains(normalized)) {
            throw new IllegalArgumentException("Unsupported " + label + ".");
        }
        return normalized;
    }

    private String normalizeState(String value) {
        String normalized = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
        if (normalized.isBlank()) {
            return "US";
        }
        if (!normalized.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("State code must be a two-letter US code.");
        }
        return normalized;
    }

    private String token(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }

    private record NormalizedRequest(
            String reason,
            List<String> signals,
            List<String> riskContexts,
            String stateCode,
            String existingTreatment,
            String useScope
    ) {
    }
}
