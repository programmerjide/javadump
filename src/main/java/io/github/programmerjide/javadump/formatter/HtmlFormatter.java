package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.StringUtil;

import java.util.Map;

/**
 * Formats DumpNode trees into styled HTML output.
 *
 * <p>Generates beautiful, syntax-highlighted HTML that can be embedded
 * in web pages, saved to files, or served via web endpoints.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class HtmlFormatter {

    private final DumperConfig config;

    public HtmlFormatter(DumperConfig config) {
        this.config = config;
    }

    /**
     * Formats a DumpNode as styled HTML.
     *
     * @param node the node to format
     * @return HTML string with embedded CSS
     */
    public String format(DumpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(getHtmlHeader());
        sb.append("<div class=\"dump-output\">\n");
        sb.append(formatNode(node, 0));
        sb.append("</div>\n");
        sb.append(getHtmlFooter());
        return sb.toString();
    }

    /**
     * Formats a DumpNode as HTML fragment (no header/footer).
     */
    public String formatFragment(DumpNode node) {
        return formatNode(node, 0);
    }

    private String formatNode(DumpNode node, int depth) {
        if (node == null) {
            return span("null", "keyword");
        }

        if (node.isCircular()) {
            return span("{ ... } ↻ CIRCULAR", "error");
        }

        if (node.isMaxDepthReached()) {
            return span("{ ... } MAX DEPTH", "dim");
        }

        switch (node.getType()) {
            case NULL:
                return span("null", "keyword");
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
                return formatArray(node, depth);
            case MAP:
                return formatMap(node, depth);
            case OBJECT:
                return formatObject(node, depth);
            default:
                return span("unknown", "dim");
        }
    }

    private String formatString(DumpNode node) {
        String str = String.valueOf(node.getValue());
        String escaped = htmlEscape(StringUtil.escape(str));
        return span(node.getTypeName() + "(" + str.length() + ")", "type") +
                " " + span("\"" + escaped + "\"", "string");
    }

    private String formatNumber(DumpNode node) {
        return span(node.getTypeName(), "type") + " " +
                span(String.valueOf(node.getValue()), "number");
    }

    private String formatBoolean(DumpNode node) {
        return span("boolean", "type") + " " +
                span(String.valueOf(node.getValue()), "keyword");
    }

    private String formatEnum(DumpNode node) {
        return span(node.getTypeName(), "type") + "." +
                span(String.valueOf(node.getValue()), "keyword");
    }

    private String formatArray(DumpNode node, int depth) {
        Map<String, DumpNode> children = node.getChildren();
        StringBuilder sb = new StringBuilder();

        sb.append(span("#" + node.getTypeName(), "type"));
        sb.append(span("[" + children.size() + "]", "structural"));

        if (children.isEmpty()) {
            sb.append(" ").append(span("{}", "structural"));
            return sb.toString();
        }

        sb.append(" ").append(span("{", "structural")).append("\n");

        int index = 0;
        int maxItems = Math.min(children.size(), config.getMaxItems());

        for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
            if (index >= maxItems) {
                sb.append(indent(depth + 1));
                sb.append(span("... " + (children.size() - maxItems) +
                        " more items", "dim"));
                sb.append("\n");
                break;
            }

            sb.append(indent(depth + 1));
            sb.append(span(entry.getKey(), "dim"));
            sb.append(span(" → ", "dim"));
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
            index++;
        }

        sb.append(indent(depth)).append(span("}", "structural"));
        return sb.toString();
    }

    private String formatMap(DumpNode node, int depth) {
        Map<String, DumpNode> children = node.getChildren();
        StringBuilder sb = new StringBuilder();

        sb.append(span("#" + node.getTypeName(), "type"));
        sb.append(span("[" + children.size() + "]", "structural"));

        if (children.isEmpty()) {
            sb.append(" ").append(span("{}", "structural"));
            return sb.toString();
        }

        sb.append(" ").append(span("{", "structural")).append("\n");

        int index = 0;
        int maxItems = Math.min(children.size(), config.getMaxItems());

        for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
            if (index >= maxItems) {
                sb.append(indent(depth + 1));
                sb.append(span("... " + (children.size() - maxItems) +
                        " more entries", "dim"));
                sb.append("\n");
                break;
            }

            sb.append(indent(depth + 1));
            String key = entry.getKey();
            if (key.startsWith("\"") && key.endsWith("\"")) {
                sb.append(span(key, "string"));
            } else {
                sb.append(span(key, "keyword"));
            }
            sb.append(span(" → ", "dim"));
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
            index++;
        }

        sb.append(indent(depth)).append(span("}", "structural"));
        return sb.toString();
    }

    private String formatObject(DumpNode node, int depth) {
        Map<String, DumpNode> children = node.getChildren();
        StringBuilder sb = new StringBuilder();

        sb.append(span("#" + node.getTypeName(), "type"));

        if (children.isEmpty()) {
            sb.append(" ").append(span("{}", "structural"));
            return sb.toString();
        }

        sb.append(" ").append(span("{", "structural")).append("\n");

        for (Map.Entry<String, DumpNode> entry : children.entrySet()) {
            sb.append(indent(depth + 1));
            sb.append(span(entry.getKey(), "field"));
            sb.append(span(": ", "structural"));
            sb.append(formatNode(entry.getValue(), depth + 1));
            sb.append("\n");
        }

        sb.append(indent(depth)).append(span("}", "structural"));
        return sb.toString();
    }

    private String span(String text, String className) {
        return "<span class=\"" + className + "\">" +
                htmlEscape(text) + "</span>";
    }

    private String indent(int depth) {
        return "<span class=\"indent\">" +
                StringUtil.repeat("  ", depth) + "</span>";
    }

    private String htmlEscape(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String getHtmlHeader() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>JavaDump Output</title>\n" +
                "<style>\n" + getCSS() + "</style>\n" +
                "</head>\n" +
                "<body>\n";
    }

    private String getHtmlFooter() {
        return "</body>\n</html>";
    }

    private String getCSS() {
        return """
            body {
                background: #1e1e1e;
                color: #d4d4d4;
                font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
                font-size: 14px;
                line-height: 1.6;
                padding: 20px;
                margin: 0;
            }
            .dump-output {
                background: #252526;
                border: 1px solid #3c3c3c;
                border-radius: 4px;
                padding: 16px;
                white-space: pre-wrap;
                word-wrap: break-word;
            }
            .type {
                color: #ce9178;
                font-weight: bold;
            }
            .string {
                color: #ce9178;
            }
            .number {
                color: #b5cea8;
            }
            .keyword {
                color: #569cd6;
            }
            .field {
                color: #9cdcfe;
            }
            .structural {
                color: #808080;
            }
            .dim {
                color: #6a6a6a;
            }
            .error {
                color: #f48771;
            }
            .indent {
                color: #3c3c3c;
            }
            """;
    }
}