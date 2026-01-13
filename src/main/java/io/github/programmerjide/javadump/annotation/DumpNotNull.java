package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Specifies that a field should be dumped even if null.
 *
 * <p>By default, null fields may be shown as "null" or hidden
 * based on configuration. This annotation forces them to always show.
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     @DumpNotNull
 *     private String middleName; // Shows even if null
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DumpNotNull {
}