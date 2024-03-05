package addon.antip2w.modules.dupe;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.Text;

public class CahDupe extends Module {

    private final SettingGroup sgGeneral = settings.createGroup("General");

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("sell command")
        .description("sell comamnd to sell to ah")
        .defaultValue("/ah sell 69420")
        .build()
    );

    private final Setting<Integer> Int1 = sgGeneral.add(new IntSetting.Builder()
        .name("before")
        .description("Set the value to how much times you wanna send the command before getting kicked")
        .defaultValue(5)
        .min(1)
        .max(15)
        .build()
    );

    private final Setting<Integer> Int2 = sgGeneral.add(new IntSetting.Builder()
        .name("after")
        .description("Set the value to how much times you wanna send the command after getting kicked")
        .defaultValue(5)
        .min(0)
        .max(15)
        .build()
    );
    public CahDupe() {
        super(Categories.DEFAULT, "CrazyAh", "CrazyAh dupe aka the macro dupe");
    }

    @Override
    public void onActivate() {
        for (int i = 0; i < Int1.get(); i++) {
            ChatUtils.sendPlayerMsg(command.get());
        }

        mc.player.networkHandler.getConnection().disconnect(Text.literal("Dupe triggered."));

        for (int i = 0; i < Int2.get(); i++) {
            ChatUtils.sendPlayerMsg(command.get());
        }

        this.toggle();
    }
}
