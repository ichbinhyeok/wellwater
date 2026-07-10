package com.example.wellwater.welltest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PivotMetricServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void writesOnlyTheAggregateMetricContract() throws Exception {
        Path csv = tempDir.resolve("pivot-metrics.csv");
        PivotMetricService service = new PivotMetricService(csv.toString());

        service.record("tool_completed", "chatgpt", "baseline", "essential", "success", 42L);

        var lines = Files.readAllLines(csv);
        assertEquals("date,event_name,channel,result_family,partner_product,outcome,latency_ms", lines.get(0));
        assertTrue(lines.get(1).contains(",tool_completed,chatgpt,baseline,essential,success,42"));
        assertFalse(lines.get(1).contains("@"));
        assertFalse(lines.get(1).contains("session"));
    }

    @Test
    void summarizesOnlyOperationalCounts() {
        Path csv = tempDir.resolve("pivot-summary.csv");
        PivotMetricService service = new PivotMetricService(csv.toString());

        service.record("tool_completed", "chatgpt", "baseline", "essential", "success", 40L);
        service.record("tool_completed", "web", "risk_context", "advanced", "success", 60L);
        service.record("tool_failed", "web", "", "", "invalid_input", 10L);
        service.record("partner_clicked", "chatgpt", "", "essential", "redirect", 0L);

        var summary = service.summary();
        assertEquals(2L, summary.completions());
        assertEquals(1L, summary.failures());
        assertEquals(2L, summary.partnerEligibleCompletions());
        assertEquals(1L, summary.partnerClicks());
        assertEquals(1L, summary.successfulPartnerRedirects());
        assertEquals(50L, summary.averageCompletionLatencyMs());
        assertEquals(1L, summary.completionsByChannel().get("chatgpt"));
        assertEquals(1L, summary.eligibleCompletionsByPartnerProduct().get("advanced"));
        assertEquals(1L, summary.partnerClicksByProduct().get("essential"));
    }

    @Test
    void removesAggregateRowsOlderThanThirteenMonthsOnWrite() throws Exception {
        Path csv = tempDir.resolve("retention.csv");
        Files.writeString(csv, "date,event_name,channel,result_family,partner_product,outcome,latency_ms\n"
                + LocalDate.now().minusMonths(14) + ",tool_completed,web,baseline,essential,success,10\n");
        PivotMetricService service = new PivotMetricService(csv.toString());

        service.record("tool_completed", "chatgpt", "baseline", "essential", "success", 20L);

        String content = Files.readString(csv);
        assertFalse(content.contains(LocalDate.now().minusMonths(14).toString()));
        assertTrue(content.contains(LocalDate.now().toString()));
    }
}
