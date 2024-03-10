package addon.antip2w.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;

public class CreativeUtils {
    public static void giveItemWithNbtToEmptySlot(MinecraftClient mc, Item item, String nbt, Text customName) {
        ItemStack stack = item.getDefaultStack();
        try {
            stack.setNbt(StringNbtReader.parse(nbt));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        stack.setCustomName(customName);
        if (mc.player.getMainHandStack().isEmpty())
            mc.interactionManager.clickCreativeStack(stack, 36 + mc.player.getInventory().selectedSlot);
        else {
            int nextEmptySlot = mc.player.getInventory().getEmptySlot();
            if (nextEmptySlot < 9) mc.interactionManager.clickCreativeStack(stack, 36 + nextEmptySlot);
            else
                mc.interactionManager.clickCreativeStack(stack, 36 + mc.player.getInventory().selectedSlot);
        }
    }

    public static void giveItemWithNbtToSelectedSlot(MinecraftClient mc, Item item, String nbt, Text customName) {
        ItemStack stack = item.getDefaultStack();
        try {
            stack.setNbt(StringNbtReader.parse(nbt));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        stack.setCustomName(customName);
        mc.interactionManager.clickCreativeStack(stack, 36 + mc.player.getInventory().selectedSlot);
    }
}
