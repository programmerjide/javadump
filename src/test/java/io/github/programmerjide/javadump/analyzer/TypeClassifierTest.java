package io.github.programmerjide.javadump.analyzer;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static io.github.programmerjide.javadump.analyzer.TypeClassifier.TypeCategory;

class TypeClassifierTest {

    // ==================== Basic Classification Tests ====================

    @Test
    void classify_null_returnsNull() {
        assertThat(TypeClassifier.classify(null))
                .isEqualTo(TypeCategory.NULL);
    }

    @Test
    void classify_integer_returnsPrimitive() {
        assertThat(TypeClassifier.classify(42))
                .isEqualTo(TypeCategory.PRIMITIVE);
    }

    @Test
    void classify_string_returnsString() {
        assertThat(TypeClassifier.classify("test"))
                .isEqualTo(TypeCategory.STRING);
    }

    @Test
    void classify_enum_returnsEnum() {
        assertThat(TypeClassifier.classify(TestEnum.VALUE_A))
                .isEqualTo(TypeCategory.ENUM);
    }

    // ==================== Collection Tests ====================

    @Test
    void classify_list_returnsCollection() {
        assertThat(TypeClassifier.classify(Arrays.asList(1, 2, 3)))
                .isEqualTo(TypeCategory.COLLECTION);
    }

    @Test
    void classify_set_returnsCollection() {
        assertThat(TypeClassifier.classify(new HashSet<>(Arrays.asList(1, 2))))
                .isEqualTo(TypeCategory.COLLECTION);
    }

    @Test
    void classify_map_returnsMap() {
        assertThat(TypeClassifier.classify(new HashMap<String, String>()))
                .isEqualTo(TypeCategory.MAP);
    }

    @Test
    void classify_array_returnsArray() {
        assertThat(TypeClassifier.classify(new int[]{1, 2, 3}))
                .isEqualTo(TypeCategory.ARRAY);
    }

    // ==================== Special Types Tests ====================

    @Test
    void classify_localDate_returnsDateTime() {
        assertThat(TypeClassifier.classify(LocalDate.now()))
                .isEqualTo(TypeCategory.DATE_TIME);
    }

    @Test
    void classify_bigDecimal_returnsNumeric() {
        assertThat(TypeClassifier.classify(BigDecimal.ONE))
                .isEqualTo(TypeCategory.NUMERIC);
    }

    @Test
    void classify_bigInteger_returnsNumeric() {
        assertThat(TypeClassifier.classify(BigInteger.TEN))
                .isEqualTo(TypeCategory.NUMERIC);
    }

    @Test
    void classify_regularObject_returnsObject() {
        assertThat(TypeClassifier.classify(new TestObject()))
                .isEqualTo(TypeCategory.OBJECT);
    }

    // ==================== Display Mode Tests ====================

    @Test
    void shouldDisplayInline_primitives_returnsTrue() {
        assertThat(TypeClassifier.shouldDisplayInline(42)).isTrue();
        assertThat(TypeClassifier.shouldDisplayInline(true)).isTrue();
        assertThat(TypeClassifier.shouldDisplayInline(3.14)).isTrue();
    }

    @Test
    void shouldDisplayInline_string_returnsTrue() {
        assertThat(TypeClassifier.shouldDisplayInline("test")).isTrue();
    }

    @Test
    void shouldDisplayInline_enum_returnsTrue() {
        assertThat(TypeClassifier.shouldDisplayInline(TestEnum.VALUE_A)).isTrue();
    }

    @Test
    void shouldDisplayInline_null_returnsTrue() {
        assertThat(TypeClassifier.shouldDisplayInline(null)).isTrue();
    }

    @Test
    void shouldDisplayInline_list_returnsFalse() {
        assertThat(TypeClassifier.shouldDisplayInline(Arrays.asList(1, 2))).isFalse();
    }

    @Test
    void shouldDisplayInline_object_returnsFalse() {
        assertThat(TypeClassifier.shouldDisplayInline(new TestObject())).isFalse();
    }

    // ==================== Simple Value Tests ====================

    @Test
    void isSimpleValue_primitives_returnsTrue() {
        assertThat(TypeClassifier.isSimpleValue(42)).isTrue();
        assertThat(TypeClassifier.isSimpleValue("test")).isTrue();
        assertThat(TypeClassifier.isSimpleValue(TestEnum.VALUE_A)).isTrue();
    }

    @Test
    void isSimpleValue_null_returnsTrue() {
        assertThat(TypeClassifier.isSimpleValue(null)).isTrue();
    }

    @Test
    void isSimpleValue_complexTypes_returnsFalse() {
        assertThat(TypeClassifier.isSimpleValue(new ArrayList<>())).isFalse();
        assertThat(TypeClassifier.isSimpleValue(new TestObject())).isFalse();
    }

    // ==================== Test Classes ====================

    private enum TestEnum {
        VALUE_A, VALUE_B
    }

    private static class TestObject {
        String name = "test";
    }
}