package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.movement.Scaffold.ListMode;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoFloor extends Module {

    private final SettingGroup sgGeneral = settings.createGroup("Main");
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Selected blocks.")
        .build()
    );

    private final Setting<Double> placeRange = sgGeneral.add(new DoubleSetting.Builder()
        .name("closest-block-range")
        .description("How far can scaffold place blocks when you are in air.")
        .defaultValue(4)
        .min(0)
        .sliderMax(8)
        .visible(() -> /*!airPlace.get()*/ false)
        .build()
    );

    private final Setting<ListMode> blocksFilter = sgGeneral.add(new EnumSetting.Builder<ListMode>()
        .name("blocks-filter")
        .description("How to use the block list setting")
        .defaultValue(ListMode.Blacklist)
        .build()
    );

    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder()
        .name("render")
        .description("Whether to render blocks that have been placed.")
        .defaultValue(true)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .visible(render::get)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The side color of the target block rendering.")
        .defaultValue(new SettingColor(197, 137, 232, 10))
        .visible(render::get)
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The line color of the target block rendering.")
        .defaultValue(new SettingColor(197, 137, 232))
        .visible(render::get)
        .build()
    );

    public AutoFloor() {
        super(Categories.WIP, "AutoFloor", "");
    }

    private final BlockPos.Mutable bp = new BlockPos.Mutable();
    private final BlockPos.Mutable prevBp = new BlockPos.Mutable();

    private boolean lastWasSneaking;
    private double lastSneakingY;

    @Override
    public void onActivate() {
        lastWasSneaking = mc.options.sneakKey.isPressed();
        if (lastWasSneaking) lastSneakingY = mc.player.getY();
    }
  

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        Vec3d vec = mc.player.getPos().add(mc.player.getVelocity()).add(0, -0.5f, 0);
        bp.set(vec.getX(), vec.getY(), vec.getZ());
        if (BlockUtils.getPlaceSide(mc.player.getBlockPos().down()) != null) {
            bp.set(mc.player.getBlockPos().down());
        } else {
            Vec3d pos = mc.player.getPos();
            pos = pos.add(0, -0.98f, 0);
            pos.add(mc.player.getVelocity());

            if (!PlayerUtils.isWithin(prevBp, placeRange.get())) {
                List<BlockPos> blockPosArray = new ArrayList<>();

                for (int x = (int) (mc.player.getX() - placeRange.get()); x < mc.player.getX() + placeRange.get(); x++) {
                    for (int z = (int) (mc.player.getZ() - placeRange.get()); z < mc.player.getZ() + placeRange.get(); z++) {
                        for (int y = (int) Math.max(mc.world.getBottomY(), mc.player.getY() - placeRange.get()); y < Math.min(mc.world.getTopY(), mc.player.getY() + placeRange.get()); y++) {
                            bp.set(x, y, z);
                            if (!mc.world.getBlockState(bp).isAir()) blockPosArray.add(new BlockPos(bp));
                        }
                    }
                }
                if (blockPosArray.isEmpty()) {
                    return;
                }

                blockPosArray.sort(Comparator.comparingDouble(PlayerUtils::squaredDistanceTo));

                prevBp.set(blockPosArray.get(0));
            }

            Vec3d vecPrevBP = new Vec3d((double) prevBp.getX() + 0.5f,
                (double) prevBp.getY() + 0.5f,
                (double) prevBp.getZ() + 0.5f);

            Vec3d sub = pos.subtract(vecPrevBP);
            Direction facing;
            if (sub.getY() < -0.5f) {
                facing = Direction.DOWN;
            } else if (sub.getY() > 0.5f) {
                facing = Direction.UP;
            } else facing = Direction.getFacing(sub.getX(), 0, sub.getZ());

            bp.set(prevBp.offset(facing));
        }

        // Move down if shifting
        if (mc.options.sneakKey.isPressed() && !mc.options.jumpKey.isPressed()) {
            if (lastSneakingY - mc.player.getY() < 0.1) {
                lastWasSneaking = false;
                return;
            }
        } else {
            lastWasSneaking = false;
        }
        if (!lastWasSneaking) lastSneakingY = mc.player.getY();/*

        fastTower(false, null);

        if (airPlace.get()) {
            List<BlockPos> blocks = new ArrayList<>();
            for (int x = (int) (mc.player.getX() - radius.get()); x < mc.player.getX() + radius.get(); x++) {
                for (int z = (int) (mc.player.getZ() - radius.get()); z < mc.player.getZ() + radius.get(); z++) {
                    blocks.add(BlockPos.ofFloored(x, mc.player.getY() - 0.5, z));
                }
            }

            if (!blocks.isEmpty()) {
                blocks.sort(Comparator.comparingDouble(PlayerUtils::squaredDistanceTo));
                int counter = 0;
                for (BlockPos block : blocks) {
                    if (place(block)) {
                        fastTower(true, block);
                        counter++;
                    }

                    if (counter >= blocksPerTick.get()) {
                        break;
                    }
                }
            }
        } else {
            if (place(bp)) fastTower(true, bp);
            if (!mc.world.getBlockState(bp).isAir()) {
                prevBp.set(bp);
            }
        }*/
    }

    private boolean validItem(ItemStack itemStack, BlockPos pos) {
        if (!(itemStack.getItem() instanceof BlockItem)) return false;

        Block block = ((BlockItem) itemStack.getItem()).getBlock();

        if (blocksFilter.get() == ListMode.Blacklist && blocks.get().contains(block)) return false;
        else if (blocksFilter.get() == ListMode.Whitelist && !blocks.get().contains(block)) return false;

        if (!Block.isShapeFullCube(block.getDefaultState().getCollisionShape(mc.world, pos))) return false;
        return !(block instanceof FallingBlock) || !FallingBlock.canFallThrough(mc.world.getBlockState(pos));
    }

      private boolean place(BlockPos bp) {
        FindItemResult item = InvUtils.findInHotbar(itemStack -> validItem(itemStack, bp));
        if (!item.found()) return false;

        if (item.getHand() == null) return false;

        if (BlockUtils.place(bp, item, /*rotate.get()*/ false, 50, true)) {
            // Render block if was placed
            /*if (render.get())
                RenderUtils.renderTickingBlock(bp.toImmutable(), sideColor.get(), lineColor.get(), 0, 8, true, false);
            */return true;
        }
        return false;
    }
  
}
