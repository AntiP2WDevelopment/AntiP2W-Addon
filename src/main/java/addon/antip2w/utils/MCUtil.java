package addon.antip2w.utils;

import addon.antip2w.AntiP2W;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MCUtil {
    public static void sendPacket(Packet<?> packet) {
        AntiP2W.MC.getNetworkHandler().sendPacket(packet);
    }

    public static ClientPlayNetworkHandler networkHandler() {
        return AntiP2W.MC.getNetworkHandler();
    }

    public static void disconnect(String reason) {
        disconnect(Text.of(reason));
    }

    public static void disconnect(Text reason) {
        networkHandler().getConnection().disconnect(reason);
    }

    public static CustomPayloadC2SPacket createCustomPayloadPacket(Consumer<PacketByteBuf> consumer, Identifier id) {
        return new CustomPayloadC2SPacket(new CustomPayload() {
            @Override
            public void write(PacketByteBuf buf) {
                consumer.accept(buf);
            }

            @Override
            public Identifier id() {
                return id;
            }
        });
    }
}
