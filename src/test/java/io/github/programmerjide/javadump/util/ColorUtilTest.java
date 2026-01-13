package io.github.programmerjide.javadump.util;

import java.util.Objects;

/**
 * ANSI color utility for terminal output with theme support.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class ColorUtilTest {

    // ==================== ANSI Color Codes ====================

    public static final String RESET = "\033[0m";
    public static final String GRAY = "\033[90m";
    public static final String DIM = "\033[2m";
    public static final String YELLOW = "\033[33m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String BLUE = "\033[0;34m";
    public static final String CYAN = "\033[38;5;38m";
    public static final String ORANGE = "\033[38;5;208m";
    public static final String PURPLE = "\033[0;35m";
    public static final String LIME = "\033[1;38;5;113m";
    public static final String MAGENTA = "\033[38;5;201m";
    public static final String REF = "\033[38;5;247m";

    // Background colors
    public static final String RED_BG = "\033[48;2;34;16;16m";
    public static final String GREEN_BG = "\033[48;2;16;34;22m";
    public static final String GRAY_BG = "\033[48;2;40;40;40m";

    // Text styles
    public static final String BOLD = "\033[1m";
    public static final String ITALIC = "\033[3m";
    public static final String UNDERLINE = "\033[4m";
    public static final String BLINK = "\033[5m";
    public static final String REVERSE = "\033[7m";
    public static final String HIDDEN = "\033[8m";
    public static final String STRIKETHROUGH = "\033[9m";

    // ==================== Instance Fields ====================

    private final boolean enabled;
    private final ColorTheme theme;

    // ==================== Color Themes ====================

    public enum ColorTheme {
        DARK,
        LIGHT,
        HIGH_CONTRAST
    }

    // ==================== Constructors ====================

    public ColorUtilTest() {
        this(supportsColors());
    }

    public ColorUtilTest(boolean enabled) {
        this(enabled, ColorTheme.DARK);
    }

    public ColorUtilTest(boolean enabled, ColorTheme theme) {
        this.enabled = enabled;
        this.theme = Objects.requireNonNull(theme, "theme cannot be null");
    }

    // ==================== Getters ====================

    public boolean isEnabled() {
        return enabled;
    }

    public ColorTheme getTheme() {
        return theme;
    }

    // ==================== Instance Color Methods ====================

    public String colorize(String text, String color) {
        if (!enabled || text == null || color == null) {
            return text != null ? text : "";
        }
        return color + text + RESET;
    }

    public String style(String text, String... styles) {
        if (!enabled || text == null || styles == null || styles.length == 0) {
            return text != null ? text : "";
        }

        StringBuilder sb = new StringBuilder();
        for (String style : styles) {
            if (style != null) {
                sb.append(style);
            }
        }
        sb.append(text).append(RESET);
        return sb.toString();
    }

    // Basic colors
    public String gray(String text) { return colorize(text, GRAY); }
    public String yellow(String text) { return colorize(text, YELLOW); }
    public String red(String text) { return colorize(text, RED); }
    public String green(String text) { return colorize(text, GREEN); }
    public String blue(String text) { return colorize(text, BLUE); }
    public String purple(String text) { return colorize(text, PURPLE); }
    public String lime(String text) { return colorize(text, LIME); }
    public String cyan(String text) { return colorize(text, CYAN); }
    public String orange(String text) { return colorize(text, ORANGE); }
    public String magenta(String text) { return colorize(text, MAGENTA); }
    public String ref(String text) { return colorize(text, REF); }

    // Text styles
    public String bold(String text) { return colorize(text, BOLD); }
    public String dim(String text) { return colorize(text, DIM); }
    public String italic(String text) { return colorize(text, ITALIC); }
    public String underline(String text) { return colorize(text, UNDERLINE); }
    public String blink(String text) { return colorize(text, BLINK); }
    public String reverse(String text) { return colorize(text, REVERSE); }
    public String strikethrough(String text) { return colorize(text, STRIKETHROUGH); }

    // Background colors
    public String redBg(String text) { return colorize(text, RED_BG); }
    public String greenBg(String text) { return colorize(text, GREEN_BG); }
    public String grayBg(String text) { return colorize(text, GRAY_BG); }

    // Semantic methods
    public String success(String text) {
        return lime("✓") + " " + gray(text);
    }

    public String error(String text) {
        return red("✗") + " " + gray(text);
    }

    public String warning(String text) {
        return yellow("⚠") + " " + gray(text);
    }

    public String info(String text) {
        return cyan("ℹ") + " " + gray(text);
    }

    // ==================== Formatting Methods ====================

    public String formatType(String typeName) {
        if (StringUtil.isEmpty(typeName)) {
            return orange("#null");
        }
        return orange("#" + typeName);
    }

    public String formatType(Class<?> clazz) {
        if (clazz == null) {
            return orange("#null");
        }
        String simpleName = clazz.getSimpleName();
        if (simpleName.isEmpty()) {
            simpleName = clazz.getName();
        }
        return orange("#" + simpleName);
    }

    public String formatField(String fieldName, boolean isPublic) {
        if (StringUtil.isEmpty(fieldName)) {
            return "";
        }
        String marker = isPublic ? "+" : "-";
        return cyan(marker) + gray(fieldName);
    }

    public String formatString(String value) {
        if (value == null) {
            return orange("null");
        }
        String escaped = StringUtil.escape(value);
        return green(StringUtil.quote(escaped));
    }

    public String formatNumber(Object value) {
        if (value == null) {
            return orange("null");
        }
        return orange(String.valueOf(value));
    }

    public String formatBoolean(boolean value) {
        return value ? green("true") : red("false");
    }

    public String formatNull() {
        return orange("null");
    }

    public String arrow() {
        return gray(" → ");
    }

    public String structural(String character) {
        return gray(character);
    }

    public String formatHeader(String filePath, int lineNumber) {
        String header = String.format("<#dump // %s:%d", filePath, lineNumber);
        return dim(header);
    }

    public String formatDiffAddition(String content) {
        return style(content, GREEN, GREEN_BG);
    }

    public String formatDiffRemoval(String content) {
        return style(content, RED, RED_BG);
    }

    public String formatRedacted() {
        return yellow("<redacted>");
    }

    public String formatCyclic() {
        return yellow("(cyclic)");
    }

    public String formatTruncated() {
        return gray("(truncated)");
    }

    // ==================== Static Methods (for backward compatibility) ====================

    public static String type(String text) {
        return ORANGE + text + RESET;
    }

    public static String string(String text) {
        return GREEN + text + RESET;
    }

    public static String number(String text) {
        return ORANGE + text + RESET;
    }

    public static String field(String text) {
        return CYAN + text + RESET;
    }

    public static String keyword(String text) {
        return YELLOW + text + RESET;
    }

    // ==================== Fluent API ====================

    public ColorUtilTest withoutColors() {
        return new ColorUtilTest(false, theme);
    }

    public ColorUtilTest withTheme(ColorTheme newTheme) {
        return new ColorUtilTest(enabled, newTheme);
    }

    // ==================== Terminal Detection ====================

    public static boolean supportsColors() {
        if (System.getenv("NO_COLOR") != null) {
            return false;
        }

        if (System.getenv("CI") != null) {
            return false;
        }

        if (System.getenv("COLORTERM") != null) {
            return true;
        }

        String term = System.getenv("TERM");
        if (term == null || term.equals("dumb")) {
            return false;
        }

        if (term.contains("color") || term.contains("xterm") ||
                term.contains("256") || term.equals("linux")) {
            return true;
        }

        return System.console() != null;
    }

    public static String stripColors(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\033\\[[;\\d]*m", "");
    }

    public static int lengthWithoutColors(String text) {
        if (text == null) {
            return 0;
        }
        return stripColors(text).length();
    }

    // ==================== Factory Methods ====================

    public static ColorUtilTest create() {
        return new ColorUtilTest();
    }

    public static ColorUtilTest enabled() {
        return new ColorUtilTest(true);
    }

    public static ColorUtilTest disabled() {
        return new ColorUtilTest(false);
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        return String.format("ColorUtilTest{enabled=%s, theme=%s}", enabled, theme);
    }
}