package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import addon.antip2w.utils.Stats;
import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.RichPresence;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.utils.StarscriptTextBoxRenderer;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.StarscriptError;

import java.util.List;

public class AntiP2WRPC extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> title = sgGeneral.add(new StringSetting.Builder()
        .name("title")
        .description("What to display as the RPC's title.")
        .renderer(StarscriptTextBoxRenderer.class)
        .defaultValue("AntiP2W Addon")
        .onChanged(booleanSetting -> alertTitle())
        .build()
    );

    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("line-1")
        .description("Messages for the first RPC line.")
        .renderer(StarscriptTextBoxRenderer.class)
        .defaultValue("Duping on {server}")
        .build()
    );

    private final Setting<List<String>> messages2 = sgGeneral.add(new StringListSetting.Builder()
        .name("line-2")
        .description("Messages for the second RPC line.")
        .renderer(StarscriptTextBoxRenderer.class)
        .defaultValue("Exteron smells")
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("update-delay")
        .description("How many seconds before switching to a new RPC message.")
        .range(0, 30)
        .sliderRange(0, 30)
        .defaultValue(5)
        .build()
    );

    public AntiP2WRPC() {
        super(Categories.FUNNY, "AntiP2W RPC", "");
    }

    public static final RichPresence rpc = new RichPresence();
    private int alertDelay = 0;
    private int updateDelay;
    private int messageI, messageI2;

    @Override
    public void onActivate() {
        if (Modules.get().isActive(DiscordPresence.class)) {
            error( "Default Meteor RPC is already enabled! Overriding.");
            Modules.get().get(DiscordPresence.class).toggle();
        }
        if (messages.get().isEmpty() || messages2.get().isEmpty()) {
            error( "Your RPC messages are empty!");
            this.toggle();
            return;
        }
        updateDelay = delay.get() * 20;
        DiscordIPC.start(1201189059511472318L, null);
        rpc.setStart(Stats.rpcStart);
        rpc.setLargeImage("template", starscript(title.get()));
        updateDetails();
        messageI = 0;
        messageI2 = 0;
    }

    @Override
    public void onDeactivate() {
        DiscordIPC.stop();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!Utils.canUpdate()) return;
        updateDelay--;
        alertDelay--;
        if (updateDelay <= 1) {
            updateDetails();
            updateDelay = delay.get() * 20;
        }
    }

    private void updateDetails() {
        if (isActive() && Utils.canUpdate()) {
            if (messages.get().isEmpty() || messages2.get().isEmpty()) return;
            if (messageI >= messages.get().size()) messageI = 0;
            if (messageI2 >= messages2.get().size()) messageI2 = 0;
            int i = messageI++;
            int i2 = messageI2++;
            rpc.setDetails(starscript(messages.get().get(i)));
            rpc.setState(starscript(messages2.get().get(i2)));
            DiscordIPC.setActivity(rpc);
        }
    }

    private void alertTitle() {
        if (alertDelay <= 0) {
            warning("Restart RPC to apply your new title.");
            alertDelay = 200;
        }
    }

    private static Script compile(String script) {
        if (script == null) return null;
        Parser.Result result = Parser.parse(script);
        if (result.hasErrors()) {
            MeteorStarscript.printChatError(result.errors.get(0));
            return null;
        }
        return Compiler.compile(result);
    }

    private static String starscript(String script) {
        var compiled = compile(script);
        if (compiled == null) ChatUtils.warning("Malformed starscript message");
        try {
            var section = MeteorStarscript.ss.run(compiled);
            return section.text;
        }
        catch (StarscriptError e) {
            MeteorStarscript.printChatError(e);
        }
        return "";
    }
}
