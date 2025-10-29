package org.takeyourdata.service.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    private static final Properties properties = new Properties();

    public static void create() {
        try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream("./config.properties")) {
            properties.store(fos, "shake this");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties get() {
        try (FileInputStream input = new FileInputStream("./config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
