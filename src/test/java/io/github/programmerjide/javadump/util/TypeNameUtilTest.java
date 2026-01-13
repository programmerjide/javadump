package io.github.programmerjide.javadump.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive tests for TypeNameUtil.
 *
 * @author Your Name
 */
@DisplayName("TypeNameUtil")
class TypeNameUtilTest {

    @Nested
    @DisplayName("getSimpleName()")
    class GetSimpleNameTests {

        @Test
        @DisplayName("should return simple name for String")
        void shouldReturnSimpleNameForString() {
            assertThat(TypeNameUtil.getSimpleName(String.class))
                    .isEqualTo("String");
        }

        @Test
        @DisplayName("should return simple name for primitive types")
        void shouldReturnSimpleNameForPrimitives() {
            assertThat(TypeNameUtil.getSimpleName(int.class)).isEqualTo("int");
            assertThat(TypeNameUtil.getSimpleName(long.class)).isEqualTo("long");
            assertThat(TypeNameUtil.getSimpleName(boolean.class)).isEqualTo("boolean");
            assertThat(TypeNameUtil.getSimpleName(double.class)).isEqualTo("double");
        }

        @Test
        @DisplayName("should return simple name for wrapper types")
        void shouldReturnSimpleNameForWrappers() {
            assertThat(TypeNameUtil.getSimpleName(Integer.class)).isEqualTo("Integer");
            assertThat(TypeNameUtil.getSimpleName(Boolean.class)).isEqualTo("Boolean");
            assertThat(TypeNameUtil.getSimpleName(Double.class)).isEqualTo("Double");
        }

        @Test
        @DisplayName("should return simple name for array types")
        void shouldReturnSimpleNameForArrays() {
            assertThat(TypeNameUtil.getSimpleName(int[].class)).isEqualTo("int[]");
            assertThat(TypeNameUtil.getSimpleName(String[].class)).isEqualTo("String[]");
            assertThat(TypeNameUtil.getSimpleName(int[][].class)).isEqualTo("int[][]");
        }

        @Test
        @DisplayName("should return simple name for collection types")
        void shouldReturnSimpleNameForCollections() {
            assertThat(TypeNameUtil.getSimpleName(List.class)).isEqualTo("List");
            assertThat(TypeNameUtil.getSimpleName(ArrayList.class)).isEqualTo("ArrayList");
            assertThat(TypeNameUtil.getSimpleName(Set.class)).isEqualTo("Set");
            assertThat(TypeNameUtil.getSimpleName(Map.class)).isEqualTo("Map");
        }

        @Test
        @DisplayName("should return 'null' for null class")
        void shouldReturnNullForNullClass() {
            assertThat(TypeNameUtil.getSimpleName(null))
                    .isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("getPrettyName()")
    class GetPrettyNameTests {

        @Test
        @DisplayName("should prefix with # symbol")
        void shouldPrefixWithHash() {
            assertThat(TypeNameUtil.getPrettyName(String.class))
                    .isEqualTo("#String");
        }

        @Test
        @DisplayName("should format primitives with prefix")
        void shouldFormatPrimitivesWithPrefix() {
            assertThat(TypeNameUtil.getPrettyName(int.class)).isEqualTo("#int");
            assertThat(TypeNameUtil.getPrettyName(boolean.class)).isEqualTo("#boolean");
        }

        @Test
        @DisplayName("should format arrays with prefix")
        void shouldFormatArraysWithPrefix() {
            assertThat(TypeNameUtil.getPrettyName(int[].class)).isEqualTo("#int[]");
            assertThat(TypeNameUtil.getPrettyName(String[].class)).isEqualTo("#String[]");
        }

        @Test
        @DisplayName("should return '#null' for null class")
        void shouldReturnHashNullForNullClass() {
            assertThat(TypeNameUtil.getPrettyName(null))
                    .isEqualTo("#null");
        }
    }

    @Nested
    @DisplayName("getFullyQualifiedName()")
    class GetFullyQualifiedNameTests {

        @Test
        @DisplayName("should return fully qualified name for classes")
        void shouldReturnFullyQualifiedName() {
            assertThat(TypeNameUtil.getFullyQualifiedName(String.class))
                    .isEqualTo("java.lang.String");
            assertThat(TypeNameUtil.getFullyQualifiedName(ArrayList.class))
                    .isEqualTo("java.util.ArrayList");
        }

        @Test
        @DisplayName("should return simple name for primitives")
        void shouldReturnSimpleNameForPrimitives() {
            assertThat(TypeNameUtil.getFullyQualifiedName(int.class))
                    .isEqualTo("int");
            assertThat(TypeNameUtil.getFullyQualifiedName(boolean.class))
                    .isEqualTo("boolean");
        }

        @Test
        @DisplayName("should include array notation in FQN")
        void shouldIncludeArrayNotation() {
            assertThat(TypeNameUtil.getFullyQualifiedName(int[].class))
                    .isEqualTo("int[]");
            assertThat(TypeNameUtil.getFullyQualifiedName(String[].class))
                    .isEqualTo("java.lang.String[]");
        }

        @Test
        @DisplayName("should return 'null' for null class")
        void shouldReturnNullForNullClass() {
            assertThat(TypeNameUtil.getFullyQualifiedName(null))
                    .isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("getPackageName()")
    class GetPackageNameTests {

        @Test
        @DisplayName("should return package name for classes")
        void shouldReturnPackageName() {
            assertThat(TypeNameUtil.getPackageName(String.class))
                    .isEqualTo("java.lang");
            assertThat(TypeNameUtil.getPackageName(ArrayList.class))
                    .isEqualTo("java.util");
        }

        @Test
        @DisplayName("should return empty string for primitives")
        void shouldReturnEmptyForPrimitives() {
            assertThat(TypeNameUtil.getPackageName(int.class)).isEmpty();
            assertThat(TypeNameUtil.getPackageName(boolean.class)).isEmpty();
        }

        @Test
        @DisplayName("should return empty string for arrays")
        void shouldReturnEmptyForArrays() {
            assertThat(TypeNameUtil.getPackageName(int[].class)).isEmpty();
            assertThat(TypeNameUtil.getPackageName(String[].class)).isEmpty();
        }

        @Test
        @DisplayName("should return empty string for null")
        void shouldReturnEmptyForNull() {
            assertThat(TypeNameUtil.getPackageName(null)).isEmpty();
        }
    }

    @Nested
    @DisplayName("isJavaLangType()")
    class IsJavaLangTypeTests {

        @Test
        @DisplayName("should return true for java.lang types")
        void shouldReturnTrueForJavaLangTypes() {
            assertThat(TypeNameUtil.isJavaLangType(String.class)).isTrue();
            assertThat(TypeNameUtil.isJavaLangType(Integer.class)).isTrue();
            assertThat(TypeNameUtil.isJavaLangType(Object.class)).isTrue();
            assertThat(TypeNameUtil.isJavaLangType(Boolean.class)).isTrue();
        }

        @Test
        @DisplayName("should return false for non-java.lang types")
        void shouldReturnFalseForNonJavaLangTypes() {
            assertThat(TypeNameUtil.isJavaLangType(ArrayList.class)).isFalse();
            assertThat(TypeNameUtil.isJavaLangType(HashMap.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for primitives")
        void shouldReturnFalseForPrimitives() {
            assertThat(TypeNameUtil.isJavaLangType(int.class)).isFalse();
            assertThat(TypeNameUtil.isJavaLangType(boolean.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isJavaLangType(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isPrimitive()")
    class IsPrimitiveTests {

        @ParameterizedTest
        @MethodSource("primitiveTypes")
        @DisplayName("should return true for primitive types")
        void shouldReturnTrueForPrimitives(Class<?> type) {
            assertThat(TypeNameUtil.isPrimitive(type)).isTrue();
        }

        static Stream<Class<?>> primitiveTypes() {
            return Stream.of(
                    boolean.class, byte.class, char.class, short.class,
                    int.class, long.class, float.class, double.class, void.class
            );
        }

        @Test
        @DisplayName("should return false for wrapper types")
        void shouldReturnFalseForWrappers() {
            assertThat(TypeNameUtil.isPrimitive(Integer.class)).isFalse();
            assertThat(TypeNameUtil.isPrimitive(Boolean.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for reference types")
        void shouldReturnFalseForReferenceTypes() {
            assertThat(TypeNameUtil.isPrimitive(String.class)).isFalse();
            assertThat(TypeNameUtil.isPrimitive(Object.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isPrimitive(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isWrapper()")
    class IsWrapperTests {

        @ParameterizedTest
        @MethodSource("wrapperTypes")
        @DisplayName("should return true for wrapper types")
        void shouldReturnTrueForWrappers(Class<?> type) {
            assertThat(TypeNameUtil.isWrapper(type)).isTrue();
        }

        static Stream<Class<?>> wrapperTypes() {
            return Stream.of(
                    Boolean.class, Byte.class, Character.class, Short.class,
                    Integer.class, Long.class, Float.class, Double.class, Void.class
            );
        }

        @Test
        @DisplayName("should return false for primitive types")
        void shouldReturnFalseForPrimitives() {
            assertThat(TypeNameUtil.isWrapper(int.class)).isFalse();
            assertThat(TypeNameUtil.isWrapper(boolean.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for other types")
        void shouldReturnFalseForOtherTypes() {
            assertThat(TypeNameUtil.isWrapper(String.class)).isFalse();
            assertThat(TypeNameUtil.isWrapper(Object.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isWrapper(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isPrimitiveOrWrapper()")
    class IsPrimitiveOrWrapperTests {

        @Test
        @DisplayName("should return true for primitives")
        void shouldReturnTrueForPrimitives() {
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(int.class)).isTrue();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(boolean.class)).isTrue();
        }

        @Test
        @DisplayName("should return true for wrappers")
        void shouldReturnTrueForWrappers() {
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(Integer.class)).isTrue();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(Boolean.class)).isTrue();
        }

        @Test
        @DisplayName("should return false for other types")
        void shouldReturnFalseForOtherTypes() {
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(String.class)).isFalse();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(Object.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isCollectionType()")
    class IsCollectionTypeTests {

        @ParameterizedTest
        @MethodSource("collectionTypes")
        @DisplayName("should return true for collection types")
        void shouldReturnTrueForCollections(Class<?> type) {
            assertThat(TypeNameUtil.isCollectionType(type)).isTrue();
        }

        static Stream<Class<?>> collectionTypes() {
            return Stream.of(
                    Collection.class, List.class, Set.class, Queue.class,
                    ArrayList.class, LinkedList.class, HashSet.class,
                    TreeSet.class, LinkedHashSet.class, Vector.class
            );
        }

        @Test
        @DisplayName("should return false for Map types")
        void shouldReturnFalseForMaps() {
            assertThat(TypeNameUtil.isCollectionType(Map.class)).isFalse();
            assertThat(TypeNameUtil.isCollectionType(HashMap.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for other types")
        void shouldReturnFalseForOtherTypes() {
            assertThat(TypeNameUtil.isCollectionType(String.class)).isFalse();
            assertThat(TypeNameUtil.isCollectionType(Integer.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isCollectionType(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isMapType()")
    class IsMapTypeTests {

        @ParameterizedTest
        @MethodSource("mapTypes")
        @DisplayName("should return true for map types")
        void shouldReturnTrueForMaps(Class<?> type) {
            assertThat(TypeNameUtil.isMapType(type)).isTrue();
        }

        static Stream<Class<?>> mapTypes() {
            return Stream.of(
                    Map.class, HashMap.class, TreeMap.class, LinkedHashMap.class,
                    Hashtable.class, WeakHashMap.class, SortedMap.class
            );
        }

        @Test
        @DisplayName("should return false for Collection types")
        void shouldReturnFalseForCollections() {
            assertThat(TypeNameUtil.isMapType(List.class)).isFalse();
            assertThat(TypeNameUtil.isMapType(ArrayList.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for other types")
        void shouldReturnFalseForOtherTypes() {
            assertThat(TypeNameUtil.isMapType(String.class)).isFalse();
            assertThat(TypeNameUtil.isMapType(Integer.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isMapType(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isArrayType()")
    class IsArrayTypeTests {

        @Test
        @DisplayName("should return true for array types")
        void shouldReturnTrueForArrays() {
            assertThat(TypeNameUtil.isArrayType(int[].class)).isTrue();
            assertThat(TypeNameUtil.isArrayType(String[].class)).isTrue();
            assertThat(TypeNameUtil.isArrayType(Object[].class)).isTrue();
            assertThat(TypeNameUtil.isArrayType(int[][].class)).isTrue();
        }

        @Test
        @DisplayName("should return false for non-array types")
        void shouldReturnFalseForNonArrays() {
            assertThat(TypeNameUtil.isArrayType(int.class)).isFalse();
            assertThat(TypeNameUtil.isArrayType(String.class)).isFalse();
            assertThat(TypeNameUtil.isArrayType(List.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isArrayType(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("getArrayComponentType()")
    class GetArrayComponentTypeTests {

        @Test
        @DisplayName("should return component type for arrays")
        void shouldReturnComponentType() {
            assertThat(TypeNameUtil.getArrayComponentType(int[].class))
                    .isEqualTo(int.class);
            assertThat(TypeNameUtil.getArrayComponentType(String[].class))
                    .isEqualTo(String.class);
        }

        @Test
        @DisplayName("should handle multi-dimensional arrays")
        void shouldHandleMultiDimensionalArrays() {
            assertThat(TypeNameUtil.getArrayComponentType(int[][].class))
                    .isEqualTo(int[].class);
        }

        @Test
        @DisplayName("should return null for non-array types")
        void shouldReturnNullForNonArrays() {
            assertThat(TypeNameUtil.getArrayComponentType(int.class)).isNull();
            assertThat(TypeNameUtil.getArrayComponentType(String.class)).isNull();
        }

        @Test
        @DisplayName("should return null for null")
        void shouldReturnNullForNull() {
            assertThat(TypeNameUtil.getArrayComponentType(null)).isNull();
        }
    }

    @Nested
    @DisplayName("formatGenericType()")
    class FormatGenericTypeTests {

        // Helper class with generic fields for testing
        static class GenericTestClass {
            List<String> stringList;
            Map<String, Integer> stringIntMap;
            List<List<String>> nestedList;
        }

        @Test
        @DisplayName("should format simple class type")
        void shouldFormatSimpleType() {
            assertThat(TypeNameUtil.formatGenericType(String.class))
                    .isEqualTo("String");
        }

        @Test
        @DisplayName("should format parameterized type")
        void shouldFormatParameterizedType() throws NoSuchFieldException {
            Field field = GenericTestClass.class.getDeclaredField("stringList");
            String formatted = TypeNameUtil.formatGenericType(field.getGenericType());

            assertThat(formatted).isEqualTo("List<String>");
        }

        @Test
        @DisplayName("should format map with multiple type parameters")
        void shouldFormatMapType() throws NoSuchFieldException {
            Field field = GenericTestClass.class.getDeclaredField("stringIntMap");
            String formatted = TypeNameUtil.formatGenericType(field.getGenericType());

            assertThat(formatted).isEqualTo("Map<String, Integer>");
        }

        @Test
        @DisplayName("should format nested generic types")
        void shouldFormatNestedGenerics() throws NoSuchFieldException {
            Field field = GenericTestClass.class.getDeclaredField("nestedList");
            String formatted = TypeNameUtil.formatGenericType(field.getGenericType());

            assertThat(formatted).isEqualTo("List<List<String>>");
        }

        @Test
        @DisplayName("should return 'null' for null type")
        void shouldReturnNullForNullType() {
            assertThat(TypeNameUtil.formatGenericType(null))
                    .isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("isString()")
    class IsStringTests {

        @Test
        @DisplayName("should return true for String class")
        void shouldReturnTrueForString() {
            assertThat(TypeNameUtil.isString(String.class)).isTrue();
        }

        @Test
        @DisplayName("should return false for other types")
        void shouldReturnFalseForOtherTypes() {
            assertThat(TypeNameUtil.isString(Integer.class)).isFalse();
            assertThat(TypeNameUtil.isString(Object.class)).isFalse();
            assertThat(TypeNameUtil.isString(CharSequence.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isString(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isCharSequence()")
    class IsCharSequenceTests {

        @Test
        @DisplayName("should return true for CharSequence and subtypes")
        void shouldReturnTrueForCharSequence() {
            assertThat(TypeNameUtil.isCharSequence(CharSequence.class)).isTrue();
            assertThat(TypeNameUtil.isCharSequence(String.class)).isTrue();
            assertThat(TypeNameUtil.isCharSequence(StringBuilder.class)).isTrue();
            assertThat(TypeNameUtil.isCharSequence(StringBuffer.class)).isTrue();
        }

        @Test
        @DisplayName("should return false for non-CharSequence types")
        void shouldReturnFalseForNonCharSequence() {
            assertThat(TypeNameUtil.isCharSequence(Integer.class)).isFalse();
            assertThat(TypeNameUtil.isCharSequence(Object.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isCharSequence(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isEnum()")
    class IsEnumTests {

        enum TestEnum {
            VALUE1, VALUE2
        }

        @Test
        @DisplayName("should return true for enum types")
        void shouldReturnTrueForEnums() {
            assertThat(TypeNameUtil.isEnum(TestEnum.class)).isTrue();
        }

        @Test
        @DisplayName("should return false for non-enum types")
        void shouldReturnFalseForNonEnums() {
            assertThat(TypeNameUtil.isEnum(String.class)).isFalse();
            assertThat(TypeNameUtil.isEnum(Integer.class)).isFalse();
        }

        @Test
        @DisplayName("should return false for null")
        void shouldReturnFalseForNull() {
            assertThat(TypeNameUtil.isEnum(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("getDisplayName()")
    class GetDisplayNameTests {

        @Test
        @DisplayName("should return simple name for java.lang types")
        void shouldReturnSimpleNameForJavaLangTypes() {
            assertThat(TypeNameUtil.getDisplayName(String.class))
                    .isEqualTo("String");
            assertThat(TypeNameUtil.getDisplayName(Integer.class))
                    .isEqualTo("Integer");
        }

        @Test
        @DisplayName("should return FQN for non-java.lang types")
        void shouldReturnFQNForNonJavaLangTypes() {
            assertThat(TypeNameUtil.getDisplayName(ArrayList.class))
                    .isEqualTo("java.util.ArrayList");
        }

        @Test
        @DisplayName("should handle arrays correctly")
        void shouldHandleArrays() {
            assertThat(TypeNameUtil.getDisplayName(int[].class))
                    .isEqualTo("int[]");
            assertThat(TypeNameUtil.getDisplayName(String[].class))
                    .isEqualTo("String[]");
        }
    }

    @Nested
    @DisplayName("toCompactString()")
    class ToCompactStringTests {

        @Test
        @DisplayName("should format java.lang types compactly")
        void shouldFormatJavaLangTypesCompactly() {
            assertThat(TypeNameUtil.toCompactString(String.class))
                    .isEqualTo("#String");
            assertThat(TypeNameUtil.toCompactString(Integer.class))
                    .isEqualTo("#Integer");
        }

        @Test
        @DisplayName("should include FQN for non-java.lang types")
        void shouldIncludeFQNForNonJavaLangTypes() {
            assertThat(TypeNameUtil.toCompactString(ArrayList.class))
                    .isEqualTo("#java.util.ArrayList");
        }

        @Test
        @DisplayName("should handle arrays")
        void shouldHandleArrays() {
            assertThat(TypeNameUtil.toCompactString(int[].class))
                    .isEqualTo("#int[]");
            assertThat(TypeNameUtil.toCompactString(String[].class))
                    .isEqualTo("#String[]");
        }

        @Test
        @DisplayName("should handle primitives")
        void shouldHandlePrimitives() {
            assertThat(TypeNameUtil.toCompactString(int.class))
                    .isEqualTo("#int");
            assertThat(TypeNameUtil.toCompactString(boolean.class))
                    .isEqualTo("#boolean");
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should not allow instantiation")
        void shouldNotAllowInstantiation() {
            assertThatThrownBy(() -> {
                var constructor = TypeNameUtil.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            })
                    .hasCauseInstanceOf(AssertionError.class)
                    .hasMessageContaining("utility class");
        }
    }
}