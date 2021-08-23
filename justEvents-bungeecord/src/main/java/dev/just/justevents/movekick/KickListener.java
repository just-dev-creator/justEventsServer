package dev.just.justevents.movekick;

import dev.just.justevents.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class KickListener implements Listener {
    @EventHandler
    public void onKick(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.getServer() != null && player.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {
            player.disconnect(new TextComponent(Main.getErrorPrefix() + "Es ist ein Fehler aufgetreten und du wurdest " +
                    "gekickt. \nVersuche es in einigen Minuten erneut oder melde das Problem an einen Organisator."));
            System.out.println(player.getName() + " was disconnected cause of: " + event.getKickReason());
        } else {
            event.setCancelled(true);
            player.connect(ProxyServer.getInstance().getServerInfo("lobby"));
            player.sendMessage(new TextComponent(Main.getErrorPrefix() + "Der vorherige Server hat dich gekickt und" +
                    " du wurdest auf die Lobby verschoben."));
            player.sendMessage(new TextComponent(Main.getErrorPrefix() + "Grrund: " + event.getKickReason()));
        }
    }
}
