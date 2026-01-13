package io.github.programmerjide.javadump.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive tests for StringUtil.
 *
 * @author Oladejo Olajide
 */
@DisplayName("StringUtil")
class StringUtilTest {

    @Nested
    @DisplayName("escape()")
    class EscapeTests {

        @Test
        @DisplayName("should escape newline characters")
        void shouldEscapeNewline() {
            assertThat(StringUtil.escape("Hello\nWorld"))
                    .isEqualTo("Hello\\nWorld");
        }

        @Test
        @DisplayName("should escape tab characters")
        void shouldEscapeTab() {
            assertThat(StringUtil.escape("Hello\tWorld"))
                    .isEqualTo("Hello\\tWorld");
        }

        @Test
        @DisplayName("should escape carriage return")
        void shouldEscapeCarriageReturn() {
            assertThat(StringUtil.escape("Hello\rWorld"))
                    .isEqualTo("Hello\\rWorld");
        }

        @Test
        @DisplayName("should escape backslash")
        void shouldEscapeBackslash() {
            assertThat(StringUtil.escape("C:\\Users\\test"))
                    .isEqualTo("C:\\\\Users\\\\test");
        }

        @Test
        @DisplayName("should escape double quotes")
        void shouldEscapeDoubleQuotes() {
            assertThat(StringUtil.escape("He said \"Hello\""))
                    .isEqualTo("He said \\\"Hello\\\"");
        }

        @Test
        @DisplayName("should escape multiple control characters")
        void shouldEscapeMultipleControlChars() {
            assertThat(StringUtil.escape("Line1\nLine2\tTab\r"))
                    .isEqualTo("Line1\\nLine2\\tTab\\r");
        }

        @Test
        @DisplayName("should escape backspace and form feed")
        void shouldEscapeBackspaceAndFormFeed() {
            assertThat(StringUtil.escape("Test\bBack\fFeed"))
                    .isEqualTo("Test\\bBack\\fFeed");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("should return empty string for null or empty input")
        void shouldReturnEmptyForNullOrEmpty(String input) {
            assertThat(StringUtil.escape(input)).isEmpty();
        }

        @Test
        @DisplayName("should handle string with no special characters")
        void shouldHandleNormalString() {
            assertThat(StringUtil.escape("Hello World"))
                    .isEqualTo("Hello World");
        }

        @Test
        @DisplayName("should escape control characters to unicode")
        void shouldEscapeControlCharsToUnicode() {
            String input = "Test\u0001\u001F";
            String result = StringUtil.escape(input);

            assertThat(result)
                    .contains("\\u0001")
                    .contains("\\u001f");
        }
    }

    @Nested
    @DisplayName("truncate()")
    class TruncateTests {

        @Test
        @DisplayName("should truncate long string with ellipsis")
        void shouldTruncateLongString() {
            String result = StringUtil.truncate("Hello World", 5);

            assertThat(result)
                    .hasSize(5)
                    .endsWith("…");
        }

        @Test
        @DisplayName("should not truncate short string")
        void shouldNotTruncateShortString() {
            assertThat(StringUtil.truncate("Hi", 10))
                    .isEqualTo("Hi");
        }

        @Test
        @DisplayName("should return empty for null input")
        void shouldReturnEmptyForNull() {
            assertThat(StringUtil.truncate(null, 5)).isEmpty();
        }

        @Test
        @DisplayName("should handle exact length match")
        void shouldHandleExactLength() {
            assertThat(StringUtil.truncate("Hello", 5))
                    .isEqualTo("Hello");
        }

        @Test
        @DisplayName("should throw exception for invalid maxLength")
        void shouldThrowForInvalidMaxLength() {
            assertThatThrownBy(() -> StringUtil.truncate("test", 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("must be at least 1");

            assertThatThrownBy(() -> StringUtil.truncate("test", -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "'Hello World', 8, 'Hello W…'",
                "'Test', 10, 'Test'",
                "'A', 1, 'A'"
        })
        @DisplayName("should truncate various strings correctly")
        void shouldTruncateVariousStrings(String input, int maxLength, String expected) {
            assertThat(StringUtil.truncate(input, maxLength))
                    .isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("safeToString()")
    class SafeToStringTests {

        @Test
        @DisplayName("should return 'null' for null object")
        void shouldReturnNullForNullObject() {
            assertThat(StringUtil.safeToString(null))
                    .isEqualTo("null");
        }

        @Test
        @DisplayName("should return string value for string object")
        void shouldReturnStringValue() {
            assertThat(StringUtil.safeToString("test"))
                    .isEqualTo("test");
        }

        @Test
        @DisplayName("should handle object with normal toString")
        void shouldHandleNormalToString() {
            Object obj = new Object() {
                @Override
                public String toString() {
                    return "CustomObject";
                }
            };

            assertThat(StringUtil.safeToString(obj))
                    .isEqualTo("CustomObject");
        }

        @Test
        @DisplayName("should handle exception in toString")
        void shouldHandleExceptionInToString() {
            Object obj = new Object() {
                @Override
                public String toString() {
                    throw new RuntimeException("toString failed");
                }
            };

            String result = StringUtil.safeToString(obj);

            assertThat(result)
                    .startsWith(obj.getClass().getName())
                    .contains("@");
        }

        @Test
        @DisplayName("should handle toString returning null")
        void shouldHandleToStringReturningNull() {
            Object obj = new Object() {
                @Override
                public String toString() {
                    return null;
                }
            };

            assertThat(StringUtil.safeToString(obj))
                    .isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("repeat()")
    class RepeatTests {

        @Test
        @DisplayName("should repeat string n times")
        void shouldRepeatString() {
            assertThat(StringUtil.repeat("ab", 3))
                    .isEqualTo("ababab");
        }

        @Test
        @DisplayName("should return empty for zero times")
        void shouldReturnEmptyForZeroTimes() {
            assertThat(StringUtil.repeat("test", 0))
                    .isEmpty();
        }

        @Test
        @DisplayName("should return original for one time")
        void shouldReturnOriginalForOnce() {
            assertThat(StringUtil.repeat("test", 1))
                    .isEqualTo("test");
        }

        @Test
        @DisplayName("should return empty for null input")
        void shouldReturnEmptyForNullInput() {
            assertThat(StringUtil.repeat(null, 5))
                    .isEmpty();
        }

        @Test
        @DisplayName("should throw exception for negative times")
        void shouldThrowForNegativeTimes() {
            assertThatThrownBy(() -> StringUtil.repeat("test", -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("must be non-negative");
        }

        @ParameterizedTest
        @CsvSource({
                "'x', 0, ''",
                "'x', 1, 'x'",
                "'x', 5, 'xxxxx'",
                "'ab', 3, 'ababab'"
        })
        @DisplayName("should repeat correctly for various inputs")
        void shouldRepeatVariousInputs(String str, int times, String expected) {
            assertThat(StringUtil.repeat(str, times))
                    .isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("indent()")
    class IndentTests {

        @Test
        @DisplayName("should create no indent for level 0")
        void shouldCreateNoIndentForZero() {
            assertThat(StringUtil.indent(0)).isEmpty();
        }

        @Test
        @DisplayName("should create 2 spaces for level 1")
        void shouldCreate2SpacesForLevel1() {
            assertThat(StringUtil.indent(1))
                    .isEqualTo("  ")
                    .hasSize(2);
        }

        @Test
        @DisplayName("should create 4 spaces for level 2")
        void shouldCreate4SpacesForLevel2() {
            assertThat(StringUtil.indent(2))
                    .isEqualTo("    ")
                    .hasSize(4);
        }

        @Test
        @DisplayName("should handle negative level as 0")
        void shouldHandleNegativeLevel() {
            assertThat(StringUtil.indent(-1)).isEmpty();
        }

        @ParameterizedTest
        @CsvSource({
                "0, 0",
                "1, 2",
                "2, 4",
                "3, 6",
                "5, 10"
        })
        @DisplayName("should create correct indent length")
        void shouldCreateCorrectIndentLength(int level, int expectedLength) {
            assertThat(StringUtil.indent(level))
                    .hasSize(expectedLength);
        }
    }

    @Nested
    @DisplayName("quote()")
    class QuoteTests {

        @Test
        @DisplayName("should wrap string in double quotes")
        void shouldWrapInQuotes() {
            assertThat(StringUtil.quote("hello"))
                    .isEqualTo("\"hello\"");
        }

        @Test
        @DisplayName("should handle null as empty string")
        void shouldHandleNull() {
            assertThat(StringUtil.quote(null))
                    .isEqualTo("\"\"");
        }

        @Test
        @DisplayName("should handle empty string")
        void shouldHandleEmptyString() {
            assertThat(StringUtil.quote(""))
                    .isEqualTo("\"\"");
        }
    }

    @Nested
    @DisplayName("isEmpty()")
    class IsEmptyTests {

        @Test
        @DisplayName("should return true for null")
        void shouldReturnTrueForNull() {
            assertThat(StringUtil.isEmpty(null)).isTrue();
        }

        @Test
        @DisplayName("should return true for empty string")
        void shouldReturnTrueForEmpty() {
            assertThat(StringUtil.isEmpty("")).isTrue();
        }

        @Test
        @DisplayName("should return false for non-empty string")
        void shouldReturnFalseForNonEmpty() {
            assertThat(StringUtil.isEmpty("test")).isFalse();
        }

        @Test
        @DisplayName("should return false for whitespace")
        void shouldReturnFalseForWhitespace() {
            assertThat(StringUtil.isEmpty("   ")).isFalse();
        }
    }

    @Nested
    @DisplayName("isBlank()")
    class IsBlankTests {

        @Test
        @DisplayName("should return true for null")
        void shouldReturnTrueForNull() {
            assertThat(StringUtil.isBlank(null)).isTrue();
        }

        @Test
        @DisplayName("should return true for empty string")
        void shouldReturnTrueForEmpty() {
            assertThat(StringUtil.isBlank("")).isTrue();
        }

        @Test
        @DisplayName("should return true for whitespace only")
        void shouldReturnTrueForWhitespace() {
            assertThat(StringUtil.isBlank("   ")).isTrue();
            assertThat(StringUtil.isBlank("\t\n")).isTrue();
        }

        @Test
        @DisplayName("should return false for non-blank string")
        void shouldReturnFalseForNonBlank() {
            assertThat(StringUtil.isBlank("test")).isFalse();
            assertThat(StringUtil.isBlank("  test  ")).isFalse();
        }
    }

    @Nested
    @DisplayName("firstNonEmpty()")
    class FirstNonEmptyTests {

        @Test
        @DisplayName("should return first non-empty string")
        void shouldReturnFirstNonEmpty() {
            assertThat(StringUtil.firstNonEmpty(null, "", "hello", "world"))
                    .isEqualTo("hello");
        }

        @Test
        @DisplayName("should return empty if all are empty")
        void shouldReturnEmptyIfAllEmpty() {
            assertThat(StringUtil.firstNonEmpty(null, "", null))
                    .isEmpty();
        }

        @Test
        @DisplayName("should handle null array")
        void shouldHandleNullArray() {
            assertThat(StringUtil.firstNonEmpty((String[]) null))
                    .isEmpty();
        }

        @Test
        @DisplayName("should return first if first is non-empty")
        void shouldReturnFirstIfNonEmpty() {
            assertThat(StringUtil.firstNonEmpty("first", "second"))
                    .isEqualTo("first");
        }
    }

    @Nested
    @DisplayName("abbreviateMiddle()")
    class AbbreviateMiddleTests {

        @Test
        @DisplayName("should abbreviate long string in the middle")
        void shouldAbbreviateLongString() {
            String result = StringUtil.abbreviateMiddle(
                    "com.example.package.ClassName", 20
            );

            assertThat(result)
                    .hasSize(20)
                    .startsWith("com.exa")
                    .endsWith("ClassName")
                    .contains("…");
        }

        @Test
        @DisplayName("should not abbreviate short string")
        void shouldNotAbbreviateShortString() {
            assertThat(StringUtil.abbreviateMiddle("short", 10))
                    .isEqualTo("short");
        }

        @Test
        @DisplayName("should handle null")
        void shouldHandleNull() {
            assertThat(StringUtil.abbreviateMiddle(null, 10))
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should not allow instantiation")
        void shouldNotAllowInstantiation() {
            assertThatThrownBy(() -> {
                var constructor = StringUtil.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            })
                    .hasCauseInstanceOf(AssertionError.class)
                    .hasMessageContaining("utility class");
        }
    }
}