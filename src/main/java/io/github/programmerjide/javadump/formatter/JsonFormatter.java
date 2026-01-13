package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.StringUtil;

import java.util.Map;

/**
 * Formats DumpNode trees into JSON output.
 *
 * <p>Converts analyzed objects into clean, pretty-printed JSON format
 * suitable for APIs, logging, and data export.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class JsonFormatter {

    private final DumperConfig config;
    private final boolean prettyPrint;

    /**
     * Creates a new JSON formatter with default pretty-printing enabled.
     */
    public JsonFormatter(DumperConfig config) {
        this(config, true);
    }

    /**
     * Creates a new JSON formatter.
     *
     * @param config the dumper configuration
     * @param prettyPrint whether to format output with indentation
     */
    public JsonFormatter(DumperConfig config, boolean prettyPrint) {
        this.config = config;
        this.prettyPrint = prettyPrint;
    }

    /**
     * Formats a DumpNode as JSON.
     *
     * @param node the node to format
     * @return JSON string representation
     */
    public String format(DumpNode node) {
        return formatNode(node, 0);
    }

    /**
     * Formats a DumpNode at the specified depth.
     */
    private String formatNode(DumpNode node, int depth) {
        if (node == null) {
            return "null";
        }

        if (node.isCircular()) {
            return "\"<circular reference>\"";
        }

        if (node.isMaxDepthReached()) {
            return "\"<max depth reached>\"";
        }

        switch (node.getType()) {
            case NULL:
                return "null";
            case STRING:
                return formatString(node.getValue());
            case NUMBER:
                return formatNumber(node.getValue());
            case BOOLEAN:
                return String.valueOf(node.getValue());
            case ENUM:
                return formatString(node.getValue());
            case ARRAY:
            case COLLECTION:
                return formatArray(node, depth);
            case MAP:
                return formatMap(node, depth);
            case OBJECT:
                return formatObject(node, depth);
            default:
                return "null";
        }
    }

    /**
     * Formats a string value for JSON.
     */
    private String formatString(Object value) {
        if (value == null) {
            return "null";
        }
        String str = String.valueOf(value);
        // Escape for JSON
        str = str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
        return "\"" + str + "\"";
    }

    /**
     * Formats a number value for JSON.
     */
    private String formatNumber(Object value) {
        if (value == null) {
            return "null";
        }
        return String.valueOf(value);
    }

    /**
     * Formats an array/collection as JSON array.
     */
    private String formatArray(DumpNode node, int depth) {
        Map<String, DumpNode> children = node.getChildren();

        if (children.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        if (prettyPrint) {
            sb.append("\n");
        }

        int index = 0;
        int maxItems = Math.min(children.size(), config.getMaxItems());

        for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
            if (index >= maxItems) {
                if (prettyPrint) {
                    sb.append(indent(depth + 1));
                }
                sb.append("\"... ").append(children.size() - maxItems)
                        .append(" more items\"");
                break;
            }

            if (index > 0) {
                sb.append(",");
                if (prettyPrint) {
                    sb.append("\n");
                }
            }

            if (prettyPrint) {
                sb.append(indent(depth + 1));
            }

            sb.append(formatNode(entry.getValue(), depth + 1));
            index++;
        }

        if (prettyPrint) {
            sb.append("\n").append(indent(depth));
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * Formats a map as JSON object.
     */
    private String formatMap(DumpNode node, int depth) {
        Map<String, DumpNode> children = node.getChildren();

        if (children.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        if (prettyPrint) {
            sb.append("\n");
        }

        int index = 0;
        int maxItems = Math.min(children.size(), config.getMaxItems());

        for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
            if (index >= maxItems) {
                if (index > 0) {
                    sb.append(",");
                    if (prettyPrint) {
                        sb.append("\n");
                    }
                }
                if (prettyPrint) {
                    sb.append(indent(depth + 1));
                }
                sb.append("\"__truncated__\": \"")
                        .append(children.size() - maxItems)
                        .append(" more entries\"");
                break;
            }

            if (index > 0) {
                sb.append(",");
                if (prettyPrint) {
                    sb.append("\n");
                }
            }

            if (prettyPrint) {
                sb.append(indent(depth + 1));
            }

            // Key
            String key = entry.getKey();
            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
            sb.append(formatString(key));

            sb.append(":");
            if (prettyPrint) {
                sb.append(" ");
            }

            // Value
            sb.append(formatNode(entry.getValue(), depth + 1));
            index++;
        }

        if (prettyPrint) {
            sb.append("\n").append(indent(depth));
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Formats an object as JSON object.
     */
    private String formatObject(DumpNode node, int depth) {
        Map<String, DumpNode> children = node.getChildren();

        if (children.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        if (prettyPrint) {
            sb.append("\n");
        }

        int index = 0;
        for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
            if (index > 0) {
                sb.append(",");
                if (prettyPrint) {
                    sb.append("\n");
                }
            }

            if (prettyPrint) {
                sb.append(indent(depth + 1));
            }

            sb.append(formatString(entry.getKey()));
            sb.append(":");
            if (prettyPrint) {
                sb.append(" ");
            }
            sb.append(formatNode(entry.getValue(), depth + 1));
            index++;
        }

        if (prettyPrint) {
            sb.append("\n").append(indent(depth));
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Creates indentation string.
     */
    private String indent(int depth) {
        return StringUtil.repeat("  ", depth);
    }
}
