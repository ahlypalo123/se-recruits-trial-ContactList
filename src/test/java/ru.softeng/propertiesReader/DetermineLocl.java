package ru.softeng.propertiesReader;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class DetermineLocl {
    private static String lang = AutoProperties.getInstance().getProperty("locale.language");
    private static String country = AutoProperties.getInstance().getProperty("locale.country");
    private static ResourceBundle resourceBundle = null;
    private static Locale locale = null;

    public static String getString(String code) {
        locale = new Locale(lang, country);
        resourceBundle = PropertyResourceBundle.getBundle("messages", locale);

        return resourceBundle.getString(code);
    }
}