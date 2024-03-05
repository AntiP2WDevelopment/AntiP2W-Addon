package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;

import java.util.List;


public class NoBlockEntities extends Module {
    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder()
            .name("render-radius")
            .description("The radius in which the blocks will render.")
            .defaultValue(0)
            .min(0)
            .sliderMax(128)
            .build()
    );

    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
            .name("blocks")
            .description("The blocks not to render.")
            .defaultValue(
                    Blocks.CHEST,
                    Blocks.TRAPPED_CHEST,
                    Blocks.SHULKER_BOX,
                    Blocks.RED_SHULKER_BOX,
                    Blocks.BLUE_SHULKER_BOX,
                    Blocks.YELLOW_SHULKER_BOX,
                    Blocks.GRAY_SHULKER_BOX,
                    Blocks.LIGHT_GRAY_SHULKER_BOX,
                    Blocks.CYAN_SHULKER_BOX,
                    Blocks.BROWN_SHULKER_BOX,
                    Blocks.BLACK_SHULKER_BOX,
                    Blocks.GREEN_SHULKER_BOX,
                    Blocks.LIGHT_BLUE_SHULKER_BOX,
                    Blocks.LIME_SHULKER_BOX,
                    Blocks.PINK_SHULKER_BOX,
                    Blocks.MAGENTA_SHULKER_BOX,
                    Blocks.PURPLE_SHULKER_BOX,
                    Blocks.WHITE_SHULKER_BOX,
                    Blocks.ORANGE_SHULKER_BOX
            )
            .filter(NoBlockEntities::isBlockEntity)
            .build()
    );

    public NoBlockEntities() {
        super(Categories.DEFAULT, "no-block-entities", "Disables rendering for specified block entities.");
    }

    @EventHandler
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        BlockEntity block = event.blockEntity;

        if (PlayerUtils.squaredDistanceTo(block.getPos()) < radius.get()*radius.get()) return;

        if (blocks.get().contains(block.getCachedState().getBlock())) {
            event.cancel();
        }
    }

    private static boolean isBlockEntity(Block block) {
        return block instanceof BlockWithEntity;
    }
}
