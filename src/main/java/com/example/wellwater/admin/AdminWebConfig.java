package com.example.wellwater.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    private final AdminBasicAuthInterceptor adminBasicAuthInterceptor;

    public AdminWebConfig(AdminBasicAuthInterceptor adminBasicAuthInterceptor) {
        this.adminBasicAuthInterceptor = adminBasicAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminBasicAuthInterceptor)
                .addPathPatterns("/admin", "/admin/**");
    }
}
