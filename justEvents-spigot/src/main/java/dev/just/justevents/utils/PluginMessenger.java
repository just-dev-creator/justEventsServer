package dev.just.justevents.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.just.justevents.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

public class PluginMessenger implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subchannel = input.readUTF();
    }

    /**
     * @param player The player
     * @param message
     */
    public static void sendMessageToBungeeCord(Player player, String message, List<String> args) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(message);
        for (String arg : args) {
            output.writeUTF(arg);
        }
        player.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", output.toByteArray());
    }
}

