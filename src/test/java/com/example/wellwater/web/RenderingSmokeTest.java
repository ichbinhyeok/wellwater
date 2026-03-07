package com.example.wellwater.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RenderingSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageRendersRealContent() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Private Well Water Guide")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"http://localhost:8080/\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Priority pages")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rotten Egg Smell in Well Water")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Regional Guides")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Authority Articles")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trust surface")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("application/ld+json")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("@content()"))));
    }

    @Test
    void resultFirstPageRenders() throws Exception {
        mockMvc.perform(get("/tool/result-first"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Run Decision Engine")));
    }

    @Test
    void arsenicAboveThresholdRendersVerifyFirstFlow() throws Exception {
        mockMvc.perform(post("/tool/result")
                        .param("entryMode", "result-first")
                        .param("analyteName", "arsenic")
                        .param("resultValue", "12")
                        .param("unit", "ppb")
                        .param("qualifier", "none")
                        .param("sampleDate", "2026-03-01")
                        .param("sampleSource", "raw well")
                        .param("labCertified", "yes")
                        .param("state", "PA"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Data First")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Threshold Check")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Save Result")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Download PDF")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Share Link")));
    }

    @Test
    void pseoDetailPageRenders() throws Exception {
        mockMvc.perform(get("/well-water/nitrate"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nitrate in Well Water What To Do")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"http://localhost:8080/well-water/nitrate\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Breadcrumb")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/tool/result-first?analyte=nitrate&amp;slug=nitrate")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Do not buy yet")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("One-line call")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Three actions before you buy anything")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Questions that should be answered before a purchase")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Official source")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lead capture")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Request follow-up")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Related next reads")));
    }

    @Test
    void regionalPageRendersStateAwareHandoff() throws Exception {
        mockMvc.perform(get("/well-water/new-hampshire-arsenic-well-water"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Regional guide")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/tool/result-first?slug=new-hampshire-arsenic-well-water&amp;state=NH")));
    }

    @Test
    void clusterComparePageRenders() throws Exception {
        mockMvc.perform(get("/well-water/ro-vs-adsorptive-media-for-arsenic"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("RO vs Adsorptive Media for Arsenic in Well Water")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Related regional reads")));
    }

    @Test
    void authorityFamilyPageRendersLeadCaptureSurface() throws Exception {
        mockMvc.perform(get("/well-water/family/authority"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Authority and methodology articles")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Start with these pages in this family")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What this hub should help you avoid")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lead capture")));
    }

    @Test
    void trustPagesRenderAsPublicAssets() throws Exception {
        mockMvc.perform(get("/trust"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trust surface")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Methodology")));

        mockMvc.perform(get("/trust/methodology"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("How This Site Turns Well-Water Clues Into Next Steps")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"http://localhost:8080/trust/methodology\"")));
    }
}
