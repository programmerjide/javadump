package io.github.programmerjide.javadump;

import io.github.programmerjide.javadump.config.DumperConfig;

/**
 * Builder for creating configured Dumper instances.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class DumperBuilder {

    private int maxDepth = 15;
    private int maxItems = 100;
    private boolean colorEnabled = true;

    public DumperBuilder withMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    public DumperBuilder withMaxItems(int maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    public DumperBuilder withoutColor() {
        this.colorEnabled = false;
        return this;
    }

    public Dumper build() {
        DumperConfig config = new DumperConfig(maxDepth, maxItems, colorEnabled);
        return new Dumper(config);
    }
}
