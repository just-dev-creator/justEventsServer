package dev.just.justevents.utils;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private static File file;
    private static Configuration configuration;
    public static void initialize() {
        File dir = new File("./plugins/justEvents");
        if (!dir.exists()) dir.mkdirs();
        file = new File(dir, "config.yml");
        try {
            if (!file.exists()) file.createNewFile();
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void set(String path, Object value) {
        configuration.set(path, value);
    }
    public static boolean contains(String path) {
        return configuration.contains(path);
    }
    public static Object get(String path) {
        return configuration.get(path);
    }

    public static String getString(String path) {
        return configuration.getString(path);
    }
    public static boolean getBoolean(String path) {
        return configuration.getBoolean(path);
    }
}
