package org.takeyourdata.service.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class ConfigProperties {
    private static final Properties properties = new Properties();

    public static void create() throws URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(ConfigProperties.class.getResource("config.properties")).toURI());
        try {
            Files.createFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties get() {
        return properties;
    }
}
