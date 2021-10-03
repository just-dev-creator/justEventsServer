package dev.just.justevents.stats;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void postLogin(PostLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        StatsManager.playerLogonTime.put(uuid, System.currentTimeMillis());
    }

    @EventHandler
    public void postLeave(PlayerDisconnectEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (StatsManager.playerLogonTime.containsKey(uuid)) {
            long timeBetween = System.currentTimeMillis() - StatsManager.playerLogonTime.get(uuid);
        }
    }
}
