package addon.antip2w.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

//Credit to the MeteorRejects addon//

public class DisconnectCommand extends Command {
    public DisconnectCommand() {
        super("disconnect", "disconnects you from the server");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            mc.player.networkHandler.getConnection().disconnect(Text.literal("Are you surprised? You did it..."));
            return SINGLE_SUCCESS;
        });
    }
}
