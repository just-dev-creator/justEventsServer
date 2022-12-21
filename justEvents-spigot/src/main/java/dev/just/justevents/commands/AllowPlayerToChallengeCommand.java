package dev.just.justevents.commands;

import dev.just.justevents.Main;
import dev.just.justevents.utils.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllowPlayerToChallengeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Main.getNoPermission());
            return false;
        }
        if (args.length != 2) {
            sender.sendMessage(Main.getErrorPrefix() + "Verwendung: /allowchallenge <Name> <Projekt>");
            return false;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Main.getErrorPrefix() + "Dieser Spieler existiert nicht!");
            return false;
        }
        Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", target.getUniqueId())).first();
        if (found == null) {
            sender.sendMessage(Main.getErrorPrefix() + "Der Spieler ist auf diesem Server nicht registriert!");
            return false;
        }
        ArrayList<String> allowedServer = new ArrayList<>();
        if (found.containsKey("allowedProjects")) allowedServer = (ArrayList<String>) found.get("allowedProjects");
        String toAdd = "Challenge-" + args[1];
        if (allowedServer.contains(toAdd)) {
            sender.sendMessage(Main.getErrorPrefix() + "Der Spieler kann diesen Server bereits betreten. ");
            return false;
        }
        allowedServer.add(toAdd);
        MongoDB.mongoDB.getPlayerCollection().updateOne(new Document("uuid", target.getUniqueId()),
            new Document("$set", new Document(
                    "allowedProjects", allowedServer
        )));
        sender.sendMessage(Main.getPrefix() + "Der Spieler wurde erfolgreich zum Server " + ChatColor.BLUE + toAdd +
                ChatColor.GRAY + " hinzugefügt.");
        return true;
    }
}
