package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Marks a field to be redacted (masked) during dumping.
 *
 * <p>The field value will be replaced with "&lt;redacted&gt;" in output.
 *
 * <p>Example:
 * <pre>{@code
 * public class User {
 *     private String username;
 *
 *     @DumpRedact
 *     private String ssn;
 *
 *     @DumpRedact
 *     private String creditCard;
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DumpRedact {
    /**
     * Custom redaction text (optional).
     * Default: "&lt;redacted&gt;"
     */
    String value() default "<redacted>";
}



