package dev.just.justevents.commands;

import dev.just.justevents.Main;
import dev.just.justevents.utils.Config;
import dev.just.justevents.utils.MongoDB;
import dev.just.justevents.utils.UUIDFetcher;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Main.getNoPermission());
            return false;
        }
        if (args.length != 2) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reasons")) {
                for (String reasonKey: Config.getKeysSection("banreasons")) {
                    sender.sendMessage(Main.getPrefix() + ChatColor.BLUE + reasonKey + ChatColor.GRAY +
                            " - " + ChatColor.BLUE + Config.get("banreasons." + reasonKey));
                }
                return true;
            }
            sender.sendMessage(Main.getErrorPrefix() + "Benutzung: /ban <Spieler> Grund");
            return false;
        }
        String uuidString = UUIDFetcher.getUUID(args[0]);
        if (uuidString == null) {
            sender.sendMessage(Main.getErrorPrefix() + "Dieser Spieler existiert nicht!");
            return false;
        }

        UUID uuid = UUID.fromString(uuidString.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));

        Document found = MongoDB.mongoDB.getPlayerCollection().find(new Document("uuid", uuid)).first();
        if (found == null) {
            sender.sendMessage(Main.getErrorPrefix() + "Der Spieler ist auf diesem Server nicht registriert!");
            return false;
        }
        String reason = args[1];
        if (!Config.contains("banreasons." + reason)) {
            sender.sendMessage(Main.getErrorPrefix() + "Dieser Grund existiert nicht!");
            return false;
        }
        MongoDB.mongoDB.getPlayerCollection().updateOne(new Document("uuid", uuid),
                new Document("$set", new Document(
                        "banned", true
                ).append("banreason", reason)));
        sender.sendMessage(Main.getPrefix() + "Der Spieler " + ChatColor.BLUE + args[0] + ChatColor.GRAY +
                " wurde wegen " + ChatColor.BLUE + Config.get("banreasons." + reason) + ChatColor.GRAY + " gebannt!");
        return true;
    }
}
