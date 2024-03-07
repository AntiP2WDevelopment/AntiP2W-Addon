package addon.antip2w.modules;

import addon.antip2w.irc.IRCHandler;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

public class IRC extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<String> key = sgGeneral.add(new StringSetting.Builder()
        .name("key")
        .description("the key")
        .filter((s, c) -> !s.isEmpty())
        .defaultValue("key")
        .build()
    );

    public final Setting<String> salt = sgGeneral.add(new StringSetting.Builder()
        .name("salt")
        .description("salt")
        .defaultValue("salt")
        .filter((s, c) -> !s.isEmpty())
        .build()
    );

    public IRC() {
        super(Categories.WIP, "IRC", "chat with other AntiP2W users");
        IRCHandler.addCallback((s) -> {
            if(s == null) {
                ChatUtils.warningPrefix("IRC", "Could not decrypt someone else's message.");
                return;
            }
            ChatUtils.infoPrefix("IRC", s);
        });
    }

    @Override
    public void onActivate() {
        IRCHandler.setRunning(true);
    }

    @Override
    public void onDeactivate() {
        IRCHandler.setRunning(false);
    }

    public static String getKey() {
        return Modules.get().get(IRC.class).key.get();
    }

    public static String getSalt() {
        return Modules.get().get(IRC.class).salt.get();
    }
}
