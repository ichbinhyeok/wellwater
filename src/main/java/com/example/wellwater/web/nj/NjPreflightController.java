package com.example.wellwater.web.nj;

import com.example.wellwater.nj.NjDistributionMetricService;
import com.example.wellwater.nj.NjMunicipalitySummary;
import com.example.wellwater.nj.NjPartner;
import com.example.wellwater.nj.NjPartnerCatalogService;
import com.example.wellwater.nj.NjPreflightResult;
import com.example.wellwater.nj.NjPreflightService;
import com.example.wellwater.nj.NjPwtaDataService;
import com.example.wellwater.nj.NjPwtaRulesService;
import com.example.wellwater.web.page.SeoMetadataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.Locale;
import java.util.Optional;

@Controller
public class NjPreflightController {

    private final NjPreflightService preflightService;
    private final NjPwtaDataService dataService;
    private final NjPartnerCatalogService partnerCatalogService;
    private final NjDistributionMetricService metricService;
    private final SeoMetadataService seoMetadataService;

    public NjPreflightController(
            NjPreflightService preflightService,
            NjPwtaDataService dataService,
            NjPartnerCatalogService partnerCatalogService,
            NjDistributionMetricService metricService,
            SeoMetadataService seoMetadataService
    ) {
        this.preflightService = preflightService;
        this.dataService = dataService;
        this.partnerCatalogService = partnerCatalogService;
        this.metricService = metricService;
        this.seoMetadataService = seoMetadataService;
    }

    @GetMapping("/nj-well-preflight")
    public String landing(Model model) {
        NjPreflightForm form = new NjPreflightForm();
        form.setChannel("direct");
        form.setSource("main");
        populateLanding(model, form, null, null, "index,follow", "/nj-well-preflight");
        return "pages/nj/preflight";
    }

    @GetMapping("/nj/private-well/{municipalitySlug}")
    public String municipality(
            @PathVariable String municipalitySlug,
            Model model,
            HttpServletResponse response
    ) {
        Optional<NjMunicipalitySummary> municipality = dataService.findMunicipality(municipalitySlug)
                .filter(NjMunicipalitySummary::pilot);
        if (municipality.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("path", "/nj/private-well/" + municipalitySlug);
            return "pages/not-found";
        }
        NjPreflightForm form = new NjPreflightForm();
        form.setMunicipalitySlug(municipality.get().slug());
        form.setChannel("organic_local");
        form.setSource(municipality.get().slug());
        populateLanding(
                model,
                form,
                municipality.get(),
                null,
                "index,follow",
                "/nj/private-well/" + municipality.get().slug()
        );
        return "pages/nj/preflight";
    }

    @GetMapping("/partners/{partnerSlug}/nj-well-preflight")
    public String partner(
            @PathVariable String partnerSlug,
            Model model,
            HttpServletResponse response
    ) {
        Optional<NjPartner> partner = partnerCatalogService.active(partnerSlug);
        if (partner.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
            model.addAttribute("path", "/partners/" + partnerSlug + "/nj-well-preflight");
            return "pages/not-found";
        }
        NjPreflightForm form = new NjPreflightForm();
        form.setChannel("partner");
        form.setSource(partner.get().slug());
        form.setPartnerSlug(partner.get().slug());
        populateLanding(model, form, null, partner.get(), "noindex,nofollow", "/nj-well-preflight");
        response.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
        return "pages/nj/preflight";
    }

    @PostMapping("/nj-well-preflight/result")
    public String result(
            @ModelAttribute NjPreflightForm form,
            Model model,
            HttpServletResponse response
    ) {
        response.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
        response.setHeader("Cache-Control", "no-store, private, max-age=0");
        long started = System.nanoTime();
        try {
            NjPreflightResult result = preflightService.create(form.toRequest());
            metricService.tryRecord(
                    "preflight_completed",
                    result.channel(),
                    result.source(),
                    result.basePlan().resultFamily(),
                    "result",
                    "success",
                    elapsedMs(started)
            );
            model.addAttribute("result", result);
            model.addAttribute("error", "");
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ChannelSource tracking = tracking(form.getChannel(), form.getSource(), form.getPartnerSlug(), form.getMunicipalitySlug());
            metricService.tryRecord("preflight_failed", tracking.channel(), tracking.source(), "", "result", "invalid_input", elapsedMs(started));
            model.addAttribute("result", null);
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("seo", seoMetadataService.publicTool(
                "/nj-well-preflight",
                "NJ Private-Well Transaction Preflight | Water Verdict",
                "Check the New Jersey PWTA path, county-specific panel, and public aggregate well-testing context before a sale or lease.",
                "noindex,nofollow",
                "NJ Well Preflight"
        ));
        return "pages/nj/result";
    }

    @PostMapping("/nj-well-preflight/event")
    @ResponseBody
    public ResponseEntity<Void> event(
            @RequestParam String eventName,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String partnerSlug,
            @RequestParam(required = false) String municipalitySlug
    ) {
        if (!"landing_view".equals(eventName) && !"tool_started".equals(eventName)) {
            return ResponseEntity.badRequest().build();
        }
        ChannelSource tracking = tracking(channel, source, partnerSlug, municipalitySlug);
        metricService.tryRecord(eventName, tracking.channel(), tracking.source(), "", "web", "recorded", 0L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nj-well-preflight/out/{destination}")
    public ResponseEntity<Void> officialRedirect(
            @PathVariable String destination,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String source
    ) {
        String target;
        String event;
        switch (destination) {
            case "pwta" -> {
                target = NjPwtaRulesService.PWTA_URL;
                event = "official_clicked";
            }
            case "testing" -> {
                target = NjPwtaRulesService.TESTING_URL;
                event = "official_clicked";
            }
            case "certified-labs" -> {
                target = NjPwtaRulesService.CERTIFIED_LABS_URL;
                event = "certified_lab_clicked";
            }
            default -> {
                return ResponseEntity.notFound().build();
            }
        }
        ChannelSource tracking = tracking(channel, source, "", "");
        metricService.tryRecord(event, tracking.channel(), tracking.source(), "transaction", destination, "redirect", 0L);
        return redirect(target);
    }

    @GetMapping("/nj-well-preflight/out/partner/{partnerSlug}")
    public ResponseEntity<Void> partnerRedirect(
            @PathVariable String partnerSlug,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String source
    ) {
        Optional<NjPartner> partner = partnerCatalogService.active(partnerSlug);
        if (partner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ChannelSource tracking = tracking(channel, source, partnerSlug, "");
        metricService.tryRecord("partner_clicked", tracking.channel(), tracking.source(), "transaction", partner.get().type(), "redirect", 0L);
        return redirect(partner.get().bookingUrl());
    }

    private void populateLanding(
            Model model,
            NjPreflightForm form,
            NjMunicipalitySummary preview,
            NjPartner partner,
            String robots,
            String canonicalPath
    ) {
        String title = preview == null
                ? "NJ Private-Well Transaction Preflight | Water Verdict"
                : preview.name() + " Private-Well Sale Testing | Water Verdict";
        String description = preview == null
                ? "Check the New Jersey PWTA path, county-specific panel, and public aggregate well-testing context before a sale or lease."
                : "Start the NJ PWTA transaction path for " + preview.name() + " and review official municipality-level private-well testing summaries before closing.";
        model.addAttribute("form", form);
        model.addAttribute("municipalities", dataService.allMunicipalities());
        model.addAttribute("preview", preview);
        model.addAttribute("partner", partner);
        model.addAttribute("seo", seoMetadataService.publicTool(canonicalPath, title, description, robots, "NJ Well Preflight"));
    }

    private ChannelSource tracking(String channel, String source, String partnerSlug, String municipalitySlug) {
        if ("partner".equalsIgnoreCase(channel) && partnerCatalogService.active(partnerSlug).isPresent()) {
            return new ChannelSource("partner", partnerSlug.toLowerCase(Locale.ROOT));
        }
        Optional<NjMunicipalitySummary> municipality = dataService.findMunicipality(municipalitySlug)
                .filter(NjMunicipalitySummary::pilot);
        if ("organic_local".equalsIgnoreCase(channel) && municipality.isPresent()) {
            return new ChannelSource("organic_local", municipality.get().slug());
        }
        if ("organic_local".equalsIgnoreCase(channel)) {
            Optional<NjMunicipalitySummary> sourceMunicipality = dataService.findMunicipality(source)
                    .filter(NjMunicipalitySummary::pilot);
            if (sourceMunicipality.isPresent()) {
                return new ChannelSource("organic_local", sourceMunicipality.get().slug());
            }
        }
        return new ChannelSource("direct", "main");
    }

    private ResponseEntity<Void> redirect(String target) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .location(URI.create(target))
                .build();
    }

    private long elapsedMs(long started) {
        return Math.max(0L, (System.nanoTime() - started) / 1_000_000L);
    }

    private record ChannelSource(String channel, String source) {
    }
}
