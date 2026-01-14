package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.ColorUtil;
import io.github.programmerjide.javadump.util.StackTraceUtil;
import io.github.programmerjide.javadump.util.StringUtil;
import io.github.programmerjide.javadump.util.TypeNameUtil;

import java.util.Map;
import java.util.Optional;

/**
 * Main formatter for converting DumpNode trees to formatted output.
 *
 * <p>This class handles the core formatting logic and delegates to specialized
 * formatters for different output formats (console, HTML, JSON).
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class Formatter {

    private final DumperConfig config;

    public Formatter(DumperConfig config) {
        this.config = config;
        // Configure ColorUtil based on config
        if (config.isColorEnabled()) {
            ColorUtil.forceEnableColors();
        } else {
            ColorUtil.forceDisableColors();
        }
    }

    /**
     * Formats multiple values into a single output string.
     *
     * @param nodes the nodes to format
     * @return formatted string
     */
    public String format(DumpNode... nodes) {
        if (nodes == null || nodes.length == 0) {
            return "";
        }

        StringBuilder output = new StringBuilder();

        // Add header with call site
        Optional<StackTraceElement> callSite = StackTraceUtil.getCallSite();
        if (callSite.isPresent()) {
            String header = formatHeader(callSite.get());
            output.append(header).append("\n");
        }

        // Format each node
        for (int i = 0; i < nodes.length; i++) {
            if (i > 0) {
                output.append("\n");
            }
            output.append(formatNode(nodes[i], 0));
        }

        return output.toString();
    }

    /**
     * Formats a single DumpNode.
     *
     * @param node the node to format
     * @param depth current indentation depth
     * @return formatted string
     */
    public String formatNode(DumpNode node, int depth) {
        if (node == null) {
            return formatNull();
        }

        return switch (node.getType()) {
            case NULL -> formatNull();
            case PRIMITIVE -> formatPrimitive(node);
            case STRING -> formatString(node);
            case ENUM -> formatEnum(node);
            case NUMBER -> formatNumber(node);
            case BOOLEAN -> formatBoolean(node);
            case ARRAY -> formatArray(node, depth);
            case COLLECTION -> formatCollection(node, depth);
            case MAP -> formatMap(node, depth);
            case OBJECT -> formatObject(node, depth);
            case CYCLIC -> formatCyclic(node);
            case TRUNCATED -> formatTruncated();
            case ERROR -> formatError(node);
        };
    }

    /**
     * Formats a null value.
     */
    private String formatNull() {
        return ColorUtil.keyword("null");
    }

    /**
     * Formats a primitive node.
     */
    private String formatPrimitive(DumpNode node) {
        // Handle boolean primitives as booleans
        if (node.getClazz() == boolean.class || node.getClazz() == Boolean.class) {
            return formatBoolean(node);
        }

        return ColorUtil.type(node.getTypeName()) + " " + ColorUtil.number(String.valueOf(node.getValue()));
    }

    /**
     * Formats a string node.
     */
    private String formatString(DumpNode node) {
        String str = String.valueOf(node.getValue());
        String escaped = StringUtil.escape(str);
        return ColorUtil.string("\"" + escaped + "\"");
    }

    /**
     * Formats an enum node.
     */
    private String formatEnum(DumpNode node) {
        return ColorUtil.type(node.getTypeName()) + "." + ColorUtil.keyword(String.valueOf(node.getValue()));
    }

    /**
     * Formats a number node.
     */
    private String formatNumber(DumpNode node) {
        return ColorUtil.type(node.getTypeName()) + " " + ColorUtil.number(String.valueOf(node.getValue()));
    }

    /**
     * Formats a boolean node.
     */
    private String formatBoolean(DumpNode node) {
        // Always output "boolean" for boolean nodes
        return ColorUtil.type("boolean") + " " + ColorUtil.keyword(String.valueOf(node.getValue()));
    }

    /**
     * Formats an array node.
     */
    private String formatArray(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #int[][3]
        sb.append(ColorUtil.formatType(node.getClazz()));
        sb.append(ColorUtil.structural("["));
        sb.append(ColorUtil.yellow(String.valueOf(node.getDisplaySize())));
        sb.append(ColorUtil.structural("]"));

        if (node.isEmpty()) {
            sb.append(" ").append(ColorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(ColorUtil.structural("{"));
        sb.append("\n");

        // Format elements
        for (int i = 0; i < node.getElements().size(); i++) {
            DumpNode element = node.getElements().get(i);
            sb.append(StringUtil.indent(depth + 1));
            sb.append(ColorUtil.gray(String.valueOf(i)));
            sb.append(ColorUtil.arrow());
            sb.append(formatNode(element, depth + 1));
            sb.append("\n");
        }

        // Truncation marker
        if (node.isTruncated()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(ColorUtil.formatTruncated());
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(ColorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats a collection node.
     */
    private String formatCollection(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #ArrayList[3]
        String typeName = TypeNameUtil.getSimpleName(node.getClazz());
        sb.append(ColorUtil.formatType(typeName));
        sb.append(ColorUtil.structural("["));
        sb.append(ColorUtil.yellow(String.valueOf(node.getDisplaySize())));
        sb.append(ColorUtil.structural("]"));

        if (node.isEmpty()) {
            sb.append(" ").append(ColorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(ColorUtil.structural("{"));
        sb.append("\n");

        // Format elements
        for (int i = 0; i < node.getElements().size(); i++) {
            DumpNode element = node.getElements().get(i);
            sb.append(StringUtil.indent(depth + 1));
            sb.append(ColorUtil.gray(String.valueOf(i)));
            sb.append(ColorUtil.arrow());
            sb.append(formatNode(element, depth + 1));
            sb.append("\n");
        }

        // Truncation marker
        if (node.isTruncated()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(ColorUtil.formatTruncated());
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(ColorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats a map node.
     */
    private String formatMap(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #HashMap[2]
        String typeName = TypeNameUtil.getSimpleName(node.getClazz());
        sb.append(ColorUtil.formatType(typeName));
        sb.append(ColorUtil.structural("["));
        sb.append(ColorUtil.yellow(String.valueOf(node.getDisplaySize())));
        sb.append(ColorUtil.structural("]"));

        if (node.isEmpty()) {
            sb.append(" ").append(ColorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(ColorUtil.structural("{"));
        sb.append("\n");

        // Format entries
        for (Map.Entry<DumpNode, DumpNode> entry : node.getEntries().entrySet()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(formatNode(entry.getKey(), depth + 1));
            sb.append(ColorUtil.arrow());
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
        }

        // Truncation marker
        if (node.isTruncated()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(ColorUtil.formatTruncated());
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(ColorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats an object node.
     */
    private String formatObject(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #TestClass
        String typeName = TypeNameUtil.getSimpleName(node.getClazz());
        sb.append(ColorUtil.formatType(typeName));

        if (node.isEmpty()) {
            sb.append(" ").append(ColorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(ColorUtil.structural("{"));
        sb.append("\n");

        // Format fields
        for (Map.Entry<String, DumpNode> field : node.getFields().entrySet()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(ColorUtil.cyan(field.getKey()));
            sb.append(ColorUtil.arrow());
            sb.append(formatNode(field.getValue(), depth + 1));
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(ColorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats a cyclic reference.
     */
    private String formatCyclic(DumpNode node) {
        return ColorUtil.formatCyclic();
    }

    /**
     * Formats a truncation marker.
     */
    private String formatTruncated() {
        return ColorUtil.formatTruncated();
    }

    /**
     * Formats an error node.
     */
    private String formatError(DumpNode node) {
        String message = node.getValue() != null ?
                String.valueOf(node.getValue()) : "Error analyzing object";
        return ColorUtil.error("[ERROR: " + message + "]");
    }

    /**
     * Formats the header with call site information.
     */
    private String formatHeader(StackTraceElement callSite) {
        String location = StackTraceUtil.formatCallSite(callSite);
        return ColorUtil.formatHeader(location, callSite.getLineNumber());
    }
}