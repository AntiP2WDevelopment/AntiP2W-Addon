package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.network.packet.s2c.play.*;

public class AntiFunny extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
        .name("log")
        .description("Logs when a funny packet is detected.")
        .defaultValue(false)
        .build()
    );

    public AntiFunny() {
        super(Categories.DEFAULT, "anti-funny", "Cancels funny packets that may freeze your game");
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
        } else if (event.packet instanceof GameStateChangeS2CPacket packet && isInvalid(packet)) {
            cancel(event, "invalid game state change");
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

    private boolean isInvalid(GameStateChangeS2CPacket packet) {
        return packet.getReason() == GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN;
    }

    private void cancel(PacketEvent.Receive event, String reason) {
        if (log.get()) ChatUtils.info("Server sent funny packet to you: " + reason);
        event.cancel();
    }
}
