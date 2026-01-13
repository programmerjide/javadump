//package io.github.programmerjide.javadump.formatter;
//
//import io.github.programmerjide.javadump.model.DumpNode;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.*;
//
//class HtmlFormatterTest {
//
//    private HtmlFormatter htmlFormatter;
//
//    @BeforeEach
//    void setUp() {
//        htmlFormatter = new HtmlFormatter();
//    }
//
//    // ==================== Basic HTML Tests ====================
//
//    @Test
//    void format_singleNode_returnsHtmlWithCss() {
//        DumpNode node = DumpNode.ofPrimitive(42, Integer.class);
//        String result = htmlFormatter.format(node);
//
//        assertThat(result)
//                .contains("<style>")
//                .contains("</style>")
//                .contains("<div class=\"dump-container\">")
//                .contains("</div>")
//                .contains("42");
//    }
//
//    @Test
//    void format_null_returnsFormattedNull() {
//        DumpNode node = DumpNode.ofNull();
//        String result = htmlFormatter.format(node);
//
//        assertThat(result)
//                .contains("dump-null")
//                .contains("null");
//    }
//
//    @Test
//    void format_string_returnsQuotedStringInHtml() {
//        DumpNode node = DumpNode.ofString("Hello");
//        String result = htmlFormatter.format(node);
//
//        assertThat(result)
//                .contains("dump-string")
//                .contains("&quot;Hello&quot;");
//    }
//
//    @Test
//    void format_boolean_returnsColoredBoolean() {
//        DumpNode nodeTrue = DumpNode.ofPrimitive(true, Boolean.class);
//        DumpNode nodeFalse = DumpNode.ofPrimitive(false, Boolean.class);
//
//        String resultTrue = htmlFormatter.format(nodeTrue);
//        String resultFalse = htmlFormatter.format(nodeFalse);
//
//        assertThat(resultTrue)
//                .contains("dump-boolean-true")
//                .contains("true");
//
//        assertThat(resultFalse)
//                .contains("dump-boolean-false")
//                .contains("false");
//    }
//
//    // ==================== HTML Escaping Tests ====================
//
//    @Test
//    void format_stringWithHtmlChars_escapesHtmlProperly() {
//        DumpNode node = DumpNode.ofString("<script>alert('xss')</script>");
//        String result = htmlFormatter.format(node);
//
//        assertThat(result)
//                .contains("&lt;script&gt;")
//                .contains("&lt;/script&gt;")
//                .doesNotContain("<script>");
//    }
//
//    // ==================== Complex Structure Tests ====================
//
//    @Test
//    void format_objectWithFields_returnsFormattedHtml() {
//        Map<String, DumpNode> fields = new LinkedHashMap<>();
//        fields.put("name", DumpNode.ofString("Alice"));
//        fields.put("age", DumpNode.ofPrimitive(30, Integer.class));
//
//        DumpNode node = DumpNode.ofObject(TestClass.class, fields);
//        String result = htmlFormatter.format(node);
//
//        assertThat(result)
//                .contains("dump-type")
//                .contains("TestClass")
//                .contains("dump-key")
//                .contains("name")
//                .contains("age");
//    }
//
//    @Test
//    void format_array_returnsFormattedHtmlArray() {
//        List<DumpNode> elements = List.of(
//                DumpNode.ofPrimitive(1, Integer.class),
//                DumpNode.ofPrimitive(2, Integer.class)
//        );
//
//        DumpNode node = DumpNode.ofArray(int[].class, elements, 2, false);
//        String result = htmlFormatter.format(node);
//
//        assertThat(result)
//                .contains("int[]")
//                .contains("[2]")
//                .contains("dump-number");
//    }
//
//    // ==================== CSS Class Tests ====================
//
//    @Test
//    void format_multipleNodeTypes_usesCorrectCssClasses() {
//        DumpNode node = DumpNode.ofString("test");
//        String result = htmlFormatter.format(node);
//
//        assertThat(result)
//                .contains("class=\"dump-string\"")
//                .contains("class=\"dump-container\"");
//    }
//
//    // ==================== Test Classes ====================
//
//    private static class TestClass {
//        String name;
//        int age;
//    }
//}
//
