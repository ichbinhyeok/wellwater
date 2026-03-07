package com.example.wellwater.bootstrap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.DefaultResourceLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvSeedBootstrapServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void seedsMissingCsvFilesAndPreservesExistingOnSecondRun() throws Exception {
        Path dataDir = tempDir.resolve("persistent-data");
        CsvSeedBootstrapService service = new CsvSeedBootstrapService(
                new DefaultResourceLoader(),
                dataDir.resolve("pseo/pages.csv").toString(),
                dataDir.resolve("pseo/page_sources.csv").toString(),
                dataDir.resolve("registry/contaminant_registry.csv").toString(),
                dataDir.resolve("registry/symptom_registry.csv").toString(),
                dataDir.resolve("registry/trigger_registry.csv").toString(),
                dataDir.resolve("registry/cost_registry.csv").toString(),
                dataDir.resolve("registry/state_resource_registry.csv").toString()
        );

        service.ensureSeedData();

        Path pseoPath = dataDir.resolve("pseo/pages.csv");
        Path citationPath = dataDir.resolve("pseo/page_sources.csv");
        Path contaminantPath = dataDir.resolve("registry/contaminant_registry.csv");

        assertTrue(Files.exists(pseoPath));
        assertTrue(Files.exists(citationPath));
        assertTrue(Files.exists(contaminantPath));
        assertTrue(Files.size(pseoPath) > 0L);

        Files.writeString(contaminantPath, "custom-content", StandardCharsets.UTF_8);

        service.ensureSeedData();

        assertEquals("custom-content", Files.readString(contaminantPath, StandardCharsets.UTF_8));
    }
}
