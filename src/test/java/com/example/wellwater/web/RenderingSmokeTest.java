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
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Threshold Check")));
    }

    @Test
    void pseoDetailPageRenders() throws Exception {
        mockMvc.perform(get("/well-water/nitrate"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nitrate in Well Water What To Do")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/tool/result-first?analyte=nitrate&slug=nitrate")));
    }
}
