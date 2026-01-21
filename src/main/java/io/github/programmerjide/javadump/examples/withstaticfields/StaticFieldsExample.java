package io.github.programmerjide.javadump.examples.withstaticfields;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.core.Dump;
import io.github.programmerjide.javadump.core.Dumper;

import static java.lang.System.*;

public class StaticFieldsExample {
  static class AppConfig{
    static String appName = "JavaDump";
    static int userCount = 999999999;
    String environment = "Production";


  }

  public static void main(String[] args) {
    section("1. Default Behavior (Static Hidden)");
    AppConfig appConfig = new AppConfig();
    Dump.dump(appConfig);

    section("2. With Static Fields Visible");
    DumperConfig dumperConfig = DumperConfig.builder()
        .showStaticFields(true)
        .build();
     new Dumper(dumperConfig).dump(appConfig);
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
