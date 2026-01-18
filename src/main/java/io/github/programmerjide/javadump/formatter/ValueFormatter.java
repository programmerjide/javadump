package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.ColorUtil;
import io.github.programmerjide.javadump.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Formats individual values from DumpNode objects.
 *
 * <p>This class handles the formatting of primitive values, strings,
 * and other basic types. Complex structures like collections and objects
 * are handled by the main Formatter class.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class ValueFormatter {

    private final DumperConfig config;
    private static final String INDENT = "  ";

    /**
     * Creates a new value formatter with the given configuration.
     */
    public ValueFormatter(DumperConfig config) {
        this.config = config;
        if (config.isColorEnabled()) {
            ColorUtil.forceEnableColors();
        } else {
            ColorUtil.forceDisableColors();
        }
    }

    /**
     * Formats a dump node at the given depth level.
     */
    public String format(DumpNode node, int depth) {
        if (node == null) {
            return formatNull();
        }

        if (node.isCircular()) {
            return formatCircular();
        }

        if (node.isMaxDepthReached()) {
            return formatMaxDepth();
        }

        switch (node.getType()) {
            case NULL:
                return formatNull();
            case STRING:
                return formatString(node);
            case NUMBER:
                return formatNumber(node);
            case BOOLEAN:
                return formatBoolean(node);
            case ENUM:
                return formatEnum(node);
            case ARRAY:
            case COLLECTION:
                return formatCollection(node, depth);
            case MAP:
                return formatMap(node, depth);
            case OBJECT:
                return formatObject(node, depth);
            default:
                return formatUnknown(node);
        }
    }

    /**
     * Formats a null value.
     */
    public String formatNull() {
        if (!config.isColorEnabled()) {
            return "null";
        }
        return ColorUtil.keyword("null");
    }

    /**
     * Formats a string node.
     */
    public String formatString(DumpNode node) {
        String str = String.valueOf(node.getValue());
        String escaped = StringUtil.escape(str);
        int length = str.length();

        if (!config.isColorEnabled()) {
            return String.format("string(%d) \"%s\"", length, escaped);
        }

        return String.format("%s %s",
                ColorUtil.type("string(" + length + ")"),
                ColorUtil.string("\"" + escaped + "\""));
    }

    /**
     * Formats a primitive/number node.
     */
    public String formatPrimitive(DumpNode node) {
        return formatNumber(node);
    }

    /**
     * Formats a number node.
     */
    public String formatNumber(DumpNode node) {
        String typeName = node.getTypeName();
        String value = String.valueOf(node.getValue());

        if (!config.isColorEnabled()) {
            return String.format("%s %s", typeName, value);
        }

        return String.format("%s %s",
                ColorUtil.type(typeName),
                ColorUtil.number(value));
    }

    /**
     * Formats a boolean node.
     */
    public String formatBoolean(DumpNode node) {
        String str = String.valueOf(node.getValue());

        if (!config.isColorEnabled()) {
            return String.format("boolean %s", str);
        }

        return String.format("%s %s",
                ColorUtil.type("boolean"),
                ColorUtil.keyword(str));
    }

    /**
     * Formats an enum node.
     */
    public String formatEnum(DumpNode node) {
        String typeName = node.getTypeName();
        String value = String.valueOf(node.getValue());

        if (!config.isColorEnabled()) {
            return String.format("%s.%s", typeName, value);
        }

        return String.format("%s.%s",
                ColorUtil.type(typeName),
                ColorUtil.keyword(value));
    }

    /**
     * Formats a circular reference indicator.
     */
    public String formatCircular() {
        if (!config.isColorEnabled()) {
            return "{ ... } ↻ CIRCULAR REFERENCE";
        }
        return ColorUtil.error("{ ... } ↻ CIRCULAR REFERENCE");
    }

    /**
     * Formats a circular reference node.
     */
    public String formatCyclic(DumpNode node) {
        return formatCircular();
    }

    /**
     * Formats a max depth indicator.
     */
    public String formatMaxDepth() {
        if (!config.isColorEnabled()) {
            return "{ ... } MAX DEPTH REACHED";
        }
        return ColorUtil.dim("{ ... } MAX DEPTH REACHED");
    }

    /**
     * Formats a truncation marker.
     */
    public String formatTruncated() {
        if (!config.isColorEnabled()) {
            return "... (truncated)";
        }
        return ColorUtil.formatTruncated();
    }

    /**
     * Formats an error node.
     */
    public String formatError(DumpNode node) {
        String message = node.getValue() != null ?
                String.valueOf(node.getValue()) : "Error analyzing object";

        if (!config.isColorEnabled()) {
            return "[ERROR: " + message + "]";
        }
        return ColorUtil.error("[ERROR: " + message + "]");
    }

    /**
     * Formats a collection (array, list, set).
     */
    private String formatCollection(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Try to get data from elements first, then from children
        List<DumpNode> elements = node.getElements();
        Map<String, DumpNode> children = node.getChildren();

        boolean useElements = !elements.isEmpty();
        boolean useChildren = !children.isEmpty();

        int size = 0;
        if (useElements) {
            size = elements.size();
        } else if (useChildren) {
            size = children.size();
        } else {
            // Check display size if no elements/children
            size = node.getDisplaySize();
        }

        // Type header with actual size
        String header = String.format("%s[%d]",
                node.getTypeName(),
                size);
        sb.append(colorize(header, ColorUtil::type));

        if (size == 0) {
            sb.append(" []");
            return sb.toString();
        }

        sb.append(" [\n");

        // Format each item
        int maxItems = Math.min(size, config.getMaxItems());

        for (int i = 0; i < maxItems; i++) {
            String indent = getIndent(depth + 1);
            sb.append(indent);
            sb.append(colorize(i + " →", ColorUtil::dim));
            sb.append(" ");

            if (useElements && i < elements.size()) {
                sb.append(format(elements.get(i), depth + 1));
            } else if (useChildren) {
                String key = String.valueOf(i);
                DumpNode child = children.get(key);
                if (child != null) {
                    sb.append(format(child, depth + 1));
                }
            } else {
                // If we have size but no actual elements (e.g., truncated)
                sb.append(colorize("...", ColorUtil::dim));
            }
            sb.append("\n");
        }

        if (size > maxItems || node.isTruncated()) {
            String indent = getIndent(depth + 1);
            sb.append(indent);
            sb.append(colorize(
                    String.format("... %d more items", size - maxItems),
                    ColorUtil::dim));
            sb.append("\n");
        }

        sb.append(getIndent(depth));
        sb.append("]");

        return sb.toString();
    }

    /**
     * Formats a map.
     */
    private String formatMap(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Try to get data from entries first, then from children
        Map<DumpNode, DumpNode> entries = node.getEntries();
        Map<String, DumpNode> children = node.getChildren();

        boolean useEntries = !entries.isEmpty();
        boolean useChildren = !children.isEmpty();

        int size = 0;
        if (useEntries) {
            size = entries.size();
        } else if (useChildren) {
            size = children.size();
        } else {
            // Check display size if no entries/children
            size = node.getDisplaySize();
        }

        // Type header
        String header = String.format("%s[%d]",
                node.getTypeName(),
                size);
        sb.append(colorize(header, ColorUtil::type));

        if (size == 0) {
            sb.append(" {}");
            return sb.toString();
        }

        sb.append(" {\n");

        // Format each entry
        int index = 0;
        int maxItems = Math.min(size, config.getMaxItems());

        if (useEntries) {
            for (Map.Entry<DumpNode, DumpNode> entry : entries.entrySet()) {
                if (index >= maxItems) break;

                String indent = getIndent(depth + 1);
                sb.append(indent);

                // Format key
                sb.append(format(entry.getKey(), depth + 1));
                sb.append(" → ");

                // Format value
                sb.append(format(entry.getValue(), depth + 1));
                sb.append("\n");

                index++;
            }
        } else if (useChildren) {
            for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
                if (index >= maxItems) break;

                String indent = getIndent(depth + 1);
                sb.append(indent);

                // Format key
                String key = entry.getKey();
                // Remove quotes if they exist for cleaner display
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                sb.append(colorize(key, ColorUtil::string));
                sb.append(" → ");

                // Format value
                sb.append(format(entry.getValue(), depth + 1));
                sb.append("\n");

                index++;
            }
        }

        if (size > maxItems || node.isTruncated()) {
            String indent = getIndent(depth + 1);
            sb.append(indent);
            sb.append(colorize(
                    String.format("... %d more entries", size - maxItems),
                    ColorUtil::dim));
            sb.append("\n");
        }

        sb.append(getIndent(depth));
        sb.append("}");

        return sb.toString();
    }

    /**
     * Formats an object with its fields.
     */
    private String formatObject(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Try to get data from fields first, then from children
        Map<String, DumpNode> fields = node.getFields();
        Map<String, DumpNode> children = node.getChildren();

        boolean useFields = !fields.isEmpty();
        boolean useChildren = !children.isEmpty();

        // Type header: #Person
        sb.append(colorize("#" + node.getTypeName(), ColorUtil::type));

        if (!useFields && !useChildren) {
            sb.append(" {}");
            return sb.toString();
        }

        sb.append(" {\n");

        // Format each field
        if (useFields) {
            for (Map.Entry<String, DumpNode> entry : fields.entrySet()) {
                formatField(sb, entry.getKey(), entry.getValue(), depth);
            }
        } else if (useChildren) {
            for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
                formatField(sb, entry.getKey(), entry.getValue(), depth);
            }
        }

        sb.append(getIndent(depth));
        sb.append("}");

        return sb.toString();
    }

    /**
     * Helper method to format a single field.
     */
    private void formatField(StringBuilder sb, String fieldName, DumpNode value, int depth) {
        String indent = getIndent(depth + 1);
        sb.append(indent);

        // Extract visibility marker if present (-, +, #, ~)
        String visibilityMarker = "";
        String cleanFieldName = fieldName;

        if (fieldName.length() > 0) {
            char first = fieldName.charAt(0);
            if (first == '-' || first == '+' || first == '#' || first == '~') {
                visibilityMarker = String.valueOf(first);
                cleanFieldName = fieldName.substring(1);
            }
        }

        // Format: -name → "value"
        if (!visibilityMarker.isEmpty()) {
            sb.append(colorize(visibilityMarker, ColorUtil::dim));
        }
        sb.append(colorize(cleanFieldName, ColorUtil::cyan));
        sb.append(colorize(" → ", ColorUtil::dim));
        sb.append(format(value, depth + 1));
        sb.append("\n");
    }

    /**
     * Formats an unknown type.
     */
    private String formatUnknown(DumpNode node) {
        return colorize(
                String.format("%s %s",
                        node.getTypeName(),
                        node.getValue()),
                ColorUtil::dim);
    }

    /**
     * Gets indentation string for the given depth.
     */
    private String getIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(INDENT);
        }
        return sb.toString();
    }

    /**
     * Applies color function if colors are enabled.
     */
    private String colorize(String text, ColorFunction colorFn) {
        if (!config.isColorEnabled()) {
            return text;
        }
        return colorFn.apply(text);
    }

    /**
     * Functional interface for color functions.
     */
    @FunctionalInterface
    private interface ColorFunction {
        String apply(String text);
    }
}