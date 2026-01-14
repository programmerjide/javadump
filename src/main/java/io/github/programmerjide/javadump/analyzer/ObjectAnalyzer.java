package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;
import io.github.programmerjide.javadump.util.TypeNameUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ObjectAnalyzer {

    private final DumperConfig config;
    private final IdentityHashMap<Object, Boolean> visited = new IdentityHashMap<>();

    public ObjectAnalyzer(DumperConfig config) {
        this.config = config;
    }

    public DumpNode analyze(Object obj) {
        return analyze(obj, 0);
    }

    public DumpNode analyze(Object obj, int depth) {

        // ===== NULL =====
        if (obj == null) {
            return DumpNode.ofNull();
        }

        // ===== MAX DEPTH =====
        if (depth >= config.getMaxDepth()) {
            return DumpNode.builder()
                    .type(DumpNode.NodeType.TRUNCATED)
                    .maxDepthReached(true)
                    .truncated(true)
                    .build();
        }

        // ===== CYCLE =====
        if (visited.containsKey(obj)) {
            return DumpNode.cyclic(obj.getClass());
        }

        Class<?> clazz = obj.getClass();

        // ===== PRIMITIVE & WRAPPER =====
        if (TypeNameUtil.isPrimitiveOrWrapper(clazz)) {
            return DumpNode.ofPrimitive(obj, clazz);
        }

        // ===== STRING =====
        if (TypeNameUtil.isString(clazz)) {
            String value = (String) obj;
            if (value.length() > config.getMaxStringLen()) {
                value = value.substring(0, config.getMaxStringLen());
            }
            return DumpNode.ofString(value);
        }

        // ===== ENUM =====
        if (TypeNameUtil.isEnum(clazz)) {
            return DumpNode.ofEnum((Enum<?>) obj);
        }

        // ===== SAFE toString SHORTCUT =====
        if (!config.isDisableStringer()
                && !TypeNameUtil.isArrayType(clazz)
                && !TypeNameUtil.isCollectionType(clazz)
                && !TypeNameUtil.isMapType(clazz)
                && hasCustomToString(clazz)) {
            try {
                return DumpNode.ofString(obj.toString());
            } catch (Exception ignored) {
            }
        }

        visited.put(obj, Boolean.TRUE);

        // ===== ARRAY =====
        if (TypeNameUtil.isArrayType(clazz)) {
            return analyzeArray(obj, depth);
        }

        // ===== COLLECTION =====
        if (TypeNameUtil.isCollectionType(clazz)) {
            return analyzeCollection((Collection<?>) obj, depth);
        }

        // ===== MAP =====
        if (TypeNameUtil.isMapType(clazz)) {
            return analyzeMap((Map<?, ?>) obj, depth);
        }

        // ===== OBJECT =====
        return analyzeObject(obj, depth);
    }

    // ------------------------------------------------------------------------

    private DumpNode analyzeArray(Object array, int depth) {
        int length = Array.getLength(array);
        int max = Math.min(length, config.getMaxItems());

        List<DumpNode> elements = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            elements.add(analyze(Array.get(array, i), depth + 1));
        }

        return DumpNode.ofArray(
                array.getClass(),
                elements,
                length,
                length > config.getMaxItems()
        );
    }

    private DumpNode analyzeCollection(Collection<?> collection, int depth) {
        int size = collection.size();
        int max = Math.min(size, config.getMaxItems());

        List<DumpNode> elements = new ArrayList<>(max);
        int count = 0;
        for (Object item : collection) {
            if (count++ >= max) break;
            elements.add(analyze(item, depth + 1));
        }

        return DumpNode.ofCollection(
                collection.getClass(),
                elements,
                size,
                size > config.getMaxItems()
        );
    }

    private DumpNode analyzeMap(Map<?, ?> map, int depth) {
        int size = map.size();
        int max = Math.min(size, config.getMaxItems());

        Map<DumpNode, DumpNode> entries = new LinkedHashMap<>();
        int count = 0;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (count++ >= max) break;
            DumpNode key = analyze(entry.getKey(), depth + 1);
            DumpNode value = analyze(entry.getValue(), depth + 1);
            entries.put(key, value);
        }

        return DumpNode.ofMap(
                map.getClass(),
                entries,
                size,
                size > config.getMaxItems()
        );
    }

    private DumpNode analyzeObject(Object obj, int depth) {
        Map<String, DumpNode> fields = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {

            int modifiers = field.getModifiers();

            if (!config.isShowPrivateFields() && Modifier.isPrivate(modifiers)) {
                continue;
            }
            if (!config.isShowStaticFields() && Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!config.isShowTransientFields() && Modifier.isTransient(modifiers)) {
                continue;
            }

            String fieldName = field.getName();

            if (!config.shouldIncludeField(fieldName)) {
                continue;
            }

            field.setAccessible(true);

            try {
                Object value = field.get(obj);

                if (config.shouldRedactField(fieldName)) {
                    fields.put(fieldName, DumpNode.ofString("***REDACTED***"));
                } else {
                    fields.put(fieldName, analyze(value, depth + 1));
                }

            } catch (IllegalAccessException e) {
                fields.put(fieldName, DumpNode.ofError("access denied"));
            }
        }

        return DumpNode.ofObject(clazz, fields);
    }

    private boolean hasCustomToString(Class<?> clazz) {
        try {
            return !clazz.getMethod("toString")
                    .getDeclaringClass()
                    .equals(Object.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
