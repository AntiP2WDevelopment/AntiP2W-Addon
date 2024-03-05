package addon.antip2w.modules.vulcan;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.entity.BoatMoveEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

// Modified version of the Meteor Client Boat Fly
public class VulcanBoatFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Horizontal speed in blocks per second.")
        .defaultValue(20)
        .range(0, 79)
        .sliderMax(79)
        .build()
    );

    private final Setting<Double> upwardSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("upward-speed-multiplier")
        .description("Upward speed multiplier.")
        .defaultValue(48)
        .range(5, 48)
        .sliderMax(48)
        .build()
    );

    private final Setting<Double> downwardSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("downward-speed")
        .description("Downward speed in blocks per second.")
        .defaultValue(100)
        .range(0, 100)
        .sliderMax(100)
        .build()
    );

    public final Setting<Double> timer = sgGeneral.add(new DoubleSetting.Builder()
        .name("timer")
        .description("Timer override.")
        .defaultValue(5)
        .range(1, 5)
        .sliderRange(1, 5)
        .build()
    );

    private static final double FALL_SPEED = 1.01;

    public VulcanBoatFly() {
        super(Categories.DEFAULT, "Vulcan Boat Fly", "395 bps with max settings guaranteed without flags");
    }

    @Override
    public void onDeactivate() {
        Modules.get().get(Timer.class).setOverride(Timer.OFF);
    }

    // don't suffocate in blocks
    private boolean moveHorizontally(double amount) {
        Box boundingBox = mc.player.getBoundingBox().union(mc.player.getVehicle().getBoundingBox());
        boundingBox = boundingBox.offset(0, amount, 0).union(boundingBox);
        if (mc.world.getBlockCollisions(null, boundingBox).iterator().hasNext()) return false;
        mc.player.getVehicle().setPosition(mc.player.getVehicle().getPos().add(0, amount, 0));
        return true;
    }

    private int verticalMoveCooldown = 0;

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (verticalMoveCooldown > 0) verticalMoveCooldown--;
        if (mc.world == null || mc.player.getVehicle() == null || mc.player.getVehicle().getControllingPassenger() != mc.player) return;
        long t = mc.world.getTime();
        if (t % 10 == 2) {
            moveHorizontally(FALL_SPEED / 2);
        }
        float multiplier = verticalMoveCooldown > 0 ? 11 : 1;
        moveHorizontally(-(FALL_SPEED / 20) * multiplier);
    }
    @EventHandler
    private void onBoatMove(BoatMoveEvent event) {
        if (event.boat.getControllingPassenger() != mc.player) return;
        boolean useTimer = mc.player.input.getMovementInput().lengthSquared() != 0 && !(mc.options.jumpKey.isPressed() || mc.options.sprintKey.isPressed());
        Modules.get().get(Timer.class).setOverride(useTimer ? timer.get() : Timer.OFF);
        long t = mc.world.getTime();
        event.boat.setYaw(mc.player.getYaw());

        // Horizontal movement
        Vec3d vel = PlayerUtils.getHorizontalVelocity(speed.get() * 5);
        double velX = t % 5 == 0 ? vel.getX() : 0;
        double velY = 0;
        double velZ = t % 5 == 0 ? vel.getZ() : 0;

        // Vertical movement
        if (mc.options.jumpKey.isPressed() && verticalMoveCooldown <= 0 && t % 5 != 0) {
            velY += upwardSpeed.get() / 2.5;
            verticalMoveCooldown = 8;
        }

        if (mc.options.sprintKey.isPressed() && t % 5 != 0) velY -= downwardSpeed.get() / 20 * 1.2;

        // Apply velocity
        Vec3d boatPosAfter = event.boat.getPos().add(velX, velY, velZ);
        ChunkPos cp = new ChunkPos(new BlockPos((int) boatPosAfter.x, (int) boatPosAfter.y, (int) boatPosAfter.z));
        if (mc.world.getChunkManager().isChunkLoaded(cp.x, cp.z)) {
            ((IVec3d) event.boat.getVelocity()).set(velX, velY, velZ);
        }
    }
}
