package io.github.programmerjide.javadump.benchmark;

import io.github.programmerjide.javadump.core.Dump;

import java.util.concurrent.TimeUnit;

/**
 * Benchmarking utilities for JavaDump.
 *
 * @author Olaldejo Olajide
 * @since 1.6.0
 */
public class Benchmark {

    /**
     * Benchmarks dump operations.
     */
    public static Benchmark.BenchmarkResult benchmark(Object obj, int iterations) {
        // Warmup
        for (int i = 0; i < 100; i++) {
            Dump.dumpStr(obj);
        }

        // Actual benchmark
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

        for (int i = 0; i < iterations; i++) {
            Dump.dumpStr(obj);
        }

        long endTime = System.nanoTime();
        long endMemory = getUsedMemory();

        long totalTime = endTime - startTime;
        long avgTime = totalTime / iterations;
        long memoryUsed = endMemory - startMemory;

        return new Benchmark.BenchmarkResult(
                iterations,
                totalTime,
                avgTime,
                memoryUsed
        );
    }

    /**
     * Compares performance with and without caching.
     */
    public static Benchmark.ComparisonResult compareWithCaching(Object obj, int iterations) {
        // Without cache
        io.github.programmerjide.javadump.cache.ReflectionCache.setEnabled(false);
        Benchmark.BenchmarkResult withoutCache = benchmark(obj, iterations);

        // With cache
        io.github.programmerjide.javadump.cache.ReflectionCache.setEnabled(true);
        io.github.programmerjide.javadump.cache.ReflectionCache.clear();
        Benchmark.BenchmarkResult withCache = benchmark(obj, iterations);

        return new Benchmark.ComparisonResult(withoutCache, withCache);
    }

    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Benchmark results.
     */
    public static class BenchmarkResult {
        private final int iterations;
        private final long totalTimeNanos;
        private final long avgTimeNanos;
        private final long memoryBytes;

        BenchmarkResult(int iterations, long totalTimeNanos,
                        long avgTimeNanos, long memoryBytes) {
            this.iterations = iterations;
            this.totalTimeNanos = totalTimeNanos;
            this.avgTimeNanos = avgTimeNanos;
            this.memoryBytes = memoryBytes;
        }

        public int getIterations() {
            return iterations;
        }

        public long getTotalTimeMillis() {
            return TimeUnit.NANOSECONDS.toMillis(totalTimeNanos);
        }

        public long getAvgTimeMicros() {
            return TimeUnit.NANOSECONDS.toMicros(avgTimeNanos);
        }

        public long getMemoryKB() {
            return memoryBytes / 1024;
        }

        @Override
        public String toString() {
            return String.format(
                    "Iterations: %d\n" +
                            "Total time: %dms\n" +
                            "Avg time: %dµs\n" +
                            "Memory: %dKB",
                    iterations,
                    getTotalTimeMillis(),
                    getAvgTimeMicros(),
                    getMemoryKB()
            );
        }
    }

    /**
     * Comparison results.
     */
    public static class ComparisonResult {
        private final Benchmark.BenchmarkResult withoutCache;
        private final Benchmark.BenchmarkResult withCache;

        ComparisonResult(Benchmark.BenchmarkResult withoutCache, Benchmark.BenchmarkResult withCache) {
            this.withoutCache = withoutCache;
            this.withCache = withCache;
        }

        public Benchmark.BenchmarkResult getWithoutCache() {
            return withoutCache;
        }

        public Benchmark.BenchmarkResult getWithCache() {
            return withCache;
        }

        public double getSpeedup() {
            return (double) withoutCache.avgTimeNanos / withCache.avgTimeNanos;
        }

        @Override
        public String toString() {
            return String.format(
                    "Without Cache:\n%s\n\n" +
                            "With Cache:\n%s\n\n" +
                            "Speedup: %.2fx",
                    withoutCache,
                    withCache,
                    getSpeedup()
            );
        }
    }
}
