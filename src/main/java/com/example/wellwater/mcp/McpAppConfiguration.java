package com.example.wellwater.mcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpStatelessServerFeatures;
import io.modelcontextprotocol.server.McpStatelessSyncServer;
import io.modelcontextprotocol.server.transport.DefaultServerTransportSecurityValidator;
import io.modelcontextprotocol.server.transport.HttpServletStatelessServerTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class McpAppConfiguration {

    private static final String WIDGET_MIME_TYPE = "text/html;profile=mcp-app";

    @Bean
    HttpServletStatelessServerTransport mcpTransport(@Value("${app.site.base-url:}") String siteBaseUrl) {
        var security = DefaultServerTransportSecurityValidator.builder()
                .allowedOrigins(List.of(
                        "https://chatgpt.com",
                        "https://waterverdict.com",
                        "http://localhost:*",
                        "http://127.0.0.1:*"
                ))
                .allowedHosts(allowedHosts(siteBaseUrl))
                .build();
        return HttpServletStatelessServerTransport.builder()
                .jsonMapper(new JacksonMcpJsonMapper(
                        tools.jackson.databind.json.JsonMapper.shared()
                                .rebuild()
                                .addMixIn(Throwable.class, SanitizedThrowable.class)
                                .build()
                ))
                .messageEndpoint("/mcp")
                .securityValidator(security)
                .build();
    }

    @Bean
    ServletRegistrationBean<HttpServletStatelessServerTransport> mcpServletRegistration(
            HttpServletStatelessServerTransport mcpTransport
    ) {
        ServletRegistrationBean<HttpServletStatelessServerTransport> registration =
                new ServletRegistrationBean<>(mcpTransport, "/mcp");
        registration.setName("waterVerdictMcp");
        registration.setLoadOnStartup(1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean(destroyMethod = "close")
    McpStatelessSyncServer mcpServer(
            HttpServletStatelessServerTransport mcpTransport,
            WellTestMcpAdapter adapter
    ) throws IOException {
        String widgetHtml = new ClassPathResource("mcp/well-test-widget.html")
                .getContentAsString(StandardCharsets.UTF_8);
        McpSchema.Resource widgetResource = McpSchema.Resource.builder()
                .uri(WellTestMcpAdapter.WIDGET_URI)
                .name("well-test-plan-widget")
                .title("Private-well test plan")
                .description("Action-first private-well testing verdict with next steps, a focused panel, official guidance, and a certified-laboratory path.")
                .mimeType(WIDGET_MIME_TYPE)
                .meta(widgetMeta())
                .build();
        var widget = new McpStatelessServerFeatures.SyncResourceSpecification(
                widgetResource,
                (context, request) -> new McpSchema.ReadResourceResult(List.of(
                        new McpSchema.TextResourceContents(WellTestMcpAdapter.WIDGET_URI, WIDGET_MIME_TYPE, widgetHtml)
                ))
        );

        return McpServer.sync(mcpTransport)
                .serverInfo(new McpSchema.Implementation("water-verdict", "Water Verdict: Private Well Test Finder", "1.1.0"))
                .instructions("Use this app only to choose a U.S. private-well testing plan before treatment decisions. Invoke it for direct or indirect well-testing intent. When private-well ownership is clear but the reason is general, use reason 'other'; omit unknown optional fields rather than delaying the first plan. Do not invoke it for municipal water, report interpretation, diagnosis, treatment sizing, legal clearance, or shopping. The app exposes no commerce.")
                .capabilities(McpSchema.ServerCapabilities.builder().tools(false).resources(false, false).build())
                .tools(adapter.toolSpecification())
                .resources(widget)
                .build();
    }

    private Map<String, Object> widgetMeta() {
        Map<String, Object> ui = new LinkedHashMap<>();
        ui.put("domain", "https://waterverdict.com");
        ui.put("prefersBorder", false);
        ui.put("csp", Map.of(
                "connectDomains", List.of(),
                "resourceDomains", List.of("https://waterverdict.com")
        ));

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("ui", Map.copyOf(ui));
        meta.put("openai/widgetDescription", "Shows the user's private-well verdict, first three actions, tests to request, what to avoid, official guidance, and a certified-lab path.");
        meta.put("openai/widgetPrefersBorder", false);
        meta.put("openai/widgetDomain", "https://waterverdict.com");
        meta.put("openai/widgetCSP", Map.of(
                "resource_domains", List.of("https://waterverdict.com"),
                "redirect_domains", List.of("https://waterverdict.com")
        ));
        return Map.copyOf(meta);
    }

    private List<String> allowedHosts(String siteBaseUrl) {
        List<String> hosts = new ArrayList<>(List.of("localhost:*", "127.0.0.1:*", "waterverdict.com:*", "www.waterverdict.com:*"));
        if (siteBaseUrl != null && !siteBaseUrl.isBlank()) {
            try {
                String host = java.net.URI.create(siteBaseUrl.trim()).getHost();
                if (host != null && !host.isBlank()) {
                    hosts.add(host + ":*");
                }
            } catch (IllegalArgumentException ignored) {
                // Production guardrails reject invalid base URLs; local development keeps safe defaults.
            }
        }
        return List.copyOf(new java.util.LinkedHashSet<>(hosts));
    }

    @JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
    private abstract static class SanitizedThrowable {
    }
}
