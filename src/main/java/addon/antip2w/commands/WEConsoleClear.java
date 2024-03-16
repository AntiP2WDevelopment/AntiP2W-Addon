package addon.antip2w.commands;

import addon.antip2w.utils.MCUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class WEConsoleClear extends Command {
    public WEConsoleClear() {
        super("weconsoleclear", "clears the console of the server running worldedit");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            MCUtil.sendPacket(MCUtil.createCustomPayloadPacket(
                buf -> buf.writeBytes("v|\033\143".getBytes(StandardCharsets.UTF_8)),
                new Identifier("worldedit", "cui")
            ));
            return SINGLE_SUCCESS;
        });
    }
}
