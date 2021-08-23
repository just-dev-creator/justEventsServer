package dev.just.justevents.hotbar;

import dev.just.justevents.Main;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BuildCommand implements CommandExecutor {
    public static ArrayList<Player> builders = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getNoPlayer());
            return false;
        }
        if (!sender.isOp()) {
            sender.sendMessage(Main.getNoPermission());
            return false;
        }
        Player player = (Player) sender;
        if (builders.contains(player)) {
            builders.remove(player);
            player.sendMessage(Main.getPrefix() + "Du kannst nun nicht weiter bauen!");
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            builders.add(player);
            player.sendMessage(Main.getPrefix() + "Du kannst nun bauen!");
            player.setGameMode(GameMode.CREATIVE);
        }
        return false;
    }
}
