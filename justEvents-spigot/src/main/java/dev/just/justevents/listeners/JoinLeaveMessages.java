package dev.just.justevents.listeners;

import dev.just.justevents.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveMessages implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(Main.getPrefix() + ChatColor.BLUE + event.getPlayer().getName() + ChatColor.GRAY + " hat die Lobby betreten.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Main.getPrefix() + ChatColor.BLUE + event.getPlayer().getName() + ChatColor.GRAY + " hat die Lobby verlassen.");
    }
}
