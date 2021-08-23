package dev.just.justevents.tablist;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TabList {
    public static void setTab(ProxiedPlayer player) {
        player.setTabHeader(new TextComponent(ChatColor.GRAY + "Du spielst auf dem " + ChatColor.BLUE + "justEvents-Server"),
                new TextComponent(ChatColor.GRAY + "discord.gg/" + ChatColor.BLUE + "3bVrzdEYgt"));
    }
}
