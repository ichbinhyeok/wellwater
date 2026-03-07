package com.example.wellwater.decision.registry;

import com.example.wellwater.decision.model.ActionMode;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Scope;
import com.example.wellwater.decision.model.Tier;
import com.example.wellwater.decision.model.Urgency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DecisionRegistryService {

    private final Path contaminantPath;
    private final Path symptomPath;
    private final Path triggerPath;

    public DecisionRegistryService(
            @Value("${app.registry.contaminant.path:./data/registry/contaminant_registry.csv}") String contaminantPath,
            @Value("${app.registry.symptom.path:./data/registry/symptom_registry.csv}") String symptomPath,
            @Value("${app.registry.trigger.path:./data/registry/trigger_registry.csv}") String triggerPath
    ) {
        this.contaminantPath = Paths.get(contaminantPath);
        this.symptomPath = Paths.get(symptomPath);
        this.triggerPath = Paths.get(triggerPath);
    }

    public Optional<RuleSignal> findContaminant(String key) {
        return findByKey(loadSignals(contaminantPath, "canonical_name"), key);
    }

    public Optional<RuleSignal> findSymptom(String key) {
        return findByKey(loadSignals(symptomPath, "symptom_name"), key);
    }

    public Optional<RuleSignal> findTrigger(String key) {
        return findByKey(loadSignals(triggerPath, "trigger_name"), key);
    }

    private Optional<RuleSignal> findByKey(Map<String, RuleSignal> map, String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.get(key.trim().toLowerCase()));
    }

    private Map<String, RuleSignal> loadSignals(Path path, String keyColumn) {
        if (!Files.exists(path)) {
            return Map.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return Map.of();
            }
            Map<String, Integer> index = headerIndex(parseCsvLine(header));
            Map<String, RuleSignal> signals = new LinkedHashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cols = parseCsvLine(line);
                RuleSignal signal = toSignal(cols, index, keyColumn);
                signals.put(signal.key(), signal);
            }
            return signals;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load registry CSV: " + path, e);
        }
    }

    private RuleSignal toSignal(List<String> cols, Map<String, Integer> idx, String keyColumn) {
        String key = value(cols, idx, keyColumn).toLowerCase();
        return new RuleSignal(
                key,
                parseTier(value(cols, idx, "support_tier")),
                parseProblemType(value(cols, idx, "problem_type_default")),
                parseUrgency(value(cols, idx, "default_urgency")),
                parseScope(value(cols, idx, "default_scope")),
                parseActionMode(value(cols, idx, "default_action_mode")),
                parseListField(value(cols, idx, "claim_requirements")),
                parseListField(value(cols, idx, "sources"))
        );
    }

    private Tier parseTier(String value) {
        String normalized = value.trim().toUpperCase();
        if (normalized.equals("A")) {
            return Tier.A;
        }
        if (normalized.equals("B")) {
            return Tier.B;
        }
        return Tier.C;
    }

    private ProblemType parseProblemType(String value) {
        return switch (value.trim().toLowerCase()) {
            case "microbial" -> ProblemType.MICROBIAL;
            case "chemical-health" -> ProblemType.CHEMICAL_HEALTH;
            case "corrosion" -> ProblemType.CORROSION;
            case "aesthetic-operational" -> ProblemType.AESTHETIC_OPERATIONAL;
            default -> ProblemType.UNSUPPORTED;
        };
    }

    private Urgency parseUrgency(String value) {
        return switch (value.trim().toLowerCase()) {
            case "immediate" -> Urgency.IMMEDIATE;
            case "prompt" -> Urgency.PROMPT;
            default -> Urgency.ROUTINE;
        };
    }

    private Scope parseScope(String value) {
        return switch (value.trim().toLowerCase()) {
            case "drinking-only" -> Scope.DRINKING_ONLY;
            case "whole-house" -> Scope.WHOLE_HOUSE;
            case "both" -> Scope.BOTH;
            default -> Scope.UNCLEAR;
        };
    }

    private ActionMode parseActionMode(String value) {
        return switch (value.trim().toLowerCase()) {
            case "use_alternate_water" -> ActionMode.USE_ALTERNATE_WATER;
            case "boil" -> ActionMode.BOIL;
            case "do_not_boil" -> ActionMode.DO_NOT_BOIL;
            case "compare_treatment" -> ActionMode.COMPARE_TREATMENT;
            case "inspect_source" -> ActionMode.INSPECT_SOURCE;
            case "contact_local_guidance" -> ActionMode.CONTACT_LOCAL_GUIDANCE;
            default -> ActionMode.RETEST;
        };
    }

    private List<String> parseListField(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        String[] parts = value.split("\\|");
        List<String> out = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isBlank()) {
                out.add(trimmed);
            }
        }
        return out;
    }

    private Map<String, Integer> headerIndex(List<String> columns) {
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            String key = columns.get(i);
            if (i == 0 && key.startsWith("\uFEFF")) {
                key = key.substring(1);
            }
            idx.put(key, i);
        }
        return idx;
    }

    private String value(List<String> cols, Map<String, Integer> idx, String key) {
        Integer i = idx.get(key);
        if (i == null || i < 0 || i >= cols.size()) {
            return "";
        }
        return cols.get(i).trim();
    }

    private List<String> parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }
            if (ch == ',' && !inQuotes) {
                out.add(current.toString());
                current.setLength(0);
                continue;
            }
            current.append(ch);
        }
        out.add(current.toString());
        return out;
    }
}

