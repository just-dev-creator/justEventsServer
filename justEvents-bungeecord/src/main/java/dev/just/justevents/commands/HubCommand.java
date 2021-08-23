package dev.just.justevents.commands;

import dev.just.justevents.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {
    public HubCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(Main.getNoPlayer()));
        } else {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {
                player.sendMessage(new TextComponent(Main.getErrorPrefix() + "Du befindest dich bereits in der Lobby"));
            } else {
                player.connect(ProxyServer.getInstance().getServerInfo("lobby"));
            }
        }
    }
}
