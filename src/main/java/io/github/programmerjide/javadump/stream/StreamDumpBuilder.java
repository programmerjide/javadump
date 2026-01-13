// ============================================================================
// StreamDumpBuilder.java - Fluent builder for complex stream dumps
// ============================================================================

package io.github.programmerjide.javadump.stream;

import io.github.programmerjide.javadump.core.Dumper;
import io.github.programmerjide.javadump.core.Dump;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Fluent builder for creating complex stream dump operations.
 *
 * <p>Example:
 * <pre>{@code
 * Consumer<User> dumper = StreamDumpBuilder.<User>create()
 *     .withLabel("Active users")
 *     .when(user -> user.isActive())
 *     .withIndex()
 *     .asJSON()
 *     .build();
 *
 * users.stream()
 *     .peek(dumper)
 *     .collect(...);
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
public class StreamDumpBuilder<T> {

    private String label;
    private Predicate<T> condition;
    private boolean withIndex;
    private boolean asJSON;
    private Function<T, ?> mapper;
    private Dumper dumper;
    private int everyN = 1;

    private StreamDumpBuilder() {
    }

    public static <T> StreamDumpBuilder<T> create() {
        return new StreamDumpBuilder<>();
    }

    public StreamDumpBuilder<T> withLabel(String label) {
        this.label = label;
        return this;
    }

    public StreamDumpBuilder<T> when(Predicate<T> condition) {
        this.condition = condition;
        return this;
    }

    public StreamDumpBuilder<T> withIndex() {
        this.withIndex = true;
        return this;
    }

    public StreamDumpBuilder<T> asJSON() {
        this.asJSON = true;
        return this;
    }

    public <R> StreamDumpBuilder<T> map(Function<T, R> mapper) {
        this.mapper = mapper;
        return this;
    }

    public StreamDumpBuilder<T> using(Dumper dumper) {
        this.dumper = dumper;
        return this;
    }

    public StreamDumpBuilder<T> every(int n) {
        this.everyN = n;
        return this;
    }

    public Consumer<T> build() {
        return new Consumer<T>() {
            private int index = 0;

            @Override
            public void accept(T value) {
                index++;

                // Check every N
                if (index % everyN != 0) {
                    return;
                }

                // Check condition
                if (condition != null && !condition.test(value)) {
                    return;
                }

                // Build output
                StringBuilder sb = new StringBuilder();
                if (label != null) {
                    sb.append("=== ").append(label);
                    if (withIndex) {
                        sb.append(" [").append(index).append("]");
                    }
                    sb.append(" ===");
                    System.out.println(sb.toString());
                }

                // Determine what to dump
                Object toDump = mapper != null ? mapper.apply(value) : value;

                // Dump
                if (dumper != null) {
                    if (asJSON) {
                        dumper.dumpJSON(toDump);
                    } else {
                        dumper.dump(toDump);
                    }
                } else {
                    if (asJSON) {
                        Dump.dumpJSON(toDump);
                    } else {
                        Dump.dump(toDump);
                    }
                }
            }
        };
    }
}