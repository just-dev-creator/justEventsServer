package dev.just.justevents;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.just.justevents.listeners.JoinListener;
import dev.just.justevents.utils.Configuration;
import net.md_5.bungee.api.ChatColor;
import org.slf4j.Logger;

@Plugin(
        id = "justevents",
        name = "justEvents",
        version = "1.0"
)
public class Main {
    public static Configuration configuration;
    @Inject
    private Logger logger;
    @ Inject
    private ProxyServer server;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        configuration = new Configuration();
        registerListeners();
    }

    private void registerListeners() {
        EventManager eventManager = server.getEventManager();
        eventManager.register(this, new JoinListener());
    }

    public static String getPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.BLUE  + "Netzwerk" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getErrorPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.DARK_RED  + "Netzwerk" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getNoPlayer() {
        return getErrorPrefix() + "Lediglich Spieler können diese Aktion ausführen.";
    }
    public static String getNoPermission() {
        return getErrorPrefix() + "Dir fehlen die für die Ausführung dieser Aktion benötigten Rechte.";
    }
}
