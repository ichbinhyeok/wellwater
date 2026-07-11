package com.example.wellwater.nj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NjPwtaDataService {

    public static final String SNAPSHOT_DATE = "2026-07-11";
    public static final int EXPECTED_GRID_COUNT = 1_676;
    public static final int EXPECTED_MUNICIPALITY_COUNT = 564;
    public static final String GRID_SHA256 = "5589133cebcfb42f419e2eee6d7a5220517c1d0455b65fcbfc0361ea396a6563";
    public static final String MUNICIPALITY_SHA256 = "40eb9c47ece32a5b869b07cb90a13f4696f1806cf28a59a28346c14cc7904e14";

    private static final String GRID_RESOURCE = "classpath:data/nj/nj-pwta-grids-2026-07-11.geojson";
    private static final String MUNICIPALITY_RESOURCE = "classpath:data/nj/nj-pwta-municipalities-2026-07-11.json";
    private static final String PILOT_RESOURCE = "classpath:data/nj/pilot-municipalities.csv";
    private static final Pattern UPPER_PERCENT = Pattern.compile("to\\s+(\\d+)\\s+percent", Pattern.CASE_INSENSITIVE);
    private static final Map<String, SignalDefinition> SIGNALS = signalDefinitions();

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private volatile List<GridSummary> grids = List.of();
    private volatile List<NjMunicipalitySummary> municipalities = List.of();
    private volatile Map<String, NjMunicipalitySummary> municipalitiesBySlug = Map.of();

    public NjPwtaDataService(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void load() {
        try {
            byte[] gridBytes = readRequired(GRID_RESOURCE);
            byte[] municipalityBytes = readRequired(MUNICIPALITY_RESOURCE);
            verifyChecksum(gridBytes, GRID_SHA256, "NJ PWTA grid snapshot");
            verifyChecksum(municipalityBytes, MUNICIPALITY_SHA256, "NJ PWTA municipality snapshot");

            Set<String> pilotSlugs = readPilotSlugs();
            List<GridSummary> loadedGrids = parseGrids(gridBytes);
            List<NjMunicipalitySummary> loadedMunicipalities = parseMunicipalities(municipalityBytes, pilotSlugs);
            if (loadedGrids.size() != EXPECTED_GRID_COUNT) {
                throw new IllegalStateException("NJ PWTA grid snapshot must contain " + EXPECTED_GRID_COUNT + " rows.");
            }
            if (loadedMunicipalities.size() != EXPECTED_MUNICIPALITY_COUNT) {
                throw new IllegalStateException("NJ PWTA municipality snapshot must contain " + EXPECTED_MUNICIPALITY_COUNT + " rows.");
            }
            long matchedPilots = loadedMunicipalities.stream().filter(NjMunicipalitySummary::pilot).count();
            if (matchedPilots != pilotSlugs.size()) {
                throw new IllegalStateException("Every NJ pilot municipality must match the official snapshot.");
            }

            Map<String, NjMunicipalitySummary> bySlug = new LinkedHashMap<>();
            for (NjMunicipalitySummary municipality : loadedMunicipalities) {
                if (bySlug.put(municipality.slug(), municipality) != null) {
                    throw new IllegalStateException("Duplicate NJ municipality slug: " + municipality.slug());
                }
            }
            this.grids = List.copyOf(loadedGrids);
            this.municipalities = List.copyOf(loadedMunicipalities);
            this.municipalitiesBySlug = Map.copyOf(bySlug);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load NJ PWTA snapshots.", e);
        }
    }

    public List<NjMunicipalitySummary> allMunicipalities() {
        return municipalities.stream()
                .sorted(Comparator.comparing(NjMunicipalitySummary::name).thenComparing(NjMunicipalitySummary::county))
                .toList();
    }

    public List<NjMunicipalitySummary> pilotMunicipalities() {
        return municipalities.stream()
                .filter(NjMunicipalitySummary::pilot)
                .sorted(Comparator.comparing(NjMunicipalitySummary::name).thenComparing(NjMunicipalitySummary::county))
                .toList();
    }

    public Optional<NjMunicipalitySummary> findMunicipality(String slug) {
        if (slug == null || slug.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(municipalitiesBySlug.get(slug.trim().toLowerCase(Locale.ROOT)));
    }

    public Optional<NjMunicipalitySummary> matchMunicipality(String name, String county) {
        String normalizedName = normalizeName(name);
        String normalizedCounty = normalizeCounty(county);
        if (normalizedName.isBlank() || normalizedCounty.isBlank()) {
            return Optional.empty();
        }
        Optional<NjMunicipalitySummary> exact = municipalities.stream()
                .filter(value -> normalizeName(value.name()).equals(normalizedName))
                .filter(value -> normalizeCounty(value.county()).equals(normalizedCounty))
                .findFirst();
        if (exact.isPresent()) {
            return exact;
        }
        String withoutType = removeMunicipalityType(normalizedName);
        return municipalities.stream()
                .filter(value -> removeMunicipalityType(normalizeName(value.name())).equals(withoutType))
                .filter(value -> normalizeCounty(value.county()).equals(normalizedCounty))
                .findFirst();
    }

    public Optional<GridMatch> findGrid(double longitude, double latitude) {
        return grids.stream()
                .filter(grid -> grid.contains(longitude, latitude))
                .findFirst()
                .map(grid -> new GridMatch(grid.gridId(), grid.signals()));
    }

    public int gridCount() {
        return grids.size();
    }

    public int municipalityCount() {
        return municipalities.size();
    }

    private List<GridSummary> parseGrids(byte[] bytes) throws IOException {
        JsonNode features = objectMapper.readTree(bytes).path("features");
        List<GridSummary> out = new ArrayList<>();
        for (JsonNode feature : features) {
            JsonNode properties = feature.path("properties");
            long gridId = properties.path("GRIDID").asLong();
            List<List<Point>> polygons = polygons(feature.path("geometry"));
            if (polygons.isEmpty()) {
                throw new IllegalStateException("NJ PWTA grid has no polygon: " + gridId);
            }
            out.add(new GridSummary(gridId, polygons, riskSignals(properties)));
        }
        return out;
    }

    private List<NjMunicipalitySummary> parseMunicipalities(byte[] bytes, Set<String> pilotSlugs) throws IOException {
        JsonNode features = objectMapper.readTree(bytes).path("features");
        List<NjMunicipalitySummary> out = new ArrayList<>();
        for (JsonNode feature : features) {
            JsonNode attributes = feature.path("attributes");
            String name = attributes.path("MUN_LABEL").asText("").trim();
            String county = titleCase(attributes.path("COUNTY").asText(""));
            String slug = slug(name, county);
            out.add(new NjMunicipalitySummary(
                    slug,
                    name,
                    county,
                    attributes.path("MUN_CODE").asText(""),
                    riskSignals(attributes),
                    pilotSlugs.contains(slug)
            ));
        }
        return out;
    }

    private List<NjRiskSignal> riskSignals(JsonNode properties) {
        List<NjRiskSignal> signals = new ArrayList<>();
        for (SignalDefinition definition : SIGNALS.values()) {
            String observed = properties.path(definition.field()).asText("").trim();
            if (!observed.toLowerCase(Locale.ROOT).startsWith("greater than")) {
                continue;
            }
            signals.add(new NjRiskSignal(
                    definition.code(),
                    definition.label(),
                    observed,
                    upperPercent(observed),
                    definition.healthRelated()
            ));
        }
        signals.sort(Comparator.comparingInt(NjRiskSignal::upperPercent).reversed()
                .thenComparing(NjRiskSignal::healthRelated, Comparator.reverseOrder())
                .thenComparing(NjRiskSignal::label));
        return List.copyOf(signals);
    }

    private int upperPercent(String observed) {
        Matcher matcher = UPPER_PERCENT.matcher(observed);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 1;
    }

    private List<List<Point>> polygons(JsonNode geometry) {
        String type = geometry.path("type").asText("");
        JsonNode coordinates = geometry.path("coordinates");
        List<List<Point>> out = new ArrayList<>();
        if ("Polygon".equals(type)) {
            addOuterRing(out, coordinates);
        } else if ("MultiPolygon".equals(type)) {
            for (JsonNode polygon : coordinates) {
                addOuterRing(out, polygon);
            }
        }
        return List.copyOf(out);
    }

    private void addOuterRing(List<List<Point>> out, JsonNode polygon) {
        if (!polygon.isArray() || polygon.isEmpty()) {
            return;
        }
        List<Point> ring = new ArrayList<>();
        for (JsonNode coordinate : polygon.get(0)) {
            if (coordinate.isArray() && coordinate.size() >= 2) {
                ring.add(new Point(coordinate.get(0).asDouble(), coordinate.get(1).asDouble()));
            }
        }
        if (ring.size() >= 3) {
            out.add(List.copyOf(ring));
        }
    }

    private Set<String> readPilotSlugs() throws IOException {
        byte[] bytes = readRequired(PILOT_RESOURCE);
        Set<String> slugs = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String value = line.trim();
                if (!value.isBlank()) {
                    slugs.add(value.toLowerCase(Locale.ROOT));
                }
            }
        }
        if (slugs.size() < 20 || slugs.size() > 25) {
            throw new IllegalStateException("NJ pilot municipality inventory must contain 20 to 25 unique slugs.");
        }
        return Set.copyOf(slugs);
    }

    private byte[] readRequired(String location) throws IOException {
        Resource resource = resourceLoader.getResource(location);
        if (!resource.exists()) {
            throw new IllegalStateException("Missing NJ data resource: " + location);
        }
        return resource.getInputStream().readAllBytes();
    }

    private void verifyChecksum(byte[] bytes, String expected, String label) {
        try {
            String actual = java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
            if (!actual.equalsIgnoreCase(expected)) {
                throw new IllegalStateException(label + " checksum mismatch. Expected " + expected + " but got " + actual + ".");
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable.", e);
        }
    }

    private static Map<String, SignalDefinition> signalDefinitions() {
        Map<String, SignalDefinition> values = new LinkedHashMap<>();
        values.put("NIT_GROUP", new SignalDefinition("nitrate", "Nitrate", "NIT_GROUP", true));
        values.put("FE_GROUP", new SignalDefinition("iron", "Iron", "FE_GROUP", false));
        values.put("MN_GROUP", new SignalDefinition("manganese", "Manganese", "MN_GROUP", false));
        values.put("VOC_GROUP", new SignalDefinition("voc", "Volatile organic compounds", "VOC_GROUP", true));
        values.put("TC_GROUP", new SignalDefinition("total-coliform", "Total coliform", "TC_GROUP", true));
        values.put("FCEC_GROUP", new SignalDefinition("fecal-coliform", "Fecal coliform or E. coli", "FCEC_GROUP", true));
        values.put("PH_GROUP", new SignalDefinition("ph", "pH outside the optimum range", "PH_GROUP", false));
        values.put("AS_GROUP", new SignalDefinition("arsenic", "Arsenic", "AS_GROUP", true));
        values.put("GA_GROUP", new SignalDefinition("gross-alpha", "Gross alpha", "GA_GROUP", true));
        values.put("HG_GROUP", new SignalDefinition("mercury", "Mercury", "HG_GROUP", true));
        values.put("SOC_GROUP", new SignalDefinition("soc", "Synthetic organic compounds", "SOC_GROUP", true));
        values.put("PFOA_GROUP", new SignalDefinition("pfoa", "PFOA", "PFOA_GROUP", true));
        values.put("PFOS_GROUP", new SignalDefinition("pfos", "PFOS", "PFOS_GROUP", true));
        values.put("PFNA_GROUP", new SignalDefinition("pfna", "PFNA", "PFNA_GROUP", true));
        values.put("PFAS_GROUP", new SignalDefinition("pfas", "At least one regulated PFAS", "PFAS_GROUP", true));
        values.put("URANIUM_GROUP", new SignalDefinition("uranium", "Uranium", "URANIUM_GROUP", true));
        return Map.copyOf(values);
    }

    private String slug(String name, String county) {
        return slugPart(name) + "-" + slugPart(county);
    }

    private String slugPart(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    private String normalizeName(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private String normalizeCounty(String value) {
        String normalized = normalizeName(value);
        return normalized.endsWith(" county") ? normalized.substring(0, normalized.length() - 7).trim() : normalized;
    }

    private String removeMunicipalityType(String value) {
        return value.replaceAll("\\s+(borough|township|city|town|village)$", "").trim();
    }

    private String titleCase(String value) {
        StringBuilder out = new StringBuilder();
        for (String token : value.trim().toLowerCase(Locale.ROOT).split("\\s+")) {
            if (!out.isEmpty()) {
                out.append(' ');
            }
            if (!token.isBlank()) {
                out.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1));
            }
        }
        return out.toString();
    }

    public record GridMatch(long gridId, List<NjRiskSignal> signals) {
        public GridMatch {
            signals = signals == null ? List.of() : List.copyOf(signals);
        }
    }

    private record SignalDefinition(String code, String label, String field, boolean healthRelated) {
    }

    private record Point(double x, double y) {
    }

    private record GridSummary(long gridId, List<List<Point>> polygons, List<NjRiskSignal> signals) {
        private boolean contains(double x, double y) {
            return polygons.stream().anyMatch(polygon -> contains(polygon, x, y));
        }

        private boolean contains(List<Point> polygon, double x, double y) {
            boolean inside = false;
            for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
                Point a = polygon.get(j);
                Point b = polygon.get(i);
                if (onSegment(a, b, x, y)) {
                    return true;
                }
                boolean crosses = ((b.y() > y) != (a.y() > y))
                        && (x < (a.x() - b.x()) * (y - b.y()) / (a.y() - b.y()) + b.x());
                if (crosses) {
                    inside = !inside;
                }
            }
            return inside;
        }

        private boolean onSegment(Point a, Point b, double x, double y) {
            double cross = (x - a.x()) * (b.y() - a.y()) - (y - a.y()) * (b.x() - a.x());
            if (Math.abs(cross) > 1e-9) {
                return false;
            }
            return x >= Math.min(a.x(), b.x()) - 1e-9 && x <= Math.max(a.x(), b.x()) + 1e-9
                    && y >= Math.min(a.y(), b.y()) - 1e-9 && y <= Math.max(a.y(), b.y()) + 1e-9;
        }
    }
}
