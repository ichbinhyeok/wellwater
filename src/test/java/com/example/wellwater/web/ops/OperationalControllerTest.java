package com.example.wellwater.web.ops;

import com.example.wellwater.welltest.PivotMetricService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationalControllerTest {

    @TempDir
    Path tempDir;

    @Test
    void exposesHealthAndConfiguredDomainChallenge() {
        OperationalController controller = new OperationalController(
                new PivotMetricService(tempDir.resolve("metrics.csv").toString()),
                "challenge-token",
                "waterverdict"
        );

        assertEquals("ok", controller.health().get("status"));
        assertEquals("waterverdict", controller.health().get("service"));
        assertEquals(200, controller.openAiDomainChallenge().getStatusCode().value());
        assertEquals("challenge-token", controller.openAiDomainChallenge().getBody());
    }

    @Test
    void hidesMissingDomainChallengeAndReturnsEmptyMetrics() {
        OperationalController controller = new OperationalController(
                new PivotMetricService(tempDir.resolve("missing.csv").toString()),
                "",
                "waterverdict"
        );

        assertEquals(404, controller.openAiDomainChallenge().getStatusCode().value());
        assertTrue(controller.pivotMetrics().completionsByChannel().isEmpty());
    }
}
