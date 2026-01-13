package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.ColorUtil;
import io.github.programmerjide.javadump.util.StackTraceUtil;

import java.util.List;

/**
 * Formats DumpNode trees into colorized console output.
 */
public class ConsoleFormatter {

    private final DumperConfig config;
    private final ValueFormatter valueFormatter;

    public ConsoleFormatter(DumperConfig config) {
        this.config = config;
        this.valueFormatter = new ValueFormatter(config);
    }

    public String format(List<DumpNode> nodes, StackTraceUtil.CallSite callSite) {
        StringBuilder sb = new StringBuilder();

        // Add call site header if available
        if (callSite != null && config.isColorEnabled()) {
            sb.append("\n");
            sb.append(formatCallSite(callSite));
            sb.append("\n");
        }

        // Format each node
        for (int i = 0; i < nodes.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(valueFormatter.format(nodes.get(i), 0));
        }

        return sb.toString();
    }

    public String formatCallSite(StackTraceUtil.CallSite callSite) {
        if (!config.isColorEnabled()) {
            return String.format("%s:%d",
                    callSite.getFileName(),
                    callSite.getLineNumber());
        }

        return ColorUtil.dim(
                String.format("%s:%d",
                        callSite.getFileName(),
                        callSite.getLineNumber())
        );
    }

    public ValueFormatter getValueFormatter() {
        return valueFormatter;
    }
}