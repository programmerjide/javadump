package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Specifies the display order of fields in dump output.
 *
 * <p>Lower values appear first. Fields without this annotation
 * appear after ordered fields in their natural order.
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     @DumpOrder(1)
 *     private Long id;
 *
 *     @DumpOrder(2)
 *     private String name;
 *
 *     @DumpOrder(3)
 *     private String email;
 *
 *     private String createdAt; // appears last
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DumpOrder {
    /**
     * The display order (lower values first).
     */
    int value();
}
