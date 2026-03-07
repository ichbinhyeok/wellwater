package com.example.wellwater.decision.normalize;

import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DecisionInputNormalizationService {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[-+]?\\d*\\.?\\d+");

    private final DecisionRegistryService registryService;

    public DecisionInputNormalizationService(DecisionRegistryService registryService) {
        this.registryService = registryService;
    }

    public DecisionNormalizedInput normalize(DecisionInput input) {
        String analyte = input.normalizedAnalyte();
        String symptom = input.normalizedSymptom();
        String trigger = input.normalizedTrigger();
        String state = input.normalizedState();

        QualifierType qualifierType = resolveQualifier(input);
        Double numericResult = parseNumber(input.normalizedResultValue());
        Double qualifierNumeric = resolveQualifierNumeric(input, qualifierType);

        boolean unitSupported = resolveUnitSupport(analyte, input.normalizedUnit(), input.entryMode());
        SampleFreshness freshness = resolveSampleFreshness(input.normalizedSampleDate());
        int completeness = scoreCompleteness(input);

        return new DecisionNormalizedInput(
                analyte,
                symptom,
                trigger,
                state,
                qualifierType,
                numericResult,
                qualifierNumeric,
                unitSupported,
                freshness,
                completeness
        );
    }

    private QualifierType resolveQualifier(DecisionInput input) {
        String q = input.normalizedQualifier();
        if (!q.isBlank()) {
            return switch (q) {
                case "nd" -> QualifierType.ND;
                case "less_than", "lt", "<" -> QualifierType.LESS_THAN;
                case "estimated" -> QualifierType.ESTIMATED;
                case "positive", "detected" -> QualifierType.POSITIVE;
                case "negative", "not_detected", "not detected" -> QualifierType.NEGATIVE;
                case "none" -> QualifierType.NONE;
                default -> QualifierType.UNKNOWN;
            };
        }
        String result = input.normalizedResultValue();
        if (result.contains("nd")) {
            return QualifierType.ND;
        }
        if (result.startsWith("<")) {
            return QualifierType.LESS_THAN;
        }
        if (result.contains("negative") || result.contains("not detected") || result.contains("undetected")) {
            return QualifierType.NEGATIVE;
        }
        if (result.contains("positive") || result.contains("detected")) {
            return QualifierType.POSITIVE;
        }
        return QualifierType.NONE;
    }

    private Double resolveQualifierNumeric(DecisionInput input, QualifierType qualifierType) {
        Double explicit = parseNumber(input.normalizedQualifierValue());
        if (explicit != null) {
            return explicit;
        }
        if (qualifierType == QualifierType.LESS_THAN) {
            return parseNumber(input.normalizedResultValue());
        }
        return null;
    }

    private boolean resolveUnitSupport(String analyte, String unit, EntryMode entryMode) {
        if (entryMode != EntryMode.RESULT_FIRST) {
            return true;
        }
        if (analyte.isBlank()) {
            return false;
        }
        var maybe = registryService.findContaminant(analyte);
        if (maybe.isEmpty()) {
            return false;
        }
        List<String> whitelist = maybe.get().unitWhitelist();
        if (whitelist.isEmpty()) {
            return true;
        }
        if (unit.isBlank()) {
            return false;
        }
        return whitelist.stream().anyMatch(allowed -> allowed.equalsIgnoreCase(unit));
    }

    private SampleFreshness resolveSampleFreshness(String sampleDate) {
        if (sampleDate == null || sampleDate.isBlank()) {
            return SampleFreshness.UNKNOWN;
        }
        try {
            LocalDate parsed = LocalDate.parse(sampleDate);
            long days = ChronoUnit.DAYS.between(parsed, LocalDate.now());
            if (days < 0) {
                return SampleFreshness.UNKNOWN;
            }
            if (days <= 30) {
                return SampleFreshness.FRESH;
            }
            if (days <= 90) {
                return SampleFreshness.AGING;
            }
            return SampleFreshness.STALE;
        } catch (Exception e) {
            return SampleFreshness.UNKNOWN;
        }
    }

    private int scoreCompleteness(DecisionInput input) {
        List<String> required = switch (input.entryMode()) {
            case RESULT_FIRST -> List.of(
                    input.normalizedAnalyte(),
                    input.normalizedResultValue(),
                    input.normalizedUnit(),
                    input.normalizedSampleDate(),
                    input.normalizedSampleSource(),
                    input.normalizedLabCertified(),
                    input.normalizedState()
            );
            case SYMPTOM_FIRST -> List.of(
                    input.normalizedSymptom(),
                    input.normalizedSampleSource(),
                    input.normalizedLabCertified(),
                    input.normalizedState()
            );
            case TRIGGER_FIRST -> List.of(
                    input.normalizedTrigger(),
                    input.normalizedSampleSource(),
                    input.normalizedLabCertified(),
                    input.normalizedState()
            );
        };

        int present = 0;
        for (String field : required) {
            if (!field.isBlank()) {
                present++;
            }
        }
        return Math.round((present * 100.0f) / required.size());
    }

    private Double parseNumber(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        Matcher matcher = NUMBER_PATTERN.matcher(text);
        if (!matcher.find()) {
            return null;
        }
        try {
            return Double.parseDouble(matcher.group());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
