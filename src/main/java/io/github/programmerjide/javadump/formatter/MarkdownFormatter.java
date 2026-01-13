package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;

import java.util.Map;

/**
 * Formats dumps as Markdown for documentation.
 *
 * @author Olaldejo Olajide
 * @since 1.3.0
 */
public class MarkdownFormatter {

    private final DumperConfig config;

    public MarkdownFormatter(DumperConfig config) {
        this.config = config;
    }

    public String format(DumpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("```java\n");
        sb.append(formatNode(node, 0));
        sb.append("\n```");
        return sb.toString();
    }

    private String formatNode(DumpNode node, int depth) {
        if (node == null) return "null";

        switch (node.getType()) {
            case STRING:
                return "\"" + node.getValue() + "\"";
            case NUMBER:
            case BOOLEAN:
                return String.valueOf(node.getValue());
            case OBJECT:
                return formatObject(node, depth);
            case COLLECTION:
                return formatCollection(node, depth);
            case MAP:
                return formatMap(node, depth);
            default:
                return String.valueOf(node.getValue());
        }
    }

    private String formatObject(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getTypeName()).append(" {\n");

        for (Map.Entry<String, DumpNode> entry : node.getChildren().entrySet()) {
            sb.append(indent(depth + 1));
            sb.append(entry.getKey()).append(": ");
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
        }

        sb.append(indent(depth)).append("}");
        return sb.toString();
    }

    private String formatCollection(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        int i = 0;
        for (DumpNode child : node.getChildren().values()) {
            if (i++ > 0) sb.append(", ");
            sb.append(formatNode(child, depth + 1));
        }

        sb.append("]");
        return sb.toString();
    }

    private String formatMap(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        for (Map.Entry<String, DumpNode> entry : node.getChildren().entrySet()) {
            sb.append(indent(depth + 1));
            sb.append(entry.getKey()).append(" => ");
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
        }

        sb.append(indent(depth)).append("}");
        return sb.toString();
    }

    private String indent(int depth) {
        return "  ".repeat(Math.max(0, depth));
    }
}

