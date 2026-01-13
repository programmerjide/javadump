package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Provides a custom toString-like method for dumping.
 *
 * <p>The method must be public, take no parameters, and return String.
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     private String firstName;
 *     private String lastName;
 *
 *     @DumpMethod
 *     public String getFullName() {
 *         return firstName + " " + lastName;
 *     }
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DumpMethod {
    /**
     * The label to use for this method's output.
     * If empty, uses the method name.
     */
    String value() default "";
}
