package com.example.wellwater.web.result;

import com.example.wellwater.decision.model.DecisionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ResultSnapshotService {

    private static final Pattern SAFE_ID = Pattern.compile("[A-Za-z0-9-]{8,80}");

    private final ObjectMapper objectMapper;
    private final Path snapshotDir;
    private final Duration ttl;

    public ResultSnapshotService(
            @Value("${app.results.snapshot.dir:./data/results/snapshots}") String snapshotDir,
            @Value("${app.results.snapshot.ttl-days:30}") long ttlDays
    ) {
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        this.snapshotDir = Paths.get(snapshotDir);
        this.ttl = Duration.ofDays(Math.max(ttlDays, 1));
    }

    public synchronized ResultSnapshot create(String sessionId, String sourceSlug, DecisionResult result) {
        String snapshotId = sanitizeId(sessionId);
        Instant now = Instant.now();
        ResultSnapshot snapshot = new ResultSnapshot(
                snapshotId,
                snapshotId,
                normalizeText(sourceSlug),
                now.toString(),
                now.plus(ttl).toString(),
                result
        );
        write(snapshot);
        return snapshot;
    }

    public synchronized Optional<ResultSnapshot> find(String snapshotId) {
        if (!isSafeId(snapshotId)) {
            return Optional.empty();
        }
        Path path = pathFor(snapshotId);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try {
            ResultSnapshot snapshot = objectMapper.readValue(path.toFile(), ResultSnapshot.class);
            if (isExpired(snapshot)) {
                Files.deleteIfExists(path);
                return Optional.empty();
            }
            return Optional.of(snapshot);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load result snapshot: " + path, e);
        }
    }

    private void write(ResultSnapshot snapshot) {
        try {
            Files.createDirectories(snapshotDir);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(pathFor(snapshot.id()).toFile(), snapshot);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store result snapshot in " + snapshotDir, e);
        }
    }

    private boolean isExpired(ResultSnapshot snapshot) {
        if (snapshot == null || snapshot.expiresAt() == null || snapshot.expiresAt().isBlank()) {
            return true;
        }
        return Instant.parse(snapshot.expiresAt()).isBefore(Instant.now());
    }

    private Path pathFor(String snapshotId) {
        return snapshotDir.resolve(snapshotId + ".json");
    }

    private String sanitizeId(String sessionId) {
        if (!isSafeId(sessionId)) {
            throw new IllegalArgumentException("Snapshot id must be URL-safe and 8-80 characters long.");
        }
        return sessionId;
    }

    private boolean isSafeId(String snapshotId) {
        return snapshotId != null && SAFE_ID.matcher(snapshotId).matches();
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String trimmed = value.trim();
        return trimmed.length() > 200 ? trimmed.substring(0, 200) : trimmed;
    }
}
