package com.example.wellwater.decision.normalize;

import com.example.wellwater.decision.model.DecisionInput;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.registry.DecisionRegistryService;
import com.example.wellwater.decision.registry.RuleSignal;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DecisionInputNormalizationService {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[-+]?\\d*\\.?\\d+");
    private static final Pattern THRESHOLD_PATTERN = Pattern.compile("^(.+?)(>=|<=|>|<)\\s*([-+]?\\d*\\.?\\d+)\\s*([^\\s]+)$");

    private final DecisionRegistryService registryService;

    public DecisionInputNormalizationService(DecisionRegistryService registryService) {
        this.registryService = registryService;
    }

    public DecisionNormalizedInput normalize(DecisionInput input) {
        String analyte = input.normalizedAnalyte();
        String symptom = resolveSymptomKey(input);
        String trigger = resolveTriggerKey(input);
        String state = input.normalizedState();
        Optional<RuleSignal> contaminantSignal = registryService.findContaminant(analyte);

        QualifierType qualifierType = resolveQualifier(input);
        Double numericResult = parseNumber(input.normalizedResultValue());
        Double qualifierNumeric = resolveQualifierNumeric(input, qualifierType);
        String canonicalUnit = resolveCanonicalUnit(contaminantSignal, input.normalizedUnit());
        Double canonicalNumeric = resolveCanonicalNumericValue(contaminantSignal, numericResult, input.normalizedUnit(), canonicalUnit);
        boolean unitSupported = resolveUnitSupport(contaminantSignal, input.normalizedUnit(), input.entryMode());
        boolean unitConverted = canonicalNumeric != null
                && numericResult != null
                && !normalizeUnitToken(input.normalizedUnit()).equals(normalizeUnitToken(canonicalUnit));
        ThresholdAssessment thresholdAssessment = resolveThresholdAssessment(contaminantSignal, canonicalNumeric, canonicalUnit);
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
                canonicalUnit,
                canonicalNumeric,
                unitSupported,
                unitConverted,
                thresholdAssessment.triggered(),
                thresholdAssessment.summary(),
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

    private String resolveSymptomKey(DecisionInput input) {
        if (!input.normalizedSymptom().isBlank()) {
            return input.normalizedSymptom();
        }
        if (!input.normalizedStainType().isBlank()) {
            return switch (input.normalizedStainType()) {
                case "orange" -> "orange-stains";
                case "black" -> "black-stains";
                case "blue-green" -> "blue-green-stains";
                default -> "";
            };
        }
        if (!input.normalizedTasteType().isBlank()) {
            if (input.normalizedTasteType().equals("metallic")) {
                return "metallic-taste";
            }
        }
        if (!input.normalizedSmellType().isBlank()) {
            if (input.normalizedSmellType().equals("rotten-egg")) {
                return "rotten-egg-smell";
            }
        }
        return "";
    }

    private String resolveTriggerKey(DecisionInput input) {
        if (!input.normalizedTrigger().isBlank()) {
            return input.normalizedTrigger();
        }
        return switch (input.normalizedChangeTiming()) {
            case "after-rain" -> "after-heavy-rain";
            case "after-repair" -> "after-repair";
            default -> "";
        };
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

    private boolean resolveUnitSupport(Optional<RuleSignal> contaminantSignal, String unit, EntryMode entryMode) {
        if (entryMode != EntryMode.RESULT_FIRST) {
            return true;
        }
        if (contaminantSignal.isEmpty()) {
            return false;
        }
        List<String> whitelist = contaminantSignal.get().unitWhitelist();
        if (whitelist.isEmpty()) {
            return true;
        }
        String normalizedUnit = normalizeUnitToken(unit);
        if (normalizedUnit.isBlank() || normalizedUnit.equals("unknown")) {
            return false;
        }
        if (whitelist.stream().map(this::normalizeUnitToken).anyMatch(allowed -> allowed.equals(normalizedUnit))) {
            return true;
        }
        String canonicalUnit = resolveCanonicalUnit(contaminantSignal, unit);
        return convertUnit(1.0d, normalizedUnit, normalizeUnitToken(canonicalUnit)) != null;
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
                    resolveSymptomKey(input),
                    input.normalizedSampleSource(),
                    input.normalizedLabCertified(),
                    input.normalizedState()
            );
            case TRIGGER_FIRST -> List.of(
                    resolveTriggerKey(input),
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
        int baseScore = Math.round((present * 100.0f) / required.size());
        int optionalCount = countOptionalContext(input);
        int bonus = Math.min(10, optionalCount * 2);
        return Math.min(100, baseScore + bonus);
    }

    private int countOptionalContext(DecisionInput input) {
        int count = 0;
        for (String field : List.of(
                input.normalizedUseScope(),
                input.normalizedExistingTreatment(),
                input.normalizedLabName(),
                input.normalizedHouseholdSize(),
                input.normalizedSmellType(),
                input.normalizedStainType(),
                input.normalizedTasteType(),
                input.normalizedLocationScope(),
                input.normalizedChangeTiming()
        )) {
            if (!field.isBlank()) {
                count++;
            }
        }
        return count;
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

    private String resolveCanonicalUnit(Optional<RuleSignal> contaminantSignal, String unit) {
        if (contaminantSignal.isPresent() && !contaminantSignal.get().canonicalUnit().isBlank()) {
            return contaminantSignal.get().canonicalUnit();
        }
        if (unit == null || unit.isBlank()) {
            return "";
        }
        return unit;
    }

    private Double resolveCanonicalNumericValue(Optional<RuleSignal> contaminantSignal, Double numericResult, String submittedUnit, String canonicalUnit) {
        if (numericResult == null || contaminantSignal.isEmpty()) {
            return null;
        }
        String fromUnit = normalizeUnitToken(submittedUnit);
        String toUnit = normalizeUnitToken(canonicalUnit);
        if (fromUnit.isBlank() || toUnit.isBlank() || fromUnit.equals("unknown") || toUnit.equals("unknown")) {
            return null;
        }
        return convertUnit(numericResult, fromUnit, toUnit);
    }

    private Double convertUnit(Double value, String fromUnit, String toUnit) {
        if (value == null || fromUnit == null || toUnit == null) {
            return null;
        }
        String from = normalizeUnitToken(fromUnit);
        String to = normalizeUnitToken(toUnit);
        if (from.isBlank() || to.isBlank()) {
            return null;
        }
        if (from.equals(to)) {
            return value;
        }
        if (sameScalePair(from, to, "mg/l", "ppm")) {
            return value;
        }
        if (sameScalePair(from, to, "ug/l", "ppb")) {
            return value;
        }
        if (sameScalePair(from, to, "ng/l", "ppt")) {
            return value;
        }
        if (sameScalePair(from, to, "su", "ph")) {
            return value;
        }
        if (sameScalePair(from, to, "pci/l", "pci/l")) {
            return value;
        }
        if (from.equals("grains/gal") && to.equals("mg/l")) {
            return value * 17.1d;
        }
        if (from.equals("mg/l") && to.equals("grains/gal")) {
            return value / 17.1d;
        }
        return null;
    }

    private boolean sameScalePair(String from, String to, String left, String right) {
        return (from.equals(left) && to.equals(right)) || (from.equals(right) && to.equals(left));
    }

    private String normalizeUnitToken(String unit) {
        if (unit == null || unit.isBlank()) {
            return "";
        }
        String normalized = unit.trim()
                .toLowerCase(Locale.ROOT)
                .replace("μ", "u")
                .replace("µ", "u")
                .replace(" ", "");
        return switch (normalized) {
            case "mg/l", "ppm" -> normalized;
            case "ug/l", "ppb" -> normalized;
            case "ng/l", "ppt" -> normalized;
            case "pci/l" -> "pci/l";
            case "grains/gal", "gpg" -> "grains/gal";
            case "su", "ph" -> normalized;
            case "cfu/100ml" -> "cfu/100ml";
            case "mpn/100ml" -> "mpn/100ml";
            case "presence/absence" -> "presence/absence";
            case "unknown" -> "unknown";
            default -> normalized;
        };
    }

    private ThresholdAssessment resolveThresholdAssessment(Optional<RuleSignal> contaminantSignal, Double canonicalNumeric, String canonicalUnit) {
        if (contaminantSignal.isEmpty() || canonicalNumeric == null || canonicalUnit == null || canonicalUnit.isBlank()) {
            return ThresholdAssessment.none();
        }
        String canonicalToken = normalizeUnitToken(canonicalUnit);
        List<String> triggeredSummaries = new java.util.ArrayList<>();
        List<String> matchedSummaries = new java.util.ArrayList<>();
        for (String ref : contaminantSignal.get().thresholdRefs()) {
            Matcher matcher = THRESHOLD_PATTERN.matcher(ref.trim());
            if (!matcher.matches()) {
                continue;
            }
            String label = matcher.group(1).trim();
            String operator = matcher.group(2);
            Double referenceValue = parseNumber(matcher.group(3));
            String referenceUnit = normalizeUnitToken(matcher.group(4));
            if (referenceValue == null || !referenceUnit.equals(canonicalToken)) {
                continue;
            }
            boolean triggered = compare(canonicalNumeric, operator, referenceValue);
            String summary = formatThresholdSummary(label, operator, canonicalNumeric, referenceValue, canonicalUnit, triggered);
            matchedSummaries.add(summary);
            if (triggered) {
                triggeredSummaries.add(summary);
            }
        }
        if (!triggeredSummaries.isEmpty()) {
            return new ThresholdAssessment(true, String.join("; ", triggeredSummaries));
        }
        if (!matchedSummaries.isEmpty()) {
            return new ThresholdAssessment(false, String.join("; ", matchedSummaries));
        }
        return ThresholdAssessment.none();
    }

    private boolean compare(Double value, String operator, Double referenceValue) {
        return switch (operator) {
            case ">=" -> value >= referenceValue;
            case ">" -> value > referenceValue;
            case "<=" -> value <= referenceValue;
            case "<" -> value < referenceValue;
            default -> false;
        };
    }

    private String formatThresholdSummary(String label, String operator, Double value, Double referenceValue, String unit, boolean triggered) {
        String relation = switch (operator) {
            case ">=", ">" -> triggered ? "meets or exceeds" : "is below";
            case "<=", "<" -> triggered ? "is below" : "is above";
            default -> "is near";
        };
        return "%s: %.3f %s %s %.3f %s".formatted(
                label,
                value,
                unit,
                relation,
                referenceValue,
                unit
        );
    }

    private record ThresholdAssessment(
            boolean triggered,
            String summary
    ) {
        private static ThresholdAssessment none() {
            return new ThresholdAssessment(false, "");
        }
    }
}
