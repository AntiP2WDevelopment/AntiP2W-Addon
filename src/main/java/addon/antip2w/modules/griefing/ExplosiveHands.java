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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ExplosiveHands extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> creeper = sgGeneral.add(new BoolSetting.Builder()
        .name("creeper")
        .description("use creepers instead of tnts")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> strength = sgGeneral.add(new IntSetting.Builder()
        .name("Strength")
        .description("of the explosion")
        .defaultValue(10)
        .range(1, 127)
        .sliderRange(1, 127)
        .visible(creeper::get)
        .build());

    public ExplosiveHands() {
        super(Categories.GRIEF, "explosive-hands", "Makes your hands explosive (requires creative mode)");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.options.attackKey.isPressed() || mc.currentScreen != null || !mc.player.getAbilities().creativeMode) return;
        HitResult hitResult = mc.cameraEntity.raycast(900, 0, false);
        if (hitResult.getType() == HitResult.Type.MISS) return;
        Vec3d p = hitResult.getPos();
        String nbt;
        if (creeper.get()) nbt = "{EntityTag:{id:\"minecraft:creeper\",Pos:[" + p.x + "," + (p.y - 1) + "," + p.z + "],Fuse:0,ignited:1b,Health:4206969f,ExplosionRadius:" + strength.get() + "b}}";
        else nbt = "{EntityTag:{id:\"minecraft:tnt\",Pos:[" + p.x + "," + p.y + "," + p.z + "],fuse:0}}";
        ItemStack lastStack = mc.player.getMainHandStack();
        CreativeUtils.giveItemWithNbtToSelectedSlot(mc, Items.STRIDER_SPAWN_EGG, nbt, null);
        BlockHitResult bhr = new BlockHitResult(mc.player.getPos().add(0, 1, 0), Direction.UP, new BlockPos(mc.player.getBlockPos().add(0, 1, 0)), false);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
        mc.interactionManager.clickCreativeStack(lastStack, 36 + mc.player.getInventory().selectedSlot);
    }
}
