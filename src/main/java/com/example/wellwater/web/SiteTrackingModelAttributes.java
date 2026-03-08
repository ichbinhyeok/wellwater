package com.example.wellwater.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SiteTrackingModelAttributes {

    private final String googleAnalyticsId;

    public SiteTrackingModelAttributes(@Value("${app.site.google-analytics-id:}") String googleAnalyticsId) {
        this.googleAnalyticsId = googleAnalyticsId;
    }

    @ModelAttribute("googleAnalyticsId")
    public String googleAnalyticsId() {
        return googleAnalyticsId;
    }
}
