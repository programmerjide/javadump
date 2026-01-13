package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.util.TypeNameUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;

/**
 * Classifies types into categories for specialized formatting.
 *
 * <p>This helps the formatter decide how to display different types of objects.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class TypeClassifier {

    /**
     * Type categories for specialized handling.
     */
    public enum TypeCategory {
        NULL,
        PRIMITIVE,
        STRING,
        ENUM,
        ARRAY,
        COLLECTION,
        MAP,
        DATE_TIME,
        NUMERIC,
        OBJECT,
        CYCLIC,
        TRUNCATED,
        ERROR
    }

    /**
     * Classifies an object's type into a category.
     */
    public static TypeCategory classify(Object obj) {
        if (obj == null) {
            return TypeCategory.NULL;
        }

        Class<?> clazz = obj.getClass();

        if (TypeNameUtil.isPrimitiveOrWrapper(clazz)) {
            return TypeCategory.PRIMITIVE;
        }

        if (TypeNameUtil.isString(clazz)) {
            return TypeCategory.STRING;
        }

        if (TypeNameUtil.isEnum(clazz)) {
            return TypeCategory.ENUM;
        }

        if (TypeNameUtil.isArrayType(clazz)) {
            return TypeCategory.ARRAY;
        }

        if (TypeNameUtil.isCollectionType(clazz)) {
            return TypeCategory.COLLECTION;
        }

        if (TypeNameUtil.isMapType(clazz)) {
            return TypeCategory.MAP;
        }

        if (isDateTime(clazz)) {
            return TypeCategory.DATE_TIME;
        }

        if (isNumeric(clazz)) {
            return TypeCategory.NUMERIC;
        }

        return TypeCategory.OBJECT;
    }

    /**
     * Checks if a type is a date/time type.
     */
    private static boolean isDateTime(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz) ||
                LocalDate.class.isAssignableFrom(clazz) ||
                LocalTime.class.isAssignableFrom(clazz) ||
                LocalDateTime.class.isAssignableFrom(clazz) ||
                ZonedDateTime.class.isAssignableFrom(clazz) ||
                OffsetDateTime.class.isAssignableFrom(clazz) ||
                Instant.class.isAssignableFrom(clazz);
    }

    /**
     * Checks if a type is a special numeric type (BigDecimal, BigInteger).
     */
    private static boolean isNumeric(Class<?> clazz) {
        return BigDecimal.class.isAssignableFrom(clazz) ||
                BigInteger.class.isAssignableFrom(clazz);
    }

    /**
     * Checks if a type should be displayed inline (on one line).
     */
    public static boolean shouldDisplayInline(Object obj) {
        if (obj == null) {
            return true;
        }

        TypeCategory category = classify(obj);
        return category == TypeCategory.NULL ||
                category == TypeCategory.PRIMITIVE ||
                category == TypeCategory.STRING ||
                category == TypeCategory.ENUM ||
                category == TypeCategory.DATE_TIME ||
                category == TypeCategory.NUMERIC;
    }

    /**
     * Checks if a type is a simple value (primitive, string, enum).
     */
    public static boolean isSimpleValue(Object obj) {
        if (obj == null) {
            return true;
        }

        Class<?> clazz = obj.getClass();
        return TypeNameUtil.isPrimitiveOrWrapper(clazz) ||
                TypeNameUtil.isString(clazz) ||
                TypeNameUtil.isEnum(clazz);
    }
}
