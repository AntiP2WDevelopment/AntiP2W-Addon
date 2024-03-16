package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiriasCaptcha extends Module {
    private static final Pattern pattern = Pattern.compile("/captcha ([a-zA-Z0-9]{5})");

    public LiriasCaptcha() {
        super(Categories.DEFAULT, "Lirias Captcha", "Bypasses Captcha on RivalsNetwork because we are lazy ducks.");
    }

    @EventHandler
    private void onRecieveMessageEvent(ReceiveMessageEvent event) {
        Matcher code = pattern.matcher(event.getMessage().getString());
        if (!code.find()) return;
        info("A manók most fejtik a kódot!");
        ChatUtils.sendPlayerMsg(code.group());
    }
}
