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
import java.util.stream.Collectors;

@Service
public class PseoCitationRegistryService {

    private final Path csvPath;

    public PseoCitationRegistryService(@Value("${app.pseo.citations.path:./data/pseo/page_sources.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public List<PseoCitation> findBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return List.of();
        }
        return load().getOrDefault(slug.trim(), List.of());
    }

    private Map<String, List<PseoCitation>> load() {
        if (!Files.exists(csvPath)) {
            return Map.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return Map.of();
            }
            Map<String, Integer> idx = headerIndex(parseCsvLine(header));
            Map<String, List<PseoCitation>> rows = new LinkedHashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cols = parseCsvLine(line);
                String slug = value(cols, idx, "slug");
                String label = value(cols, idx, "label");
                String url = value(cols, idx, "url");
                if (slug.isBlank() || label.isBlank() || url.isBlank()) {
                    continue;
                }
                rows.computeIfAbsent(slug, ignored -> new ArrayList<>())
                        .add(new PseoCitation(label, url));
            }
            return rows.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> List.copyOf(entry.getValue()),
                            (left, right) -> left,
                            LinkedHashMap::new
                    ));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load pSEO citation CSV at " + csvPath, e);
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
}
