package addon.antip2w.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayersCommand extends Command {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public PlayersCommand() {
        super("players", "Gets all the players and counts it.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("Getting Players.... (With ignore self)");

            List<String> playerNames = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerList().stream()
                .map(entry -> entry.getProfile().getName())
                .filter(name -> !name.equals(mc.player.getGameProfile().getName()))
                .collect(Collectors.toList());

            String playerList = String.join(", ", playerNames);

            info("Number of players: %d", playerNames.size());
            info("Players: %s", playerList );

            return SINGLE_SUCCESS;
        });
    }
}
