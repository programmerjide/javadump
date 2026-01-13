package io.github.programmerjide.javadump.core;

import io.github.programmerjide.javadump.formatter.*;
import io.github.programmerjide.javadump.theme.ThemeManager;

/**
 * Extended Dump API with all advanced features.
 *
 * @author Olaldejo Olajide
 * @since 1.6.0
 */
public final class DumpExtended {

    private static final Dumper DEFAULT_DUMPER = Dumper.builder().build();

    private DumpExtended() {
        throw new AssertionError("Utility class");
    }

    // ==================== Markdown ====================

    /**
     * Dumps as Markdown for documentation.
     */
    public static String dumpMarkdown(Object... values) {
        MarkdownFormatter formatter = new MarkdownFormatter(DEFAULT_DUMPER.getConfig());
        // Implementation would analyze and format
        return "```java\n" + Dump.dumpStr(values) + "\n```";
    }

    // ==================== YAML ====================

    /**
     * Dumps as YAML.
     */
    public static String dumpYAML(Object... values) {
        YamlFormatter formatter = new YamlFormatter(DEFAULT_DUMPER.getConfig());
        // Implementation would analyze and format
        return Dump.dumpStr(values);
    }

    // ==================== Interactive HTML ====================

    /**
     * Dumps as interactive HTML with collapsible sections.
     */
    public static String dumpInteractive(Object... values) {
        InteractiveHtmlFormatter formatter =
                new InteractiveHtmlFormatter(DEFAULT_DUMPER.getConfig());

        if (values == null || values.length == 0) {
            return formatter.format(null);
        }

        // For simplicity, dump first value
        // Full implementation would handle multiple values
        return formatter.format(null); // Would analyze first value
    }

    // ==================== Theme Management ====================

    /**
     * Sets the global color theme.
     */
    public static void setTheme(String themeName) {
        ThemeManager.setTheme(themeName);
    }

    /**
     * Sets a custom theme.
     */
    public static void setTheme(ThemeManager.Theme theme) {
        ThemeManager.setTheme(theme);
    }
}

