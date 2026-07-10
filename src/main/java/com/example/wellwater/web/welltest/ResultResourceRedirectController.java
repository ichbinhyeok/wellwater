package com.example.wellwater.web.welltest;

import com.example.wellwater.welltest.PivotMetricService;
import com.example.wellwater.welltest.ResultResourceLinkService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ResultResourceRedirectController {

    private final ResultResourceLinkService resultResourceLinkService;
    private final PivotMetricService pivotMetricService;

    public ResultResourceRedirectController(
            ResultResourceLinkService resultResourceLinkService,
            PivotMetricService pivotMetricService
    ) {
        this.resultResourceLinkService = resultResourceLinkService;
        this.pivotMetricService = pivotMetricService;
    }

    @GetMapping("/out/resource/{kind}/{targetToken}")
    public ResponseEntity<Void> redirect(
            @PathVariable String kind,
            @PathVariable String targetToken,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String family
    ) {
        String target = resultResourceLinkService.isAllowedKind(kind)
                ? resultResourceLinkService.resolve(targetToken).orElse("/")
                : "/";
        if (target.startsWith("https://")) {
            pivotMetricService.tryRecord(
                    "resource_clicked",
                    "chatgpt".equalsIgnoreCase(source) ? "chatgpt" : "web",
                    resultResourceLinkService.safeFamily(family),
                    "",
                    kind,
                    0L
            );
        }

        URI location = URI.create(target);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .cacheControl(CacheControl.noStore())
                .header("X-Robots-Tag", "noindex, nofollow, noarchive")
                .build();
    }
}
