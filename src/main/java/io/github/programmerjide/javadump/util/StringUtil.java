package io.github.programmerjide.javadump.util;

/**
 * String manipulation utilities.
 */
public final class StringUtil {

    private static final String ELLIPSIS = "…";

    private StringUtil() {
        throw new AssertionError("Utility class");
    }

    /**
     * Escapes control characters in a string.
     */
    public static String escape(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder(input.length() + 16);

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (c) {
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '"':
                    result.append("\\\"");
                    break;
                default:
                    if (isControlCharacter(c)) {
                        result.append(String.format("\\u%04x", (int) c));
                    } else {
                        result.append(c);
                    }
            }
        }

        return result.toString();
    }

    /**
     * Truncates a string with ellipsis.
     */
    public static String truncate(String input, int maxLength) {
        if (maxLength < 1) {
            throw new IllegalArgumentException("maxLength must be at least 1");
        }

        if (input == null || input.isEmpty()) {
            return "";
        }

        if (input.length() <= maxLength) {
            return input;
        }

        int truncateAt = Math.max(0, maxLength - ELLIPSIS.length());
        return input.substring(0, truncateAt) + ELLIPSIS;
    }

    /**
     * Safely converts object to string.
     */
    public static String safeToString(Object obj) {
        if (obj == null) {
            return "null";
        }

        try {
            String result = obj.toString();
            return result != null ? result : "null";
        } catch (Exception e) {
            return obj.getClass().getName() + "@" +
                    Integer.toHexString(System.identityHashCode(obj));
        }
    }

    /**
     * Repeats a string n times.
     */
    public static String repeat(String str, int times) {
        if (times < 0) {
            throw new IllegalArgumentException("times must be non-negative");
        }

        if (str == null || str.isEmpty() || times == 0) {
            return "";
        }

        if (times == 1) {
            return str;
        }

        StringBuilder result = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++) {
            result.append(str);
        }

        return result.toString();
    }

    /**
     * Creates indentation string.
     */
    public static String indent(int level) {
        return repeat("  ", Math.max(0, level));
    }

    /**
     * Wraps string in quotes.
     */
    public static String quote(String str) {
        return "\"" + (str != null ? str : "") + "\"";
    }

    /**
     * Checks if string is null or empty.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if string is null, empty, or whitespace.
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Returns first non-empty string.
     */
    public static String firstNonEmpty(String... strings) {
        if (strings == null) {
            return "";
        }

        for (String str : strings) {
            if (!isEmpty(str)) {
                return str;
            }
        }

        return "";
    }

    /**
     * Abbreviates string in the middle.
     */
    public static String abbreviateMiddle(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str != null ? str : "";
        }

        int halfLength = (maxLength - ELLIPSIS.length()) / 2;
        int endStart = str.length() - halfLength;

        return str.substring(0, halfLength) + ELLIPSIS + str.substring(endStart);
    }

    /**
     * Checks if character is a control character.
     */
    private static boolean isControlCharacter(char c) {
        return (c >= 0x00 && c <= 0x1F) || c == 0x7F || (c >= 0x80 && c <= 0x9F);
    }
}