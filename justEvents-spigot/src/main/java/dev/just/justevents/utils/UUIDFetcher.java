package dev.just.justevents.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UUIDFetcher {
    // write a method to get the uuid from a given player name using the mojang api
    public static String getUUID(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode != 200){
                throw new IOException("Failed to get the UUID, response code : " + responseCode);
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            String response = scanner.nextLine();
            scanner.close();
            connection.disconnect();

            JsonObject json = new JsonParser().parse(response).getAsJsonObject();
            String uuid = json.get("id").getAsString();
            return uuid;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}