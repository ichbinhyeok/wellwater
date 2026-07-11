package com.example.wellwater.nj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

@Service
public class CensusNjGeocodingService implements NjGeocodingService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String endpoint;
    private final boolean enabled;

    @Autowired
    public CensusNjGeocodingService(
            ObjectMapper objectMapper,
            @Value("${app.nj.census-geocoder-url:https://geocoding.geo.census.gov/geocoder/geographies/onelineaddress}") String endpoint,
            @Value("${app.nj.geocoder.enabled:true}") boolean enabled
    ) {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build(), objectMapper, endpoint, enabled);
    }

    CensusNjGeocodingService(HttpClient httpClient, ObjectMapper objectMapper, String endpoint, boolean enabled) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.endpoint = endpoint;
        this.enabled = enabled;
    }

    @Override
    public Optional<NjLocation> locate(String address) {
        if (!enabled || address == null || address.isBlank()) {
            return Optional.empty();
        }
        try {
            String query = endpoint
                    + "?address=" + URLEncoder.encode(address.trim(), StandardCharsets.UTF_8)
                    + "&benchmark=Public_AR_Current&vintage=Current_Current&format=json";
            HttpRequest request = HttpRequest.newBuilder(URI.create(query))
                    .timeout(Duration.ofSeconds(5))
                    .header("Accept", "application/json")
                    .header("User-Agent", "WaterVerdict-NJ-Preflight/1.0")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return Optional.empty();
            }
            return parse(response.body());
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    Optional<NjLocation> parse(String body) {
        try {
            JsonNode matches = objectMapper.readTree(body).path("result").path("addressMatches");
            if (!matches.isArray() || matches.isEmpty()) {
                return Optional.empty();
            }
            JsonNode match = matches.get(0);
            JsonNode coordinates = match.path("coordinates");
            JsonNode components = match.path("addressComponents");
            String state = components.path("state").asText("");
            String county = firstName(match.path("geographies").path("Counties"));
            String municipality = firstName(match.path("geographies").path("County Subdivisions"));
            if (municipality.isBlank()) {
                municipality = firstName(match.path("geographies").path("Incorporated Places"));
            }
            if (municipality.isBlank()) {
                municipality = components.path("city").asText("");
            }
            return Optional.of(new NjLocation(
                    coordinates.path("x").asDouble(),
                    coordinates.path("y").asDouble(),
                    municipality,
                    county,
                    "NJ".equalsIgnoreCase(state)
            ));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    private String firstName(JsonNode values) {
        if (!values.isArray() || values.isEmpty()) {
            return "";
        }
        return values.get(0).path("NAME").asText("");
    }
}
