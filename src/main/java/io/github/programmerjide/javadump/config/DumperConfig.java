package io.github.programmerjide.javadump.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for Dumper behavior.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class DumperConfig {

    private final int maxDepth;
    private final int maxItems;
    private final int maxStringLen;
    private final boolean colorEnabled;
    private final boolean showPrivateFields;
    private final boolean showStaticFields;
    private final boolean showTransientFields;
    private final boolean showHeader;
    private final boolean disableStringer;
    private final Set<String> onlyFields;
    private final Set<String> excludeFields;
    private final Set<String> redactFields;
    private final FieldMatchMode fieldMatchMode;
    private final FieldMatchMode redactMatchMode;
    private final boolean redactSensitive;

    /**
     * Field matching modes for filtering and redaction.
     */
    public enum FieldMatchMode {
        /** Exact match (default) */
        EXACT,
        /** Case-insensitive match */
        IGNORE_CASE,
        /** Contains substring */
        CONTAINS,
        /** Starts with */
        STARTS_WITH,
        /** Ends with */
        ENDS_WITH
    }

    /**
     * Default sensitive field patterns.
     */
    private static final Set<String> DEFAULT_SENSITIVE_PATTERNS = Set.of(
            "password", "passwd", "pwd",
            "secret", "token", "apikey", "api_key",
            "ssn", "social_security",
            "creditcard", "credit_card", "cardnumber", "card_number",
            "cvv", "cvc",
            "pin",
            "privatekey", "private_key"
    );

    private DumperConfig(Builder builder) {
        this.maxDepth = builder.maxDepth;
        this.maxItems = builder.maxItems;
        this.maxStringLen = builder.maxStringLen;
        this.colorEnabled = builder.colorEnabled;
        this.showPrivateFields = builder.showPrivateFields;
        this.showStaticFields = builder.showStaticFields;
        this.showTransientFields = builder.showTransientFields;
        this.showHeader = builder.showHeader;
        this.disableStringer = builder.disableStringer;
        this.onlyFields = builder.onlyFields != null ?
                Collections.unmodifiableSet(new HashSet<>(builder.onlyFields)) :
                Collections.emptySet();
        this.excludeFields = builder.excludeFields != null ?
                Collections.unmodifiableSet(new HashSet<>(builder.excludeFields)) :
                Collections.emptySet();
        this.redactFields = builder.redactFields != null ?
                Collections.unmodifiableSet(new HashSet<>(builder.redactFields)) :
                Collections.emptySet();
        this.fieldMatchMode = builder.fieldMatchMode;
        this.redactMatchMode = builder.redactMatchMode;
        this.redactSensitive = builder.redactSensitive;
    }

    // Legacy constructor for backward compatibility
    public DumperConfig(int maxDepth, int maxItems, boolean colorEnabled) {
        this(maxDepth, maxItems, colorEnabled, true, false);
    }

    public DumperConfig(int maxDepth, int maxItems, boolean colorEnabled,
                        boolean showPrivateFields, boolean showStaticFields) {
        this.maxDepth = maxDepth;
        this.maxItems = maxItems;
        this.maxStringLen = 100000;
        this.colorEnabled = colorEnabled;
        this.showPrivateFields = showPrivateFields;
        this.showStaticFields = showStaticFields;
        this.showTransientFields = false;
        this.showHeader = true;
        this.disableStringer = false;
        this.onlyFields = Collections.emptySet();
        this.excludeFields = Collections.emptySet();
        this.redactFields = Collections.emptySet();
        this.fieldMatchMode = FieldMatchMode.EXACT;
        this.redactMatchMode = FieldMatchMode.EXACT;
        this.redactSensitive = false;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public int getMaxDepth() { return maxDepth; }
    public int getMaxItems() { return maxItems; }
    public int getMaxStringLen() { return maxStringLen; }
    public boolean isColorEnabled() { return colorEnabled; }
    public boolean isShowPrivateFields() { return showPrivateFields; }
    public boolean isShowStaticFields() { return showStaticFields; }
    public boolean isShowTransientFields() { return showTransientFields; }
    public boolean isShowHeader() { return showHeader; }
    public boolean isDisableStringer() { return disableStringer; }
    public Set<String> getOnlyFields() { return onlyFields; }
    public Set<String> getExcludeFields() { return excludeFields; }
    public Set<String> getRedactFields() { return redactFields; }
    public FieldMatchMode getFieldMatchMode() { return fieldMatchMode; }
    public FieldMatchMode getRedactMatchMode() { return redactMatchMode; }
    public boolean isRedactSensitive() { return redactSensitive; }

    /**
     * Checks if a field should be included based on filtering rules.
     */
    public boolean shouldIncludeField(String fieldName) {
        // If onlyFields is specified, field must be in that list
        if (!onlyFields.isEmpty()) {
            return matchesAny(fieldName, onlyFields, fieldMatchMode);
        }

        // If excludeFields is specified, field must not be in that list
        if (!excludeFields.isEmpty()) {
            return !matchesAny(fieldName, excludeFields, fieldMatchMode);
        }

        return true;
    }

    /**
     * Checks if a field should be redacted.
     */
    public boolean shouldRedactField(String fieldName) {
        // Check explicit redact fields
        if (!redactFields.isEmpty() &&
                matchesAny(fieldName, redactFields, redactMatchMode)) {
            return true;
        }

        // Check sensitive patterns if enabled
        if (redactSensitive) {
            return matchesAny(fieldName.toLowerCase(),
                    DEFAULT_SENSITIVE_PATTERNS,
                    FieldMatchMode.CONTAINS);
        }

        return false;
    }

    /**
     * Checks if a field name matches any pattern in the set.
     */
    private boolean matchesAny(String fieldName, Set<String> patterns,
                               FieldMatchMode mode) {
        for (String pattern : patterns) {
            if (matches(fieldName, pattern, mode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a field name matches a pattern using the specified mode.
     */
    private boolean matches(String fieldName, String pattern, FieldMatchMode mode) {
        switch (mode) {
            case EXACT:
                return fieldName.equals(pattern);
            case IGNORE_CASE:
                return fieldName.equalsIgnoreCase(pattern);
            case CONTAINS:
                return fieldName.toLowerCase().contains(pattern.toLowerCase());
            case STARTS_WITH:
                return fieldName.toLowerCase().startsWith(pattern.toLowerCase());
            case ENDS_WITH:
                return fieldName.toLowerCase().endsWith(pattern.toLowerCase());
            default:
                return false;
        }
    }

    /**
     * Builder for DumperConfig.
     */
    public static class Builder {
        private int maxDepth = 15;
        private int maxItems = 100;
        private int maxStringLen = 100000;
        private boolean colorEnabled = true;
        private boolean showPrivateFields = true;
        private boolean showStaticFields = false;
        private boolean showTransientFields = false;
        private boolean showHeader = true;
        private boolean disableStringer = false;
        private Set<String> onlyFields;
        private Set<String> excludeFields;
        private Set<String> redactFields;
        private FieldMatchMode fieldMatchMode = FieldMatchMode.EXACT;
        private FieldMatchMode redactMatchMode = FieldMatchMode.EXACT;
        private boolean redactSensitive = false;

        public Builder maxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }

        public Builder maxItems(int maxItems) {
            this.maxItems = maxItems;
            return this;
        }

        public Builder maxStringLen(int maxStringLen) {
            this.maxStringLen = maxStringLen;
            return this;
        }

        public Builder colorEnabled(boolean colorEnabled) {
            this.colorEnabled = colorEnabled;
            return this;
        }

        public Builder showPrivateFields(boolean showPrivateFields) {
            this.showPrivateFields = showPrivateFields;
            return this;
        }

        public Builder showStaticFields(boolean showStaticFields) {
            this.showStaticFields = showStaticFields;
            return this;
        }

        public Builder showTransientFields(boolean showTransientFields) {
            this.showTransientFields = showTransientFields;
            return this;
        }

        public Builder showHeader(boolean showHeader) {
            this.showHeader = showHeader;
            return this;
        }

        public Builder disableStringer(boolean disableStringer) {
            this.disableStringer = disableStringer;
            return this;
        }

        public Builder onlyFields(String... fields) {
            this.onlyFields = new HashSet<>();
            Collections.addAll(this.onlyFields, fields);
            return this;
        }

        public Builder excludeFields(String... fields) {
            this.excludeFields = new HashSet<>();
            Collections.addAll(this.excludeFields, fields);
            return this;
        }

        public Builder redactFields(String... fields) {
            this.redactFields = new HashSet<>();
            Collections.addAll(this.redactFields, fields);
            return this;
        }

        public Builder fieldMatchMode(FieldMatchMode mode) {
            this.fieldMatchMode = mode;
            return this;
        }

        public Builder redactMatchMode(FieldMatchMode mode) {
            this.redactMatchMode = mode;
            return this;
        }

        public Builder redactSensitive(boolean redactSensitive) {
            this.redactSensitive = redactSensitive;
            return this;
        }

        public DumperConfig build() {
            return new DumperConfig(this);
        }
    }
}