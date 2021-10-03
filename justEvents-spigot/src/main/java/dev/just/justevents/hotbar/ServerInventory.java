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
//        inventory.setItem(1, ServerItems.getItem(ServerItems.ServerType.ETS));
        inventory.setItem(1, ServerItems.getItem(ServerItems.ServerType.ETB2));
        inventory.setItem(2, ServerItems.getItem(ServerItems.ServerType.PLS));
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
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Server")) {
            HotbarGiveOnJoin.createPlayerInventory((Player) event.getPlayer());
        }
    }
}
