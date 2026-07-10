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
                .description("Compact private-well testing verdict, recommended panel, official paths, and optional physical test-kit handoff.")
                .mimeType(WIDGET_MIME_TYPE)
                .meta(Map.of("ui", Map.of(
                        "domain", "https://waterverdict.com",
                        "csp", Map.of(
                                "connectDomains", List.of(),
                                "resourceDomains", List.of("https://waterverdict.com")
                        )
                )))
                .build();
        var widget = new McpStatelessServerFeatures.SyncResourceSpecification(
                widgetResource,
                (context, request) -> new McpSchema.ReadResourceResult(List.of(
                        new McpSchema.TextResourceContents(WellTestMcpAdapter.WIDGET_URI, WIDGET_MIME_TYPE, widgetHtml)
                ))
        );

        return McpServer.sync(mcpTransport)
                .serverInfo(new McpSchema.Implementation("water-verdict", "Water Verdict: Well Test Finder", "1.0.0"))
                .instructions("Use this app only to choose a U.S. private-well testing plan before treatment or test-kit purchase. Keep commerce secondary to certified testing, official guidance, and the user's explicit context.")
                .capabilities(McpSchema.ServerCapabilities.builder().tools(false).resources(false, false).build())
                .tools(adapter.toolSpecification())
                .resources(widget)
                .build();
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
