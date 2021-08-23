package dev.just.justevents.utils;

import dev.just.justEvents.MongoDBProvider;
import net.md_5.bungee.api.ChatColor;

public class MongoDB {
    public static MongoDBProvider mongoDB;
    public static void getMongoDB() {
        MongoDBProvider.print("Loading credentials from config...");
        String connectionString = Config.getString("mongodb.connectionString");
        if (connectionString == null) {
            MongoDBProvider.print(ChatColor.RED + "Please fill in the connection string in the config!");
            return;
        }
        MongoDBProvider.print("Connection-String found");
        String dbName = Config.getString("mongodb.dbName");
        if (dbName == null) {
            MongoDBProvider.print(ChatColor.RED + "Please fill in the db name in the config!");
            return;
        }
        MongoDBProvider.print("Database name found");
        String collectionName = Config.getString("mongodb.collectionName");
        if (collectionName == null) {
            MongoDBProvider.print(ChatColor.RED + "Please fill in the collection name in the config!");
            return;
        }
        MongoDBProvider.print("Collection name found");

        mongoDB = new MongoDBProvider(connectionString, dbName, collectionName);
    }
}
