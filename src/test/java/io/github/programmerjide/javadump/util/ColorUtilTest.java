package io.github.programmerjide.javadump.util;

import org.junit.jupiter.api.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.*;

class ColorUtilTest {

    @BeforeEach
    void setUp() {
        ColorUtil.forceEnableColors();
    }

    @AfterEach
    void tearDown() {
        // Reset to default state
        ColorUtil.forceEnableColors();
    }

    // ==================== Utility Class Validation ====================

    @Test
    void colorUtil_isUtilityClass_cannotBeInstantiated() {
        // Verify it's a final class
        assertThat(ColorUtil.class).isFinal();

        // Verify it has only one private constructor
        Constructor<?>[] constructors = ColorUtil.class.getDeclaredConstructors();
        assertThat(constructors).hasSize(1);

        Constructor<?> constructor = constructors[0];
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

        // Verify constructor throws exception when called
        constructor.setAccessible(true);
        assertThatThrownBy(() -> {
            try {
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                throw e.getCause(); // Unwrap the actual exception
            }
        })
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("utility class");
    }

    // ==================== Basic Color Methods Tests ====================

    @Test
    void type_withColorsEnabled_returnsColoredText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.type("String");

        assertThat(result)
                .contains("\033[")
                .contains("String")
                .contains("\033[0m");
    }

    @Test
    void type_withColorsDisabled_returnsPlainText() {
        ColorUtil.forceDisableColors();
        String result = ColorUtil.type("String");

        assertThat(result)
                .doesNotContain("\033[")
                .isEqualTo("String");
    }

    @Test
    void string_withColorsEnabled_returnsGreenText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.string("test");

        assertThat(result)
                .contains("\033[")
                .contains("test");
    }

    @Test
    void number_withColorsEnabled_returnsOrangeText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.number("42");

        assertThat(result)
                .contains("\033[")
                .contains("42");
    }

    @Test
    void field_withColorsEnabled_returnsCyanText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.field("name");

        assertThat(result)
                .contains("\033[")
                .contains("name");
    }

    @Test
    void keyword_withColorsEnabled_returnsYellowText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.keyword("null");

        assertThat(result)
                .contains("\033[")
                .contains("null");
    }

    @Test
    void error_withColorsEnabled_returnsRedText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.error("failure");

        assertThat(result)
                .contains("\033[")
                .contains("failure");
    }

    @Test
    void dim_withColorsEnabled_returnsDimText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.dim("dimmed");

        assertThat(result)
                .contains("\033[")
                .contains("dimmed");
    }

    @Test
    void gray_withColorsEnabled_returnsGrayText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.gray("gray");

        assertThat(result)
                .contains("\033[")
                .contains("gray");
    }

    // ==================== Color Constant Tests ====================

    @Test
    void colorConstants_areCorrect() {
        assertThat(ColorUtil.RESET).isEqualTo("\033[0m");
        assertThat(ColorUtil.GRAY).isEqualTo("\033[90m");
        assertThat(ColorUtil.DIM).isEqualTo("\033[2m");
        assertThat(ColorUtil.YELLOW).isEqualTo("\033[33m");
        assertThat(ColorUtil.RED).isEqualTo("\033[31m");
        assertThat(ColorUtil.GREEN).isEqualTo("\033[32m");
        assertThat(ColorUtil.CYAN).isEqualTo("\033[38;5;38m");
        assertThat(ColorUtil.ORANGE).isEqualTo("\033[38;5;208m");
    }

    // ==================== Formatting Helper Tests ====================

    @Test
    void structural_withColorsEnabled_returnsGrayText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.structural("{");

        assertThat(result)
                .contains("\033[")
                .contains("{");
    }

    @Test
    void arrow_withColorsEnabled_returnsColoredArrow() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.arrow();

        assertThat(result)
                .contains("\033[")
                .contains(" → ");
    }

    @Test
    void arrow_withColorsDisabled_returnsPlainArrow() {
        ColorUtil.forceDisableColors();
        String result = ColorUtil.arrow();

        assertThat(result)
                .doesNotContain("\033[")
                .isEqualTo(" → ");
    }

    @Test
    void formatType_withClass_returnsFormattedType() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.formatType(String.class);

        assertThat(result)
                .contains("\033[")
                .contains("#String");
    }

    @Test
    void formatType_withNullClass_returnsNullType() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.formatType((Class<?>) null);

        assertThat(result)
                .contains("#null");
    }

    @Test
    void formatType_withString_returnsFormattedType() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.formatType("Integer");

        assertThat(result)
                .contains("#Integer")
                .contains("\033[");
    }

    @Test
    void formatHeader_withColorsEnabled_returnsDimText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.formatHeader("Test.java", 42);

        assertThat(result)
                .contains("\033[")
                .contains("Test.java:42");
    }

    @Test
    void formatHeader_withColorsDisabled_returnsPlainText() {
        ColorUtil.forceDisableColors();
        String result = ColorUtil.formatHeader("Test.java", 42);

        assertThat(result)
                .doesNotContain("\033[")
                .isEqualTo("Test.java:42");
    }

    @Test
    void formatTruncated_withColorsEnabled_returnsDimText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.formatTruncated();

        assertThat(result)
                .contains("\033[")
                .contains("... (truncated)");
    }

    @Test
    void formatCyclic_withColorsEnabled_returnsYellowText() {
        ColorUtil.forceEnableColors();
        String result = ColorUtil.formatCyclic();

        assertThat(result)
                .contains("\033[")
                .contains("↻ (circular)");
    }

    // ==================== Color Management Tests ====================

    @Test
    void forceEnableColors_alwaysEnablesColors() {
        ColorUtil.forceEnableColors();
        assertThat(ColorUtil.isColorsEnabled()).isTrue();

        String result = ColorUtil.type("test");
        assertThat(result).contains("\033[");
    }

    @Test
    void forceDisableColors_alwaysDisablesColors() {
        ColorUtil.forceDisableColors();
        assertThat(ColorUtil.isColorsEnabled()).isFalse();

        String result = ColorUtil.type("test");
        assertThat(result).doesNotContain("\033[");
    }

    @Test
    void setColorsEnabled_smokeTest() {
        // Just verify it doesn't throw
        assertThatCode(() -> ColorUtil.setColorsEnabled(true))
                .doesNotThrowAnyException();
    }

    // ==================== Utility Method Tests ====================

    @Test
    void stripColors_removesAnsiCodes() {
        String coloredText = "\033[31mError\033[0m";
        String result = ColorUtil.stripColors(coloredText);

        assertThat(result)
                .doesNotContain("\033[")
                .isEqualTo("Error");
    }

    @Test
    void stripColors_withNull_returnsNull() {
        String result = ColorUtil.stripColors(null);
        assertThat(result).isNull();
    }

    @Test
    void stripColors_withNoColors_returnsSameText() {
        String plainText = "Hello World";
        String result = ColorUtil.stripColors(plainText);

        assertThat(result).isEqualTo(plainText);
    }

    @Test
    void lengthWithoutColors_calculatesCorrectLength() {
        String coloredText = "\033[31mError\033[0m";
        int length = ColorUtil.lengthWithoutColors(coloredText);

        assertThat(length).isEqualTo("Error".length());
    }

    @Test
    void lengthWithoutColors_withNull_returnsZero() {
        int length = ColorUtil.lengthWithoutColors(null);
        assertThat(length).isZero();
    }

    // ==================== Terminal Detection Tests ====================

    @Test
    void supportsColors_smokeTest() {
        // Just verify the method doesn't throw
        assertThatCode(ColorUtil::supportsColors).doesNotThrowAnyException();
    }

    // ==================== Integration Style Tests ====================

    @Test
    void allMethods_withColorsDisabled_returnPlainText() {
        ColorUtil.forceDisableColors();

        assertThat(ColorUtil.type("test")).isEqualTo("test");
        assertThat(ColorUtil.string("test")).isEqualTo("test");
        assertThat(ColorUtil.number("42")).isEqualTo("42");
        assertThat(ColorUtil.field("name")).isEqualTo("name");
        assertThat(ColorUtil.keyword("null")).isEqualTo("null");
        assertThat(ColorUtil.error("error")).isEqualTo("error");
        assertThat(ColorUtil.dim("dim")).isEqualTo("dim");
        assertThat(ColorUtil.gray("gray")).isEqualTo("gray");
        assertThat(ColorUtil.green("green")).isEqualTo("green");
        assertThat(ColorUtil.red("red")).isEqualTo("red");
        assertThat(ColorUtil.yellow("yellow")).isEqualTo("yellow");
        assertThat(ColorUtil.cyan("cyan")).isEqualTo("cyan");
        assertThat(ColorUtil.orange("orange")).isEqualTo("orange");
        assertThat(ColorUtil.structural("{")).isEqualTo("{");
        assertThat(ColorUtil.arrow()).isEqualTo(" → ");
        assertThat(ColorUtil.formatType(String.class)).isEqualTo("#String");
        assertThat(ColorUtil.formatType("Integer")).isEqualTo("#Integer");
        assertThat(ColorUtil.formatHeader("Test.java", 1)).isEqualTo("Test.java:1");
        assertThat(ColorUtil.formatTruncated()).isEqualTo("... (truncated)");
        assertThat(ColorUtil.formatCyclic()).isEqualTo("↻ (circular)");
    }

    @Test
    void methods_withEmptyString_handleCorrectly() {
        ColorUtil.forceDisableColors();

        assertThat(ColorUtil.type("")).isEmpty();
        assertThat(ColorUtil.string("")).isEmpty();
        assertThat(ColorUtil.number("")).isEmpty();
        assertThat(ColorUtil.field("")).isEmpty();
        assertThat(ColorUtil.keyword("")).isEmpty();
        assertThat(ColorUtil.error("")).isEmpty();
        assertThat(ColorUtil.dim("")).isEmpty();
        assertThat(ColorUtil.gray("")).isEmpty();
        assertThat(ColorUtil.green("")).isEmpty();
        assertThat(ColorUtil.red("")).isEmpty();
        assertThat(ColorUtil.yellow("")).isEmpty();
        assertThat(ColorUtil.cyan("")).isEmpty();
        assertThat(ColorUtil.orange("")).isEmpty();
        assertThat(ColorUtil.structural("")).isEmpty();
    }

    @Test
    void isColorsEnabled_reflectsCurrentState() {
        // Start with disabled
        ColorUtil.forceDisableColors();
        assertThat(ColorUtil.isColorsEnabled()).isFalse();

        // Enable
        ColorUtil.forceEnableColors();
        assertThat(ColorUtil.isColorsEnabled()).isTrue();

        // Disable again
        ColorUtil.forceDisableColors();
        assertThat(ColorUtil.isColorsEnabled()).isFalse();
    }

    // ==================== Edge Case Tests ====================

    @Test
    void stripColors_performance_onLongStrings() {
        // Build a long colored string
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100; i++) { // Reduced from 1000 to 100 for faster tests
            builder.append("\033[31mError\033[0m");
        }
        String longColoredText = builder.toString();

        // Should strip all colors without issue
        String stripped = ColorUtil.stripColors(longColoredText);

        // Should be 5 * 100 characters ("Error" repeated 100 times)
        assertThat(stripped.length()).isEqualTo(5 * 100);
        assertThat(stripped).doesNotContain("\033[");
    }

    @Test
    void lengthWithoutColors_handlesMixedContent() {
        String mixed = "Plain \033[31mRed\033[0m Plain";
        int length = ColorUtil.lengthWithoutColors(mixed);

        // "Plain Red Plain" = 5 + 1 + 3 + 1 + 5 = 15
        assertThat(length).isEqualTo(15);
    }
}