# Showing Static Fields Example

This example demonstrates how to control the visibility of `static` fields in your dumps using the `showStaticFields()` configuration.

## 📋 Overview

By default, **JavaDump** focuses on the state of an object instance and ignores static fields to reduce noise. However, there are scenarios—such as inspecting configuration classes, singletons, or global constants—where static fields are the most important part of the data.

This example shows how to enable static field dumping using the `DumperConfig` builder.

## 🚀 How to Run

Run the `main` method in `StaticFieldsExample.java` to see the comparison in your console.

## 🔍 Comparison

### 1. Default Behavior (Hidden)

When using the default configuration, static fields are ignored. If a class contains static fields, they will not appear in the output.

**Code:**
```java
// Default configuration (showStaticFields is false by default)
Dump.dump(appConfig);
```


**Output:**

```java
#AppConfig {
  environment → "Production"
}
```

(Note: appName and userCount are static, so they are hidden)

### 2. With showStaticFields(true)

To see static fields, you must create a custom DumperConfig and enable the option.

**Code:**

```Java
// 1. Create the configuration
DumperConfig config = DumperConfig.builder()
    .showStaticFields(true)
    .build();

// 2. Use a Dumper instance with this config
new Dumper(config).dump(appConfig);
```
**Output:**

```Java
Plaintext
#AppConfig {
  appName → "JavaDump Demo"
  userCount → Integer 500
  environment → "Production"
}