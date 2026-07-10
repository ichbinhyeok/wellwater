package com.example.wellwater.mcp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "app.site.base-url=https://waterverdict.com",
                "app.partner.tap-score.essential-url=https://mytapscore.com/products/essential-well-water-test?ref=test",
                "app.partner.tap-score.advanced-url=https://mytapscore.com/products/advanced-well-water-test?ref=test",
                "app.pivot.metrics.csv.path=build/test-data/mcp-pivot-metrics.csv"
        }
)
class McpAppIntegrationTest {

    @LocalServerPort
    int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void initializesListsTheSingleToolAndReturnsStructuredPlan() {
        ResponseEntity<String> initialize = post("""
                {"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2025-06-18","capabilities":{},"clientInfo":{"name":"integration-test","version":"1.0"}}}
                """);
        assertEquals(200, initialize.getStatusCode().value());
        assertNotNull(initialize.getBody());
        assertTrue(initialize.getBody().contains("Water Verdict: Private Well Test Finder"));

        ResponseEntity<String> tools = post("""
                {"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}
                """);
        assertEquals(200, tools.getStatusCode().value());
        assertTrue(tools.getBody().contains("recommend_private_well_test_plan"));
        assertTrue(tools.getBody().contains("\"readOnlyHint\":false"));
        assertTrue(tools.getBody().contains("\"idempotentHint\":false"));
        assertTrue(tools.getBody().contains("ui://widget/well-test-plan-v2.html"));
        assertTrue(tools.getBody().contains("Use this when a U.S. private-well owner asks"));
        assertTrue(tools.getBody().contains("openai/toolInvocation/invoking"));

        ResponseEntity<String> call = post("""
                {"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"recommend_private_well_test_plan","arguments":{"reason":"annual","signals":["no_obvious_issue"],"risk_contexts":[],"state_code":"NH","existing_treatment":"none","use_scope":"drinking_only"}}}
                """);
        assertEquals(200, call.getStatusCode().value());
        assertTrue(call.getBody().contains("structuredContent"));
        assertTrue(call.getBody().contains("certified baseline well-water panel"));
        assertFalse(call.getBody().contains("\"partner_offer\""));
        assertFalse(call.getBody().contains("physical test kit"));
        assertTrue(call.getBody().contains("next_steps"));
        assertTrue(call.getBody().contains("avoid_for_now"));
        assertTrue(call.getBody().contains("/out/resource/certified_lab/"));
        assertFalse(call.getBody().contains("session_id"));
    }

    @Test
    void exposesTheMcpAppWidgetResource() {
        ResponseEntity<String> resource = post("""
                {"jsonrpc":"2.0","id":4,"method":"resources/read","params":{"uri":"ui://widget/well-test-plan-v2.html"}}
                """);

        assertEquals(200, resource.getStatusCode().value());
        assertTrue(resource.getBody().contains("text/html;profile=mcp-app"));
        assertTrue(resource.getBody().contains("Recommended panel"));
        assertTrue(resource.getBody().contains("Do this first"));
        assertTrue(resource.getBody().contains("openExternal"));
        assertTrue(resource.getBody().contains("ui/initialize"));
    }

    @Test
    void rejectsUnsupportedInputWithoutEchoingIt() {
        ResponseEntity<String> call = post("""
                {"jsonrpc":"2.0","id":5,"method":"tools/call","params":{"name":"recommend_private_well_test_plan","arguments":{"reason":"annual","signals":["medical_history"],"risk_contexts":[]}}}
                """);

        assertEquals(200, call.getStatusCode().value());
        assertTrue(call.getBody().contains("isError"));
        assertTrue(call.getBody().contains("Unsupported signal"));
        assertFalse(call.getBody().contains("medical_history"));
    }

    @Test
    void malformedJsonDoesNotExposeServerStackFrames() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/mcp"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json, text/event-stream")
                .POST(HttpRequest.BodyPublishers.ofString("{"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("Invalid message format"));
        assertFalse(response.body().contains("stackTrace"));
        assertFalse(response.body().contains("classLoaderName"));
    }

    private ResponseEntity<String> post(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM));
        return restTemplate.exchange(
                "http://localhost:" + port + "/mcp",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );
    }
}
