package dev.just.justevents;

import dev.just.justevents.commands.HubCommand;
import dev.just.justevents.joinme.JoinMeCommand;
import dev.just.justevents.movekick.KickListener;
import dev.just.justevents.tablist.JoinListener;
import dev.just.justevents.utils.Config;
import dev.just.justevents.utils.MongoDB;
import dev.just.justevents.whitelist.WhiteListJoinListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class Main extends Plugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        Config.initialize();
        MongoDB.getMongoDB();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new WhiteListJoinListener());
        pluginManager.registerListener(this, new KickListener());
        pluginManager.registerListener(this, new JoinListener());
    }
    private void registerCommands() {
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerCommand(this, new HubCommand("lobby"));
        pluginManager.registerCommand(this, new HubCommand("hub"));
        pluginManager.registerCommand(this, new HubCommand("return"));
        pluginManager.registerCommand(this, new JoinMeCommand());
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
