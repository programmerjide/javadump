package io.github.programmerjide.javadump.test;

import io.github.programmerjide.javadump.core.Dump;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

/**
 * JUnit 5 extension for automatic dump on test failure.
 *
 * <p>Usage:
 * <pre>{@code
 * @ExtendWith(DumpOnFailure.class)
 * class MyTest {
 *     @Test
 *     void testSomething() {
 *         User user = createUser();
 *         // If test fails, user will be dumped automatically
 *     }
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.4.0
 */
public class DumpOnFailure implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.err.println("\n╔════════════════════════════════════════════");
        System.err.println("║ TEST FAILED: " + context.getDisplayName());
        System.err.println("╠════════════════════════════════════════════");

        // Dump exception
        System.err.println("Exception:");
        cause.printStackTrace(System.err);

        // Dump test instance if available
        context.getTestInstance().ifPresent(instance -> {
            System.err.println("\n║ Test Instance:");
            Dump.dump(instance);
        });

        System.err.println("╚════════════════════════════════════════════\n");
    }
}
