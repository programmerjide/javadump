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
        DumperConfig config = new DumperConfig(10, 100, true);
        DumperConfig configNoColor = new DumperConfig(10, 100, false);

        formatter = new Formatter(config);
        formatterNoColor = new Formatter(configNoColor);
    }

    // ==================== Basic Formatting Tests ====================

    @Test
    void formatNode_null_returnsFormattedNull() {
        DumpNode node = DumpNode.ofNull();
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).contains("null");
    }

    @Test
    void formatNode_primitive_returnsFormattedNumber() {
        DumpNode node = DumpNode.ofPrimitive(42, Integer.class);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).contains("42");
    }

    @Test
    void formatNode_boolean_returnsFormattedBoolean() {
        DumpNode nodeTrue = DumpNode.ofPrimitive(true, Boolean.class);
        DumpNode nodeFalse = DumpNode.ofPrimitive(false, Boolean.class);

        String resultTrue = formatterNoColor.formatNode(nodeTrue, 0);
        String resultFalse = formatterNoColor.formatNode(nodeFalse, 0);

        assertThat(resultTrue).contains("true");
        assertThat(resultFalse).contains("false");
    }

    @Test
    void formatNode_string_returnsQuotedString() {
        DumpNode node = DumpNode.ofString("Hello World");
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).contains("\"Hello World\"");
    }

    @Test
    void formatNode_enum_returnsFormattedEnum() {
        DumpNode node = DumpNode.ofEnum(TestEnum.VALUE_A);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("TestEnum")
                .contains("VALUE_A");
    }

    // ==================== Array Formatting Tests ====================

    @Test
    void formatNode_emptyArray_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofArray(int[].class, List.of(), 0, false);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("int[]")
                .contains("[0]")
                .contains("{}");
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

        assertThat(result)
                .contains("int[]")
                .contains("[3]")
                .contains("0")
                .contains("1")
                .contains("2");
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
                .contains("[100]")
                .contains("(truncated)");
    }

    // ==================== Collection Formatting Tests ====================

    @Test
    void formatNode_emptyList_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofCollection(ArrayList.class, List.of(), 0, false);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("ArrayList")
                .contains("[0]")
                .contains("{}");
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

        assertThat(result)
                .contains("ArrayList")
                .contains("[3]")
                .contains("\"a\"")
                .contains("\"b\"")
                .contains("\"c\"");
    }

    // ==================== Map Formatting Tests ====================

    @Test
    void formatNode_emptyMap_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofMap(HashMap.class, Map.of(), 0, false);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("HashMap")
                .contains("[0]")
                .contains("{}");
    }

    @Test
    void formatNode_mapWithEntries_returnsFormattedMap() {
        Map<DumpNode, DumpNode> entries = new LinkedHashMap<>();
        entries.put(DumpNode.ofString("key1"), DumpNode.ofPrimitive(1, Integer.class));
        entries.put(DumpNode.ofString("key2"), DumpNode.ofPrimitive(2, Integer.class));

        DumpNode node = DumpNode.ofMap(HashMap.class, entries, 2, false);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("HashMap")
                .contains("[2]")
                .contains("key1")
                .contains("key2");
    }

    // ==================== Object Formatting Tests ====================

    @Test
    void formatNode_emptyObject_returnsEmptyBraces() {
        DumpNode node = DumpNode.ofObject(TestClass.class, Map.of());
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("TestClass")
                .contains("{}");
    }

    @Test
    void formatNode_objectWithFields_returnsFormattedObject() {
        Map<String, DumpNode> fields = new LinkedHashMap<>();
        fields.put("name", DumpNode.ofString("Alice"));
        fields.put("age", DumpNode.ofPrimitive(30, Integer.class));

        DumpNode node = DumpNode.ofObject(TestClass.class, fields);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("TestClass")
                .contains("name")
                .contains("\"Alice\"")
                .contains("age")
                .contains("30");
    }

    // ==================== Special Markers Tests ====================

    @Test
    void formatNode_cyclic_returnsCyclicMarker() {
        DumpNode node = DumpNode.cyclic(TestClass.class);
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("TestClass")
                .contains("(cyclic)");
    }

    @Test
    void formatNode_truncated_returnsTruncatedMarker() {
        DumpNode node = DumpNode.truncated();
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result).contains("(truncated)");
    }

    @Test
    void formatNode_error_returnsErrorMarker() {
        DumpNode node = DumpNode.ofError("Access denied");
        String result = formatterNoColor.formatNode(node, 0);

        assertThat(result)
                .contains("error")
                .contains("Access denied");
    }

    // ==================== Color Tests ====================

    @Test
    void format_withColorEnabled_containsAnsiCodes() {
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

        assertThat(result)
                .contains("42")
                .contains("\"test\"");
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

        assertThat(result)
                .contains("OuterClass")
                .contains("inner")
                .contains("InnerClass")
                .contains("value")
                .contains("42");
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