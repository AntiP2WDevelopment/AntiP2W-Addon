package addon.antip2w.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.Utils;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ChestCommand extends Command {
    public ChestCommand() {
        super("chest", "Allows you to preview memory of your ender chest.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("before container");
            Utils.openContainer(Items.CHEST.getDefaultStack(), new ItemStack[54], true);
            info("after container");
            return SINGLE_SUCCESS;
        });
    }
}
