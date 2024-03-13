package addon.antip2w.commands;

import addon.antip2w.AntiP2W;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class PlayersCommand extends Command {

    public PlayersCommand() {
        super("players", "Gets all the players and counts it.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("Getting Players.... (With ignore self)");

            List<String> playerNames = Objects.requireNonNull(AntiP2W.MC.getNetworkHandler()).getPlayerList().stream()
                .map(entry -> entry.getProfile().getName())
                .filter(name -> !name.equals(AntiP2W.MC.player.getGameProfile().getName()))
                .collect(Collectors.toList());

            String playerList = String.join(", ", playerNames);

            info("Number of players: %d", playerNames.size());
            info("Players: %s", playerList );

            return SINGLE_SUCCESS;
        });
    }
}
