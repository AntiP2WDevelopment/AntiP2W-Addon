package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPModule extends Module {

    private final SettingGroup sgGeneral = settings.createGroup("General");

    private final Setting<Boolean> sendToAll = sgGeneral.add(new BoolSetting.Builder()
        .name("send-to-all")
        .description("Whether to send the IP address to all players.")
        .defaultValue(false)
        .build()
    );

    public IPModule() {
        super(Categories.FUNNY, "What's my ip?", "Shows your IP address");
    }

    @Override
    public void onActivate() {
        super.onActivate();

        try {
            URL url = new URL("https://httpbin.org/ip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                String ipAddress = response.toString().split(":")[1].replaceAll("[\"}]", "").trim();

                if (sendToAll.get()) {
                    ChatUtils.sendPlayerMsg("Hey everyone! My IP address is " + ipAddress + "!");
                } else {
                    info("Your external IP address is: " + ipAddress);
                }
            } else {
                info("Error: Unable to retrieve external IP address. HTTP response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            info("Error retrieving external IP address. " + e.getMessage());
        }

        this.toggle();
    }
}
