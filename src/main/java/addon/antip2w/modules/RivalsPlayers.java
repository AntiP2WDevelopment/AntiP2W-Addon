package addon.antip2w.modules;

import addon.antip2w.utils.RivalsUtils.PlayerData;
import addon.antip2w.utils.RivalsUtils.RivalsRequestException;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.network.PlayerListEntry;

import java.util.Map;

import static addon.antip2w.utils.RivalsUtils.OpKingdomWorld;
import static addon.antip2w.utils.RivalsUtils.getPlayerData;

public class RivalsPlayers extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<OpKingdom> mapURL = sgGeneral.add(new EnumSetting.Builder<OpKingdom>()
        .name("Map URL")
        .description("the URL, what else could it be?")
        .defaultValue(OpKingdom.Map1)
        .build()
    );

    public final Setting<String> userAgent = sgGeneral.add(new StringSetting.Builder()
        .name("UserAgent")
        .description("UserAgent for da request")
        .defaultValue("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
        .build());

    public final Setting<String> cfToken = sgGeneral.add(new StringSetting.Builder()
        .name("CF token")
        .description("Copy 'cf_clearance' from browser cookies")
        .defaultValue("")
        .build());

    public final Setting<Boolean> showHidden = sgGeneral.add(new BoolSetting.Builder()
        .name("Show hidden")
        .description("Won't show players who aren't on the map")
        .defaultValue(false)
        .build());

    public final Setting<OpKingdomWorld> filterByWorld = sgGeneral.add(new EnumSetting.Builder<OpKingdomWorld>()
        .name("Filter world")
        .description("Show players who are in the selected world")
        .defaultValue(OpKingdomWorld.ALL)
        .build()
    );

    public RivalsPlayers() {
        super(Categories.DEFAULT, "RivalsNocom", "Shows player's locations lol ..........................................");
    }

    @Override
    public void onActivate() {
        if(mc.player == null) {
            toggle();
            return;
        }

        try {
            Map<PlayerListEntry, PlayerData> playerDatas = getPlayerData();
            for(PlayerListEntry entry: playerDatas.keySet()) {
                PlayerData data = playerDatas.get(entry);
                if (data.hidden() && showHidden.get())
                    ChatUtils.info(String.format("%s - is hidden from the map", entry.getDisplayName()));
                else
                    ChatUtils.info(String.format("%s - Position: (%d, %d) - World: %s", entry.getDisplayName(), data.x(), data.z(), data.worldName()));
            }
        } catch (RivalsRequestException e) {
            throw new RuntimeException(e);
        }
        toggle();
    }

    public enum OpKingdom {
        Map1("opkd.rivalsnetwork.hu"),
        Map2("opkd2.rivalsnetwork.hu");

        public final String URL;
        OpKingdom(String URL) {
            this.URL = URL;
        }

        @Override
        public String toString() {
            return URL;
        }
    }
}

