package com.example.wellwater.analytics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Service
public class AnalyticsEventService {

    private static final String HEADER = "happened_at,event_name,entry_mode,session_id,slug,tier,branch,cta_type,target_url,note";
    private final Path csvPath;

    public AnalyticsEventService(@Value("${app.analytics.csv.path:./data/analytics/events.csv}") String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    public synchronized void logEvent(
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
        try {
            ensureReady();
            String row = String.join(",",
                    csv(Instant.now().toString()),
                    csv(eventName),
                    csv(entryMode),
                    csv(sessionId),
                    csv(slug),
                    csv(tier),
                    csv(branch),
                    csv(ctaType),
                    csv(targetUrl),
                    csv(note)
            );
            Files.writeString(csvPath, row + System.lineSeparator(), StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write analytics event CSV: " + csvPath, e);
        }
    }

    private void ensureReady() throws IOException {
        if (csvPath.getParent() != null) {
            Files.createDirectories(csvPath.getParent());
        }
        if (!Files.exists(csvPath)) {
            Files.writeString(csvPath, HEADER + System.lineSeparator(), StandardCharsets.UTF_8);
        }
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

