Aqui está o arquivo completo em Markdown. É só clicar em Copy e colar dentro do arquivo examples/withprivatefields/README.md.

Markdown
# Hiding Private Fields Example

This example demonstrates how to control the visibility of `private` fields in your dumps using the `showPrivateFields()` configuration.

##  Overview

By default, **JavaDump** is designed for deep debugging, so it **shows private fields** (`showPrivateFields = true`) to give you a complete view of the object's internal state. However, sometimes you may want to hide internal implementation details and focus only on accessible fields (like public or package-private).

This example uses a `Classroom` class to demonstrate how to filter out private members.

##  How to Run

Run the `main` method in `PrivateFieldsExamples.java`.

##  Comparison

### 1. Default Behavior (Private Fields Visible)

By default, the dumper reveals everything, including private properties. This is ideal when you need to see the full state of an object.

**Code:**
```java
// Default configuration (showPrivateFields is true by default)
Dump.dump(classroom);
```

**Output:**

```java
#Classroom {
  teatcher → "Asdrubal"
  discipline → "History"
  classTime → LocalTime 15:30
  age → Integer 0
}
```
(Note: Private fields teatcher and classTime are visible)

### 2. With showPrivateFields(false)

You can configure the dumper to ignore private fields. In this mode, only non-private fields (public, protected, or package-private) will be shown.

**Code:**

```java
// 1. Create configuration to hide private fields
DumperConfig config = DumperConfig.builder()
    .showPrivateFields(false)
    .build();

// 2. Use custom dumper
new Dumper(config).dump(classroom);
```

**Output:**

```java
#Classroom {
  discipline → string(7) "History"
  classTime → string(5) "15:30"
  age → Integer 0
}
```
(Note: Only the non-private field discipline is shown. The private implementation details are hidden)