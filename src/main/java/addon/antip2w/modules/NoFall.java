package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    private static double lastY = 0;
    private static boolean shouldCatch = false;

    public NoFall() {
        super(Categories.DEFAULT, "NoFall", "Universal NoFall (works on almost any anticheat).");
    }


    @EventHandler
    public void preMovement(SendMovementPacketsEvent.Pre event) {
        if (mc.player.fallDistance == 0 && !mc.player.isTouchingWater() && mc.player.isOnGround() && shouldCatch) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), lastY+0.01, mc.player.getZ(), false));
            shouldCatch = false;
        } else if (mc.player.fallDistance > 3) {
            shouldCatch = true;
        }
        lastY = mc.player.getY();
    }
}
