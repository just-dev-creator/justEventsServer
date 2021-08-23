package dev.just.justevents.tablist;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent event) {
        TabList.setTab(event.getPlayer());
    }
}
