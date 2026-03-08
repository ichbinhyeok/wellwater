package com.example.wellwater.pseo;

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
public class RegionalContextRegistryService {

    private final Path csvPath;

    public RegionalContextRegistryService(@Value("${app.registry.regional-context.path:./data/registry/regional_context_registry.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public Optional<RegionalContextRow> findBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return Optional.empty();
        }
        return load().stream()
                .filter(row -> row.slug().equalsIgnoreCase(slug.trim()))
                .findFirst();
    }

    private List<RegionalContextRow> load() {
        if (!Files.exists(csvPath)) {
            return List.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return List.of();
            }
            Map<String, Integer> idx = headerIndex(parseCsvLine(header));
            List<RegionalContextRow> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cols = parseCsvLine(line);
                rows.add(new RegionalContextRow(
                        value(cols, idx, "slug"),
                        value(cols, idx, "state_code"),
                        value(cols, idx, "state_name"),
                        value(cols, idx, "local_delta"),
                        value(cols, idx, "decision_trigger"),
                        value(cols, idx, "lab_note")
                ));
            }
            return List.copyOf(rows);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load regional context CSV at " + csvPath, e);
        }
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

    public record RegionalContextRow(
            String slug,
            String stateCode,
            String stateName,
            String localDelta,
            String decisionTrigger,
            String labNote
    ) {
    }
}
