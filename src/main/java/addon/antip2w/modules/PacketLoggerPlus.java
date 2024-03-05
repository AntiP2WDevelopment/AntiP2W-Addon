package addon.antip2w.modules;

import addon.antip2w.AntiP2W;
import io.netty.buffer.Unpooled;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.PacketListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.PacketUtils;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

public class PacketLoggerPlus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Set<Class<? extends Packet<?>>>> RXFilter = sgGeneral.add(new PacketListSetting.Builder()
        .name("RX")
        .description("Server-to-client.")
        .filter(aClass -> PacketUtils.getS2CPackets().contains(aClass))
        .build()
    );

    private final Setting<Set<Class<? extends Packet<?>>>> TXFilter = sgGeneral.add(new PacketListSetting.Builder()
        .name("TX")
        .description("Client-to-server")
        .filter(aClass -> PacketUtils.getC2SPackets().contains(aClass))
        .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
        .name("log")
        .description("Log to the log")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> logToChat = sgGeneral.add(new BoolSetting.Builder()
        .name("log-to-chat")
        .description("Log to chat")
        .defaultValue(true)
        .build()
    );


    private final Setting<Boolean> logBigPackets = sgGeneral.add(new BoolSetting.Builder()
        .name("log-big-packets")
        .description("If enabled it can cause massive lag spikes")
        .defaultValue(false)
        .build()
    );

    public PacketLoggerPlus() {
        super(Categories.WIP, "Packet Logger Plus", "Logs packets.");
    }

    public void onPacket(Packet<?> packet, NetworkSide side) {
        if(!isActive() || side == NetworkSide.SERVERBOUND /* side check for singleplayer */) return;
        if(packet instanceof BundleS2CPacket p) {
            p.getPackets().forEach(packet1 -> onPacket(packet1, side));
            return;
        }
        boolean clientBound = RXFilter.get().contains(packet.getClass());
        if (!(clientBound || TXFilter.get().contains(packet.getClass()))) return;
        Pair<String, Text> printMessages = getPrintMessages(packet, clientBound);
        print(printMessages.getLeft(), printMessages.getRight(), log.get(), logToChat.get());
    }

    private void print(String s, Text t, boolean log, boolean toChat) {
        if(log) AntiP2W.LOG.info(s);
        if(toChat) mc.inGameHud.getChatHud().addMessage(t, null, mc.inGameHud.getTicks(), null, false);
    }

    private Pair<String, Text> getPrintMessages(Packet<?> packet, boolean clientBound) {
        String prefix = clientBound ? "RX: " : "TX: ";
        String packetName = PacketUtils.getName((Class<? extends Packet<?>>) packet.getClass());
        PacketByteBuf payloadBuffer = new PacketByteBuf(Unpooled.buffer());
        packet.write(payloadBuffer);
        byte[] bytes = new byte[payloadBuffer.readableBytes()];
        payloadBuffer.readBytes(bytes, 0, payloadBuffer.readableBytes());

        String logMessage = prefix + packetName + " data: ";
        if(bytes.length < 1024 || logBigPackets.get()) {
            logMessage += new String(bytes, StandardCharsets.US_ASCII);
        } else {
            logMessage += " <payload too big>";
        }

        String firstBytesASCII = new String(Arrays.copyOfRange(bytes, 0, Math.min(bytes.length, 255)), StandardCharsets.US_ASCII).replaceAll("[^\\x20-\\x7e]", "?");
        String chatMessage = prefix + packetName + " | data[0:min(len, 255)]: " + firstBytesASCII;
        String bytesBase64 = Base64.getEncoder().encodeToString(bytes);
        Text text = Text.literal(chatMessage).styled((style) -> style
            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, bytesBase64))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("Click to copy Base 64 data")))
        );
        return new Pair<>(logMessage, text);
    }


}
