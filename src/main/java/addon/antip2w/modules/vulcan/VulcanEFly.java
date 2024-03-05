package addon.antip2w.modules.vulcan;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.ClientPlayerEntityAccessor;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class VulcanEFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Horizontal speed in blocks per second.")
        .defaultValue(20)
        .range(0, 69.9)
        .sliderMax(69.9)
        .build()
    );

    private final Setting<Double> vertSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("vertical-speed")
        .description("Vertical speed in blocks per second.")
        .defaultValue(20)
        .range(0, 69.9)
        .sliderMax(69.9)
        .build()
    );

    public VulcanEFly() {
        super(Categories.DEFAULT, "Vulcan EFly", "A Redbull szááárnyakat ad!");
    }

    @Override
    public void onDeactivate() {
        mc.player.getAbilities().allowFlying = false;
        mc.player.getAbilities().flying = false;
    }

    // don't clip into blocks
    private boolean canMoveHorizontally(double amount) {
        Box boundingBox = mc.player.getBoundingBox();
        boundingBox = boundingBox.offset(0, amount, 0).union(boundingBox);
        if (mc.world.getBlockCollisions(null, boundingBox).iterator().hasNext()) return false;
        return true;
    }

    private Vec3d jesse_we_need_to_cook = Vec3d.ZERO;

    @EventHandler
    private void postTick(TickEvent.Post event) {
        jesse_we_need_to_cook = Vec3d.ZERO;
        mc.player.getAbilities().allowFlying = true;
        if(!mc.player.getAbilities().flying) return;
        Vec3d vel = PlayerUtils.getHorizontalVelocity(speed.get() - 0.001);
        double vely = 0;
        double t = mc.world.getTime();
        if (mc.options.jumpKey.isPressed()) vely += (vertSpeed.get() + 0.001) / 20;
        if (mc.options.sneakKey.isPressed()) vely -= (vertSpeed.get() + 0.001) / 20;
        if (t % 20 == 0) if (canMoveHorizontally(-0.035)) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.035, mc.player.getZ(), false));
            ((ClientPlayerEntityAccessor) mc.player).setTicksSinceLastPositionPacketSent(19);
        }
        jesse_we_need_to_cook = vel.add(0, vely, 0);
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        if(mc.player.getAbilities().flying) ((IVec3d)mc.player.getVelocity()).set(jesse_we_need_to_cook.x, jesse_we_need_to_cook.y, jesse_we_need_to_cook.z);
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof UpdatePlayerAbilitiesC2SPacket) event.cancel();
    }
}
