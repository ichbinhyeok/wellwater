package com.example.wellwater.web.welltest;

import com.example.wellwater.decision.registry.StateResourceRegistryService;
import com.example.wellwater.welltest.PivotMetricService;
import com.example.wellwater.welltest.PlanResourceLink;
import com.example.wellwater.welltest.ResultResourceLinkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultResourceRedirectControllerTest {

    @TempDir
    Path tempDir;

    @Test
    void tracksAndRedirectsOnlyToRegisteredOfficialHosts() throws Exception {
        ResultResourceLinkService links = linkService();
        PivotMetricService metrics = new PivotMetricService(tempDir.resolve("metrics.csv").toString());
        ResultResourceRedirectController controller = new ResultResourceRedirectController(links, metrics);
        String officialUrl = "https://www.epa.gov/privatewells";
        PlanResourceLink tracked = links.tracked(
                new PlanResourceLink("EPA guidance", officialUrl),
                "official_guidance",
                "chatgpt",
                "baseline"
        );
        String path = URI.create(tracked.url()).getPath();
        String token = path.substring(path.lastIndexOf('/') + 1);

        var response = controller.redirect("official_guidance", token, "chatgpt", "baseline");

        assertEquals(302, response.getStatusCode().value());
        assertEquals(officialUrl, response.getHeaders().getLocation().toString());
        assertTrue(response.getHeaders().getFirst("X-Robots-Tag").contains("noindex"));
        assertEquals(1L, metrics.summary().officialGuidanceClicks());
        assertEquals(1L, metrics.summary().externalActionsByChannel().get("chatgpt"));
    }

    @Test
    void rejectsAnEncodedOpenRedirectTargetWithoutCountingIt() throws Exception {
        ResultResourceLinkService links = linkService();
        PivotMetricService metrics = new PivotMetricService(tempDir.resolve("blocked.csv").toString());
        ResultResourceRedirectController controller = new ResultResourceRedirectController(links, metrics);
        String token = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("https://evil.example/collect".getBytes(StandardCharsets.UTF_8));

        var response = controller.redirect("certified_lab", token, "chatgpt", "baseline");

        assertEquals(URI.create("/"), response.getHeaders().getLocation());
        assertEquals(0L, metrics.summary().resourceClicks());
    }

    private ResultResourceLinkService linkService() throws Exception {
        Path registryPath = tempDir.resolve("state-resources.csv");
        Files.writeString(registryPath, "state,local_guidance_url,certified_lab_url,source_url\n"
                + "US,https://www.epa.gov/privatewells,https://www.epa.gov/dwlabcert/labs,https://www.epa.gov/privatewells\n");
        return new ResultResourceLinkService(
                "https://waterverdict.com",
                new StateResourceRegistryService(registryPath.toString())
        );
    }
}
