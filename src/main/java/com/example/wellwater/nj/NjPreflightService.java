package com.example.wellwater.nj;

import com.example.wellwater.welltest.WellTestPlanRequest;
import com.example.wellwater.welltest.WellTestPlanResult;
import com.example.wellwater.welltest.WellTestPlanService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class NjPreflightService {

    private static final Set<String> TRANSACTION_TYPES = Set.of("sale", "lease", "planning");
    private static final Set<String> WATER_SOURCES = Set.of("private_well", "shared_small_well", "irrigation_only", "unsure");
    private static final Set<String> EXTRA_CONTEXTS = Set.of(
            "infant_or_pregnant", "recent_flood_or_repair", "older_plumbing", "nearby_contamination", "treatment_installed"
    );

    private final NjGeocodingService geocodingService;
    private final NjPwtaDataService dataService;
    private final NjPwtaRulesService rulesService;
    private final NjPartnerCatalogService partnerCatalogService;
    private final WellTestPlanService wellTestPlanService;

    public NjPreflightService(
            NjGeocodingService geocodingService,
            NjPwtaDataService dataService,
            NjPwtaRulesService rulesService,
            NjPartnerCatalogService partnerCatalogService,
            WellTestPlanService wellTestPlanService
    ) {
        this.geocodingService = geocodingService;
        this.dataService = dataService;
        this.rulesService = rulesService;
        this.partnerCatalogService = partnerCatalogService;
        this.wellTestPlanService = wellTestPlanService;
    }

    public NjPreflightResult create(NjPreflightRequest request) {
        NormalizedRequest normalized = normalize(request);
        LocationResolution location = resolveLocation(normalized);
        NjCoverage coverage = rulesService.coverage(normalized.transactionType(), normalized.waterSource());
        String county = location.county();
        List<NjRequirement> requiredPanel = rulesService.requiredPanel(county);
        List<NjRiskSignal> signals = location.signals().stream().limit(6).toList();
        String channel = safeChannel(normalized.channel());
        String source = safeSource(channel, normalized.partnerSlug(), location.municipality());
        WellTestPlanResult basePlan = wellTestPlanService.create(
                new WellTestPlanRequest("home_purchase", List.of("no_obvious_issue"), mappedRiskContexts(normalized.extraContexts()), "NJ", "unknown", "both"),
                channel
        );
        NjPartner partner = "partner".equals(channel)
                ? partnerCatalogService.active(normalized.partnerSlug()).orElse(null)
                : null;

        return new NjPreflightResult(
                coverage,
                location.locationLabel(),
                location.resolution(),
                location.notice(),
                county,
                location.municipality() == null ? "" : location.municipality().slug(),
                requiredPanel,
                signals,
                location.signalScope(),
                extraDiscussions(normalized.extraContexts(), signals),
                rulesService.nextSteps(normalized.transactionType()),
                basePlan,
                partner,
                channel,
                source
        );
    }

    private NormalizedRequest normalize(NjPreflightRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("A preflight request is required.");
        }
        String transactionType = token(request.transactionType());
        String waterSource = token(request.waterSource());
        if (!TRANSACTION_TYPES.contains(transactionType)) {
            throw new IllegalArgumentException("Choose a sale, lease, or planning path.");
        }
        if (!WATER_SOURCES.contains(waterSource)) {
            throw new IllegalArgumentException("Choose how the property receives water.");
        }
        String address = request.address() == null ? "" : request.address().trim();
        if (address.length() > 200 || address.contains("\n") || address.contains("\r")) {
            throw new IllegalArgumentException("Enter a valid one-line New Jersey address.");
        }
        List<String> contexts = request.extraContexts().stream()
                .map(this::token)
                .filter(value -> !value.isBlank())
                .toList();
        if (!EXTRA_CONTEXTS.containsAll(contexts)) {
            throw new IllegalArgumentException("Unsupported property or household context.");
        }
        String municipalitySlug = token(request.municipalitySlug());
        if (address.isBlank() && municipalitySlug.isBlank()) {
            throw new IllegalArgumentException("Enter an address or choose a New Jersey municipality.");
        }
        return new NormalizedRequest(
                transactionType,
                waterSource,
                address,
                municipalitySlug,
                List.copyOf(contexts),
                token(request.channel()),
                token(request.partnerSlug())
        );
    }

    private LocationResolution resolveLocation(NormalizedRequest request) {
        Optional<NjMunicipalitySummary> selectedMunicipality = dataService.findMunicipality(request.municipalitySlug());
        if (!request.municipalitySlug().isBlank() && selectedMunicipality.isEmpty()) {
            throw new IllegalArgumentException("Choose a municipality from the New Jersey list.");
        }
        if (!request.address().isBlank()) {
            Optional<NjLocation> geocoded = geocodingService.locate(request.address());
            if (geocoded.isPresent()) {
                NjLocation location = geocoded.get();
                if (!location.inNewJersey()) {
                    throw new IllegalArgumentException("The address must be in New Jersey.");
                }
                Optional<NjMunicipalitySummary> matchedMunicipality = dataService.matchMunicipality(location.municipality(), location.county());
                NjMunicipalitySummary municipality = matchedMunicipality.orElse(selectedMunicipality.orElse(null));
                Optional<NjPwtaDataService.GridMatch> grid = dataService.findGrid(location.longitude(), location.latitude());
                if (grid.isPresent()) {
                    return new LocationResolution(
                            areaLabel(municipality, location.county()),
                            "2-mile aggregate grid",
                            "The address was used only in memory to select an NJDEP 2-mile grid. It was not stored, logged, or placed in this result URL.",
                            normalizeCounty(location.county()),
                            municipality,
                            grid.get().signals(),
                            "Historical PWTA summaries for the surrounding 2-mile grid, not this property or well."
                    );
                }
                if (municipality != null) {
                    return municipalityResolution(
                            municipality,
                            "The address matched New Jersey, but no 2-mile grid row covered the point. Municipality-level aggregate data is shown instead."
                    );
                }
            }
            if (selectedMunicipality.isEmpty()) {
                throw new IllegalArgumentException("We could not match that address. Choose a municipality to continue without storing the address.");
            }
            return municipalityResolution(
                    selectedMunicipality.get(),
                    "The address could not be matched, so this result uses the municipality summary you selected."
            );
        }
        return municipalityResolution(selectedMunicipality.orElseThrow(), "No address was used. This result uses municipality-level aggregate data.");
    }

    private LocationResolution municipalityResolution(NjMunicipalitySummary municipality, String notice) {
        return new LocationResolution(
                municipality.name() + ", " + municipality.county() + " County",
                "municipality aggregate",
                notice,
                municipality.county(),
                municipality,
                municipality.signals(),
                "Historical PWTA summaries for the municipality, not this property or well."
        );
    }

    private List<String> extraDiscussions(List<String> contexts, List<NjRiskSignal> signals) {
        LinkedHashSet<String> discussions = new LinkedHashSet<>();
        if (contexts.contains("infant_or_pregnant")) {
            discussions.add("Tell the laboratory about an infant or pregnancy so nitrate and lead timing is explicit.");
        }
        if (contexts.contains("recent_flood_or_repair")) {
            discussions.add("Discuss post-flood or post-repair microbial sampling and turbidity before relying on a transaction result.");
        }
        if (contexts.contains("older_plumbing")) {
            discussions.add("Ask how the required lead sample and any plumbing-specific follow-up should be collected.");
        }
        if (contexts.contains("nearby_contamination")) {
            discussions.add("Bring the known nearby contamination source to the certified lab; it may justify targeted testing beyond PWTA.");
        }
        if (contexts.contains("treatment_installed")) {
            discussions.add("Identify an untreated sampling point before the visit; PWTA sampling must follow the certified protocol.");
        }
        signals.stream().filter(NjRiskSignal::healthRelated).limit(3).forEach(signal ->
                discussions.add("Ask the lab to explain the required " + signal.label() + " result in light of the surrounding aggregate history."));
        if (discussions.isEmpty()) {
            discussions.add("Use the required PWTA panel first; add tests only when the certified lab or local health authority identifies a property-specific reason.");
        }
        return List.copyOf(discussions);
    }

    private List<String> mappedRiskContexts(List<String> contexts) {
        List<String> risks = new ArrayList<>();
        if (contexts.contains("nearby_contamination")) {
            risks.add("industrial");
        }
        if (contexts.contains("recent_flood_or_repair")) {
            risks.add("nearby_septic");
        }
        return risks.isEmpty() ? List.of("unknown") : List.copyOf(risks);
    }

    private String safeChannel(String channel) {
        return switch (channel) {
            case "partner" -> "partner";
            case "organic_local" -> "organic_local";
            default -> "direct";
        };
    }

    private String safeSource(String channel, String partnerSlug, NjMunicipalitySummary municipality) {
        if ("partner".equals(channel) && partnerCatalogService.active(partnerSlug).isPresent()) {
            return partnerSlug;
        }
        if ("organic_local".equals(channel) && municipality != null && municipality.pilot()) {
            return municipality.slug();
        }
        return "main";
    }

    private String areaLabel(NjMunicipalitySummary municipality, String county) {
        if (municipality != null) {
            return municipality.name() + ", " + municipality.county() + " County";
        }
        return normalizeCounty(county) + " County";
    }

    private String normalizeCounty(String county) {
        if (county == null || county.isBlank()) {
            return "New Jersey";
        }
        String value = county.trim();
        return value.toLowerCase(Locale.ROOT).endsWith(" county")
                ? value.substring(0, value.length() - 7).trim()
                : value;
    }

    private String token(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private record NormalizedRequest(
            String transactionType,
            String waterSource,
            String address,
            String municipalitySlug,
            List<String> extraContexts,
            String channel,
            String partnerSlug
    ) {
    }

    private record LocationResolution(
            String locationLabel,
            String resolution,
            String notice,
            String county,
            NjMunicipalitySummary municipality,
            List<NjRiskSignal> signals,
            String signalScope
    ) {
        private LocationResolution {
            signals = signals == null ? List.of() : List.copyOf(signals);
        }
    }
}
