package com.example.wellwater.nj;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NjPwtaDataServiceTest {

    private NjPwtaDataService service;

    @BeforeEach
    void setUp() {
        service = new NjPwtaDataService(new ObjectMapper(), new DefaultResourceLoader());
        service.load();
    }

    @Test
    void loadsVersionedSnapshotsAndExactlyTwentyFourPilotMunicipalities() {
        assertEquals(1_676, service.gridCount());
        assertEquals(564, service.municipalityCount());
        assertEquals(24, service.pilotMunicipalities().size());
        assertTrue(service.findMunicipality("jackson-township-ocean").orElseThrow().pilot());
        assertFalse(service.findMunicipality("chester-borough-morris").orElseThrow().pilot());
    }

    @Test
    void matchesCensusMunicipalityNamesAndARealNjGridPoint() {
        NjMunicipalitySummary chester = service.matchMunicipality("Chester borough", "Morris County").orElseThrow();
        assertEquals("Chester Borough", chester.name());
        assertEquals("Morris", chester.county());

        NjPwtaDataService.GridMatch grid = service.findGrid(-74.6958826, 40.7826913).orElseThrow();
        assertTrue(grid.gridId() > 0);
        assertFalse(grid.signals().isEmpty());
    }

    @Test
    void rejectsCoordinatesOutsideNewJerseySnapshot() {
        assertTrue(service.findGrid(-73.9857, 40.7484).isEmpty());
    }
}
