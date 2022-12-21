package dev.just.justevents.commands;

import dev.just.justevents.Main;
import dev.just.justevents.hotbar.ServerItems;
import dev.just.justevents.utils.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AllowPlayerToServerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Main.getNoPermission());
            return false;
        }
        else {
            if (args.length != 2) {
                sender.sendMessage(Main.getErrorPrefix() + "Verwendung: /allowplayer <Name> <Projekt>");
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Main.getErrorPrefix() + "Dieser Spieler existiert nicht!");
                return false;
            }
            Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", target.getUniqueId())).first();
            if (found == null) {
                sender.sendMessage(Main.getErrorPrefix() + "Der Spieler ist auf diesem Server nicht registriert!");
                return false;
            }
            boolean exists = false;
            for (ServerItems.ServerType serverType : ServerItems.ServerType.values()) {
                if (serverType.toString().equals(args[1])) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                sender.sendMessage(Main.getErrorPrefix() + "Dieses Projekt existiert nicht!");
                return false;
            }
            ArrayList<String> allowedServer = new ArrayList<>();
            if (found.containsKey("allowedProjects")) allowedServer = (ArrayList<String>) found.get("allowedProjects");
            allowedServer.add(args[1]);
            MongoDB.mongoDB.getPlayerCollection().updateOne(new Document("uuid", target.getUniqueId()),
                    new Document("$set", new Document(
                    "allowedProjects", allowedServer
            )));
            return true;
        }
    }
}
