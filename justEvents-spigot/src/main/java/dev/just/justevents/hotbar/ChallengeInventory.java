package dev.just.justevents.hotbar;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceLifeCycle;
import dev.just.justevents.Main;
import dev.just.justevents.utils.ItemBuilder;
import dev.just.justevents.utils.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ChallengeInventory implements Listener {
    public static Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Challenge-Server");
        inventory.setItem(0, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(ChatColor.BLUE + "«" + ChatColor.GRAY + "Zurück")
                .toItemStack());
        inventory.setItem(8, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(ChatColor.BLUE + "«" + ChatColor.GRAY + "Zurück")
                .toItemStack());

        Collection<ServiceInfoSnapshot> cloudServices =
                Main.getCloudNetDriver().getCloudServiceProvider().getCloudServices("Challenge");

        Optional<ServiceInfoSnapshot> service = cloudServices.stream().filter(serviceInfoSnapshot -> {
            return serviceInfoSnapshot.getName().equals("Challenge-1");
        }).findFirst();
        if (service.isPresent() && service.get().isConnected() &&
                service.get().getLifeCycle().equals(ServiceLifeCycle.RUNNING)) {
            inventory.setItem(1, new ItemBuilder(Material.LIME_DYE)
                    .setName(ChatColor.BLUE + "Challenge 1")
                    .addLoreLine(ChatColor.GRAY + "Dieser Server ist aktuell direkt " + ChatColor.GREEN +
                            " verfügbar" + ChatColor.GRAY + ".")
                    .toItemStack());
        } else {
            inventory.setItem(1, new ItemBuilder(Material.LIGHT_GRAY_DYE)
                    .setName(ChatColor.BLUE + "Challenge 1")
                    .addLoreLine(ChatColor.GRAY + "Du kannst diesen Server aktuell " + ChatColor.BLUE + " starten" +
                            ChatColor.GRAY + ".")
                    .toItemStack());
        }
        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory() == null || event.getCurrentItem() == null ||
                !event.getView().getTitle().equals(ChatColor.BLUE + "Challenge-Server")) return;
        if (event.getCurrentItem().isSimilar(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(ChatColor.BLUE + "«" + ChatColor.GRAY + "Zurück")
                .toItemStack())) {
            event.getWhoClicked().openInventory(ServerInventory.inventory());
        }
        else if (event.getCurrentItem().isSimilar(new ItemBuilder(Material.LIGHT_GRAY_DYE)
                .setName(ChatColor.BLUE + "Challenge 1")
                .addLoreLine(ChatColor.GRAY + "Du kannst diesen Server aktuell " + ChatColor.BLUE + " starten" +
                        ChatColor.GRAY + ".")
                .toItemStack())) {
            event.getWhoClicked().closeInventory();
            player.sendMessage(Main.getNetworkPrefix() + "Wir prüfen gerade deine Berechtigungen. Dies kann einige Sekunden dauern. ");
            Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", player.getUniqueId())).first();
            if (found == null || !found.containsKey("allowedProjects")) {
                player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du hast keinen Zugriff auf diesen Server!" +
                        " Dieses Ereignis wird gemeldet werden. ");
                ((Player) player).sendTitle(ChatColor.RED + "Fehlende Berechtigungen!", ChatColor.GRAY + "Du " +
                        "hast keine Zugangsberechtigungen für diesen Server. ", 10, 40, 10);
                return;
            }
            ArrayList<String> allowedProjects = (ArrayList<String>) found.get("allowedProjects");
            if (!allowedProjects.contains("Challenge-1")) {
                player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du hast keinen Zugriff auf diesen Server!" +
                        " Dieses Ereignis wird gemeldet werden. ");
                ((Player) player).sendTitle(ChatColor.RED + "Fehlende Berechtigungen!", ChatColor.GRAY + "Du " +
                        "hast keine Zugangsberechtigungen für diesen Server. ", 10, 40, 10);
                return;
            }
            Collection<ServiceInfoSnapshot> cloudServices =
                    Main.getCloudNetDriver().getCloudServiceProvider().getCloudServices("Challenge");
            Optional<ServiceInfoSnapshot> service = cloudServices.stream().filter(serviceInfoSnapshot -> {
                return serviceInfoSnapshot.getName().equals("Challenge-1");
            }).findFirst();
            ServerItems.sendConnectInfo(player);
            ServerItems.startAndConnect(player, "Challenge-1", service.get());
        } else if (event.getCurrentItem().isSimilar(new ItemBuilder(Material.LIME_DYE)
                .setName(ChatColor.BLUE + "Challenge 1")
                .addLoreLine(ChatColor.GRAY + "Dieser Server ist aktuell direkt " + ChatColor.GREEN +
                        " verfügbar" + ChatColor.GRAY + ".")
                .toItemStack())) {
            event.getWhoClicked().closeInventory();
            player.sendMessage(Main.getNetworkPrefix() + "Wir prüfen gerade deine Berechtigungen. Dies kann einige Sekunden dauern. ");
            Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", player.getUniqueId())).first();
            if (found == null || !found.containsKey("allowedProjects")) {
                player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du hast keinen Zugriff auf diesen Server!" +
                        " Dieses Ereignis wird gemeldet werden. ");
                ((Player) player).sendTitle(ChatColor.RED + "Fehlende Berechtigungen!", ChatColor.GRAY + "Du " +
                        "hast keine Zugangsberechtigungen für diesen Server. ", 10, 40, 10);
                return;
            }
            ArrayList<String> allowedProjects = (ArrayList<String>) found.get("allowedProjects");
            if (!allowedProjects.contains("Challenge-1")) {
                player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du hast keinen Zugriff auf diesen Server!" +
                        " Dieses Ereignis wird gemeldet werden. ");
                ((Player) player).sendTitle(ChatColor.RED + "Fehlende Berechtigungen!", ChatColor.GRAY + "Du " +
                        "hast keine Zugangsberechtigungen für diesen Server. ", 10, 40, 10);
                return;
            }
            ServerItems.connectToServer(player, "Challenge-1");
        }
    }
}
