package com.example.wellwater.decision.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class StateResourceRegistryService {

    private final Path csvPath;

    public StateResourceRegistryService(@Value("${app.registry.state.path:./data/registry/state_resource_registry.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public Optional<StateResource> findByState(String stateCode) {
        List<StateResource> rows = load();
        if (stateCode != null && !stateCode.isBlank()) {
            String key = stateCode.trim().toUpperCase();
            Optional<StateResource> match = rows.stream()
                    .filter(row -> row.stateCode().equalsIgnoreCase(key))
                    .findFirst();
            if (match.isPresent()) {
                return match;
            }
        }
        return rows.stream()
                .filter(row -> row.stateCode().equalsIgnoreCase("US"))
                .findFirst();
    }

    public int supportedStateCount() {
        return (int) load().stream()
                .map(StateResource::stateCode)
                .filter(code -> code != null && !code.isBlank())
                .filter(code -> !"US".equalsIgnoreCase(code))
                .distinct()
                .count();
    }

    public Set<String> allowedOutboundHosts() {
        LinkedHashSet<String> hosts = new LinkedHashSet<>();
        for (StateResource row : load()) {
            addHost(hosts, row.localGuidanceUrl());
            addHost(hosts, row.certifiedLabUrl());
            addHost(hosts, row.sourceUrl());
        }
        return Set.copyOf(hosts);
    }

    private List<StateResource> load() {
        if (!Files.exists(csvPath)) {
            return List.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return List.of();
            }
            Map<String, Integer> idx = headerIndex(parseCsvLine(header));
            List<StateResource> out = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cols = parseCsvLine(line);
                out.add(new StateResource(
                        value(cols, idx, "state"),
                        value(cols, idx, "local_guidance_url"),
                        value(cols, idx, "certified_lab_url"),
                        value(cols, idx, "source_url")
                ));
            }
            return out;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load state resource CSV: " + csvPath, e);
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

    private void addHost(Set<String> hosts, String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        try {
            URI uri = URI.create(url.trim());
            if (uri.getHost() != null && !uri.getHost().isBlank()) {
                hosts.add(uri.getHost().toLowerCase(Locale.ROOT));
            }
        } catch (IllegalArgumentException ignored) {
            // Ignore malformed registry URLs so one bad row does not break startup.
        }
    }
}
