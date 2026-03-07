package com.example.wellwater.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminRequiresBasicAuth() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", containsString("Basic realm=\"wellwater-admin\"")))
                .andExpect(header().string("X-Robots-Tag", containsString("noindex")));
    }

    @Test
    void adminDashboardRendersWhenAuthorized() throws Exception {
        mockMvc.perform(get("/admin")
                        .header("Authorization", basicAuth("admin", "shinhyeok")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Operations Dashboard")))
                .andExpect(content().string(containsString("Search Visibility Gaps")));
    }

    private String basicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
