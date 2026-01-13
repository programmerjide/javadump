//package io.github.programmerjide.javadump.util;
//
///**
// * ANSI color utility for terminal output.
// *
// * <p>Provides static methods for colorizing text output with ANSI codes.
// * All methods return plain text if colors would not be visible.
// *
// * @author Olaldejo Olajide
// * @since 1.0.0
// */
//public final class ColorUtil {
//
//    // ==================== ANSI Color Codes ====================
//
//    public static final String RESET = "\033[0m";
//    public static final String GRAY = "\033[90m";
//    public static final String DIM = "\033[2m";
//    public static final String YELLOW = "\033[33m";
//    public static final String RED = "\033[31m";
//    public static final String GREEN = "\033[32m";
//    public static final String BLUE = "\033[0;34m";
//    public static final String CYAN = "\033[38;5;38m";
//    public static final String ORANGE = "\033[38;5;208m";
//    public static final String PURPLE = "\033[0;35m";
//
//    private ColorUtil() {
//        throw new AssertionError("Utility class");
//    }
//
//    // ==================== Basic Color Methods ====================
//
//    /**
//     * Colors text as a type name (orange).
//     */
//    public static String type(String text) {
//        return ORANGE + text + RESET;
//    }
//
//    /**
//     * Colors text as a string value (green).
//     */
//    public static String string(String text) {
//        return GREEN + text + RESET;
//    }
//
//    /**
//     * Colors text as a number (orange).
//     */
//    public static String number(String text) {
//        return ORANGE + text + RESET;
//    }
//
//    /**
//     * Colors text as a field name (cyan).
//     */
//    public static String field(String text) {
//        return CYAN + text + RESET;
//    }
//
//    /**
//     * Colors text as a keyword (yellow).
//     */
//    public static String keyword(String text) {
//        return YELLOW + text + RESET;
//    }
//
//    /**
//     * Colors text as an error (red).
//     */
//    public static String error(String text) {
//        return RED + text + RESET;
//    }
//
//    /**
//     * Colors text as dim/faded (dim gray).
//     */
//    public static String dim(String text) {
//        return DIM + text + RESET;
//    }
//
//    /**
//     * Colors text in gray.
//     */
//    public static String gray(String text) {
//        return GRAY + text + RESET;
//    }
//
//    /**
//     * Colors text in green.
//     */
//    public static String green(String text) {
//        return GREEN + text + RESET;
//    }
//
//    /**
//     * Colors text in red.
//     */
//    public static String red(String text) {
//        return RED + text + RESET;
//    }
//
//    /**
//     * Colors text in yellow.
//     */
//    public static String yellow(String text) {
//        return YELLOW + text + RESET;
//    }
//
//    /**
//     * Colors text in cyan.
//     */
//    public static String cyan(String text) {
//        return CYAN + text + RESET;
//    }
//
//    /**
//     * Colors text in orange.
//     */
//    public static String orange(String text) {
//        return ORANGE + text + RESET;
//    }
//
//    // ==================== Formatting Helper Methods ====================
//
//    /**
//     * Colors structural characters (braces, brackets, etc.) in gray.
//     */
//    public static String structural(String text) {
//        return GRAY + text + RESET;
//    }
//
//    /**
//     * Returns a colored arrow for separating items.
//     */
//    public static String arrow() {
//        return GRAY + " → " + RESET;
//    }
//
//    /**
//     * Formats a type with class information.
//     * Example: formatType(String.class) returns "#String" in orange
//     */
//    public static String formatType(Class<?> clazz) {
//        if (clazz == null) {
//            return ORANGE + "#null" + RESET;
//        }
//        String simpleName = clazz.getSimpleName();
//        if (simpleName.isEmpty()) {
//            simpleName = clazz.getName();
//        }
//        return ORANGE + "#" + simpleName + RESET;
//    }
//
//    /**
//     * Formats a type name string.
//     */
//    public static String formatType(String typeName) {
//        if (typeName == null || typeName.isEmpty()) {
//            return ORANGE + "#null" + RESET;
//        }
//        return ORANGE + "#" + typeName + RESET;
//    }
//
//    /**
//     * Formats a header line (file:line) in dim style.
//     */
//    public static String formatHeader(String fileName, int lineNumber) {
//        return DIM + fileName + ":" + lineNumber + RESET;
//    }
//
//    /**
//     * Formats a truncation marker.
//     */
//    public static String formatTruncated() {
//        return DIM + "... (truncated)" + RESET;
//    }
//
//    /**
//     * Formats a cyclic/circular reference marker.
//     */
//    public static String formatCyclic() {
//        return YELLOW + "↻ (circular)" + RESET;
//    }
//
//    // ==================== Terminal Detection ====================
//
//    /**
//     * Checks if the terminal supports colors.
//     *
//     * <p>Checks various environment variables and system properties
//     * to determine if ANSI colors should be enabled.
//     */
//    public static boolean supportsColors() {
//        // Check NO_COLOR standard (https://no-color.org/)
//        if (System.getenv("NO_COLOR") != null) {
//            return false;
//        }
//
//        // Check if running in CI environment
//        if (System.getenv("CI") != null) {
//            return false;
//        }
//
//        // Check COLORTERM (modern terminals)
//        if (System.getenv("COLORTERM") != null) {
//            return true;
//        }
//
//        // Check TERM variable
//        String term = System.getenv("TERM");
//        if (term == null || term.equals("dumb")) {
//            return false;
//        }
//
//        if (term.contains("color") ||
//                term.contains("xterm") ||
//                term.contains("256") ||
//                term.equals("linux")) {
//            return true;
//        }
//
//        // Check if console is available
//        return System.console() != null;
//    }
//
//    /**
//     * Strips all ANSI color codes from a string.
//     */
//    public static String stripColors(String text) {
//        if (text == null) {
//            return null;
//        }
//        return text.replaceAll("\033\\[[;\\d]*m", "");
//    }
//
//    /**
//     * Gets the length of text without ANSI codes.
//     */
//    public static int lengthWithoutColors(String text) {
//        if (text == null) {
//            return 0;
//        }
//        return stripColors(text).length();
//    }
//
//    // ==================== Factory Methods ====================
//
//    /**
//     * Singleton instance for compatibility with non-static usage.
//     */
//    private static final ColorUtil INSTANCE = createInstance();
//
//    /**
//     * Creates a ColorUtil instance (for compatibility).
//     * This class uses static methods, so this returns a singleton instance.
//     */
//    public static ColorUtil create() {
//        return INSTANCE;
//    }
//
//    /**
//     * Creates a disabled ColorUtil (for compatibility).
//     * Since all methods are static, this returns the same instance.
//     */
//    public static ColorUtil disabled() {
//        return INSTANCE;
//    }
//
//    /**
//     * Creates the singleton instance using reflection.
//     */
//    private static ColorUtil createInstance() {
//        try {
//            java.lang.reflect.Constructor<ColorUtil> constructor =
//                    ColorUtil.class.getDeclaredConstructor();
//            constructor.setAccessible(true);
//            return constructor.newInstance();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create ColorUtil instance", e);
//        }
//    }
//}

package io.github.programmerjide.javadump.util;

/**
 * ANSI color utility for terminal output.
 *
 * <p>Provides static methods for colorizing text output with ANSI codes.
 * All methods return plain text if colors would not be visible.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public final class ColorUtil {

    // ==================== Configuration ====================

    private static boolean colorsEnabled = true;
    private static boolean initialized = false;

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

    private ColorUtil() {
        // Prevent instantiation - this is a utility class
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== Initialization ====================

    static {
        initialize();
    }

    private static void initialize() {
        if (initialized) {
            return;
        }

        // Check if we should enable colors
        colorsEnabled = checkColorSupport();
        initialized = true;
    }

    private static boolean checkColorSupport() {
        // Check NO_COLOR standard (https://no-color.org/)
        if (System.getenv("NO_COLOR") != null) {
            return false;
        }

        // Check if running in CI environment
        if (System.getenv("CI") != null) {
            return false;
        }

        // Check COLORTERM (modern terminals)
        if (System.getenv("COLORTERM") != null) {
            return true;
        }

        // Check TERM variable
        String term = System.getenv("TERM");
        if (term == null || term.equals("dumb")) {
            return false;
        }

        if (term.contains("color") ||
                term.contains("xterm") ||
                term.contains("256") ||
                term.equals("linux")) {
            return true;
        }

        // Check if console is available
        return System.console() != null;
    }

    // ==================== Basic Color Methods ====================

    /**
     * Colors text as a type name (orange).
     */
    public static String type(String text) {
        return colorsEnabled ? ORANGE + text + RESET : text;
    }

    /**
     * Colors text as a string value (green).
     */
    public static String string(String text) {
        return colorsEnabled ? GREEN + text + RESET : text;
    }

    /**
     * Colors text as a number (orange).
     */
    public static String number(String text) {
        return colorsEnabled ? ORANGE + text + RESET : text;
    }

    /**
     * Colors text as a field name (cyan).
     */
    public static String field(String text) {
        return colorsEnabled ? CYAN + text + RESET : text;
    }

    /**
     * Colors text as a keyword (yellow).
     */
    public static String keyword(String text) {
        return colorsEnabled ? YELLOW + text + RESET : text;
    }

    /**
     * Colors text as an error (red).
     */
    public static String error(String text) {
        return colorsEnabled ? RED + text + RESET : text;
    }

    /**
     * Colors text as dim/faded (dim gray).
     */
    public static String dim(String text) {
        return colorsEnabled ? DIM + text + RESET : text;
    }

    /**
     * Colors text in gray.
     */
    public static String gray(String text) {
        return colorsEnabled ? GRAY + text + RESET : text;
    }

    /**
     * Colors text in green.
     */
    public static String green(String text) {
        return colorsEnabled ? GREEN + text + RESET : text;
    }

    /**
     * Colors text in red.
     */
    public static String red(String text) {
        return colorsEnabled ? RED + text + RESET : text;
    }

    /**
     * Colors text in yellow.
     */
    public static String yellow(String text) {
        return colorsEnabled ? YELLOW + text + RESET : text;
    }

    /**
     * Colors text in cyan.
     */
    public static String cyan(String text) {
        return colorsEnabled ? CYAN + text + RESET : text;
    }

    /**
     * Colors text in orange.
     */
    public static String orange(String text) {
        return colorsEnabled ? ORANGE + text + RESET : text;
    }

    // ==================== Formatting Helper Methods ====================

    /**
     * Colors structural characters (braces, brackets, etc.) in gray.
     */
    public static String structural(String text) {
        return colorsEnabled ? GRAY + text + RESET : text;
    }

    /**
     * Returns a colored arrow for separating items.
     */
    public static String arrow() {
        return colorsEnabled ? GRAY + " → " + RESET : " → ";
    }

    /**
     * Formats a type with class information.
     * Example: formatType(String.class) returns "#String" in orange
     */
    public static String formatType(Class<?> clazz) {
        if (clazz == null) {
            return colorsEnabled ? ORANGE + "#null" + RESET : "#null";
        }
        String simpleName = clazz.getSimpleName();
        if (simpleName.isEmpty()) {
            simpleName = clazz.getName();
        }
        return colorsEnabled ? ORANGE + "#" + simpleName + RESET : "#" + simpleName;
    }

    /**
     * Formats a type name string.
     */
    public static String formatType(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return colorsEnabled ? ORANGE + "#null" + RESET : "#null";
        }
        return colorsEnabled ? ORANGE + "#" + typeName + RESET : "#" + typeName;
    }

    /**
     * Formats a header line (file:line) in dim style.
     */
    public static String formatHeader(String fileName, int lineNumber) {
        return colorsEnabled ? DIM + fileName + ":" + lineNumber + RESET : fileName + ":" + lineNumber;
    }

    /**
     * Formats a truncation marker.
     */
    public static String formatTruncated() {
        return colorsEnabled ? DIM + "... (truncated)" + RESET : "... (truncated)";
    }

    /**
     * Formats a cyclic/circular reference marker.
     */
    public static String formatCyclic() {
        return colorsEnabled ? YELLOW + "↻ (circular)" + RESET : "↻ (circular)";
    }

    // ==================== Configuration Methods ====================

    /**
     * Enables or disables color output.
     * @param enabled true to enable colors, false to disable
     */
    public static void setColorsEnabled(boolean enabled) {
        colorsEnabled = enabled && checkColorSupport();
    }

    /**
     * Returns whether colors are currently enabled.
     */
    public static boolean isColorsEnabled() {
        return colorsEnabled;
    }

    /**
     * Forcefully enables colors regardless of terminal detection.
     */
    public static void forceEnableColors() {
        colorsEnabled = true;
    }

    /**
     * Forcefully disables colors.
     */
    public static void forceDisableColors() {
        colorsEnabled = false;
    }

    /**
     * Checks if the terminal supports colors.
     *
     * <p>Checks various environment variables and system properties
     * to determine if ANSI colors should be enabled.
     */
    public static boolean supportsColors() {
        return checkColorSupport();
    }

    /**
     * Strips all ANSI color codes from a string.
     */
    public static String stripColors(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\033\\[[;\\d]*m", "");
    }

    /**
     * Gets the length of text without ANSI codes.
     */
    public static int lengthWithoutColors(String text) {
        if (text == null) {
            return 0;
        }
        return stripColors(text).length();
    }

    // ==================== Factory Methods (for backward compatibility) ====================

    /**
     * Creates a ColorUtil instance (for backward compatibility).
     * Note: This method exists only for backward compatibility.
     * @return null since this is a utility class with only static methods
     * @deprecated Use static methods directly instead.
     */
    @Deprecated
    public static ColorUtil create() {
        // Return null to indicate this shouldn't be used
        // Alternatively, you could throw an UnsupportedOperationException
        return null;
    }

    /**
     * Creates a disabled ColorUtil (for backward compatibility).
     * Note: This method exists only for backward compatibility.
     * @return null since this is a utility class with only static methods
     * @deprecated Use {@link #forceDisableColors()} or {@link #setColorsEnabled(boolean)} instead.
     */
    @Deprecated
    public static ColorUtil disabled() {
        forceDisableColors();
        // Return null to indicate this shouldn't be used
        return null;
    }
}