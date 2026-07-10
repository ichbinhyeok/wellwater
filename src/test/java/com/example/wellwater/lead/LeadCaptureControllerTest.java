package com.example.wellwater.lead;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeadCaptureControllerTest {

    @Test
    void publicLeadCollectionIsRetired() {
        var response = new LeadCaptureController().submit();

        assertEquals(410, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }
}
