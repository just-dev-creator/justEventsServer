package dev.just.justevents.joinme;

import dev.just.justevents.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JoinMeCommand extends Command {
    public JoinMeCommand() {
        super("joinme");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(Main.getNoPlayer()));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 2 && args[0].equals("accept")) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
            if (target == null || target.getServer() == null ||
                    target.getServer().getInfo().getName().equalsIgnoreCase("lobby") ||
                    target.getServer().getInfo().getName().equals(player.getServer().getInfo().getName())) {
                sender.sendMessage(new TextComponent(Main.getErrorPrefix() + "Du kannst dich aktuell nicht auf den" +
                        " Server des Spielers verbinden!"));
            } else {
                player.connect(target.getServer().getInfo());
                Title title = ProxyServer.getInstance().createTitle();
                title.fadeIn(1);
                title.stay(20);
                title.fadeOut(1);
                title.title(new TextComponent(ChatColor.BLUE + "Du wirst verbunden..."));
                title.subTitle(new TextComponent(ChatColor.GRAY + "Warte" +
                        " einen kurzen Moment"));
                player.sendTitle(title);
            }
            return;
        }
        if (!sender.hasPermission("justevents.command.joinme")) {
            sender.sendMessage(new TextComponent(Main.getNoPermission()));
            return;
        }
        if (player.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {
            sender.sendMessage(new TextComponent(Main.getErrorPrefix() + "Du kannst in der Lobby kein JoinMe erstellen!"));
            return;
        }
        BaseComponent pre = new TextComponent(Main.getPrefix() + "Der Spieler " + ChatColor.BLUE +
                player.getName() + ChatColor.GRAY + " spielt nun auf dem Server " + ChatColor.BLUE +
                player.getServer().getInfo().getName().toUpperCase() + ChatColor.GRAY + ".");
        BaseComponent after = new TextComponent(ChatColor.GRAY + " Klicke, um mit ihm zu spielen!");
        after.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/joinme accept " + player.getName()));
        ProxyServer.getInstance().broadcast(pre, after);
    }
}
