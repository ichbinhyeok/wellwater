package com.example.wellwater.nj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class NjDistributionMetricService {

    private static final String HEADER = "date,event_name,channel,distribution_source,result_family,destination_type,outcome,latency_ms";
    private static final Set<String> EVENT_NAMES = Set.of(
            "landing_view", "tool_started", "preflight_completed", "preflight_failed",
            "official_clicked", "certified_lab_clicked", "partner_clicked"
    );
    private static final Set<String> CHANNELS = Set.of("direct", "organic_local", "partner");
    private static final System.Logger LOGGER = System.getLogger(NjDistributionMetricService.class.getName());

    private final Path csvPath;

    public NjDistributionMetricService(
            @Value("${app.nj.metrics.csv.path:./data/analytics/nj-distribution.csv}") String csvPath
    ) {
        this.csvPath = Paths.get(csvPath);
    }

    public synchronized void record(
            String eventName,
            String channel,
            String distributionSource,
            String resultFamily,
            String destinationType,
            String outcome,
            long latencyMs
    ) {
        String safeEvent = requiredToken(eventName, EVENT_NAMES, "event");
        String safeChannel = requiredToken(channel, CHANNELS, "channel");
        String safeSource = safeToken(distributionSource, 80, "main");
        String safeResultFamily = safeToken(resultFamily, 40, "");
        String safeDestination = safeToken(destinationType, 40, "");
        String safeOutcome = safeToken(outcome, 40, "");
        try {
            ensureReady();
            String row = String.join(",",
                    LocalDate.now().toString(),
                    safeEvent,
                    safeChannel,
                    safeSource,
                    safeResultFamily,
                    safeDestination,
                    safeOutcome,
                    Long.toString(Math.max(0L, latencyMs))
            );
            Files.writeString(csvPath, row + System.lineSeparator(), StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write NJ distribution metric CSV: " + csvPath, e);
        }
    }

    public boolean tryRecord(
            String eventName,
            String channel,
            String distributionSource,
            String resultFamily,
            String destinationType,
            String outcome,
            long latencyMs
    ) {
        try {
            record(eventName, channel, distributionSource, resultFamily, destinationType, outcome, latencyMs);
            return true;
        } catch (RuntimeException e) {
            LOGGER.log(System.Logger.Level.WARNING, "NJ distribution metric write failed: " + e.getMessage());
            return false;
        }
    }

    public synchronized Summary summary() {
        if (!Files.exists(csvPath)) {
            return new Summary(0L, Map.of(), Map.of(), Map.of());
        }
        try {
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            long total = 0L;
            Map<String, Long> byEvent = new LinkedHashMap<>();
            Map<String, Long> byChannel = new LinkedHashMap<>();
            Map<String, Long> bySource = new LinkedHashMap<>();
            for (int i = 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(",", -1);
                if (values.length < 8) {
                    continue;
                }
                total++;
                increment(byEvent, values[1]);
                increment(byChannel, values[2]);
                increment(bySource, values[3]);
            }
            return new Summary(total, Map.copyOf(byEvent), Map.copyOf(byChannel), Map.copyOf(bySource));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read NJ distribution metric CSV: " + csvPath, e);
        }
    }

    private void ensureReady() throws IOException {
        if (csvPath.getParent() != null) {
            Files.createDirectories(csvPath.getParent());
        }
        if (!Files.exists(csvPath)) {
            Files.writeString(csvPath, HEADER + System.lineSeparator(), StandardCharsets.UTF_8);
        }
    }

    private String requiredToken(String value, Set<String> allowed, String field) {
        String token = safeToken(value, 80, "");
        if (!allowed.contains(token)) {
            throw new IllegalArgumentException("Unsupported NJ distribution " + field + ".");
        }
        return token;
    }

    private String safeToken(String value, int maxLength, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String token = value.trim().toLowerCase(Locale.ROOT);
        if (token.length() > maxLength || !token.matches("[a-z0-9_-]+")) {
            return fallback;
        }
        return token;
    }

    private void increment(Map<String, Long> values, String key) {
        values.put(key, values.getOrDefault(key, 0L) + 1L);
    }

    public record Summary(
            long totalEvents,
            Map<String, Long> byEvent,
            Map<String, Long> byChannel,
            Map<String, Long> bySource
    ) {
    }
}
