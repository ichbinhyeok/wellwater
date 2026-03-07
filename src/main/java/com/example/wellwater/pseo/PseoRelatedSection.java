package com.example.wellwater.pseo;

import java.util.List;

public record PseoRelatedSection(
        String title,
        String description,
        List<PseoPage> pages
) {
}
