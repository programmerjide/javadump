package io.github.programmerjide.javadump.config;

/**
 * Configuration for Dumper instances.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class DumperConfig {

    private final int maxDepth;
    private final int maxItems;
    private final boolean colorEnabled;

    public DumperConfig(int maxDepth, int maxItems, boolean colorEnabled) {
        this.maxDepth = maxDepth;
        this.maxItems = maxItems;
        this.colorEnabled = colorEnabled;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public boolean isColorEnabled() {
        return colorEnabled;
    }
}
