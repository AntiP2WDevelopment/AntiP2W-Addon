package addon.antip2w.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class HotbarClearCommand extends Command {
    public HotbarClearCommand() {
        super("hotbar-clear", "Clears hotbar. (not Clientside)");
    }
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = mc.player.getInventory().getStack(i);
                if (itemStack != null) {
                    mc.interactionManager.clickSlot(0, invIndexToSlotId(i), 300, SlotActionType.SWAP, mc.player);
                }
            }
            return SINGLE_SUCCESS;
        });
    }
    public static int invIndexToSlotId(int invIndex) {
        return invIndex < 9 && invIndex != -1 ? 44 - (8 - invIndex) : invIndex;
    }
}
