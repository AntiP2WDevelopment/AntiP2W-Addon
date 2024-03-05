package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;

import java.util.ArrayList;

public class AutoLogin extends Module {


    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> password = sgGeneral.add(new StringSetting.Builder()
        .name("password")
        .description("The password to log in with.")
        .defaultValue("password")
        .build()
    );

    private final ArrayList<String> loginMessages = new ArrayList<String>() {{
        add("/login ");
        add("/login <password>");
    }};

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("command that runs before your password")
        .defaultValue("/login")
        .build()
    );

    public AutoLogin() {
        super(Categories.DEFAULT, "auto-login", "Automatically log into servers with the specified command");
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    private void onMessageRecieve(ReceiveMessageEvent event) {
        if (mc.world == null || mc.player == null) return;
        String msg = event.getMessage().getString();
        if (msg.startsWith(">")) return;
        for (String loginMsg: loginMessages) {
            if (msg.contains(loginMsg)) {

                String message = command.get() + " " + password;
                ChatUtils.sendPlayerMsg(message);
                break;
            }
        }
    }

}
