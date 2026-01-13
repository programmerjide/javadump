package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.util.ColorUtil;

/**
 * Helper formatter for structural elements (braces, brackets, arrows).
 *
 * <p>This class provides methods for formatting structural characters
 * that organize the dump output.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class StructureFormatter {

    private final ColorUtil colorUtil;

    /**
     * Creates a new structure formatter.
     *
     * @param colorUtil the color utility to use
     */
    public StructureFormatter(ColorUtil colorUtil) {
        this.colorUtil = colorUtil;
    }

    /**
     * Formats an opening brace.
     */
    public String openBrace() {
        return ColorUtil.structural("{");
    }

    /**
     * Formats a closing brace.
     */
    public String closeBrace() {
        return ColorUtil.structural("}");
    }

    /**
     * Formats an opening bracket.
     */
    public String openBracket() {
        return ColorUtil.structural("[");
    }

    /**
     * Formats a closing bracket.
     */
    public String closeBracket() {
        return ColorUtil.structural("]");
    }

    /**
     * Formats an arrow separator.
     */
    public String arrow() {
        return ColorUtil.arrow();
    }

    /**
     * Formats a colon separator.
     */
    public String colon() {
        return ColorUtil.structural(":");
    }

    /**
     * Formats a comma separator.
     */
    public String comma() {
        return ColorUtil.structural(",");
    }
}