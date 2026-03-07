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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PseoCatalogService {

    private static final Set<String> ALLOWED_FAMILIES = Set.of("contaminants", "symptoms", "compares", "triggers");
    private final Path csvPath;

    public PseoCatalogService(@Value("${app.pseo.csv.path:./data/pseo/pages.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public List<PseoPage> allPages() {
        if (!Files.exists(csvPath)) {
            return List.of();
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return List.of();
            }
            Map<String, Integer> index = toHeaderIndex(parseCsvLine(header));
            List<PseoPage> pages = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                PseoPage page = toPage(parseCsvLine(line), index);
                validatePage(page);
                pages.add(page);
            }
            ensureUniqueSlugs(pages);
            return pages;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load pSEO CSV at " + csvPath, e);
        }
    }

    public Optional<PseoPage> findBySlug(String slug) {
        return allPages().stream()
                .filter(page -> page.slug().equals(slug))
                .findFirst();
    }

    public List<PseoPage> byFamily(String family) {
        if (family == null) {
            return List.of();
        }
        return allPages().stream()
                .filter(page -> page.family().equals(family))
                .sorted(Comparator.comparing(PseoPage::slug))
                .toList();
    }

    public Map<String, Long> familyCounts() {
        Map<String, Long> grouped = allPages().stream()
                .collect(Collectors.groupingBy(PseoPage::family, LinkedHashMap::new, Collectors.counting()));

        Map<String, Long> ordered = new LinkedHashMap<>();
        for (String family : List.of("contaminants", "symptoms", "compares", "triggers")) {
            ordered.put(family, grouped.getOrDefault(family, 0L));
        }
        return ordered;
    }

    public List<PseoPage> featured(int limit) {
        return allPages().stream()
                .sorted(Comparator.comparing(PseoPage::family).thenComparing(PseoPage::slug))
                .limit(Math.max(limit, 0))
                .toList();
    }

    public String sitemapXml(String baseUrl) {
        String normalizedBase = normalizeBaseUrl(baseUrl);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        appendUrl(xml, normalizedBase + "/");
        for (String family : List.of("contaminants", "symptoms", "compares", "triggers")) {
            appendUrl(xml, normalizedBase + "/well-water/family/" + family);
        }
        for (PseoPage page : allPages()) {
            appendUrl(xml, normalizedBase + "/well-water/" + page.slug());
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private void appendUrl(StringBuilder xml, String loc) {
        xml.append("  <url><loc>")
                .append(escapeXml(loc))
                .append("</loc></url>\n");
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            return "http://localhost:8080";
        }
        if (baseUrl.endsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
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

    private PseoPage toPage(List<String> cols, Map<String, Integer> idx) {
        return new PseoPage(
                value(cols, idx, "family"),
                value(cols, idx, "slug"),
                value(cols, idx, "title"),
                value(cols, idx, "h1"),
                value(cols, idx, "meta_description"),
                value(cols, idx, "intro"),
                value(cols, idx, "action_now"),
                value(cols, idx, "what_to_test"),
                value(cols, idx, "primary_cta_label"),
                value(cols, idx, "primary_cta_url"),
                value(cols, idx, "money_cta_label"),
                value(cols, idx, "money_cta_url"),
                value(cols, idx, "disclosure"),
                value(cols, idx, "source_url"),
                value(cols, idx, "search_query"),
                value(cols, idx, "search_performed_at"),
                value(cols, idx, "fetched_at")
        );
    }

    private String value(List<String> cols, Map<String, Integer> idx, String key) {
        Integer i = idx.get(key);
        if (i == null || i < 0 || i >= cols.size()) {
            return "";
        }
        return cols.get(i).trim();
    }

    private void validatePage(PseoPage page) {
        requireNotBlank(page.family(), "family");
        requireNotBlank(page.slug(), "slug");
        requireNotBlank(page.title(), "title");
        requireNotBlank(page.h1(), "h1");
        requireNotBlank(page.metaDescription(), "meta_description");
        requireNotBlank(page.intro(), "intro");
        requireNotBlank(page.actionNow(), "action_now");
        requireNotBlank(page.primaryCtaLabel(), "primary_cta_label");
        requireNotBlank(page.primaryCtaUrl(), "primary_cta_url");
        requireNotBlank(page.moneyCtaLabel(), "money_cta_label");
        requireNotBlank(page.moneyCtaUrl(), "money_cta_url");
        requireNotBlank(page.disclosure(), "disclosure");
        requireNotBlank(page.sourceUrl(), "source_url");
        requireNotBlank(page.searchQuery(), "search_query");
        requireNotBlank(page.searchPerformedAt(), "search_performed_at");
        requireNotBlank(page.fetchedAt(), "fetched_at");
        if (!ALLOWED_FAMILIES.contains(page.family())) {
            throw new IllegalStateException("Unsupported family: " + page.family());
        }
    }

    private void ensureUniqueSlugs(List<PseoPage> pages) {
        Map<String, Long> counts = pages.stream()
                .collect(Collectors.groupingBy(PseoPage::slug, Collectors.counting()));
        List<String> duplicates = counts.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
        if (!duplicates.isEmpty()) {
            throw new IllegalStateException("Duplicate slugs in pSEO CSV: " + duplicates);
        }
    }

    private void requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required field in pSEO CSV: " + field);
        }
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

    private String escapeXml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
