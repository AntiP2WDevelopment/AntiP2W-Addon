package addon.antip2w.modules.griefing;

import addon.antip2w.modules.Categories;
import addon.antip2w.modules.VanillaFlight;
import addon.antip2w.utils.MCUtil;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.movement.Scaffold;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.EntityPose;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AutoLC extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Selected blocks.")
        .build()
    );

    private final Setting<Scaffold.ListMode> blocksFilter = sgGeneral.add(new EnumSetting.Builder<Scaffold.ListMode>()
        .name("blocks-filter")
        .description("How to use the block list setting")
        .defaultValue(Scaffold.ListMode.Blacklist)
        .build()
    );

    private final Setting<Boolean> enablePlace = sgGeneral.add(new BoolSetting.Builder()
        .name("enable-place")
        .description("Places a block below you when enabled.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> renderSwing = sgGeneral.add(new BoolSetting.Builder()
        .name("swing")
        .description("Renders your client-side swing.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> autoSwitch = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-switch")
        .description("Automatically swaps to a block before placing.")
        .defaultValue(true)
        .build()
    );


    private final Setting<Integer> blocksPerTick = sgGeneral.add(new IntSetting.Builder()
        .name("blocks-per-tick")
        .description("How many blocks to place in one tick.")
        .defaultValue(3)
        .min(1)
        .build()
    );

    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder()
        .name("render")
        .description("Whether to render things.")
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
        .description("The side color.")
        .defaultValue(new SettingColor(197, 137, 232, 10))
        .visible(render::get)
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The line color.")
        .defaultValue(new SettingColor(197, 137, 232))
        .visible(render::get)
        .build()
    );

    public AutoLC() {
        super(Categories.GRIEF, "AutoLC", "Auto Lava Cast");
    }

    @Override
    public void onActivate() {
        if(Modules.get().get(VanillaFlight.class).isActive()) Modules.get().get(VanillaFlight.class).toggle();
        if (enablePlace.get() && mc.player != null) {
            place(mc.player.getBlockPos().down());
        }
    }

    private boolean isCollidingWithSomething(Vec3d pos) {
        Box boundingBox = mc.player.getDimensions(EntityPose.STANDING).getBoxAt(pos);
        return mc.world.getBlockCollisions(null, boundingBox).iterator().hasNext();
    }
    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null) return;
        Direction facing = mc.player.getHorizontalFacing();
        boolean goingDown = mc.player.getPitch() > 30;
        BlockPos pos = mc.player.getBlockPos().add(facing.getOffsetX(), goingDown ? -2 : 0, facing.getOffsetZ());
        RenderUtils.renderTickingBlock(pos.toImmutable(), sideColor.get(), lineColor.get(), shapeMode.get(), 0, 1, false, false);
        if (!mc.player.input.pressingForward) return;
        ((IVec3d)mc.player.getVelocity()).set(0, mc.player.getVelocity().y, 0);
        if (mc.world.getTime() % blocksPerTick.get() != 0) return;
        place(pos);
        Vec3d newPos = pos.toCenterPos().add(0, 0.5, 0);
        Vec3d firstPacketPos = new Vec3d(goingDown ? newPos.x : mc.player.getX(),
            goingDown ? mc.player.getY() : newPos.y,
            goingDown ? newPos.z : mc.player.getZ());
        if (isCollidingWithSomething(firstPacketPos) || isCollidingWithSomething(newPos)) return;
        MCUtil.sendPacket(new PositionAndOnGround(firstPacketPos.x, firstPacketPos.y, firstPacketPos.z, false));
        MCUtil.sendPacket(new PositionAndOnGround(newPos.x, newPos.y, newPos.z, true));
        mc.player.setPosition(newPos.x, newPos.y, newPos.z);
        mc.player.resetPosition(); // don't lerp pos because it looks wonky
    }

    private boolean validItem(ItemStack itemStack, BlockPos pos) {
        if (!(itemStack.getItem() instanceof BlockItem)) return false;

        Block block = ((BlockItem) itemStack.getItem()).getBlock();

        if (blocksFilter.get() == Scaffold.ListMode.Blacklist && blocks.get().contains(block)) return false;
        else if (blocksFilter.get() == Scaffold.ListMode.Whitelist && !blocks.get().contains(block)) return false;

        if (!Block.isShapeFullCube(block.getDefaultState().getCollisionShape(mc.world, pos))) return false;
        return !(block instanceof FallingBlock) || !FallingBlock.canFallThrough(mc.world.getBlockState(pos));
    }

    private boolean place(BlockPos bp) {
        FindItemResult item = InvUtils.findInHotbar(itemStack -> validItem(itemStack, bp));
        if (!item.found()) return false;

        if (item.getHand() == null && !autoSwitch.get()) return false;

        if (BlockUtils.place(bp, item, false, -1, renderSwing.get(), true)) {
            // Render block if was placed
            if (render.get())
                RenderUtils.renderTickingBlock(bp.toImmutable(), sideColor.get(), lineColor.get(), shapeMode.get(), 0, 8, true, false);
            return true;
        }
        return false;
    }
}
