package io.github.programmerjide.javadump.core;

/**
 * Main API for JavaDump - provides static convenience methods for debugging.
 *
 * <p>This class provides static methods that use a default dumper configuration.
 * All methods delegate to a singleton {@link Dumper} instance.
 *
 * <p>Example usage:
 * <pre>{@code
 * import static io.github.programmerjide.javadump.core.Dump.*;
 *
 * dump("Hello", 42, List.of(1, 2, 3));
 * dumpJSON(user);
 * diff(oldUser, newUser);
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public final class Dump {

    private static final Dumper DEFAULT_DUMPER = Dumper.builder().build();

    private Dump() {
        throw new AssertionError("Dump class should not be instantiated");
    }

    // ==================== Console Output ====================

    /**
     * Dumps the given values to stdout with colorized output.
     *
     * <p>Example:
     * <pre>{@code
     * dump("Hello", 42, List.of(1, 2, 3));
     * }</pre>
     *
     * @param values the values to dump
     */
    public static void dump(Object... values) {
        DEFAULT_DUMPER.dump(values);
    }

    /**
     * Dumps the given values and exits the program with status code 0.
     *
     * <p>Useful for quick debugging - dump and terminate execution.
     *
     * <p>Example:
     * <pre>{@code
     * dd("Debug and die", errorObject);
     * // Program exits here
     * }</pre>
     *
     * @param values the values to dump before exiting
     */
    public static void dd(Object... values) {
        DEFAULT_DUMPER.dd(values);
    }

    /**
     * Returns a string representation of the dumped values.
     *
     * <p>Useful for logging or testing.
     *
     * <p>Example:
     * <pre>{@code
     * String result = dumpStr("Hello", 42);
     * logger.info("Dump: {}", result);
     * }</pre>
     *
     * @param values the values to dump
     * @return formatted string representation
     */
    public static String dumpStr(Object... values) {
        return DEFAULT_DUMPER.dumpStr(values);
    }

    // ==================== JSON Output ====================

    /**
     * Dumps the given values as pretty-printed JSON to stdout.
     *
     * <p>Example:
     * <pre>{@code
     * Map<String, Object> data = Map.of("name", "Alice", "age", 30);
     * dumpJSON(data);
     * // Output:
     * // {
     * //   "name": "Alice",
     * //   "age": 30
     * // }
     * }</pre>
     *
     * @param values the values to dump as JSON
     */
    public static void dumpJSON(Object... values) {
        DEFAULT_DUMPER.dumpJSON(values);
    }

    /**
     * Returns a JSON string representation of the dumped values.
     *
     * <p>Example:
     * <pre>{@code
     * String json = dumpJSONStr(user);
     * // Use in tests, logging, API responses, etc.
     * }</pre>
     *
     * @param values the values to dump as JSON
     * @return JSON string representation
     */
    public static String dumpJSONStr(Object... values) {
        return DEFAULT_DUMPER.dumpJSONStr(values);
    }

    // ==================== HTML Output ====================

    /**
     * Returns an HTML representation of the dumped values.
     *
     * <p>The HTML includes embedded CSS for syntax highlighting and
     * can be saved to a file or embedded in a web page.
     *
     * <p>Example:
     * <pre>{@code
     * String html = dumpHTML(user);
     * Files.writeString(Path.of("dump.html"), html);
     * }</pre>
     *
     * @param values the values to dump as HTML
     * @return HTML string with embedded CSS
     */
    public static String dumpHTML(Object... values) {
        return DEFAULT_DUMPER.dumpHTML(values);
    }

    // ==================== Diff Output ====================

    /**
     * Compares two objects and prints the differences to stdout.
     *
     * <p>Shows added, removed, and changed fields with color coding:
     * <ul>
     *   <li>Red (-) for removals</li>
     *   <li>Green (+) for additions</li>
     * </ul>
     *
     * <p>Example:
     * <pre>{@code
     * User before = new User("Alice", 25);
     * User after = new User("Alice", 26);
     * diff(before, after);
     * // Output:
     * // - age: 25
     * // + age: 26
     * }</pre>
     *
     * @param before the original object
     * @param after the modified object
     */
    public static void diff(Object before, Object after) {
        DEFAULT_DUMPER.diff(before, after);
    }

    /**
     * Compares two objects and returns the differences as a string.
     *
     * <p>Example:
     * <pre>{@code
     * String differences = diffStr(oldConfig, newConfig);
     * logger.info("Configuration changed:\n{}", differences);
     * }</pre>
     *
     * @param before the original object
     * @param after the modified object
     * @return string representation of differences
     */
    public static String diffStr(Object before, Object after) {
        return DEFAULT_DUMPER.diffStr(before, after);
    }

    /**
     * Compares two objects and returns the differences as HTML.
     *
     * <p>The HTML includes embedded CSS with color-coded additions
     * and removals, perfect for web-based debugging or documentation.
     *
     * <p>Example:
     * <pre>{@code
     * String html = diffHTML(oldUser, newUser);
     * Files.writeString(Path.of("user-changes.html"), html);
     * }</pre>
     *
     * @param before the original object
     * @param after the modified object
     * @return HTML string with embedded CSS
     */
    public static String diffHTML(Object before, Object after) {
        return DEFAULT_DUMPER.diffHTML(before, after);
    }
}