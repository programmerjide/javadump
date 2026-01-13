package io.github.programmerjide.javadump.core;

import io.github.programmerjide.javadump.analyzer.DiffAnalyzer;
import io.github.programmerjide.javadump.analyzer.ObjectAnalyzer;
import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.formatter.*;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.StackTraceUtil;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Configurable dumper instance that handles object analysis and formatting.
 *
 * <p>This class coordinates the entire dump process:
 * <ul>
 *   <li>Analyzes objects using {@link ObjectAnalyzer}</li>
 *   <li>Formats output using various formatters</li>
 *   <li>Handles call site detection</li>
 *   <li>Supports multiple output streams</li>
 *   <li>Provides diff functionality</li>
 * </ul>
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class Dumper {

    private final DumperConfig config;
    private final ObjectAnalyzer analyzer;
    private final ConsoleFormatter consoleFormatter;
    private final JsonFormatter jsonFormatter;
    private final HtmlFormatter htmlFormatter;
    private final DiffAnalyzer diffAnalyzer;
    private final DiffFormatter diffFormatter;
    private final PrintStream outputStream;

    /**
     * Creates a new Dumper with the specified configuration.
     *
     * @param config the dumper configuration
     */
    public Dumper(DumperConfig config) {
        this(config, System.out);
    }

    /**
     * Creates a new Dumper with the specified configuration and output stream.
     *
     * @param config the dumper configuration
     * @param outputStream the output stream to write to
     */
    public Dumper(DumperConfig config, PrintStream outputStream) {
        this.config = config;
        this.analyzer = new ObjectAnalyzer(config);
        this.consoleFormatter = new ConsoleFormatter(config);
        this.jsonFormatter = new JsonFormatter(config);
        this.htmlFormatter = new HtmlFormatter(config);
        this.diffAnalyzer = new DiffAnalyzer(config);
        this.diffFormatter = new DiffFormatter(config);
        this.outputStream = outputStream;
    }

    /**
     * Creates a new builder for constructing Dumper instances.
     *
     * @return a new DumperBuilder
     */
    public static DumperBuilder builder() {
        return new DumperBuilder();
    }

    // ==================== Console Output ====================

    /**
     * Dumps the given values to the configured output stream with colorized output.
     */
    public void dump(Object... values) {
        String output = dumpStr(values);
        outputStream.println(output);
    }

    /**
     * Dumps the given values and exits the program with status code 0.
     */
    public void dd(Object... values) {
        dump(values);
        System.exit(0);
    }

    /**
     * Returns a string representation of the dumped values.
     */
    public String dumpStr(Object... values) {
        if (values == null || values.length == 0) {
            return formatEmpty();
        }

        StackTraceUtil.CallSite callSite = StackTraceUtil.findCallSite();
        List<DumpNode> nodes = analyzeValues(values);
        return consoleFormatter.format(nodes, callSite);
    }

    // ==================== JSON Output ====================

    /**
     * Dumps the given values as pretty-printed JSON to the output stream.
     */
    public void dumpJSON(Object... values) {
        String json = dumpJSONStr(values);
        outputStream.println(json);
    }

    /**
     * Returns a JSON string representation of the dumped values.
     */
    public String dumpJSONStr(Object... values) {
        if (values == null || values.length == 0) {
            return "null";
        }

        List<DumpNode> nodes = analyzeValues(values);

        if (nodes.size() == 1) {
            return jsonFormatter.format(nodes.get(0));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < nodes.size(); i++) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append("  ").append(jsonFormatter.format(nodes.get(i)));
        }
        sb.append("\n]");
        return sb.toString();
    }

    // ==================== HTML Output ====================

    /**
     * Returns an HTML representation of the dumped values.
     */
    public String dumpHTML(Object... values) {
        if (values == null || values.length == 0) {
            return htmlFormatter.format(null);
        }

        List<DumpNode> nodes = analyzeValues(values);

        if (nodes.size() == 1) {
            return htmlFormatter.format(nodes.get(0));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n<head>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<title>JavaDump Output</title>\n");
        sb.append("<style>\n").append(getHtmlCSS()).append("</style>\n");
        sb.append("</head>\n<body>\n");

        for (int i = 0; i < nodes.size(); i++) {
            if (i > 0) {
                sb.append("<hr class=\"dump-separator\">\n");
            }
            sb.append(htmlFormatter.formatFragment(nodes.get(i)));
        }

        sb.append("</body>\n</html>");
        return sb.toString();
    }

    // ==================== Diff Output ====================

    /**
     * Compares two objects and prints the differences to the output stream.
     */
    public void diff(Object before, Object after) {
        String output = diffStr(before, after);
        outputStream.println(output);
    }

    /**
     * Compares two objects and returns the differences as a string.
     */
    public String diffStr(Object before, Object after) {
        StackTraceUtil.CallSite callSite = StackTraceUtil.findCallSite();
        DiffAnalyzer.DiffResult result = diffAnalyzer.analyze(before, after);
        return diffFormatter.format(result, callSite);
    }

    /**
     * Compares two objects and returns the differences as HTML.
     */
    public String diffHTML(Object before, Object after) {
        StackTraceUtil.CallSite callSite = StackTraceUtil.findCallSite();
        DiffAnalyzer.DiffResult result = diffAnalyzer.analyze(before, after);
        return diffFormatter.formatHTML(result, callSite);
    }

    // ==================== Helper Methods ====================

    private List<DumpNode> analyzeValues(Object... values) {
        List<DumpNode> nodes = new ArrayList<>();
        for (Object value : values) {
            nodes.add(analyzer.analyze(value, 0));
        }
        return nodes;
    }

    private String formatEmpty() {
        StackTraceUtil.CallSite callSite = StackTraceUtil.findCallSite();
        StringBuilder sb = new StringBuilder();

        if (callSite != null && config.isColorEnabled() && config.isShowHeader()) {
            sb.append("\n")
                    .append(consoleFormatter.formatCallSite(callSite))
                    .append("\n");
        }

        sb.append("(no values to dump)");
        return sb.toString();
    }

    private String getHtmlCSS() {
        return """
            body {
                background: #1e1e1e;
                color: #d4d4d4;
                font-family: 'Consolas', 'Monaco', monospace;
                font-size: 14px;
                padding: 20px;
            }
            .dump-separator {
                border: none;
                border-top: 1px solid #3c3c3c;
                margin: 20px 0;
            }
            """;
    }

    // ==================== Getters ====================

    public DumperConfig getConfig() {
        return config;
    }

    public PrintStream getOutputStream() {
        return outputStream;
    }
}