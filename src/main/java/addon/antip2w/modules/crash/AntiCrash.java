package addon.antip2w.modules.crash;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

//thanks meteor rejects
public class AntiCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
        .name("log")
        .description("Logs when crash packet detected.")
        .defaultValue(false)
        .build()
    );

    public AntiCrash() {
        super(Categories.DEFAULT, "anti-crash", "Attempts to cancel packets that may crash the client.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof ExplosionS2CPacket packet) {
            if (/* outside of world */ packet.getX() > 30_000_000 || packet.getY() > 30_000_000 || packet.getZ() > 30_000_000 || packet.getX() < -30_000_000 || packet.getY() < -30_000_000 || packet.getZ() < -30_000_000 ||
                // power too high
                packet.getRadius() > 1000 ||
                // too many blocks
                packet.getAffectedBlocks().size() > 100_000 ||
                // too much knockback
                packet.getPlayerVelocityX() > 30_000_000 || packet.getPlayerVelocityY() > 30_000_000 || packet.getPlayerVelocityZ() > 30_000_000
                // knockback can be negative?
                || packet.getPlayerVelocityX() < -30_000_000 || packet.getPlayerVelocityY() < -30_000_000 || packet.getPlayerVelocityZ() < -30_000_000
            ) cancel(event, "invalid explosion");
        } else if (event.packet instanceof ParticleS2CPacket packet) {
            // too many particles
            if (packet.getCount() > 100_000) cancel(event, "too many particles");
            // too fast particles
            else if(packet.getSpeed() > 100_000) cancel(event, "too fast particles");
        } else if (event.packet instanceof PlayerPositionLookS2CPacket packet) {
            // out of world movement
            if (packet.getX() > 30_000_000 || packet.getY() > 30_000_000 || packet.getZ() > 30_000_000 || packet.getX() < -30_000_000 || packet.getY() < -30_000_000 || packet.getZ() < -30_000_000)
                cancel(event, "out of world movement");
        } else if (event.packet instanceof EntityVelocityUpdateS2CPacket packet) {
            // velocity
            if (packet.getVelocityX() > 30_000_000 || packet.getVelocityY() > 30_000_000 || packet.getVelocityZ() > 30_000_000
                || packet.getVelocityX() < -30_000_000 || packet.getVelocityY() < -30_000_000 || packet.getVelocityZ() < -30_000_000
            ) cancel(event, "entity velocity too big");
        }
    }

    private void cancel(PacketEvent.Receive event, String reason) {
        if (log.get()) ChatUtils.info("Server attempted to crash you: " + reason);
        event.cancel();
    }
}
