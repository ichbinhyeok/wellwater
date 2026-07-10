package com.example.wellwater.welltest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PivotMetricService {

    private static final String HEADER = "date,event_name,channel,result_family,partner_product,outcome,latency_ms";
    private static final System.Logger LOGGER = System.getLogger(PivotMetricService.class.getName());
    private final Path csvPath;
    private final LocalDate experimentStartDate;
    private LocalDate lastPrunedDate;

    @Autowired
    public PivotMetricService(
            @Value("${app.pivot.metrics.csv.path:./data/analytics/pivot-metrics.csv}") String csvPath,
            @Value("${app.pivot.experiment-start-date:}") String experimentStartDate
    ) {
        this.csvPath = Paths.get(csvPath);
        this.experimentStartDate = parseExperimentStartDate(experimentStartDate);
    }

    public PivotMetricService(String csvPath) {
        this(csvPath, "");
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

    public boolean tryRecord(
            String eventName,
            String channel,
            String resultFamily,
            String partnerProduct,
            String outcome,
            long latencyMs
    ) {
        try {
            record(eventName, channel, resultFamily, partnerProduct, outcome, latencyMs);
            return true;
        } catch (RuntimeException e) {
            LOGGER.log(System.Logger.Level.WARNING, "Pivot metric write failed: " + e.getMessage());
            return false;
        }
    }

    public synchronized PivotMetricSummary summary() {
        if (!Files.exists(csvPath)) {
            return emptySummary();
        }
        try {
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            long completions = 0L;
            long failures = 0L;
            long partnerClicks = 0L;
            long successfulPartnerRedirects = 0L;
            long partnerEligibleCompletions = 0L;
            long resourceClicks = 0L;
            long officialGuidanceClicks = 0L;
            long certifiedLabClicks = 0L;
            long externalActionClicks = 0L;
            long totalCompletionLatencyMs = 0L;
            String firstDate = "";
            String lastDate = "";
            Map<String, Long> completionsByChannel = new LinkedHashMap<>();
            Map<String, Long> failuresByChannel = new LinkedHashMap<>();
            Map<String, Long> completionsByResultFamily = new LinkedHashMap<>();
            Map<String, Long> partnerClicksByProduct = new LinkedHashMap<>();
            Map<String, Long> eligibleCompletionsByPartnerProduct = new LinkedHashMap<>();
            Map<String, Long> eligibleCompletionsByChannel = new LinkedHashMap<>();
            Map<String, Long> successfulPartnerRedirectsByChannel = new LinkedHashMap<>();
            Map<String, Long> resourceClicksByType = new LinkedHashMap<>();
            Map<String, Long> externalActionsByChannel = new LinkedHashMap<>();

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
                if (isBeforeExperiment(date)) {
                    continue;
                }
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
                        increment(eligibleCompletionsByChannel, channel);
                    }
                } else if ("tool_failed".equals(eventName)) {
                    failures++;
                    increment(failuresByChannel, channel);
                } else if ("partner_clicked".equals(eventName)) {
                    partnerClicks++;
                    increment(partnerClicksByProduct, partnerProduct);
                    if ("redirect".equals(outcome)) {
                        successfulPartnerRedirects++;
                        increment(successfulPartnerRedirectsByChannel, channel);
                        externalActionClicks++;
                        increment(externalActionsByChannel, channel);
                    }
                } else if ("resource_clicked".equals(eventName)
                        && ("official_guidance".equals(outcome) || "certified_lab".equals(outcome))) {
                    resourceClicks++;
                    externalActionClicks++;
                    increment(resourceClicksByType, outcome);
                    increment(externalActionsByChannel, channel);
                    if ("official_guidance".equals(outcome)) {
                        officialGuidanceClicks++;
                    } else {
                        certifiedLabClicks++;
                    }
                }
            }

            long averageLatencyMs = completions == 0L ? 0L : totalCompletionLatencyMs / completions;
            long chatgptCompletions = completionsByChannel.getOrDefault("chatgpt", 0L);
            long chatgptFailures = failuresByChannel.getOrDefault("chatgpt", 0L);
            long chatgptExternalActions = externalActionsByChannel.getOrDefault("chatgpt", 0L);
            long chatgptPartnerEligibleCompletions = eligibleCompletionsByChannel.getOrDefault("chatgpt", 0L);
            long chatgptPartnerRedirects = successfulPartnerRedirectsByChannel.getOrDefault("chatgpt", 0L);
            return new PivotMetricSummary(
                    experimentStartDate == null ? "" : experimentStartDate.toString(),
                    experimentDay(),
                    firstDate,
                    lastDate,
                    completions,
                    failures,
                    partnerEligibleCompletions,
                    partnerClicks,
                    successfulPartnerRedirects,
                    resourceClicks,
                    officialGuidanceClicks,
                    certifiedLabClicks,
                    externalActionClicks,
                    chatgptCompletions,
                    chatgptFailures,
                    chatgptExternalActions,
                    chatgptPartnerEligibleCompletions,
                    chatgptPartnerRedirects,
                    percent(chatgptFailures, chatgptCompletions + chatgptFailures),
                    percent(chatgptExternalActions, chatgptCompletions),
                    percent(chatgptPartnerRedirects, chatgptPartnerEligibleCompletions),
                    averageLatencyMs,
                    Map.copyOf(completionsByChannel),
                    Map.copyOf(failuresByChannel),
                    Map.copyOf(completionsByResultFamily),
                    Map.copyOf(eligibleCompletionsByPartnerProduct),
                    Map.copyOf(eligibleCompletionsByChannel),
                    Map.copyOf(partnerClicksByProduct),
                    Map.copyOf(successfulPartnerRedirectsByChannel),
                    Map.copyOf(resourceClicksByType),
                    Map.copyOf(externalActionsByChannel)
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read pivot metric CSV: " + csvPath, e);
        }
    }

    private boolean isBeforeExperiment(String date) {
        if (experimentStartDate == null) {
            return false;
        }
        try {
            return LocalDate.parse(date).isBefore(experimentStartDate);
        } catch (DateTimeParseException ignored) {
            return true;
        }
    }

    private LocalDate parseExperimentStartDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("app.pivot.experiment-start-date must use YYYY-MM-DD.", e);
        }
    }

    private long experimentDay() {
        if (experimentStartDate == null || LocalDate.now().isBefore(experimentStartDate)) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(experimentStartDate, LocalDate.now()) + 1L;
    }

    private double percent(long numerator, long denominator) {
        if (denominator <= 0L) {
            return 0.0d;
        }
        return Math.round((numerator * 10_000.0d) / denominator) / 100.0d;
    }

    private PivotMetricSummary emptySummary() {
        return new PivotMetricSummary(
                experimentStartDate == null ? "" : experimentStartDate.toString(),
                experimentDay(),
                "", "",
                0L, 0L, 0L, 0L, 0L,
                0L, 0L, 0L, 0L,
                0L, 0L, 0L,
                0L, 0L,
                0.0d, 0.0d, 0.0d, 0L,
                Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of()
        );
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
            String experimentStartDate,
            long experimentDay,
            String firstDate,
            String lastDate,
            long completions,
            long failures,
            long partnerEligibleCompletions,
            long partnerClicks,
            long successfulPartnerRedirects,
            long resourceClicks,
            long officialGuidanceClicks,
            long certifiedLabClicks,
            long externalActionClicks,
            long chatgptCompletions,
            long chatgptFailures,
            long chatgptExternalActions,
            long chatgptPartnerEligibleCompletions,
            long chatgptPartnerRedirects,
            double chatgptFailureRatePct,
            double chatgptExternalActionRatePct,
            double partnerEligibleClickRatePct,
            long averageCompletionLatencyMs,
            Map<String, Long> completionsByChannel,
            Map<String, Long> failuresByChannel,
            Map<String, Long> completionsByResultFamily,
            Map<String, Long> eligibleCompletionsByPartnerProduct,
            Map<String, Long> eligibleCompletionsByChannel,
            Map<String, Long> partnerClicksByProduct,
            Map<String, Long> successfulPartnerRedirectsByChannel,
            Map<String, Long> resourceClicksByType,
            Map<String, Long> externalActionsByChannel
    ) {
    }
}
