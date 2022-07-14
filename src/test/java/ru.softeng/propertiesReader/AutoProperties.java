package ru.softeng.propertiesReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// This class determine locale in properties-file
public final class AutoProperties {
    private static final String PROPERTIES_FILE = "/choicelang.properties";
    private static Properties properties = new Properties();

    //Return instance of class
    public static AutoProperties getInstance() {
        final AutoProperties me = new AutoProperties();
        me.loadProperties(me.getPropertiesFile());
        return me;
    }

    private InputStream getPropertiesFile() { return this.getClass().getResourceAsStream(PROPERTIES_FILE); }

    private void loadProperties(InputStream propertiesStream) {
        try {
            properties.load(propertiesStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) { return properties.getProperty(key); }

    public String getProperty(String key, String defaultValue) { return properties.getProperty(key, defaultValue); }

    public void setProperty(String key, String value) { properties.setProperty(key, value); }
}