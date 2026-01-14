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

    // ==================== Null ====================

    @Test
    void analyze_null_returnsNullNode() {
        DumpNode node = analyzer.analyze(null);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.NULL);
    }

    // ==================== Primitive ====================

    @Test
    void analyze_int_returnsPrimitiveNode() {
        DumpNode node = analyzer.analyze(42);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.PRIMITIVE);
        assertThat(node.getValue()).isEqualTo(42);
    }

    @Test
    void analyze_boolean_returnsPrimitiveNode() {
        DumpNode node = analyzer.analyze(true);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.PRIMITIVE);
        assertThat(node.getValue()).isEqualTo(true);
    }

    // ==================== String ====================

    @Test
    void analyze_string_returnsStringNode() {
        DumpNode node = analyzer.analyze("Hello");

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.STRING);
        assertThat(node.getValue()).isEqualTo("Hello");
    }

    // ==================== Enum ====================

    @Test
    void analyze_enum_returnsEnumNode() {
        DumpNode node = analyzer.analyze(TestEnum.VALUE_A);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.ENUM);
        assertThat(node.getValue()).isEqualTo("VALUE_A");
    }

    // ==================== Array ====================

    @Test
    void analyze_array_returnsArrayNode() {
        int[] array = {1, 2, 3};

        DumpNode node = analyzer.analyze(array);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.ARRAY);
        assertThat(node.getElements()).hasSize(3);
        assertThat(node.getDisplaySize()).isEqualTo(3);
    }

    @Test
    void analyze_largeArray_isTruncated() {
        int[] array = new int[200];

        DumpNode node = analyzer.analyze(array);

        assertThat(node.isTruncated()).isTrue();
        assertThat(node.getElements().size())
                .isLessThanOrEqualTo(config.getMaxItems());
    }

    // ==================== Collection ====================

    @Test
    void analyze_list_returnsCollectionNode() {
        List<String> list = List.of("a", "b", "c");

        DumpNode node = analyzer.analyze(list);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.COLLECTION);
        assertThat(node.getElements()).hasSize(3);
    }

    @Test
    void analyze_emptyList_returnsEmptyCollection() {
        DumpNode node = analyzer.analyze(Collections.emptyList());

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.COLLECTION);
        assertThat(node.getElements()).isEmpty();
    }

    // ==================== Map ====================

    @Test
    void analyze_map_returnsMapNode() {
        Map<String, Integer> map = Map.of("a", 1, "b", 2);

        DumpNode node = analyzer.analyze(map);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.MAP);
        assertThat(node.getEntries()).hasSize(2);
    }

    @Test
    void analyze_largeMap_isTruncated() {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 200; i++) {
            map.put(i, i);
        }

        DumpNode node = analyzer.analyze(map);

        assertThat(node.isTruncated()).isTrue();
        assertThat(node.getEntries().size())
                .isLessThanOrEqualTo(config.getMaxItems());
    }

    // ==================== Object ====================

    @Test
    void analyze_object_returnsFields() {
        TestPerson person = new TestPerson("Alice", 30);

        DumpNode node = analyzer.analyze(person);

        assertThat(node.getType()).isEqualTo(DumpNode.NodeType.OBJECT);
        assertThat(node.getFields()).containsKeys("name", "age");
    }

    @Test
    void analyze_object_doesNotIncludeInheritedFields() {
        TestStudent student = new TestStudent("Bob", 20, "CS101");

        DumpNode node = analyzer.analyze(student);

        assertThat(node.getFields())
                .containsKey("studentId")
                .doesNotContainKey("name")
                .doesNotContainKey("age");
    }

    // ==================== Cyclic ====================

    @Test
    void analyze_selfReference_detectsCycle() {
        TestNode node = new TestNode("A");
        node.next = node;

        DumpNode dumpNode = analyzer.analyze(node);

        DumpNode next = dumpNode.getFields().get("next");
        assertThat(next.getType()).isEqualTo(DumpNode.NodeType.CYCLIC);
    }

    @Test
    void analyze_mutualReference_detectsCycle() {
        TestNode a = new TestNode("A");
        TestNode b = new TestNode("B");
        a.next = b;
        b.next = a;

        DumpNode dumpNode = analyzer.analyze(a);

        DumpNode next = dumpNode.getFields().get("next");
        DumpNode cycle = next.getFields().get("next");

        assertThat(cycle.getType()).isEqualTo(DumpNode.NodeType.CYCLIC);
    }

    // ==================== Test Types ====================

    private enum TestEnum {
        VALUE_A, VALUE_B
    }

    private static class TestPerson {
        private final String name;
        private final int age;

        TestPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    private static class TestStudent extends TestPerson {
        private final String studentId;

        TestStudent(String name, int age, String studentId) {
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
}
