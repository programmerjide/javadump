package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Provides a custom label for a field in dump output.
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     @DumpLabel("User ID")
 *     private Long id;
 *
 *     @DumpLabel("Full Name")
 *     private String name;
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DumpLabel {
    /**
     * The custom label to display.
     */
    String value();
}