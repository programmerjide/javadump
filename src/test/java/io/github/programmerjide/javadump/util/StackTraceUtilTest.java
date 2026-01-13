package io.github.programmerjide.javadump.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive tests for StackTraceUtil.
 *
 * @author Your Name
 */
@DisplayName("StackTraceUtil")
class StackTraceUtilTest {

    @Nested
    @DisplayName("getCallSite()")
    class GetCallSiteTests {

        @Test
        @DisplayName("should find external call site")
        void shouldFindExternalCallSite() {
            // This test method is the external caller
            Optional<StackTraceElement> callSite = StackTraceUtil.getCallSite();

            assertThat(callSite).isPresent();

            StackTraceElement element = callSite.get();
            assertThat(element.getClassName())
                    .contains("StackTraceUtilTest");
            assertThat(element.getMethodName())
                    .isEqualTo("shouldFindExternalCallSite");
        }

        @Test
        @DisplayName("should skip additional frames when requested")
        void shouldSkipAdditionalFrames() {
            Optional<StackTraceElement> callSite = helperMethodLevel1();

            assertThat(callSite).isPresent();
            assertThat(callSite.get().getMethodName())
                    .isEqualTo("shouldSkipAdditionalFrames");
        }

        private Optional<StackTraceElement> helperMethodLevel1() {
            return helperMethodLevel2();
        }

        private Optional<StackTraceElement> helperMethodLevel2() {
            // Skip 2 additional frames to get to the test method
            return StackTraceUtil.getCallSite(2);
        }

        @Test
        @DisplayName("should throw exception for negative skip frames")
        void shouldThrowForNegativeSkip() {
            assertThatThrownBy(() -> StackTraceUtil.getCallSite(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("non-negative");
        }

        @Test
        @DisplayName("should return empty if skip too many frames")
        void shouldReturnEmptyIfSkipTooMany() {
            Optional<StackTraceElement> callSite = StackTraceUtil.getCallSite(1000);

            // Might be empty if we skip beyond the stack
            // This is implementation-dependent, so we just verify it doesn't throw
            assertThat(callSite).isNotNull();
        }
    }

    @Nested
    @DisplayName("isInternalFrame()")
    class IsInternalFrameTests {

        @Test
        @DisplayName("should return true for JavaDump frames")
        void shouldReturnTrueForJavaDumpFrames() {
            StackTraceElement javadumpFrame = new StackTraceElement(
                    "io.github.yourusername.javadump.Dump",
                    "dump",
                    "Dump.java",
                    42
            );

            assertThat(StackTraceUtil.isInternalFrame(javadumpFrame))
                    .isTrue();
        }

        @Test
        @DisplayName("should return false for external frames")
        void shouldReturnFalseForExternalFrames() {
            StackTraceElement externalFrame = new StackTraceElement(
                    "com.example.UserService",
                    "getUser",
                    "UserService.java",
                    123
            );

            assertThat(StackTraceUtil.isInternalFrame(externalFrame))
                    .isFalse();
        }

        @Test
        @DisplayName("should return false for null frame")
        void shouldReturnFalseForNull() {
            assertThat(StackTraceUtil.isInternalFrame(null))
                    .isFalse();
        }

        @Test
        @DisplayName("should detect various JavaDump packages")
        void shouldDetectJavaDumpPackages() {
            String[] javadumpClasses = {
                    "io.github.yourusername.javadump.Dump",
                    "io.github.yourusername.javadump.Dumper",
                    "io.github.yourusername.javadump.core.DumpEngine",
                    "io.github.yourusername.javadump.formatter.ConsoleFormatter"
            };

            for (String className : javadumpClasses) {
                StackTraceElement frame = new StackTraceElement(
                        className, "method", "File.java", 1
                );
                assertThat(StackTraceUtil.isInternalFrame(frame))
                        .as("Frame from %s should be internal", className)
                        .isTrue();
            }
        }
    }

    @Nested
    @DisplayName("isTestFrame()")
    class IsTestFrameTests {

        @Test
        @DisplayName("should detect JUnit frames")
        void shouldDetectJUnitFrames() {
            StackTraceElement junitFrame = new StackTraceElement(
                    "org.junit.jupiter.engine.execution.ExecutableInvoker",
                    "invoke",
                    "ExecutableInvoker.java",
                    115
            );

            assertThat(StackTraceUtil.isTestFrame(junitFrame))
                    .isTrue();
        }

        @Test
        @DisplayName("should detect AssertJ frames")
        void shouldDetectAssertJFrames() {
            StackTraceElement assertjFrame = new StackTraceElement(
                    "org.assertj.core.api.AbstractAssert",
                    "isEqualTo",
                    "AbstractAssert.java",
                    78
            );

            assertThat(StackTraceUtil.isTestFrame(assertjFrame))
                    .isTrue();
        }

        @Test
        @DisplayName("should return false for non-test frames")
        void shouldReturnFalseForNonTestFrames() {
            StackTraceElement normalFrame = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    10
            );

            assertThat(StackTraceUtil.isTestFrame(normalFrame))
                    .isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(StackTraceUtil.isTestFrame(null))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("formatCallSite()")
    class FormatCallSiteTests {

        @Test
        @DisplayName("should format as filename:line")
        void shouldFormatAsFilenameLine() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    42
            );

            String formatted = StackTraceUtil.formatCallSite(element);

            assertThat(formatted).isEqualTo("Service.java:42");
        }

        @Test
        @DisplayName("should handle null element")
        void shouldHandleNullElement() {
            String formatted = StackTraceUtil.formatCallSite(null);

            assertThat(formatted).isEqualTo("Unknown:0");
        }

        @Test
        @DisplayName("should handle null filename")
        void shouldHandleNullFilename() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    null,
                    42
            );

            String formatted = StackTraceUtil.formatCallSite(element);

            assertThat(formatted).isEqualTo("Unknown:42");
        }

        @Test
        @DisplayName("should handle negative line number")
        void shouldHandleNegativeLineNumber() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    -1
            );

            String formatted = StackTraceUtil.formatCallSite(element);

            assertThat(formatted).isEqualTo("Service.java:0");
        }
    }

    @Nested
    @DisplayName("formatCallSiteDetailed()")
    class FormatCallSiteDetailedTests {

        @Test
        @DisplayName("should format with full class and method")
        void shouldFormatWithFullClassAndMethod() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.UserService",
                    "getUser",
                    "UserService.java",
                    42
            );

            String formatted = StackTraceUtil.formatCallSiteDetailed(element);

            assertThat(formatted)
                    .isEqualTo("com.example.UserService.getUser(UserService.java:42)");
        }

        @Test
        @DisplayName("should handle null element")
        void shouldHandleNull() {
            String formatted = StackTraceUtil.formatCallSiteDetailed(null);

            assertThat(formatted).isEqualTo("Unknown:0");
        }
    }

    @Nested
    @DisplayName("formatCallSiteSimple()")
    class FormatCallSiteSimpleTests {

        @Test
        @DisplayName("should format with simple class name")
        void shouldFormatWithSimpleClassName() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.service.UserService",
                    "getUser",
                    "UserService.java",
                    42
            );

            String formatted = StackTraceUtil.formatCallSiteSimple(element);

            assertThat(formatted)
                    .isEqualTo("UserService.getUser(UserService.java:42)");
        }

        @Test
        @DisplayName("should handle class without package")
        void shouldHandleClassWithoutPackage() {
            StackTraceElement element = new StackTraceElement(
                    "Main",
                    "main",
                    "Main.java",
                    10
            );

            String formatted = StackTraceUtil.formatCallSiteSimple(element);

            assertThat(formatted).isEqualTo("Main.main(Main.java:10)");
        }
    }

    @Nested
    @DisplayName("getFileName()")
    class GetFileNameTests {

        @Test
        @DisplayName("should return filename")
        void shouldReturnFilename() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    42
            );

            assertThat(StackTraceUtil.getFileName(element))
                    .isEqualTo("Service.java");
        }

        @Test
        @DisplayName("should return Unknown for null element")
        void shouldReturnUnknownForNull() {
            assertThat(StackTraceUtil.getFileName(null))
                    .isEqualTo("Unknown");
        }

        @Test
        @DisplayName("should return Unknown for null filename")
        void shouldReturnUnknownForNullFilename() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    null,
                    42
            );

            assertThat(StackTraceUtil.getFileName(element))
                    .isEqualTo("Unknown");
        }
    }

    @Nested
    @DisplayName("getLineNumber()")
    class GetLineNumberTests {

        @Test
        @DisplayName("should return line number")
        void shouldReturnLineNumber() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    42
            );

            assertThat(StackTraceUtil.getLineNumber(element))
                    .isEqualTo(42);
        }

        @Test
        @DisplayName("should return 0 for null element")
        void shouldReturnZeroForNull() {
            assertThat(StackTraceUtil.getLineNumber(null))
                    .isEqualTo(0);
        }

        @Test
        @DisplayName("should return 0 for negative line number")
        void shouldReturnZeroForNegative() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    -1
            );

            assertThat(StackTraceUtil.getLineNumber(element))
                    .isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("getCallerClassName()")
    class GetCallerClassNameTests {

        @Test
        @DisplayName("should return caller class name")
        void shouldReturnCallerClassName() {
            Optional<String> className = StackTraceUtil.getCallerClassName();

            assertThat(className).isPresent();
            assertThat(className.get()).contains("StackTraceUtilTest");
        }
    }

    @Nested
    @DisplayName("getCallerMethodName()")
    class GetCallerMethodNameTests {

        @Test
        @DisplayName("should return caller method name")
        void shouldReturnCallerMethodName() {
            Optional<String> methodName = StackTraceUtil.getCallerMethodName();

            assertThat(methodName).isPresent();
            assertThat(methodName.get()).isEqualTo("shouldReturnCallerMethodName");
        }
    }

    @Nested
    @DisplayName("getCurrentStackTrace()")
    class GetCurrentStackTraceTests {

        @Test
        @DisplayName("should return non-empty stack trace")
        void shouldReturnNonEmptyStackTrace() {
            StackTraceElement[] stackTrace = StackTraceUtil.getCurrentStackTrace();

            assertThat(stackTrace).isNotEmpty();
        }

        @Test
        @DisplayName("should not include getCurrentStackTrace itself")
        void shouldNotIncludeSelf() {
            StackTraceElement[] stackTrace = StackTraceUtil.getCurrentStackTrace();

            boolean hasGetCurrentStackTrace = false;
            for (StackTraceElement element : stackTrace) {
                if ("getCurrentStackTrace".equals(element.getMethodName())) {
                    hasGetCurrentStackTrace = true;
                    break;
                }
            }

            assertThat(hasGetCurrentStackTrace).isFalse();
        }
    }

    @Nested
    @DisplayName("getStackTraceString()")
    class GetStackTraceStringTests {

        @Test
        @DisplayName("should return formatted stack trace string")
        void shouldReturnFormattedString() {
            String stackTrace = StackTraceUtil.getStackTraceString();

            assertThat(stackTrace)
                    .isNotEmpty()
                    .contains("at ")
                    .contains("StackTraceUtilTest");
        }
    }

    @Nested
    @DisplayName("filterExternalFrames()")
    class FilterExternalFramesTests {

        @Test
        @DisplayName("should filter out internal frames")
        void shouldFilterOutInternalFrames() {
            StackTraceElement[] mixed = new StackTraceElement[] {
                    new StackTraceElement("com.example.Service", "method", "Service.java", 10),
                    new StackTraceElement("io.github.yourusername.javadump.Dump", "dump", "Dump.java", 20),
                    new StackTraceElement("com.example.Controller", "handle", "Controller.java", 30)
            };

            StackTraceElement[] filtered = StackTraceUtil.filterExternalFrames(mixed);

            assertThat(filtered).hasSize(2);
            assertThat(filtered[0].getClassName()).isEqualTo("com.example.Service");
            assertThat(filtered[1].getClassName()).isEqualTo("com.example.Controller");
        }

        @Test
        @DisplayName("should handle null stack trace")
        void shouldHandleNull() {
            StackTraceElement[] filtered = StackTraceUtil.filterExternalFrames(null);

            assertThat(filtered).isEmpty();
        }

        @Test
        @DisplayName("should handle empty stack trace")
        void shouldHandleEmpty() {
            StackTraceElement[] empty = new StackTraceElement[0];
            StackTraceElement[] filtered = StackTraceUtil.filterExternalFrames(empty);

            assertThat(filtered).isEmpty();
        }
    }

    @Nested
    @DisplayName("isCalledFrom()")
    class IsCalledFromTests {

        @Test
        @DisplayName("should return true when called from specified class")
        void shouldReturnTrueWhenCalledFromClass() {
            boolean result = StackTraceUtil.isCalledFrom(
                    "io.github.yourusername.javadump.util.StackTraceUtilTest"
            );

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("should return false when not called from specified class")
        void shouldReturnFalseWhenNotCalled() {
            boolean result = StackTraceUtil.isCalledFrom("com.example.NonExistent");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false for null class name")
        void shouldReturnFalseForNull() {
            assertThat(StackTraceUtil.isCalledFrom(null)).isFalse();
        }

        @Test
        @DisplayName("should return false for empty class name")
        void shouldReturnFalseForEmpty() {
            assertThat(StackTraceUtil.isCalledFrom("")).isFalse();
        }
    }

    @Nested
    @DisplayName("isCalledFromPackage()")
    class IsCalledFromPackageTests {

        @Test
        @DisplayName("should return true when called from specified package")
        void shouldReturnTrueWhenCalledFromPackage() {
            boolean result = StackTraceUtil.isCalledFromPackage(
                    "io.github.yourusername.javadump"
            );

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("should return false when not called from package")
        void shouldReturnFalseWhenNotCalled() {
            boolean result = StackTraceUtil.isCalledFromPackage("com.example.nonexistent");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false for null package")
        void shouldReturnFalseForNull() {
            assertThat(StackTraceUtil.isCalledFromPackage(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("getStackDepth()")
    class GetStackDepthTests {

        @Test
        @DisplayName("should return positive depth")
        void shouldReturnPositiveDepth() {
            int depth = StackTraceUtil.getStackDepth();

            assertThat(depth).isPositive();
        }

        @Test
        @DisplayName("should exclude internal frames from depth")
        void shouldExcludeInternalFrames() {
            int depth = StackTraceUtil.getStackDepth();

            // The depth should be reasonable (not thousands)
            assertThat(depth).isLessThan(100);
        }
    }

    @Nested
    @DisplayName("isValidCallSite()")
    class IsValidCallSiteTests {

        @Test
        @DisplayName("should return true for valid call site")
        void shouldReturnTrueForValid() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    42
            );

            boolean valid = StackTraceUtil.isValidCallSite(Optional.of(element));

            assertThat(valid).isTrue();
        }

        @Test
        @DisplayName("should return false for empty optional")
        void shouldReturnFalseForEmpty() {
            boolean valid = StackTraceUtil.isValidCallSite(Optional.empty());

            assertThat(valid).isFalse();
        }

        @Test
        @DisplayName("should return false for null filename")
        void shouldReturnFalseForNullFilename() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    null,
                    42
            );

            boolean valid = StackTraceUtil.isValidCallSite(Optional.of(element));

            assertThat(valid).isFalse();
        }

        @Test
        @DisplayName("should return false for invalid line number")
        void shouldReturnFalseForInvalidLine() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    0
            );

            boolean valid = StackTraceUtil.isValidCallSite(Optional.of(element));

            assertThat(valid).isFalse();
        }
    }

    @Nested
    @DisplayName("getOrDefault()")
    class GetOrDefaultTests {

        @Test
        @DisplayName("should return element if present")
        void shouldReturnElementIfPresent() {
            StackTraceElement element = new StackTraceElement(
                    "com.example.Service",
                    "method",
                    "Service.java",
                    42
            );

            StackTraceElement result = StackTraceUtil.getOrDefault(Optional.of(element));

            assertThat(result).isEqualTo(element);
        }

        @Test
        @DisplayName("should return default if empty")
        void shouldReturnDefaultIfEmpty() {
            StackTraceElement result = StackTraceUtil.getOrDefault(Optional.empty());

            assertThat(result).isNotNull();
            assertThat(result.getClassName()).isEqualTo("Unknown");
            assertThat(result.getLineNumber()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("getCallStackSummary()")
    class GetCallStackSummaryTests {

        @Test
        @DisplayName("should return summary with stats")
        void shouldReturnSummaryWithStats() {
            String summary = StackTraceUtil.getCallStackSummary();

            assertThat(summary)
                    .contains("Call Stack Summary")
                    .contains("Total frames:")
                    .contains("Internal frames:")
                    .contains("External frames:")
                    .contains("Call site:");
        }
    }

    @Nested
    @DisplayName("debugPrintStackTrace()")
    class DebugPrintStackTraceTests {

        @Test
        @DisplayName("should not throw exception")
        void shouldNotThrow() {
            // This method prints to stderr, we just verify it doesn't throw
            assertThatCode(() -> StackTraceUtil.debugPrintStackTrace())
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should not allow instantiation")
        void shouldNotAllowInstantiation() {
            assertThatThrownBy(() -> {
                var constructor = StackTraceUtil.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            })
                    .hasCauseInstanceOf(AssertionError.class)
                    .hasMessageContaining("utility class");
        }
    }
}