# Basic Dump Example

This example demonstrates the fundamental `Dump.dump()` functionality with various data types.

## 📝 What This Example Covers

- **Primitive types**: int, double, boolean, char, long, float
- **Strings**: simple, empty, multiline, with special characters
- **Null values**: handling null objects and variables
- **Arrays**: primitive arrays, string arrays, empty arrays
- **Custom objects**: simple POJOs with private fields
- **Multiple values**: dumping several values in one call
- **Mixed types**: combining different types in a single dump

## 🚀 How to Run

### Using Maven:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="io.github.programmerjide.javadump.examples.dump.BasicDumpExample"
```

### Using your IDE:
1. Open `BasicDumpExample.java`
2. Right-click and select "Run"

### Using compiled JAR:
```bash
java -cp target/javadump-1.0.0.jar io.github.programmerjide.javadump.examples.dump.BasicDumpExample
```

## 📸 Expected Output

You should see colorized output showing:
```
=============================================================
  JavaDump - Basic Usage Examples
=============================================================

────────────────────────────────────────────────────────────
📍 1. Primitive Types
────────────────────────────────────────────────────────────

→ Integer:
<#dump // BasicDumpExample.java:42
42 #int

→ Double:
<#dump // BasicDumpExample.java:45
3.14159 #double

→ Boolean (true):
<#dump // BasicDumpExample.java:48
true #boolean

...
```

## 🎓 What You'll Learn

1. **Basic usage**: The simplest way to dump any value
2. **Type information**: How JavaDump shows type annotations
3. **Null handling**: How null values are displayed
4. **Array dumping**: How arrays are formatted and indexed
5. **Object introspection**: How private fields are accessed via reflection
6. **Multiple values**: How to dump several values at once

## 💡 Key Takeaways

- `Dump.dump()` works with **any** Java type
- **No setup required** - just import and use
- **Private fields** are accessible via reflection
- **Type information** is shown with `#TypeName` notation
- **Multiple values** can be dumped in a single call

## 🔗 Related Examples

- [DumpStr Example](../dumpstr/) - Capturing output as a string
- [Builder Example](../builder/) - Customizing dump behavior
- [Kitchen Sink Example](../kitchensink/) - All features in one place

## 📚 Documentation

For more information, see the [JavaDump README](../../README.md).