package io.github.programmerjide.javadump.annotation;

import java.lang.annotation.*;

/**
 * Marks a class to use a specific dump strategy.
 *
 * <p>Example:
 * <pre>{@code
 * @Dumpable(
 *     includePrivate = false,
 *     includeStatic = true,
 *     maxDepth = 3
 * )
 * public class User {
 *     // ...
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Dumpable {
    /**
     * Whether to include private fields.
     */
    boolean includePrivate() default true;

    /**
     * Whether to include static fields.
     */
    boolean includeStatic() default false;

    /**
     * Maximum depth for this object.
     */
    int maxDepth() default -1; // -1 means use global config

    /**
     * Fields to exclude.
     */
    String[] exclude() default {};

    /**
     * Fields to include (if specified, only these are shown).
     */
    String[] include() default {};
}
