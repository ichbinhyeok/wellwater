package com.example.wellwater.web.page;

import java.util.List;

public record TrustSection(
        String title,
        String body,
        List<String> bullets
) {
}
