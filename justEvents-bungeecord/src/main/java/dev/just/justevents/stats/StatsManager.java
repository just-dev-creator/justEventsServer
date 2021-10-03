package dev.just.justevents.stats;

import com.mongodb.client.model.Updates;
import dev.just.justevents.utils.MongoDB;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class StatsManager {
    public static Map<UUID, Long> playerLogonTime = new HashMap<>();
    public static Map<UUID, Long> playerCachedTime = new HashMap<>();

    public static void addTime(UUID uuid, long seconds) {
        Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", uuid)).first();
        if (found == null) return;
        if (!found.containsKey("time_played") || !(found.get("time_played") instanceof Long)) {
            setTime(uuid, seconds);
        } else {
            Bson updates = Updates.combine(
                    Updates.inc("time_played", seconds)
            );
            MongoDB.mongoDB.getPlayerCollection().updateOne(found, updates);
        }
    }

    public static long getTime(UUID uuid) {
        Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", uuid)).first();
        if (found == null || !found.containsKey("time_played") || !(found.get("time_played") instanceof Long))
            return 0L;
        return found.getLong("time_played");
    }

    private static void setTime(UUID uuid, long time) {
        Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", uuid)).first();
        if (found == null) return;
        Bson updates = Updates.combine(
                Updates.set("time_played", time)
        );
        MongoDB.mongoDB.getPlayerCollection().updateOne(found, updates);
    }
}
