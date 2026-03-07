package com.example.wellwater.lead;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeadCaptureServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void submitPersistsLeadAndRecentViewsReadItBack() {
        LeadCaptureService service = new LeadCaptureService(tempDir.resolve("leads.csv").toString());
        LeadCaptureRequest request = new LeadCaptureRequest();
        request.setEmail("owner@example.com");
        request.setName("Owner");
        request.setState("pa");
        request.setNote("Need a follow-up on arsenic.");
        request.setSourceType("pseo-detail");
        request.setSourceFamily("regional");
        request.setSourceSlug("new-hampshire-arsenic-well-water");
        request.setSourceLabel("New Hampshire Arsenic in Well Water What To Do");

        assertTrue(service.submit(request).isPresent());
        assertEquals(1, service.totalLeads());
        assertEquals("PA", service.recentLeads(5).get(0).state());
        assertEquals("new-hampshire-arsenic-well-water", service.topLeadSlugs(5).get(0).label());
    }

    @Test
    void submitRejectsInvalidEmailAndHoneypotSpam() {
        LeadCaptureService service = new LeadCaptureService(tempDir.resolve("leads.csv").toString());

        LeadCaptureRequest invalid = new LeadCaptureRequest();
        invalid.setEmail("not-an-email");
        assertFalse(service.submit(invalid).isPresent());

        LeadCaptureRequest spam = new LeadCaptureRequest();
        spam.setEmail("owner@example.com");
        spam.setWebsite("https://spam.invalid");
        assertFalse(service.submit(spam).isPresent());
    }
}
