package io.github.programmerjide.javadump.core;

import io.github.programmerjide.javadump.analyzer.DiffAnalyzer;
import io.github.programmerjide.javadump.config.DumperConfig;

import java.util.Objects;

/**
 * Assertion methods for tests using dump functionality.
 *
 * @author Olaldejo Olajide
 * @since 1.4.0
 */
public class DumpAssertions {

    /**
     * Asserts that two objects are equal, showing diff on failure.
     */
    public static void assertDumpEquals(Object expected, Object actual) {
        assertDumpEquals(null, expected, actual);
    }

    /**
     * Asserts that two objects are equal with message.
     */
    public static void assertDumpEquals(String message, Object expected, Object actual) {
        if (!Objects.deepEquals(expected, actual)) {
            String header = message != null ? message + "\n" : "";
            String diff = Dump.diffStr(expected, actual);

            throw new AssertionError(header + "Objects differ:\n" + diff);
        }
    }

    /**
     * Asserts that specific fields changed between objects.
     */
    public static void assertChanged(Object before, Object after, String... fields) {
        DiffAnalyzer analyzer = new DiffAnalyzer(DumperConfig.builder().build());
        DiffAnalyzer.DiffResult result = analyzer.analyze(before, after);

        for (String field : fields) {
            boolean changed = result.getChanges().stream()
                    .anyMatch(e -> field.equals(e.getField()));

            if (!changed) {
                throw new AssertionError("Expected field '" + field + "' to change, but it didn't.\n" +
                        "Before: " + Dump.dumpStr(before) + "\n" +
                        "After: " + Dump.dumpStr(after));
            }
        }
    }

    /**
     * Asserts that specific fields did NOT change.
     */
    public static void assertUnchanged(Object before, Object after, String... fields) {
        DiffAnalyzer analyzer = new DiffAnalyzer(DumperConfig.builder().build());
        DiffAnalyzer.DiffResult result = analyzer.analyze(before, after);

        for (String field : fields) {
            boolean changed = result.getChanges().stream()
                    .anyMatch(e -> field.equals(e.getField()));

            if (changed) {
                throw new AssertionError("Expected field '" + field + "' to remain unchanged.\n" +
                        Dump.diffStr(before, after));
            }
        }
    }
}
