package io.github.programmerjide.javadump.examples.dump;

import io.github.programmerjide.javadump.core.Dump;import static java.lang.System.*;

/**
 * Basic Dump Example
 * This example demonstrates the fundamental Dump.dump() functionality
 * with various primitive types, strings, arrays, and simple objects.
 */
public class BasicDumpExample {

    // Simple data class for demonstration
    static class Person {
        private String name;
        private int age;
        private String email;

        public Person(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
    }

    static void main(String[] args) {
        printHeader("JavaDump - Basic Usage Examples");

        // Example 1: Primitive Types
        section("1. Primitive Types");

        out.println("→ Integer:");
        Dump.dump(42);

        out.println("\n→ Double:");
        Dump.dump(3.14159);

        out.println("\n→ Boolean (true):");
        Dump.dump(true);

        out.println("\n→ Boolean (false):");
        Dump.dump(false);

        out.println("\n→ Character:");
        Dump.dump('A');

        out.println("\n→ Long:");
        Dump.dump(9876543210L);

        out.println("\n→ Float:");
        Dump.dump(2.5f);

        // Example 2: Strings
        section("2. Strings");

        out.println("→ Simple string:");
        Dump.dump("Hello, JavaDump!");

        out.println("\n→ Empty string:");
        Dump.dump("");

        out.println("\n→ Multiline string:");
        Dump.dump("Line 1\nLine 2\nLine 3");

        out.println("\n→ String with tabs:");
        Dump.dump("Column1\tColumn2\tColumn3");

        // Example 3: Null Values
        section("3. Null Values");

        out.println("→ Null:");
        Dump.dump(null);

        String nullString = null;
        out.println("\n→ Null string variable:");
        Dump.dump(nullString);

        // Example 4: Arrays
        section("4. Arrays");

        out.println("→ Integer array:");
        int[] numbers = {1, 2, 3, 4, 5};
        Dump.dump(numbers);

        out.println("\n→ String array:");
        String[] fruits = {"Apple", "Banana", "Cherry"};
        Dump.dump(fruits);

        out.println("\n→ Double array:");
        double[] prices = {9.99, 19.99, 29.99};
        Dump.dump(prices);

        out.println("\n→ Boolean array:");
        boolean[] flags = {true, false, true, true};
        Dump.dump(flags);

        out.println("\n→ Empty array:");
        int[] emptyArray = {};
        Dump.dump(emptyArray);

        // Example 5: Custom Objects
        section("5. Custom Objects");

        out.println("→ Simple Person object:");
        Person person = new Person("John Doe", 30, "john@example.com");
        Dump.dump(person);

        out.println("\n→ Person with null email:");
        Person personWithNullEmail = new Person("Jane Smith", 25, null);
        Dump.dump(personWithNullEmail);

        // Example 6: Multiple Values
        section("6. Multiple Values in One Call");

        String name = "Alice";
        int age = 28;
        boolean active = true;

        out.println("→ Dump multiple values:");
        Dump.dump("Name:", name, "Age:", age, "Active:", active);

        // Example 7: Mixed Types
        section("7. Mixed Types");

        out.println("→ Various types together:");
        Dump.dump(
                "String value",
                123,
                45.67,
                true,
                new int[]{1, 2, 3},
                new Person("Bob", 35, "bob@example.com")
        );

        printFooter();
    }

    // Helper methods for formatting
    private static void printHeader(String title) {
        String line = "=".repeat(title.length() + 4);
        out.println("\n" + line);
        out.println("  " + title);
        out.println(line + "\n");
    }

    private static void section(String title) {
        out.println("\n" + "─".repeat(60));
        out.println("📍 " + title);
        out.println("─".repeat(60) + "\n");
    }

    private static void printFooter() {
        out.println("\n" + "=".repeat(60));
        out.println("  ✅ Basic dump examples completed!");
        out.println("=".repeat(60) + "\n");
    }
}