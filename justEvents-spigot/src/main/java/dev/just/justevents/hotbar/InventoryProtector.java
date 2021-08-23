package dev.just.justevents.hotbar;

import dev.just.justevents.Main;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryProtector implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (BuildCommand.builders.contains(((Player)event.getWhoClicked()))) {
            return;
        }
        if (event.getInventory().getType().equals(InventoryType.PLAYER) ||
                event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            HumanEntity player = event.getWhoClicked();
            event.setCancelled(true);
            player.sendMessage(Main.getErrorPrefix() + "Du darfst dies hier nicht! ");
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (BuildCommand.builders.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage(Main.getErrorPrefix() + "Du darfst dies hier nicht! ");
        event.setCancelled(true);
    }
}
