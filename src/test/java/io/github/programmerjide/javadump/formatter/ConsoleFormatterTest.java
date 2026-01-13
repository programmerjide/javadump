package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.StackTraceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ConsoleFormatter.
 */
class ConsoleFormatterTest {

    private DumperConfig config;
    private ConsoleFormatter formatter;

    @BeforeEach
    void setUp() {
        config = new DumperConfig(15, 100, true);
        formatter = new ConsoleFormatter(config);
    }

    @Test
    @DisplayName("Should format single node")
    void testFormatSingleNode() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("Hello")
                .build();

        String result = formatter.format(Arrays.asList(node), null);
        assertNotNull(result);
        assertTrue(result.contains("Hello"));
    }

    @Test
    @DisplayName("Should format multiple nodes")
    void testFormatMultipleNodes() {
        DumpNode node1 = DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("First")
                .build();

        DumpNode node2 = DumpNode.builder()
                .type(DumpNode.NodeType.NUMBER)
                .typeName("int")
                .value(42)
                .build();

        String result = formatter.format(Arrays.asList(node1, node2), null);
        assertNotNull(result);
        assertTrue(result.contains("First"));
        assertTrue(result.contains("42"));
    }

    @Test
    @DisplayName("Should format call site")
    void testFormatCallSite() {
        StackTraceUtil.CallSite callSite =
                new StackTraceUtil.CallSite("Test.java", "testMethod", 42);

        String result = formatter.formatCallSite(callSite);
        assertNotNull(result);
        assertTrue(result.contains("Test.java"));
        assertTrue(result.contains("42"));
    }

    @Test
    @DisplayName("Should include call site in output")
    void testFormatWithCallSite() {
        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("Test")
                .build();

        StackTraceUtil.CallSite callSite =
                new StackTraceUtil.CallSite("Test.java", "testMethod", 10);

        String result = formatter.format(Arrays.asList(node), callSite);
        assertNotNull(result);
        assertTrue(result.contains("Test.java"));
        assertTrue(result.contains("Test"));
    }

    @Test
    @DisplayName("Should format without colors when disabled")
    void testFormatWithoutColors() {
        DumperConfig noColorConfig = new DumperConfig(15, 100, false);
        ConsoleFormatter noColorFormatter = new ConsoleFormatter(noColorConfig);

        DumpNode node = DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .typeName("String")
                .value("Test")
                .build();

        String result = noColorFormatter.format(Arrays.asList(node), null);
        assertNotNull(result);
        assertFalse(result.contains("\033["));
        assertTrue(result.contains("Test"));
    }

    @Test
    @DisplayName("Should handle empty node list")
    void testFormatEmptyList() {
        String result = formatter.format(Arrays.asList(), null);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    @DisplayName("Should get value formatter")
    void testGetValueFormatter() {
        ValueFormatter valueFormatter = formatter.getValueFormatter();
        assertNotNull(valueFormatter);
    }
}