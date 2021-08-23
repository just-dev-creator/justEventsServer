package dev.just.justevents.listeners;

import dev.just.justevents.Main;
import dev.just.justevents.utils.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InformationListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String information = Config.getString("information");
        if (information == null) return;
        event.getPlayer().sendMessage(Main.getPrefix() + "Es liegen einige neue Informationen vor: ");
        event.getPlayer().sendMessage(Main.getPrefix() + information);
    }
}
