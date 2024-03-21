package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;

import java.util.concurrent.CompletableFuture;

public class PingSpoof extends Module {
    private final SettingGroup settingsGeneral = settings.getDefaultGroup();

    private final Setting<Integer> desiredPing = settingsGeneral.add(new IntSetting.Builder()
        .name("Ping")
        .description("The desired ping")
        .defaultValue(6969)
        .range(0, 10000)
        .sliderRange(0, 10000)
        .build()
    );

    private final Setting<Integer> avgPing = settingsGeneral.add(new IntSetting.Builder()
        .name("Average Ping")
        .description("Your average ping with the module disabled")
        .defaultValue(50)
        .range(0, 10000)
        .sliderRange(0, 10000)
        .build()
    );

    private final Setting<Mode> mode = settingsGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The mode")
        .defaultValue(Mode.FAKE)
        .build()
    );

    private final Setting<Boolean> delayKeepAlive = settingsGeneral.add(new BoolSetting.Builder()
        .name("delay-keep-alive")
        .description("Delay Keep Alive packets.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.FAKE)
        .build()
    );

    private final Setting<Boolean> delayPong = settingsGeneral.add(new BoolSetting.Builder()
        .name("delay-pong")
        .description("Delay Pong / Transaction packets.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.FAKE)
        .build()
    );

    public PingSpoof() {
        super(Categories.DEFAULT, "Ping Spoof", "Increases your ping.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (mode.get() == Mode.FAKE) {
            if (event.packet instanceof KeepAliveC2SPacket p && delayKeepAlive.get()) {
                event.cancel();
                sendLater(new KeepAliveC2SPacket(p.getId()));
            } else if (event.packet instanceof CommonPongC2SPacket p && delayPong.get()) {
                event.cancel();
                sendLater(new CommonPongC2SPacket(p.getParameter()));
            }
        } else {
            event.cancel();
            sendLater(event.packet);
        }
    }

    private void sendLater(Packet<?> packet) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(desiredPing.get() - avgPing.get());
            } catch (InterruptedException ignored) {
            }
            if (mc.getNetworkHandler() == null) return;
            mc.getNetworkHandler().getConnection().send(packet, null); // bypass Meteor onSendPacket event
        });
    }

    private enum Mode {
        FAKE,
        LEGIT
    }
}
