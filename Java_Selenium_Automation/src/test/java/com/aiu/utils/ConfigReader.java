package com.aiu.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from properties file
 */
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value;
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getApiUrl() {
        return getProperty("api.url");
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout"));
    }

    public static String getAdminEmail() {
        return getProperty("admin.email");
    }

    public static String getAdminPassword() {
        return getProperty("admin.password");
    }

    public static String getStudentEmail() {
        return getProperty("student.email");
    }

    public static String getStudentPassword() {
        return getProperty("student.password");
    }

    public static String getOrganizerEmail() {
        return getProperty("organizer.email");
    }

    public static String getOrganizerPassword() {
        return getProperty("organizer.password");
    }

    public static String getScreenshotPath() {
        return getProperty("screenshot.path");
    }

    public static String getReportPath() {
        return getProperty("report.path");
    }

    public static String getExtentReportName() {
        return getProperty("extent.report.name");
    }

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure"));
    }
}
