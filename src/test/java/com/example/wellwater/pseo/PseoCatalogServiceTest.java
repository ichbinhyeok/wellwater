package com.example.wellwater.pseo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PseoCatalogServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void loadsPagesAndCountsFamilies() throws Exception {
        Path csv = tempDir.resolve("pages.csv");
        Files.writeString(csv, """
                family,slug,title,h1,meta_description,intro,action_now,what_to_test,primary_cta_label,primary_cta_url,money_cta_label,money_cta_url,disclosure,source_url,search_query,search_performed_at,fetched_at,tier
                contaminants,nitrate,nitrate guide,Nitrate in well water,nitrate meaning,quick intro,test now,retest in 7 days,Find certified lab,https://example.com/lab,Compare test kits,https://example.com/kit,Affiliate links may pay us,https://example.com/source,nitrate query,2026-03-07T00:00:00Z,2026-03-07T00:05:00Z,A
                symptoms,rotten-egg-smell,smell guide,Rotten egg smell in well water,smell meaning,quick intro,check hot and cold,test sulfur and bacteria,Check local guidance,https://example.com/guidance,Compare filters,https://example.com/filter,Affiliate links may pay us,https://example.com/source2,smell query,2026-03-07T00:00:00Z,2026-03-07T00:05:00Z,B
                """);

        PseoCatalogService service = new PseoCatalogService(csv.toString());

        assertEquals(2, service.allPages().size());
        assertEquals(1L, service.familyCounts().get("contaminants"));
        assertEquals(1L, service.familyCounts().get("symptoms"));
        assertTrue(service.findBySlug("nitrate").isPresent());
    }
}
