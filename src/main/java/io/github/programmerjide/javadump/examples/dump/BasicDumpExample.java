package io.github.programmerjide.javadump.examples.dump;

import io.github.programmerjide.javadump.core.Dump;import static java.lang.System.*;

/**
 * Basic Dump Example
 *
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

        IO.println("→ Integer:");
        Dump.dump(42);

        IO.println("\n→ Double:");
        Dump.dump(3.14159);

        IO.println("\n→ Boolean (true):");
        Dump.dump(true);

        IO.println("\n→ Boolean (false):");
        Dump.dump(false);

        IO.println("\n→ Character:");
        Dump.dump('A');

        IO.println("\n→ Long:");
        Dump.dump(9876543210L);

        IO.println("\n→ Float:");
        Dump.dump(2.5f);

        // Example 2: Strings
        section("2. Strings");

        IO.println("→ Simple string:");
        Dump.dump("Hello, JavaDump!");

        IO.println("\n→ Empty string:");
        Dump.dump("");

        IO.println("\n→ Multiline string:");
        Dump.dump("Line 1\nLine 2\nLine 3");

        IO.println("\n→ String with tabs:");
        Dump.dump("Column1\tColumn2\tColumn3");

        // Example 3: Null Values
        section("3. Null Values");

        IO.println("→ Null:");
        Dump.dump(null);

        String nullString = null;
        IO.println("\n→ Null string variable:");
        Dump.dump(nullString);

        // Example 4: Arrays
        section("4. Arrays");

        IO.println("→ Integer array:");
        int[] numbers = {1, 2, 3, 4, 5};
        Dump.dump(numbers);

        IO.println("\n→ String array:");
        String[] fruits = {"Apple", "Banana", "Cherry"};
        Dump.dump(fruits);

        IO.println("\n→ Double array:");
        double[] prices = {9.99, 19.99, 29.99};
        Dump.dump(prices);

        IO.println("\n→ Boolean array:");
        boolean[] flags = {true, false, true, true};
        Dump.dump(flags);

        IO.println("\n→ Empty array:");
        int[] emptyArray = {};
        Dump.dump(emptyArray);

        // Example 5: Custom Objects
        section("5. Custom Objects");

        IO.println("→ Simple Person object:");
        Person person = new Person("John Doe", 30, "john@example.com");
        Dump.dump(person);

        IO.println("\n→ Person with null email:");
        Person personWithNullEmail = new Person("Jane Smith", 25, null);
        Dump.dump(personWithNullEmail);

        // Example 6: Multiple Values
        section("6. Multiple Values in One Call");

        String name = "Alice";
        int age = 28;
        boolean active = true;

        IO.println("→ Dump multiple values:");
        Dump.dump("Name:", name, "Age:", age, "Active:", active);

        // Example 7: Mixed Types
        section("7. Mixed Types");

        IO.println("→ Various types together:");
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
        IO.println("\n" + line);
        IO.println("  " + title);
        IO.println(line + "\n");
    }

    private static void section(String title) {
        IO.println("\n" + "─".repeat(60));
        IO.println("📍 " + title);
        IO.println("─".repeat(60) + "\n");
    }

    private static void printFooter() {
        IO.println("\n" + "=".repeat(60));
        IO.println("  ✅ Basic dump examples completed!");
        IO.println("=".repeat(60) + "\n");
    }
}