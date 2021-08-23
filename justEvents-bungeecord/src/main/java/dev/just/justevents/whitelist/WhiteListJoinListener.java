package dev.just.justevents.whitelist;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WhiteListJoinListener implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (!WhiteList.isWhitelisted(player.getUniqueId())) {
            player.disconnect(new TextComponent(ChatColor.GREEN + "justEvents\n" + ChatColor.RED +
                    "Du hast aktuell keinen Zugriff auf den Server!"));
            return;
        }
        if (player.getServer() != null && player.getServer().getInfo() != null &&
                player.getServer().getInfo().getName().equalsIgnoreCase("lobby")) return;
        player.connect(ProxyServer.getInstance().getServerInfo("lobby"));
    }
}
