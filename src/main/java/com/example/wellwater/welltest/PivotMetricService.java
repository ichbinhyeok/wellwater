package com.example.wellwater.welltest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PivotMetricService {

    private static final String HEADER = "date,event_name,channel,result_family,partner_product,outcome,latency_ms";
    private final Path csvPath;
    private LocalDate lastPrunedDate;

    public PivotMetricService(@Value("${app.pivot.metrics.csv.path:./data/analytics/pivot-metrics.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public synchronized void record(
            String eventName,
            String channel,
            String resultFamily,
            String partnerProduct,
            String outcome,
            long latencyMs
    ) {
        try {
            ensureReady();
            String row = String.join(",",
                    csv(LocalDate.now().toString()),
                    csv(eventName),
                    csv(channel),
                    csv(resultFamily),
                    csv(partnerProduct),
                    csv(outcome),
                    Long.toString(Math.max(0L, latencyMs))
            );
            Files.writeString(csvPath, row + System.lineSeparator(), StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write pivot metric CSV: " + csvPath, e);
        }
    }

    public synchronized PivotMetricSummary summary() {
        if (!Files.exists(csvPath)) {
            return PivotMetricSummary.empty();
        }
        try {
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            long completions = 0L;
            long failures = 0L;
            long partnerClicks = 0L;
            long successfulPartnerRedirects = 0L;
            long partnerEligibleCompletions = 0L;
            long totalCompletionLatencyMs = 0L;
            String firstDate = "";
            String lastDate = "";
            Map<String, Long> completionsByChannel = new LinkedHashMap<>();
            Map<String, Long> completionsByResultFamily = new LinkedHashMap<>();
            Map<String, Long> partnerClicksByProduct = new LinkedHashMap<>();
            Map<String, Long> eligibleCompletionsByPartnerProduct = new LinkedHashMap<>();

            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).isBlank()) {
                    continue;
                }
                List<String> columns = parseCsvLine(lines.get(i));
                if (columns.size() < 7) {
                    continue;
                }
                String date = columns.get(0);
                String eventName = columns.get(1);
                String channel = columns.get(2);
                String resultFamily = columns.get(3);
                String partnerProduct = columns.get(4);
                String outcome = columns.get(5);
                firstDate = firstDate.isBlank() || date.compareTo(firstDate) < 0 ? date : firstDate;
                lastDate = lastDate.isBlank() || date.compareTo(lastDate) > 0 ? date : lastDate;

                if ("tool_completed".equals(eventName)) {
                    completions++;
                    totalCompletionLatencyMs += parseLong(columns.get(6));
                    increment(completionsByChannel, channel);
                    increment(completionsByResultFamily, resultFamily);
                    if (!partnerProduct.isBlank()) {
                        partnerEligibleCompletions++;
                        increment(eligibleCompletionsByPartnerProduct, partnerProduct);
                    }
                } else if ("tool_failed".equals(eventName)) {
                    failures++;
                } else if ("partner_clicked".equals(eventName)) {
                    partnerClicks++;
                    increment(partnerClicksByProduct, partnerProduct);
                    if ("redirect".equals(outcome)) {
                        successfulPartnerRedirects++;
                    }
                }
            }

            long averageLatencyMs = completions == 0L ? 0L : totalCompletionLatencyMs / completions;
            return new PivotMetricSummary(
                    firstDate,
                    lastDate,
                    completions,
                    failures,
                    partnerEligibleCompletions,
                    partnerClicks,
                    successfulPartnerRedirects,
                    averageLatencyMs,
                    Map.copyOf(completionsByChannel),
                    Map.copyOf(completionsByResultFamily),
                    Map.copyOf(eligibleCompletionsByPartnerProduct),
                    Map.copyOf(partnerClicksByProduct)
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read pivot metric CSV: " + csvPath, e);
        }
    }

    private void ensureReady() throws IOException {
        if (csvPath.getParent() != null) {
            Files.createDirectories(csvPath.getParent());
        }
        if (!Files.exists(csvPath)) {
            Files.writeString(csvPath, HEADER + System.lineSeparator(), StandardCharsets.UTF_8);
        }
        LocalDate today = LocalDate.now();
        if (!today.equals(lastPrunedDate)) {
            pruneExpired(today.minusMonths(13));
            lastPrunedDate = today;
        }
    }

    private void pruneExpired(LocalDate cutoff) throws IOException {
        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        if (lines.size() <= 1) {
            return;
        }
        java.util.ArrayList<String> retained = new java.util.ArrayList<>();
        retained.add(lines.get(0));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank() || shouldRetain(line, cutoff)) {
                retained.add(line);
            }
        }
        if (retained.size() != lines.size()) {
            Files.write(csvPath, retained, StandardCharsets.UTF_8);
        }
    }

    private boolean shouldRetain(String line, LocalDate cutoff) {
        List<String> columns = parseCsvLine(line);
        if (columns.isEmpty()) {
            return true;
        }
        try {
            return !LocalDate.parse(columns.get(0)).isBefore(cutoff);
        } catch (DateTimeParseException ignored) {
            return true;
        }
    }

    private String csv(String value) {
        String safe = value == null ? "" : value;
        if (safe.contains(",") || safe.contains("\"") || safe.contains("\n") || safe.contains("\r")) {
            return "\"" + safe.replace("\"", "\"\"") + "\"";
        }
        return safe;
    }

    private List<String> parseCsvLine(String line) {
        java.util.ArrayList<String> columns = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char value = line.charAt(i);
            if (value == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (value == ',' && !quoted) {
                columns.add(current.toString());
                current.setLength(0);
            } else {
                current.append(value);
            }
        }
        columns.add(current.toString());
        return columns;
    }

    private long parseLong(String value) {
        try {
            return Math.max(0L, Long.parseLong(value));
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private void increment(Map<String, Long> counts, String key) {
        String safeKey = key == null || key.isBlank() ? "unknown" : key;
        counts.merge(safeKey, 1L, Long::sum);
    }

    public record PivotMetricSummary(
            String firstDate,
            String lastDate,
            long completions,
            long failures,
            long partnerEligibleCompletions,
            long partnerClicks,
            long successfulPartnerRedirects,
            long averageCompletionLatencyMs,
            Map<String, Long> completionsByChannel,
            Map<String, Long> completionsByResultFamily,
            Map<String, Long> eligibleCompletionsByPartnerProduct,
            Map<String, Long> partnerClicksByProduct
    ) {
        static PivotMetricSummary empty() {
            return new PivotMetricSummary("", "", 0L, 0L, 0L, 0L, 0L, 0L, Map.of(), Map.of(), Map.of(), Map.of());
        }
    }
}
