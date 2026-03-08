package com.example.wellwater.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ProductionGuardrails {

    private final String baseUrl;
    private final String adminUsername;
    private final String adminPassword;

    public ProductionGuardrails(
            @Value("${app.site.base-url:}") String baseUrl,
            @Value("${app.admin.username:}") String adminUsername,
            @Value("${app.admin.password:}") String adminPassword
    ) {
        this.baseUrl = baseUrl == null ? "" : baseUrl.trim();
        this.adminUsername = adminUsername == null ? "" : adminUsername.trim();
        this.adminPassword = adminPassword == null ? "" : adminPassword.trim();
    }

    @PostConstruct
    void validate() {
        if (baseUrl.isBlank()) {
            throw new IllegalStateException("APP_SITE_BASE_URL must be set in production.");
        }
        String normalized = baseUrl.toLowerCase();
        if (normalized.contains("localhost") || normalized.contains("127.0.0.1")) {
            throw new IllegalStateException("APP_SITE_BASE_URL must not point to localhost in production.");
        }
        if (adminUsername.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException("APP_ADMIN_USERNAME and APP_ADMIN_PASSWORD must be set in production.");
        }
    }
}
