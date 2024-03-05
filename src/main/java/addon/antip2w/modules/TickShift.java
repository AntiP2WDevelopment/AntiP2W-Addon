package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.orbit.EventHandler;

//Credits to the BlackOut addon//

public class TickShift extends Module {
    public TickShift() {
        super(Categories.DEFAULT, "Tick Shift", "Stores packets when standing still and uses them when you start moving.");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<SmoothMode> smooth = sgGeneral.add(new EnumSetting.Builder<SmoothMode>()
        .name("Smoothness")
        .description(".")
        .defaultValue(SmoothMode.Exponent)
        .build()
    );
    public final Setting<Integer> packets = sgGeneral.add(new IntSetting.Builder()
        .name("Packets")
        .description("How many packets to store for later use.")
        .defaultValue(50)
        .min(0)
        .sliderRange(0, 100)
        .build()
    );
    private final Setting<Double> timer = sgGeneral.add(new DoubleSetting.Builder()
        .name("Timer")
        .description("How many packets to send every movement tick.")
        .defaultValue(2)
        .min(1)
        .sliderRange(0, 10)
        .build()
    );

    public int unSent = 0;
    private boolean lastTimer = false;
    private boolean lastMoving = false;
    private final Timer timerModule = Modules.get().get(Timer.class);

    public void onActivate() {
        unSent = 0;
    }

    public void onDeactivate() {
        if (lastTimer) {
            lastTimer = false;
            timerModule.setOverride(Timer.OFF);
        }
    }

    public String getInfoString() {
        return String.valueOf(unSent);
    }

    @EventHandler
    private void onTick(TickEvent.Pre e) {
        if (unSent > 0 && lastMoving) {
            lastMoving = false;
            lastTimer = true;
            timerModule.setOverride(getTimer());
        } else if (lastTimer) {
            lastTimer = false;
            timerModule.setOverride(Timer.OFF);
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        if (e.movement.length() > 0 && !(e.movement.length() > 0.0784 && e.movement.length() < 0.0785)) {
            unSent = Math.max(0, unSent - 1);
            lastMoving = true;
        }
    }

    private double getTimer() {
        if (smooth.get() == SmoothMode.Disabled) {
            return timer.get();
        }
        double progress = 1 - (unSent / (float) packets.get());

        if (smooth.get() == SmoothMode.Exponent) {
            progress *= progress * progress * progress * progress;
        }

        return 1 + (timer.get() - 1) * (1 - progress);
    }

    public enum SmoothMode {
        Disabled,
        Normal,
        Exponent
    }
}
