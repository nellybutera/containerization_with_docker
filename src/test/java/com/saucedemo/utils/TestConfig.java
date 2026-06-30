package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = TestConfig.class.getClassLoader()
                .getResourceAsStream("testdata.properties")) {
            if (is == null) {
                throw new RuntimeException("testdata.properties not found on classpath");
            }
            PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load testdata.properties", e);
        }
    }

    public static String get(String key) {
        String value = PROPS.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing key in testdata.properties: " + key);
        }
        return value;
    }
}
