package com.example.wellwater.web.tool;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToolRequestTest {

    @Test
    void toDecisionInputUsesExistingTreatmentsWhenPrimaryFieldIsBlank() {
        ToolRequest request = new ToolRequest();
        request.setEntryMode("result-first");
        request.setExistingTreatments(List.of("ro", "uv"));
        request.setSupportingSignals(List.of("lead", "after-flood"));
        CompanionReportLineForm line = new CompanionReportLineForm();
        line.setAnalyteName("ph");
        line.setResultValue("6.1");
        line.setUnit("su");
        line.setQualifier("none");
        request.setCompanionLines(List.of(line));

        var input = request.toDecisionInput();

        assertEquals("ro", input.normalizedExistingTreatment());
        assertEquals(List.of("ro", "uv"), input.normalizedExistingTreatments());
        assertEquals(List.of("lead", "after-flood"), input.normalizedSupportingSignals());
        assertEquals(1, input.companionReportLines().size());
        assertEquals("ph", input.companionReportLines().get(0).normalizedAnalyteName());
    }
}
