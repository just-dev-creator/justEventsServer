package dev.just.justevents.hotbar;

import dev.just.justevents.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HotbarGiveOnJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        createPlayerInventory(event.getPlayer());
    }

    public static void createPlayerInventory(Player player) {
        Inventory inventory = player.getInventory();
        if (!BuildCommand.builders.contains(player)) {
            inventory.clear();
        }
        inventory.setItem(4, warpItem());
    }

    public static ItemStack warpItem() {
        return new ItemBuilder(Material.CHAIN_COMMAND_BLOCK)
                .setName(ChatColor.BLUE + "Server")
                .toItemStack();
    }
}
