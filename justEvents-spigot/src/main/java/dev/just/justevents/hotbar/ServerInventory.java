package dev.just.justevents.hotbar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class ServerInventory implements Listener {
    public static Inventory inventory() {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Server");
        inventory.setItem(0, ServerItems.getItem(ServerItems.ServerType.ETB3));
        inventory.setItem(1, ServerItems.getItem(ServerItems.ServerType.PLS));
        inventory.setItem(2, ServerItems.getItem(ServerItems.ServerType.SMASH));
        inventory.setItem(3, ServerItems.getItem(ServerItems.ServerType.BUILD));
        inventory.setItem(3, ServerItems.getItem(ServerItems.ServerType.BORDER100));
        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getCurrentItem() == null ||
                !event.getView().getTitle().equals(ChatColor.BLUE + "Server")) return;
        if (event.getCurrentItem().isSimilar(ServerItems.getItem(ServerItems.ServerType.ETB3)))
            ServerItems.connectToServer(event.getWhoClicked(), "ETB3");
        else if (event.getCurrentItem().isSimilar(ServerItems.getItem(ServerItems.ServerType.ETS)))
            ServerItems.connectToServer(event.getWhoClicked(), "ETS");
        else if (event.getCurrentItem().isSimilar(ServerItems.getItem(ServerItems.ServerType.ETB2)))
            ServerItems.connectToServer(event.getWhoClicked(), "ETB2");
        else if (event.getCurrentItem().isSimilar(ServerItems.getItem(ServerItems.ServerType.PLS)))
            ServerItems.connectToServer(event.getWhoClicked(), "MBE");
        else if (event.getCurrentItem().isSimilar(ServerItems.getItem(ServerItems.ServerType.SMASH)))
            ServerItems.connectToServer(event.getWhoClicked(), "SMASH");
        else if (event.getCurrentItem().isSimilar(ServerItems.getItem(ServerItems.ServerType.BUILD)))
            ServerItems.connectToServer(event.getWhoClicked(), "BUILD");
        else if (event.getCurrentItem().isSimilar(ServerItems.getItem(ServerItems.ServerType.BORDER100))) {
            ServerItems.connectToServer(event.getWhoClicked(), "BORDER100");
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Server")) {
            HotbarGiveOnJoin.createPlayerInventory((Player) event.getPlayer());
        }
    }
}
