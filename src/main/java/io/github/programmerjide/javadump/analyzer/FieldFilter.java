package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.config.DumperConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filters fields based on configuration and exclusion rules.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class FieldFilter {

    private final DumperConfig config;
    private final Set<String> excludedNames;

    // Common fields to exclude by default
    private static final Set<String> DEFAULT_EXCLUDED = new HashSet<>();
    static {
        DEFAULT_EXCLUDED.add("serialVersionUID");
        DEFAULT_EXCLUDED.add("$jacocoData");
        DEFAULT_EXCLUDED.add("__$lineHits$__");
        DEFAULT_EXCLUDED.add("$assertionsDisabled");
    }

    public FieldFilter(DumperConfig config) {
        this.config = config;
        this.excludedNames = new HashSet<>(DEFAULT_EXCLUDED);
    }

    /**
     * Gets all accessible fields for a class based on configuration.
     *
     * @param clazz the class to get fields from
     * @return list of accessible fields
     */
    public List<Field> getAccessibleFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();

        if (clazz == null) {
            return result;
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (shouldSkip(field)) {
                continue;
            }
            result.add(field);
        }

        return result;
    }

    /**
     * Checks if a field should be skipped based on configuration.
     *
     * @param field the field to check
     * @return true if field should be skipped
     */
    public boolean shouldSkip(Field field) {
        if (field == null) {
            return true;
        }

        int modifiers = field.getModifiers();
        String fieldName = field.getName();

        // Skip excluded field names
        if (excludedNames.contains(fieldName)) {
            return true;
        }

        // Skip static fields if not configured to show them
        if (Modifier.isStatic(modifiers) && !config.isShowStaticFields()) {
            return true;
        }

        // Skip private fields if not configured to show them
        if (Modifier.isPrivate(modifiers) && !config.isShowPrivateFields()) {
            return true;
        }

        // Skip transient fields if not configured to show them
        if (Modifier.isTransient(modifiers) && !config.isShowTransientFields()) {
            return true;
        }

        return false;
    }

    /**
     * Adds a field name to the exclusion list.
     *
     * @param fieldName the field name to exclude
     */
    public void exclude(String fieldName) {
        if (fieldName != null && !fieldName.isEmpty()) {
            excludedNames.add(fieldName);
        }
    }

    /**
     * Removes a field name from the exclusion list.
     *
     * @param fieldName the field name to include
     */
    public void include(String fieldName) {
        excludedNames.remove(fieldName);
    }

    /**
     * Clears all custom exclusions (keeps default exclusions).
     */
    public void clearCustomExclusions() {
        excludedNames.clear();
        excludedNames.addAll(DEFAULT_EXCLUDED);
    }

    /**
     * Checks if a field is public.
     *
     * @param field the field to check
     * @return true if field is public
     */
    public static boolean isPublic(Field field) {
        return field != null && Modifier.isPublic(field.getModifiers());
    }

    /**
     * Checks if a field is private.
     *
     * @param field the field to check
     * @return true if field is private
     */
    public static boolean isPrivate(Field field) {
        return field != null && Modifier.isPrivate(field.getModifiers());
    }

    /**
     * Checks if a field is protected.
     *
     * @param field the field to check
     * @return true if field is protected
     */
    public static boolean isProtected(Field field) {
        return field != null && Modifier.isProtected(field.getModifiers());
    }

    /**
     * Gets the visibility marker for a field.
     *
     * @param field the field to check
     * @return "+" for public, "-" for private, "#" for protected, "~" for package-private
     */
    public static String getVisibilityMarker(Field field) {
        if (field == null) {
            return "?";
        }

        int modifiers = field.getModifiers();

        if (Modifier.isPublic(modifiers)) {
            return "+";
        } else if (Modifier.isPrivate(modifiers)) {
            return "-";
        } else if (Modifier.isProtected(modifiers)) {
            return "#";
        } else {
            return "~"; // package-private
        }
    }

    /**
     * Gets the excluded field names.
     *
     * @return set of excluded field names
     */
    public Set<String> getExcludedNames() {
        return new HashSet<>(excludedNames);
    }

    /**
     * Resets the filter to default exclusions.
     */
    public void reset() {
        excludedNames.clear();
        excludedNames.addAll(DEFAULT_EXCLUDED);
    }
}