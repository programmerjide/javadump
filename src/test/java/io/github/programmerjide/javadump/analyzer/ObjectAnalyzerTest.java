package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class ObjectAnalyzerTest {

    private ObjectAnalyzer analyzer;
    private DumperConfig config;

    @BeforeEach
    void setUp() {
        config = new DumperConfig(10, 100, true);
        analyzer = new ObjectAnalyzer(config);
    }

    // ==================== Null Tests ====================

    @Test
    void analyze_null_returnsNullNode() {
        DumpNode node = analyzer.analyze(null, 0);

        assertThat(node).isNotNull();
        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.NULL);
    }

    // ==================== Primitive Tests ====================

    @Test
    void analyze_intPrimitive_returnsPrimitiveNode() {
        DumpNode node = analyzer.analyze(42, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.PRIMITIVE);
        assertThat(node.getValue()).isEqualTo(42);
    }

    @Test
    void analyze_booleanPrimitive_returnsPrimitiveNode() {
        DumpNode node = analyzer.analyze(true, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.PRIMITIVE);
        assertThat(node.getValue()).isEqualTo(true);
    }

    @Test
    void analyze_doublePrimitive_returnsPrimitiveNode() {
        DumpNode node = analyzer.analyze(3.14, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.PRIMITIVE);
        assertThat(node.getValue()).isEqualTo(3.14);
    }

    // ==================== String Tests ====================

    @Test
    void analyze_string_returnsStringNode() {
        DumpNode node = analyzer.analyze("Hello", 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.STRING);
        assertThat(node.getValue()).isEqualTo("Hello");
    }

    @Test
    void analyze_emptyString_returnsStringNode() {
        DumpNode node = analyzer.analyze("", 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.STRING);
        assertThat(node.getValue()).isEqualTo("");
    }

    // ==================== Enum Tests ====================

    @Test
    void analyze_enum_returnsEnumNode() {
        DumpNode node = analyzer.analyze(TestEnum.VALUE_A, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.ENUM);
        assertThat(node.getValue()).isEqualTo(TestEnum.VALUE_A);
    }

    // ==================== Array Tests ====================

    @Test
    void analyze_intArray_returnsArrayNode() {
        int[] array = {1, 2, 3};
        DumpNode node = analyzer.analyze(array, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.ARRAY);
        assertThat(node.getElements()).hasSize(3);
    }

    @Test
    void analyze_stringArray_returnsArrayNode() {
        String[] array = {"a", "b", "c"};
        DumpNode node = analyzer.analyze(array, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.ARRAY);
        assertThat(node.getElements()).hasSize(3);
    }

    @Test
    void analyze_largeArray_truncatesAtMaxItems() {
        int[] array = new int[200];
        DumpNode node = analyzer.analyze(array, 0);

        assertThat(node.getElements()).hasSizeLessThanOrEqualTo(config.getMaxItems());
        assertThat(node.isTruncated()).isTrue();
    }

    @Test
    void analyze_emptyArray_returnsEmptyArrayNode() {
        int[] array = {};
        DumpNode node = analyzer.analyze(array, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.ARRAY);
        assertThat(node.getElements()).isEmpty();
    }

    // ==================== Collection Tests ====================

    @Test
    void analyze_list_returnsCollectionNode() {
        List<String> list = Arrays.asList("a", "b", "c");
        DumpNode node = analyzer.analyze(list, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.COLLECTION);
        assertThat(node.getElements()).hasSize(3);
    }

    @Test
    void analyze_set_returnsCollectionNode() {
        Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));
        DumpNode node = analyzer.analyze(set, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.COLLECTION);
        assertThat(node.getElements()).hasSize(3);
    }

    @Test
    void analyze_largeList_truncatesAtMaxItems() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            list.add(i);
        }

        DumpNode node = analyzer.analyze(list, 0);

        assertThat(node.getElements()).hasSizeLessThanOrEqualTo(config.getMaxItems());
        assertThat(node.isTruncated()).isTrue();
    }

    @Test
    void analyze_emptyList_returnsEmptyCollectionNode() {
        List<String> list = Collections.emptyList();
        DumpNode node = analyzer.analyze(list, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.COLLECTION);
        assertThat(node.getElements()).isEmpty();
    }

    // ==================== Map Tests ====================

    @Test
    void analyze_map_returnsMapNode() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);

        DumpNode node = analyzer.analyze(map, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.MAP);
        assertThat(node.getEntries()).hasSize(2);
    }

    @Test
    void analyze_largeMap_truncatesAtMaxItems() {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 200; i++) {
            map.put(i, "value" + i);
        }

        DumpNode node = analyzer.analyze(map, 0);

        assertThat(node.getEntries()).hasSizeLessThanOrEqualTo(config.getMaxItems());
        assertThat(node.isTruncated()).isTrue();
    }

    @Test
    void analyze_emptyMap_returnsEmptyMapNode() {
        Map<String, String> map = Collections.emptyMap();
        DumpNode node = analyzer.analyze(map, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.MAP);
        assertThat(node.getEntries()).isEmpty();
    }

    // ==================== Object Tests ====================

    @Test
    void analyze_simpleObject_returnsObjectNode() {
        TestPerson person = new TestPerson("Alice", 30);
        DumpNode node = analyzer.analyze(person, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.OBJECT);
        assertThat(node.getFields()).containsKeys("name", "age");
    }

    @Test
    void analyze_objectWithPrivateFields_accessesPrivateFields() {
        TestPerson person = new TestPerson("Bob", 25);
        DumpNode node = analyzer.analyze(person, 0);

        assertThat(node.getFields()).containsKey("name");
        DumpNode nameNode = node.getFields().get("name");
        assertThat(nameNode.getValue()).isEqualTo("Bob");
    }

    @Test
    void analyze_objectWithInheritedFields_includesInheritedFields() {
        TestStudent student = new TestStudent("Charlie", 20, "CS101");
        DumpNode node = analyzer.analyze(student, 0);

        assertThat(node.getFields())
                .containsKeys("name", "age", "studentId");
    }

    // ==================== Depth Limit Tests ====================

    @Test
    void analyze_exceedsMaxDepth_returnsTruncatedNode() {
        TestPerson person = new TestPerson("Deep", 99);
        DumpNode node = analyzer.analyze(person, config.getMaxDepth() + 1);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.TRUNCATED);
    }

    @Test
    void analyze_nestedObject_respectsDepthLimit() {
        TestNestedObject nested = new TestNestedObject();
        nested.level = 0;
        nested.next = new TestNestedObject();
        nested.next.level = 1;

        DumpNode node = analyzer.analyze(nested, 0);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.OBJECT);
        assertThat(node.getFields()).containsKey("next");
    }

    // ==================== Cyclic Reference Tests ====================

    @Test
    void analyze_cyclicReference_detectsCycle() {
        TestNode node1 = new TestNode("A");
        TestNode node2 = new TestNode("B");
        node1.next = node2;
        node2.next = node1; // Creates cycle

        DumpNode dumpNode = analyzer.analyze(node1, 0);

        assertThat(dumpNode.getType()).isEqualTo(DumpNode.NodeType.OBJECT);
        assertThat(dumpNode.getFields()).containsKey("next");

        DumpNode nextNode = dumpNode.getFields().get("next");
        assertThat(nextNode.getFields()).containsKey("next");

        DumpNode cyclicNode = nextNode.getFields().get("next");
        assertThat(cyclicNode.getType()).isEqualTo(DumpNode.NodeType.CYCLIC);
    }

    @Test
    void analyze_selfReference_detectsCycle() {
        TestNode node = new TestNode("Self");
        node.next = node; // Self reference

        DumpNode dumpNode = analyzer.analyze(node, 0);

        assertThat(dumpNode.getType()).isEqualTo(DumpNode.NodeType.OBJECT);
        DumpNode nextNode = dumpNode.getFields().get("next");
        assertThat(nextNode.getType()).isEqualTo(DumpNode.NodeType.CYCLIC);
    }

    // ==================== Reset Tests ====================

    @Test
    void reset_clearsVisitedObjects() {
        TestPerson person = new TestPerson("Alice", 30);
        analyzer.analyze(person, 0);
        analyzer.reset();

        // Should be able to analyze again without cycle detection
        DumpNode node = analyzer.analyze(person, 0);
        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.OBJECT);
    }

    // ==================== Test Classes ====================

    private enum TestEnum {
        VALUE_A, VALUE_B, VALUE_C
    }

    private static class TestPerson {
        private String name;
        private int age;

        public TestPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    private static class TestStudent extends TestPerson {
        private String studentId;

        public TestStudent(String name, int age, String studentId) {
            super(name, age);
            this.studentId = studentId;
        }
    }

    private static class TestNode {
        String value;
        TestNode next;

        TestNode(String value) {
            this.value = value;
        }
    }

    private static class TestNestedObject {
        int level;
        TestNestedObject next;
    }
}