#!/bin/bash

echo "Creating JavaDump example issues..."
echo ""

REPO="programmerjide/javadump"

# Issue #1: Basic Dump Example
gh issue create \
  --title "feat: Add basic dump example" \
  --label "enhancement,good first issue,examples,documentation,priority: high" \
  --body "## 📝 Description
Create a basic example demonstrating the fundamental \`Dump.dump()\` functionality with various primitive types and simple objects.

## 📂 Location
Create folder: \`examples/dump/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Dumping primitive types (int, double, boolean, char, long, float)
- [ ] Dumping strings (simple, empty, multiline)
- [ ] Dumping null values
- [ ] Dumping simple arrays
- [ ] Dumping basic custom objects
- [ ] Multiple value dumping in a single call

## 📄 Files to Create
- \`examples/dump/BasicDumpExample.java\`
- \`examples/dump/README.md\` (explaining what this example demonstrates)

## 💡 Example Structure
\`\`\`java
package io.github.programmerjide.javadump.examples.dump;

import io.github.programmerjide.javadump.core.Dump;

public class BasicDumpExample {
    public static void main(String[] args) {
        // Example 1: Primitives
        Dump.dump(42);
        Dump.dump(3.14);
        // ... more examples
    }
}
\`\`\`

## 📚 Reference
See godump's basic example: https://github.com/goforj/godump/tree/main/examples/basic

## ✅ Acceptance Criteria
- [ ] Example runs without errors
- [ ] Code is well-commented
- [ ] README.md explains the example
- [ ] Output is clear and demonstrates the feature
- [ ] Follows project code style"

echo "✅ Issue #1 created: Basic dump example"

# Issue #2: DumpStr Example
gh issue create \
  --title "feat: Add dumpStr example" \
  --label "enhancement,good first issue,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating \`Dump.dumpStr()\` which returns the dump output as a string instead of printing to stdout.

## 📂 Location
Create folder: \`examples/dumpstr/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Using \`dumpStr()\` to capture output as a string
- [ ] Storing dump output in variables
- [ ] Using dump strings in logging
- [ ] Using dump strings in test assertions
- [ ] Comparing dump outputs
- [ ] Building custom output formats

## 📄 Files to Create
- \`examples/dumpstr/DumpStrExample.java\`
- \`examples/dumpstr/README.md\`

## 💡 Example Structure
\`\`\`java
package io.github.programmerjide.javadump.examples.dumpstr;

import io.github.programmerjide.javadump.core.Dump;

public class DumpStrExample {
    public static void main(String[] args) {
        Map<String, Integer> data = Map.of(\"a\", 1, \"b\", 2);
        String output = Dump.dumpStr(data);

        System.out.println(\"Captured output:\");
        System.out.println(output);

        // Use in logging, assertions, etc.
    }
}
\`\`\`

## 📚 Reference
See godump's dumpstr example: https://github.com/goforj/godump/tree/main/examples/dumpstr

## ✅ Acceptance Criteria
- [ ] Example runs without errors
- [ ] Shows practical use cases for string output
- [ ] README.md explains when to use dumpStr vs dump
- [ ] Well-commented code"

echo "✅ Issue #2 created: DumpStr example"

# Issue #3: Dump & Die Example
gh issue create \
  --title "feat: Add dd (dump and die) example" \
  --label "enhancement,good first issue,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating the \`Dump.dd()\` functionality which dumps values and then exits the program (similar to Laravel's dd()).

## 📂 Location
Create folder: \`examples/dd/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Basic usage of \`dd()\`
- [ ] Difference between \`dump()\` and \`dd()\`
- [ ] When to use \`dd()\` vs \`dump()\`
- [ ] Common debugging scenarios where \`dd()\` is useful
- [ ] Multiple values with \`dd()\`

## 📄 Files to Create
- \`examples/dd/DumpAndDieExample.java\`
- \`examples/dd/README.md\`

## 💡 Example Structure
\`\`\`java
package io.github.programmerjide.javadump.examples.dd;

import io.github.programmerjide.javadump.core.Dump;

public class DumpAndDieExample {
    public static void main(String[] args) {
        System.out.println(\"Before dd()\");

        Map<String, Object> debugData = new HashMap<>();
        debugData.put(\"status\", \"processing\");
        debugData.put(\"step\", 3);

        // This will dump and exit
        Dump.dd(\"Debug point reached:\", debugData);

        // This line will never execute
        System.out.println(\"After dd() - you won't see this\");
    }
}
\`\`\`

## 📚 Reference
See godump's dd example: https://github.com/goforj/godump/tree/main/examples/dd

## ✅ Acceptance Criteria
- [ ] Example clearly shows program termination
- [ ] README explains the use case for dd()
- [ ] Includes warning about using in production code
- [ ] Shows before/after behavior"

echo "✅ Issue #3 created: Dump & Die example"

# Issue #4: Builder Pattern Example
gh issue create \
  --title "feat: Add builder pattern configuration example" \
  --label "enhancement,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating the builder pattern for configuring custom Dumper instances with various options.

## 📂 Location
Create folder: \`examples/builder/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Creating custom dumper with \`Dumper.builder()\`
- [ ] Using \`withMaxDepth()\`
- [ ] Using \`withMaxItems()\`
- [ ] Using \`withMaxStringLen()\`
- [ ] Combining multiple options
- [ ] Reusing configured dumpers
- [ ] Different configurations for different scenarios

## 📄 Files to Create
- \`examples/builder/BuilderExample.java\`
- \`examples/builder/README.md\`

## 💡 Example Structure
\`\`\`java
package io.github.programmerjide.javadump.examples.builder;

import io.github.programmerjide.javadump.core.Dumper;

public class BuilderExample {
    public static void main(String[] args) {
        // Create a custom dumper for large datasets
        Dumper compactDumper = Dumper.builder()
            .withMaxDepth(3)
            .withMaxItems(10)
            .withMaxStringLen(50)
            .build();

        // Use the custom dumper
        List<String> largeList = // ...
        compactDumper.dump(largeList);
    }
}
\`\`\`

## 📚 Reference
See godump's builder example: https://github.com/goforj/godump/tree/main/examples/builder

## ✅ Acceptance Criteria
- [ ] Shows multiple builder configurations
- [ ] Explains each configuration option
- [ ] Demonstrates practical scenarios
- [ ] README includes configuration reference table"

echo "✅ Issue #4 created: Builder pattern example"

# Issue #5: OnlyFields Example
gh issue create \
  --title "feat: Add field filtering with onlyFields example" \
  --label "enhancement,examples,documentation,help wanted" \
  --body "## 📝 Description
Create an example demonstrating how to show only specific fields from objects using \`onlyFields()\` configuration.

## 📂 Location
Create folder: \`examples/withonlyfields/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Basic usage of \`onlyFields()\`
- [ ] Filtering fields from custom objects
- [ ] Multiple field selection
- [ ] Use cases (API responses, sensitive data hiding)
- [ ] Nested object field filtering behavior
- [ ] Combining with other dumper options

## 📄 Files to Create
- \`examples/withonlyfields/OnlyFieldsExample.java\`
- \`examples/withonlyfields/README.md\`

## 💡 Example Structure
\`\`\`java
package io.github.programmerjide.javadump.examples.withonlyfields;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.core.Dumper;

public class OnlyFieldsExample {
    static class User {
        private String id;
        private String username;
        private String email;
        private String password;
        private String internalToken;
    }

    public static void main(String[] args) {
        User user = new User(/*...*/);

        // Show only specific fields
        DumperConfig config = DumperConfig.builder()
            .onlyFields(\"id\", \"username\", \"email\")
            .build();

        new Dumper(config).dump(user);
    }
}
\`\`\`

## 📚 Reference
See godump's withonlyfields example: https://github.com/goforj/godump/tree/main/examples/withonlyfields

## ✅ Acceptance Criteria
- [ ] Clear demonstration of field filtering
- [ ] Shows before/after comparison
- [ ] README explains practical use cases
- [ ] Includes API response filtering example"

echo "✅ Issue #5 created: OnlyFields example"

# Issue #6: ExcludeFields Example
gh issue create \
  --title "feat: Add field exclusion with excludeFields example" \
  --label "enhancement,examples,documentation,help wanted" \
  --body "## 📝 Description
Create an example demonstrating how to exclude specific fields from dump output using \`excludeFields()\` configuration.

## 📂 Location
Create folder: \`examples/withexcludefields/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Basic usage of \`excludeFields()\`
- [ ] Excluding sensitive fields
- [ ] Excluding internal/debug fields
- [ ] Multiple field exclusion
- [ ] Difference between \`onlyFields()\` and \`excludeFields()\`
- [ ] Real-world scenarios

## 📄 Files to Create
- \`examples/withexcludefields/ExcludeFieldsExample.java\`
- \`examples/withexcludefields/README.md\`

## ✅ Acceptance Criteria
- [ ] Clear before/after examples
- [ ] README explains when to use exclude vs only
- [ ] Shows practical use cases"

echo "✅ Issue #6 created: ExcludeFields example"

# Issue #7: RedactFields Example
gh issue create \
  --title "feat: Add field redaction example" \
  --label "enhancement,examples,documentation,security" \
  --body "## 📝 Description
Create an example demonstrating field redaction for masking sensitive data like passwords, API keys, and tokens.

## 📂 Location
Create folder: \`examples/withredactfields/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Basic redaction with \`redactFields()\`
- [ ] Redacting passwords
- [ ] Redacting API keys and tokens
- [ ] Multiple field redaction
- [ ] Redaction with field match modes (EXACT, CONTAINS, etc.)
- [ ] Auto-redaction with \`redactSensitive(true)\`
- [ ] Security best practices

## 📄 Files to Create
- \`examples/withredactfields/RedactFieldsExample.java\`
- \`examples/withredactfields/README.md\`

## 💡 Key Features to Show
\`\`\`java
DumperConfig config = DumperConfig.builder()
    .redactFields(\"password\", \"apiKey\", \"secretToken\")
    .build();

// Output shows: -password → <redacted>
\`\`\`

## ✅ Acceptance Criteria
- [ ] Shows various sensitive data types
- [ ] Explains security implications
- [ ] README includes best practices
- [ ] Shows \`<redacted>\` placeholder clearly"

echo "✅ Issue #7 created: RedactFields example"

# Issue #8: RedactSensitive Example
gh issue create \
  --title "feat: Add auto-redact sensitive fields example" \
  --label "enhancement,examples,documentation,security" \
  --body "## 📝 Description
Create an example demonstrating automatic redaction of common sensitive field names using \`redactSensitive(true)\`.

## 📂 Location
Create folder: \`examples/withredactsensitive/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Using \`redactSensitive(true)\`
- [ ] What fields are auto-detected (password, token, secret, apiKey, etc.)
- [ ] Comparison with manual redaction
- [ ] Combining auto-redact with manual redact
- [ ] Use in API credential objects
- [ ] Configuration objects with secrets

## 📄 Files to Create
- \`examples/withredactsensitive/RedactSensitiveExample.java\`
- \`examples/withredactsensitive/README.md\`

## ✅ Acceptance Criteria
- [ ] Lists all auto-detected sensitive field patterns
- [ ] Shows real-world credential objects
- [ ] README explains when to use auto vs manual"

echo "✅ Issue #8 created: RedactSensitive example"

# Issue #9: Field Match Mode Example
gh issue create \
  --title "feat: Add field match mode example" \
  --label "enhancement,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating different field matching modes (EXACT, CONTAINS, STARTS_WITH, ENDS_WITH, IGNORE_CASE).

## 📂 Location
Create folder: \`examples/withfieldmatchmode/\`

## 📋 Requirements

The example should demonstrate:
- [ ] All field match modes
- [ ] \`EXACT\` - exact name match
- [ ] \`CONTAINS\` - field name contains pattern
- [ ] \`STARTS_WITH\` - field name starts with pattern
- [ ] \`ENDS_WITH\` - field name ends with pattern
- [ ] \`IGNORE_CASE\` - case-insensitive matching
- [ ] Practical use cases for each mode
- [ ] Combining with \`redactFields()\` and \`excludeFields()\`

## 📄 Files to Create
- \`examples/withfieldmatchmode/FieldMatchModeExample.java\`
- \`examples/withfieldmatchmode/README.md\`

## ✅ Acceptance Criteria
- [ ] Each match mode clearly demonstrated
- [ ] README includes match mode reference table
- [ ] Shows pattern matching examples"

echo "✅ Issue #9 created: Field match mode example"

# Issue #10: Max Depth Example
gh issue create \
  --title "feat: Add max depth control example" \
  --label "enhancement,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating depth limiting for nested objects using \`withMaxDepth()\`.

## 📂 Location
Create folder: \`examples/withmaxdepth/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Basic \`withMaxDepth()\` usage
- [ ] Deeply nested objects (5+ levels)
- [ ] Different depth limits (1, 2, 3, 5)
- [ ] Performance benefits with large nested structures
- [ ] When to use depth limiting
- [ ] Visual comparison of different depths

## 📄 Files to Create
- \`examples/withmaxdepth/MaxDepthExample.java\`
- \`examples/withmaxdepth/README.md\`

## ✅ Acceptance Criteria
- [ ] Shows clear truncation at max depth
- [ ] Demonstrates performance aspect
- [ ] README explains practical use cases"

echo "✅ Issue #10 created: Max depth example"

# Issue #11: Max Items Example
gh issue create \
  --title "feat: Add max items control example" \
  --label "enhancement,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating collection truncation using \`withMaxItems()\` for large arrays, lists, and maps.

## 📂 Location
Create folder: \`examples/withmaxitems/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Basic \`withMaxItems()\` usage
- [ ] Large lists (100+ items)
- [ ] Large maps
- [ ] Large arrays
- [ ] Different item limits (5, 10, 50)
- [ ] Truncation indicators
- [ ] Performance with very large collections

## 📄 Files to Create
- \`examples/withmaxitems/MaxItemsExample.java\`
- \`examples/withmaxitems/README.md\`

## ✅ Acceptance Criteria
- [ ] Shows \`... (truncated N items)\` message
- [ ] Demonstrates with 500+ item collections
- [ ] README explains memory/performance benefits"

echo "✅ Issue #11 created: Max items example"

# Issue #12: Max String Length Example
gh issue create \
  --title "feat: Add max string length example" \
  --label "enhancement,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating string truncation using \`withMaxStringLen()\`.

## 📂 Location
Create folder: \`examples/withmaxstringlen/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Basic \`withMaxStringLen()\` usage
- [ ] Very long strings (1000+ chars)
- [ ] Different length limits
- [ ] Truncation indicator (\`…\`)
- [ ] Use cases (log messages, large text content)
- [ ] JSON strings, HTML content

## 📄 Files to Create
- \`examples/withmaxstringlen/MaxStringLenExample.java\`
- \`examples/withmaxstringlen/README.md\`

## ✅ Acceptance Criteria
- [ ] Shows truncation with ellipsis
- [ ] Demonstrates practical scenarios
- [ ] README explains when to truncate strings"

echo "✅ Issue #12 created: Max string length example"

# Issue #13: Static Fields Example
gh issue create \
  --title "feat: Add static fields example" \
  --label "enhancement,good first issue,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating how to show/hide static fields using \`showStaticFields()\`.

## 📂 Location
Create folder: \`examples/withstaticfields/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Default behavior (static fields hidden)
- [ ] Showing static fields with \`showStaticFields(true)\`
- [ ] Class with multiple static fields
- [ ] Static vs instance field comparison
- [ ] Practical use cases

## 📄 Files to Create
- \`examples/withstaticfields/StaticFieldsExample.java\`
- \`examples/withstaticfields/README.md\`

## ✅ Acceptance Criteria
- [ ] Clear before/after comparison
- [ ] Shows static field indicators
- [ ] README explains when to show static fields"

echo "✅ Issue #13 created: Static fields example"

# Issue #14: Private Fields Example
gh issue create \
  --title "feat: Add private fields visibility example" \
  --label "enhancement,good first issue,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating field visibility markers and controlling private field display.

## 📂 Location
Create folder: \`examples/withprivatefields/\`

## 📋 Requirements

The example should demonstrate:
- [ ] All visibility types: public (+), private (-), protected (#), package (~)
- [ ] Default behavior (shows private fields)
- [ ] Hiding private fields with \`showPrivateFields(false)\`
- [ ] Reflection access to private fields
- [ ] Visibility markers in output

## 📄 Files to Create
- \`examples/withprivatefields/PrivateFieldsExample.java\`
- \`examples/withprivatefields/README.md\`

## ✅ Acceptance Criteria
- [ ] Shows all four visibility types
- [ ] README explains visibility markers
- [ ] Demonstrates toggle behavior"

echo "✅ Issue #14 created: Private fields example"

# Issue #15: Kitchen Sink Example
gh issue create \
  --title "feat: Add comprehensive kitchen sink example" \
  --label "enhancement,examples,documentation,showcase,priority: high" \
  --body "## 📝 Description
Create a comprehensive \"kitchen sink\" example that demonstrates ALL JavaDump features in one place. This is the showcase example that demonstrates everything the library can do.

## 📂 Location
Create folder: \`examples/kitchensink/\`

## 📋 Requirements

The example should demonstrate:
- [ ] All 18+ features from the comprehensive test
- [ ] Basic types and primitives
- [ ] Collections and maps
- [ ] Custom objects
- [ ] Circular references
- [ ] All configuration options
- [ ] Field filtering, exclusion, and redaction
- [ ] Depth and item limiting
- [ ] Real-world complex scenarios
- [ ] Beautiful formatting and output
- [ ] Clear section headers

## 📄 Files to Create
- \`examples/kitchensink/KitchenSinkExample.java\`
- \`examples/kitchensink/README.md\`

## 📚 Reference
godump kitchen sink: https://github.com/goforj/godump/tree/main/examples/kitchensink

## ✅ Acceptance Criteria
- [ ] Runs all feature demonstrations
- [ ] Professional banner and formatting
- [ ] Clear section dividers
- [ ] Comprehensive README
- [ ] Shows colorized terminal output
- [ ] Can be used as main demo for the project"

echo "✅ Issue #15 created: Kitchen sink example"

# Issue #16: Extended Example
gh issue create \
  --title "feat: Add extended usage example" \
  --label "enhancement,examples,documentation,showcase" \
  --body "## 📝 Description
Create an example showing extended features and advanced usage patterns.

## 📂 Location
Create folder: \`examples/extended/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Custom dumper instances
- [ ] Reusing configured dumpers
- [ ] Dumper as a utility class
- [ ] Integration with logging frameworks
- [ ] Usage in different contexts (tests, debugging, development)

## 📄 Files to Create
- \`examples/extended/ExtendedExample.java\`
- \`examples/extended/README.md\`

## ✅ Acceptance Criteria
- [ ] Shows advanced patterns
- [ ] Demonstrates real-world integration
- [ ] README includes best practices"

echo "✅ Issue #16 created: Extended example"

# Issue #17: Fdump Example
gh issue create \
  --title "feat: Add fdump (write to io.Writer) example" \
  --label "enhancement,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating writing dump output to custom writers (files, StringWriter, etc.).

## 📂 Location
Create folder: \`examples/fdump/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Writing to StringWriter
- [ ] Writing to files
- [ ] Writing to custom output streams
- [ ] Using \`withWriter()\` in builder
- [ ] Buffered vs unbuffered output
- [ ] Use cases (logging to files, capturing output)

## 📄 Files to Create
- \`examples/fdump/FdumpExample.java\`
- \`examples/fdump/README.md\`

## 📚 Reference
See godump's fdump example: https://github.com/goforj/godump/tree/main/examples/fdump

## ✅ Acceptance Criteria
- [ ] Shows writing to different output targets
- [ ] README explains use cases
- [ ] Demonstrates file output clearly"

echo "✅ Issue #17 created: Fdump example"

# Issue #18: New Dumper Example
gh issue create \
  --title "feat: Add newDumper custom instance example" \
  --label "enhancement,examples,documentation" \
  --body "## 📝 Description
Create an example demonstrating creating and reusing custom Dumper instances with specific configurations.

## 📂 Location
Create folder: \`examples/newdumper/\`

## 📋 Requirements

The example should demonstrate:
- [ ] Creating multiple dumper instances
- [ ] Different configurations for different purposes
- [ ] Reusing dumpers across application
- [ ] Dumper as a singleton pattern
- [ ] Dumper factory pattern

## 📄 Files to Create
- \`examples/newdumper/NewDumperExample.java\`
- \`examples/newdumper/README.md\`

## ✅ Acceptance Criteria
- [ ] Shows multiple dumper configurations
- [ ] Demonstrates reusability
- [ ] README explains when to use custom instances"

echo "✅ Issue #18 created: New dumper example"

# Meta Issue: Track All Examples
gh issue create \
  --title "[META] Implement all JavaDump examples" \
  --label "examples,documentation,tracking" \
  --body "## 📝 Overview
This meta-issue tracks the implementation of all JavaDump example folders, inspired by godump's example structure.

## 📊 Progress Tracker

### Core Features
- [ ] Basic dump example
- [ ] DumpStr example
- [ ] Dump & Die (dd) example
- [ ] Builder pattern example
- [ ] Fdump (write to writer) example
- [ ] New dumper instance example

### Field Filtering & Redaction
- [ ] OnlyFields example
- [ ] ExcludeFields example
- [ ] RedactFields example
- [ ] RedactSensitive example
- [ ] Field match mode example

### Configuration Options
- [ ] Max depth example
- [ ] Max items example
- [ ] Max string length example
- [ ] Static fields example
- [ ] Private fields example

### Showcase
- [ ] Kitchen sink example
- [ ] Extended usage example

## 📁 Expected Structure
\`\`\`
examples/
├── basic/
├── builder/
├── dd/
├── dumpstr/
├── fdump/
├── kitchensink/
├── newdumper/
├── withexcludefields/
├── withfieldmatchmode/
├── withmaxdepth/
├── withmaxitems/
├── withmaxstringlen/
├── withonlyfields/
├── withprivatefields/
├── withredactfields/
├── withredactsensitive/
├── withstaticfields/
└── extended/
\`\`\`

## 🎯 Goals
- Provide clear, runnable examples for every feature
- Match godump's comprehensive example coverage
- Help users understand how to use JavaDump effectively
- Improve documentation through code examples

## 🤝 Contributing
Each example should include:
- Working Java code
- README.md explaining the feature
- Clear comments
- Expected output samples"

echo "✅ Meta issue created: Track all examples"

echo ""
echo "🎉 All 19 issues created successfully!"
echo ""
echo "View your issues at: https://github.com/$REPO/issues"