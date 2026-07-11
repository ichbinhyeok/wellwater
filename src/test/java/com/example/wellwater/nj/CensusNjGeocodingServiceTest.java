package com.example.wellwater.nj;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CensusNjGeocodingServiceTest {

    private final CensusNjGeocodingService service = new CensusNjGeocodingService(
            java.net.http.HttpClient.newHttpClient(), new ObjectMapper(), "https://example.test/geocode", true
    );

    @Test
    void parsesCoordinatesCountyAndMunicipalityWithoutRetainingInput() {
        String body = """
                {"result":{"addressMatches":[{
                  "matchedAddress":"24 MAPLE AVE, CHESTER, NJ, 07930",
                  "coordinates":{"x":-74.6958826,"y":40.7826913},
                  "addressComponents":{"state":"NJ","city":"CHESTER"},
                  "geographies":{"Counties":[{"NAME":"Morris County"}],"County Subdivisions":[{"NAME":"Chester borough"}]}
                }]}}
                """;

        NjLocation location = service.parse(body).orElseThrow();
        assertTrue(location.inNewJersey());
        assertEquals("Morris County", location.county());
        assertEquals("Chester borough", location.municipality());
        assertEquals(-74.6958826, location.longitude(), 0.0000001);
    }

    @Test
    void marksAnOutOfStateMatchInsteadOfTreatingItAsNj() {
        String body = """
                {"result":{"addressMatches":[{
                  "matchedAddress":"1 MAIN ST, NEW YORK, NY, 10001",
                  "coordinates":{"x":-73.99,"y":40.75},
                  "addressComponents":{"state":"NY","city":"NEW YORK"},
                  "geographies":{"Counties":[{"NAME":"New York County"}]}
                }]}}
                """;

        assertFalse(service.parse(body).orElseThrow().inNewJersey());
    }

    @Test
    void returnsEmptyForAmbiguousOrEmptyResults() {
        assertTrue(service.parse("{\"result\":{\"addressMatches\":[]}}").isEmpty());
    }
}
