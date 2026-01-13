package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.annotation.*;
import io.github.programmerjide.javadump.config.DumperConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Processes JavaDump annotations on classes and fields.
 *
 * @author Olaldejo Olajide
 * @since 1.1.0
 */
public class AnnotationProcessor {

    private final DumperConfig config;

    public AnnotationProcessor(DumperConfig config) {
        this.config = config;
    }

    /**
     * Checks if a field should be ignored based on @DumpIgnore annotation.
     */
    public boolean isIgnored(Field field) {
        return field.isAnnotationPresent(DumpIgnore.class);
    }

    /**
     * Checks if a field should be redacted based on @DumpRedact annotation.
     */
    public boolean isRedacted(Field field) {
        return field.isAnnotationPresent(DumpRedact.class);
    }

    /**
     * Gets the redaction text for a field.
     */
    public String getRedactionText(Field field) {
        DumpRedact annotation = field.getAnnotation(DumpRedact.class);
        return annotation != null ? annotation.value() : "<redacted>";
    }

    /**
     * Gets the display label for a field.
     */
    public String getFieldLabel(Field field) {
        DumpLabel annotation = field.getAnnotation(DumpLabel.class);
        return annotation != null ? annotation.value() : field.getName();
    }

    /**
     * Gets the display order for a field.
     * Returns Integer.MAX_VALUE if no order specified.
     */
    public int getFieldOrder(Field field) {
        DumpOrder annotation = field.getAnnotation(DumpOrder.class);
        return annotation != null ? annotation.value() : Integer.MAX_VALUE;
    }

    /**
     * Sorts fields by their @DumpOrder annotation.
     */
    public List<Field> sortByOrder(List<Field> fields) {
        return fields.stream()
                .sorted(Comparator.comparingInt(this::getFieldOrder)
                        .thenComparing(Field::getName))
                .collect(Collectors.toList());
    }

    /**
     * Gets custom formatter for a field if specified.
     */
    public Optional<Object> getCustomFormatter(Field field) {
        DumpFormat annotation = field.getAnnotation(DumpFormat.class);
        if (annotation == null) {
            return Optional.empty();
        }

        try {
            Class<?> formatterClass = annotation.value();
            return Optional.of(formatterClass);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Formats a value using a custom formatter.
     */
    public String formatWithCustomFormatter(Class<?> formatterClass, Object value) {
        try {
            Method formatMethod = formatterClass.getMethod("format", Object.class);
            return (String) formatMethod.invoke(null, value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    /**
     * Gets @Dumpable configuration for a class.
     */
    public Optional<DumpableConfig> getDumpableConfig(Class<?> clazz) {
        Dumpable annotation = clazz.getAnnotation(Dumpable.class);
        if (annotation == null) {
            return Optional.empty();
        }

        return Optional.of(new DumpableConfig(
                annotation.includePrivate(),
                annotation.includeStatic(),
                annotation.maxDepth(),
                Arrays.asList(annotation.exclude()),
                Arrays.asList(annotation.include())
        ));
    }

    /**
     * Gets methods annotated with @DumpMethod.
     */
    public List<Method> getDumpMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(DumpMethod.class)) {
                methods.add(method);
            }
        }

        return methods;
    }

    /**
     * Gets the label for a @DumpMethod.
     */
    public String getMethodLabel(Method method) {
        DumpMethod annotation = method.getAnnotation(DumpMethod.class);
        if (annotation != null && !annotation.value().isEmpty()) {
            return annotation.value();
        }
        return method.getName();
    }

    /**
     * Invokes a @DumpMethod and returns its result.
     */
    public Optional<String> invokeDumpMethod(Method method, Object obj) {
        try {
            method.setAccessible(true);
            Object result = method.invoke(obj);
            return Optional.ofNullable(result != null ? result.toString() : null);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Checks if a field must be shown even if null.
     */
    public boolean isNotNullAnnotated(Field field) {
        return field.isAnnotationPresent(DumpNotNull.class);
    }

    /**
     * Gets the group name for a field.
     */
    public Optional<String> getFieldGroup(Field field) {
        DumpGroup annotation = field.getAnnotation(DumpGroup.class);
        return annotation != null ? Optional.of(annotation.value()) : Optional.empty();
    }

    /**
     * Groups fields by their @DumpGroup annotation.
     */
    public Map<String, List<Field>> groupFields(List<Field> fields) {
        Map<String, List<Field>> groups = new LinkedHashMap<>();
        List<Field> ungrouped = new ArrayList<>();

        for (Field field : fields) {
            Optional<String> group = getFieldGroup(field);
            if (group.isPresent()) {
                groups.computeIfAbsent(group.get(), k -> new ArrayList<>()).add(field);
            } else {
                ungrouped.add(field);
            }
        }

        if (!ungrouped.isEmpty()) {
            groups.put("", ungrouped); // Empty string for ungrouped
        }

        return groups;
    }

    /**
     * Configuration extracted from @Dumpable annotation.
     */
    public static class DumpableConfig {
        private final boolean includePrivate;
        private final boolean includeStatic;
        private final int maxDepth;
        private final List<String> exclude;
        private final List<String> include;

        public DumpableConfig(boolean includePrivate, boolean includeStatic,
                              int maxDepth, List<String> exclude, List<String> include) {
            this.includePrivate = includePrivate;
            this.includeStatic = includeStatic;
            this.maxDepth = maxDepth;
            this.exclude = exclude;
            this.include = include;
        }

        public boolean isIncludePrivate() { return includePrivate; }
        public boolean isIncludeStatic() { return includeStatic; }
        public int getMaxDepth() { return maxDepth; }
        public List<String> getExclude() { return exclude; }
        public List<String> getInclude() { return include; }

        public boolean shouldIncludeField(String fieldName) {
            if (!include.isEmpty()) {
                return include.contains(fieldName);
            }
            return !exclude.contains(fieldName);
        }
    }
}