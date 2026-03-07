package com.example.wellwater.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AdminBasicAuthInterceptor implements HandlerInterceptor {

    private final String username;
    private final String password;

    public AdminBasicAuthInterceptor(
            @Value("${app.admin.username:admin}") String username,
            @Value("${app.admin.password:shinhyeok}") String password
    ) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
        response.setHeader("Cache-Control", "no-store, max-age=0");
        response.setHeader("Pragma", "no-cache");

        if (isAuthorized(request.getHeader("Authorization"))) {
            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"wellwater-admin\"");
        return false;
    }

    private boolean isAuthorized(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            return false;
        }

        try {
            String decoded = new String(
                    Base64.getDecoder().decode(authorizationHeader.substring(6)),
                    StandardCharsets.UTF_8
            );
            int separator = decoded.indexOf(':');
            if (separator < 0) {
                return false;
            }
            String candidateUser = decoded.substring(0, separator);
            String candidatePassword = decoded.substring(separator + 1);
            return username.equals(candidateUser) && password.equals(candidatePassword);
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }
}
