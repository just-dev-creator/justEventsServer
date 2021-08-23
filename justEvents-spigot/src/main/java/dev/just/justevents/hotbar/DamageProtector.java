package dev.just.justevents.hotbar;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class DamageProtector implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) event.setCancelled(true);
        else return;
        ((Player) event.getEntity()).setHealth(20.0);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) event.setCancelled(true);
        event.getEntity().setFoodLevel(20);
    }
}
