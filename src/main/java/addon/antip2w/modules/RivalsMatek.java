package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.HoverEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RivalsMatek extends Module {
    private static final Pattern pattern = Pattern.compile("(-?\\d+) \\* (-?\\d+)");
    private static final Pattern patternketto = Pattern.compile("(-?\\d+) (\\+|-) (-?\\d+)");

    public RivalsMatek() {
        super(Categories.WIP, "RivalsMatek", "Csináld meg a matek házit inkább :joy: ");
    }

    @EventHandler
    private void onReceiveMessageEvent(ReceiveMessageEvent event) {
        if (event.getMessage().getSiblings().size() > 4 && event.getMessage().getSiblings().get(4).getStyle().getHoverEvent() != null && event.getMessage().getSiblings().get(4).getStyle().getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT) != null) {
            String message = event.getMessage().getSiblings().get(4).getStyle().getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT).getString();
            szamolas(message);
        }
    }
    public void szamolas(String string) {
        Matcher mittudtomen = pattern.matcher(string);
        if (mittudtomen.find()) {
            int result = Integer.parseInt(mittudtomen.group(1)) * Integer.parseInt(mittudtomen.group(2));
            string = string.replaceFirst(pattern.pattern(), result + "");
            szamolas(string);
        }
        mittudtomen = patternketto.matcher(string);
        if (mittudtomen.find()) {
            if (mittudtomen.group(0).contains("+")) {
                int result = Integer.parseInt(mittudtomen.group(1)) + Integer.parseInt(mittudtomen.group(3));
                string = string.replaceFirst(patternketto.pattern(), result + "");
            } else {
                int result = Integer.parseInt(mittudtomen.group(1)) - Integer.parseInt(mittudtomen.group(3));
                string = string.replaceFirst(patternketto.pattern(), result + "");
            } szamolas(string);
        } ChatUtils.info("A manócskák kiszámolták. Itt az eredmény: "+ string);
    }
}
