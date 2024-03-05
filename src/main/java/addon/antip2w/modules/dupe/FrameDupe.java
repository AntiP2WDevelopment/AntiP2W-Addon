package addon.antip2w.modules.dupe;

import addon.antip2w.modules.Categories;
import addon.antip2w.utils.TimerUtils;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("ALL")
public class FrameDupe  extends Module {
    public FrameDupe() {
        super(Categories.DEFAULT,"frame-dupe","balls");
    }

    public TimerUtils timer = new TimerUtils();

    public SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Boolean> placeFrame = sgGeneral.add(new BoolSetting.Builder()
        .name("place-frame")
        .description("If to place the frame back")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> sleep = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("its ticks bitch")
        .defaultValue(20)
        .min(-1)
        .sliderMax(100)
        .build()
    );

    @Override
    public void onActivate() {
        timer.reset();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onTick(TickEvent.Post event){
        //First check if frame placed
        if (!timer.hasReached(sleep.get() * 50)) return;
        for(Entity entity : mc.world.getEntities()){
            if(entity instanceof ItemFrameEntity frame){
                if (PlayerUtils.distanceTo(frame.getPos()) > 4) continue;
                //Alright found frame let's do it

                FindItemResult shulker =InvUtils.findInHotbar(itemStack -> Block.getBlockFromItem(itemStack.getItem()) instanceof ShulkerBoxBlock);
                if(shulker.found()){
                    InvUtils.swap(shulker.slot(),false);
                    mc.interactionManager.interactEntity(mc.player, frame, mc.player.getActiveHand());
                    mc.interactionManager.interactEntity(mc.player, frame, mc.player.getActiveHand());
                    mc.interactionManager.interactEntity(mc.player, frame, mc.player.getActiveHand());
                }
                if (frame != null && !frame.getHeldItemStack().isEmpty()) {
                    mc.interactionManager.attackEntity(mc.player, frame);
                }

            }
        }
        if(mc.crosshairTarget.getType() == HitResult.Type.BLOCK && placeFrame.get()){
            BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos().up();
            BlockState state = mc.world.getBlockState(pos);
            if(state.getBlock() == Blocks.NETHERITE_BLOCK){
                FindItemResult B = InvUtils.findInHotbar(Items.ITEM_FRAME, Items.GLOW_ITEM_FRAME);
                if(B.found()){
                    InvUtils.swap(B.slot(),false);
                    BlockUtils.place(pos,B,0);
                }
            }
        }
        timer.reset();
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) { toggle(); }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen instanceof DisconnectedScreen) {
            toggle();
        }
    }
}
