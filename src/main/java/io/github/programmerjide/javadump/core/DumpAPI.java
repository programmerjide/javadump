package io.github.programmerjide.javadump.core;

/**
 * Complete JavaDump API Reference.
 *
 * <h2>Basic Usage</h2>
 * <pre>{@code
 * import static io.github.programmerjide.javadump.core.Dump.*;
 *
 * // Console output
 * dump(user);
 * dd(user);  // dump and die
 * String str = dumpStr(user);
 *
 * // JSON output
 * dumpJSON(user);
 * String json = dumpJSONStr(user);
 *
 * // HTML output
 * String html = dumpHTML(user);
 *
 * // Diff
 * diff(before, after);
 * String diffStr = diffStr(before, after);
 * String diffHtml = diffHTML(before, after);
 * }</pre>
 *
 * <h2>Advanced Formats</h2>
 * <pre>{@code
 * import static io.github.programmerjide.javadump.core.DumpExtended.*;
 *
 * String markdown = dumpMarkdown(user);
 * String yaml = dumpYAML(user);
 * String interactive = dumpInteractive(user);
 * }</pre>
 *
 * <h2>Builder Configuration</h2>
 * <pre>{@code
 * Dumper dumper = Dumper.builder()
 *     // Size limits
 *     .withMaxDepth(15)
 *     .withMaxItems(100)
 *     .withMaxStringLen(1000)
 *
 *     // Field control
 *     .withOnlyFields("id", "name")
 *     .withExcludeFields("password")
 *     .withRedactFields("ssn", "creditCard")
 *     .withRedactSensitive()
 *
 *     // Display options
 *     .withoutColor()
 *     .withoutHeader()
 *     .withDisableStringer()
 *
 *     // Output
 *     .withWriter(customStream)
 *     .build();
 * }</pre>
 *
 * <h2>Annotations</h2>
 * <pre>{@code
 * public class User {
 *     @DumpOrder(1)
 *     private Long id;
 *
 *     @DumpLabel("Full Name")
 *     private String name;
 *
 *     @DumpIgnore
 *     private String internalId;
 *
 *     @DumpRedact
 *     private String password;
 *
 *     @DumpFormat(DateFormatter.class)
 *     private LocalDateTime created;
 *
 *     @DumpMethod("accountAge")
 *     public String getAge() {
 *         return "5 years";
 *     }
 * }
 * }</pre>
 *
 * <h2>Stream Integration</h2>
 * <pre>{@code
 * import static io.github.programmerjide.javadump.stream.StreamDump.*;
 *
 * users.stream()
 *     .peek(dumpPeek("Before filter"))
 *     .filter(User::isActive)
 *     .peek(dumpPeekJSON("After filter"))
 *     .peek(dumpIf(u -> u.getAge() > 30))
 *     .peek(dumpWithIndex())
 *     .collect(Collectors.toList());
 * }</pre>
 *
 * <h2>Exception Handling</h2>
 * <pre>{@code
 * try {
 *     processOrder(order);
 * } catch (Exception e) {
 *     ExceptionDumper.dump(e, order, cart);
 *     String html = ExceptionDumper.dumpHTML(e, order);
 * }
 * }</pre>
 *
 * <h2>Test Integration</h2>
 * <pre>{@code
 * // JUnit 5
 * @ExtendWith(DumpOnFailure.class)
 * class MyTest {
 *     @Test
 *     void test() {
 *         DumpAssertions.assertDumpEquals(expected, actual);
 *         DumpAssertions.assertChanged(before, after, "name");
 *         SnapshotTesting.assertMatchesSnapshot("user", user);
 *     }
 * }
 * }</pre>
 *
 * @author Olaldejo Olajide
 * @since 1.6.0
 */
public final class DumpAPI {
    // Documentation class only
    private DumpAPI() {
    }
}
