package dev.just.justevents.whitelist;

import dev.just.justevents.utils.MongoDB;
import org.bson.Document;

import java.util.UUID;

public class WhiteList {
    public static boolean isWhitelisted(UUID uuid) {
        try {
            Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", uuid)).first();
            return found != null && found.containsKey("can_access") && found.getBoolean("can_access");
        } catch (Exception ignored) {
            return false;
        }
    }
}
