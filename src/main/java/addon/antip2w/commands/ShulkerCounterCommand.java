package addon.antip2w.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;

import java.util.List;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

@SuppressWarnings("ALL")
public class ShulkerCounterCommand extends Command {
    private static final List<Item> SHULKERS = List.of(
        Items.SHULKER_BOX,
        Items.WHITE_SHULKER_BOX,
        Items.ORANGE_SHULKER_BOX,
        Items.MAGENTA_SHULKER_BOX,
        Items.LIGHT_BLUE_SHULKER_BOX,
        Items.YELLOW_SHULKER_BOX,
        Items.LIME_SHULKER_BOX,
        Items.PINK_SHULKER_BOX,
        Items.GRAY_SHULKER_BOX,
        Items.LIGHT_GRAY_SHULKER_BOX,
        Items.CYAN_SHULKER_BOX,
        Items.PURPLE_SHULKER_BOX,
        Items.BLUE_SHULKER_BOX,
        Items.BROWN_SHULKER_BOX,
        Items.GREEN_SHULKER_BOX,
        Items.RED_SHULKER_BOX,
        Items.BLACK_SHULKER_BOX);

    public ShulkerCounterCommand() {
        super("shulker-counter", "Counts every shulker item on the ground.");
    }
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            int count = 0;
            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof ItemEntity item) || !SHULKERS.contains(item.getStack().getItem())) continue;
                count += 1;
            }
            info("There are " + Formatting.WHITE + count + " shulkers on the ground.");
            return SINGLE_SUCCESS;
        });
    }
}
