package dev.just.justevents.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.just.justevents.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.stream.Collectors;

public class BanCommand implements SimpleCommand {

    private final ProxyServer proxyServer;

    public BanCommand(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length != 2) {
            invocation.source().sendMessage(Component.text(Main.getErrorPrefix() + "Du musst den Spieler" +
                    " und den Grund angeben. "));
            return;
        }
        Optional<Player> player = proxyServer.getPlayer(invocation.arguments()[0]);
        if (player.isEmpty()) {
            invocation.source().sendMessage(Component.text(Main.getErrorPrefix() + "Dieser Spieler existiert nicht"));
            return;
        }
        String reason = numberToReason(invocation.arguments()[1]);
        player.get().disconnect(Component.text(Main.getPrefix() + "Du wurdest ").append(
                Component.text("permanent", NamedTextColor.BLUE).append(
                Component.text(" gebannt.", NamedTextColor.GRAY))).append(
                Component.text("Grund: ").append(
                Component.text(reason, NamedTextColor.BLUE).append(
                Component.text("Du kannst Einspruch gegen diesen Ban via Discord an ", NamedTextColor.GRAY).append(
                Component.text("just.#3095", NamedTextColor.BLUE).append(
                Component.text("einlegen. ", NamedTextColor.GRAY)
                ))))));
        invocation.source().sendMessage(Component.text("Spieler erfolgreich gebannt. "));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length == 1) {
            return proxyServer.getAllPlayers().stream()
                    .map(player -> player.getUsername())
                    .filter(s -> s.startsWith(invocation.arguments()[0]))
                    .collect(Collectors.toList());
        } else if (invocation.arguments().length == 2) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("justEvents.commands.ban");
    }

    private String numberToReason(String reason) {
        Map<String, String> reasons = (Map<String, String>) Main.configuration.get("reasons");
        if (reasons == null) {
            reasons = new HashMap<>();
            reasons.put("1", "Unerlaubte Clientmodifikationen");
            reasons.put("2", "Beleidigungen oder anderes verletztendes Verhalten");
            reasons.put("3", "Computersabotage");
            Main.configuration.set("reasons", reasons);
        }
        if (!reasons.containsKey(reason)) {
            return "Nicht näher definiert";
        }
        return reasons.get(reason);
    }
}
