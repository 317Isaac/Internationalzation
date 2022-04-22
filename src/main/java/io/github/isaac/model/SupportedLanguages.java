package io.github.isaac.model;

import java.util.ArrayList;
import java.util.List;

public enum SupportedLanguages {
    Chinese_Simplified("zh-CN", "简体中文", "Chinese Simplified"),

    Chinese_Traditional("zh-HK", "繁体中文", "Chinese Traditional"),

    English("en", "English", "English"),

    DefaultLocal("", "Default","Default");

    private String languageCode;

    private String languageDisplayName;

    private String languageEnglishDisplayName;

    SupportedLanguages(String languageCode, String languageDisplayName, String languageEnglishDisplayName) {
        this.languageCode = languageCode;
        this.languageDisplayName = languageDisplayName;
        this.languageEnglishDisplayName = languageEnglishDisplayName;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public String getLanguageDisplayName() {
        return this.languageDisplayName;
    }

    public String getLanguageEnglishDisplayName() {
        return this.languageEnglishDisplayName;
    }

    public static List<SupportedLanguages> getAllSupportedLanguages() {
        return getI18nsLanguages();
    }

    public String toString() {
        return getLanguageEnglishDisplayName() + "(\"" + getLanguageCode() + "\", \"" + getLanguageDisplayName() + "\")";
    }

    public String getAndroidStringFolderNameSuffix() {
        if (this == Chinese_Simplified)
            return "zh-rCN";
        if (this == Chinese_Traditional)
            return "zh-rTW";
        return getLanguageCode();
    }

    private static List<SupportedLanguages> getI18nsLanguages() {
        List<SupportedLanguages> result = new ArrayList<>();
        result.add(DefaultLocal);
        result.add(English);
        result.add(Chinese_Simplified);
        result.add(Chinese_Traditional);
        return result;
    }
}

