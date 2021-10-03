package dev.just.justEvents.abstracts;

import dev.just.justEvents.MongoDBProvider;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class Server {
    private final String ip;
    private final int port;
    private final String name;
    private final boolean requiresPermission;

    public Server(String ip, int port, String name, boolean requiresPermission) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.requiresPermission = requiresPermission;
    }

    public boolean isPlayerAllowed(UUID uuid) {
        Document found = MongoDBProvider.getInstance().getPlayerCollection().find(new Document(
                "uuid", uuid
        )).first();
        if (!found.containsKey("allowedProjects")) return false;
        List<String> allowedProjects = (List<String>) found.get("allowedProjects");
        return allowedProjects.contains(name);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public boolean isRequiresPermission() {
        return requiresPermission;
    }
}
