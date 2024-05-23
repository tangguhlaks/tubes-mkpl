package com.example.jafafxlearn;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class RuntimeConfiguration {
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static final String LOGIN_ID_KEY = "loginId";
    public static void saveLoginId(String loginId) {
        Properties properties = new Properties();
        properties.setProperty(LOGIN_ID_KEY, loginId);

        try (FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(fileOutputStream, "User Preferences");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getLoginId() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
            return properties.getProperty(LOGIN_ID_KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
