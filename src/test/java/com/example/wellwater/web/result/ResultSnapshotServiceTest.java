package com.example.wellwater.web.result;

import com.example.wellwater.decision.model.ActionMode;
import com.example.wellwater.decision.model.Branch;
import com.example.wellwater.decision.model.Confidence;
import com.example.wellwater.decision.model.DecisionResult;
import com.example.wellwater.decision.model.EntryMode;
import com.example.wellwater.decision.model.ProblemType;
import com.example.wellwater.decision.model.Scope;
import com.example.wellwater.decision.model.Tier;
import com.example.wellwater.decision.model.Urgency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultSnapshotServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void createPersistsAndFindReadsBackSnapshot() {
        ResultSnapshotService service = new ResultSnapshotService(
                tempDir.toString(),
                30
        );

        ResultSnapshot created = service.create("123e4567-e89b-12d3-a456-426614174000", "nitrate", sampleResult());
        ResultSnapshot loaded = service.find(created.id()).orElseThrow();

        assertEquals(created.id(), loaded.id());
        assertEquals("nitrate", loaded.sourceSlug());
        assertEquals("Compare options", loaded.result().consumerPriorityLabel());
        assertTrue(loaded.expiresAt().compareTo(loaded.createdAt()) > 0);
    }

    private DecisionResult sampleResult() {
        return new DecisionResult(
                EntryMode.RESULT_FIRST,
                Tier.A,
                Confidence.HIGH,
                Branch.GREEN,
                Urgency.ROUTINE,
                Scope.DRINKING_ONLY,
                ProblemType.CHEMICAL_HEALTH,
                ActionMode.COMPARE_TREATMENT,
                "Sample verdict",
                "Sample verdict sentence.",
                List.of("Reason one"),
                List.of("Fresh certified sample."),
                List.of("Action today"),
                List.of("Action this week"),
                List.of("Action later"),
                List.of("Recommended test"),
                List.of("Compare gate"),
                List.of(),
                "Fresh",
                92,
                "$$",
                "$500-$1,500",
                "Annual filter change",
                "https://example.com/state",
                "https://example.com/lab",
                "v1",
                "source-v1",
                "Below threshold",
                List.of("Assumption"),
                List.of("Source"),
                "Educational only.",
                true,
                List.of()
        );
    }
}
