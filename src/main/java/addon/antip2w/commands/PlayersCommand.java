package addon.antip2w.commands;

import addon.antip2w.AntiP2W;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class PlayersCommand extends Command {

    public PlayersCommand() {
        super("players", "Gets all players and counts them (without you).");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("Getting Players.... (without you included)");

            UUID selfId = AntiP2W.MC.player.getGameProfile().getId();

            List<String> playerNames = AntiP2W.MC.getNetworkHandler().getPlayerList().stream()
                .filter(entry -> !(entry.getProfile().getId() == selfId))
                .map(entry -> entry.getProfile().getName())
                .collect(Collectors.toList());

            String players = String.join(", ", playerNames);

            info("Number of players: %d", playerNames.size());
            info("Players: %s", players );

            return SINGLE_SUCCESS;
        });
    }
}
