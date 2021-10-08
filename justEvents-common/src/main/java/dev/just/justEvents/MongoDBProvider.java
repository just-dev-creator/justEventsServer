package dev.just.justEvents;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;

import java.util.Arrays;

public class MongoDBProvider {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> users;
    private static MongoDBProvider instance;
    private static boolean noColor = false;

    public MongoDBProvider(String connectionString, String dataBaseName, String collectionName) {
        init(connectionString, dataBaseName, collectionName);
    }

    public MongoDBProvider(String connectionString, String dataBaseName, String collectionName, boolean noColor) {
        MongoDBProvider.noColor = noColor;
        init(connectionString, dataBaseName, collectionName);
    }

    private void init(String connectionString, String dataBaseName, String collectionName) {
        print("Connecting to server...");
        try {
            mongoClient = MongoClients.create(connectionString);
        } catch (Exception e) {
            print(ChatColor.RED + "There was an error while connecting to the Database. Exiting...");
            print(e.toString());
            return;
        }
        boolean dbExists = false;
        print("Searching for the database...");
        for (String name : mongoClient.listDatabaseNames()) {
            if (name.equals(dataBaseName)) {
                dbExists = true;
                print("Found database");
                break;
            }
        }
        if (!dbExists) {
            print(ChatColor.RED + "Database isn't existing. Exiting...");
            return;
        }
        mongoDatabase = mongoClient.getDatabase(dataBaseName);

        boolean collectionExists = false;
        for (String name : mongoDatabase.listCollectionNames()) {
            if (name.equals(collectionName)) {
                collectionExists = true;
                print("Found collection");
                break;
            }
        }
        if (!dbExists) {
            print(ChatColor.RED + "Collection doesn't exist. Exiting...");
            return;
        }
        users = mongoDatabase.getCollection(collectionName);

        print("Connection to database established.");
        instance = this;
    }


    public MongoCollection<Document> getPlayerCollection() {
        return this.users;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    private static String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GREEN + "MongoDB-Connector" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY;
    }
    public static void print(String... message) {
        if (!noColor) {
            System.out.println(getPrefix() + Arrays.toString(message));
        } else {
            System.out.println(ChatColor.stripColor(Arrays.toString(message)));
        }
    }

    public static MongoDBProvider getInstance() {
        return instance;
    }
}
