package com.example.wellwater.nj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class NjPartnerCatalogService {

    private static final String CLASSPATH_PROSPECTS = "classpath:data/nj/partner-prospects.csv";
    private static final Set<String> ALLOWED_TYPES = Set.of("lab", "inspector");

    private final ResourceLoader resourceLoader;
    private final String configuredPath;

    public NjPartnerCatalogService(
            ResourceLoader resourceLoader,
            @Value("${app.nj.partners.csv.path:}") String configuredPath
    ) {
        this.resourceLoader = resourceLoader;
        this.configuredPath = configuredPath == null ? "" : configuredPath.trim();
    }

    public List<NjPartner> all() {
        try (InputStream input = openCatalog();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            List<NjPartner> partners = new ArrayList<>();
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                partners.add(parse(line));
            }
            validateInventory(partners);
            return List.copyOf(partners);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load NJ partner catalog.", e);
        }
    }

    public Optional<NjPartner> active(String slug) {
        if (slug == null || slug.isBlank()) {
            return Optional.empty();
        }
        String normalized = slug.trim().toLowerCase(Locale.ROOT);
        return all().stream()
                .filter(NjPartner::active)
                .filter(partner -> partner.slug().equals(normalized))
                .findFirst();
    }

    private InputStream openCatalog() throws IOException {
        if (!configuredPath.isBlank()) {
            Path path = Paths.get(configuredPath);
            if (!Files.exists(path)) {
                throw new IllegalStateException("Configured NJ partner catalog does not exist: " + path);
            }
            return Files.newInputStream(path);
        }
        Resource resource = resourceLoader.getResource(CLASSPATH_PROSPECTS);
        if (!resource.exists()) {
            throw new IllegalStateException("Missing NJ partner prospect catalog.");
        }
        return resource.getInputStream();
    }

    private NjPartner parse(String line) {
        String[] values = line.split(",", -1);
        if (values.length != 8) {
            throw new IllegalStateException("NJ partner CSV rows must contain 8 columns.");
        }
        String slug = token(values[0]);
        String name = values[1].trim();
        String type = token(values[2]);
        boolean active = Boolean.parseBoolean(values[3].trim());
        String verification = values[4].trim();
        String bookingUrl = values[5].trim();
        List<String> counties = Arrays.stream(values[6].split("\\|"))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();
        String disclosure = values[7].trim();
        if (slug.isBlank() || name.isBlank() || !ALLOWED_TYPES.contains(type) || disclosure.isBlank()) {
            throw new IllegalStateException("Invalid NJ partner catalog row for slug: " + slug);
        }
        if (!bookingUrl.isBlank()) {
            validateHttpsUrl(bookingUrl);
        }
        if (active && !verification.toLowerCase(Locale.ROOT).startsWith("verified:")) {
            throw new IllegalStateException("Active NJ partners require a verification_status beginning with 'verified:'.");
        }
        if (active && bookingUrl.isBlank()) {
            throw new IllegalStateException("Active NJ partners require an HTTPS booking URL.");
        }
        return new NjPartner(slug, name, type, active, verification, bookingUrl, counties, disclosure);
    }

    private void validateInventory(List<NjPartner> partners) {
        if (partners.size() != 10) {
            throw new IllegalStateException("NJ partner prospect inventory must contain exactly 10 rows.");
        }
        long labs = partners.stream().filter(value -> value.type().equals("lab")).count();
        long inspectors = partners.stream().filter(value -> value.type().equals("inspector")).count();
        if (labs != 5 || inspectors != 5) {
            throw new IllegalStateException("NJ partner prospect inventory must contain 5 labs and 5 inspectors.");
        }
        long uniqueSlugs = partners.stream().map(NjPartner::slug).distinct().count();
        if (uniqueSlugs != partners.size()) {
            throw new IllegalStateException("NJ partner slugs must be unique.");
        }
    }

    private void validateHttpsUrl(String value) {
        URI uri = URI.create(value);
        if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null || uri.getHost().isBlank()) {
            throw new IllegalStateException("NJ partner booking URLs must use absolute HTTPS URLs.");
        }
    }

    private String token(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
