package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.config.DumperConfig;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FieldFilterTest {

    // ==================== Basic Filtering Tests ====================

    @Test
    void getAccessibleFields_withStaticField_excludesStaticByDefault() {
        DumperConfig config = DumperConfig.builder()
                .showStaticFields(false)
                .build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(TestClass.class);

        assertThat(fields)
                .noneMatch(f -> f.getName().equals("staticField"));
    }

    @Test
    void getAccessibleFields_withStaticField_includesWhenConfigured() {
        DumperConfig config = DumperConfig.builder()
                .showStaticFields(true)
                .build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(TestClass.class);

        assertThat(fields)
                .anyMatch(f -> f.getName().equals("staticField"));
    }

    @Test
    void getAccessibleFields_withPrivateField_excludesPrivateByDefault() {
        DumperConfig config = DumperConfig.builder()
                .showPrivateFields(false)
                .build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(TestClass.class);

        assertThat(fields)
                .noneMatch(f -> f.getName().equals("regularField"));
    }

    @Test
    void getAccessibleFields_withPrivateField_includesWhenConfigured() {
        DumperConfig config = DumperConfig.builder()
                .showPrivateFields(true)
                .build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(TestClass.class);

        assertThat(fields)
                .anyMatch(f -> f.getName().equals("regularField"));
    }

    @Test
    void getAccessibleFields_withPublicField_alwaysIncludesPublic() {
        DumperConfig config = DumperConfig.builder()
                .showPrivateFields(false)
                .build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(TestClass.class);

        assertThat(fields)
                .anyMatch(f -> f.getName().equals("publicField"));
    }

    @Test
    void shouldSkip_staticField_returnsTrue() throws NoSuchFieldException {
        DumperConfig config = DumperConfig.builder()
                .showStaticFields(false)
                .build();
        FieldFilter filter = new FieldFilter(config);
        Field field = TestClass.class.getDeclaredField("staticField");

        assertThat(filter.shouldSkip(field)).isTrue();
    }

    @Test
    void shouldSkip_regularField_returnsFalse() throws NoSuchFieldException {
        DumperConfig config = DumperConfig.builder()
                .showPrivateFields(true)
                .build();
        FieldFilter filter = new FieldFilter(config);
        Field field = TestClass.class.getDeclaredField("regularField");

        assertThat(filter.shouldSkip(field)).isFalse();
    }

    @Test
    void shouldSkip_transientField_returnsTrueByDefault() throws NoSuchFieldException {
        DumperConfig config = DumperConfig.builder().build();
        FieldFilter filter = new FieldFilter(config);
        Field field = TestClass.class.getDeclaredField("transientField");

        assertThat(filter.shouldSkip(field)).isTrue();
    }

    @Test
    void shouldSkip_nullField_returnsTrue() {
        DumperConfig config = DumperConfig.builder().build();
        FieldFilter filter = new FieldFilter(config);

        assertThat(filter.shouldSkip(null)).isTrue();
    }

    @Test
    void shouldSkip_serialVersionUID_returnsTrue() throws NoSuchFieldException {
        DumperConfig config = DumperConfig.builder()
                .showStaticFields(true)
                .build();
        FieldFilter filter = new FieldFilter(config);
        Field field = TestClass.class.getDeclaredField("serialVersionUID");

        assertThat(filter.shouldSkip(field)).isTrue();
    }

    @Test
    void shouldSkip_jacocoData_returnsTrue() throws NoSuchFieldException {
        DumperConfig config = DumperConfig.builder()
                .showPrivateFields(true)
                .build();
        FieldFilter filter = new FieldFilter(config);
        Field field = TestClass.class.getDeclaredField("$jacocoData");

        assertThat(filter.shouldSkip(field)).isTrue();
    }

    @Test
    void exclude_customFieldName_excludesField() throws NoSuchFieldException {
        DumperConfig config = DumperConfig.builder()
                .showPrivateFields(true)
                .build();
        FieldFilter filter = new FieldFilter(config);
        filter.exclude("regularField");
        Field field = TestClass.class.getDeclaredField("regularField");

        assertThat(filter.shouldSkip(field)).isTrue();
    }

    // ==================== Combined Configuration Tests ====================

    @Test
    void getAccessibleFields_showAllFields_includesAllFields() {
        DumperConfig config = DumperConfig.builder()
                .showStaticFields(true)
                .showPrivateFields(true)
                .build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(TestClass.class);

        // Should include all except excluded names (serialVersionUID, $jacocoData)
        assertThat(fields)
                .anyMatch(f -> f.getName().equals("staticField"))
                .anyMatch(f -> f.getName().equals("regularField"))
                .anyMatch(f -> f.getName().equals("publicField"))
                .anyMatch(f -> f.getName().equals("protectedField"));
    }

    @Test
    void getAccessibleFields_hideAllPrivate_onlyShowsPublicFields() {
        DumperConfig config = DumperConfig.builder()
                .showStaticFields(false)
                .showPrivateFields(false)
                .build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(TestClass.class);

        // Should only have public and protected fields
        assertThat(fields)
                .allMatch(f -> !java.lang.reflect.Modifier.isPrivate(f.getModifiers())
                        && !java.lang.reflect.Modifier.isStatic(f.getModifiers()));
    }

    // ==================== Visibility Tests ====================

    @Test
    void isPublic_publicField_returnsTrue() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("publicField");

        assertThat(FieldFilter.isPublic(field)).isTrue();
    }

    @Test
    void isPrivate_privateField_returnsTrue() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("regularField");

        assertThat(FieldFilter.isPrivate(field)).isTrue();
    }

    @Test
    void getVisibilityMarker_publicField_returnsPlusSign() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("publicField");

        assertThat(FieldFilter.getVisibilityMarker(field)).isEqualTo("+");
    }

    @Test
    void getVisibilityMarker_privateField_returnsMinusSign() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("regularField");

        assertThat(FieldFilter.getVisibilityMarker(field)).isEqualTo("-");
    }

    @Test
    void getVisibilityMarker_protectedField_returnsHashSign() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("protectedField");

        assertThat(FieldFilter.getVisibilityMarker(field)).isEqualTo("#");
    }

    // ==================== Null Safety Tests ====================

    @Test
    void getAccessibleFields_nullClass_returnsEmptyList() {
        DumperConfig config = DumperConfig.builder().build();
        FieldFilter filter = new FieldFilter(config);

        List<Field> fields = filter.getAccessibleFields(null);

        assertThat(fields).isEmpty();
    }

    // ==================== Test Class ====================

    @SuppressWarnings("unused")
    private static class TestClass {
        private static final long serialVersionUID = 1L;
        private static String staticField = "static";
        private transient String transientField = "transient";
        private String regularField = "regular";
        public String publicField = "public";
        protected String protectedField = "protected";
        private Object[] $jacocoData;
    }
}