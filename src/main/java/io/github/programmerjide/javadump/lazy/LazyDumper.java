package io.github.programmerjide.javadump.lazy;

import io.github.programmerjide.javadump.core.Dump;

import java.util.function.Supplier;

/**
 * Provides lazy evaluation for expensive dump operations.
 *
 * <p>Only evaluates and dumps when actually used.
 *
 * <p>Example:
 * <pre>{@code
 * LazyDump<User> lazyUser = LazyDumper.of(() -> expensiveUserQuery());
 *
 * // No evaluation yet
 * if (debugMode) {
 *     lazyUser.dump(); // Only evaluated if debug mode is on
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.2.0
 */
public class LazyDumper {

    /**
     * Creates a lazy dump wrapper.
     */
    public static <T> LazyDump<T> of(Supplier<T> supplier) {
        return new LazyDump<>(supplier);
    }

    /**
     * Creates a lazy dump with label.
     */
    public static <T> LazyDump<T> of(String label, Supplier<T> supplier) {
        return new LazyDump<>(label, supplier);
    }

    /**
     * Lazy dump wrapper.
     */
    public static class LazyDump<T> {
        private final String label;
        private final Supplier<T> supplier;
        private T value;
        private boolean evaluated;

        LazyDump(Supplier<T> supplier) {
            this(null, supplier);
        }

        LazyDump(String label, Supplier<T> supplier) {
            this.label = label;
            this.supplier = supplier;
        }

        /**
         * Forces evaluation and dumps.
         */
        public void dump() {
            evaluate();
            if (label != null) {
                System.out.println("=== " + label + " ===");
            }
            Dump.dump(value);
        }

        /**
         * Forces evaluation and dumps as JSON.
         */
        public void dumpJSON() {
            evaluate();
            if (label != null) {
                System.out.println("=== " + label + " (JSON) ===");
            }
            Dump.dumpJSON(value);
        }

        /**
         * Forces evaluation and returns dump string.
         */
        public String dumpStr() {
            evaluate();
            return Dump.dumpStr(value);
        }

        /**
         * Gets value (forces evaluation).
         */
        public T get() {
            evaluate();
            return value;
        }

        /**
         * Checks if already evaluated.
         */
        public boolean isEvaluated() {
            return evaluated;
        }

        private void evaluate() {
            if (!evaluated) {
                value = supplier.get();
                evaluated = true;
            }
        }
    }
}
