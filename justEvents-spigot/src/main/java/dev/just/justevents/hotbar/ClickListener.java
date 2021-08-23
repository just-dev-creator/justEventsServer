package dev.just.justevents.hotbar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickListener implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!BuildCommand.builders.contains(event.getPlayer())) event.setCancelled(true);
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getItem() == null) return;
            if (event.getItem().isSimilar(HotbarGiveOnJoin.warpItem())) {
                event.setCancelled(true);
                event.getPlayer().openInventory(ServerInventory.inventory());
                event.getPlayer().getInventory().clear();
            }
        }
    }
}
