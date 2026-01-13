package io.github.programmerjide.javadump.analyzer;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Memory-efficient circular reference detection.
 *
 * <p>Uses identity hash codes instead of storing full object references.
 *
 * @author Olaldejo Olajide
 * @since 1.2.0
 */
public class MemoryEfficientCircularDetector {

    private final Map<Integer, Integer> visitCounts = new IdentityHashMap<>();
    private final int maxVisits;

    public MemoryEfficientCircularDetector() {
        this(1);
    }

    public MemoryEfficientCircularDetector(int maxVisits) {
        this.maxVisits = maxVisits;
    }

    /**
     * Marks an object as visited.
     */
    public void visit(Object obj) {
        if (obj == null) return;

        int hash = System.identityHashCode(obj);
        visitCounts.merge(hash, 1, Integer::sum);
    }

    /**
     * Checks if object has been visited too many times (circular).
     */
    public boolean isCircular(Object obj) {
        if (obj == null) return false;

        int hash = System.identityHashCode(obj);
        return visitCounts.getOrDefault(hash, 0) > maxVisits;
    }

    /**
     * Removes an object from tracking.
     */
    public void unvisit(Object obj) {
        if (obj == null) return;

        int hash = System.identityHashCode(obj);
        Integer count = visitCounts.get(hash);

        if (count != null) {
            if (count <= 1) {
                visitCounts.remove(hash);
            } else {
                visitCounts.put(hash, count - 1);
            }
        }
    }

    /**
     * Clears all tracked objects.
     */
    public void clear() {
        visitCounts.clear();
    }

    /**
     * Gets number of tracked objects.
     */
    public int size() {
        return visitCounts.size();
    }
}
