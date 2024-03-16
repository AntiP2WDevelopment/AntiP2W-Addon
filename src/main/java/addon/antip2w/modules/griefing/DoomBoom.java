package addon.antip2w.modules.griefing;

import addon.antip2w.modules.Categories;
import addon.antip2w.utils.CreativeUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;

public class DoomBoom extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> creeper = sgGeneral.add(new BoolSetting.Builder()
        .name("creeper")
        .description("use creepers instead of tnts")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> strength = sgGeneral.add(new IntSetting.Builder()
        .name("strength")
        .description("of the explosion")
        .defaultValue(10)
        .range(1, 127)
        .sliderRange(1, 127)
        .visible(creeper::get)
        .build()
    );

    private final Setting<Integer> rate = sgGeneral.add(new IntSetting.Builder()
        .name("rate")
        .description("of doom")
        .defaultValue(1)
        .range(1, 100)
        .sliderRange(1, 100)
        .build()
    );

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("of the apocalypse")
        .defaultValue(100)
        .range(1, 200)
        .sliderRange(1, 200)
        .build()
    );

    public DoomBoom() {
        super(Categories.GRIEF, "doom-boom", "Obliterates nearby terrain");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.player.getAbilities().creativeMode) return;
        ItemStack lastStack = mc.player.getMainHandStack();
        for (int i = 0; i < rate.get(); i++) {
            double x = mc.player.getX() + range.get() - range.get() * 2 * mc.player.getRandom().nextFloat();
            double z = mc.player.getZ() + range.get() - range.get() * 2 * mc.player.getRandom().nextFloat();
            if (!mc.world.getChunkManager().isChunkLoaded(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z))) return;
            double y = mc.world.getTopY(Heightmap.Type.WORLD_SURFACE, MathHelper.floor(x), MathHelper.floor(z)) - 1;
            String nbt;
            if (creeper.get()) nbt = "{EntityTag:{id:\"minecraft:creeper\",Pos:[" + x + "," + y + "," + z + "],Fuse:0,ignited:1b,Health:4206969f,ExplosionRadius:" + strength.get() + "b}}";
            else nbt = "{EntityTag:{id:\"minecraft:tnt\",Pos:[" + x + "," + y + "," + z + "],fuse:0}}";
            CreativeUtils.giveItemWithNbtToSelectedSlot(mc, Items.STRIDER_SPAWN_EGG, nbt, null);
            BlockHitResult bhr = new BlockHitResult(mc.player.getPos().add(0, 1, 0), Direction.UP, new BlockPos(mc.player.getBlockPos().add(0, 1, 0)), false);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
        }
        mc.interactionManager.clickCreativeStack(lastStack, 36 + mc.player.getInventory().selectedSlot);
    }
}
