package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import addon.antip2w.utils.MCUtil;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CreativePuke extends Module {
    private final SettingGroup settingsGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> randomItem = settingsGeneral.add(new BoolSetting.Builder()
        .name("Random Item")
        .description("Use a random item.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Item> item = settingsGeneral.add(new ItemSetting.Builder()
        .name("Item")
        .description("The item.")
        .defaultValue(Items.ANCIENT_DEBRIS)
        .visible(() -> !randomItem.get())
        .build()
    );

    private final Setting<Boolean> applyGlint = settingsGeneral.add(new BoolSetting.Builder()
        .name("Apply Glint")
        .description("Apply a layer of glint to the item to make the client lag more.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> pack = settingsGeneral.add(new BoolSetting.Builder()
        .name("Pack")
        .description("Pack the items in a Shulker Box. Good for crashing servers")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> rate = settingsGeneral.add(new IntSetting.Builder()
        .name("Rate")
        .description("The amount of items to puke per tick")
        .defaultValue(1)
        .sliderRange(1, 20)
        .range(1, 20)
        .build()
    );

    public CreativePuke() {
        super(Categories.FUNNY, "Creative Puke", "Pukes items out of your mouth.");
    }

    private ItemStack pack(ItemStack stack) {
        ItemStack box = Items.SHULKER_BOX.getDefaultStack();
        NbtCompound boxNbt = new NbtCompound();
        NbtCompound blockEntityTag = new NbtCompound();
        boxNbt.put("BlockEntityTag", blockEntityTag);
        NbtList entries = new NbtList();
        blockEntityTag.put("Items", entries);
        for (byte i = 0; i < 27; i++) {
            NbtCompound entry = new NbtCompound();
            entry.putByte("Slot", i);
            entry.putString("id", Registries.ITEM.getId(stack.getItem()).toString());
            entry.putByte("Count", (byte) stack.getCount());
            if(stack.hasNbt()) entry.put("tag", stack.getNbt());
            entries.add(entry);
        }
        box.setNbt(boxNbt);
        return box;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if(!mc.player.isCreative()) return;
        int selectedSlot = mc.player.getInventory().selectedSlot;
        for (int i = 0; i < rate.get(); i++) {
            ItemStack stack;
            if(randomItem.get())
                stack = new ItemStack(Registries.ITEM.getRandom(mc.player.getRandom()).get().value(), 64);
            else stack = new ItemStack(item.get(), 64);
            if(applyGlint.get()) {
                NbtList real = new NbtList();
                real.add(new NbtCompound());
                stack.setSubNbt("Enchantments", real);
            }
            if(pack.get()) {
                stack = pack(stack);
            }
            MCUtil.sendPacket(new CreativeInventoryActionC2SPacket(36 + selectedSlot, stack));
            MCUtil.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, Direction.DOWN));
        }
    }
}
