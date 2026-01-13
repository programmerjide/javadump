package io.github.programmerjide.javadump.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Snapshot testing for objects.
 *
 * <p>Example:
 * <pre>{@code
 * @Test
 * void testUserSerialization() {
 *     User user = createUser();
 *     SnapshotTesting.assertMatchesSnapshot("user-snapshot", user);
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.4.0
 */
public class SnapshotTesting {

    private static Path snapshotDir = Paths.get("src/test/resources/snapshots");
    private static boolean updateSnapshots =
            Boolean.getBoolean("javadump.updateSnapshots");

    /**
     * Sets the snapshot directory.
     */
    public static void setSnapshotDirectory(Path dir) {
        snapshotDir = dir;
    }

    /**
     * Enables snapshot updating mode.
     */
    public static void setUpdateMode(boolean update) {
        updateSnapshots = update;
    }

    /**
     * Asserts that object matches stored snapshot.
     */
    public static void assertMatchesSnapshot(String snapshotName, Object actual) {
        try {
            Files.createDirectories(snapshotDir);

            Path snapshotFile = snapshotDir.resolve(snapshotName + ".snapshot");
            String actualDump = Dump.dumpStr(actual);

            if (updateSnapshots || !Files.exists(snapshotFile)) {
                // Update or create snapshot
                Files.writeString(snapshotFile, actualDump);

                if (updateSnapshots) {
                    System.out.println("Updated snapshot: " + snapshotName);
                }
            } else {
                // Compare with existing snapshot
                String expectedDump = Files.readString(snapshotFile);

                if (!actualDump.equals(expectedDump)) {
                    throw new AssertionError(
                            "Snapshot mismatch for: " + snapshotName + "\n" +
                                    "Expected:\n" + expectedDump + "\n" +
                                    "Actual:\n" + actualDump + "\n" +
                                    "\nRun with -Djavadump.updateSnapshots=true to update");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to handle snapshot: " + snapshotName, e);
        }
    }

    /**
     * Asserts snapshot match with JSON format.
     */
    public static void assertMatchesJSONSnapshot(String snapshotName, Object actual) {
        try {
            Files.createDirectories(snapshotDir);

            Path snapshotFile = snapshotDir.resolve(snapshotName + ".json");
            String actualJSON = Dump.dumpJSONStr(actual);

            if (updateSnapshots || !Files.exists(snapshotFile)) {
                Files.writeString(snapshotFile, actualJSON);

                if (updateSnapshots) {
                    System.out.println("Updated JSON snapshot: " + snapshotName);
                }
            } else {
                String expectedJSON = Files.readString(snapshotFile);

                if (!actualJSON.equals(expectedJSON)) {
                    throw new AssertionError(
                            "JSON snapshot mismatch for: " + snapshotName + "\n" +
                                    "Run with -Djavadump.updateSnapshots=true to update");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to handle JSON snapshot: " + snapshotName, e);
        }
    }
}
