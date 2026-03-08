package com.example.wellwater.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RenderingSmokeTest {

    private static final Pattern SAVED_RESULT_PATH = Pattern.compile("/result/saved/([A-Za-z0-9-]+)");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageRendersRealContent() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Private Well Water Guide")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"https://wellwater.test/\"")))
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
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("noindex,nofollow")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Companion Report Lines")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add report line")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Other Context Flags")))
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
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("<title>Decision Result</title>"))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Data First")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Threshold Check")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Recommended Tests")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Sample plan")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("PA certified lab path")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("PA testing guidance")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Before You Compare Or Buy")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Save Result")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Download PDF")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Share Link")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Review saved view")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Copy link")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Method And Trust")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("right panel"))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/result/saved/")));
    }

    @Test
    void floodDrivenResultShowsOrderedTestingSequence() throws Exception {
        mockMvc.perform(post("/tool/result")
                        .param("entryMode", "result-first")
                        .param("analyteName", "total coliform")
                        .param("resultValue", "positive")
                        .param("unit", "presence/absence")
                        .param("qualifier", "positive")
                        .param("sampleDate", "2026-03-01")
                        .param("sampleSource", "raw well")
                        .param("labCertified", "yes")
                        .param("state", "FL")
                        .param("triggerFlag", "after-flood"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Recommended Tests")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Start with flood-sensitive retesting and source review before you widen the panel or compare treatment classes.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Step 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Post-flood certified bacteria retest")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("FL testing guidance")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Report Context Used")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("After flood")));
    }

    @Test
    void resultViewShowsSupportingContextSignalsWhenProvided() throws Exception {
        mockMvc.perform(post("/tool/result")
                        .param("entryMode", "result-first")
                        .param("analyteName", "hardness")
                        .param("resultValue", "18")
                        .param("unit", "grains/gal")
                        .param("qualifier", "none")
                        .param("sampleDate", "2026-03-01")
                        .param("sampleSource", "raw well")
                        .param("labCertified", "yes")
                        .param("state", "PA")
                        .param("useScope", "whole-house")
                        .param("householdSize", "4")
                        .param("companionLines[0].analyteName", "lead")
                        .param("companionLines[0].resultValue", "20")
                        .param("companionLines[0].unit", "ppb")
                        .param("companionLines[0].qualifier", "none")
                        .param("companionLines[1].analyteName", "ph")
                        .param("companionLines[1].resultValue", "6.0")
                        .param("companionLines[1].unit", "su")
                        .param("companionLines[1].qualifier", "none"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Report Context Used")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Report Lines Reviewed")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Softener Sizing Preview")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Corrosion split before softener sizing")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lead")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("pH")));
    }

    @Test
    void hardnessResultCanRenderEligibleSoftenerSizingPreview() throws Exception {
        mockMvc.perform(post("/tool/result")
                        .param("entryMode", "result-first")
                        .param("analyteName", "hardness")
                        .param("resultValue", "20")
                        .param("unit", "grains/gal")
                        .param("qualifier", "none")
                        .param("sampleDate", "2026-03-01")
                        .param("sampleSource", "raw well")
                        .param("labCertified", "yes")
                        .param("state", "PA")
                        .param("useScope", "whole-house")
                        .param("householdSize", "4"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Softener Sizing Preview")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("48k grain class")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("6,000")));
    }

    @Test
    void savedResultViewAndPdfRender() throws Exception {
        MvcResult result = mockMvc.perform(post("/tool/result")
                        .param("entryMode", "result-first")
                        .param("analyteName", "nitrate")
                        .param("resultValue", "12")
                        .param("unit", "mg/L")
                        .param("qualifier", "none")
                        .param("sampleDate", "2026-03-01")
                        .param("sampleSource", "raw well")
                        .param("labCertified", "yes")
                        .param("state", "IA")
                        .param("useScope", "drinking-only")
                        .param("infantPresent", "true"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Matcher matcher = SAVED_RESULT_PATH.matcher(html);
        org.junit.jupiter.api.Assertions.assertTrue(matcher.find(), "saved result link should be rendered");
        String snapshotId = matcher.group(1);

        mockMvc.perform(get("/result/saved/" + snapshotId))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Decision result")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Open saved view")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Follow-Up")));

        MvcResult pdfResult = mockMvc.perform(get("/result/saved/" + snapshotId + ".pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString("application/pdf")))
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andReturn();

        org.junit.jupiter.api.Assertions.assertTrue(
                new String(pdfResult.getResponse().getContentAsByteArray(), 0, 5, StandardCharsets.US_ASCII).equals("%PDF-"),
                "pdf response should start with %PDF-"
        );
    }

    @Test
    void pseoDetailPageRenders() throws Exception {
        mockMvc.perform(get("/well-water/nitrate"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nitrate in Well Water What To Do")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"https://wellwater.test/well-water/nitrate\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Breadcrumb")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/tool/result-first?analyte=nitrate&amp;slug=nitrate")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Do not buy yet")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("One-line call")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Three actions before you buy anything")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What changes the decision fastest")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Escalate now if")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Questions that should be answered before a purchase")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Official source")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lead capture")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Request follow-up")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Method, review, and disclosure")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Related next reads")));
    }

    @Test
    void secondWaveWinnerPageRendersDecisionSplitsAndEscalation() throws Exception {
        mockMvc.perform(get("/well-water/pfas"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("PFAS in Well Water What To Do")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What changes the decision fastest")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Escalate now if")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Official source")));
    }

    @Test
    void regionalPageRendersStateAwareHandoff() throws Exception {
        mockMvc.perform(get("/well-water/new-hampshire-arsenic-well-water"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Regional guide")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/tool/result-first?slug=new-hampshire-arsenic-well-water&amp;state=NH")));
    }

    @Test
    void strategicAuthorityAndRegionalPagesRenderFaqBlocks() throws Exception {
        mockMvc.perform(get("/well-water/how-to-read-a-well-water-lab-report"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("How do I read a well water lab report?")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Questions that should be answered before a purchase")));

        mockMvc.perform(get("/well-water/new-jersey-pwta-private-well-testing"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What does the New Jersey PWTA test for?")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What changes the decision fastest")));
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
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Well water testing and decision articles")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Start with these pages in this family")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What this hub should help you avoid")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lead capture")));
    }

    @Test
    void trustPagesRenderAsPublicAssets() throws Exception {
        mockMvc.perform(get("/trust"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trust pages")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Methodology")));

        mockMvc.perform(get("/trust/methodology"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("How This Site Turns Well-Water Clues Into Next Steps")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"https://wellwater.test/trust/methodology\"")));
    }
}
