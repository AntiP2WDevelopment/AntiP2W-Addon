package addon.antip2w.commands;

import addon.antip2w.AntiP2W;
import addon.antip2w.irc.IRCHandler;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class IRCCommand extends Command {
    public IRCCommand() {
        super("irc", "chat with other AntiP2W users", "i");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
            argument("message", StringArgumentType.greedyString())
                .executes((ctx) -> {
                    if(!IRCHandler.isRunning()) {
                        ChatUtils.errorPrefix("IRC", "Not running!");
                        return SINGLE_SUCCESS;
                    }

                    String message = StringArgumentType.getString(ctx, "message");
                    message = AntiP2W.MC.getGameProfile().getName() + " >> " + message;

                    IRCHandler.sendAsync(message);
                    ChatUtils.infoPrefix("IRC", message);
                    return SINGLE_SUCCESS;
                })
        );
    }
}
