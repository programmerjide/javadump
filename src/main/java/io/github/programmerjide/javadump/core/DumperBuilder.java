package io.github.programmerjide.javadump.core;

import io.github.programmerjide.javadump.config.DumperConfig;

import java.io.PrintStream;

/**
 * Builder for creating configured Dumper instances with fluent API.
 *
 * <p>Example usage:
 * <pre>{@code
 * Dumper dumper = Dumper.builder()
 *     .withMaxDepth(10)
 *     .withMaxItems(50)
 *     .withMaxStringLen(100)
 *     .withoutColor()
 *     .withOnlyFields("id", "name")
 *     .withRedactSensitive()
 *     .build();
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class DumperBuilder {

    private final DumperConfig.Builder configBuilder;
    private PrintStream outputStream = System.out;

    public DumperBuilder() {
        this.configBuilder = DumperConfig.builder();
    }

    // ==================== Depth & Size Controls ====================

    /**
     * Sets the maximum depth for nested object traversal.
     *
     * <p>Default: 15
     *
     * @param maxDepth maximum depth (must be positive)
     * @return this builder
     * @throws IllegalArgumentException if maxDepth is not positive
     */
    public DumperBuilder withMaxDepth(int maxDepth) {
        if (maxDepth <= 0) {
            throw new IllegalArgumentException("maxDepth must be positive");
        }
        configBuilder.maxDepth(maxDepth);
        return this;
    }

    /**
     * Sets the maximum number of items to display in collections.
     *
     * <p>Default: 100
     *
     * @param maxItems maximum items (must be positive)
     * @return this builder
     * @throws IllegalArgumentException if maxItems is not positive
     */
    public DumperBuilder withMaxItems(int maxItems) {
        if (maxItems <= 0) {
            throw new IllegalArgumentException("maxItems must be positive");
        }
        configBuilder.maxItems(maxItems);
        return this;
    }

    /**
     * Sets the maximum string length before truncation.
     *
     * <p>Strings longer than this will be truncated with an ellipsis (…).
     * Default: 100000
     *
     * <p>Example:
     * <pre>{@code
     * Dumper dumper = Dumper.builder()
     *     .withMaxStringLen(50)
     *     .build();
     *
     * dumper.dump("This is a very long string..."); // "This is a very long str…" #string
     * }</pre>
     *
     * @param maxStringLen maximum string length (must be positive)
     * @return this builder
     * @throws IllegalArgumentException if maxStringLen is not positive
     */
    public DumperBuilder withMaxStringLen(int maxStringLen) {
        if (maxStringLen <= 0) {
            throw new IllegalArgumentException("maxStringLen must be positive");
        }
        configBuilder.maxStringLen(maxStringLen);
        return this;
    }

    // ==================== Display Options ====================

    /**
     * Disables color output.
     *
     * <p>Default: colors enabled
     *
     * @return this builder
     */
    public DumperBuilder withoutColor() {
        configBuilder.colorEnabled(false);
        return this;
    }

    /**
     * Enables color output (default).
     *
     * @return this builder
     */
    public DumperBuilder withColor() {
        configBuilder.colorEnabled(true);
        return this;
    }

    /**
     * Disables the file:line header in output.
     *
     * <p>By default, dumps show where they were called from:
     * <pre>
     * &lt;#dump // Example.java:42
     * </pre>
     *
     * <p>This option removes that header for cleaner output.
     *
     * @return this builder
     */
    public DumperBuilder withoutHeader() {
        configBuilder.showHeader(false);
        return this;
    }

    /**
     * Enables the file:line header in output (default).
     *
     * @return this builder
     */
    public DumperBuilder withHeader() {
        configBuilder.showHeader(true);
        return this;
    }

    // ==================== Field Visibility ====================

    /**
     * Shows private fields (default).
     *
     * @return this builder
     */
    public DumperBuilder withPrivateFields() {
        configBuilder.showPrivateFields(true);
        return this;
    }

    /**
     * Hides private fields.
     *
     * @return this builder
     */
    public DumperBuilder withoutPrivateFields() {
        configBuilder.showPrivateFields(false);
        return this;
    }

    /**
     * Shows static fields.
     *
     * @return this builder
     */
    public DumperBuilder withStaticFields() {
        configBuilder.showStaticFields(true);
        return this;
    }

    /**
     * Hides static fields (default).
     *
     * @return this builder
     */
    public DumperBuilder withoutStaticFields() {
        configBuilder.showStaticFields(false);
        return this;
    }

    // ==================== Field Filtering ====================

    /**
     * Shows only the specified fields in structs/objects.
     *
     * <p>When set, only fields matching these names will be displayed.
     * Cannot be used together with {@link #withExcludeFields}.
     *
     * <p>Example:
     * <pre>{@code
     * Dumper dumper = Dumper.builder()
     *     .withOnlyFields("id", "name", "email")
     *     .build();
     *
     * dumper.dump(user); // Only shows id, name, and email fields
     * }</pre>
     *
     * @param fields field names to include
     * @return this builder
     */
    public DumperBuilder withOnlyFields(String... fields) {
        configBuilder.onlyFields(fields);
        return this;
    }

    /**
     * Excludes the specified fields from structs/objects.
     *
     * <p>When set, fields matching these names will be hidden.
     * Cannot be used together with {@link #withOnlyFields}.
     *
     * <p>Example:
     * <pre>{@code
     * Dumper dumper = Dumper.builder()
     *     .withExcludeFields("password", "secretToken")
     *     .build();
     *
     * dumper.dump(user); // Hides password and secretToken
     * }</pre>
     *
     * @param fields field names to exclude
     * @return this builder
     */
    public DumperBuilder withExcludeFields(String... fields) {
        configBuilder.excludeFields(fields);
        return this;
    }

    /**
     * Sets the field matching mode for include/exclude filters.
     *
     * <p>Default: EXACT
     *
     * <p>Example:
     * <pre>{@code
     * Dumper dumper = Dumper.builder()
     *     .withExcludeFields("id")
     *     .withFieldMatchMode(FieldMatchMode.CONTAINS)
     *     .build();
     *
     * // Will hide: userId, orderId, customerId, etc.
     * }</pre>
     *
     * @param mode the matching mode
     * @return this builder
     */
    public DumperBuilder withFieldMatchMode(DumperConfig.FieldMatchMode mode) {
        configBuilder.fieldMatchMode(mode);
        return this;
    }

    // ==================== Field Redaction ====================

    /**
     * Redacts (masks) the specified fields with "&lt;redacted&gt;".
     *
     * <p>Useful for hiding sensitive data while still showing structure.
     *
     * <p>Example:
     * <pre>{@code
     * Dumper dumper = Dumper.builder()
     *     .withRedactFields("password", "ssn", "creditCard")
     *     .build();
     *
     * dumper.dump(user);
     * // Output:
     * // #User {
     * //   +password => <redacted> #string
     * //   +ssn => <redacted> #string
     * // }
     * }</pre>
     *
     * @param fields field names to redact
     * @return this builder
     */
    public DumperBuilder withRedactFields(String... fields) {
        configBuilder.redactFields(fields);
        return this;
    }

    /**
     * Sets the field matching mode for redaction.
     *
     * <p>Default: EXACT
     *
     * @param mode the matching mode
     * @return this builder
     */
    public DumperBuilder withRedactMatchMode(DumperConfig.FieldMatchMode mode) {
        configBuilder.redactMatchMode(mode);
        return this;
    }

    /**
     * Enables automatic redaction of common sensitive fields.
     *
     * <p>Automatically redacts fields with names containing:
     * <ul>
     *   <li>password, passwd, pwd</li>
     *   <li>secret, token, apikey</li>
     *   <li>ssn, social_security</li>
     *   <li>creditcard, cvv, cvc, pin</li>
     *   <li>privatekey</li>
     * </ul>
     *
     * <p>Example:
     * <pre>{@code
     * Dumper dumper = Dumper.builder()
     *     .withRedactSensitive()
     *     .build();
     *
     * // Automatically redacts password, apiToken, etc.
     * }</pre>
     *
     * @return this builder
     */
    public DumperBuilder withRedactSensitive() {
        configBuilder.redactSensitive(true);
        return this;
    }

    // ==================== Stringer Control ====================

    /**
     * Disables using toString() for objects that implement it.
     *
     * <p>By default, if an object has a toString() method, that's used.
     * This option forces inspection of the actual fields instead.
     *
     * <p>Example:
     * <pre>{@code
     * Dumper dumper = Dumper.builder()
     *     .withDisableStringer()
     *     .build();
     *
     * // Shows actual fields instead of toString() output
     * }</pre>
     *
     * @return this builder
     */
    public DumperBuilder withDisableStringer() {
        configBuilder.disableStringer(true);
        return this;
    }

    /**
     * Enables using toString() for objects (default).
     *
     * @return this builder
     */
    public DumperBuilder withStringer() {
        configBuilder.disableStringer(false);
        return this;
    }

    // ==================== Output Stream ====================

    /**
     * Sets a custom output stream.
     *
     * <p>Default: System.out
     *
     * <p>Example:
     * <pre>{@code
     * // Write to file
     * Dumper dumper = Dumper.builder()
     *     .withWriter(new PrintStream(new FileOutputStream("debug.log")))
     *     .build();
     *
     * // Write to string
     * ByteArrayOutputStream baos = new ByteArrayOutputStream();
     * Dumper dumper = Dumper.builder()
     *     .withWriter(new PrintStream(baos))
     *     .build();
     * }</pre>
     *
     * @param outputStream the output stream to use
     * @return this builder
     * @throws IllegalArgumentException if outputStream is null
     */
    public DumperBuilder withWriter(PrintStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null");
        }
        this.outputStream = outputStream;
        return this;
    }

    // ==================== Build ====================

    /**
     * Builds a new Dumper instance with the configured settings.
     *
     * @return a new Dumper instance
     */
    public Dumper build() {
        return new Dumper(configBuilder.build(), outputStream);
    }
}