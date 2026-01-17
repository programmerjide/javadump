package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ValueFormatter.
 */
class ValueFormatterTest {

    private DumperConfig config;
    private ValueFormatter formatter;

    @BeforeEach
    void setUp() {
        config = new DumperConfig(15, 100, true);
        formatter = new ValueFormatter(config);
    }

    @Test
    @DisplayName("Should format null")
    void testFormatNull() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.NULL)
                .typeName("null")
                .value(null)
                .build();

        String result = formatter.format(node, 0);
        assertTrue(result.contains("null"));
    }

    @Test
    @DisplayName("Should format string")
    void testFormatString() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("Hello World")
                .build();

        String result = formatter.format(node, 0);
        assertTrue(result.contains("Hello World"));
        assertTrue(result.contains("11")); // length
    }

    @Test
    @DisplayName("Should format number")
    void testFormatNumber() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.NUMBER)
                .typeName("int")
                .value(42)
                .build();

        String result = formatter.format(node, 0);
        assertTrue(result.contains("42"));
        assertTrue(result.contains("int"));
    }

    @Test
    @DisplayName("Should format boolean")
    void testFormatBoolean() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.BOOLEAN)
                .typeName("boolean")
                .value(true)
                .build();

        // FIXED: Call the correct public API method
        String result = formatter.format(node, 0);
        assertTrue(result.contains("true"));
        assertTrue(result.contains("boolean"));
    }

    @Test
    @DisplayName("Should format collection")
    void testFormatCollection() {
        Map<String, DumpNode> children = new HashMap<>();
        children.put("0", DumpNode.builder()
                .type(DumpNode.NodeType.NUMBER)
                .typeName("int")
                .value(1)
                .build());
        children.put("1", DumpNode.builder()
                .type(DumpNode.NodeType.NUMBER)
                .typeName("int")
                .value(2)
                .build());

        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.COLLECTION)
                .typeName("ArrayList")
                .children(children)
                .build();

        // FIXED: Call the correct public API method
        String result = formatter.format(node, 0);
        assertTrue(result.contains("ArrayList"));
        assertTrue(result.contains("["));
        assertTrue(result.contains("]"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
    }

    @Test
    @DisplayName("Should format map")
    void testFormatMap() {
        Map<String, DumpNode> children = new HashMap<>();
        children.put("\"key1\"", DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("value1")
                .build());

        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.MAP)
                .typeName("HashMap")
                .children(children)
                .build();

        // FIXED: Call the correct public API method
        String result = formatter.format(node, 0);
        assertTrue(result.contains("HashMap"));
        assertTrue(result.contains("{"));
        assertTrue(result.contains("}"));
        assertTrue(result.contains("key1"));
        assertTrue(result.contains("value1"));
    }

    @Test
    @DisplayName("Should format object")
    void testFormatObject() {
        Map<String, DumpNode> children = new HashMap<>();
        children.put("name", DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("John")
                .build());
        children.put("age", DumpNode.builder()
                .type(DumpNode.NodeType.NUMBER)
                .typeName("int")
                .value(30)
                .build());

        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.OBJECT)
                .typeName("Person")
                .children(children)
                .build();

        // FIXED: Call the correct public API method
        String result = formatter.format(node, 0);
        assertTrue(result.contains("Person"));
        assertTrue(result.contains("name"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("age"));
        assertTrue(result.contains("30"));
    }

    @Test
    @DisplayName("Should format circular reference")
    void testFormatCircular() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.OBJECT)
                .typeName("Node")
                .circular(true)
                .build();

        String result = formatter.format(node, 0);
        assertTrue(result.contains("CIRCULAR"));
    }

    @Test
    @DisplayName("Should format max depth reached")
    void testFormatMaxDepth() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.OBJECT)
                .typeName("Deep")
                .maxDepthReached(true)
                .build();

        String result = formatter.format(node, 0);
        assertTrue(result.contains("MAX DEPTH"));
    }

    @Test
    @DisplayName("Should truncate large collections")
    void testTruncateLargeCollections() {
        DumperConfig limitedConfig = new DumperConfig(15, 3, true);
        ValueFormatter limitedFormatter = new ValueFormatter(limitedConfig);

        Map<String, DumpNode> children = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            children.put(String.valueOf(i), DumpNode.builder()
                    .type(DumpNode.NodeType.NUMBER)
                    .typeName("int")
                    .value(i)
                    .build());
        }

        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.COLLECTION)
                .typeName("ArrayList")
                .children(children)
                .build();

        // FIXED: Call the correct public API method
        String result = limitedFormatter.format(node, 0);
        assertTrue(result.contains("more items") || result.contains("..."));
    }

    @Test
    @DisplayName("Should format without colors when disabled")
    void testFormatWithoutColors() {
        DumperConfig noColorConfig = new DumperConfig(15, 100, false);
        ValueFormatter noColorFormatter = new ValueFormatter(noColorConfig);

        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("Test")
                .build();

        String result = noColorFormatter.format(node, 0);
        assertFalse(result.contains("\033["));
        assertTrue(result.contains("Test"));
    }

    // Additional tests for other public methods
    @Test
    @DisplayName("Should format null via formatNull()")
    void testFormatNullMethod() {
        String result = formatter.formatNull();
        assertTrue(result.contains("null"));
    }

    @Test
    @DisplayName("Should format circular via formatCircular()")
    void testFormatCircularMethod() {
        String result = formatter.formatCircular();
        assertTrue(result.contains("CIRCULAR"));
    }

    @Test
    @DisplayName("Should format max depth via formatMaxDepth()")
    void testFormatMaxDepthMethod() {
        String result = formatter.formatMaxDepth();
        assertTrue(result.contains("MAX DEPTH"));
    }
}