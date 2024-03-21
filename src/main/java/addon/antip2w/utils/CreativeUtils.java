package addon.antip2w.utils;

import addon.antip2w.AntiP2W;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class CreativeUtils {
    public static void giveItemWithNbtToEmptySlot(Item item, @Nullable String nbt, @Nullable Text customName, int count) {
        ItemStack stack = item.getDefaultStack();
        stack.setCount(count);
        if (nbt != null) {
            try {
                stack.setNbt(StringNbtReader.parse(nbt));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        if(customName != null) stack.setCustomName(customName);
        if (AntiP2W.MC.player.getMainHandStack().isEmpty())
            AntiP2W.MC.interactionManager.clickCreativeStack(stack, 36 + AntiP2W.MC.player.getInventory().selectedSlot);
        else {
            int nextEmptySlot = AntiP2W.MC.player.getInventory().getEmptySlot();
            if (nextEmptySlot < 9) AntiP2W.MC.interactionManager.clickCreativeStack(stack, 36 + nextEmptySlot);
            else
                AntiP2W.MC.interactionManager.clickCreativeStack(stack, 36 + AntiP2W.MC.player.getInventory().selectedSlot);
        }
    }

    public static void giveItemWithNbtToSelectedSlot(Item item, @Nullable String nbt, @Nullable Text customName, int count) {
        ItemStack stack = item.getDefaultStack();
        stack.setCount(count);
        if (nbt != null) {
            try {
                stack.setNbt(StringNbtReader.parse(nbt));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        if(customName != null) stack.setCustomName(customName);
        AntiP2W.MC.interactionManager.clickCreativeStack(stack, 36 + AntiP2W.MC.player.getInventory().selectedSlot);
    }
}
