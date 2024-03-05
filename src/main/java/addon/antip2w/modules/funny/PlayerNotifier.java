package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class PlayerNotifier extends Module {

    public PlayerNotifier() {
        super(Categories.FUNNY, "PlayerNotifier", "000000000000000000000000000000000000000000000000");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> filters = sgGeneral.add(new StringListSetting.Builder()
            .name("filters")
            .description("The contents to check if a message matches.")
            .defaultValue()
            .build());

    private final Setting<Boolean> leave = sgGeneral.add(new BoolSetting.Builder()
            .name("leave when detected")
            .defaultValue(false)
            .build());

    @EventHandler
    private void onTick(TickEvent.Post event) {
        List<String> playerList = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerList().stream()
                .map(entry -> entry.getProfile().getName())
                .collect(toList());
        for (String playerName : playerList) {
            for (String filter : filters.get()) {
                if (playerName.contains(filter)) {
                    if (leave.get()) {
                        mc.player.networkHandler.getConnection().disconnect(Text.literal(playerName + " is online"));
                    } else {
                        ChatUtils.info(playerName + " is online");
                    }
                    break;
                }
            }
        }
    }
}
