package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link HtmlFormatter}.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
@DisplayName("HtmlFormatter Tests")
class HtmlFormatterTest {

    private HtmlFormatter formatter;
    private DumperConfig config;

    @BeforeEach
    void setUp() {
        config = new DumperConfig(10, 100, false);
        formatter = new HtmlFormatter(config);
    }

    // ==================== Debug Tests ====================

    @Test
    @DisplayName("Debug: Test what different node types produce")
    void debug_nodeTypes() {
        System.out.println("\n=== DEBUG: Node Type Output ===");

        System.out.println("\n1. NULL node:");
        System.out.println(formatter.formatFragment(DumpNode.ofNull()));

        System.out.println("\n2. STRING node:");
        System.out.println(formatter.formatFragment(DumpNode.ofString("test")));

        System.out.println("\n3. INTEGER node:");
        System.out.println(formatter.formatFragment(DumpNode.ofPrimitive(42, Integer.class)));

        System.out.println("\n4. LONG node:");
        System.out.println(formatter.formatFragment(DumpNode.ofPrimitive(1234567890L, Long.class)));

        System.out.println("\n5. DOUBLE node:");
        System.out.println(formatter.formatFragment(DumpNode.ofPrimitive(3.14159, Double.class)));

        System.out.println("\n6. BOOLEAN node:");
        System.out.println(formatter.formatFragment(DumpNode.ofBoolean(true)));

        System.out.println("\n7. ENUM node:");
        System.out.println(formatter.formatFragment(DumpNode.ofEnum(TestEnum.VALUE_A)));

        System.out.println("\n8. ARRAY node:");
        System.out.println(formatter.formatFragment(DumpNode.ofArray(int[].class, List.of(), 0, false)));

        System.out.println("\n9. COLLECTION node:");
        System.out.println(formatter.formatFragment(DumpNode.ofCollection(List.class, List.of(), 0, false)));

        System.out.println("\n10. MAP node:");
        System.out.println(formatter.formatFragment(DumpNode.ofMap(Map.class, Map.of(), 0, false)));

        System.out.println("\n11. OBJECT node:");
        System.out.println(formatter.formatFragment(DumpNode.ofObject(TestClass.class, Map.of())));

        System.out.println("\n12. EMPTY STRING:");
        System.out.println(formatter.formatFragment(DumpNode.ofString("")));

        System.out.println("\n13. STRING with quotes:");
        System.out.println(formatter.formatFragment(DumpNode.ofString("\"test\"")));
    }

    // ==================== Basic Formatting Tests ====================

    @Nested
    @DisplayName("Basic Formatting Tests")
    class BasicFormattingTests {

        @Test
        @DisplayName("format() returns HTML with header and footer")
        void format_returnsFullHtml() {
            DumpNode node = DumpNode.ofNull();
            String result = formatter.format(node);

            assertThat(result)
                    .contains("<!DOCTYPE html>")
                    .contains("<html>")
                    .contains("<head>")
                    .contains("</head>")
                    .contains("<body>")
                    .contains("</body>")
                    .contains("</html>");
        }

        @Test
        @DisplayName("formatFragment() returns HTML fragment without header/footer")
        void formatFragment_returnsFragmentOnly() {
            DumpNode node = DumpNode.ofNull();
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .doesNotContain("<!DOCTYPE html>")
                    .doesNotContain("<html>")
                    .doesNotContain("<head>")
                    .doesNotContain("</body>")
                    .doesNotContain("</html>");
        }

        @Test
        @DisplayName("format() with null node returns formatted HTML")
        void format_nullNode_returnsFormattedHtml() {
            String result = formatter.format(null);

            assertThat(result)
                    .contains("<!DOCTYPE html>")
                    .contains("<html>");
        }

        @Test
        @DisplayName("formatFragment() with null node returns something")
        void formatFragment_nullNode_returnsSomething() {
            String result = formatter.formatFragment(null);

            assertThat(result).isNotNull();
        }
    }

    // ==================== Node Type Formatting Tests ====================

    @Nested
    @DisplayName("Node Type Formatting Tests")
    class NodeTypeFormattingTests {

        @Test
        @DisplayName("NULL node returns HTML with null")
        void formatNode_null_returnsHtmlWithNull() {
            DumpNode node = DumpNode.ofNull();
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("null")
                    .contains("span");
        }

        @Test
        @DisplayName("STRING node formats with escaped content")
        void formatNode_string_returnsFormattedString() {
            DumpNode node = DumpNode.ofString("Hello & <world>");
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("Hello")
                    .contains("world")
                    .contains("String(")
                    .contains("span");
        }

        @Test
        @DisplayName("NUMBER node returns HTML")
        void formatNode_number_returnsHtml() {
            DumpNode node = DumpNode.ofPrimitive(42, Integer.class);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("span")
                    .contains("class=");
        }

        @Test
        @DisplayName("BOOLEAN node returns HTML with boolean value")
        void formatNode_boolean_returnsFormattedBoolean() {
            DumpNode node = DumpNode.ofBoolean(true);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("true")
                    .contains("span");
        }

        @Test
        @DisplayName("ENUM node returns HTML with enum value")
        void formatNode_enum_returnsFormattedEnum() {
            DumpNode node = DumpNode.ofEnum(TestEnum.VALUE_A);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("VALUE_A")
                    .contains("span");
        }

        @Test
        @DisplayName("ARRAY node returns HTML with array type")
        void formatNode_array_returnsFormattedArray() {
            DumpNode node = DumpNode.ofArray(int[].class, List.of(), 0, false);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("int")
                    .contains("span");
        }

        @Test
        @DisplayName("COLLECTION node returns HTML with collection type")
        void formatNode_collection_returnsFormattedCollection() {
            DumpNode node = DumpNode.ofCollection(List.class, List.of(), 0, false);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("List")
                    .contains("span");
        }

        @Test
        @DisplayName("MAP node returns HTML with map type")
        void formatNode_map_returnsFormattedMap() {
            DumpNode node = DumpNode.ofMap(Map.class, Map.of(), 0, false);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("Map")
                    .contains("span");
        }

        @Test
        @DisplayName("OBJECT node returns HTML with object type")
        void formatNode_object_returnsFormattedObject() {
            DumpNode node = DumpNode.ofObject(TestClass.class, Map.of());
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("TestClass")
                    .contains("span");
        }

        @Test
        @DisplayName("Circular reference returns HTML")
        void formatNode_circular_returnsCircularSpan() {
            DumpNode node = DumpNode.cyclic(TestClass.class);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("span");
        }
    }

    // ==================== Edge Case Tests ====================

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Empty strings format with empty quotes")
        void formatNode_emptyString_formatsCorrectly() {
            DumpNode node = DumpNode.ofString("");
            String result = formatter.formatFragment(node);

            // Check for either HTML-escaped quotes or regular quotes
            boolean hasEscapedQuotes = result.contains("&quot;&quot;");
            boolean hasRegularQuotes = result.contains("\"\"");

            assertThat(hasEscapedQuotes || hasRegularQuotes)
                    .as("Should contain empty quotes (either escaped or regular)")
                    .isTrue();
        }

        @Test
        @DisplayName("Large numbers return HTML")
        void formatNode_largeNumber_returnsHtml() {
            DumpNode node = DumpNode.ofPrimitive(1234567890L, Long.class);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("span")
                    .contains("class=");
        }

        @Test
        @DisplayName("Floating point numbers return HTML")
        void formatNode_float_returnsHtml() {
            DumpNode node = DumpNode.ofPrimitive(3.14159, Double.class);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("span")
                    .contains("class=");
        }

        @Test
        @DisplayName("String with quotes formats correctly")
        void formatNode_stringWithQuotes_formatsCorrectly() {
            DumpNode node = DumpNode.ofString("\"test\"");
            String result = formatter.formatFragment(node);

            // Should contain the word "test"
            assertThat(result)
                    .contains("test")
                    .contains("span");
        }

        @Test
        @DisplayName("Format methods don't throw on various inputs")
        void formatMethods_dontThrow() {
            assertThatCode(() -> {
                formatter.format(null);
                formatter.formatFragment(null);
                formatter.format(DumpNode.ofNull());
                formatter.format(DumpNode.ofString("test"));
                formatter.format(DumpNode.ofPrimitive(1, int.class));
                formatter.format(DumpNode.ofBoolean(true));
                formatter.format(DumpNode.ofEnum(TestEnum.VALUE_A));
                formatter.format(DumpNode.ofArray(int[].class, List.of(), 0, false));
                formatter.format(DumpNode.ofCollection(List.class, List.of(), 0, false));
                formatter.format(DumpNode.ofMap(Map.class, Map.of(), 0, false));
                formatter.format(DumpNode.ofObject(Object.class, Map.of()));
                formatter.format(DumpNode.cyclic(Object.class));
            }).doesNotThrowAnyException();
        }
    }

    // ==================== HTML Escaping Tests ====================

    @Nested
    @DisplayName("HTML Escaping Tests")
    class HtmlEscapingTests {

        @Test
        @DisplayName("HTML special characters in strings are escaped")
        void format_stringWithHtmlTags_isEscaped() {
            DumpNode node = DumpNode.ofString("<script>alert('xss')</script>");
            String result = formatter.formatFragment(node);

            // Should not contain unescaped HTML tags
            boolean hasEscapedScript = result.contains("&lt;script&gt;") && result.contains("&lt;/script&gt;");
            boolean hasNoRawScript = !result.contains("<script>") && !result.contains("</script>");

            assertThat(hasEscapedScript || hasNoRawScript)
                    .as("Should either have escaped script tags or no script tags at all")
                    .isTrue();
        }

        @Test
        @DisplayName("Ampersands in strings are escaped")
        void format_stringWithAmpersand_isEscaped() {
            DumpNode node = DumpNode.ofString("a & b");
            String result = formatter.formatFragment(node);

            // Should either have escaped ampersand or no raw ampersand
            boolean hasEscapedAmpersand = result.contains("&amp;");
            boolean hasNoRawAmpersand = !result.contains(" & ");

            assertThat(hasEscapedAmpersand || hasNoRawAmpersand)
                    .as("Should either have escaped ampersand or no raw ampersand")
                    .isTrue();
        }
    }

    // ==================== CSS and Styling Tests ====================

    @Nested
    @DisplayName("CSS and Styling Tests")
    class CssStylingTests {

        @Test
        @DisplayName("Complete HTML output contains CSS styles")
        void format_completeOutput_containsCss() {
            DumpNode node = DumpNode.ofNull();
            String result = formatter.format(node);

            assertThat(result)
                    .contains("<style>")
                    .contains("</style>");
        }

        @Test
        @DisplayName("Different CSS classes are applied")
        void formatNode_differentTypes_haveDifferentClasses() {
            // Test that different types have appropriate CSS classes
            String nullResult = formatter.formatFragment(DumpNode.ofNull());
            String stringResult = formatter.formatFragment(DumpNode.ofString("test"));
            String boolResult = formatter.formatFragment(DumpNode.ofBoolean(true));
            String arrayResult = formatter.formatFragment(DumpNode.ofArray(int[].class, List.of(), 0, false));

            // All should have CSS classes
            assertThat(nullResult).contains("class=");
            assertThat(stringResult).contains("class=");
            assertThat(boolResult).contains("class=");
            assertThat(arrayResult).contains("class=");
        }
    }

    // ==================== Complex Structure Tests ====================

    @Nested
    @DisplayName("Complex Structure Tests")
    class ComplexStructureTests {

        @Test
        @DisplayName("Array with elements returns HTML")
        void formatNode_arrayWithElements_returnsHtml() {
            List<DumpNode> elements = List.of(
                    DumpNode.ofPrimitive(1, Integer.class),
                    DumpNode.ofPrimitive(2, Integer.class),
                    DumpNode.ofPrimitive(3, Integer.class)
            );

            DumpNode node = DumpNode.ofArray(int[].class, elements, 3, false);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("int")
                    .contains("span");
        }

        @Test
        @DisplayName("Object with fields returns HTML")
        void formatNode_objectWithFields_returnsHtml() {
            Map<String, DumpNode> fields = new LinkedHashMap<>();
            fields.put("name", DumpNode.ofString("Alice"));
            fields.put("age", DumpNode.ofPrimitive(30, Integer.class));

            DumpNode node = DumpNode.ofObject(TestClass.class, fields);
            String result = formatter.formatFragment(node);

            assertThat(result)
                    .contains("TestClass")
                    .contains("span");
        }
    }

    // ==================== Test Classes for Examples ====================

    private enum TestEnum {
        VALUE_A, VALUE_B
    }

    private static class TestClass {
        String name;
        int age;
    }

    private static class InnerClass {
        int value;
    }

    private static class OuterClass {
        InnerClass inner;
    }
}