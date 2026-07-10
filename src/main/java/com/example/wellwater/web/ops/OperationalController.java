package com.example.wellwater.web.ops;

import com.example.wellwater.welltest.PivotMetricService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class OperationalController {

    private final PivotMetricService pivotMetricService;
    private final String domainChallengeToken;
    private final String applicationName;

    public OperationalController(
            PivotMetricService pivotMetricService,
            @Value("${app.openai.domain-challenge-token:}") String domainChallengeToken,
            @Value("${spring.application.name:waterverdict}") String applicationName
    ) {
        this.pivotMetricService = pivotMetricService;
        this.domainChallengeToken = domainChallengeToken == null ? "" : domainChallengeToken.trim();
        this.applicationName = applicationName;
    }

    @GetMapping(value = "/health/app", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> health() {
        return Map.of(
                "status", "ok",
                "service", applicationName,
                "surface", "private-well-test-finder",
                "checkedAt", Instant.now().toString()
        );
    }

    @GetMapping(value = "/.well-known/openai-apps-challenge", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> openAiDomainChallenge() {
        if (domainChallengeToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
        return ResponseEntity.ok(domainChallengeToken);
    }

    @GetMapping(value = "/admin/pivot-metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public PivotMetricService.PivotMetricSummary pivotMetrics() {
        return pivotMetricService.summary();
    }
}
