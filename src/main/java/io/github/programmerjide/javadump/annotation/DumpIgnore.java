package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Marks a field to be ignored during dumping.
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     private String username;
 *
 *     @DumpIgnore
 *     private String password;
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DumpIgnore {
}
