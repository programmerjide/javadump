package io.github.programmerjide.javadump.model;

import java.util.*;

/**
 * Represents a node in the dump tree structure.
 *
 * <p>This class holds the analyzed representation of any object,
 * including its type, value, children, and metadata about the analysis.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class DumpNode {

    /**
     * The type of node in the dump tree.
     */
    public enum NodeType {
        NULL,
        STRING,
        NUMBER,
        BOOLEAN,
        ARRAY,
        COLLECTION,
        MAP,
        OBJECT,
        ENUM,
        PRIMITIVE,
        CYCLIC,
        TRUNCATED,
        ERROR
    }

    private final NodeType type;
    private final Class<?> clazz;
    private final Object value;
    private final String typeName;
    private final Map<String, DumpNode> children;
    private final List<DumpNode> elements;
    private final Map<DumpNode, DumpNode> entries;
    private final Map<String, DumpNode> fields;
    private final boolean circular;
    private final boolean maxDepthReached;
    private final boolean truncated;
    private final int displaySize;

    private DumpNode(Builder builder) {
        this.type = builder.type;
        this.clazz = builder.clazz;
        this.value = builder.value;
        this.typeName = builder.typeName;
        this.children = builder.children != null ?
                new LinkedHashMap<>(builder.children) : new LinkedHashMap<>();
        this.elements = builder.elements != null ?
                new ArrayList<>(builder.elements) : new ArrayList<>();
        this.entries = builder.entries != null ?
                new LinkedHashMap<>(builder.entries) : new LinkedHashMap<>();
        this.fields = builder.fields != null ?
                new LinkedHashMap<>(builder.fields) : new LinkedHashMap<>();
        this.circular = builder.circular;
        this.maxDepthReached = builder.maxDepthReached;
        this.truncated = builder.truncated;
        this.displaySize = builder.displaySize;
    }

    /**
     * Creates a new builder for constructing DumpNode instances.
     */
    public static Builder builder() {
        return new Builder();
    }

    // ==================== Getters ====================

    public NodeType getType() {
        return type;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getValue() {
        return value;
    }

    public String getTypeName() {
        return typeName;
    }

    public Map<String, DumpNode> getChildren() {
        return Collections.unmodifiableMap(children);
    }

    public List<DumpNode> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public Map<DumpNode, DumpNode> getEntries() {
        return Collections.unmodifiableMap(entries);
    }

    public Map<String, DumpNode> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    public boolean isCircular() {
        return circular;
    }

    public boolean isMaxDepthReached() {
        return maxDepthReached;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public boolean isEmpty() {
        return children.isEmpty() &&
                elements.isEmpty() &&
                entries.isEmpty() &&
                fields.isEmpty();
    }

    // ==================== Static Factory Methods ====================

    /**
     * Creates a NULL node.
     */
    public static DumpNode ofNull() {
        return builder()
                .type(NodeType.NULL)
                .typeName("null")
                .build();
    }

    /**
     * Creates a PRIMITIVE node.
     */
    public static DumpNode ofPrimitive(Object value, Class<?> clazz) {
        return builder()
                .type(NodeType.PRIMITIVE)
                .value(value)
                .clazz(clazz)
                .typeName(clazz.getSimpleName())
                .build();
    }

    /**
     * Creates a STRING node.
     */
    public static DumpNode ofString(String value) {
        return builder()
                .type(NodeType.STRING)
                .value(value)
                .clazz(String.class)
                .typeName("String")
                .build();
    }

    /**
     * Creates a BOOLEAN node.
     */
    public static DumpNode ofBoolean(boolean value) {
        return builder()
                .type(NodeType.BOOLEAN)
                .value(value)
                .clazz(Boolean.class)
                .typeName("boolean")
                .build();
    }

    /**
     * Creates an ENUM node.
     */
    public static DumpNode ofEnum(Enum<?> value) {
        return builder()
                .type(NodeType.ENUM)
                .value(value.name())
                .clazz(value.getClass())
                .typeName(value.getClass().getSimpleName())
                .build();
    }

    /**
     * Creates an ARRAY node.
     */
    public static DumpNode ofArray(Class<?> clazz, List<DumpNode> elements, int size, boolean truncated) {
        return builder()
                .type(NodeType.ARRAY)
                .clazz(clazz)
                .typeName(clazz.getComponentType().getSimpleName() + "[]")
                .elements(elements)
                .displaySize(size)
                .truncated(truncated)
                .build();
    }

    /**
     * Creates a COLLECTION node.
     */
    public static DumpNode ofCollection(Class<?> clazz, List<DumpNode> elements, int size, boolean truncated) {
        return builder()
                .type(NodeType.COLLECTION)
                .clazz(clazz)
                .typeName(clazz.getSimpleName())
                .elements(elements)
                .displaySize(size)
                .truncated(truncated)
                .build();
    }

    /**
     * Creates a MAP node.
     */
    public static DumpNode ofMap(Class<?> clazz, Map<DumpNode, DumpNode> entries, int size, boolean truncated) {
        return builder()
                .type(NodeType.MAP)
                .clazz(clazz)
                .typeName(clazz.getSimpleName())
                .entries(entries)
                .displaySize(size)
                .truncated(truncated)
                .build();
    }

    /**
     * Creates an OBJECT node.
     */
    public static DumpNode ofObject(Class<?> clazz, Map<String, DumpNode> fields) {
        return builder()
                .type(NodeType.OBJECT)
                .clazz(clazz)
                .typeName(clazz.getSimpleName())
                .fields(fields)
                .build();
    }

    /**
     * Creates a CYCLIC reference node.
     */
    public static DumpNode cyclic(Class<?> clazz) {
        return builder()
                .type(NodeType.CYCLIC)
                .clazz(clazz)
                .typeName(clazz.getSimpleName())
                .circular(true)
                .build();
    }

    /**
     * Creates a TRUNCATED node.
     */
    public static DumpNode truncated() {
        return builder()
                .type(NodeType.TRUNCATED)
                .typeName("(truncated)")
                .truncated(true)
                .build();
    }

    /**
     * Creates an ERROR node.
     */
    public static DumpNode ofError(String message) {
        return builder()
                .type(NodeType.ERROR)
                .value(message)
                .typeName("error")
                .build();
    }

    // ==================== Builder ====================

    public static class Builder {
        private NodeType type = NodeType.NULL;
        private Class<?> clazz;
        private Object value;
        private String typeName = "null";
        private Map<String, DumpNode> children;
        private List<DumpNode> elements;
        private Map<DumpNode, DumpNode> entries;
        private Map<String, DumpNode> fields;
        private boolean circular;
        private boolean maxDepthReached;
        private boolean truncated;
        private int displaySize;

        public Builder type(NodeType type) {
            this.type = type;
            return this;
        }

        public Builder clazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public Builder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        public Builder children(Map<String, DumpNode> children) {
            this.children = children;
            return this;
        }

        public Builder elements(List<DumpNode> elements) {
            this.elements = elements;
            return this;
        }

        public Builder entries(Map<DumpNode, DumpNode> entries) {
            this.entries = entries;
            return this;
        }

        public Builder fields(Map<String, DumpNode> fields) {
            this.fields = fields;
            return this;
        }

        public Builder circular(boolean circular) {
            this.circular = circular;
            return this;
        }

        public Builder maxDepthReached(boolean maxDepthReached) {
            this.maxDepthReached = maxDepthReached;
            return this;
        }

        public Builder truncated(boolean truncated) {
            this.truncated = truncated;
            return this;
        }

        public Builder displaySize(int displaySize) {
            this.displaySize = displaySize;
            return this;
        }

        public DumpNode build() {
            return new DumpNode(this);
        }
    }

    @Override
    public String toString() {
        return String.format("DumpNode{type=%s, typeName='%s', value=%s}",
                type, typeName, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DumpNode dumpNode = (DumpNode) o;
        return circular == dumpNode.circular &&
                maxDepthReached == dumpNode.maxDepthReached &&
                type == dumpNode.type &&
                Objects.equals(clazz, dumpNode.clazz) &&
                Objects.equals(value, dumpNode.value) &&
                Objects.equals(typeName, dumpNode.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, clazz, value, typeName, circular, maxDepthReached);
    }
}