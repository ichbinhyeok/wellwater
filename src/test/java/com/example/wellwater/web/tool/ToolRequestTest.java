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

        var input = request.toDecisionInput();

        assertEquals("ro", input.normalizedExistingTreatment());
        assertEquals(List.of("ro", "uv"), input.normalizedExistingTreatments());
    }
}
