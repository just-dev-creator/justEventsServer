package dev.just.justevents.hotbar;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceLifeCycle;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.ServerSelectorType;
import dev.just.justevents.Main;
import dev.just.justevents.utils.ItemBuilder;
import dev.just.justevents.utils.MongoDB;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class ServerItems {
    private static ArrayList<HumanEntity> onCooldown = new ArrayList<>();
    public enum ServerType {
        ETB3,
        CHALLENGEJ,
        CHALLENGEB,
        ETS,
        ETB2,
        MBE,
        PLS,
        SMASH,
	    BUILD,
        BORDER100
    }

    public static ItemStack getItem(ServerType serverType) {
        if (serverType.equals(ServerType.ETB3)) {
            return new ItemBuilder(Material.NETHERITE_AXE)
                    .setName(ChatColor.BLUE + "ExploreTheBuild Ep. 3")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        } else if (serverType.equals(ServerType.ETS)) {
            return new ItemBuilder(Material.ICE)
                    .setName(ChatColor.BLUE + "ExploreTheSky")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        } else if (serverType.equals(ServerType.ETB2)) {
            return new ItemBuilder(Material.GLOWSTONE)
                    .setName(ChatColor.BLUE + "ExploreTheBuild Ep. 2")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        } else if (serverType.equals(ServerType.MBE)) {
            return new ItemBuilder(Material.LECTERN)
                    .setName(ChatColor.BLUE + "MasterBuildersExtended")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        } else if (serverType.equals(ServerType.PLS)) {
            return new ItemBuilder(Material.LANTERN)
                    .setName(ChatColor.BLUE + "Paula-Laurenz-Server")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        } else if (serverType.equals(ServerType.SMASH)) {
            return new ItemBuilder(Material.STICK)
                    .setName(ChatColor.BLUE + "Smash")
                    .addLoreLine(ChatColor.RED + "⚠⚠⚠ Dieser Server befindet sich im Wartungsmodus ⚠⚠⚠")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        } else if (serverType.equals(ServerType.BUILD)) {
            return new ItemBuilder(Material.NETHERITE_PICKAXE)
                    .setName(ChatColor.BLUE + "Build")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        } else if (serverType.equals(ServerType.BORDER100)) {
            return new ItemBuilder(Material.BEDROCK)
                    .setName(ChatColor.BLUE + "100x100-Border " + ChatColor.ITALIC + "(EtB 3.5)")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                    .toItemStack();
        }
        return new ItemBuilder(Material.BARRIER)
                .setName(ChatColor.RED + "Es ist ein Fehler aufgetreten!")
                .addLoreLine("Trace-ID: " + new Random().nextInt(1000000) + 1 + "_" + serverType.toString())
                .addLoreLine("ServerType:" + serverType.toString())
                .toItemStack();
    }

    public static void connectToServer(HumanEntity player, String taskName) {
        if (onCooldown.contains(player)) return;
        player.closeInventory();
        HotbarGiveOnJoin.createPlayerInventory((Player) player);
        player.sendMessage(Main.getNetworkPrefix() + "Warte einen Moment, während wir deinen Datenbankeintrag " +
                "abrufen.");
        Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", player.getUniqueId())).first();
        if (found == null || !found.containsKey("allowedProjects")) {
            player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du bist kein Teilnehmer dieses Projekts!");
            ((Player) player).sendTitle(ChatColor.RED + "Keine Berechtigungen!", ChatColor.GRAY + "Du " +
                    "bist kein Teilnehmer dieses Projektes", 10, 40, 10);
            return;
        }
        ArrayList<String> allowedProjects = (ArrayList<String>) found.get("allowedProjects");
        if (!allowedProjects.contains(taskName)) {
            player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Du bist kein Teilnehmer dieses Projekts!");
            ((Player) player).sendTitle(ChatColor.RED + "Keine Berechtigungen!", ChatColor.GRAY + "Du " +
                    "bist kein Teilnehmer dieses Projektes", 10, 40, 10);
            return;
        }
        player.sendMessage(Main.getNetworkPrefix() + "Wir suchen nach dem Server im Cloudsystem. Das kann einen " +
                "Moment dauern!");
        Collection<ServiceInfoSnapshot> cloudServices =
                Main.getCloudNetDriver().getCloudServiceProvider().getCloudServices(taskName);
        if (!cloudServices.isEmpty()) {
            ServiceInfoSnapshot topServer = cloudServices.iterator().next();
            if (!topServer.getLifeCycle().equals(ServiceLifeCycle.RUNNING)) {
                sendConnectInfo(player);
                startAndConnect(player, taskName, topServer);
            } else {
                ((Player) player).sendTitle(ChatColor.BLUE + "Du wirst verbunden...", ChatColor.GRAY
                        + "Warte" + " einen kurzen Moment", 1, 20,1);
                final IPlayerManager playerManager =
                        CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(player.getUniqueId());
                cloudPlayer.getPlayerExecutor().connectToTask(taskName, ServerSelectorType.HIGHEST_PLAYERS);
            }
        } else {
            sendConnectInfo(player);
            final ServiceInfoSnapshot newService;
            try {
                newService = Main.getCloudNetDriver().getCloudServiceFactory().
                        createCloudService(Main.getCloudNetDriver().getServiceTaskProvider().getServiceTask(taskName));
            } catch (NullPointerException e) {
                ((Player) player).sendTitle(ChatColor.RED + "Es ist ein Fehler aufgetreten!",
                        ChatColor.GRAY + "Bitte melde dich bei einem Adminstrator",
                        1, 40,1);
                player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "FEHLER: " + ChatColor.GRAY +
                        e.getMessage());
                System.out.println(taskName);
                return;
            }
            if (newService == null) {
                ((Player) player).sendTitle(ChatColor.RED + "Es ist ein Fehler aufgetreten!",
                        ChatColor.GRAY + "Bitte melde dich bei einem Adminstrator",
                        1, 40, 1);
                return;
            }
            startAndConnect(player, taskName, newService);
        }
    }

    public static void sendConnectInfo(HumanEntity player) {
        ((Player) player).sendTitle(ChatColor.BLUE + "Starte Server...", ChatColor.GRAY + "Warte" +
                " einige Zeit", 1, 100,1);
        player.sendMessage(Main.getNetworkPrefix() +
                "Dein gewünschter Server ist aktuell im Ruhemodus. Wir müssen ihn daher erst starten.");
        player.sendMessage(Main.getNetworkPrefix() + "Sobald der Server bereit ist, wirst du " +
                "automatisch verbunden!");
    }
    public static void startAndConnect(HumanEntity player, String taskName, ServiceInfoSnapshot topServer) {
        topServer.provider().startAsync().onComplete(unused -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(Main.getNetworkPrefix() + ChatColor.RED + "Es kann sein, dass der Server seine " +
                            "Startroutine noch nicht abgeschlossen hat. Solltest du eine erhebliche Verzögerung deiner " +
                            "Spielaktionen feststellen, warte noch einige Minuten.");
                    player.sendMessage(Main.getNetworkPrefix() + "Wenn du einige Zeit nicht auf dem Server spielst, wird " +
                            "dieser wieder in den Ruhemodus versetzt. Du musst ihn dann erneut starten. ");
                    ((Player) player).sendTitle(ChatColor.BLUE + "Server gestartet...", ChatColor.GRAY + "Du" +
                            " wirst automatisch verbunden!", 1, 20,1);
                    final IPlayerManager playerManager =
                            CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                    ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(player.getUniqueId());
                    cloudPlayer.getPlayerExecutor().connectToTask(taskName, ServerSelectorType.HIGHEST_PLAYERS);
                }
            }.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 400);
        });
    }
}
