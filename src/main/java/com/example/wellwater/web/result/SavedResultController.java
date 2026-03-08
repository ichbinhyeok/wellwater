package com.example.wellwater.web.result;

import com.example.wellwater.analytics.AnalyticsEventService;
import com.example.wellwater.lead.LeadCaptureContext;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class SavedResultController {

    private static final DateTimeFormatter SNAPSHOT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.US).withZone(ZoneId.of("America/New_York"));

    private final ResultSnapshotService resultSnapshotService;
    private final ResultPdfService resultPdfService;
    private final ResultCtaService resultCtaService;
    private final AnalyticsEventService analyticsEventService;

    public SavedResultController(
            ResultSnapshotService resultSnapshotService,
            ResultPdfService resultPdfService,
            ResultCtaService resultCtaService,
            AnalyticsEventService analyticsEventService
    ) {
        this.resultSnapshotService = resultSnapshotService;
        this.resultPdfService = resultPdfService;
        this.resultCtaService = resultCtaService;
        this.analyticsEventService = analyticsEventService;
    }

    @GetMapping("/result/saved/{snapshotId}")
    public String savedResult(@PathVariable String snapshotId, @RequestParam(required = false) String lead, Model model, HttpServletResponse response) {
        response.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
        ResultSnapshot snapshot = loadSnapshot(snapshotId);
        model.addAttribute("result", snapshot.result());
        model.addAttribute("sessionId", snapshot.sessionId());
        model.addAttribute("ctaLinks", resultCtaService.renderableCtas(snapshot.result(), snapshot.sessionId(), snapshot.sourceSlug()));
        model.addAttribute("savedResultUrl", savedResultPath(snapshot.id()));
        model.addAttribute("shareUrl", savedResultPath(snapshot.id()));
        model.addAttribute("pdfUrl", savedPdfPath(snapshot.id()));
        model.addAttribute("savedAtLabel", formatTimestamp(snapshot.createdAt()));
        model.addAttribute("expiresLabel", formatTimestamp(snapshot.expiresAt()));
        model.addAttribute("sharedView", true);
        model.addAttribute("leadStatus", sanitizeLeadStatus(lead));
        model.addAttribute("leadContext", buildResultLeadContext(snapshot));

        analyticsEventService.logEvent(
                "result_snapshot_viewed",
                snapshot.result().entryMode().wireValue(),
                snapshot.sessionId(),
                snapshot.sourceSlug(),
                snapshot.result().tier().label(),
                snapshot.result().branch().label(),
                null,
                savedResultPath(snapshot.id()),
                "saved-view"
        );
        return "pages/result/view";
    }

    @GetMapping(value = "/result/saved/{snapshotId}.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> savedResultPdf(@PathVariable String snapshotId) {
        ResultSnapshot snapshot = loadSnapshot(snapshotId);
        byte[] pdf = resultPdfService.render(snapshot);

        analyticsEventService.logEvent(
                "result_pdf_downloaded",
                snapshot.result().entryMode().wireValue(),
                snapshot.sessionId(),
                snapshot.sourceSlug(),
                snapshot.result().tier().label(),
                snapshot.result().branch().label(),
                null,
                savedPdfPath(snapshot.id()),
                "pdf"
        );

        return ResponseEntity.ok()
                .header("X-Robots-Tag", "noindex, nofollow, noarchive")
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("waterverdict-result-" + snapshot.id() + ".pdf")
                        .build()
                        .toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private ResultSnapshot loadSnapshot(String snapshotId) {
        return resultSnapshotService.find(snapshotId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    private String savedResultPath(String snapshotId) {
        return "/result/saved/" + snapshotId;
    }

    private String savedPdfPath(String snapshotId) {
        return savedResultPath(snapshotId) + ".pdf";
    }

    private String formatTimestamp(String instantValue) {
        if (instantValue == null || instantValue.isBlank()) {
            return "";
        }
        return SNAPSHOT_DATE_FORMAT.format(Instant.parse(instantValue));
    }

    private String sanitizeLeadStatus(String lead) {
        if ("success".equalsIgnoreCase(lead)) {
            return "success";
        }
        if ("invalid".equalsIgnoreCase(lead)) {
            return "invalid";
        }
        return "";
    }

    private LeadCaptureContext buildResultLeadContext(ResultSnapshot snapshot) {
        return new LeadCaptureContext(
                "Need a private-well follow-up on this result?",
                "Use this when you want help turning the verdict into a testing, compare, or next-action plan without losing the saved snapshot context.",
                "Request follow-up",
                savedResultPath(snapshot.id()),
                "result-snapshot",
                snapshot.result().problemType().wireValue(),
                snapshot.sourceSlug(),
                snapshot.result().primaryVerdictLabel()
        );
    }
}
