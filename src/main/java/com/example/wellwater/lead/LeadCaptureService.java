package com.example.wellwater.lead;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeadCaptureService {

    private static final String HEADER = "submitted_at,email,name,state,note,source_type,source_family,source_slug,source_label,status";
    private final Path csvPath;

    public LeadCaptureService(@Value("${app.leads.csv.path:./data/leads/leads.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public synchronized Optional<LeadRecord> submit(LeadCaptureRequest request) {
        if (request == null || hasSpamSignal(request) || !validEmail(request.getEmail())) {
            return Optional.empty();
        }

        LeadRecord record = new LeadRecord(
                Instant.now().toString(),
                sanitize(request.getEmail()),
                sanitize(request.getName()),
                normalizeState(request.getState()),
                sanitize(request.getNote()),
                sanitize(request.getSourceType()),
                sanitize(request.getSourceFamily()),
                sanitize(request.getSourceSlug()),
                sanitize(request.getSourceLabel()),
                "new"
        );

        try {
            ensureReady();
            String row = String.join(",",
                    csv(record.submittedAt()),
                    csv(record.email()),
                    csv(record.name()),
                    csv(record.state()),
                    csv(record.note()),
                    csv(record.sourceType()),
                    csv(record.sourceFamily()),
                    csv(record.sourceSlug()),
                    csv(record.sourceLabel()),
                    csv(record.status())
            );
            Files.writeString(csvPath, row + System.lineSeparator(), StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
            return Optional.of(record);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write leads CSV: " + csvPath, e);
        }
    }

    public List<LeadRecord> allLeads() {
        if (!Files.exists(csvPath)) {
            return List.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return List.of();
            }
            Map<String, Integer> index = toHeaderIndex(parseCsvLine(header));
            List<LeadRecord> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cols = parseCsvLine(line);
                rows.add(new LeadRecord(
                        value(cols, index, "submitted_at"),
                        value(cols, index, "email"),
                        value(cols, index, "name"),
                        value(cols, index, "state"),
                        value(cols, index, "note"),
                        value(cols, index, "source_type"),
                        value(cols, index, "source_family"),
                        value(cols, index, "source_slug"),
                        value(cols, index, "source_label"),
                        value(cols, index, "status")
                ));
            }
            return rows.stream()
                    .sorted(Comparator.comparing(LeadCaptureService::submittedAtOrEpoch).reversed())
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read leads CSV at " + csvPath, e);
        }
    }

    public long totalLeads() {
        return allLeads().size();
    }

    public List<LeadRecord> recentLeads(int limit) {
        return allLeads().stream().limit(limit).toList();
    }

    public List<LeadCountRow> topLeadSlugs(int limit) {
        return groupedCounts(LeadRecord::sourceSlug, limit);
    }

    public List<LeadCountRow> topLeadFamilies(int limit) {
        return groupedCounts(LeadRecord::sourceFamily, limit);
    }

    private List<LeadCountRow> groupedCounts(java.util.function.Function<LeadRecord, String> classifier, int limit) {
        return allLeads().stream()
                .map(classifier)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.groupingBy(value -> value, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .map(entry -> new LeadCountRow(entry.getKey(), entry.getValue()))
                .toList();
    }

    private boolean hasSpamSignal(LeadCaptureRequest request) {
        return request.getWebsite() != null && !request.getWebsite().isBlank();
    }

    private boolean validEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String trimmed = email.trim();
        int at = trimmed.indexOf('@');
        int dot = trimmed.lastIndexOf('.');
        return at > 0 && dot > at + 1 && dot < trimmed.length() - 1;
    }

    private String normalizeState(String state) {
        if (state == null || state.isBlank()) {
            return "";
        }
        String trimmed = state.trim().toUpperCase();
        return trimmed.length() > 12 ? trimmed.substring(0, 12) : trimmed;
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        return trimmed.length() > 600 ? trimmed.substring(0, 600) : trimmed;
    }

    private void ensureReady() throws IOException {
        if (csvPath.getParent() != null) {
            Files.createDirectories(csvPath.getParent());
        }
        if (!Files.exists(csvPath)) {
            Files.writeString(csvPath, HEADER + System.lineSeparator(), StandardCharsets.UTF_8);
        }
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private Map<String, Integer> toHeaderIndex(List<String> headerColumns) {
        Map<String, Integer> index = new HashMap<>();
        for (int i = 0; i < headerColumns.size(); i++) {
            String key = headerColumns.get(i);
            if (i == 0 && key.startsWith("\uFEFF")) {
                key = key.substring(1);
            }
            index.put(key, i);
        }
        return index;
    }

    private String value(List<String> cols, Map<String, Integer> index, String key) {
        Integer position = index.get(key);
        if (position == null || position < 0 || position >= cols.size()) {
            return "";
        }
        return cols.get(position).trim();
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

    private static Instant submittedAtOrEpoch(LeadRecord record) {
        if (record == null || record.submittedAt() == null || record.submittedAt().isBlank()) {
            return Instant.EPOCH;
        }
        try {
            return Instant.parse(record.submittedAt());
        } catch (DateTimeParseException ignored) {
            return Instant.EPOCH;
        }
    }

    public record LeadRecord(
            String submittedAt,
            String email,
            String name,
            String state,
            String note,
            String sourceType,
            String sourceFamily,
            String sourceSlug,
            String sourceLabel,
            String status
    ) {
    }

    public record LeadCountRow(
            String label,
            long count
    ) {
    }
}
