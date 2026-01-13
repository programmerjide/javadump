package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.StringUtil;
import io.github.programmerjide.javadump.util.TypeNameUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Analyzes objects and creates DumpNode trees with field filtering and redaction.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class ObjectAnalyzer {

    private final DumperConfig config;
    private final Set<Integer> visitedObjects;
    private final TypeClassifier typeClassifier;
    private final FieldFilter fieldFilter;

    public ObjectAnalyzer(DumperConfig config) {
        this.config = config;
        this.visitedObjects = new HashSet<>();
        this.typeClassifier = new TypeClassifier();
        this.fieldFilter = new FieldFilter(config);
    }

    /**
     * Analyzes an object and returns a DumpNode.
     */
    public DumpNode analyze(Object obj, int depth) {
        // Handle null
        if (obj == null) {
            return DumpNode.builder()
                    .type(DumpNode.NodeType.NULL)
                    .typeName("null")
                    .build();
        }

        // Check max depth
        if (depth >= config.getMaxDepth()) {
            return DumpNode.builder()
                    .type(DumpNode.NodeType.OBJECT)
                    .clazz(obj.getClass())
                    .typeName(TypeNameUtil.getSimpleName(obj.getClass()))
                    .maxDepthReached(true)
                    .build();
        }

        // Check circular reference
        int objectId = System.identityHashCode(obj);
        if (visitedObjects.contains(objectId)) {
            return DumpNode.builder()
                    .type(DumpNode.NodeType.OBJECT)
                    .clazz(obj.getClass())
                    .typeName(TypeNameUtil.getSimpleName(obj.getClass()))
                    .circular(true)
                    .build();
        }

        Class<?> clazz = obj.getClass();

        // Check for toString() if not disabled
        if (!config.isDisableStringer() && hasCustomToString(clazz)) {
            try {
                String stringValue = obj.toString();
                return analyzeString(stringValue);
            } catch (Exception e) {
                // Fall through to normal analysis
            }
        }

        // Primitives and wrappers
        if (TypeNameUtil.isPrimitiveOrWrapper(clazz)) {
            return analyzeNumber(obj, clazz);
        }

        // String
        if (obj instanceof String) {
            return analyzeString((String) obj);
        }

        // Boolean
        if (obj instanceof Boolean) {
            return analyzeBoolean((Boolean) obj);
        }

        // Enum
        if (clazz.isEnum()) {
            return analyzeEnum(obj);
        }

        // Mark as visited for circular detection
        visitedObjects.add(objectId);

        try {
            // Array
            if (clazz.isArray()) {
                return analyzeArray(obj, depth);
            }

            // Collection
            if (obj instanceof Collection) {
                return analyzeCollection((Collection<?>) obj, depth);
            }

            // Map
            if (obj instanceof Map) {
                return analyzeMap((Map<?, ?>) obj, depth);
            }

            // Regular object
            return analyzeObject(obj, depth);

        } finally {
            visitedObjects.remove(objectId);
        }
    }

    private DumpNode analyzeString(String str) {
        // Apply max string length
        String displayValue = str;
        if (str.length() > config.getMaxStringLen()) {
            displayValue = StringUtil.truncate(str, config.getMaxStringLen());
        }

        return DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .clazz(String.class)
                .value(displayValue)
                .typeName("String")
                .build();
    }

    private DumpNode analyzeNumber(Object obj, Class<?> clazz) {
        return DumpNode.builder()
                .type(DumpNode.NodeType.NUMBER)
                .clazz(clazz)
                .value(obj)
                .typeName(clazz.getSimpleName())
                .build();
    }

    private DumpNode analyzeBoolean(Boolean bool) {
        return DumpNode.builder()
                .type(DumpNode.NodeType.BOOLEAN)
                .clazz(Boolean.class)
                .value(bool)
                .typeName("boolean")
                .build();
    }

    private DumpNode analyzeEnum(Object obj) {
        return DumpNode.builder()
                .type(DumpNode.NodeType.ENUM)
                .clazz(obj.getClass())
                .value(obj.toString())
                .typeName(obj.getClass().getSimpleName())
                .build();
    }

    private DumpNode analyzeArray(Object array, int depth) {
        int length = java.lang.reflect.Array.getLength(array);
        Map<String, DumpNode> children = new LinkedHashMap<>();

        int limit = Math.min(length, config.getMaxItems());
        for (int i = 0; i < limit; i++) {
            Object element = java.lang.reflect.Array.get(array, i);
            children.put(String.valueOf(i), analyze(element, depth + 1));
        }

        return DumpNode.builder()
                .type(DumpNode.NodeType.ARRAY)
                .clazz(array.getClass())
                .typeName(array.getClass().getComponentType().getSimpleName() + "[]")
                .children(children)
                .build();
    }

    private DumpNode analyzeCollection(Collection<?> collection, int depth) {
        Map<String, DumpNode> children = new LinkedHashMap<>();
        int index = 0;
        int limit = Math.min(collection.size(), config.getMaxItems());

        for (Object element : collection) {
            if (index >= limit) break;
            children.put(String.valueOf(index), analyze(element, depth + 1));
            index++;
        }

        return DumpNode.builder()
                .type(DumpNode.NodeType.COLLECTION)
                .clazz(collection.getClass())
                .typeName(TypeNameUtil.getSimpleName(collection.getClass()))
                .children(children)
                .build();
    }

    private DumpNode analyzeMap(Map<?, ?> map, int depth) {
        Map<String, DumpNode> children = new LinkedHashMap<>();
        int index = 0;
        int limit = Math.min(map.size(), config.getMaxItems());

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (index >= limit) break;
            String key = String.valueOf(entry.getKey());
            children.put("\"" + key + "\"", analyze(entry.getValue(), depth + 1));
            index++;
        }

        return DumpNode.builder()
                .type(DumpNode.NodeType.MAP)
                .clazz(map.getClass())
                .typeName(TypeNameUtil.getSimpleName(map.getClass()))
                .children(children)
                .build();
    }

    private DumpNode analyzeObject(Object obj, int depth) {
        Map<String, DumpNode> children = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();

        List<Field> fields = fieldFilter.getAccessibleFields(clazz);

        for (Field field : fields) {
            String fieldName = field.getName();

            // Check if field should be included
            if (!config.shouldIncludeField(fieldName)) {
                continue;
            }

            try {
                field.setAccessible(true);
                Object value = field.get(obj);

                // Check if field should be redacted
                if (config.shouldRedactField(fieldName)) {
                    children.put(fieldName, createRedactedNode(field.getType()));
                } else {
                    children.put(fieldName, analyze(value, depth + 1));
                }
            } catch (Exception e) {
                // Skip inaccessible fields
            }
        }

        return DumpNode.builder()
                .type(DumpNode.NodeType.OBJECT)
                .clazz(clazz)
                .typeName(TypeNameUtil.getSimpleName(clazz))
                .children(children)
                .build();
    }

    /**
     * Creates a redacted placeholder node.
     */
    private DumpNode createRedactedNode(Class<?> fieldType) {
        return DumpNode.builder()
                .type(DumpNode.NodeType.STRING)
                .clazz(fieldType)
                .value("<redacted>")
                .typeName(fieldType.getSimpleName())
                .build();
    }

    /**
     * Checks if a class has a custom toString() method.
     */
    private boolean hasCustomToString(Class<?> clazz) {
        try {
            // Check if toString() is overridden (not from Object class)
            return !clazz.getMethod("toString").getDeclaringClass().equals(Object.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Resets the analyzer state, clearing visited objects.
     * This allows re-analyzing the same objects without circular reference detection.
     */
    public void reset() {
        visitedObjects.clear();
    }

    /**
     * Gets the type classifier.
     */
    public TypeClassifier getTypeClassifier() {
        return typeClassifier;
    }

    /**
     * Gets the field filter.
     */
    public FieldFilter getFieldFilter() {
        return fieldFilter;
    }
}