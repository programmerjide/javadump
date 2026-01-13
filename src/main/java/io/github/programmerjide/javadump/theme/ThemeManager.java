package io.github.programmerjide.javadump.theme;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages color themes for dumps.
 *
 * @author Olaldejo Olajide
 * @since 1.3.0
 */
public class ThemeManager {

    private static ThemeManager.Theme currentTheme = ThemeManager.Theme.DARK;
    private static final Map<String, ThemeManager.Theme> customThemes = new HashMap<>();

    static {
        // Register built-in themes
        registerTheme("dark", ThemeManager.Theme.DARK);
        registerTheme("light", ThemeManager.Theme.LIGHT);
        registerTheme("dracula", ThemeManager.Theme.DRACULA);
        registerTheme("monokai", ThemeManager.Theme.MONOKAI);
        registerTheme("solarized", ThemeManager.Theme.SOLARIZED_DARK);
    }

    public static void setTheme(ThemeManager.Theme theme) {
        currentTheme = theme;
    }

    public static void setTheme(String themeName) {
        ThemeManager.Theme theme = customThemes.get(themeName.toLowerCase());
        if (theme != null) {
            currentTheme = theme;
        }
    }

    public static ThemeManager.Theme getCurrentTheme() {
        return currentTheme;
    }

    public static void registerTheme(String name, ThemeManager.Theme theme) {
        customThemes.put(name.toLowerCase(), theme);
    }

    /**
     * Color theme for dumps.
     */
    public static class Theme {
        private final String background;
        private final String foreground;
        private final String typeColor;
        private final String stringColor;
        private final String numberColor;
        private final String keywordColor;
        private final String fieldColor;
        private final String structuralColor;

        public Theme(String background, String foreground, String typeColor,
                     String stringColor, String numberColor, String keywordColor,
                     String fieldColor, String structuralColor) {
            this.background = background;
            this.foreground = foreground;
            this.typeColor = typeColor;
            this.stringColor = stringColor;
            this.numberColor = numberColor;
            this.keywordColor = keywordColor;
            this.fieldColor = fieldColor;
            this.structuralColor = structuralColor;
        }

        // Getters
        public String getBackground() {
            return background;
        }

        public String getForeground() {
            return foreground;
        }

        public String getTypeColor() {
            return typeColor;
        }

        public String getStringColor() {
            return stringColor;
        }

        public String getNumberColor() {
            return numberColor;
        }

        public String getKeywordColor() {
            return keywordColor;
        }

        public String getFieldColor() {
            return fieldColor;
        }

        public String getStructuralColor() {
            return structuralColor;
        }

        // Built-in themes
        public static final ThemeManager.Theme DARK = new ThemeManager.Theme(
                "#1e1e1e", "#d4d4d4", "#ce9178", "#ce9178",
                "#b5cea8", "#569cd6", "#9cdcfe", "#808080"
        );

        public static final ThemeManager.Theme LIGHT = new ThemeManager.Theme(
                "#ffffff", "#000000", "#a31515", "#a31515",
                "#098658", "#0000ff", "#001080", "#808080"
        );

        public static final ThemeManager.Theme DRACULA = new ThemeManager.Theme(
                "#282a36", "#f8f8f2", "#ff79c6", "#f1fa8c",
                "#bd93f9", "#8be9fd", "#50fa7b", "#6272a4"
        );

        public static final ThemeManager.Theme MONOKAI = new ThemeManager.Theme(
                "#272822", "#f8f8f2", "#66d9ef", "#e6db74",
                "#ae81ff", "#f92672", "#a6e22e", "#75715e"
        );

        public static final ThemeManager.Theme SOLARIZED_DARK = new ThemeManager.Theme(
                "#002b36", "#839496", "#b58900", "#2aa198",
                "#d33682", "#268bd2", "#859900", "#586e75"
        );
    }
}
