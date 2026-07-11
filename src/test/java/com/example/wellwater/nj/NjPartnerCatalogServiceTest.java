package com.example.wellwater.nj;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.DefaultResourceLoader;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NjPartnerCatalogServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void shipsFiveLabAndFiveInspectorProspectsButPublishesNoneBeforeApproval() {
        NjPartnerCatalogService service = new NjPartnerCatalogService(new DefaultResourceLoader(), "");

        assertEquals(10, service.all().size());
        assertEquals(5, service.all().stream().filter(value -> value.type().equals("lab")).count());
        assertEquals(5, service.all().stream().filter(value -> value.type().equals("inspector")).count());
        assertTrue(service.all().stream().noneMatch(NjPartner::active));
        assertTrue(service.active("environmental-well-testing").isEmpty());
    }

    @Test
    void activatesOnlyAReviewedHttpsDestination() throws Exception {
        Path catalog = tempDir.resolve("partners.csv");
        Files.writeString(catalog, catalogWithFirstUrl("https://lab.example.test/book"));
        NjPartnerCatalogService service = new NjPartnerCatalogService(new DefaultResourceLoader(), catalog.toString());

        NjPartner partner = service.active("lab-one").orElseThrow();
        assertEquals("https://lab.example.test/book", partner.bookingUrl());
    }

    @Test
    void rejectsAnHttpPartnerDestinationBeforeItCanBecomeARedirect() throws Exception {
        Path catalog = tempDir.resolve("unsafe-partners.csv");
        Files.writeString(catalog, catalogWithFirstUrl("http://lab.example.test/book"));
        NjPartnerCatalogService service = new NjPartnerCatalogService(new DefaultResourceLoader(), catalog.toString());

        assertThrows(IllegalStateException.class, service::all);
    }

    private String catalogWithFirstUrl(String firstUrl) {
        return """
                slug,name,type,active,verification_status,booking_url,supported_counties,disclosure
                lab-one,Lab One,lab,true,verified: 2026-07-11,%s,ALL,Independent partner disclosure.
                lab-two,Lab Two,lab,false,pending,https://lab2.example.test/,ALL,Independent prospect disclosure.
                lab-three,Lab Three,lab,false,pending,https://lab3.example.test/,ALL,Independent prospect disclosure.
                lab-four,Lab Four,lab,false,pending,https://lab4.example.test/,ALL,Independent prospect disclosure.
                lab-five,Lab Five,lab,false,pending,https://lab5.example.test/,ALL,Independent prospect disclosure.
                inspector-one,Inspector One,inspector,false,pending,https://inspector1.example.test/,ALL,Independent prospect disclosure.
                inspector-two,Inspector Two,inspector,false,pending,https://inspector2.example.test/,ALL,Independent prospect disclosure.
                inspector-three,Inspector Three,inspector,false,pending,https://inspector3.example.test/,ALL,Independent prospect disclosure.
                inspector-four,Inspector Four,inspector,false,pending,https://inspector4.example.test/,ALL,Independent prospect disclosure.
                inspector-five,Inspector Five,inspector,false,pending,https://inspector5.example.test/,ALL,Independent prospect disclosure.
                """.formatted(firstUrl);
    }
}
