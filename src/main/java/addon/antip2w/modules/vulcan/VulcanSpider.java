package addon.antip2w.modules.vulcan;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class VulcanSpider extends Module {
    public static boolean spoofOnGround = false;

    public VulcanSpider() {
        super(Categories.DEFAULT, "Vulcan Spider", "Hello! I am a spider.");
    }

    @EventHandler
    public void onMove(SendMovementPacketsEvent.Pre event) {
        if (mc.player.horizontalCollision && !mc.player.isClimbing()) {
            spoofOnGround = true;
            double dy = 0.2 + Math.random() * 0.001;
            // can't go up any further
            if (mc.player.clientWorld.getBlockCollisions(null, mc.player.getBoundingBox().offset(0, dy, 0)).iterator().hasNext()) return;
            mc.player.setPosition(mc.player.getPos().add(0, dy, 0));
            mc.player.setVelocity(mc.player.getVelocity().x, 0, mc.player.getVelocity().z);
        } else spoofOnGround = false;
    }

    @EventHandler
    public void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof PlayerMoveC2SPacket p)
            if (spoofOnGround) ((PlayerMoveC2SPacketAccessor) p).setOnGround(true);
    }
}
