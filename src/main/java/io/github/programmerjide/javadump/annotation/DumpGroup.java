package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Groups related fields together in output.
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     @DumpGroup("identity")
 *     private Long id;
 *
 *     @DumpGroup("identity")
 *     private String username;
 *
 *     @DumpGroup("contact")
 *     private String email;
 *
 *     @DumpGroup("contact")
 *     private String phone;
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DumpGroup {
    /**
     * The group name.
     */
    String value();
}