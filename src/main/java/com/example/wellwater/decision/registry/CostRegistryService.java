package com.example.wellwater.decision.registry;

import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Scope;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CostRegistryService {

    private final Path csvPath;

    public CostRegistryService(@Value("${app.registry.cost.path:./data/registry/cost_registry.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public Optional<CostProfile> find(ProblemType problemType, Scope scope) {
        List<CostProfile> profiles = load();
        String pType = problemType.wireValue();
        String pScope = scope.wireValue();

        return profiles.stream()
                .filter(p -> p.problemType().equals(pType) && p.scope().equals(pScope))
                .findFirst()
                .or(() -> profiles.stream()
                        .filter(p -> p.problemType().equals(pType) && p.scope().equals("any"))
                        .findFirst())
                .or(() -> profiles.stream()
                        .filter(p -> p.problemType().equals("any") && p.scope().equals("any"))
                        .findFirst());
    }

    private List<CostProfile> load() {
        if (!Files.exists(csvPath)) {
            return List.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return List.of();
            }
            Map<String, Integer> idx = headerIndex(parseCsvLine(header));
            List<CostProfile> out = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cols = parseCsvLine(line);
                out.add(new CostProfile(
                        value(cols, idx, "problem_type").toLowerCase(),
                        value(cols, idx, "scope").toLowerCase(),
                        value(cols, idx, "install_range"),
                        value(cols, idx, "maintenance_range"),
                        value(cols, idx, "source_url")
                ));
            }
            return out;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load cost registry CSV: " + csvPath, e);
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
