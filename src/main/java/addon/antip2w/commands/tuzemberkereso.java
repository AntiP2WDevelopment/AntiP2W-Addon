package addon.antip2w.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class tuzemberkereso extends Command {
    public tuzemberkereso() {
        super("fyreplayer", "Does things");
    }
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", StringArgumentType.word()).executes(ctx -> {
            String playerName = StringArgumentType.getString(ctx, "player");
            queryDatabaseAPI(playerName);
            return 1;
        }));
    }
    /*TODO:
    maybe replace the try function when pinging the api,
    make formatting when printing out the got details,
    make red formatted Error message when "Invalid player name,
    its json, just split everything and organize
   */
    private static void queryDatabaseAPI(String playerName) {
        String apiURL = "https://account.fyremc.hu/api/player/" + playerName;

        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                ChatUtils.info("[Tűzemberkereső]" + response);
            } else {
                ChatUtils.info("an error occured, here's the response code btw:" + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            ChatUtils.info("TűzMájncraft web down :c");
        }
    }
}
