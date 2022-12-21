package dev.just.justevents.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

/**
 * Simple config as velocity does not provide its own instances
 * @author justCoding
 * @version 0.1-ALPHA
 */
public class Configuration {
    private String fileName;
    private File file;
    private Gson gson;
    private Map<String, Object> data;

    /**
     * Generates a config in plugin directory
     * @param fileName Name of the config file
     */
    public Configuration(String fileName) {
        this.fileName = fileName;
//        Check if directory exists, if not, create it
        File directory = new File("./plugins/justEvents");
        if (!directory.exists()) directory.mkdirs();
//        Check if file exists, if not, create it
        this.file = new File(directory, this.fileName);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Create gson instance
        this.gson = new GsonBuilder().setPrettyPrinting().create();
//        Read all remote values
        this.readRemoteFile();
    }

    /**
     * Generates a config with the file name "config.json"
     */
    public Configuration() {
        this("config.json");
    }

    /**
     * Writes all local values to the config file
     */
    private void writeLocalMap() {
        try {
            Writer writer = new FileWriter(this.file);
            this.gson.toJson(this.data, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears local data, and reads it from the json file
     */
    private void readRemoteFile() {
        this.data.clear();
        try {
            Reader reader = Files.newBufferedReader(this.file.toPath());
            this.data = gson.fromJson(reader, Map.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets the local value.
     * @param path Path in the local map.
     * @return The value of the path. Null if not existent.
     */
    public Object get(String path) {
        return this.data.get(path);
    }

    /**
     * Sets an object locally
     * @param path Path in the local map.
     * @param value The value to set.
     */
    public void setLocal(String path, Object value) {
        this.data.put(path, value);
    }

    /**
     * Sets an object locally and saves local storage to the file
     * @param path Path in the local map
     * @param value The value to set
     */
    public void set(String path, Object value) {
        this.setLocal(path, value);
        this.writeLocalMap();
    }

    public String getString(String path) {
        Object object = get(path);
        if (!(object instanceof String)) return null;
        return (String) object;
    }

}
