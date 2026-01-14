package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.ColorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class FormatterTest {

    private Formatter formatter;
    private Formatter formatterNoColor;

    @BeforeEach
    void setUp() {
        // Force colors ON/OFF before creating formatters
        ColorUtil.forceEnableColors();
        DumperConfig config = new DumperConfig(10, 100, true);
        formatter = new Formatter(config);

        ColorUtil.forceDisableColors();
        DumperConfig configNoColor = new DumperConfig(10, 100, false);
        formatterNoColor = new Formatter(configNoColor);
    }

    // ==================== Basic Formatting Tests ====================

    @Test
    void formatNode_null_returnsFormattedNull() {
        DumpNode node = DumpNode.ofNull();
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("null");
    }

    @Test
    void formatNode_primitive_returnsFormattedNumber() {
        DumpNode node = DumpNode.ofPrimitive(42, Integer.class);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("Integer 42");
    }

    @Test
    void formatNode_boolean_returnsFormattedBoolean() {
        // Test with proper boolean factory method
        DumpNode nodeTrue = DumpNode.ofBoolean(true);
        DumpNode nodeFalse = DumpNode.ofBoolean(false);

        String resultTrue = formatterNoColor.formatNode(nodeTrue, 0);
        String resultFalse = formatterNoColor.formatNode(nodeFalse, 0);

        assertThat(resultTrue).isEqualTo("boolean true");
        assertThat(resultFalse).isEqualTo("boolean false");

        // Also test with primitive if you want
        DumpNode nodeTruePrimitive = DumpNode.ofPrimitive(true, boolean.class);
        DumpNode nodeFalsePrimitive = DumpNode.ofPrimitive(false, boolean.class);

        String resultTruePrimitive = formatterNoColor.formatNode(nodeTruePrimitive, 0);
        String resultFalsePrimitive = formatterNoColor.formatNode(nodeFalsePrimitive, 0);

        assertThat(resultTruePrimitive).isEqualTo("boolean true");
        assertThat(resultFalsePrimitive).isEqualTo("boolean false");
    }

    @Test
    void formatNode_string_returnsQuotedString() {
        DumpNode node = DumpNode.ofString("Hello World");
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("\"Hello World\"");
    }

    @Test
    void formatNode_enum_returnsFormattedEnum() {
        DumpNode node = DumpNode.ofEnum(TestEnum.VALUE_A);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("TestEnum.VALUE_A");
    }

    // ==================== Array Formatting Tests ====================

    @Test
    void formatNode_emptyArray_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofArray(int[].class, List.of(), 0, false);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("#int[][0] {}");
    }

    @Test
    void formatNode_arrayWithElements_returnsFormattedArray() {
        List<DumpNode> elements = List.of(
                DumpNode.ofPrimitive(1, Integer.class),
                DumpNode.ofPrimitive(2, Integer.class),
                DumpNode.ofPrimitive(3, Integer.class)
        );

        DumpNode node = DumpNode.ofArray(int[].class, elements, 3, false);
        String result = formatterNoColor.formatNode(node, 0);

        // Check the exact format
        String expected = "#int[][3] {\n" +
                "  0 → Integer 1\n" +
                "  1 → Integer 2\n" +
                "  2 → Integer 3\n" +
                "}";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void formatNode_truncatedArray_showsTruncationMarker() {
        List<DumpNode> elements = List.of(
                DumpNode.ofPrimitive(1, Integer.class),
                DumpNode.ofPrimitive(2, Integer.class)
        );

        DumpNode node = DumpNode.ofArray(int[].class, elements, 100, true);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("#int[][100] {")
                .contains("... (truncated)")
                .contains("0 → Integer 1")
                .contains("1 → Integer 2");
    }

    // ==================== Collection Formatting Tests ====================

    @Test
    void formatNode_emptyList_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofCollection(ArrayList.class, List.of(), 0, false);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("#ArrayList[0] {}");
    }

    @Test
    void formatNode_listWithElements_returnsFormattedList() {
        List<DumpNode> elements = List.of(
                DumpNode.ofString("a"),
                DumpNode.ofString("b"),
                DumpNode.ofString("c")
        );

        DumpNode node = DumpNode.ofCollection(ArrayList.class, elements, 3, false);
        String result = formatterNoColor.formatNode(node, 0);

        String expected = "#ArrayList[3] {\n" +
                "  0 → \"a\"\n" +
                "  1 → \"b\"\n" +
                "  2 → \"c\"\n" +
                "}";
        assertThat(result).isEqualTo(expected);
    }

    // ==================== Map Formatting Tests ====================

    @Test
    void formatNode_emptyMap_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofMap(HashMap.class, Map.of(), 0, false);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("#HashMap[0] {}");
    }

    @Test
    void formatNode_mapWithEntries_returnsFormattedMap() {
        Map<DumpNode, DumpNode> entries = new LinkedHashMap<>();
        entries.put(DumpNode.ofString("key1"), DumpNode.ofPrimitive(1, Integer.class));
        entries.put(DumpNode.ofString("key2"), DumpNode.ofPrimitive(2, Integer.class));

        DumpNode node = DumpNode.ofMap(HashMap.class, entries, 2, false);
        String result = formatterNoColor.formatNode(node, 0);

        String expected = "#HashMap[2] {\n" +
                "  \"key1\" → Integer 1\n" +
                "  \"key2\" → Integer 2\n" +
                "}";
        assertThat(result).isEqualTo(expected);
    }

    // ==================== Object Formatting Tests ====================

    @Test
    void formatNode_emptyObject_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofObject(TestClass.class, Map.of());
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("#TestClass {}");
    }

    @Test
    void formatNode_objectWithFields_returnsFormattedObject() {
        Map<String, DumpNode> fields = new LinkedHashMap<>();
        fields.put("name", DumpNode.ofString("Alice"));
        fields.put("age", DumpNode.ofPrimitive(30, Integer.class));

        DumpNode node = DumpNode.ofObject(TestClass.class, fields);
        String result = formatterNoColor.formatNode(node, 0);

        String expected = "#TestClass {\n" +
                "  name → \"Alice\"\n" +
                "  age → Integer 30\n" +
                "}";
        assertThat(result).isEqualTo(expected);
    }

    // ==================== Special Markers Tests ====================

    @Test
    void formatNode_cyclic_returnsCyclicMarker() {
        DumpNode node = DumpNode.cyclic(TestClass.class);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("↻ (circular)");
    }

    @Test
    void formatNode_truncated_returnsTruncatedMarker() {
        DumpNode node = DumpNode.truncated();
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("... (truncated)");
    }

    @Test
    void formatNode_error_returnsErrorMarker() {
        DumpNode node = DumpNode.ofError("Access denied");
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).isEqualTo("[ERROR: Access denied]");
    }

    // ==================== Color Tests ====================

    @Test
    void format_withColorEnabled_containsAnsiCodes() {
        ColorUtil.forceEnableColors();

        DumpNode node = DumpNode.ofString("test");
        String result = formatter.formatNode(node, 0);

        // Should contain ANSI escape codes
        assertThat(result).contains("\033[");
    }

    @Test
    void format_withColorDisabled_doesNotContainAnsiCodes() {
        DumpNode node = DumpNode.ofString("test");
        String result = formatterNoColor.formatNode(node, 0);

        // Should NOT contain ANSI escape codes
        assertThat(result).doesNotContain("\033[");
    }

    // ==================== Multiple Values Tests ====================

    @Test
    void format_multipleNodes_formatsEachNode() {
        DumpNode node1 = DumpNode.ofPrimitive(42, Integer.class);
        DumpNode node2 = DumpNode.ofString("test");

        String result = formatterNoColor.format(node1, node2);

        // Remove header line (call site) from comparison
        String[] lines = result.split("\n");
        if (lines.length > 2) {
            result = lines[1] + "\n" + lines[2];
        }

        assertThat(result).contains("Integer 42").contains("\"test\"");
    }

    @Test
    void format_emptyArray_returnsEmptyString() {
        String result = formatterNoColor.format();

        assertThat(result).isEmpty();
    }

    @Test
    void format_nullArray_returnsEmptyString() {
        String result = formatterNoColor.format((DumpNode[]) null);

        assertThat(result).isEmpty();
    }

    // ==================== Indentation Tests ====================

    @Test
    void formatNode_nestedObject_hasProperIndentation() {
        Map<String, DumpNode> innerFields = Map.of(
                "value", DumpNode.ofPrimitive(42, Integer.class)
        );
        DumpNode innerObject = DumpNode.ofObject(InnerClass.class, innerFields);

        Map<String, DumpNode> outerFields = Map.of(
                "inner", innerObject
        );
        DumpNode outerObject = DumpNode.ofObject(OuterClass.class, outerFields);

        String result = formatterNoColor.formatNode(outerObject, 0);

        String expected = "#OuterClass {\n" +
                "  inner → #InnerClass {\n" +
                "    value → Integer 42\n" +
                "  }\n" +
                "}";
        assertThat(result).isEqualTo(expected);
    }

    // ==================== Test Classes ====================

    private enum TestEnum {
        VALUE_A, VALUE_B
    }

    private static class TestClass {
        String name;
        int age;
    }

    private static class OuterClass {
        InnerClass inner;
    }

    private static class InnerClass {
        int value;
    }
}