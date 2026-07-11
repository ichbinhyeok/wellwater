package com.example.wellwater.nj;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NjDistributionMetricServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void recordsOnlyAggregateDistributionDimensions() throws Exception {
        Path csv = tempDir.resolve("nj-distribution.csv");
        NjDistributionMetricService service = new NjDistributionMetricService(csv.toString());

        service.record("preflight_completed", "organic_local", "jackson-township-ocean", "transaction", "result", "success", 12L);

        String contents = Files.readString(csv);
        assertFalse(contents.toLowerCase().contains("address"));
        assertFalse(contents.contains("session_id"));
        assertEquals(1L, service.summary().byEvent().get("preflight_completed"));
        assertEquals(1L, service.summary().bySource().get("jackson-township-ocean"));
    }
}
