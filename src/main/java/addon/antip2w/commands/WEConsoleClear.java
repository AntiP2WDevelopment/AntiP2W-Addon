package addon.antip2w.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;

public class WEConsoleClear extends Command {
    public WEConsoleClear() {
        super("weconsoleclear", "clears the console of the server running worldedit");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            if(MinecraftClient.getInstance().getNetworkHandler() != null)
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(new CustomPayload() {
                    @Override
                    public void write(PacketByteBuf buf) {
                        buf.writeBytes("v|\033\143".getBytes(StandardCharsets.UTF_8));
                    }

                    @Override
                    public Identifier id() {
                        return new Identifier("worldedit", "cui");
                    }
                }));
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });
    }
}
