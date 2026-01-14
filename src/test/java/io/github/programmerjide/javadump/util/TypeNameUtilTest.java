package io.github.programmerjide.javadump.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link TypeNameUtil}.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
@DisplayName("TypeNameUtil Tests")
class TypeNameUtilTest {

    // ==================== Utility Class Validation ====================

    @Test
    @DisplayName("TypeNameUtil is a utility class and cannot be instantiated")
    void typeNameUtil_isUtilityClass_cannotBeInstantiated() {
        // Verify it's a final class
        assertThat(TypeNameUtil.class).isFinal();

        // Verify it has only one private constructor
        Constructor<?>[] constructors = TypeNameUtil.class.getDeclaredConstructors();
        assertThat(constructors).hasSize(1);

        Constructor<?> constructor = constructors[0];
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

        // Verify constructor throws exception when called
        constructor.setAccessible(true);
        assertThatThrownBy(() -> {
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        })
                .isInstanceOf(AssertionError.class)
                .hasMessage("Utility class");
    }

    // ==================== getSimpleName() Tests ====================

    @Nested
    @DisplayName("getSimpleName() Method Tests")
    class GetSimpleNameTests {

        @Test
        @DisplayName("getSimpleName() returns 'null' for null input")
        void getSimpleName_nullInput_returnsNullString() {
            String result = TypeNameUtil.getSimpleName(null);
            assertThat(result).isEqualTo("null");
        }

        @Test
        @DisplayName("getSimpleName() returns simple name for regular classes")
        void getSimpleName_regularClass_returnsSimpleName() {
            assertThat(TypeNameUtil.getSimpleName(String.class)).isEqualTo("String");
            assertThat(TypeNameUtil.getSimpleName(Integer.class)).isEqualTo("Integer");
            assertThat(TypeNameUtil.getSimpleName(List.class)).isEqualTo("List");
            assertThat(TypeNameUtil.getSimpleName(ArrayList.class)).isEqualTo("ArrayList");
        }

        @Test
        @DisplayName("getSimpleName() returns primitive names")
        void getSimpleName_primitiveTypes_returnsPrimitiveName() {
            assertThat(TypeNameUtil.getSimpleName(int.class)).isEqualTo("int");
            assertThat(TypeNameUtil.getSimpleName(boolean.class)).isEqualTo("boolean");
            assertThat(TypeNameUtil.getSimpleName(void.class)).isEqualTo("void");
        }

        @Test
        @DisplayName("getSimpleName() handles arrays")
        void getSimpleName_arrayTypes_returnsArrayNotation() {
            assertThat(TypeNameUtil.getSimpleName(String[].class)).isEqualTo("String[]");
            assertThat(TypeNameUtil.getSimpleName(int[].class)).isEqualTo("int[]");
            assertThat(TypeNameUtil.getSimpleName(int[][].class)).isEqualTo("int[][]");
            assertThat(TypeNameUtil.getSimpleName(List[].class)).isEqualTo("List[]");
        }

        @Test
        @DisplayName("getSimpleName() handles nested classes")
        void getSimpleName_nestedClass_returnsSimpleName() {
            assertThat(TypeNameUtil.getSimpleName(Map.Entry.class)).isEqualTo("Entry");
        }
    }

    // ==================== getPrettyName() Tests ====================

    @Nested
    @DisplayName("getPrettyName() Method Tests")
    class GetPrettyNameTests {

        @Test
        @DisplayName("getPrettyName() returns '#null' for null input")
        void getPrettyName_nullInput_returnsNullWithPrefix() {
            String result = TypeNameUtil.getPrettyName(null);
            assertThat(result).isEqualTo("#null");
        }

        @Test
        @DisplayName("getPrettyName() returns prefixed simple name")
        void getPrettyName_regularClass_returnsPrefixedName() {
            assertThat(TypeNameUtil.getPrettyName(String.class)).isEqualTo("#String");
            assertThat(TypeNameUtil.getPrettyName(Integer.class)).isEqualTo("#Integer");
            assertThat(TypeNameUtil.getPrettyName(List.class)).isEqualTo("#List");
        }

        @Test
        @DisplayName("getPrettyName() handles arrays")
        void getPrettyName_arrayTypes_returnsPrefixedArrayNotation() {
            assertThat(TypeNameUtil.getPrettyName(String[].class)).isEqualTo("#String[]");
            assertThat(TypeNameUtil.getPrettyName(int[].class)).isEqualTo("#int[]");
        }
    }

    // ==================== getFullyQualifiedName() Tests ====================

    @Nested
    @DisplayName("getFullyQualifiedName() Method Tests")
    class GetFullyQualifiedNameTests {

        @Test
        @DisplayName("getFullyQualifiedName() returns 'null' for null input")
        void getFullyQualifiedName_nullInput_returnsNullString() {
            String result = TypeNameUtil.getFullyQualifiedName(null);
            assertThat(result).isEqualTo("null");
        }

        @Test
        @DisplayName("getFullyQualifiedName() returns fully qualified name")
        void getFullyQualifiedName_regularClass_returnsFullyQualifiedName() {
            assertThat(TypeNameUtil.getFullyQualifiedName(String.class))
                    .isEqualTo("java.lang.String");
            assertThat(TypeNameUtil.getFullyQualifiedName(Integer.class))
                    .isEqualTo("java.lang.Integer");
            assertThat(TypeNameUtil.getFullyQualifiedName(List.class))
                    .isEqualTo("java.util.List");
        }

        @Test
        @DisplayName("getFullyQualifiedName() returns primitive names")
        void getFullyQualifiedName_primitiveTypes_returnsPrimitiveName() {
            assertThat(TypeNameUtil.getFullyQualifiedName(int.class)).isEqualTo("int");
            assertThat(TypeNameUtil.getFullyQualifiedName(boolean.class)).isEqualTo("boolean");
            assertThat(TypeNameUtil.getFullyQualifiedName(void.class)).isEqualTo("void");
        }

        @Test
        @DisplayName("getFullyQualifiedName() handles arrays")
        void getFullyQualifiedName_arrayTypes_returnsFullyQualifiedArrayNotation() {
            assertThat(TypeNameUtil.getFullyQualifiedName(String[].class))
                    .isEqualTo("java.lang.String[]");
            assertThat(TypeNameUtil.getFullyQualifiedName(int[].class))
                    .isEqualTo("int[]");
            assertThat(TypeNameUtil.getFullyQualifiedName(List[].class))
                    .isEqualTo("java.util.List[]");
        }
    }

    // ==================== getPackageName() Tests ====================

    @Nested
    @DisplayName("getPackageName() Method Tests")
    class GetPackageNameTests {

        @Test
        @DisplayName("getPackageName() returns empty string for null input")
        void getPackageName_nullInput_returnsEmptyString() {
            String result = TypeNameUtil.getPackageName(null);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("getPackageName() returns empty string for primitive types")
        void getPackageName_primitiveType_returnsEmptyString() {
            assertThat(TypeNameUtil.getPackageName(int.class)).isEmpty();
            assertThat(TypeNameUtil.getPackageName(boolean.class)).isEmpty();
        }

        @Test
        @DisplayName("getPackageName() returns empty string for arrays")
        void getPackageName_arrayType_returnsEmptyString() {
            assertThat(TypeNameUtil.getPackageName(String[].class)).isEmpty();
            assertThat(TypeNameUtil.getPackageName(int[].class)).isEmpty();
        }

        @Test
        @DisplayName("getPackageName() returns package name for regular classes")
        void getPackageName_regularClass_returnsPackageName() {
            assertThat(TypeNameUtil.getPackageName(String.class)).isEqualTo("java.lang");
            assertThat(TypeNameUtil.getPackageName(List.class)).isEqualTo("java.util");
            assertThat(TypeNameUtil.getPackageName(Test.class)).isEqualTo("org.junit.jupiter.api");
        }

        @Test
        @DisplayName("getPackageName() returns empty string for default package")
        void getPackageName_defaultPackage_returnsEmptyString() {
            // Classes in default package (rare) would return empty string
            // This is hard to test without creating a class in default package
        }
    }

    // ==================== isJavaLangType() Tests ====================

    @Nested
    @DisplayName("isJavaLangType() Method Tests")
    class IsJavaLangTypeTests {

        @Test
        @DisplayName("isJavaLangType() returns false for null input")
        void isJavaLangType_nullInput_returnsFalse() {
            boolean result = TypeNameUtil.isJavaLangType(null);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("isJavaLangType() returns true for java.lang types")
        void isJavaLangType_javaLangClass_returnsTrue() {
            assertThat(TypeNameUtil.isJavaLangType(String.class)).isTrue();
            assertThat(TypeNameUtil.isJavaLangType(Integer.class)).isTrue();
            assertThat(TypeNameUtil.isJavaLangType(Object.class)).isTrue();
        }

        @Test
        @DisplayName("isJavaLangType() returns false for non-java.lang types")
        void isJavaLangType_nonJavaLangClass_returnsFalse() {
            assertThat(TypeNameUtil.isJavaLangType(List.class)).isFalse();
            assertThat(TypeNameUtil.isJavaLangType(ArrayList.class)).isFalse();
            assertThat(TypeNameUtil.isJavaLangType(Test.class)).isFalse();
        }

        @Test
        @DisplayName("isJavaLangType() returns false for primitives")
        void isJavaLangType_primitive_returnsFalse() {
            assertThat(TypeNameUtil.isJavaLangType(int.class)).isFalse();
            assertThat(TypeNameUtil.isJavaLangType(void.class)).isFalse();
        }

        @Test
        @DisplayName("isJavaLangType() returns false for arrays")
        void isJavaLangType_array_returnsFalse() {
            assertThat(TypeNameUtil.isJavaLangType(String[].class)).isFalse();
            assertThat(TypeNameUtil.isJavaLangType(int[].class)).isFalse();
        }
    }

    // ==================== Primitive/Wrapper Detection Tests ====================

    @Nested
    @DisplayName("Primitive and Wrapper Detection Tests")
    class PrimitiveWrapperTests {

        @Test
        @DisplayName("isPrimitive() returns true for primitive types")
        void isPrimitive_primitiveTypes_returnsTrue() {
            assertThat(TypeNameUtil.isPrimitive(int.class)).isTrue();
            assertThat(TypeNameUtil.isPrimitive(boolean.class)).isTrue();
            assertThat(TypeNameUtil.isPrimitive(void.class)).isTrue();
        }

        @Test
        @DisplayName("isPrimitive() returns false for non-primitive types")
        void isPrimitive_nonPrimitive_returnsFalse() {
            assertThat(TypeNameUtil.isPrimitive(Integer.class)).isFalse();
            assertThat(TypeNameUtil.isPrimitive(String.class)).isFalse();
            assertThat(TypeNameUtil.isPrimitive(null)).isFalse();
        }

        @Test
        @DisplayName("isWrapper() returns true for wrapper types")
        void isWrapper_wrapperTypes_returnsTrue() {
            assertThat(TypeNameUtil.isWrapper(Integer.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Boolean.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Character.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Void.class)).isTrue();
        }

        @Test
        @DisplayName("isWrapper() returns false for non-wrapper types")
        void isWrapper_nonWrapper_returnsFalse() {
            assertThat(TypeNameUtil.isWrapper(int.class)).isFalse();
            assertThat(TypeNameUtil.isWrapper(String.class)).isFalse();
            assertThat(TypeNameUtil.isWrapper(List.class)).isFalse();
            assertThat(TypeNameUtil.isWrapper(null)).isFalse();
        }

        @Test
        @DisplayName("isPrimitiveOrWrapper() returns true for both primitive and wrapper")
        void isPrimitiveOrWrapper_primitiveOrWrapper_returnsTrue() {
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(int.class)).isTrue();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(Integer.class)).isTrue();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(boolean.class)).isTrue();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(Boolean.class)).isTrue();
        }

        @Test
        @DisplayName("isPrimitiveOrWrapper() returns false for non-primitive/non-wrapper")
        void isPrimitiveOrWrapper_otherTypes_returnsFalse() {
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(String.class)).isFalse();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(List.class)).isFalse();
            assertThat(TypeNameUtil.isPrimitiveOrWrapper(null)).isFalse();
        }
    }

    // ==================== Collection/Map/Array Detection Tests ====================

    @Nested
    @DisplayName("Collection, Map, and Array Detection Tests")
    class CollectionMapArrayTests {

        @Test
        @DisplayName("isCollectionType() returns true for Collection types")
        void isCollectionType_collectionTypes_returnsTrue() {
            assertThat(TypeNameUtil.isCollectionType(List.class)).isTrue();
            assertThat(TypeNameUtil.isCollectionType(ArrayList.class)).isTrue();
            assertThat(TypeNameUtil.isCollectionType(Set.class)).isTrue();
            assertThat(TypeNameUtil.isCollectionType(HashSet.class)).isTrue();
        }

        @Test
        @DisplayName("isCollectionType() returns false for non-Collection types")
        void isCollectionType_nonCollection_returnsFalse() {
            assertThat(TypeNameUtil.isCollectionType(String.class)).isFalse();
            assertThat(TypeNameUtil.isCollectionType(Map.class)).isFalse();
            assertThat(TypeNameUtil.isCollectionType(int[].class)).isFalse();
            assertThat(TypeNameUtil.isCollectionType(null)).isFalse();
        }

        @Test
        @DisplayName("isMapType() returns true for Map types")
        void isMapType_mapTypes_returnsTrue() {
            assertThat(TypeNameUtil.isMapType(Map.class)).isTrue();
            assertThat(TypeNameUtil.isMapType(HashMap.class)).isTrue();
            assertThat(TypeNameUtil.isMapType(TreeMap.class)).isTrue();
        }

        @Test
        @DisplayName("isMapType() returns false for non-Map types")
        void isMapType_nonMap_returnsFalse() {
            assertThat(TypeNameUtil.isMapType(List.class)).isFalse();
            assertThat(TypeNameUtil.isMapType(String.class)).isFalse();
            assertThat(TypeNameUtil.isMapType(null)).isFalse();
        }

        @Test
        @DisplayName("isArrayType() returns true for array types")
        void isArrayType_arrayTypes_returnsTrue() {
            assertThat(TypeNameUtil.isArrayType(String[].class)).isTrue();
            assertThat(TypeNameUtil.isArrayType(int[].class)).isTrue();
            assertThat(TypeNameUtil.isArrayType(int[][].class)).isTrue();
            assertThat(TypeNameUtil.isArrayType(List[].class)).isTrue();
        }

        @Test
        @DisplayName("isArrayType() returns false for non-array types")
        void isArrayType_nonArray_returnsFalse() {
            assertThat(TypeNameUtil.isArrayType(String.class)).isFalse();
            assertThat(TypeNameUtil.isArrayType(List.class)).isFalse();
            assertThat(TypeNameUtil.isArrayType(null)).isFalse();
        }

        @Test
        @DisplayName("getArrayComponentType() returns component type for arrays")
        void getArrayComponentType_array_returnsComponentType() {
            assertThat(TypeNameUtil.getArrayComponentType(String[].class)).isEqualTo(String.class);
            assertThat(TypeNameUtil.getArrayComponentType(int[].class)).isEqualTo(int.class);
            assertThat(TypeNameUtil.getArrayComponentType(int[][].class)).isEqualTo(int[].class);
        }

        @Test
        @DisplayName("getArrayComponentType() returns null for non-arrays")
        void getArrayComponentType_nonArray_returnsNull() {
            assertThat(TypeNameUtil.getArrayComponentType(String.class)).isNull();
            assertThat(TypeNameUtil.getArrayComponentType(List.class)).isNull();
            assertThat(TypeNameUtil.getArrayComponentType(null)).isNull();
        }
    }

    // ==================== formatGenericType() Tests ====================

    @Nested
    @DisplayName("formatGenericType() Method Tests")
    class FormatGenericTypeTests {

        @Test
        @DisplayName("formatGenericType() returns 'null' for null input")
        void formatGenericType_null_returnsNullString() {
            String result = TypeNameUtil.formatGenericType(null);
            assertThat(result).isEqualTo("null");
        }

        @Test
        @DisplayName("formatGenericType() formats simple class")
        void formatGenericType_simpleClass_returnsSimpleName() {
            String result = TypeNameUtil.formatGenericType(String.class);
            assertThat(result).isEqualTo("String");
        }

        @Test
        @DisplayName("formatGenericType() formats parameterized types")
        void formatGenericType_parameterizedType_returnsWithTypeParameters() {
            // Get a parameterized type (e.g., List<String>)
            ParameterizedType listOfString = new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{String.class};
                }

                @Override
                public Type getRawType() {
                    return List.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            };

            String result = TypeNameUtil.formatGenericType(listOfString);
            assertThat(result).isEqualTo("List<String>");
        }

        @Test
        @DisplayName("formatGenericType() formats nested parameterized types")
        void formatGenericType_nestedParameterized_returnsNestedFormat() {
            // Create a mock ParameterizedType for Map<String, List<Integer>>
            ParameterizedType mapType = createMockParameterizedType(
                    Map.class,
                    String.class,
                    createMockParameterizedType(List.class, Integer.class)
            );

            String result = TypeNameUtil.formatGenericType(mapType);
            assertThat(result).isEqualTo("Map<String, List<Integer>>");
        }

        @Test
        @DisplayName("formatGenericType() handles multiple type arguments")
        void formatGenericType_multipleTypeArguments_handlesCorrectly() {
            ParameterizedType type = createMockParameterizedType(
                    Map.class,
                    String.class,
                    Integer.class
            );

            String result = TypeNameUtil.formatGenericType(type);
            assertThat(result).isEqualTo("Map<String, Integer>");
        }

        @Test
        @DisplayName("formatGenericType() returns type name for unknown Type implementations")
        void formatGenericType_unknownType_returnsTypeName() {
            Type unknownType = new Type() {
                @Override
                public String getTypeName() {
                    return "UnknownType";
                }
            };

            String result = TypeNameUtil.formatGenericType(unknownType);
            assertThat(result).isEqualTo("UnknownType");
        }

        private ParameterizedType createMockParameterizedType(Type rawType, Type... typeArgs) {
            return new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return typeArgs;
                }

                @Override
                public Type getRawType() {
                    return rawType;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            };
        }
    }

    // ==================== String/CharSequence/Enum Detection Tests ====================

    @Nested
    @DisplayName("String, CharSequence, and Enum Detection Tests")
    class StringCharSequenceEnumTests {

        @Test
        @DisplayName("isString() returns true for String class")
        void isString_stringClass_returnsTrue() {
            assertThat(TypeNameUtil.isString(String.class)).isTrue();
        }

        @Test
        @DisplayName("isString() returns false for non-String types")
        void isString_nonString_returnsFalse() {
            assertThat(TypeNameUtil.isString(CharSequence.class)).isFalse();
            assertThat(TypeNameUtil.isString(StringBuilder.class)).isFalse();
            assertThat(TypeNameUtil.isString(null)).isFalse();
        }

        @Test
        @DisplayName("isCharSequence() returns true for CharSequence and subtypes")
        void isCharSequence_charSequenceTypes_returnsTrue() {
            assertThat(TypeNameUtil.isCharSequence(CharSequence.class)).isTrue();
            assertThat(TypeNameUtil.isCharSequence(String.class)).isTrue();
            assertThat(TypeNameUtil.isCharSequence(StringBuilder.class)).isTrue();
            assertThat(TypeNameUtil.isCharSequence(StringBuffer.class)).isTrue();
        }

        @Test
        @DisplayName("isCharSequence() returns false for non-CharSequence types")
        void isCharSequence_nonCharSequence_returnsFalse() {
            assertThat(TypeNameUtil.isCharSequence(Integer.class)).isFalse();
            assertThat(TypeNameUtil.isCharSequence(List.class)).isFalse();
            assertThat(TypeNameUtil.isCharSequence(null)).isFalse();
        }

        @Test
        @DisplayName("isEnum() returns true for enum types")
        void isEnum_enumTypes_returnsTrue() {
            assertThat(TypeNameUtil.isEnum(TestEnum.class)).isTrue();
            assertThat(TypeNameUtil.isEnum(Thread.State.class)).isTrue();
        }

        @Test
        @DisplayName("isEnum() returns false for non-enum types")
        void isEnum_nonEnum_returnsFalse() {
            assertThat(TypeNameUtil.isEnum(String.class)).isFalse();
            assertThat(TypeNameUtil.isEnum(List.class)).isFalse();
            assertThat(TypeNameUtil.isEnum(null)).isFalse();
        }
    }

    // ==================== getDisplayName() Tests ====================

    @Nested
    @DisplayName("getDisplayName() Method Tests")
    class GetDisplayNameTests {

        @Test
        @DisplayName("getDisplayName() returns 'null' for null input")
        void getDisplayName_nullInput_returnsNullString() {
            String result = TypeNameUtil.getDisplayName(null);
            assertThat(result).isEqualTo("null");
        }

        @Test
        @DisplayName("getDisplayName() returns simple name for java.lang types")
        void getDisplayName_javaLangType_returnsSimpleName() {
            assertThat(TypeNameUtil.getDisplayName(String.class)).isEqualTo("String");
            assertThat(TypeNameUtil.getDisplayName(Integer.class)).isEqualTo("Integer");
        }

        @Test
        @DisplayName("getDisplayName() returns simple name for primitives")
        void getDisplayName_primitive_returnsSimpleName() {
            assertThat(TypeNameUtil.getDisplayName(int.class)).isEqualTo("int");
            assertThat(TypeNameUtil.getDisplayName(boolean.class)).isEqualTo("boolean");
        }

        @Test
        @DisplayName("getDisplayName() returns FQN for non-java.lang types")
        void getDisplayName_nonJavaLangType_returnsFullyQualifiedName() {
            assertThat(TypeNameUtil.getDisplayName(List.class)).isEqualTo("java.util.List");
            assertThat(TypeNameUtil.getDisplayName(ArrayList.class)).isEqualTo("java.util.ArrayList");
        }

        @Test
        @DisplayName("getDisplayName() handles arrays of java.lang types")
        void getDisplayName_arrayOfJavaLang_returnsSimpleArrayNotation() {
            assertThat(TypeNameUtil.getDisplayName(String[].class)).isEqualTo("String[]");
            assertThat(TypeNameUtil.getDisplayName(Integer[].class)).isEqualTo("Integer[]");
        }

        @Test
        @DisplayName("getDisplayName() handles arrays of primitives")
        void getDisplayName_arrayOfPrimitives_returnsSimpleArrayNotation() {
            assertThat(TypeNameUtil.getDisplayName(int[].class)).isEqualTo("int[]");
            assertThat(TypeNameUtil.getDisplayName(boolean[].class)).isEqualTo("boolean[]");
        }

        @Test
        @DisplayName("getDisplayName() returns FQN for arrays of non-java.lang types")
        void getDisplayName_arrayOfNonJavaLang_returnsFullyQualifiedArrayNotation() {
            assertThat(TypeNameUtil.getDisplayName(List[].class)).isEqualTo("java.util.List[]");
        }
    }

    // ==================== toCompactString() Tests ====================

    @Nested
    @DisplayName("toCompactString() Method Tests")
    class ToCompactStringTests {

        @Test
        @DisplayName("toCompactString() returns '#null' for null input")
        void toCompactString_nullInput_returnsNullWithPrefix() {
            String result = TypeNameUtil.toCompactString(null);
            assertThat(result).isEqualTo("#null");
        }

        @Test
        @DisplayName("toCompactString() returns prefixed simple name for java.lang types")
        void toCompactString_javaLangType_returnsPrefixedSimpleName() {
            assertThat(TypeNameUtil.toCompactString(String.class)).isEqualTo("#String");
            assertThat(TypeNameUtil.toCompactString(Integer.class)).isEqualTo("#Integer");
        }

        @Test
        @DisplayName("toCompactString() returns prefixed simple name for primitives")
        void toCompactString_primitive_returnsPrefixedSimpleName() {
            assertThat(TypeNameUtil.toCompactString(int.class)).isEqualTo("#int");
            assertThat(TypeNameUtil.toCompactString(boolean.class)).isEqualTo("#boolean");
        }

        @Test
        @DisplayName("toCompactString() returns prefixed FQN for non-java.lang types")
        void toCompactString_nonJavaLangType_returnsPrefixedFullyQualifiedName() {
            assertThat(TypeNameUtil.toCompactString(List.class)).isEqualTo("#java.util.List");
            assertThat(TypeNameUtil.toCompactString(ArrayList.class)).isEqualTo("#java.util.ArrayList");
        }

        @Test
        @DisplayName("toCompactString() handles arrays")
        void toCompactString_arrayTypes_returnsPrefixedArrayNotation() {
            assertThat(TypeNameUtil.toCompactString(String[].class)).isEqualTo("#String[]");
            assertThat(TypeNameUtil.toCompactString(int[].class)).isEqualTo("#int[]");
            assertThat(TypeNameUtil.toCompactString(List[].class)).isEqualTo("#java.util.List[]");
        }
    }

    // ==================== Integration Tests ====================

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Methods work together consistently")
        void methods_workTogetherConsistently() {
            Class<?> clazz = ArrayList.class;

            String simpleName = TypeNameUtil.getSimpleName(clazz);
            String prettyName = TypeNameUtil.getPrettyName(clazz);
            String displayName = TypeNameUtil.getDisplayName(clazz);
            String compactString = TypeNameUtil.toCompactString(clazz);

            assertThat(simpleName).isEqualTo("ArrayList");
            assertThat(prettyName).isEqualTo("#ArrayList");
            assertThat(displayName).isEqualTo("java.util.ArrayList");
            assertThat(compactString).isEqualTo("#java.util.ArrayList");

            // Verify relationships
            assertThat(prettyName).isEqualTo("#" + simpleName);
            assertThat(compactString).startsWith("#");
        }

        @Test
        @DisplayName("Type detection methods are consistent")
        void typeDetection_consistent() {
            assertThat(TypeNameUtil.isCollectionType(ArrayList.class)).isTrue();
            assertThat(TypeNameUtil.isMapType(ArrayList.class)).isFalse();
            assertThat(TypeNameUtil.isArrayType(ArrayList.class)).isFalse();

            assertThat(TypeNameUtil.isString(String.class)).isTrue();
            assertThat(TypeNameUtil.isCharSequence(String.class)).isTrue();
            assertThat(TypeNameUtil.isEnum(String.class)).isFalse();
        }

        @Test
        @DisplayName("Array handling is consistent across methods")
        void arrayHandling_consistent() {
            Class<?> arrayClass = String[].class;

            assertThat(TypeNameUtil.isArrayType(arrayClass)).isTrue();
            assertThat(TypeNameUtil.getArrayComponentType(arrayClass)).isEqualTo(String.class);
            assertThat(TypeNameUtil.getSimpleName(arrayClass)).isEqualTo("String[]");
            assertThat(TypeNameUtil.getDisplayName(arrayClass)).isEqualTo("String[]");
            assertThat(TypeNameUtil.toCompactString(arrayClass)).isEqualTo("#String[]");
        }
    }

    // ==================== Edge Case Tests ====================

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("All methods handle null input gracefully")
        void allMethods_handleNullInput() {
            assertThatCode(() -> {
                TypeNameUtil.getSimpleName(null);
                TypeNameUtil.getPrettyName(null);
                TypeNameUtil.getFullyQualifiedName(null);
                TypeNameUtil.getPackageName(null);
                TypeNameUtil.isJavaLangType(null);
                TypeNameUtil.isPrimitive(null);
                TypeNameUtil.isWrapper(null);
                TypeNameUtil.isPrimitiveOrWrapper(null);
                TypeNameUtil.isCollectionType(null);
                TypeNameUtil.isMapType(null);
                TypeNameUtil.isArrayType(null);
                TypeNameUtil.getArrayComponentType(null);
                TypeNameUtil.formatGenericType(null);
                TypeNameUtil.isString(null);
                TypeNameUtil.isCharSequence(null);
                TypeNameUtil.isEnum(null);
                TypeNameUtil.getDisplayName(null);
                TypeNameUtil.toCompactString(null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Wrapper types set is correct")
        void wrapperTypes_setCorrect() {
            // Verify all expected wrapper types are recognized
            assertThat(TypeNameUtil.isWrapper(Boolean.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Byte.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Character.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Short.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Integer.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Long.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Float.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Double.class)).isTrue();
            assertThat(TypeNameUtil.isWrapper(Void.class)).isTrue();

            // Verify some non-wrappers
            assertThat(TypeNameUtil.isWrapper(String.class)).isFalse();
            assertThat(TypeNameUtil.isWrapper(Object.class)).isFalse();
            assertThat(TypeNameUtil.isWrapper(Number.class)).isFalse();
        }

        @Test
        @DisplayName("Multi-dimensional arrays work correctly")
        void multiDimensionalArrays_workCorrectly() {
            Class<?> multiArray = String[][].class;

            assertThat(TypeNameUtil.isArrayType(multiArray)).isTrue();
            assertThat(TypeNameUtil.getSimpleName(multiArray)).isEqualTo("String[][]");
            assertThat(TypeNameUtil.getArrayComponentType(multiArray)).isEqualTo(String[].class);

            // Component of component
            Class<?> component = TypeNameUtil.getArrayComponentType(multiArray);
            assertThat(TypeNameUtil.getArrayComponentType(component)).isEqualTo(String.class);
        }

        @Test
        @DisplayName("Anonymous and local classes are handled without throwing exceptions")
        void anonymousAndLocalClasses_handled() {
            // Anonymous class
            Object anonymous = new Object() {};
            Class<?> anonymousClass = anonymous.getClass();

            // All methods should handle anonymous classes without throwing
            assertThatCode(() -> {
                TypeNameUtil.getSimpleName(anonymousClass);
                TypeNameUtil.getPrettyName(anonymousClass);
                TypeNameUtil.getFullyQualifiedName(anonymousClass);
                TypeNameUtil.getPackageName(anonymousClass);
                TypeNameUtil.getDisplayName(anonymousClass);
                TypeNameUtil.toCompactString(anonymousClass);
                TypeNameUtil.isJavaLangType(anonymousClass);
                TypeNameUtil.isPrimitive(anonymousClass);
                TypeNameUtil.isWrapper(anonymousClass);
                TypeNameUtil.isPrimitiveOrWrapper(anonymousClass);
                TypeNameUtil.isCollectionType(anonymousClass);
                TypeNameUtil.isMapType(anonymousClass);
                TypeNameUtil.isArrayType(anonymousClass);
                TypeNameUtil.getArrayComponentType(anonymousClass);
                TypeNameUtil.isString(anonymousClass);
                TypeNameUtil.isCharSequence(anonymousClass);
                TypeNameUtil.isEnum(anonymousClass);
            }).doesNotThrowAnyException();

            // Local class example
            class LocalClass {}
            Class<?> localClass = LocalClass.class;

            // Should also work for local classes
            assertThatCode(() -> TypeNameUtil.getSimpleName(localClass))
                    .doesNotThrowAnyException();

            // Local class should have a non-empty simple name
            assertThat(TypeNameUtil.getSimpleName(localClass)).isNotEmpty();
        }
    }

    // ==================== Test Enum for Testing ====================

    private enum TestEnum {
        VALUE1, VALUE2
    }
}