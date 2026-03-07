package com.example.wellwater.bootstrap;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class CsvSeedBootstrapService {

    private final ResourceLoader resourceLoader;
    private final List<SeedTarget> seedTargets;

    public CsvSeedBootstrapService(
            ResourceLoader resourceLoader,
            @Value("${app.pseo.csv.path:./data/pseo/pages.csv}") String pseoPath,
            @Value("${app.pseo.citations.path:./data/pseo/page_sources.csv}") String pseoCitationPath,
            @Value("${app.registry.contaminant.path:./data/registry/contaminant_registry.csv}") String contaminantPath,
            @Value("${app.registry.symptom.path:./data/registry/symptom_registry.csv}") String symptomPath,
            @Value("${app.registry.trigger.path:./data/registry/trigger_registry.csv}") String triggerPath,
            @Value("${app.registry.cost.path:./data/registry/cost_registry.csv}") String costPath,
            @Value("${app.registry.state.path:./data/registry/state_resource_registry.csv}") String statePath
    ) {
        this.resourceLoader = resourceLoader;
        this.seedTargets = List.of(
                new SeedTarget(Paths.get(pseoPath), "classpath:seed-data/pseo/pages.csv"),
                new SeedTarget(Paths.get(pseoCitationPath), "classpath:seed-data/pseo/page_sources.csv"),
                new SeedTarget(Paths.get(contaminantPath), "classpath:seed-data/registry/contaminant_registry.csv"),
                new SeedTarget(Paths.get(symptomPath), "classpath:seed-data/registry/symptom_registry.csv"),
                new SeedTarget(Paths.get(triggerPath), "classpath:seed-data/registry/trigger_registry.csv"),
                new SeedTarget(Paths.get(costPath), "classpath:seed-data/registry/cost_registry.csv"),
                new SeedTarget(Paths.get(statePath), "classpath:seed-data/registry/state_resource_registry.csv")
        );
    }

    @PostConstruct
    public void ensureSeedData() {
        for (SeedTarget seedTarget : seedTargets) {
            copyIfMissing(seedTarget.targetPath(), seedTarget.classpathLocation());
        }
    }

    private void copyIfMissing(Path targetPath, String classpathLocation) {
        try {
            if (targetPath.getParent() != null) {
                Files.createDirectories(targetPath.getParent());
            }
            if (Files.exists(targetPath)) {
                return;
            }

            Resource resource = resourceLoader.getResource(classpathLocation);
            if (!resource.exists()) {
                throw new IllegalStateException("Missing seed resource: " + classpathLocation);
            }

            try (InputStream inputStream = resource.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize CSV at " + targetPath, e);
        }
    }

    private record SeedTarget(
            Path targetPath,
            String classpathLocation
    ) {
    }
}
