package com.example.wellwater.web.nj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.nj.geocoder.enabled=false",
        "app.nj.metrics.csv.path=./build/test-data/nj-distribution-web.csv"
})
@AutoConfigureMockMvc
class NjPreflightWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void mainLandingIsAnIndexableActionFirstTool() throws Exception {
        mockMvc.perform(get("/nj-well-preflight"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Check the well before the closing clock starts.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("action=\"/nj-well-preflight/result\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("0</strong> addresses retained")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("index,follow")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Request follow-up"))));
    }

    @Test
    void pilotMunicipalityPageRoutesIntoTheSameToolAndWeakPagesDoNotPublish() throws Exception {
        mockMvc.perform(get("/nj/private-well/jackson-township-ocean"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Jackson Township well-sale preflight")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Official local aggregate")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("channel\" value=\"organic_local")));

        mockMvc.perform(get("/nj/private-well/chester-borough-morris"))
                .andExpect(status().isNotFound());
    }

    @Test
    void resultUsesCountyRulesAndNeverEchoesTheSubmittedAddress() throws Exception {
        mockMvc.perform(post("/nj-well-preflight/result")
                        .param("transactionType", "sale")
                        .param("waterSource", "private_well")
                        .param("address", "123 Secret Lane, Jackson, NJ 08527")
                        .param("municipalitySlug", "jackson-township-ocean")
                        .param("channel", "direct")
                        .param("source", "main")
                        .param("extraContexts", "treatment_installed"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andExpect(header().string("Cache-Control", org.hamcrest.Matchers.containsString("no-store")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("This sale appears to be covered by the NJ PWTA.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Mercury")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Your address was not retained")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("123 Secret Lane"))));
    }

    @Test
    void partnerPagesStayUnavailableUntilAProspectIsVerifiedAndActivated() throws Exception {
        mockMvc.perform(get("/partners/environmental-well-testing/nj-well-preflight"))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")));
    }

    @Test
    void sitemapContainsOnlyMainAndPilotNjSurfaces() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/nj-well-preflight")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/nj/private-well/jackson-township-ocean")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("/nj/private-well/chester-borough-morris"))));
    }

    @Test
    void clientMetricEndpointRejectsUnknownEvents() throws Exception {
        mockMvc.perform(post("/nj-well-preflight/event")
                        .param("eventName", "address_submitted")
                        .param("channel", "direct")
                        .param("source", "main"))
                .andExpect(status().isBadRequest());
    }
}
