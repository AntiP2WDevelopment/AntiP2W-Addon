package addon.antip2w.modules;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.Renderer3D;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RivalsKingdoms extends Module {
    public static final HttpClient httpClient = HttpClient.newHttpClient();
    public static final Pattern pattern = Pattern.compile("g>(.+?)<");

    public static final HashMap<String, HashSet<Vec2s>> kingdoms = new HashMap<>();
    public static final HashSet<Vec2s> chunks = new HashSet<>();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<RivalsPlayers.OpKingdom> mapURL = sgGeneral.add(new EnumSetting.Builder<RivalsPlayers.OpKingdom>()
        .name("Map URL")
        .description("the URL, what else could it be?")
        .defaultValue(RivalsPlayers.OpKingdom.Map1)
        .build()
    );

    private final Setting<Integer> renderY = sgGeneral.add(new IntSetting.Builder()
        .name("RenderY")
        .description("The y level of the renderer.")
        .defaultValue(64)
        .sliderRange(-100, 320)
        .build());

    private final Setting<Boolean> limitRange = sgGeneral.add(new BoolSetting.Builder()
        .name("Limit range")
        .description("Limits the range of rendering.")
        .defaultValue(true)
        .build());

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("Range")
        .description("The range in blocks.")
        .defaultValue(64)
        .visible(limitRange::get)
        .sliderRange(10, 1024)
        .build());

    private final Setting<Boolean> shouldSearch = sgGeneral.add(new BoolSetting.Builder()
        .name("Search by kingdom")
        .description("Search by name")
        .defaultValue(false)
        .build());

    private final Setting<String> search = sgGeneral.add(new StringSetting.Builder()
        .name("Search")
        .description("Contain search")
        .defaultValue("EquaL")
        .visible(shouldSearch::get)
        .build());

    public RivalsKingdoms() {
        super(Categories.DEFAULT, "CapturedChunks", "Shows you captured chunks on Rivals Kingdoms servers. (use RivalsNocom to config)");
    }

    @Override
    public void onActivate() {
        HttpRequest httpRequest;
        HttpResponse<String> result;
        RivalsPlayers nocom = Modules.get().get(RivalsPlayers.class);
        try {
            httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://"+mapURL.get()+"/tiles/world/markers/kingdoms.json"))
                .header("User-Agent", nocom.userAgent.get())
                .header("Cookie", "cf_clearance="+nocom.cfToken.get())
                .build();
            result = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            error("Szar a request... Majd írj rá uhm... azt hiszem japankacsa01-re, mert ő teremtett, de honnan tudnám, hisz én csak egy frissen írt kód vagyok. ;P");
            toggle();
            return;
        }
        if (result.statusCode() != 200) {
            error("Hibás szerver válasz!?: "+result.statusCode());
            toggle();
            return;
        }
        JsonArray object = new Gson().fromJson(result.body(), JsonArray.class);
        for (JsonElement element : object) {
            JsonObject point1 = element.getAsJsonObject().getAsJsonObject("data").getAsJsonObject("point1");
            String name = element.getAsJsonObject().getAsJsonObject("options").getAsJsonObject("popup").get("content").getAsString();
            Matcher matcher = pattern.matcher(name);
            if (!matcher.find()) {
                System.out.println("no found");
                continue;
            }
            name = matcher.group(1);
            Vec2s fromPos = new Vec2s(point1.get("x").getAsShort(), point1.get("z").getAsShort());
            if (kingdoms.get(name) != null) kingdoms.get(name).add(fromPos);
            else kingdoms.put(name, new HashSet<>());
            chunks.add(fromPos);
        }
    }

    @Override
    public void onDeactivate() {
        chunks.clear();
        kingdoms.clear();
    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        if (shouldSearch.get()) {
            for (String key : kingdoms.keySet()) {
                if (key.startsWith(search.get())) renderChunks(event.renderer, kingdoms.get(key));
            }
        } else renderChunks(event.renderer, chunks);
    }

    private void renderChunks(Renderer3D renderer, HashSet<Vec2s> chunks) {
        for (Vec2s chunk : chunks) {
            if (limitRange.get() && chunk.squaredDistance(mc.player.getPos()) > range.get()*range.get()) continue;
            renderer.boxLines(chunk.x, renderY.get(), chunk.z, chunk.x+16, renderY.get(), chunk.z+16, Color.RED, 0);
        }
    }

    public record Vec2s(short x, short z) {
        public double squaredDistance(Vec3d from) {
            double dx = from.x - x;
            double dz = from.z - z;
            return dx * dx + dz * dz;
        }
    }
}
