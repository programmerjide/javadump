# JavaDump

[![Maven Central](https://img.shields.io/maven-central/v/io.github.programmerjide/javadump.svg)](https://central.sonatype.com/artifact/io.github.programmerjide/javadump)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://github.com/programmerjide/javadump/workflows/Build/badge.svg)](https://github.com/programmerjide/javadump/actions)
[![Java Version](https://img.shields.io/badge/Java-25%2B-blue)](https://openjdk.org/)

> A developer-friendly, zero-dependency debug dumper for Java with pretty printing, colorized output, and Laravel-style `dd()` debugging.

## ✨ Features

- ✅ **Zero Dependencies** - Uses only JDK standard library
- 🎨 **Colorized Terminal Output** - ANSI colors for better readability
- 🌐 **HTML Output** - Perfect for web-based debugging
- 🔍 **Diff Support** - Visual comparison between two objects
- 💀 **Dump & Die** - Laravel-style `dd()` for quick debugging
- 🏗️ **Builder API** - Fluent configuration interface

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>io.github.programmerjide</groupId>
    <artifactId>javadump</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'io.github.programmerjide:javadump:1.0.0'
```

## 🚀 Quick Start

```java
import io.github.programmerjide.javadump.Dump;

public class Example {
    public static void main(String[] args) {
        User user = new User("Alice", "alice@example.com");
        Dump.dump(user);
    }
}
```

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by [godump](https://github.com/goforj/godump) for Go
- Inspired by Laravel's `dump()` and `dd()` functions

---

**Made with ❤️ by Olaldejo Olajide**
