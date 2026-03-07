package com.example.wellwater.admin;

import com.example.wellwater.lead.LeadCaptureService;
import com.example.wellwater.pseo.PseoCatalogService;
import com.example.wellwater.pseo.PseoPage;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminDashboardService {

    private final Path analyticsCsvPath;
    private final PseoCatalogService pseoCatalogService;
    private final LeadCaptureService leadCaptureService;

    public AdminDashboardService(
            @Value("${app.analytics.csv.path:./data/analytics/events.csv}") String analyticsCsvPath,
            PseoCatalogService pseoCatalogService,
            LeadCaptureService leadCaptureService
    ) {
        this.analyticsCsvPath = Paths.get(analyticsCsvPath);
        this.pseoCatalogService = pseoCatalogService;
        this.leadCaptureService = leadCaptureService;
    }

    public DashboardSnapshot snapshot() {
        List<PseoPage> pages = pseoCatalogService.allPages();
        List<AnalyticsEventRow> events = analyticsEvents();
        List<LeadCaptureService.LeadRecord> leads = leadCaptureService.allLeads();

        Map<String, Long> funnel = orderedCounts(
                events,
                AnalyticsEventRow::eventName,
                List.of("entry_mode_selected", "test_completed", "result_viewed", "cta_clicked", "lead_submitted", "feature_interest")
        );

        Map<String, Long> familyCounts = new LinkedHashMap<>(pseoCatalogService.familyCounts());

        return new DashboardSnapshot(
                pages.size(),
                familyCounts,
                formatInstant(extremeInstant(pages, PseoPage::fetchedAt, Comparator.reverseOrder())),
                formatInstant(extremeInstant(pages, PseoPage::fetchedAt, Comparator.naturalOrder())),
                formatInstant(extremeInstant(pages, PseoPage::searchPerformedAt, Comparator.reverseOrder())),
                formatInstant(extremeInstant(pages, PseoPage::searchPerformedAt, Comparator.naturalOrder())),
                events.size(),
                leads.size(),
                funnel,
                percent(countOf(funnel, "test_completed"), countOf(funnel, "entry_mode_selected")),
                percent(countOf(funnel, "cta_clicked"), countOf(funnel, "result_viewed")),
                perPage(leads.size(), pages.size()),
                topCounts(events.stream(), AnalyticsEventRow::entryMode, 6),
                topCounts(events.stream(), AnalyticsEventRow::slug, 8),
                topCounts(events.stream(), AnalyticsEventRow::ctaType, 8),
                topCounts(events.stream(), AnalyticsEventRow::branch, 6),
                topLeadCounts(leads, LeadCaptureService.LeadRecord::sourceFamily, 6),
                topLeadCounts(leads, LeadCaptureService.LeadRecord::sourceSlug, 10),
                recentLeads(leads, 18),
                recentEvents(events, 18)
        );
    }

    private List<AnalyticsEventRow> analyticsEvents() {
        if (!Files.exists(analyticsCsvPath)) {
            return List.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(analyticsCsvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return List.of();
            }
            Map<String, Integer> index = toHeaderIndex(parseCsvLine(header));
            List<AnalyticsEventRow> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cols = parseCsvLine(line);
                rows.add(new AnalyticsEventRow(
                        value(cols, index, "happened_at"),
                        value(cols, index, "event_name"),
                        value(cols, index, "entry_mode"),
                        value(cols, index, "session_id"),
                        value(cols, index, "slug"),
                        value(cols, index, "tier"),
                        value(cols, index, "branch"),
                        value(cols, index, "cta_type"),
                        value(cols, index, "target_url"),
                        value(cols, index, "note")
                ));
            }
            return rows.stream()
                    .sorted(Comparator.comparing(AdminDashboardService::eventInstantOrEpoch).reversed())
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read analytics CSV at " + analyticsCsvPath, e);
        }
    }

    private Map<String, Long> orderedCounts(
            List<AnalyticsEventRow> events,
            Function<AnalyticsEventRow, String> classifier,
            List<String> order
    ) {
        Map<String, Long> counts = events.stream()
                .map(classifier)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Long> ordered = new LinkedHashMap<>();
        for (String key : order) {
            ordered.put(key, counts.getOrDefault(key, 0L));
        }
        return ordered;
    }

    private List<CountRow> topCounts(Stream<AnalyticsEventRow> events, Function<AnalyticsEventRow, String> classifier, int limit) {
        return events
                .map(classifier)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .map(entry -> new CountRow(entry.getKey(), entry.getValue()))
                .toList();
    }

    private List<AnalyticsEventRow> recentEvents(List<AnalyticsEventRow> events, int limit) {
        return events.stream().limit(limit).toList();
    }

    private List<LeadCaptureService.LeadRecord> recentLeads(List<LeadCaptureService.LeadRecord> leads, int limit) {
        return leads.stream().limit(limit).toList();
    }

    private List<CountRow> topLeadCounts(
            List<LeadCaptureService.LeadRecord> leads,
            Function<LeadCaptureService.LeadRecord, String> classifier,
            int limit
    ) {
        return leads.stream()
                .map(classifier)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .map(entry -> new CountRow(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Instant extremeInstant(List<PseoPage> pages, Function<PseoPage, String> extractor, Comparator<Instant> comparator) {
        return pages.stream()
                .map(extractor)
                .map(AdminDashboardService::parseInstant)
                .flatMap(AdminDashboardService::optionalStream)
                .sorted(comparator)
                .findFirst()
                .orElse(null);
    }

    private static Instant eventInstantOrEpoch(AnalyticsEventRow row) {
        return parseInstant(row.happenedAt()).orElse(Instant.EPOCH);
    }

    private static java.util.Optional<Instant> parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return java.util.Optional.empty();
        }
        try {
            return java.util.Optional.of(Instant.parse(value));
        } catch (DateTimeParseException ignored) {
            return java.util.Optional.empty();
        }
    }

    private String formatInstant(Instant instant) {
        if (instant == null) {
            return "N/A";
        }
        return instant.toString();
    }

    private long countOf(Map<String, Long> counts, String key) {
        return counts.getOrDefault(key, 0L);
    }

    private String percent(long numerator, long denominator) {
        if (denominator <= 0L) {
            return "0.0%";
        }
        return "%.1f%%".formatted((numerator * 100.0d) / denominator);
    }

    private String perPage(long numerator, long denominator) {
        if (denominator <= 0L) {
            return "0.00";
        }
        return "%.2f".formatted(numerator / (double) denominator);
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

    private static <T> Stream<T> optionalStream(java.util.Optional<T> optional) {
        return optional.stream();
    }

    public record DashboardSnapshot(
            int totalIndexablePages,
            Map<String, Long> familyCounts,
            String newestFetchedAt,
            String oldestFetchedAt,
            String newestSearchAt,
            String oldestSearchAt,
            int totalEvents,
            int totalLeads,
            Map<String, Long> funnelCounts,
            String completionRate,
            String ctaClickRate,
            String leadsPerPage,
            List<CountRow> entryModes,
            List<CountRow> topSlugs,
            List<CountRow> topCtaTypes,
            List<CountRow> topBranches,
            List<CountRow> leadFamilies,
            List<CountRow> leadSlugs,
            List<LeadCaptureService.LeadRecord> recentLeads,
            List<AnalyticsEventRow> recentEvents
    ) {
    }

    public record CountRow(
            String label,
            long count
    ) {
    }

    public record AnalyticsEventRow(
            String happenedAt,
            String eventName,
            String entryMode,
            String sessionId,
            String slug,
            String tier,
            String branch,
            String ctaType,
            String targetUrl,
            String note
    ) {
    }
}
