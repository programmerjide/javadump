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
    private final ColorUtil colorUtil;
    private final ValueFormatter valueFormatter;
    private final StructureFormatter structureFormatter;

    public Formatter(DumperConfig config) {
        this.config = config;
        this.colorUtil = config.isColorEnabled() ? ColorUtil.create() : ColorUtil.disabled();
        this.valueFormatter = new ValueFormatter(config);
        this.structureFormatter = new StructureFormatter(colorUtil);
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
            return valueFormatter.formatNull();
        }

        return switch (node.getType()) {
            case NULL -> valueFormatter.formatNull();
            case PRIMITIVE -> valueFormatter.formatPrimitive(node);
            case STRING -> valueFormatter.formatString(node);
            case ENUM -> valueFormatter.formatEnum(node);
            case NUMBER -> valueFormatter.formatNumber(node);
            case BOOLEAN -> valueFormatter.formatBoolean(node);
            case ARRAY -> formatArray(node, depth);
            case COLLECTION -> formatCollection(node, depth);
            case MAP -> formatMap(node, depth);
            case OBJECT -> formatObject(node, depth);
            case CYCLIC -> valueFormatter.formatCyclic(node);
            case TRUNCATED -> valueFormatter.formatTruncated();
            case ERROR -> valueFormatter.formatError(node);
        };
    }

    /**
     * Formats an array node.
     */
    private String formatArray(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #int[3]
        sb.append(colorUtil.formatType(node.getClazz()));
        sb.append(colorUtil.structural("["));
        sb.append(colorUtil.yellow(String.valueOf(node.getDisplaySize())));
        sb.append(colorUtil.structural("]"));

        if (node.isEmpty()) {
            sb.append(" ").append(colorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(colorUtil.structural("{"));
        sb.append("\n");

        // Format elements
        for (int i = 0; i < node.getElements().size(); i++) {
            DumpNode element = node.getElements().get(i);
            sb.append(StringUtil.indent(depth + 1));
            sb.append(colorUtil.gray(String.valueOf(i)));
            sb.append(colorUtil.arrow());
            sb.append(formatNode(element, depth + 1));
            sb.append("\n");
        }

        // Truncation marker
        if (node.isTruncated()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(colorUtil.formatTruncated());
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(colorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats a collection node.
     */
    private String formatCollection(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #List[3]
        String typeName = TypeNameUtil.getSimpleName(node.getClazz());
        sb.append(colorUtil.formatType(typeName));
        sb.append(colorUtil.structural("["));
        sb.append(colorUtil.yellow(String.valueOf(node.getDisplaySize())));
        sb.append(colorUtil.structural("]"));

        if (node.isEmpty()) {
            sb.append(" ").append(colorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(colorUtil.structural("{"));
        sb.append("\n");

        // Format elements
        for (int i = 0; i < node.getElements().size(); i++) {
            DumpNode element = node.getElements().get(i);
            sb.append(StringUtil.indent(depth + 1));
            sb.append(colorUtil.gray(String.valueOf(i)));
            sb.append(colorUtil.arrow());
            sb.append(formatNode(element, depth + 1));
            sb.append("\n");
        }

        // Truncation marker
        if (node.isTruncated()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(colorUtil.formatTruncated());
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(colorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats a map node.
     */
    private String formatMap(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #Map[3]
        String typeName = TypeNameUtil.getSimpleName(node.getClazz());
        sb.append(colorUtil.formatType(typeName));
        sb.append(colorUtil.structural("["));
        sb.append(colorUtil.yellow(String.valueOf(node.getDisplaySize())));
        sb.append(colorUtil.structural("]"));

        if (node.isEmpty()) {
            sb.append(" ").append(colorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(colorUtil.structural("{"));
        sb.append("\n");

        // Format entries
        for (Map.Entry<DumpNode, DumpNode> entry : node.getEntries().entrySet()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(formatNode(entry.getKey(), depth + 1));
            sb.append(colorUtil.arrow());
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
        }

        // Truncation marker
        if (node.isTruncated()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(colorUtil.formatTruncated());
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(colorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats an object node.
     */
    private String formatObject(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();

        // Type header: #User
        String typeName = TypeNameUtil.getSimpleName(node.getClazz());
        sb.append(colorUtil.formatType(typeName));

        if (node.isEmpty()) {
            sb.append(" ").append(colorUtil.structural("{}"));
            return sb.toString();
        }

        sb.append(" ").append(colorUtil.structural("{"));
        sb.append("\n");

        // Format fields
        for (Map.Entry<String, DumpNode> field : node.getFields().entrySet()) {
            sb.append(StringUtil.indent(depth + 1));
            sb.append(colorUtil.cyan(field.getKey()));
            sb.append(colorUtil.arrow());
            sb.append(formatNode(field.getValue(), depth + 1));
            sb.append("\n");
        }

        sb.append(StringUtil.indent(depth));
        sb.append(colorUtil.structural("}"));

        return sb.toString();
    }

    /**
     * Formats the header with call site information.
     */
    private String formatHeader(StackTraceElement callSite) {
        String location = StackTraceUtil.formatCallSite(callSite);
        return colorUtil.formatHeader(location, callSite.getLineNumber());
    }

    /**
     * Gets the ColorUtil instance.
     */
    public ColorUtil getColorUtil() {
        return colorUtil;
    }
}