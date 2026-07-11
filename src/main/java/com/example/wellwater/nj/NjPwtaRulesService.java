package com.example.wellwater.nj;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class NjPwtaRulesService {

    public static final String PWTA_URL = "https://dep.nj.gov/privatewells/pwta/";
    public static final String CERTIFIED_LABS_URL = "https://dep.nj.gov/dsr/oqa/certified-laboratories/";
    public static final String TESTING_URL = "https://dep.nj.gov/privatewells/testing/";

    private static final Set<String> MERCURY_COUNTIES = Set.of(
            "atlantic", "burlington", "camden", "cape may", "cumberland", "gloucester", "monmouth", "ocean", "salem"
    );
    private static final Set<String> URANIUM_COUNTIES = Set.of(
            "bergen", "essex", "hudson", "hunterdon", "mercer", "middlesex", "morris", "passaic", "somerset", "sussex", "union", "warren"
    );

    public NjCoverage coverage(String transactionType, String waterSource) {
        if ("irrigation_only".equals(waterSource)) {
            return new NjCoverage(
                    "not_likely_covered",
                    "This use does not appear to trigger PWTA testing.",
                    "NJDEP says a well used only for non-drinking purposes, such as lawn watering, is not covered. Confirm the actual potable-water source and local requirements."
            );
        }
        if ("planning".equals(transactionType) || "unsure".equals(waterSource)) {
            return new NjCoverage(
                    "verify",
                    "PWTA coverage needs one more confirmation.",
                    "Confirm that the property receives drinking water from a private or qualifying small shared well and check any stricter local health requirements."
            );
        }
        if ("lease".equals(transactionType)) {
            return new NjCoverage(
                    "likely_covered",
                    "This lease appears to be covered by the NJ PWTA.",
                    "Covered landlords arrange the full test every five years and provide tenants with the most recent results."
            );
        }
        return new NjCoverage(
                "likely_covered",
                "This sale appears to be covered by the NJ PWTA.",
                "Testing is a condition of a covered sale, and buyer and seller must receive and review the results before title closing."
        );
    }

    public List<NjRequirement> requiredPanel(String county) {
        List<NjRequirement> panel = new ArrayList<>();
        panel.add(new NjRequirement("Total coliform", "E. coli is required when total coliform is present.", false));
        panel.add(new NjRequirement("Nitrate", "Required statewide.", false));
        panel.add(new NjRequirement("pH", "Must follow the certified immediate-analysis sampling path.", false));
        panel.add(new NjRequirement("Iron and manganese", "Required secondary water-quality parameters.", false));
        panel.add(new NjRequirement("Lead and arsenic", "Required statewide.", false));
        panel.add(new NjRequirement("Gross alpha particle activity", "Required statewide.", false));
        panel.add(new NjRequirement("PFOA, PFOS, and PFNA", "Required regulated PFAS panel.", false));
        panel.add(new NjRequirement("Volatile organic compounds", "NJDEP lists 29 required VOC analytes.", false));
        panel.add(new NjRequirement("Synthetic organic compounds", "NJDEP lists three required SOC analytes.", false));
        String normalizedCounty = normalizeCounty(county);
        if (MERCURY_COUNTIES.contains(normalizedCounty)) {
            panel.add(new NjRequirement("Mercury", "Required for " + displayCounty(county) + " County.", true));
        }
        if (URANIUM_COUNTIES.contains(normalizedCounty)) {
            panel.add(new NjRequirement("Uranium", "Required for " + displayCounty(county) + " County.", true));
        }
        return List.copyOf(panel);
    }

    public boolean requiresMercury(String county) {
        return MERCURY_COUNTIES.contains(normalizeCounty(county));
    }

    public boolean requiresUranium(String county) {
        return URANIUM_COUNTIES.contains(normalizeCounty(county));
    }

    public List<String> nextSteps(String transactionType) {
        if ("lease".equals(transactionType)) {
            return List.of(
                    "Confirm whether the existing full PWTA report is less than five years old.",
                    "Use one NJDEP-certified laboratory to coordinate certified sampling and electronic reporting when a new test is due.",
                    "Give the tenant the most recent complete results and retain the transaction record."
            );
        }
        if ("sale".equals(transactionType)) {
            return List.of(
                    "Put the PWTA test requirement and payment responsibility into the sale workflow.",
                    "Use one NJDEP-certified laboratory to coordinate certified sampling of untreated water and electronic reporting.",
                    "Make sure buyer and seller receive, review, and certify receipt of the results before closing."
            );
        }
        return List.of(
                "Confirm the drinking-water source and whether a sale or lease is active.",
                "Check the NJDEP PWTA page and any stricter local health requirements.",
                "Use a certified laboratory before collecting a compliance sample."
        );
    }

    private String normalizeCounty(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        return normalized.endsWith(" county") ? normalized.substring(0, normalized.length() - 7).trim() : normalized;
    }

    private String displayCounty(String value) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.toLowerCase(Locale.ROOT).endsWith(" county")) {
            return normalized.substring(0, normalized.length() - 7).trim();
        }
        return normalized;
    }
}
