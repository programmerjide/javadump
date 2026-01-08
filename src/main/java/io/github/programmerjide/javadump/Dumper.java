package io.github.programmerjide.javadump;

import io.github.programmerjide.javadump.config.DumperConfig;

/**
 * Configurable dumper instance.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class Dumper {

    private final DumperConfig config;

    private Dumper(DumperConfig config) {
        this.config = config;
    }

    public static DumperBuilder builder() {
        return new DumperBuilder();
    }

    public void dump(Object... values) {
        // TODO: Implement dump logic
        for (Object value : values) {
            System.out.println("TODO: Implement dump() - " + value);
        }
    }

    public void dd(Object... values) {
        dump(values);
        System.exit(0);
    }

    public String dumpStr(Object... values) {
        // TODO: Implement string dump
        return "TODO: Implement dumpStr()";
    }
}
