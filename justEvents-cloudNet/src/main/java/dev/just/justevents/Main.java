package dev.just.justevents;

import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.driver.module.driver.DriverModule;
import dev.just.justevents.utils.MongoDB;

public class Main extends DriverModule {
    public static String connectionString;
    public static String dbName;
    public static String collectionName;

    @ModuleTask(event = ModuleLifeCycle.STARTED)
    public void initConfig() {
        connectionString = getConfig().getString("ConnectionString", "mongodb://mongodb0.example.com:27017");
        dbName = getConfig().getString("DbName", "main");
        collectionName = getConfig().getString("CollectionName", "server");
    }

    @ModuleTask(event = ModuleLifeCycle.STARTED)
    public void enable() {
        System.out.println("justEvents enabled!");
        MongoDB.getMongoDB();
    }
}