# 🚀 JavaDump

<div align="center">

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-4.0.0-C71A36.svg)](https://maven.apache.org/)
[![GitHub release](https://img.shields.io/github/v/release/programmerjide/javadump)](https://github.com/programmerjide/javadump/releases)
[![GitHub issues](https://img.shields.io/github/issues/programmerjide/javadump)](https://github.com/programmerjide/javadump/issues)
[![GitHub stars](https://img.shields.io/github/stars/programmerjide/javadump)](https://github.com/programmerjide/javadump/stargazers)


**A developer-friendly, zero-dependency debug dumper for Java 17+**

Pretty, colorized output of your objects, arrays, collections, and more - with cyclic reference detection, field filtering, and sensitive data redaction.

Inspired by [Symfony's VarDumper](https://symfony.com/doc/current/components/var_dumper.html) (used in Laravel's `dump()` and `dd()`) and Go's [godump](https://github.com/goforj/godump).

[Features](#-features) • [Installation](#-installation) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [Examples](#-examples) • [Comparison](#-comparison-with-alternatives)

</div>

---

## 📸 Terminal Output Example

```java
User user = new User("John Doe", 30, "john@example.com");
user.addRole("admin");
user.addRole("developer");

Dump.dump(user);
```

**Output:**
```
<#dump // Main.java:42
#User {
  -name → "John Doe"
  -age → 30
  -email → "john@example.com"
  -roles → #ArrayList[2] {
    0 → "admin"
    1 → "developer"
  }
}
```

---

## ✨ Features

### Core Features
- ✅ **Zero Dependencies** - No external libraries required
- 🎨 **Colorized Terminal Output** - Beautiful, syntax-highlighted dumps
- 🔄 **Cyclic Reference Detection** - Handles circular references gracefully
- 📍 **Call Site Detection** - Shows file:line where dump was called
- 🔒 **Private Field Access** - Dumps private fields via reflection
- 🎯 **Type Information** - Shows actual types with `#ClassName` notation

### Advanced Features
- 🔍 **Field Filtering** - Show only specific fields or exclude fields
- 🔐 **Field Redaction** - Mask sensitive data (passwords, API keys, tokens)
- 📊 **Collection Truncation** - Limit output with `maxDepth` and `maxItems`
- 📝 **String Truncation** - Prevent overwhelming output from huge strings
- ⚙️ **Builder Pattern API** - Flexible, chainable configuration
- 💀 **Dump & Die** - Laravel-style `dd()` function

### Upcoming Features (Roadmap)
- 🌐 **HTML Output** - Web-friendly dumps with collapsible sections
- 📄 **JSON Output** - Export as JSON for APIs and logging
- 🔀 **Diff Support** - Compare objects and show differences
- 🎨 **Themes** - Multiple color schemes (Dracula, Monokai, Solarized)
- 📊 **Performance Profiling** - Measure execution time and memory

---

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>io.github.programmerjide</groupId>
    <artifactId>javadump</artifactId>
    <version>1.0.2</version>
</dependency>
```

### Gradle

```gradle
implementation 'io.github.programmerjide:javadump:1.0.0'
```

### Manual JAR

Download the latest JAR from [releases](https://github.com/programmerjide/javadump/releases) and add it to your classpath.

---

## 🚀 Quick Start

### Basic Usage

```java
import io.github.programmerjide.javadump.Dump;

// Dump any object
Dump.dump("Hello World");
Dump.dump(42);
Dump.dump(new int[]{1, 2, 3});

// Dump multiple values
String name = "Alice";
int age = 25;
Dump.dump("Name:", name, "Age:", age);
```

### Custom Objects

```java
class User {
    private String name;
    private int age;
    private List<String> roles;
    
    // constructor, getters, setters...
}

User user = new User("Bob", 30, Arrays.asList("admin", "developer"));
Dump.dump(user);
```

**Output:**
```
#User {
  -name → "Bob"
  -age → 30
  -roles → #ArrayList[2] {
    0 → "admin"
    1 → "developer"
  }
}
```

### Dump & Die

```java
// Dump and exit (like Laravel's dd())
Dump.dd(user);
// Program exits after dumping
```

---

## 📚 Documentation

### Configuration with Builder Pattern

```java
import io.github.programmerjide.javadump.Dumper;
import io.github.programmerjide.javadump.config.DumperConfig;

// Create a custom dumper
Dumper dumper = Dumper.builder()
    .withMaxDepth(5)           // Limit nesting depth (default: 15)
    .withMaxItems(50)          // Limit array/collection items (default: 100)
    .withMaxStringLen(200)     // Truncate long strings (default: 100000)
    .build();

dumper.dump(myObject);
```

### Field Filtering

**Show only specific fields:**

```java
DumperConfig config = DumperConfig.builder()
    .onlyFields("id", "name", "email")
    .build();

new Dumper(config).dump(user);
```

**Output:**
```
#User {
  -id → 1
  -name → "John Doe"
  -email → "john@example.com"
}
```

**Exclude specific fields:**

```java
DumperConfig config = DumperConfig.builder()
    .excludeFields("password", "internalId")
    .build();

new Dumper(config).dump(user);
```

### Field Redaction

**Redact specific fields:**

```java
DumperConfig config = DumperConfig.builder()
    .redactFields("password", "apiKey", "secretToken")
    .build();

new Dumper(config).dump(credentials);
```

**Output:**
```
#Credentials {
  -username → "admin"
  -password → <redacted>
  -apiKey → <redacted>
}
```

**Auto-redact sensitive fields:**

```java
DumperConfig config = DumperConfig.builder()
    .redactSensitive(true)  // Auto-detects: password, token, secret, apiKey, etc.
    .build();

new Dumper(config).dump(config);
```

### Field Match Modes

```java
DumperConfig config = DumperConfig.builder()
    .redactFields("key")
    .redactMatchMode(DumperConfig.FieldMatchMode.CONTAINS)  // Matches any field containing "key"
    .build();

// Will redact: apiKey, secretKey, keychain, etc.
```

**Available match modes:**
- `EXACT` - Exact field name match (default)
- `IGNORE_CASE` - Case-insensitive match
- `CONTAINS` - Field name contains the pattern
- `STARTS_WITH` - Field name starts with pattern
- `ENDS_WITH` - Field name ends with pattern

### Static vs Private Fields

```java
// Show static fields (hidden by default)
DumperConfig config = DumperConfig.builder()
    .showStaticFields(true)
    .build();

// Hide private fields (shown by default)
DumperConfig config = DumperConfig.builder()
    .showPrivateFields(false)
    .build();
```

### Handling Circular References

JavaDump automatically detects and handles circular references:

```java
class Node {
    String name;
    Node next;
}

Node node1 = new Node("A");
Node node2 = new Node("B");
node1.next = node2;
node2.next = node1;  // Circular!

Dump.dump(node1);
```

**Output:**
```
#Node {
  -name → "A"
  -next → #Node {
    -name → "B"
    -next → (cyclic)  ← Circular reference detected!
  }
}
```

---

## 💡 Examples

### 1. Debug Collections

```java
List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
Dump.dump(fruits);

Map<String, Integer> scores = new HashMap<>();
scores.put("Alice", 95);
scores.put("Bob", 87);
Dump.dump(scores);
```

### 2. Debug Nested Objects

```java
class Address {
    private String street;
    private String city;
}

class Person {
    private String name;
    private Address address;
}

Person person = new Person("John", new Address("123 Main St", "NYC"));
Dump.dump(person);
```

### 3. Debug Large Collections (with truncation)

```java
List<Integer> largeList = new ArrayList<>();
for (int i = 0; i < 1000; i++) {
    largeList.add(i);
}

Dumper dumper = Dumper.builder()
    .withMaxItems(10)  // Only show first 10 items
    .build();

dumper.dump(largeList);
```

**Output:**
```
#ArrayList[1000] {
  0 → 0
  1 → 1
  ...
  9 → 9
  ... (truncated 990 items)
}
```

### 4. Debug API Responses (with redaction)

```java
class ApiResponse {
    private String status;
    private String accessToken;
    private String refreshToken;
    private User user;
}

DumperConfig config = DumperConfig.builder()
    .redactFields("accessToken", "refreshToken")
    .build();

new Dumper(config).dump(response);
```

### 5. Debug in Tests

```java
@Test
void testUserCreation() {
    User user = userService.createUser("John", "john@example.com");
    
    // Quick visual inspection during development
    Dump.dump(user);
    
    assertEquals("John", user.getName());
}
```

---

## 🔍 Comparison with Alternatives

| Feature | JavaDump | godump (Go) | Apache Commons Lang | Jackson ObjectMapper | Java Spew |
|---------|----------|-------------|---------------------|----------------------|-----------|
| **Zero Dependencies** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Colorized Output** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **HTML Output** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **JSON Output** | ✅ | ✅ | ❌ | ✅ | ❌ |
| **Diff Support** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Call Site Detection** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Circular Reference Detection** | ✅ | ✅ | ⚠️ | ✅ | ⚠️ |
| **Private Field Access** | ✅ | ✅ | ⚠️ | ✅ | ✅ |
| **Field Filtering** | ✅ | ✅ | ❌ | ✅ | ❌ |
| **Field Redaction** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Max Depth Control** | ✅ | ✅ | ❌ | ✅ | ❌ |
| **Max Items Control** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **String Truncation** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Builder API** | ✅ | ✅ | ❌ | ✅ | ❌ |
| **Dump & Die** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Field Match Modes** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Visibility Markers (+/-)** | ✅ | ✅ | ❌ | ❌ | ❌ |
| **Pretty Type Names (#Class)** | ✅ | ✅ | ❌ | ❌ | ❌ |

**Legend:**
- ✅ Fully supported
- 🔜 Coming soon
- ⚠️ Partial support
- ❌ Not supported

### Why JavaDump over alternatives?

**vs Apache Commons Lang's `ToStringBuilder`:**
- ❌ Requires modifying your classes
- ❌ No circular reference detection
- ❌ No colorized output
- ❌ No field filtering/redaction

**vs Jackson `ObjectMapper`:**
- ❌ Heavyweight dependency
- ❌ Requires serialization configuration
- ❌ No terminal colors
- ❌ No call site detection
- ✅ Better for JSON APIs (use Jackson for that!)

**vs Custom `toString()` methods:**
- ❌ Manual implementation for every class
- ❌ No automatic circular reference handling
- ❌ Maintenance burden
- ❌ No runtime configuration

**JavaDump advantages:**
- ✅ Zero setup - works with any object
- ✅ Beautiful, readable output
- ✅ Powerful filtering and redaction
- ✅ Built specifically for debugging

---

## 🎨 Output Format Guide

### Type Notation
```
#ClassName    - Type indicator
-fieldName    - Private field
+fieldName    - Public field
#fieldName    - Protected field
~fieldName    - Package-private field
→             - Maps to (key → value)
(cyclic)      - Circular reference detected
<redacted>    - Redacted sensitive field
... (truncated) - More items not shown
```

### Color Scheme
- **Orange** - Types, numbers (`#Integer`, `42`)
- **Green** - Strings (`"Hello World"`)
- **Cyan** - Field names (`name`, `email`)
- **Gray** - Structural characters (`{`, `}`, `→`)
- **Yellow** - Special markers (`(cyclic)`, `<redacted>`)
- **Red/Green** - Booleans (`true`, `false`)

---

## 🗺️ Roadmap

### ✅ Phase 1: Core Features (Complete - v1.0.0)
- [x] Basic dump functionality
- [x] Colorized console output
- [x] Circular reference detection
- [x] Max depth/items limiting
- [x] Builder pattern API
- [x] Field filtering and redaction
- [x] Call site detection

### 🔜 Phase 2: Parity with godump (v1.1.0 - Next)
- [ ] JSON output (`dumpJSON()`, `dumpJSONStr()`)
- [ ] HTML output (`dumpHTML()`)
- [ ] Diff functionality (`diff()`, `diffStr()`, `diffHTML()`)
- [ ] Custom output writers
- [ ] Header control (`withoutHeader()`)

### 🚀 Phase 3: Java-Specific Advantages (v2.0.0)
- [ ] Annotation support (`@DumpIgnore`, `@DumpRedact`, `@DumpLabel`)
- [ ] Custom type formatters
- [ ] Conditional dumps (`dumpIf()`, `dumpWhen()`)
- [ ] Performance profiling
- [ ] Stream support (`.peek(Dump::dump)`)
- [ ] Exception context dumps
- [ ] Test framework integration

### 🎨 Phase 4: Advanced Features (v2.5.0)
- [ ] Multiple themes (Dracula, Monokai, Solarized)
- [ ] Export formats (Markdown, YAML)
- [ ] Interactive HTML with search/filter
- [ ] Reflection caching for performance
- [ ] Plugin system for custom renderers

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Write tests** for your changes
4. **Run tests** (`mvn test`)
5. **Commit** your changes (`git commit -m 'Add amazing feature'`)
6. **Push** to the branch (`git push origin feature/amazing-feature`)
7. **Open** a Pull Request

### Development Setup

```bash
# Clone the repository
git clone https://github.com/programmerjide/javadump.git
cd javadump

# Build the project
mvn clean install

# Run tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report
```

### Code Style
- Follow standard Java conventions
- Add Javadoc for public APIs
- Write unit tests for new features
- Keep classes focused and maintainable

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Inspired by [Symfony VarDumper](https://symfony.com/doc/current/components/var_dumper.html)
- Inspired by [godump](https://github.com/goforj/godump) for Go
- Thanks to Laravel for popularizing `dd()` debugging
- Built with ❤️ for the Java community

---

## 📞 Support

- 📧 **Email:** programmerolajide@gmail.com
- 🐛 **Issues:** [GitHub Issues](https://github.com/programmerjide/javadump/issues)
- 💬 **Discussions:** [GitHub Discussions](https://github.com/programmerjide/javadump/discussions)
- ⭐ **Star** this repo if you find it useful!

---

<div align="center">

**Made with ❤️ by [Olaldejo Olajide](https://github.com/programmerjide)**

If JavaDump helps you debug faster, consider giving it a ⭐!

</div>
