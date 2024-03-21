package addon.antip2w.commands;

import addon.antip2w.AntiP2W;
import addon.antip2w.commands.argument.CUuidArgumentType;
import addon.antip2w.utils.MCUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerListEntryArgumentType;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;

import java.util.UUID;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SpectatorFunny extends Command {
    public SpectatorFunny() {
        super("spectator-funny", "coordinate exploit");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
            literal("name").then(
                argument("player", PlayerListEntryArgumentType.create())
                    .executes(ctx -> run(PlayerListEntryArgumentType.get(ctx).getProfile().getId()))
            ).then(
                argument("uuid", CUuidArgumentType.create())
                    .executes(ctx -> run(CUuidArgumentType.getUuid(ctx, "uuid")))
            )
        );
    }

    private static int run(UUID uuid) {
        if (!AntiP2W.MC.player.isSpectator()) {
            ChatUtils.warning("Not in spectator");
            return SINGLE_SUCCESS;
        }
        MCUtil.sendPacket(new SpectatorTeleportC2SPacket(uuid));
        return SINGLE_SUCCESS;
    }
}
