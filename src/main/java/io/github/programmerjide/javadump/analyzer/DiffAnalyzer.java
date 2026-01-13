package io.github.programmerjide.javadump.analyzer;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;

import java.util.*;

/**
 * Analyzes differences between two objects.
 *
 * <p>Compares two DumpNode trees and identifies added, removed,
 * and changed nodes for visualization in diff output.
 *
 * @author Olaldejo Olajide
 * @since 1.0.0
 */
public class DiffAnalyzer {

    private final DumperConfig config;
    private final ObjectAnalyzer analyzer;

    public DiffAnalyzer(DumperConfig config) {
        this.config = config;
        this.analyzer = new ObjectAnalyzer(config);
    }

    /**
     * Analyzes differences between two objects.
     *
     * @param before the original object
     * @param after the modified object
     * @return a DiffResult containing the comparison
     */
    public DiffResult analyze(Object before, Object after) {
        DumpNode beforeNode = analyzer.analyze(before, 0);
        DumpNode afterNode = analyzer.analyze(after, 0);

        return compareTrees(beforeNode, afterNode);
    }

    /**
     * Compares two DumpNode trees.
     */
    private DiffResult compareTrees(DumpNode before, DumpNode after) {
        DiffResult result = new DiffResult();

        // Both null
        if (before == null && after == null) {
            return result;
        }

        // One is null
        if (before == null) {
            result.addAddition(after);
            return result;
        }

        if (after == null) {
            result.addRemoval(before);
            return result;
        }

        // Different types
        if (before.getType() != after.getType()) {
            result.addRemoval(before);
            result.addAddition(after);
            return result;
        }

        // Simple types - compare values
        if (isSimpleType(before)) {
            if (!Objects.equals(before.getValue(), after.getValue())) {
                result.addRemoval(before);
                result.addAddition(after);
            } else {
                result.addUnchanged(before);
            }
            return result;
        }

        // Complex types - compare children
        return compareComplexNodes(before, after, result);
    }

    /**
     * Compares complex nodes (objects, collections, maps).
     */
    private DiffResult compareComplexNodes(DumpNode before, DumpNode after,
                                           DiffResult result) {
        Map<String, DumpNode> beforeChildren = before.getChildren();
        Map<String, DumpNode> afterChildren = after.getChildren();

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(beforeChildren.keySet());
        allKeys.addAll(afterChildren.keySet());

        for (String key : allKeys) {
            DumpNode beforeChild = beforeChildren.get(key);
            DumpNode afterChild = afterChildren.get(key);

            if (beforeChild == null) {
                // Added
                result.addFieldAddition(key, afterChild);
            } else if (afterChild == null) {
                // Removed
                result.addFieldRemoval(key, beforeChild);
            } else {
                // Compare
                DiffResult childResult = compareTrees(beforeChild, afterChild);
                if (childResult.hasChanges()) {
                    result.addFieldChange(key, beforeChild, afterChild);
                } else {
                    result.addFieldUnchanged(key, beforeChild);
                }
            }
        }

        return result;
    }

    /**
     * Checks if a node represents a simple type.
     */
    private boolean isSimpleType(DumpNode node) {
        switch (node.getType()) {
            case NULL:
            case STRING:
            case NUMBER:
            case BOOLEAN:
            case ENUM:
                return true;
            default:
                return false;
        }
    }

    /**
     * Result of a diff analysis.
     */
    public static class DiffResult {
        private final List<DiffEntry> additions = new ArrayList<>();
        private final List<DiffEntry> removals = new ArrayList<>();
        private final List<DiffEntry> changes = new ArrayList<>();
        private final List<DiffEntry> unchanged = new ArrayList<>();

        public void addAddition(DumpNode node) {
            additions.add(new DiffEntry(null, null, node));
        }

        public void addRemoval(DumpNode node) {
            removals.add(new DiffEntry(null, node, null));
        }

        public void addUnchanged(DumpNode node) {
            unchanged.add(new DiffEntry(null, node, node));
        }

        public void addFieldAddition(String field, DumpNode node) {
            additions.add(new DiffEntry(field, null, node));
        }

        public void addFieldRemoval(String field, DumpNode node) {
            removals.add(new DiffEntry(field, node, null));
        }

        public void addFieldChange(String field, DumpNode before, DumpNode after) {
            changes.add(new DiffEntry(field, before, after));
        }

        public void addFieldUnchanged(String field, DumpNode node) {
            unchanged.add(new DiffEntry(field, node, node));
        }

        public boolean hasChanges() {
            return !additions.isEmpty() || !removals.isEmpty() || !changes.isEmpty();
        }

        public List<DiffEntry> getAdditions() {
            return Collections.unmodifiableList(additions);
        }

        public List<DiffEntry> getRemovals() {
            return Collections.unmodifiableList(removals);
        }

        public List<DiffEntry> getChanges() {
            return Collections.unmodifiableList(changes);
        }

        public List<DiffEntry> getUnchanged() {
            return Collections.unmodifiableList(unchanged);
        }
    }

    /**
     * A single difference entry.
     */
    public static class DiffEntry {
        private final String field;
        private final DumpNode before;
        private final DumpNode after;

        public DiffEntry(String field, DumpNode before, DumpNode after) {
            this.field = field;
            this.before = before;
            this.after = after;
        }

        public String getField() {
            return field;
        }

        public DumpNode getBefore() {
            return before;
        }

        public DumpNode getAfter() {
            return after;
        }

        public boolean isAddition() {
            return before == null && after != null;
        }

        public boolean isRemoval() {
            return before != null && after == null;
        }

        public boolean isChange() {
            return before != null && after != null &&
                    !Objects.equals(before, after);
        }
    }
}