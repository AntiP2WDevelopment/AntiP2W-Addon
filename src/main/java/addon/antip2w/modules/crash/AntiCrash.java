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

public class AntiCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
        .name("log")
        .description("Logs when a crash packet is detected.")
        .defaultValue(false)
        .build()
    );

    public AntiCrash() {
        super(Categories.DEFAULT, "anti-crash", "Attempts to cancel packets that may crash the client.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof ExplosionS2CPacket packet && isInvalid(packet)) {
            cancel(event, "invalid explosion");
        } else if (event.packet instanceof ParticleS2CPacket packet && isInvalid(packet)) {
            cancel(event, "invalid particles");
        } else if (event.packet instanceof PlayerPositionLookS2CPacket packet && isInvalid(packet)) {
            cancel(event, "invalid movement");
        } else if (event.packet instanceof EntityVelocityUpdateS2CPacket packet && isInvalid(packet)) {
            cancel(event, "invalid velocity update");
        }
    }

    private boolean isInvalid(ExplosionS2CPacket packet) {
        return packet.getX() > 30_000_000 ||
            packet.getY() > 30_000_000 ||
            packet.getZ() > 30_000_000 ||
            packet.getX() < -30_000_000 ||
            packet.getY() < -30_000_000 ||
            packet.getZ() < -30_000_000 ||
            packet.getRadius() > 1000 ||
            packet.getAffectedBlocks().size() > 100_000 ||
            packet.getPlayerVelocityX() > 30_000_000 ||
            packet.getPlayerVelocityY() > 30_000_000 ||
            packet.getPlayerVelocityZ() > 30_000_000 ||
            packet.getPlayerVelocityX() < -30_000_000 ||
            packet.getPlayerVelocityY() < -30_000_000 ||
            packet.getPlayerVelocityZ() < -30_000_000;
    }

    private boolean isInvalid(ParticleS2CPacket packet) {
        return packet.getCount() > 100_000 ||
            packet.getSpeed() > 100_000;
    }

    private boolean isInvalid(PlayerPositionLookS2CPacket packet) {
        return packet.getX() > 30_000_000 ||
            packet.getY() > 30_000_000 ||
            packet.getZ() > 30_000_000 ||
            packet.getX() < -30_000_000 ||
            packet.getY() < -30_000_000 ||
            packet.getZ() < -30_000_000;
    }

    private boolean isInvalid(EntityVelocityUpdateS2CPacket packet) {
        return packet.getVelocityX() > 30_000_000 ||
            packet.getVelocityY() > 30_000_000 ||
            packet.getVelocityZ() > 30_000_000 ||
            packet.getVelocityX() < -30_000_000 ||
            packet.getVelocityY() < -30_000_000 ||
            packet.getVelocityZ() < -30_000_000;
    }

    private void cancel(PacketEvent.Receive event, String reason) {
        if (log.get()) ChatUtils.info("Server attempted to crash you: " + reason);
        event.cancel();
    }
}
