package addon.antip2w.modules.griefing;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.utils.StarscriptTextBoxRenderer;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;

public class AutoScoreboard extends Module {
    private final SettingGroup sgTitle = settings.createGroup("Title Options");
    private final SettingGroup sgContent = settings.createGroup("Content Options");

    private final Setting<String> title = sgTitle.add(new StringSetting.Builder()
        .name("title")
        .description("Title of the scoreboard to create. Supports Starscript.")
        .defaultValue("AntiP2W Client!")
        .wide()
        .renderer(StarscriptTextBoxRenderer.class)
        .build()
    );

    private final Setting<String> titleColor = sgTitle.add(new StringSetting.Builder()
        .name("title-color")
        .description("Color of the title")
        .defaultValue("dark_red")
        .wide()
        .build()
    );

    private final Setting<List<String>> content = sgContent.add(new StringListSetting.Builder()
        .name("content")
        .description("Content of the scoreboard. Supports Starscript.")
        .defaultValue(Arrays.asList(
            "Exteron says hi!",
            "AntiP2W Client on top!",
            "{player} was here",
            "{date}"
        ))
        .renderer(StarscriptTextBoxRenderer.class)
        .build()
    );

    private final Setting<String> contentColor = sgContent.add(new StringSetting.Builder()
        .name("content-color")
        .description("Color of the content")
        .defaultValue("red")
        .build()
    );

    public AutoScoreboard() {
        super(Categories.GRIEF, "Auto Scoreboard", "Prints funny stuff to the users screen");
    }

    @Override
    public void onActivate() {
        assert mc.player != null;
        if(!mc.player.hasPermissionLevel(2)) {
            toggle();
            error("No permission!");
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        String scoreboardName = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        String thecommand = "/scoreboard objectives add " + scoreboardName + " dummy {\"text\":\"" + MeteorStarscript.run(MeteorStarscript.compile(title.get())) + "\",\"color\":\"" + titleColor.get() + "\"}";
        if (thecommand.length() <= 257) {
            ChatUtils.sendPlayerMsg(thecommand);
        } else {
            int characterstodelete = thecommand.length()-257;
            error("Title is too long. Shorten it by "+characterstodelete+" characters.");
            toggle();
            return;
        }
        ChatUtils.sendPlayerMsg("/scoreboard objectives setdisplay sidebar " + scoreboardName);
        int i = content.get().size();
        for (String string : content.get()) {
            String randomName = RandomStringUtils.randomAlphabetic(10).toLowerCase();
            ChatUtils.sendPlayerMsg("/team add " + randomName);
            String thecommand2 = "/team modify " + randomName + " suffix {\"text\":\" " + MeteorStarscript.run(MeteorStarscript.compile(string)) + "\"}";
            if (thecommand2.length()<=257){
                ChatUtils.sendPlayerMsg(thecommand2);
            }
            else {
                int characterstodelete = thecommand2.length()-257;
                error("This content line is too long (" + MeteorStarscript.run(MeteorStarscript.compile(string))+"). Shorten it by " + characterstodelete + " characters.");
                toggle();
                return;
            }
            ChatUtils.sendPlayerMsg("/team modify " + randomName + " color " + contentColor);
            ChatUtils.sendPlayerMsg("/team join " + randomName + " " + i);
            ChatUtils.sendPlayerMsg("/scoreboard players set " + i + " " + scoreboardName + " " + i);
            i--;
        }
        toggle();
        info("Created scoreboard.");
    }
}