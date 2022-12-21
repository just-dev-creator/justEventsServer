package dev.just.justevents.utils;

import dev.just.justEvents.MongoDBProvider;
import dev.just.justevents.Main;
import net.md_5.bungee.api.ChatColor;

public class MongoDB {
    public static MongoDBProvider mongoDB;
    public static void getMongoDB() {
        MongoDBProvider.print("Loading credentials from config...");
        if (Main.configuration.getString("mongodb.connectionString").equals("")) {
            MongoDBProvider.print("Please fill in the connection string in the config!");
            return;
        }
        MongoDBProvider.print("Connection-String found");
        if (Main.configuration.getString("mongodb.dbName").equals("")) {
            MongoDBProvider.print(ChatColor.RED + "Please fill in the db name in the config!");
            return;
        }
        MongoDBProvider.print("Database name found");
        if (Main.configuration.getString("mongodb.collectionName").equals("")) {
            MongoDBProvider.print(ChatColor.RED + "Please fill in the collection name in the config!");
            return;
        }
        MongoDBProvider.print("Collection name found");

        mongoDB = new MongoDBProvider(Main.configuration.getString("mongodb.connectionString"),
                Main.configuration.getString("mongodb.dbName"),
                Main.configuration.getString("mongodb.collectionName"),
                true);
    }
}
