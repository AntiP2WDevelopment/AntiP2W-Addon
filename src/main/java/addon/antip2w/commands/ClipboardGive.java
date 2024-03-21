package addon.antip2w.commands;

import addon.antip2w.AntiP2W;
import addon.antip2w.utils.CreativeUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ClipboardGive extends Command {
    public ClipboardGive() {
        super("clipboardgive", "Gives an item from a copied give command", "clipgive", "giveclip");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            try {
                String clipboard = AntiP2W.MC.keyboard.getClipboard();
                if (!clipboard.startsWith("/give")) {
                    ChatUtils.warning("Clipboard content is not a give command");
                    return SINGLE_SUCCESS;
                }
                String command = clipboard.substring(clipboard.indexOf(" ", 6) + 1);
                Item item = getItem(command);
                String nbt = getNbtString(command);
                ChatUtils.info(String.valueOf(nbt));
                int count = getCount(command);

                CreativeUtils.giveItemWithNbtToEmptySlot(item, nbt, null, count);

                return SINGLE_SUCCESS;
            } catch (Exception e) {
                ChatUtils.warning("Give command is malformed");
                return SINGLE_SUCCESS;
            }
        });
    }

    private static Item getItem(String command) {
        boolean hasNbt = command.contains("{");
        String identifierString;
        if (hasNbt) identifierString = command.substring(0, command.indexOf("{"));
        else identifierString = command.substring(0, command.indexOf(" "));
        return Registries.ITEM.get(new Identifier(identifierString));
    }

    private static String getNbtString(String command) {
        boolean hasNbt = command.contains("{");
        if (!hasNbt) return null;
        return command.substring(command.indexOf("{"), command.lastIndexOf("}") + 1);
    }

    private static int getCount(String command) {
        if (command.substring(command.lastIndexOf("}")).isBlank()) return 1;
        return MathHelper.clamp(Integer.parseInt(command.substring(command.lastIndexOf("} ") + 2)), 1, 64);
    }
}
