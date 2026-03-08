package com.example.wellwater.web.result;

import com.example.wellwater.decision.model.DecisionResult;
import com.example.wellwater.decision.model.ScenarioOption;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ResultPdfService {

    private static final float PAGE_MARGIN = 52f;
    private static final float PAGE_WIDTH = PDRectangle.LETTER.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.LETTER.getHeight();
    private static final float CONTENT_WIDTH = PAGE_WIDTH - (PAGE_MARGIN * 2);
    private static final float BODY_LINE_HEIGHT = 14f;
    private static final float TITLE_LINE_HEIGHT = 16f;
    private static final float FOOTER_BASELINE = 26f;
    private static final PDFont BODY_FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDFont BOLD_FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final PDFont OBLIQUE_FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.US).withZone(ZoneId.of("America/New_York"));
    private static final Color INK = new Color(18, 25, 20);
    private static final Color MUTED = new Color(95, 111, 99);
    private static final Color ACCENT = new Color(15, 118, 110);
    private static final Color ACCENT_TINT = new Color(237, 245, 243);
    private static final Color PANEL = new Color(247, 249, 245);
    private static final Color BORDER = new Color(217, 224, 208);
    private static final Color SAFE = new Color(31, 157, 115);
    private static final Color WARN = new Color(216, 134, 45);
    private static final Color DANGER = new Color(210, 85, 63);

    public byte[] render(ResultSnapshot snapshot) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Cursor cursor = new Cursor(document);
            DecisionResult result = snapshot.result();

            drawHeroCard(cursor, snapshot, result);
            drawSummaryPanel(cursor, result);

            writeSectionTitle(cursor, "Decision summary");
            writeParagraph(cursor, result.primaryVerdictLabel(), BOLD_FONT, 17f, 22f);
            writeParagraph(cursor, result.primaryVerdictSentence(), BODY_FONT, 11f, 16f, PAGE_MARGIN, CONTENT_WIDTH, INK);
            writeBulletList(cursor, List.of(
                    "Priority: " + result.consumerPriorityLabel(),
                    "Support: " + result.consumerSupportLabel(),
                    "Likely scope: " + result.consumerScopeLabel(),
                    "Confidence: " + result.confidence().label()
            ));

            if (!result.keyReasons().isEmpty()) {
                writeSectionTitle(cursor, "Key reasons");
                writeBulletList(cursor, result.keyReasons());
            }

            if (result.hasReportLinesReviewed()) {
                writeSectionTitle(cursor, "Report lines reviewed");
                List<String> reportLines = new ArrayList<>();
                result.reportLinesReviewed().forEach(line -> reportLines.add(
                        (line.primary() ? "Primary" : "Supporting")
                                + " | "
                                + line.analyteLabel()
                                + " | "
                                + line.observedValueLabel()
                                + " | "
                                + line.interpretationNote()
                ));
                writeBulletList(cursor, reportLines);
            }

            writeSectionTitle(cursor, "Action timeline");
            writeLabeledBullets(cursor, "Today", result.todayActions());
            writeLabeledBullets(cursor, "This week", result.thisWeekActions());
            writeLabeledBullets(cursor, "Next 30 days", result.laterActions());

            if (result.hasRecommendedTestCards()) {
                writeSectionTitle(cursor, "Recommended tests");
                if (result.hasRecommendedTestOrderNote()) {
                    writeParagraph(cursor, result.recommendedTestOrderNote(), BODY_FONT, 10.5f, 15f, PAGE_MARGIN, CONTENT_WIDTH, INK);
                }
                for (int i = 0; i < result.recommendedTestCards().size(); i++) {
                    var card = result.recommendedTestCards().get(i);
                    List<String> items = new ArrayList<>();
                    items.add("Why now: " + card.whyNow());
                    items.add("Sample plan: " + card.samplePlan());
                    if (card.hasResourceLink()) {
                        items.add(card.resourceLabel() + ": " + card.resourceUrl());
                    }
                    drawInfoPanel(cursor, "Step " + (i + 1) + " | " + card.testName(), items);
                }
            }

            if (!result.scenarios().isEmpty()) {
                writeSectionTitle(cursor, "Scenario compare");
                for (ScenarioOption scenario : result.scenarios()) {
                    drawScenarioCard(cursor, scenario, result.isRecommendedScenario(scenario));
                }
            }

            if (result.hasSoftenerSizingPreview()) {
                writeSectionTitle(cursor, "Softener sizing preview");
                List<String> items = new ArrayList<>();
                items.add(result.softenerSizingPreview().summary());
                if (result.softenerSizingPreview().eligible()) {
                    items.add("Recommended class: " + result.softenerSizingPreview().recommendedClass());
                    items.add("Sizing verdict: " + result.softenerSizingPreview().sizingVerdict());
                    items.add("Hardness basis: " + result.softenerSizingPreview().hardnessLabel());
                    items.add("Daily grain load: " + result.softenerSizingPreview().dailyGrainLoadLabel());
                    items.add("Regeneration pace: " + result.softenerSizingPreview().regenerationLabel());
                } else {
                    items.add("Status: " + result.softenerSizingPreview().sizingVerdict());
                }
                items.addAll(result.softenerSizingPreview().notes());
                items.add("Next action: " + result.softenerSizingPreview().nextAction());
                writeBulletList(cursor, items);
            }

            writeSectionTitle(cursor, "Data quality");
            drawInfoPanel(cursor, "Data quality", List.of(
                    "Sample freshness: " + result.sampleFreshness(),
                    "Completeness: " + result.completenessScore() + "/100"
            ));

            List<String> qualityItems = new ArrayList<>();
            if (result.hasThresholdSummary()) {
                qualityItems.add("Threshold check: " + result.thresholdSummary());
            }
            qualityItems.add("Rule version: " + result.decisionVersion());
            qualityItems.addAll(result.dataQualityNotes());
            writeBulletList(cursor, qualityItems);

            writeSectionTitle(cursor, "Follow-up links");
            drawInfoPanel(cursor, "Trusted next steps", List.of(
                    "State guidance: " + result.localGuidanceUrl(),
                    "Certified lab finder: " + result.certifiedLabUrl()
            ));

            writeSectionTitle(cursor, "Disclosure");
            drawDisclosurePanel(cursor, result.disclosureText());

            addPageFooters(document, snapshot.id());

            cursor.close();
            document.save(output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to render result PDF.", e);
        }
    }

    private void drawHeroCard(Cursor cursor, ResultSnapshot snapshot, DecisionResult result) throws IOException {
        float cardHeight = 118f;
        cursor.ensureBlockSpace(cardHeight + 16f);
        float bottom = cursor.y - cardHeight;
        cursor.drawFilledRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, cardHeight, ACCENT_TINT);
        cursor.drawFilledRect(PAGE_MARGIN, bottom, 8f, cardHeight, branchColor(result));
        cursor.drawStrokeRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, cardHeight, BORDER);

        float textX = PAGE_MARGIN + 22f;
        float textWidth = CONTENT_WIDTH - 44f;
        writeParagraph(cursor, "Private snapshot | noindex result", BOLD_FONT, 9f, 12f, textX, textWidth, ACCENT);
        writeParagraph(cursor, "Water Verdict Saved Result", BOLD_FONT, 21f, 25f, textX, textWidth, INK);
        writeParagraph(cursor, result.primaryVerdictLabel(), BOLD_FONT, 14f, 18f, textX, textWidth, branchColor(result));
        writeParagraph(cursor, result.primaryVerdictSentence(), BODY_FONT, 10f, 14f, textX, textWidth, INK);
        writeParagraph(
                cursor,
                "Saved " + displayInstant(snapshot.createdAt()) + " | Expires " + displayInstant(snapshot.expiresAt()) + " | Snapshot " + snapshot.id(),
                BODY_FONT,
                9f,
                12f,
                textX,
                textWidth,
                MUTED
        );
        cursor.addGap(10f);
    }

    private void drawSummaryPanel(Cursor cursor, DecisionResult result) throws IOException {
        float panelHeight = 82f;
        cursor.ensureBlockSpace(panelHeight + 12f);
        float bottom = cursor.y - panelHeight;
        cursor.drawFilledRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, panelHeight, PANEL);
        cursor.drawStrokeRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, panelHeight, BORDER);

        float left = PAGE_MARGIN + 18f;
        float top = cursor.y - 16f;
        drawMetricCell(cursor, "Priority", result.consumerPriorityLabel(), left, top, 116f);
        drawMetricCell(cursor, "Support", result.consumerSupportLabel(), left + 132f, top, 116f);
        drawMetricCell(cursor, "Likely scope", result.consumerScopeLabel(), left + 264f, top, 116f);
        drawMetricCell(cursor, "Confidence", result.confidence().label(), left + 396f, top, 96f);
        cursor.y = bottom - 12f;
    }

    private void drawMetricCell(Cursor cursor, String label, String value, float x, float top, float width) throws IOException {
        writeTextAt(cursor, label.toUpperCase(Locale.ROOT), BOLD_FONT, 8.5f, x, top, MUTED);
        List<String> lines = wrapText(ascii(value), BOLD_FONT, 12.5f, width);
        float y = top - 16f;
        for (String line : lines) {
            writeTextAt(cursor, line, BOLD_FONT, 12.5f, x, y, INK);
            y -= 14f;
        }
    }

    private void drawScenarioCard(Cursor cursor, ScenarioOption scenario, boolean recommended) throws IOException {
        float cardHeight = estimatedScenarioHeight(scenario);
        cursor.ensureBlockSpace(cardHeight + 10f);
        float bottom = cursor.y - cardHeight;
        Color fill = recommended ? ACCENT_TINT : PANEL;
        Color border = recommended ? ACCENT : BORDER;
        cursor.drawFilledRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, cardHeight, fill);
        cursor.drawStrokeRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, cardHeight, border);
        if (recommended) {
            cursor.drawFilledRect(PAGE_MARGIN, bottom, 8f, cardHeight, ACCENT);
        }

        float x = PAGE_MARGIN + 20f;
        float width = CONTENT_WIDTH - 40f;
        writeParagraph(cursor, scenario.scenarioTitle(), BOLD_FONT, 13f, 17f, x, width, INK);
        writeParagraph(cursor, scenario.scenarioType().toUpperCase(Locale.ROOT), BOLD_FONT, 8.5f, 12f, x, width, recommended ? ACCENT : MUTED);
        if (recommended) {
            writeParagraph(cursor, "Recommended fit", BOLD_FONT, 9f, 12f, x, width, ACCENT);
        }
        writeBulletList(cursor, List.of(
                            "Why it fits: " + scenario.fitReason(),
                            "Recommended scope: " + scenario.recommendedScope(),
                            "Upfront cost: " + scenario.estimatedCostBand(),
                            "Maintenance: " + scenario.estimatedMaintenanceBand(),
                            "Limitation: " + scenario.limitations(),
                            "Next action: " + scenario.nextAction()
                    ), x, width, INK);
        cursor.y = bottom - 10f;
    }

    private void drawInfoPanel(Cursor cursor, String title, List<String> items) throws IOException {
        float panelHeight = estimatedCardHeight(title, items);
        cursor.ensureBlockSpace(panelHeight + 10f);
        float bottom = cursor.y - panelHeight;
        cursor.drawFilledRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, panelHeight, PANEL);
        cursor.drawStrokeRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, panelHeight, BORDER);
        float x = PAGE_MARGIN + 18f;
        float width = CONTENT_WIDTH - 36f;
        writeParagraph(cursor, title, BOLD_FONT, 11.5f, 15f, x, width, INK);
        writeBulletList(cursor, items, x, width, INK);
        cursor.y = bottom - 10f;
    }

    private void drawDisclosurePanel(Cursor cursor, String disclosureText) throws IOException {
        float panelHeight = estimateParagraphHeight(disclosureText, OBLIQUE_FONT, 10.5f, 15f, CONTENT_WIDTH - 36f) + 28f;
        cursor.ensureBlockSpace(panelHeight + 8f);
        float bottom = cursor.y - panelHeight;
        cursor.drawFilledRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, panelHeight, ACCENT_TINT);
        cursor.drawStrokeRect(PAGE_MARGIN, bottom, CONTENT_WIDTH, panelHeight, BORDER);
        writeParagraph(cursor, disclosureText, OBLIQUE_FONT, 10.5f, 15f, PAGE_MARGIN + 18f, CONTENT_WIDTH - 36f, INK);
        cursor.y = bottom - 10f;
    }

    private void writeSectionTitle(Cursor cursor, String title) throws IOException {
        cursor.addGap(4f);
        float titleTop = cursor.y;
        writeTextAt(cursor, title.toUpperCase(Locale.ROOT), BOLD_FONT, 9f, PAGE_MARGIN, titleTop, ACCENT);
        float lineX = PAGE_MARGIN + 118f;
        float lineY = titleTop - 3f;
        cursor.drawLine(lineX, lineY, PAGE_MARGIN + CONTENT_WIDTH, lineY, BORDER, 1f);
        cursor.y -= 18f;
    }

    private void writeHeading(Cursor cursor, String text, float fontSize) throws IOException {
        writeParagraph(cursor, text, BOLD_FONT, fontSize, fontSize + 8f);
    }

    private void writeLabeledBullets(Cursor cursor, String label, List<String> items) throws IOException {
        if (items == null || items.isEmpty()) {
            return;
        }
        writeParagraph(cursor, label, BOLD_FONT, 11.5f, 15f, PAGE_MARGIN, CONTENT_WIDTH, INK);
        writeBulletList(cursor, items);
    }

    private void writeBulletList(Cursor cursor, List<String> items) throws IOException {
        writeBulletList(cursor, items, PAGE_MARGIN, CONTENT_WIDTH, INK);
    }

    private void writeBulletList(Cursor cursor, List<String> items, float x, float width, Color color) throws IOException {
        if (items == null || items.isEmpty()) {
            return;
        }
        for (String item : items) {
            List<String> wrapped = wrapText("- " + ascii(item), BODY_FONT, 10f, width);
            for (String line : wrapped) {
                writeParagraph(cursor, line, BODY_FONT, 10f, BODY_LINE_HEIGHT, x, width, color);
            }
        }
        cursor.addGap(4f);
    }

    private void writeParagraph(Cursor cursor, String text, PDFont font, float fontSize, float lineHeight) throws IOException {
        writeParagraph(cursor, text, font, fontSize, lineHeight, PAGE_MARGIN, CONTENT_WIDTH, INK);
    }

    private void writeParagraph(Cursor cursor, String text, PDFont font, float fontSize, float lineHeight, float x, float width, Color color) throws IOException {
        List<String> lines = wrapText(ascii(text), font, fontSize, width);
        for (String line : lines) {
            cursor.ensureLineSpace(lineHeight);
            writeTextAt(cursor, line, font, fontSize, x, cursor.y, color);
            cursor.y -= lineHeight;
        }
    }

    private void writeTextAt(Cursor cursor, String text, PDFont font, float fontSize, float x, float y, Color color) throws IOException {
        cursor.stream.beginText();
        cursor.stream.setNonStrokingColor(color);
        cursor.stream.setFont(font, fontSize);
        cursor.stream.newLineAtOffset(x, y);
        cursor.stream.showText(ascii(text));
        cursor.stream.endText();
    }

    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isBlank()) {
            lines.add("");
            return lines;
        }
        StringBuilder current = new StringBuilder();
        for (String word : text.split("\\s+")) {
            String candidate = current.isEmpty() ? word : current + " " + word;
            float width = font.getStringWidth(candidate) / 1000f * fontSize;
            if (width <= maxWidth || current.isEmpty()) {
                current.setLength(0);
                current.append(candidate);
                continue;
            }
            lines.add(current.toString());
            current.setLength(0);
            current.append(word);
        }
        if (!current.isEmpty()) {
            lines.add(current.toString());
        }
        return lines;
    }

    private float estimatedScenarioHeight(ScenarioOption scenario) throws IOException {
        float height = 0f;
        height += estimateParagraphHeight(
                scenario.scenarioTitle(),
                BOLD_FONT,
                13f,
                17f,
                CONTENT_WIDTH - 40f
        );
        height += estimateParagraphHeight(scenario.scenarioType().toUpperCase(Locale.ROOT), BOLD_FONT, 8.5f, 12f, CONTENT_WIDTH - 40f);
        height += 12f;
        for (String item : List.of(
                "Why it fits: " + scenario.fitReason(),
                "Recommended scope: " + scenario.recommendedScope(),
                "Upfront cost: " + scenario.estimatedCostBand(),
                "Maintenance: " + scenario.estimatedMaintenanceBand(),
                "Limitation: " + scenario.limitations(),
                "Next action: " + scenario.nextAction()
        )) {
            height += estimateParagraphHeight("- " + item, BODY_FONT, 10f, BODY_LINE_HEIGHT, CONTENT_WIDTH - 40f);
        }
        return height + 26f;
    }

    private float estimatedCardHeight(String title, List<String> items) throws IOException {
        float height = estimateParagraphHeight(title, BOLD_FONT, 11.5f, 15f, CONTENT_WIDTH - 36f);
        if (items != null) {
            for (String item : items) {
                height += estimateParagraphHeight("- " + item, BODY_FONT, 10f, BODY_LINE_HEIGHT, CONTENT_WIDTH - 36f);
            }
        }
        return height + 24f;
    }

    private float estimateParagraphHeight(String text, PDFont font, float fontSize, float lineHeight, float width) throws IOException {
        return wrapText(ascii(text), font, fontSize, width).size() * lineHeight;
    }

    private String displayInstant(String rawInstant) {
        if (rawInstant == null || rawInstant.isBlank()) {
            return "";
        }
        return DISPLAY_DATE.format(Instant.parse(rawInstant));
    }

    private void addPageFooters(PDDocument document, String snapshotId) throws IOException {
        int totalPages = document.getNumberOfPages();
        for (int i = 0; i < totalPages; i++) {
            PDPage page = document.getPage(i);
            try (PDPageContentStream stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                stream.setStrokingColor(BORDER);
                stream.setLineWidth(1f);
                stream.moveTo(PAGE_MARGIN, 42f);
                stream.lineTo(PAGE_WIDTH - PAGE_MARGIN, 42f);
                stream.stroke();

                stream.beginText();
                stream.setFont(BODY_FONT, 9f);
                stream.setNonStrokingColor(MUTED);
                stream.newLineAtOffset(PAGE_MARGIN, FOOTER_BASELINE);
                stream.showText("Water Verdict | Saved result " + snapshotId.substring(0, Math.min(snapshotId.length(), 8)));
                stream.endText();

                String pageLabel = "Page " + (i + 1) + " of " + totalPages;
                float width = BODY_FONT.getStringWidth(pageLabel) / 1000f * 9f;
                stream.beginText();
                stream.setFont(BODY_FONT, 9f);
                stream.setNonStrokingColor(MUTED);
                stream.newLineAtOffset(PAGE_WIDTH - PAGE_MARGIN - width, FOOTER_BASELINE);
                stream.showText(pageLabel);
                stream.endText();
            }
        }
    }

    private Color branchColor(DecisionResult result) {
        return switch (result.branch()) {
            case RED -> DANGER;
            case AMBER -> WARN;
            case GREEN -> SAFE;
        };
    }

    private String ascii(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value
                .replace('\u2018', '\'')
                .replace('\u2019', '\'')
                .replace('\u201C', '"')
                .replace('\u201D', '"')
                .replace('\u2013', '-')
                .replace('\u2014', '-')
                .replace('\u2022', '-');
        String decomposed = Normalizer.normalize(normalized, Normalizer.Form.NFKD);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < decomposed.length(); i++) {
            char ch = decomposed.charAt(i);
            if (ch == '\n' || ch == '\r' || ch == '\t') {
                out.append(' ');
                continue;
            }
            if (ch >= 32 && ch <= 126) {
                out.append(ch);
            }
        }
        return out.toString().replaceAll(" +", " ").trim();
    }

    private static final class Cursor {
        private final PDDocument document;
        private PDPage page;
        private PDPageContentStream stream;
        private float y;

        private Cursor(PDDocument document) throws IOException {
            this.document = document;
            newPage();
        }

        private void ensureLineSpace(float lineHeight) throws IOException {
            if (y - lineHeight < PAGE_MARGIN) {
                newPage();
            }
        }

        private void ensureBlockSpace(float blockHeight) throws IOException {
            if (y - blockHeight < PAGE_MARGIN) {
                newPage();
            }
        }

        private void addGap(float gap) {
            y -= gap;
        }

        private void drawFilledRect(float x, float y, float width, float height, Color color) throws IOException {
            stream.setNonStrokingColor(color);
            stream.addRect(x, y, width, height);
            stream.fill();
            stream.setNonStrokingColor(INK);
        }

        private void drawStrokeRect(float x, float y, float width, float height, Color color) throws IOException {
            stream.setStrokingColor(color);
            stream.setLineWidth(1f);
            stream.addRect(x, y, width, height);
            stream.stroke();
        }

        private void drawLine(float startX, float startY, float endX, float endY, Color color, float width) throws IOException {
            stream.setStrokingColor(color);
            stream.setLineWidth(width);
            stream.moveTo(startX, startY);
            stream.lineTo(endX, endY);
            stream.stroke();
        }

        private void newPage() throws IOException {
            if (stream != null) {
                stream.close();
            }
            page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);
            stream = new PDPageContentStream(document, page);
            y = PAGE_HEIGHT - PAGE_MARGIN;
            drawFilledRect(PAGE_MARGIN, PAGE_HEIGHT - 26f, CONTENT_WIDTH, 6f, ACCENT);
        }

        private void close() throws IOException {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
