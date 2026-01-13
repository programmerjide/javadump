package io.github.programmerjide.javadump.exception;

import io.github.programmerjide.javadump.core.Dump;
import io.github.programmerjide.javadump.core.Dumper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Dumps exceptions with full context including stack traces and related data.
 *
 * <p>Example:
 * <pre>{@code
 * try {
 *     processUser(user);
 * } catch (Exception e) {
 *     ExceptionDumper.dump(e, user, request);
 *     throw e;
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
public class ExceptionDumper {

    private ExceptionDumper() {
        throw new AssertionError("Utility class");
    }

    /**
     * Dumps an exception with context objects.
     *
     * @param exception the exception to dump
     * @param context additional context objects
     */
    public static void dump(Throwable exception, Object... context) {
        System.err.println("╔════════════════════════════════════════════════════");
        System.err.println("║ EXCEPTION DUMP");
        System.err.println("╠════════════════════════════════════════════════════");

        dumpException(exception);

        if (context != null && context.length > 0) {
            System.err.println("╠════════════════════════════════════════════════════");
            System.err.println("║ CONTEXT DATA");
            System.err.println("╠════════════════════════════════════════════════════");
            Dump.dump(context);
        }

        System.err.println("╚════════════════════════════════════════════════════");
    }

    /**
     * Dumps an exception with context using a custom dumper.
     */
    public static void dump(Dumper dumper, Throwable exception, Object... context) {
        System.err.println("╔════════════════════════════════════════════════════");
        System.err.println("║ EXCEPTION DUMP");
        System.err.println("╠════════════════════════════════════════════════════");

        dumpException(exception);

        if (context != null && context.length > 0) {
            System.err.println("╠════════════════════════════════════════════════════");
            System.err.println("║ CONTEXT DATA");
            System.err.println("╠════════════════════════════════════════════════════");
            dumper.dump(context);
        }

        System.err.println("╚════════════════════════════════════════════════════");
    }

    /**
     * Returns exception dump as string.
     */
    public static String dumpStr(Throwable exception, Object... context) {
        StringBuilder sb = new StringBuilder();

        sb.append("╔════════════════════════════════════════════════════\n");
        sb.append("║ EXCEPTION DUMP\n");
        sb.append("╠════════════════════════════════════════════════════\n");

        sb.append(getExceptionInfo(exception));

        if (context != null && context.length > 0) {
            sb.append("╠════════════════════════════════════════════════════\n");
            sb.append("║ CONTEXT DATA\n");
            sb.append("╠════════════════════════════════════════════════════\n");
            sb.append(Dump.dumpStr(context));
        }

        sb.append("╚════════════════════════════════════════════════════\n");

        return sb.toString();
    }

    /**
     * Returns exception dump as HTML.
     */
    public static String dumpHTML(Throwable exception, Object... context) {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>\n<html>\n<head>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<title>Exception Dump</title>\n");
        sb.append("<style>\n").append(getExceptionCSS()).append("</style>\n");
        sb.append("</head>\n<body>\n");

        sb.append("<div class=\"exception-container\">\n");
        sb.append("<h1>Exception Dump</h1>\n");

        sb.append("<div class=\"exception-info\">\n");
        sb.append("<h2>Exception Details</h2>\n");
        sb.append("<div class=\"exception-type\">").append(exception.getClass().getName()).append("</div>\n");
        sb.append("<div class=\"exception-message\">").append(htmlEscape(exception.getMessage())).append("</div>\n");
        sb.append("</div>\n");

        sb.append("<div class=\"stack-trace\">\n");
        sb.append("<h2>Stack Trace</h2>\n");
        sb.append("<pre>").append(htmlEscape(getStackTraceString(exception))).append("</pre>\n");
        sb.append("</div>\n");

        if (context != null && context.length > 0) {
            sb.append("<div class=\"context-data\">\n");
            sb.append("<h2>Context Data</h2>\n");
            sb.append(Dump.dumpHTML(context));
            sb.append("</div>\n");
        }

        sb.append("</div>\n");
        sb.append("</body>\n</html>");

        return sb.toString();
    }

    /**
     * Analyzes an exception and returns structured data.
     */
    public static ExceptionContext analyze(Throwable exception) {
        return new ExceptionContext(exception);
    }

    // ==================== Helper Methods ====================

    private static void dumpException(Throwable exception) {
        System.err.println("Type: " + exception.getClass().getName());
        System.err.println("Message: " + exception.getMessage());
        System.err.println();
        System.err.println("Stack Trace:");
        exception.printStackTrace(System.err);

        if (exception.getCause() != null) {
            System.err.println();
            System.err.println("Caused by:");
            dumpException(exception.getCause());
        }
    }

    private static String getExceptionInfo(Throwable exception) {
        StringBuilder sb = new StringBuilder();
        sb.append("Type: ").append(exception.getClass().getName()).append("\n");
        sb.append("Message: ").append(exception.getMessage()).append("\n\n");
        sb.append("Stack Trace:\n");
        sb.append(getStackTraceString(exception));

        if (exception.getCause() != null) {
            sb.append("\nCaused by:\n");
            sb.append(getExceptionInfo(exception.getCause()));
        }

        return sb.toString();
    }

    private static String getStackTraceString(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

    private static String htmlEscape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static String getExceptionCSS() {
        return """
            body {
                background: #1e1e1e;
                color: #d4d4d4;
                font-family: 'Consolas', 'Monaco', monospace;
                padding: 20px;
            }
            .exception-container {
                background: #252526;
                border: 2px solid #f48771;
                border-radius: 8px;
                padding: 20px;
            }
            h1 {
                color: #f48771;
                margin-top: 0;
            }
            h2 {
                color: #569cd6;
                border-bottom: 1px solid #3c3c3c;
                padding-bottom: 10px;
            }
            .exception-type {
                color: #ce9178;
                font-weight: bold;
                font-size: 18px;
            }
            .exception-message {
                color: #f48771;
                margin: 10px 0;
                padding: 10px;
                background: rgba(244, 135, 113, 0.1);
                border-left: 3px solid #f48771;
            }
            .stack-trace pre {
                background: #1e1e1e;
                padding: 15px;
                border-radius: 4px;
                overflow-x: auto;
                color: #808080;
            }
            .context-data {
                margin-top: 20px;
            }
            """;
    }

    // ==================== Exception Context ====================

    /**
     * Structured exception context data.
     */
    public static class ExceptionContext {
        private final Throwable exception;
        private final String type;
        private final String message;
        private final List<StackTraceElement> stackTrace;
        private final ExceptionContext cause;

        public ExceptionContext(Throwable exception) {
            this.exception = exception;
            this.type = exception.getClass().getName();
            this.message = exception.getMessage();
            this.stackTrace = Arrays.asList(exception.getStackTrace());
            this.cause = exception.getCause() != null ?
                    new ExceptionContext(exception.getCause()) : null;
        }

        public Throwable getException() { return exception; }
        public String getType() { return type; }
        public String getMessage() { return message; }
        public List<StackTraceElement> getStackTrace() { return stackTrace; }
        public ExceptionContext getCause() { return cause; }

        public boolean hasCause() {
            return cause != null;
        }

        public int getStackDepth() {
            return stackTrace.size();
        }

        public Optional<StackTraceElement> getRootCause() {
            if (stackTrace.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(stackTrace.get(0));
        }
    }
}