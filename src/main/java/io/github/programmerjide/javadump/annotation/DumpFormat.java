package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Specifies a custom formatter for a field.
 *
 * <p>The formatter class must have a static method:
 * {@code public static String format(Object value)}
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     @DumpFormat(DateFormatter.class)
 *     private LocalDateTime createdAt;
 * }
 *
 * public class DateFormatter {
 *     public static String format(Object value) {
 *         LocalDateTime date = (LocalDateTime) value;
 *         return date.format(DateTimeFormatter.ISO_DATE_TIME);
 *     }
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DumpFormat {
    /**
     * The formatter class containing a static format(Object) method.
     */
    Class<?> value();
}