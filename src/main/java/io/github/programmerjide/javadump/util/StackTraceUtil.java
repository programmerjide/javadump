package io.github.programmerjide.javadump.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for stack trace analysis and call site detection.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public final class StackTraceUtil {

    private static final String JAVADUMP_PACKAGE = "io.github.programmerjide.javadump";
    private static final String UNKNOWN_FILE = "Unknown";
    private static final int UNKNOWN_LINE = 0;

    private StackTraceUtil() {
        throw new AssertionError("Utility class");
    }

    /**
     * Represents a call site location in source code.
     */
    public static class CallSite {
        private final String fileName;
        private final String methodName;
        private final int lineNumber;
        private final String className;

        public CallSite(String fileName, String methodName, int lineNumber) {
            this(fileName, methodName, lineNumber, "Unknown");
        }

        public CallSite(String fileName, String methodName, int lineNumber, String className) {
            this.fileName = fileName;
            this.methodName = methodName;
            this.lineNumber = lineNumber;
            this.className = className;
        }

        public String getFileName() { return fileName; }
        public String getMethodName() { return methodName; }
        public int getLineNumber() { return lineNumber; }
        public String getClassName() { return className; }

        @Override
        public String toString() {
            return String.format("%s:%d", fileName, lineNumber);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CallSite callSite = (CallSite) o;
            return lineNumber == callSite.lineNumber &&
                    Objects.equals(fileName, callSite.fileName) &&
                    Objects.equals(methodName, callSite.methodName) &&
                    Objects.equals(className, callSite.className);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fileName, methodName, lineNumber, className);
        }
    }

    /**
     * Finds the call site where dump() was called.
     */
    public static CallSite findCallSite() {
        Optional<StackTraceElement> element = getCallSite();

        if (element.isEmpty()) {
            return new CallSite(UNKNOWN_FILE, "unknown", UNKNOWN_LINE);
        }

        StackTraceElement e = element.get();
        return new CallSite(
                e.getFileName() != null ? e.getFileName() : UNKNOWN_FILE,
                e.getMethodName(),
                e.getLineNumber() > 0 ? e.getLineNumber() : UNKNOWN_LINE,
                e.getClassName()
        );
    }

    /**
     * Gets the call site with no additional frames to skip.
     */
    public static Optional<StackTraceElement> getCallSite() {
        return getCallSite(0);
    }

    /**
     * Gets the call site, skipping additional frames.
     */
    public static Optional<StackTraceElement> getCallSite(int skipAdditionalFrames) {
        if (skipAdditionalFrames < 0) {
            throw new IllegalArgumentException("skipAdditionalFrames must be non-negative");
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int skipped = 0;

        for (StackTraceElement frame : stackTrace) {
            if (isInternalFrame(frame) || isJavaInternalFrame(frame) || isTestFrame(frame)) {
                continue;
            }

            if (skipped < skipAdditionalFrames) {
                skipped++;
                continue;
            }

            return Optional.of(frame);
        }

        return Optional.empty();
    }

    /**
     * Checks if a frame is from JavaDump internals.
     */
    public static boolean isInternalFrame(StackTraceElement frame) {
        if (frame == null) {
            return false;
        }
        return frame.getClassName().startsWith(JAVADUMP_PACKAGE);
    }

    /**
     * Checks if a frame is from test frameworks.
     */
    public static boolean isTestFrame(StackTraceElement frame) {
        if (frame == null) {
            return false;
        }
        String className = frame.getClassName();
        return className.startsWith("org.junit.") ||
                className.startsWith("org.assertj.") ||
                className.startsWith("org.testng.") ||
                className.startsWith("org.mockito.");
    }

    /**
     * Checks if a frame is from Java internals.
     */
    private static boolean isJavaInternalFrame(StackTraceElement frame) {
        if (frame == null) {
            return false;
        }
        String className = frame.getClassName();
        return className.startsWith("java.lang.Thread") ||
                className.startsWith("java.lang.reflect.") ||
                className.startsWith("jdk.internal.");
    }

    /**
     * Formats a call site as "filename:line".
     */
    public static String formatCallSite(StackTraceElement element) {
        if (element == null) {
            return UNKNOWN_FILE + ":" + UNKNOWN_LINE;
        }

        String fileName = element.getFileName();
        int lineNumber = element.getLineNumber();

        if (fileName == null) {
            fileName = UNKNOWN_FILE;
        }
        if (lineNumber < 0) {
            lineNumber = UNKNOWN_LINE;
        }

        return fileName + ":" + lineNumber;
    }

    /**
     * Formats call site with full class and method details.
     */
    public static String formatCallSiteDetailed(StackTraceElement element) {
        if (element == null) {
            return UNKNOWN_FILE + ":" + UNKNOWN_LINE;
        }

        return String.format("%s.%s(%s:%d)",
                element.getClassName(),
                element.getMethodName(),
                element.getFileName() != null ? element.getFileName() : UNKNOWN_FILE,
                Math.max(0, element.getLineNumber()));
    }

    /**
     * Formats call site with simple class name.
     */
    public static String formatCallSiteSimple(StackTraceElement element) {
        if (element == null) {
            return UNKNOWN_FILE + ":" + UNKNOWN_LINE;
        }

        String className = element.getClassName();
        String simpleName = className.substring(className.lastIndexOf('.') + 1);

        return String.format("%s.%s(%s:%d)",
                simpleName,
                element.getMethodName(),
                element.getFileName() != null ? element.getFileName() : UNKNOWN_FILE,
                Math.max(0, element.getLineNumber()));
    }

    /**
     * Gets the file name from a stack trace element.
     */
    public static String getFileName(StackTraceElement element) {
        if (element == null || element.getFileName() == null) {
            return UNKNOWN_FILE;
        }
        return element.getFileName();
    }

    /**
     * Gets the line number from a stack trace element.
     */
    public static int getLineNumber(StackTraceElement element) {
        if (element == null || element.getLineNumber() < 0) {
            return UNKNOWN_LINE;
        }
        return element.getLineNumber();
    }

    /**
     * Gets the caller class name.
     */
    public static Optional<String> getCallerClassName() {
        return getCallSite().map(StackTraceElement::getClassName);
    }

    /**
     * Gets the caller method name.
     */
    public static Optional<String> getCallerMethodName() {
        return getCallSite().map(StackTraceElement::getMethodName);
    }

    /**
     * Gets the current stack trace excluding internal methods.
     */
    public static StackTraceElement[] getCurrentStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return Arrays.stream(stackTrace)
                .skip(2) // Skip getStackTrace() and this method
                .toArray(StackTraceElement[]::new);
    }

    /**
     * Gets the stack trace as a formatted string.
     */
    public static String getStackTraceString() {
        return Arrays.stream(getCurrentStackTrace())
                .map(e -> "    at " + formatCallSiteDetailed(e))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Filters out internal frames from stack trace.
     */
    public static StackTraceElement[] filterExternalFrames(StackTraceElement[] stackTrace) {
        if (stackTrace == null || stackTrace.length == 0) {
            return new StackTraceElement[0];
        }

        return Arrays.stream(stackTrace)
                .filter(frame -> !isInternalFrame(frame))
                .filter(frame -> !isJavaInternalFrame(frame))
                .toArray(StackTraceElement[]::new);
    }

    /**
     * Checks if current execution is called from a specific class.
     */
    public static boolean isCalledFrom(String className) {
        if (className == null || className.isEmpty()) {
            return false;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return Arrays.stream(stackTrace)
                .anyMatch(frame -> frame.getClassName().equals(className));
    }

    /**
     * Checks if current execution is called from a specific package.
     */
    public static boolean isCalledFromPackage(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return Arrays.stream(stackTrace)
                .anyMatch(frame -> frame.getClassName().startsWith(packageName));
    }

    /**
     * Gets the stack depth (excluding internal frames).
     */
    public static int getStackDepth() {
        return filterExternalFrames(getCurrentStackTrace()).length;
    }

    /**
     * Checks if a call site is valid.
     */
    public static boolean isValidCallSite(Optional<StackTraceElement> callSite) {
        if (callSite.isEmpty()) {
            return false;
        }

        StackTraceElement element = callSite.get();
        return element.getFileName() != null && element.getLineNumber() > 0;
    }

    /**
     * Gets the element or returns a default.
     */
    public static StackTraceElement getOrDefault(Optional<StackTraceElement> optional) {
        return optional.orElseGet(() ->
                new StackTraceElement("Unknown", "unknown", UNKNOWN_FILE, UNKNOWN_LINE));
    }

    /**
     * Gets a summary of the call stack.
     */
    public static String getCallStackSummary() {
        StackTraceElement[] stackTrace = getCurrentStackTrace();
        StackTraceElement[] external = filterExternalFrames(stackTrace);
        int internal = stackTrace.length - external.length;

        Optional<StackTraceElement> callSite = getCallSite();

        return String.format(
                "Call Stack Summary:\n" +
                        "  Total frames: %d\n" +
                        "  Internal frames: %d\n" +
                        "  External frames: %d\n" +
                        "  Call site: %s",
                stackTrace.length,
                internal,
                external.length,
                callSite.map(StackTraceUtil::formatCallSite).orElse("Unknown")
        );
    }

    /**
     * Debug prints the current stack trace to stderr.
     */
    public static void debugPrintStackTrace() {
        System.err.println("=== Stack Trace ===");
        System.err.println(getStackTraceString());
        System.err.println("===================");
    }
}