package dev.just.justevents.hotbar;

import dev.just.justevents.Main;
import dev.just.justevents.utils.ItemBuilder;
import dev.just.justevents.utils.MongoDB;
import dev.just.justevents.utils.PluginMessenger;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ServerItems {
    private static ArrayList<HumanEntity> onCooldown = new ArrayList<>();
    public enum ServerType {
        ETB5,
    }

    public static ItemStack getItem(ServerType serverType) {
        if (serverType.equals(ServerType.ETB5)) {
            return new ItemBuilder(Material.MACE)
                    .setName(ChatColor.BLUE + "ExploreTheBuild Ep. 5")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        }

        return new ItemBuilder(Material.BARRIER)
                .setName(ChatColor.RED + "Es ist ein Fehler aufgetreten!")
                .addLoreLine("Trace-ID: " + new Random().nextInt(1000000) + 1 + "_" + serverType.toString())
                .addLoreLine("ServerType:" + serverType.toString())
                .toItemStack();
    }

    public static void connectToServer(HumanEntity player, String serverName) {
        if (onCooldown.contains(player)) return;
        player.closeInventory();
        HotbarGiveOnJoin.createPlayerInventory((Player) player);
        player.sendMessage(Main.getNetworkPrefix() + "Warte einen Moment, während wir deinen Datenbankeintrag " +
                "abrufen.");
        Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", player.getUniqueId())).first();
        if (found == null || !found.containsKey("allowedProjects")) {
            player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du bist kein Teilnehmer dieses Projekts!");
            ((Player) player).sendTitle(ChatColor.RED + "Keine Berechtigungen!", ChatColor.GRAY + "Du " +
                    "bist kein Teilnehmer dieses Projektes", 10, 40, 10);
            return;
        }
        ArrayList<String> allowedProjects = (ArrayList<String>) found.get("allowedProjects");
        if (!allowedProjects.contains(serverName)) {
            player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du bist kein Teilnehmer dieses Projekts!");
            ((Player) player).sendTitle(ChatColor.RED + "Keine Berechtigungen!", ChatColor.GRAY + "Du " +
                    "bist kein Teilnehmer dieses Projektes", 10, 40, 10);
            return;
        }

        onCooldown.add(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                onCooldown.remove(player);
            }
        }.runTaskLater(Main.getPlugin(Main.class), 20 * 3);

        PluginMessenger.sendMessageToBungeeCord((Player) player, "ConnectToServer", Collections.singletonList(serverName));
    }
}
