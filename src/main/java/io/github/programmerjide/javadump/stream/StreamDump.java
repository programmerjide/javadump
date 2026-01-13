package io.github.programmerjide.javadump.stream;

import io.github.programmerjide.javadump.core.Dump;
import io.github.programmerjide.javadump.core.Dumper;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Stream utilities for JavaDump integration.
 *
 * <p>Provides convenient methods for debugging streams with dump operations.
 *
 * <p>Example:
 * <pre>{@code
 * import static io.github.programmerjide.javadump.stream.StreamDump.*;
 *
 * users.stream()
 *     .peek(dumpPeek())              // Dump each element
 *     .filter(user -> user.isActive())
 *     .peek(dumpPeek("Active users")) // Dump with label
 *     .collect(Collectors.toList());
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
public class StreamDump {

    private StreamDump() {
        throw new AssertionError("Utility class");
    }

    // ==================== Peek Operations ====================

    /**
     * Creates a Consumer that dumps each element.
     *
     * <p>Usage in stream:
     * <pre>{@code
     * users.stream()
     *     .peek(dumpPeek())
     *     .filter(...)
     *     .collect(...);
     * }</pre>
     */
    public static <T> Consumer<T> dumpPeek() {
        return Dump::dump;
    }

    /**
     * Creates a Consumer that dumps each element with a label.
     *
     * <p>Usage in stream:
     * <pre>{@code
     * users.stream()
     *     .peek(dumpPeek("Processing user"))
     *     .map(...)
     *     .collect(...);
     * }</pre>
     */
    public static <T> Consumer<T> dumpPeek(String label) {
        return value -> {
            System.out.println("=== " + label + " ===");
            Dump.dump(value);
        };
    }

    /**
     * Creates a Consumer that dumps using a custom dumper.
     */
    public static <T> Consumer<T> dumpPeek(Dumper dumper) {
        return dumper::dump;
    }

    /**
     * Creates a Consumer that dumps with label using custom dumper.
     */
    public static <T> Consumer<T> dumpPeek(String label, Dumper dumper) {
        return value -> {
            System.out.println("=== " + label + " ===");
            dumper.dump(value);
        };
    }

    // ==================== JSON Peek Operations ====================

    /**
     * Creates a Consumer that dumps each element as JSON.
     */
    public static <T> Consumer<T> dumpPeekJSON() {
        return Dump::dumpJSON;
    }

    /**
     * Creates a Consumer that dumps each element as JSON with label.
     */
    public static <T> Consumer<T> dumpPeekJSON(String label) {
        return value -> {
            System.out.println("=== " + label + " (JSON) ===");
            Dump.dumpJSON(value);
        };
    }

    // ==================== Conditional Dump ====================

    /**
     * Creates a Consumer that dumps only if predicate matches.
     *
     * <p>Usage:
     * <pre>{@code
     * users.stream()
     *     .peek(dumpIf(user -> user.getAge() > 30))
     *     .collect(...);
     * }</pre>
     */
    public static <T> Consumer<T> dumpIf(Predicate<T> predicate) {
        return value -> {
            if (predicate.test(value)) {
                Dump.dump(value);
            }
        };
    }

    /**
     * Creates a Consumer that dumps only if predicate matches, with label.
     */
    public static <T> Consumer<T> dumpIf(String label, Predicate<T> predicate) {
        return value -> {
            if (predicate.test(value)) {
                System.out.println("=== " + label + " ===");
                Dump.dump(value);
            }
        };
    }

    // ==================== Transform and Dump ====================

    /**
     * Creates a Consumer that transforms value before dumping.
     *
     * <p>Usage:
     * <pre>{@code
     * users.stream()
     *     .peek(dumpMap(user -> user.getName()))
     *     .collect(...);
     * }</pre>
     */
    public static <T, R> Consumer<T> dumpMap(Function<T, R> mapper) {
        return value -> Dump.dump(mapper.apply(value));
    }

    /**
     * Creates a Consumer that transforms value before dumping, with label.
     */
    public static <T, R> Consumer<T> dumpMap(String label, Function<T, R> mapper) {
        return value -> {
            System.out.println("=== " + label + " ===");
            Dump.dump(mapper.apply(value));
        };
    }

    // ==================== Count and Dump ====================

    /**
     * Creates a stateful Consumer that dumps with index.
     *
     * <p>Usage:
     * <pre>{@code
     * users.stream()
     *     .peek(dumpWithIndex())
     *     .collect(...);
     * // Output:
     * // [0] User{...}
     * // [1] User{...}
     * }</pre>
     */
    public static <T> Consumer<T> dumpWithIndex() {
        return new Consumer<T>() {
            private int index = 0;

            @Override
            public void accept(T value) {
                System.out.println("[" + index++ + "]");
                Dump.dump(value);
            }
        };
    }

    /**
     * Creates a stateful Consumer that dumps with index and label.
     */
    public static <T> Consumer<T> dumpWithIndex(String label) {
        return new Consumer<T>() {
            private int index = 0;

            @Override
            public void accept(T value) {
                System.out.println("=== " + label + " [" + index++ + "] ===");
                Dump.dump(value);
            }
        };
    }

    // ==================== Batch Dump ====================

    /**
     * Creates a stateful Consumer that dumps every N elements.
     *
     * <p>Usage:
     * <pre>{@code
     * users.stream()
     *     .peek(dumpEvery(10)) // Dump every 10th element
     *     .collect(...);
     * }</pre>
     */
    public static <T> Consumer<T> dumpEvery(int n) {
        return new Consumer<T>() {
            private int count = 0;

            @Override
            public void accept(T value) {
                count++;
                if (count % n == 0) {
                    System.out.println("=== Element " + count + " ===");
                    Dump.dump(value);
                }
            }
        };
    }

    // ==================== Summary Operations ====================

    /**
     * Creates a Consumer that dumps a summary at intervals.
     */
    public static <T> Consumer<T> dumpSummary(int intervalSize) {
        return new Consumer<T>() {
            private int count = 0;
            private long startTime = System.currentTimeMillis();

            @Override
            public void accept(T value) {
                count++;
                if (count % intervalSize == 0) {
                    long elapsed = System.currentTimeMillis() - startTime;
                    System.out.printf("=== Processed %d items in %dms ===\n",
                            count, elapsed);
                }
            }
        };
    }
}