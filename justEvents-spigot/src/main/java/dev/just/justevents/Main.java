package dev.just.justevents;

import dev.just.justevents.commands.AllowPlayerToServerCommand;
import dev.just.justevents.commands.BanCommand;
import dev.just.justevents.hotbar.*;
import dev.just.justevents.listeners.InformationListener;
import dev.just.justevents.listeners.JoinLeaveMessages;
import dev.just.justevents.utils.Config;
import dev.just.justevents.utils.MongoDB;
import dev.just.justevents.utils.NPCHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

//    private static CloudNetDriver cloudNetDriver;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config.registerConfiguration();
        MongoDB.getMongoDB();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        registerListeners();
        registerCommands();
//        NPCHandler.setup();
//        cloudNetDriver = CloudNetDriver.getInstance();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ClickListener(), this);
        pluginManager.registerEvents(new HotbarGiveOnJoin(), this);
        pluginManager.registerEvents(new ServerInventory(), this);
        pluginManager.registerEvents(new InventoryProtector(), this);
        pluginManager.registerEvents(new DamageProtector(), this);
        pluginManager.registerEvents(new JoinLeaveMessages(), this);
        pluginManager.registerEvents(new InformationListener(), this);
    }
    private void registerCommands() {
        getCommand("allowplayer").setExecutor(new AllowPlayerToServerCommand());
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("ban").setExecutor(new BanCommand());
    }

    public static String getPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.BLUE  + "Lobby" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getNetworkPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.BLUE  + "Network" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getErrorPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.DARK_RED  + "Lobby" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getNoPlayer() {
        return getErrorPrefix() + "Lediglich Spieler können diese Aktion ausführen.";
    }
    public static String getNoPermission() {
        return getErrorPrefix() + "Dir fehlen die für die Ausführung dieser Aktion benötigten Rechte.";
    }

//    public static CloudNetDriver getCloudNetDriver() {
//        return cloudNetDriver;
//    }
}
