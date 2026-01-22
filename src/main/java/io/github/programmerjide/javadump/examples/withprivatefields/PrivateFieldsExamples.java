package io.github.programmerjide.javadump.examples.withprivatefields;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.core.Dump;
import io.github.programmerjide.javadump.core.Dumper;

import java.time.LocalTime;

import static java.lang.System.out;

public class PrivateFieldsExamples {

  private static class Classroom{
    private String teatcher = "Asdrubal";
    String discipline = "History";
    protected LocalTime classTime = LocalTime.of(15, 30, 0);
    int age;
  }

  public static void main(String[] args) {
    section("1. Default Behavior (Shows private fields)");
    Classroom classroom = new Classroom();
    Dump.dump(classroom);

    section("2. With Privates Fields Non Visible");
    DumperConfig dumperConfig = DumperConfig.builder()
        .showPrivateFields(false)
        .build();
    new Dumper(dumperConfig).dump(classroom);
  }
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
