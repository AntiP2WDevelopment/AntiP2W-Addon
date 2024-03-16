package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.network.packet.Packet;

import java.util.LinkedList;
import java.util.Queue;

public class DelayPackets extends Module {
    private final Queue<Packet<?>> delayedPackets = new LinkedList<>();

    public DelayPackets() {
        super(Categories.DEFAULT, "delay-packets", "Delays C2S packets when turned on.");
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onSendPacket(PacketEvent.Send event) {
        delayedPackets.add(event.packet);
        event.cancel();
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        toggle();
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen instanceof DisconnectedScreen) {
            toggle();
        }
    }

    @Override
    public void onDeactivate() {
        while (!delayedPackets.isEmpty()) {
            Packet<?> packet = delayedPackets.poll();
            mc.getNetworkHandler().sendPacket(packet);
        }
    }
}
