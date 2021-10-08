package dev.just.justevents.utils;

import dev.just.justEvents.MongoDBProvider;
import dev.just.justevents.Main;
import net.md_5.bungee.api.ChatColor;

public class MongoDB {
    public static MongoDBProvider mongoDB;
    public static void getMongoDB() {
        MongoDBProvider.print("Loading credentials from config...");
        if (Main.connectionString.equals("")) {
            MongoDBProvider.print("Please fill in the connection string in the config!");
            return;
        }
        MongoDBProvider.print("Connection-String found");
        if (Main.dbName.equals("")) {
            MongoDBProvider.print(ChatColor.RED + "Please fill in the db name in the config!");
            return;
        }
        MongoDBProvider.print("Database name found");
        if (Main.collectionName.equals("")) {
            MongoDBProvider.print(ChatColor.RED + "Please fill in the collection name in the config!");
            return;
        }
        MongoDBProvider.print("Collection name found");

        mongoDB = new MongoDBProvider(Main.connectionString, Main.dbName, Main.collectionName, true);
    }
}
