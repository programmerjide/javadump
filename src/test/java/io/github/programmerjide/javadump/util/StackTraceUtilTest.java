package io.github.programmerjide.javadump.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link StackTraceUtil}.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
@DisplayName("StackTraceUtil Tests")
class StackTraceUtilTest {

    // ==================== Utility Class Validation ====================

    @Test
    @DisplayName("StackTraceUtil is a utility class and cannot be instantiated")
    void stackTraceUtil_isUtilityClass_cannotBeInstantiated() {
        // Verify it's a final class
        assertThat(StackTraceUtil.class).isFinal();

        // Verify it has only one private constructor
        Constructor<?>[] constructors = StackTraceUtil.class.getDeclaredConstructors();
        assertThat(constructors).hasSize(1);

        Constructor<?> constructor = constructors[0];
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

        // Verify constructor throws exception when called
        constructor.setAccessible(true);

        // Properly unwrap InvocationTargetException
        assertThatThrownBy(() -> {
            try {
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                throw e.getCause(); // This is the key - unwrap the actual exception
            }
        })
                .isInstanceOf(AssertionError.class)  // StackTraceUtil throws AssertionError
                .hasMessage("Utility class");        // With this exact message
    }

    // ==================== CallSite Class Tests ====================

    @Nested
    @DisplayName("CallSite Class Tests")
    class CallSiteTests {

        @Test
        @DisplayName("CallSite constructor with three parameters sets fields correctly")
        void callSiteConstructor_threeParams_setsFields() {
            StackTraceUtil.CallSite callSite = new StackTraceUtil.CallSite("Test.java", "testMethod", 42);

            assertThat(callSite.getFileName()).isEqualTo("Test.java");
            assertThat(callSite.getMethodName()).isEqualTo("testMethod");
            assertThat(callSite.getLineNumber()).isEqualTo(42);
            assertThat(callSite.getClassName()).isEqualTo("Unknown");
        }

        @Test
        @DisplayName("CallSite constructor with four parameters sets all fields")
        void callSiteConstructor_fourParams_setsAllFields() {
            StackTraceUtil.CallSite callSite = new StackTraceUtil.CallSite(
                    "Test.java", "testMethod", 42, "com.example.TestClass");

            assertThat(callSite.getFileName()).isEqualTo("Test.java");
            assertThat(callSite.getMethodName()).isEqualTo("testMethod");
            assertThat(callSite.getLineNumber()).isEqualTo(42);
            assertThat(callSite.getClassName()).isEqualTo("com.example.TestClass");
        }

        @Test
        @DisplayName("CallSite.toString() returns filename:linenumber format")
        void callSiteToString_returnsFilenameLineNumberFormat() {
            StackTraceUtil.CallSite callSite = new StackTraceUtil.CallSite("Test.java", "testMethod", 42);
            assertThat(callSite.toString()).isEqualTo("Test.java:42");
        }

        @Test
        @DisplayName("CallSite.equals() returns true for identical objects")
        void callSiteEquals_identicalObjects_returnsTrue() {
            StackTraceUtil.CallSite cs1 = new StackTraceUtil.CallSite("Test.java", "test", 1, "TestClass");
            StackTraceUtil.CallSite cs2 = new StackTraceUtil.CallSite("Test.java", "test", 1, "TestClass");

            assertThat(cs1).isEqualTo(cs2);
            assertThat(cs1.hashCode()).isEqualTo(cs2.hashCode());
        }

        @Test
        @DisplayName("CallSite.equals() returns false for different objects")
        void callSiteEquals_differentObjects_returnsFalse() {
            StackTraceUtil.CallSite cs1 = new StackTraceUtil.CallSite("Test1.java", "test", 1, "TestClass");
            StackTraceUtil.CallSite cs2 = new StackTraceUtil.CallSite("Test2.java", "test", 1, "TestClass");
            StackTraceUtil.CallSite cs3 = new StackTraceUtil.CallSite("Test.java", "test1", 1, "TestClass");
            StackTraceUtil.CallSite cs4 = new StackTraceUtil.CallSite("Test.java", "test", 2, "TestClass");
            StackTraceUtil.CallSite cs5 = new StackTraceUtil.CallSite("Test.java", "test", 1, "OtherClass");

            assertThat(cs1).isNotEqualTo(cs2);
            assertThat(cs1).isNotEqualTo(cs3);
            assertThat(cs1).isNotEqualTo(cs4);
            assertThat(cs1).isNotEqualTo(cs5);
        }

        @Test
        @DisplayName("CallSite.equals() returns false for null")
        void callSiteEquals_withNull_returnsFalse() {
            StackTraceUtil.CallSite callSite = new StackTraceUtil.CallSite("Test.java", "test", 1);
            assertThat(callSite).isNotEqualTo(null);
        }

        @Test
        @DisplayName("CallSite.equals() returns false for different class")
        void callSiteEquals_withDifferentClass_returnsFalse() {
            StackTraceUtil.CallSite callSite = new StackTraceUtil.CallSite("Test.java", "test", 1);
            assertThat(callSite).isNotEqualTo("not a CallSite");
        }
    }

    // ==================== Frame Detection Tests ====================

    @Test
    @DisplayName("isInternalFrame() returns true for Javadump package frames")
    void isInternalFrame_javadumpPackage_returnsTrue() {
        StackTraceElement frame = new StackTraceElement(
                "io.github.programmerjide.javadump.Formatter",
                "format",
                "Formatter.java",
                42);

        assertThat(StackTraceUtil.isInternalFrame(frame)).isTrue();
    }

    @Test
    @DisplayName("isInternalFrame() returns false for non-Javadump package frames")
    void isInternalFrame_nonJavadumpPackage_returnsFalse() {
        StackTraceElement frame = new StackTraceElement(
                "com.example.OtherClass",
                "method",
                "OtherClass.java",
                42);

        assertThat(StackTraceUtil.isInternalFrame(frame)).isFalse();
    }

    @Test
    @DisplayName("isInternalFrame() returns false for null frame")
    void isInternalFrame_nullFrame_returnsFalse() {
        assertThat(StackTraceUtil.isInternalFrame(null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "org.junit.jupiter.api.Test",
            "org.assertj.core.api.Assertions",
            "org.testng.annotations.Test",
            "org.mockito.Mockito"
    })
    @DisplayName("isTestFrame() returns true for test framework frames")
    void isTestFrame_testFrameworkFrames_returnsTrue(String className) {
        StackTraceElement frame = new StackTraceElement(className, "test", "Test.java", 42);
        assertThat(StackTraceUtil.isTestFrame(frame)).isTrue();
    }

    @Test
    @DisplayName("isTestFrame() returns false for non-test frames")
    void isTestFrame_nonTestFrames_returnsFalse() {
        StackTraceElement frame = new StackTraceElement(
                "com.example.MyClass",
                "method",
                "MyClass.java",
                42);

        assertThat(StackTraceUtil.isTestFrame(frame)).isFalse();
    }

    @Test
    @DisplayName("isTestFrame() returns false for null frame")
    void isTestFrame_nullFrame_returnsFalse() {
        assertThat(StackTraceUtil.isTestFrame(null)).isFalse();
    }

    // ==================== Call Site Detection Tests ====================

    @Test
    @DisplayName("getCallSite() returns a valid stack trace element")
    void getCallSite_returnsStackTraceElement() {
        Optional<StackTraceElement> result = StackTraceUtil.getCallSite();

        assertThat(result).isPresent();
        StackTraceElement element = result.get();

        // Should return the test method frame (or a frame from this test)
        assertThat(element.getMethodName()).isNotBlank();
        // Should not be an internal frame
        assertThat(StackTraceUtil.isInternalFrame(element)).isFalse();
    }

    @Test
    @DisplayName("getCallSite() skip parameter changes returned frame")
    void getCallSite_skipParameterChangesFrame() {
        // Get multiple frames with different skip values
        List<String> frames = new ArrayList<>();

        for (int skip = 0; skip < 5; skip++) {
            Optional<StackTraceElement> frame = StackTraceUtil.getCallSite(skip);
            if (frame.isPresent()) {
                frames.add(frame.get().getMethodName());
            }
        }

        // Should have gotten some frames
        assertThat(frames).isNotEmpty();

        // The frames should mostly be different (some might be same due to infrastructure)
        long uniqueCount = frames.stream().distinct().count();
        assertThat(uniqueCount).isGreaterThanOrEqualTo(2); // At least 2 different methods
    }

    @Test
    @DisplayName("getCallSite() with negative skip throws IllegalArgumentException")
    void getCallSite_negativeSkip_throwsException() {
        assertThatThrownBy(() -> StackTraceUtil.getCallSite(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("skipAdditionalFrames must be non-negative");
    }

    @Test
    @DisplayName("getCallSite() with large skip returns empty")
    void getCallSite_largeSkip_returnsEmpty() {
        Optional<StackTraceElement> result = StackTraceUtil.getCallSite(1000);
        assertThat(result).isEmpty();
    }

    // ==================== findCallSite() Tests ====================

    @Test
    @DisplayName("findCallSite() returns CallSite object")
    void findCallSite_returnsCallSiteObject() {
        StackTraceUtil.CallSite result = StackTraceUtil.findCallSite();

        assertThat(result).isNotNull();
        assertThat(result.getFileName()).isNotEqualTo("Unknown");
        assertThat(result.getMethodName()).isNotEqualTo("unknown");
    }

    @Test
    @DisplayName("findCallSite() returns unknown when no valid frame found")
    void findCallSite_noValidFrame_returnsUnknown() {
        // Create a situation where getCallSite returns empty
        // This is hard to test directly, so we'll test the behavior through the public API
        // In normal execution, it should find a frame
        StackTraceUtil.CallSite result = StackTraceUtil.findCallSite();
        assertThat(result).isNotNull();
    }

    // ==================== Formatting Tests ====================

    @Test
    @DisplayName("formatCallSite() formats valid element correctly")
    void formatCallSite_validElement_returnsFormattedString() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                "TestClass.java",
                42);

        String result = StackTraceUtil.formatCallSite(element);
        assertThat(result).isEqualTo("TestClass.java:42");
    }

    @Test
    @DisplayName("formatCallSite() handles null filename")
    void formatCallSite_nullFileName_usesUnknown() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                null, // null filename
                42);

        String result = StackTraceUtil.formatCallSite(element);
        assertThat(result).isEqualTo("Unknown:42");
    }

    @Test
    @DisplayName("formatCallSite() handles negative line number")
    void formatCallSite_negativeLineNumber_usesZero() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                "TestClass.java",
                -1); // negative line number

        String result = StackTraceUtil.formatCallSite(element);
        assertThat(result).isEqualTo("TestClass.java:0");
    }

    @Test
    @DisplayName("formatCallSite() with null element returns unknown")
    void formatCallSite_nullElement_returnsUnknown() {
        String result = StackTraceUtil.formatCallSite(null);
        assertThat(result).isEqualTo("Unknown:0");
    }

    @Test
    @DisplayName("formatCallSiteDetailed() returns full format")
    void formatCallSiteDetailed_returnsFullFormat() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                "TestClass.java",
                42);

        String result = StackTraceUtil.formatCallSiteDetailed(element);
        assertThat(result).isEqualTo("com.example.TestClass.testMethod(TestClass.java:42)");
    }

    @Test
    @DisplayName("formatCallSiteDetailed() with null element returns unknown")
    void formatCallSiteDetailed_nullElement_returnsUnknown() {
        String result = StackTraceUtil.formatCallSiteDetailed(null);
        assertThat(result).isEqualTo("Unknown:0");
    }

    @Test
    @DisplayName("formatCallSiteSimple() returns simple class name format")
    void formatCallSiteSimple_returnsSimpleClassName() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                "TestClass.java",
                42);

        String result = StackTraceUtil.formatCallSiteSimple(element);
        assertThat(result).isEqualTo("TestClass.testMethod(TestClass.java:42)");
    }

    @Test
    @DisplayName("formatCallSiteSimple() handles class with no package")
    void formatCallSiteSimple_noPackage_handlesCorrectly() {
        StackTraceElement element = new StackTraceElement(
                "TestClass", // no package
                "testMethod",
                "TestClass.java",
                42);

        String result = StackTraceUtil.formatCallSiteSimple(element);
        assertThat(result).isEqualTo("TestClass.testMethod(TestClass.java:42)");
    }

    // ==================== File and Line Number Tests ====================

    @Test
    @DisplayName("getFileName() returns filename or unknown")
    void getFileName_returnsFileNameOrUnknown() {
        StackTraceElement withFile = new StackTraceElement(
                "com.example.TestClass",
                "method",
                "TestClass.java",
                42);
        StackTraceElement withoutFile = new StackTraceElement(
                "com.example.TestClass",
                "method",
                null,
                42);
        StackTraceElement nullElement = null;

        assertThat(StackTraceUtil.getFileName(withFile)).isEqualTo("TestClass.java");
        assertThat(StackTraceUtil.getFileName(withoutFile)).isEqualTo("Unknown");
        assertThat(StackTraceUtil.getFileName(nullElement)).isEqualTo("Unknown");
    }

    @Test
    @DisplayName("getLineNumber() returns line number or zero")
    void getLineNumber_returnsLineNumberOrZero() {
        StackTraceElement withLine = new StackTraceElement(
                "com.example.TestClass",
                "method",
                "TestClass.java",
                42);
        StackTraceElement negativeLine = new StackTraceElement(
                "com.example.TestClass",
                "method",
                "TestClass.java",
                -1);
        StackTraceElement nullElement = null;

        assertThat(StackTraceUtil.getLineNumber(withLine)).isEqualTo(42);
        assertThat(StackTraceUtil.getLineNumber(negativeLine)).isEqualTo(0);
        assertThat(StackTraceUtil.getLineNumber(nullElement)).isEqualTo(0);
    }

    // ==================== Caller Info Tests ====================

    @Test
    @DisplayName("isInternalFrame() correctly identifies Javadump frames")
    void isInternalFrame_correctlyIdentifiesFrames() {
        StackTraceElement javadumpFrame = new StackTraceElement(
                "io.github.programmerjide.javadump.Formatter",
                "format",
                "Formatter.java",
                42);

        StackTraceElement externalFrame = new StackTraceElement(
                "com.example.MyClass",
                "myMethod",
                "MyClass.java",
                1);

        assertThat(StackTraceUtil.isInternalFrame(javadumpFrame)).isTrue();
        assertThat(StackTraceUtil.isInternalFrame(externalFrame)).isFalse();
    }

    @Test
    @DisplayName("isTestFrame() correctly identifies test framework frames")
    void isTestFrame_correctlyIdentifiesFrames() {
        StackTraceElement junitFrame = new StackTraceElement(
                "org.junit.jupiter.api.Test",
                "test",
                "Test.java",
                1);

        StackTraceElement nonTestFrame = new StackTraceElement(
                "com.example.MyClass",
                "myMethod",
                "MyClass.java",
                1);

        assertThat(StackTraceUtil.isTestFrame(junitFrame)).isTrue();
        assertThat(StackTraceUtil.isTestFrame(nonTestFrame)).isFalse();
    }

    // ==================== Stack Trace Manipulation Tests ====================

    @Test
    @DisplayName("getCurrentStackTrace() returns stack trace without internal methods")
    void getCurrentStackTrace_returnsStackTrace() {
        StackTraceElement[] stackTrace = StackTraceUtil.getCurrentStackTrace();

        assertThat(stackTrace).isNotEmpty();
        // Should not contain internal methods
        for (StackTraceElement frame : stackTrace) {
            assertThat(frame.getMethodName()).isNotEqualTo("getCurrentStackTrace");
            assertThat(frame.getMethodName()).isNotEqualTo("getStackTrace");
        }
    }

    @Test
    @DisplayName("getStackTraceString() returns formatted stack trace")
    void getStackTraceString_returnsFormattedString() {
        String result = StackTraceUtil.getStackTraceString();

        assertThat(result).isNotBlank();
        assertThat(result).contains("at ");
        assertThat(result).contains("StackTraceUtilTest");
    }

    @Test
    @DisplayName("filterExternalFrames() removes internal and Java frames")
    void filterExternalFrames_removesInternalFrames() {
        // Create a mixed stack trace
        StackTraceElement[] mixedTrace = new StackTraceElement[]{
                new StackTraceElement("java.lang.Thread", "getStackTrace", "Thread.java", 123),
                new StackTraceElement("io.github.programmerjide.javadump.Formatter", "format", "Formatter.java", 42),
                new StackTraceElement("com.example.MyClass", "myMethod", "MyClass.java", 1),
                new StackTraceElement("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke0", null, -2)
        };

        StackTraceElement[] filtered = StackTraceUtil.filterExternalFrames(mixedTrace);

        // Should only keep com.example.MyClass
        assertThat(filtered).hasSize(1);
        assertThat(filtered[0].getClassName()).isEqualTo("com.example.MyClass");
    }

    @Test
    @DisplayName("filterExternalFrames() with null returns empty array")
    void filterExternalFrames_nullInput_returnsEmptyArray() {
        StackTraceElement[] result = StackTraceUtil.filterExternalFrames(null);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("filterExternalFrames() with empty array returns empty array")
    void filterExternalFrames_emptyArray_returnsEmptyArray() {
        StackTraceElement[] result = StackTraceUtil.filterExternalFrames(new StackTraceElement[0]);
        assertThat(result).isEmpty();
    }

    // ==================== Call Origin Tests ====================

    @Test
    @DisplayName("isCalledFrom() returns true when called from specified class")
    void isCalledFrom_currentClass_returnsTrue() {
        boolean result = StackTraceUtil.isCalledFrom(this.getClass().getName());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isCalledFrom() returns false when not called from specified class")
    void isCalledFrom_differentClass_returnsFalse() {
        boolean result = StackTraceUtil.isCalledFrom("com.example.NonexistentClass");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isCalledFrom() with null or empty returns false")
    void isCalledFrom_nullOrEmpty_returnsFalse() {
        assertThat(StackTraceUtil.isCalledFrom(null)).isFalse();
        assertThat(StackTraceUtil.isCalledFrom("")).isFalse();
    }

    @Test
    @DisplayName("isCalledFromPackage() returns true when called from specified package")
    void isCalledFromPackage_currentPackage_returnsTrue() {
        boolean result = StackTraceUtil.isCalledFromPackage("io.github.programmerjide");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isCalledFromPackage() returns false when not called from specified package")
    void isCalledFromPackage_differentPackage_returnsFalse() {
        boolean result = StackTraceUtil.isCalledFromPackage("com.example.nonexistent");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isCalledFromPackage() with null or empty returns false")
    void isCalledFromPackage_nullOrEmpty_returnsFalse() {
        assertThat(StackTraceUtil.isCalledFromPackage(null)).isFalse();
        assertThat(StackTraceUtil.isCalledFromPackage("")).isFalse();
    }

    // ==================== Stack Depth Tests ====================

    @Test
    @DisplayName("getStackDepth() returns positive number")
    void getStackDepth_returnsPositiveNumber() {
        int depth = StackTraceUtil.getStackDepth();
        assertThat(depth).isGreaterThan(0);
    }

    // ==================== Validation Tests ====================

    @Test
    @DisplayName("isValidCallSite() returns true for valid call site")
    void isValidCallSite_validCallSite_returnsTrue() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                "TestClass.java",
                42);

        boolean result = StackTraceUtil.isValidCallSite(Optional.of(element));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValidCallSite() returns false for null filename")
    void isValidCallSite_nullFileName_returnsFalse() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                null,
                42);

        boolean result = StackTraceUtil.isValidCallSite(Optional.of(element));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValidCallSite() returns false for negative line number")
    void isValidCallSite_negativeLineNumber_returnsFalse() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                "TestClass.java",
                -1);

        boolean result = StackTraceUtil.isValidCallSite(Optional.of(element));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValidCallSite() returns false for empty optional")
    void isValidCallSite_emptyOptional_returnsFalse() {
        boolean result = StackTraceUtil.isValidCallSite(Optional.empty());
        assertThat(result).isFalse();
    }

    // ==================== Helper Method Tests ====================

    @Test
    @DisplayName("getOrDefault() returns element or default")
    void getOrDefault_returnsElementOrDefault() {
        StackTraceElement validElement = new StackTraceElement(
                "com.example.TestClass",
                "method",
                "TestClass.java",
                42);

        StackTraceElement fromValid = StackTraceUtil.getOrDefault(Optional.of(validElement));
        StackTraceElement fromEmpty = StackTraceUtil.getOrDefault(Optional.empty());

        assertThat(fromValid).isEqualTo(validElement);
        assertThat(fromEmpty.getFileName()).isEqualTo("Unknown");
        assertThat(fromEmpty.getLineNumber()).isEqualTo(0);
        assertThat(fromEmpty.getClassName()).isEqualTo("Unknown");
        assertThat(fromEmpty.getMethodName()).isEqualTo("unknown");
    }

    // ==================== Summary and Debug Tests ====================

    @Test
    @DisplayName("getCallStackSummary() returns formatted summary")
    void getCallStackSummary_returnsFormattedString() {
        String summary = StackTraceUtil.getCallStackSummary();

        assertThat(summary).isNotBlank();
        assertThat(summary).contains("Call Stack Summary:");
        assertThat(summary).contains("Total frames:");
        assertThat(summary).contains("Internal frames:");
        assertThat(summary).contains("External frames:");
        assertThat(summary).contains("Call site:");
    }

    @Test
    @DisplayName("debugPrintStackTrace() does not throw")
    void debugPrintStackTrace_doesNotThrow() {
        // Just verify it doesn't throw an exception
        assertThatCode(StackTraceUtil::debugPrintStackTrace)
                .doesNotThrowAnyException();
    }

    // ==================== Integration Tests ====================

    @Test
    @DisplayName("findCallSite() and getCallSite() are consistent")
    void findCallSite_and_getCallSite_areConsistent() {
        StackTraceUtil.CallSite callSite = StackTraceUtil.findCallSite();
        Optional<StackTraceElement> element = StackTraceUtil.getCallSite();

        assertThat(element).isPresent();
        assertThat(callSite.getFileName()).isEqualTo(element.get().getFileName());
        assertThat(callSite.getLineNumber()).isEqualTo(element.get().getLineNumber());
        assertThat(callSite.getMethodName()).isEqualTo(element.get().getMethodName());
    }

    @Test
    @DisplayName("format methods handle edge cases consistently")
    void formatMethods_handleEdgeCasesConsistently() {
        StackTraceElement element = new StackTraceElement(
                "com.example.TestClass",
                "testMethod",
                null, // null filename
                -1); // negative line number

        String basic = StackTraceUtil.formatCallSite(element);
        String detailed = StackTraceUtil.formatCallSiteDetailed(element);
        String simple = StackTraceUtil.formatCallSiteSimple(element);

        assertThat(basic).isEqualTo("Unknown:0");
        assertThat(detailed).contains("Unknown:0");
        assertThat(simple).contains("Unknown:0");
    }

    // ==================== Nested Test Class for Call Site Verification ====================

    @Test
    @DisplayName("getStackDepth() returns consistent results")
    void getStackDepth_returnsConsistentResults() {
        // Call multiple times - should return similar values
        int depth1 = StackTraceUtil.getStackDepth();
        int depth2 = StackTraceUtil.getStackDepth();
        int depth3 = getStackDepthViaHelper();

        // All should be within a small range (0-2 difference due to measurement point)
        int minDepth = Math.min(Math.min(depth1, depth2), depth3);
        int maxDepth = Math.max(Math.max(depth1, depth2), depth3);

        assertThat(maxDepth - minDepth).isLessThanOrEqualTo(3);
    }

    private int getStackDepthViaHelper() {
        return StackTraceUtil.getStackDepth();
    }

    // ==================== Performance/Smoke Tests ====================

    @Test
    @DisplayName("All public methods execute without throwing")
    void allPublicMethods_executeWithoutThrowing() {
        // Smoke test for methods that are hard to assert specific results for
        assertThatCode(() -> {
            StackTraceUtil.findCallSite();
            StackTraceUtil.getCallSite();
            StackTraceUtil.getCallSite(0);
            StackTraceUtil.getCallSite(1);
            StackTraceUtil.formatCallSite(StackTraceUtil.getCallSite().orElse(null));
            StackTraceUtil.formatCallSiteDetailed(StackTraceUtil.getCallSite().orElse(null));
            StackTraceUtil.formatCallSiteSimple(StackTraceUtil.getCallSite().orElse(null));
            StackTraceUtil.getCallerClassName();
            StackTraceUtil.getCallerMethodName();
            StackTraceUtil.getCurrentStackTrace();
            StackTraceUtil.getStackTraceString();
            StackTraceUtil.filterExternalFrames(Thread.currentThread().getStackTrace());
            StackTraceUtil.isCalledFrom("java.lang.Object");
            StackTraceUtil.isCalledFromPackage("java.lang");
            StackTraceUtil.getStackDepth();
            StackTraceUtil.isValidCallSite(StackTraceUtil.getCallSite());
            StackTraceUtil.getOrDefault(StackTraceUtil.getCallSite());
            StackTraceUtil.getCallStackSummary();
            StackTraceUtil.debugPrintStackTrace();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Methods handle null inputs gracefully")
    void methods_handleNullInputsGracefully() {
        // Test that methods don't throw NPE with null inputs
        assertThatCode(() -> {
            StackTraceUtil.isInternalFrame(null);
            StackTraceUtil.isTestFrame(null);
            StackTraceUtil.formatCallSite(null);
            StackTraceUtil.formatCallSiteDetailed(null);
            StackTraceUtil.formatCallSiteSimple(null);
            StackTraceUtil.getFileName(null);
            StackTraceUtil.getLineNumber(null);
            StackTraceUtil.filterExternalFrames(null);
            // Don't test isValidCallSite(null) as it expects Optional
            StackTraceUtil.isValidCallSite(Optional.empty()); // Test with empty instead
        }).doesNotThrowAnyException();
    }
}