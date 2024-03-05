package addon.antip2w.utils;

import addon.antip2w.modules.RivalsPlayers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RivalsUtils {
    public static final HttpClient httpClient = HttpClient.newHttpClient();

    private static String getMapSettingsJson() throws RivalsRequestException {
        RivalsPlayers theModule = Modules.get().get(RivalsPlayers.class);
        HttpResponse<String> result;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://" + theModule.mapURL.get() + "/tiles/settings.json"))
                .header("User-Agent", theModule.userAgent.get())
                .header("Cookie", "cf_clearance=" + theModule.cfToken.get())
                .build();
            result = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RivalsRequestException(e);
        }
        if (result.statusCode() != 200) {
            throw new RivalsRequestException("Beszart a szerver ezzel a válaszkóddal: " + result.statusCode());
        }
        return result.body();
    }

    private static Map<UUID, JsonObject> getOnlinePlayerData() throws RivalsRequestException {
        RivalsPlayers theModule = Modules.get().get(RivalsPlayers.class);
        String json = getMapSettingsJson();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonArray jsonPlayers = jsonObject.getAsJsonArray("players");

        Map<UUID, JsonObject> onlineData = new HashMap<>();
        for (int i = 0; i < jsonPlayers.size(); i++) {
            JsonObject object = jsonPlayers.get(i).getAsJsonObject();
            onlineData.put(UUID.fromString(object.get("uuid").getAsString()), object);
        }
        return onlineData;
    }

    public static Map<PlayerListEntry, PlayerData> getPlayerData() throws RivalsRequestException {
        RivalsPlayers theModule = Modules.get().get(RivalsPlayers.class);
        Map<UUID, JsonObject> onlineData = getOnlinePlayerData();
        Map<PlayerListEntry, PlayerData> playerDatas = new HashMap<>();

        for (PlayerListEntry clientEntry: MinecraftClient.getInstance().getNetworkHandler().getPlayerList()) {
            JsonObject playerData = onlineData.get(clientEntry.getProfile().getId());
            if(playerData == null) {
                playerDatas.put(clientEntry, new PlayerData(true, 0, 0, null));
                continue;
            }
            String world = playerData.get("world").getAsString();
            if (!world.contains(theModule.filterByWorld.get().name)) continue;
            JsonObject pos = playerData.getAsJsonObject("position");
            int x = pos.get("x").getAsInt();
            int z = pos.get("z").getAsInt();
            playerDatas.put(clientEntry, new PlayerData(true, x, z, world));
        }

        return playerDatas;
    }

    public record PlayerData(boolean hidden, int x, int z, String worldName) {
    }

    public enum OpKingdomWorld {
        KOTH("koth"),
        SPAWN("spawn"),
        WORLD("world"),
        ALL("");

        public final String name;

        OpKingdomWorld(String name) {
            this.name = name;
        }
    }

    public static class RivalsRequestException extends Exception {
        public RivalsRequestException() {
            super();
        }

        public RivalsRequestException(String message) {
            super(message);
        }

        public RivalsRequestException(Throwable cause) {
            super(cause);
        }
    }
}
