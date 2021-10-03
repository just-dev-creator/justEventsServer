package dev.just.justevents.utils;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import com.github.juliarn.npc.profile.Profile;
import dev.just.justevents.Main;
import dev.just.justevents.hotbar.ServerItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;
import java.util.UUID;

public class NPCHandler implements Listener {
    private static NPCPool pool;
    private static final Random random = new Random();
    public static void setup() {
        pool = NPCPool.builder(Main.getPlugin(Main.class))
                .spawnDistance(60)
                .actionDistance(30)
                .tabListRemoveTicks(20)
                .build();
        Bukkit.getPluginManager().registerEvents(new NPCHandler(), Main.getPlugin(Main.class));
        registerAllNPCs();
    }

    public static void registerNPC(Location location, UUID skinUUID, String name) {
        Profile profile = new Profile(skinUUID);
        profile.complete();

        profile.setName(name);
        profile.setUniqueId(new UUID(random.nextLong(), 0));

        NPC npc = NPC.builder()
                .profile(profile)
                .location(location)
                .imitatePlayer(false)
                .lookAtPlayer(true)
                .build(pool);
    }
    private static void registerAllNPCs() {
        registerEtbNCP();
    }

    private static void registerEtbNCP() {
        NPCHandler.registerNPC(new Location(Bukkit.getWorld("world"), 0.5, 102, -34.5),
                UUID.fromString("be4b5533-c56c-4b69-aa77-f80f75934b10"), ChatColor.BLUE + "EtB " +
                        "Ep. 2");
    }

    @EventHandler
    public void handleNPCInteract(PlayerNPCInteractEvent event) {
        if (event.getNPC().getProfile().getName().equalsIgnoreCase(ChatColor.BLUE + "EtB " +
                "Ep. 2")) {
            ServerItems.connectToServer(event.getPlayer(), "ETB2");
        }
    }
}
