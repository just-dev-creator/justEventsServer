package dev.just.justevents.jnr;

import dev.just.justevents.Main;
import dev.just.justevents.hotbar.BuildCommand;
import dev.just.justevents.utils.Config;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JumpNRunManager implements Listener {

    Map<Player, LocalDateTime> playerTimes = new HashMap<>();

    public JumpNRunManager() {
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player : playerTimes.keySet()) {
                    long secondsBetween = ChronoUnit.MILLIS.between(playerTimes.get(player), LocalDateTime.now());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                            ChatColor.GREEN + String.valueOf(secondsBetween/1000) + " Sekunden"
                    ));
                    if (BuildCommand.builders.contains(player) || player.getGameMode() != GameMode.ADVENTURE) {
                        player.sendMessage(Main.getErrorPrefix() + "Parkour erfolgreich beendet. ");
                        playerTimes.remove(player);
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, 20);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getTo().getBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
            if (BuildCommand.builders.contains(player)) {
                player.sendMessage(Main.getErrorPrefix() + "Du kannst das nur tun, wenn du nicht im Build-Modus" +
                        " bist! ");
                return;
            }
            player.setGameMode(GameMode.ADVENTURE);
            playerTimes.put(player, LocalDateTime.now());
        } else if (event.getTo().getBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
            if (!playerTimes.containsKey(player)) return;
            long secondsBetween = ChronoUnit.MILLIS.between(playerTimes.get(player), LocalDateTime.now());
            player.sendMessage(Main.getPrefix() + "Du hast " + ChatColor.BLUE + ((double) secondsBetween/1000) + ChatColor.GRAY +
                    " Sekunden für den Parcour benötigt. ");
            if (Config.contains("jnr.times." + player.getUniqueId().toString())) {
                int playerConfigTime = Config.getInt("jnr.times." + player.getUniqueId().toString());
                if (playerConfigTime > secondsBetween) {
                    player.sendMessage(Main.getPrefix() + "Du hast damit einen neuen persönlichen " + ChatColor.BLUE +
                            "Highscore" + ChatColor.GRAY + " aufgestellt.");
                }
            } else {
                player.sendMessage(Main.getPrefix() + "Du hast damit einen neuen persönlichen " + ChatColor.BLUE +
                        "Highscore" + ChatColor.GRAY + " aufgestellt.");
            }
            Config.set("jnr.times." + player.getUniqueId().toString(), secondsBetween);
            if (Config.contains("jnr.highscore")) {
                if (Config.getInt("jnr.highscore.time") > secondsBetween) {
                    player.sendMessage(Main.getPrefix() + "Du hast damit einen neuen globalen " + ChatColor.BLUE +
                            "Highscore" + ChatColor.GRAY + " aufgestellt. Herzlichen Glückwunsch!");
                    Config.set("jnr.highscore.player", player.getUniqueId().toString());
                    Config.set("jnr.highscore.time", secondsBetween);
                } else {
                    player.sendMessage(Main.getPrefix() + "Der aktuelle Highscore liegt bei " + ChatColor.BLUE +
                            ((double)Config.getInt("jnr.highscore.time")/1000) + " Sekunden " + ChatColor.GRAY + "und " +
                            "wird von " + ChatColor.BLUE +
                            Bukkit.getOfflinePlayer(UUID.fromString(Config.getString("jnr.highscore.player"))).getName() +
                            ChatColor.GRAY + " innegehalten. ");
                }
            } else {
                Config.set("jnr.highscore.player", player.getUniqueId().toString());
                Config.set("jnr.highscore.time", secondsBetween);
                player.sendMessage(Main.getPrefix() + "Du hast damit einen neuen globalen " + ChatColor.BLUE +
                        "Highscore" + ChatColor.GRAY + " aufgestellt. Herzlichen Glückwunsch!");
            }
            playerTimes.remove(player);
        } else if (playerTimes.containsKey(player) && player.getLocation().getY() < -1) {
            playerTimes.remove(player);
            player.teleport(new Location(Bukkit.getWorld("world"), 9, 0, -15, -90f, 0f));
        }
    }
}
