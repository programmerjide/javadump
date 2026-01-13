package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;

import java.util.Map;

/**
 * Formats dumps as YAML.
 *
 * @author Olaldejo Olajide
 * @since 1.3.0
 */
public class YamlFormatter {

    private final DumperConfig config;

    public YamlFormatter(DumperConfig config) {
        this.config = config;
    }

    public String format(DumpNode node) {
        return formatNode(node, 0);
    }

    private String formatNode(DumpNode node, int depth) {
        if (node == null) return "null";

        switch (node.getType()) {
            case NULL:
                return "null";
            case STRING:
                return "\"" + escape(String.valueOf(node.getValue())) + "\"";
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
        sb.append("\n");

        for (Map.Entry<String, DumpNode> entry : node.getChildren().entrySet()) {
            sb.append(indent(depth));
            sb.append(entry.getKey()).append(": ");
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String formatCollection(DumpNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        for (DumpNode child : node.getChildren().values()) {
            sb.append(indent(depth)).append("- ");
            sb.append(formatNode(child, depth + 1));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String formatMap(DumpNode node, int depth) {
        return formatObject(node, depth);
    }

    private String indent(int depth) {
        return "  ".repeat(Math.max(0, depth));
    }

    private String escape(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}
