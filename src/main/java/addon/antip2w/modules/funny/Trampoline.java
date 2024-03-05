package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import addon.antip2w.utils.TimerUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.orbit.EventHandler;

public class Trampoline extends Module {

    public Trampoline() {
        super(Categories.FUNNY, "Trampoline", "Its a trampoline woooooo");
    }

    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> sneaktimer = sgGeneral.add(new IntSetting.Builder()
        .name("sneak delay")
        .description("its milliseconds bitch")
        .defaultValue(100)
        .min(-1)
        .sliderMax(500)
        .build()
    );

    public TimerUtils timer = new TimerUtils();

    @Override
    public void onDeactivate() {
        timer.reset();
        super.onDeactivate();
        if (Modules.get().get("Sneak").isActive()) {
            Modules.get().get("Sneak").toggle();
        }
    }

    @Override
    public void onActivate() {
        timer.reset();
        super.onActivate();
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.player.isOnGround()) {
            mc.player.jump();
        } else if (timer.hasReached(sneaktimer.get())) {
            Modules.get().get("Sneak").toggle();
            timer.reset();
        }
    }
}
