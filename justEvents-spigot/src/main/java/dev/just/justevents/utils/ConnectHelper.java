package dev.just.justevents.utils;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dev.just.justEvents.abstracts.Server;
import dev.just.justevents.Main;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ConnectHelper {
    public static ArrayList<Server> bungeeServers = new ArrayList<>();
    public static void init() {
        Bukkit.getLogger().info(Main.getNetworkPrefix() + "Searching for servers in network...");
        MongoDatabase database = MongoDB.mongoDB.getMongoDatabase();
        boolean found = false;
        for (String name : database.listCollectionNames()) {
            if (name.equals("servers")) {
                found = true;
                break;
            }
        }
        if (!found) database.createCollection("servers");
        MongoCollection<Document> serverCollection = MongoDB.mongoDB.getMongoDatabase().getCollection("servers");
        for (Document server : getAllDocuments(serverCollection)) {
            if (server.containsKey("ip") && server.containsKey("port") && server.containsKey("name") &&
                    server.containsKey("requiresPermission")) {
                Server bungeeServer = new Server(
                        server.getString("ip"),
                        server.getInteger("port"),
                        server.getString("name"),
                        server.getBoolean("requiresPermission")
                );
                bungeeServers.add(bungeeServer);
                Bukkit.getLogger().info(Main.getNetworkPrefix() + "Found Server \"" + ChatColor.BLUE +
                        bungeeServer.getName() + ChatColor.GRAY + "\" with IP " + ChatColor.BLUE +
                        bungeeServer.getIp() + ":" + bungeeServer.getPort());
            }
        }
        Bukkit.getLogger().info(Main.getNetworkPrefix() + "Ending search for servers in the network...");
    }

    private static List<Document> getAllDocuments(MongoCollection<Document> collection) {
        FindIterable<Document> i = collection.find();
        MongoCursor<Document> cursor = i.iterator();
        ArrayList<Document> allDocuments = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                allDocuments.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return allDocuments;
    }
}
