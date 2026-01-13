package io.github.programmerjide.javadump.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Type name formatting utilities.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public final class TypeNameUtil {

    private static final String TYPE_PREFIX = "#";

    private static final Set<Class<?>> WRAPPER_TYPES = Set.of(
            Boolean.class, Byte.class, Character.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class
    );

    private TypeNameUtil() {
        throw new AssertionError("Utility class");
    }

    /**
     * Gets simple class name.
     */
    public static String getSimpleName(Class<?> clazz) {
        if (clazz == null) {
            return "null";
        }

        if (clazz.isArray()) {
            return getSimpleName(clazz.getComponentType()) + "[]";
        }

        return clazz.getSimpleName();
    }

    /**
     * Gets pretty formatted type name with prefix.
     */
    public static String getPrettyName(Class<?> clazz) {
        if (clazz == null) {
            return TYPE_PREFIX + "null";
        }

        return TYPE_PREFIX + getSimpleName(clazz);
    }

    /**
     * Gets fully qualified class name.
     */
    public static String getFullyQualifiedName(Class<?> clazz) {
        if (clazz == null) {
            return "null";
        }

        if (clazz.isArray()) {
            return getFullyQualifiedName(clazz.getComponentType()) + "[]";
        }

        if (clazz.isPrimitive()) {
            return clazz.getName();
        }

        return clazz.getName();
    }

    /**
     * Gets package name.
     */
    public static String getPackageName(Class<?> clazz) {
        if (clazz == null || clazz.isArray() || clazz.isPrimitive()) {
            return "";
        }

        Package pkg = clazz.getPackage();
        return pkg != null ? pkg.getName() : "";
    }

    /**
     * Checks if type is from java.lang package.
     */
    public static boolean isJavaLangType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return "java.lang".equals(getPackageName(clazz));
    }

    /**
     * Checks if type is primitive.
     */
    public static boolean isPrimitive(Class<?> clazz) {
        return clazz != null && clazz.isPrimitive();
    }

    /**
     * Checks if type is primitive wrapper.
     */
    public static boolean isWrapper(Class<?> clazz) {
        return clazz != null && WRAPPER_TYPES.contains(clazz);
    }

    /**
     * Checks if type is primitive or wrapper.
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return isPrimitive(clazz) || isWrapper(clazz);
    }

    /**
     * Checks if type is collection.
     */
    public static boolean isCollectionType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * Checks if type is map.
     */
    public static boolean isMapType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return Map.class.isAssignableFrom(clazz);
    }

    /**
     * Checks if type is array.
     */
    public static boolean isArrayType(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    /**
     * Gets array component type.
     */
    public static Class<?> getArrayComponentType(Class<?> clazz) {
        if (clazz == null || !clazz.isArray()) {
            return null;
        }
        return clazz.getComponentType();
    }

    /**
     * Formats generic type with type parameters.
     */
    public static String formatGenericType(Type type) {
        if (type == null) {
            return "null";
        }

        if (type instanceof Class<?>) {
            return getSimpleName((Class<?>) type);
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type rawType = paramType.getRawType();
            Type[] typeArgs = paramType.getActualTypeArguments();

            StringBuilder sb = new StringBuilder();
            sb.append(formatGenericType(rawType));

            if (typeArgs.length > 0) {
                sb.append("<");
                for (int i = 0; i < typeArgs.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(formatGenericType(typeArgs[i]));
                }
                sb.append(">");
            }

            return sb.toString();
        }

        return type.getTypeName();
    }

    /**
     * Checks if type is String.
     */
    public static boolean isString(Class<?> clazz) {
        return clazz != null && clazz == String.class;
    }

    /**
     * Checks if type is CharSequence or subtype.
     */
    public static boolean isCharSequence(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return CharSequence.class.isAssignableFrom(clazz);
    }

    /**
     * Checks if type is enum.
     */
    public static boolean isEnum(Class<?> clazz) {
        return clazz != null && clazz.isEnum();
    }

    /**
     * Gets display-friendly type name.
     * Returns simple name for java.lang types, FQN for others.
     */
    public static String getDisplayName(Class<?> clazz) {
        if (clazz == null) {
            return "null";
        }

        if (isArrayType(clazz)) {
            Class<?> componentType = getArrayComponentType(clazz);
            if (isJavaLangType(componentType) || isPrimitive(componentType)) {
                return getSimpleName(clazz);
            }
            return getDisplayName(componentType) + "[]";
        }

        if (isPrimitive(clazz) || isJavaLangType(clazz)) {
            return getSimpleName(clazz);
        }

        return getFullyQualifiedName(clazz);
    }

    /**
     * Converts type to compact string with # prefix.
     * Uses simple name for java.lang types, FQN for others.
     */
    public static String toCompactString(Class<?> clazz) {
        if (clazz == null) {
            return TYPE_PREFIX + "null";
        }

        if (isArrayType(clazz)) {
            Class<?> componentType = getArrayComponentType(clazz);
            if (isJavaLangType(componentType) || isPrimitive(componentType)) {
                return TYPE_PREFIX + getSimpleName(clazz);
            }
            return TYPE_PREFIX + getDisplayName(componentType) + "[]";
        }

        if (isPrimitive(clazz) || isJavaLangType(clazz)) {
            return TYPE_PREFIX + getSimpleName(clazz);
        }

        return TYPE_PREFIX + getFullyQualifiedName(clazz);
    }
}