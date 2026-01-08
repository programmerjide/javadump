package io.github.programmerjide.javadump;

/**
 * Main API for JavaDump - provides static convenience methods for debugging.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class Dump {

    private static final Dumper DEFAULT_DUMPER = Dumper.builder().build();

    /**
     * Dumps the given values to stdout with colorized output.
     *
     * @param values the values to dump
     */
    public static void dump(Object... values) {
        DEFAULT_DUMPER.dump(values);
    }

    /**
     * Dumps the given values and exits the program.
     *
     * @param values the values to dump
     */
    public static void dd(Object... values) {
        DEFAULT_DUMPER.dd(values);
    }

    /**
     * Returns a string representation of the dumped values.
     *
     * @param values the values to dump
     * @return string representation
     */
    public static String dumpStr(Object... values) {
        return DEFAULT_DUMPER.dumpStr(values);
    }

    // TODO: Implement additional methods
    // - dumpHTML()
    // - dumpJSON()
    // - diff()
    // - diffStr()
    // - diffHTML()
}
