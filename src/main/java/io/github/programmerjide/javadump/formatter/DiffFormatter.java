package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.analyzer.DiffAnalyzer;
import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.util.ColorUtil;
import io.github.programmerjide.javadump.util.StackTraceUtil;

/**
 * Formats diff results for console output.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class DiffFormatter {

    private final DumperConfig config;
    private final ValueFormatter valueFormatter;

    public DiffFormatter(DumperConfig config) {
        this.config = config;
        this.valueFormatter = new ValueFormatter(config);
    }

    /**
     * Formats a diff result for console output.
     *
     * @param result the diff result
     * @param callSite the call site information
     * @return formatted diff string
     */
    public String format(DiffAnalyzer.DiffResult result,
                         StackTraceUtil.CallSite callSite) {
        if (result == null) {
            if (config.isColorEnabled()) {
                return ColorUtil.dim("(no diff result)");
            } else {
                return "(no diff result)";
            }
        }

        StringBuilder sb = new StringBuilder();

        // Header
        if (callSite != null && config.isColorEnabled()) {
            sb.append("\n");
            sb.append(ColorUtil.dim("<#diff // " + callSite.getFileName() +
                    ":" + callSite.getLineNumber()));
            sb.append("\n");
        }

        // No changes
        if (!result.hasChanges()) {
            if (config.isColorEnabled()) {
                sb.append(ColorUtil.dim("(no differences)"));
            } else {
                sb.append("(no differences)");
            }
            return sb.toString();
        }

        // Removals
        for (DiffAnalyzer.DiffEntry entry : result.getRemovals()) {
            sb.append(formatRemoval(entry));
        }

        // Additions
        for (DiffAnalyzer.DiffEntry entry : result.getAdditions()) {
            sb.append(formatAddition(entry));
        }

        // Changes
        for (DiffAnalyzer.DiffEntry entry : result.getChanges()) {
            sb.append(formatChange(entry));
        }

        return sb.toString();
    }

    private String formatRemoval(DiffAnalyzer.DiffEntry entry) {
        StringBuilder sb = new StringBuilder();
        String prefix = config.isColorEnabled() ?
                ColorUtil.red("- ") : "- ";

        String content = valueFormatter.format(entry.getBefore(), 0);

        // Add prefix to each line
        for (String line : content.split("\n")) {
            sb.append(prefix).append(line).append("\n");
        }

        return sb.toString();
    }

    private String formatAddition(DiffAnalyzer.DiffEntry entry) {
        StringBuilder sb = new StringBuilder();
        String prefix = config.isColorEnabled() ?
                ColorUtil.green("+ ") : "+ ";

        String content = valueFormatter.format(entry.getAfter(), 0);

        // Add prefix to each line
        for (String line : content.split("\n")) {
            sb.append(prefix).append(line).append("\n");
        }

        return sb.toString();
    }

    private String formatChange(DiffAnalyzer.DiffEntry entry) {
        // Show both removal and addition
        return formatRemoval(entry) + formatAddition(entry);
    }

    /**
     * Formats a diff result as HTML.
     */
    public String formatHTML(DiffAnalyzer.DiffResult result,
                             StackTraceUtil.CallSite callSite) {
        StringBuilder sb = new StringBuilder();

        sb.append(getHtmlHeader());
        sb.append("<div class=\"diff-output\">\n");

        // Handle null result
        if (result == null) {
            sb.append("<div class=\"diff-no-changes\">(no diff result)</div>\n");
            sb.append("</div>\n");
            sb.append(getHtmlFooter());
            return sb.toString();
        }

        // Header
        if (callSite != null) {
            sb.append("<div class=\"diff-header\">");
            sb.append("&lt;#diff // ")
                    .append(htmlEscape(callSite.getFileName()))
                    .append(":")
                    .append(callSite.getLineNumber());
            sb.append("</div>\n");
        }

        if (!result.hasChanges()) {
            sb.append("<div class=\"diff-no-changes\">(no differences)</div>\n");
        } else {
            // Removals
            for (DiffAnalyzer.DiffEntry entry : result.getRemovals()) {
                sb.append(formatHTMLRemoval(entry));
            }

            // Additions
            for (DiffAnalyzer.DiffEntry entry : result.getAdditions()) {
                sb.append(formatHTMLAddition(entry));
            }

            // Changes
            for (DiffAnalyzer.DiffEntry entry : result.getChanges()) {
                sb.append(formatHTMLRemoval(entry));
                sb.append(formatHTMLAddition(entry));
            }
        }

        sb.append("</div>\n");
        sb.append(getHtmlFooter());

        return sb.toString();
    }

    private String formatHTMLRemoval(DiffAnalyzer.DiffEntry entry) {
        String content = valueFormatter.format(entry.getBefore(), 0);
        return "<div class=\"diff-removal\">- " +
                htmlEscape(content) + "</div>\n";
    }

    private String formatHTMLAddition(DiffAnalyzer.DiffEntry entry) {
        String content = valueFormatter.format(entry.getAfter(), 0);
        return "<div class=\"diff-addition\">+ " +
                htmlEscape(content) + "</div>\n";
    }

    private String htmlEscape(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private String getHtmlHeader() {
        return "<!DOCTYPE html>\n<html>\n<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>JavaDump Diff</title>\n" +
                "<style>\n" + getDiffCSS() + "</style>\n" +
                "</head>\n<body>\n";
    }

    private String getHtmlFooter() {
        return "</body>\n</html>";
    }

    private String getDiffCSS() {
        return """
            body {
                background: #1e1e1e;
                color: #d4d4d4;
                font-family: 'Consolas', 'Monaco', monospace;
                font-size: 14px;
                padding: 20px;
                margin: 0;
            }
            .diff-output {
                background: #252526;
                border: 1px solid #3c3c3c;
                border-radius: 4px;
                padding: 16px;
                white-space: pre-wrap;
            }
            .diff-header {
                color: #6a6a6a;
                margin-bottom: 10px;
            }
            .diff-removal {
                background: rgba(244, 135, 113, 0.2);
                color: #f48771;
                padding: 2px 0;
                margin: 1px 0;
            }
            .diff-addition {
                background: rgba(181, 206, 168, 0.2);
                color: #b5cea8;
                padding: 2px 0;
                margin: 1px 0;
            }
            .diff-no-changes {
                color: #6a6a6a;
                font-style: italic;
            }
            """;
    }
}
