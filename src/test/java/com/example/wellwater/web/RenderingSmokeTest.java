package com.example.wellwater.web;

import com.example.wellwater.pseo.PseoCatalogService;
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

    @Autowired
    private PseoCatalogService pseoCatalogService;

    @Test
    void homePageRendersRealContent() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Water Verdict")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"https://waterverdict.test/\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Find the right water test before you buy the wrong one.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Why are you testing your well?")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("action=\"/tool/test-plan\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Annual check")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("After a flood")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nearby risk context")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No contact details")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Request follow-up"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("High-signal pages"))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("application/ld+json")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("@content()"))));
    }

    @Test
    void legacyToolLandingRedirectsToTheSingleToolSurface() throws Exception {
        mockMvc.perform(get("/tool"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/#test-plan"));
    }

    @Test
    void webTestPlanRendersAnImmediateResultWithoutCollectingContactDetails() throws Exception {
        mockMvc.perform(post("/tool/test-plan")
                        .param("reason", "after_flood")
                        .param("signals", "cloudy_water")
                        .param("stateCode", "PA")
                        .param("existingTreatment", "none")
                        .param("useScope", "drinking_only"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Use an alternate drinking-water source")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Turbidity")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Use a certified path")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("View matching test kit"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("email"))));
    }

    @Test
    void resultFirstPageRenders() throws Exception {
        mockMvc.perform(get("/tool/result-first"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("noindex,nofollow")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add lab and household context")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No symbol (Exact number)")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("MPN / 100mL")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Companion Report Lines")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add report line")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Other Context Flags")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Run with basic result")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Run with added context")));
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
                        .param("sampleDate", java.time.LocalDate.now().minusDays(30).toString())
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
                        .param("sampleDate", java.time.LocalDate.now().minusDays(30).toString())
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
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Request follow-up"))));

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
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"https://waterverdict.test/well-water/nitrate\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Breadcrumb")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ctaType=detail_tool_cta")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ctaType=detail_primary_cta")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ctaType=detail_compare_cta")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ctaType=detail_support_link")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Do not buy yet")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("One-line call")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Three actions before you buy anything")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What changes the decision fastest")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Escalate now if")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Questions that should be answered before a purchase")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Official source")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Request follow-up"))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Method, review, and disclosure")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Where to go from here")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Decision router")));
    }

    @Test
    void everyPseoDetailPageRendersWithoutServerError() throws Exception {
        for (var page : pseoCatalogService.allPages()) {
            mockMvc.perform(get("/well-water/" + page.slug()))
                    .andExpect(status().isOk());
        }
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
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/tool/out?target=/tool/result-first?slug%3Dnew-hampshire-arsenic-well-water%26state%3DNH")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ctaType=detail_tool_cta")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Why New Hampshire changes the answer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Open NH guidance")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Find NH certified lab")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What changes the decision fastest")));
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
                .andExpect(content().string(org.hamcrest.Matchers.containsString("noindex,follow")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("RO vs Adsorptive Media for Arsenic in Well Water")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Slow down category shopping until scope and claims are clearer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Related regional reads")));
    }

    @Test
    void authorityFamilyPageRendersWithoutLeadCaptureSurface() throws Exception {
        mockMvc.perform(get("/well-water/family/authority"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Use the method layer to start the right decision path")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Pick the first path, not the first article")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What this family should stop you from doing")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Request follow-up"))));
    }

    @Test
    void trustPagesRenderAsPublicAssets() throws Exception {
        mockMvc.perform(get("/trust"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trust pages")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Methodology")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Request follow-up"))));

        mockMvc.perform(get("/trust/methodology"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("How This Site Turns Well-Water Clues Into Next Steps")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rel=\"canonical\" href=\"https://waterverdict.test/trust/methodology\"")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Request follow-up"))));

        mockMvc.perform(get("/trust/terms"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Water Verdict Terms Of Use")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("support@waterverdict.com")));
    }
}
