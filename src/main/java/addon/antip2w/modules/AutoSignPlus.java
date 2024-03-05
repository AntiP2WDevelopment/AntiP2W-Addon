package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.BlockUpdateEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockEntityIterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Objects;

import static meteordevelopment.orbit.EventPriority.HIGHEST;

public class AutoSignPlus extends Module {

    private final HashSet<BlockPos> reinteract = new HashSet<>();
    private final HashSet<BlockPos> interacted = new HashSet<>();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final SettingGroup sgFront = settings.createGroup("Front Side");
    private final SettingGroup sgBack = settings.createGroup("Back Side");

    private final Setting<String> frontLine1 = sgFront.add(new StringSetting.Builder()
        .name("1st line")
        .description("First line of the front side of the sign.")
        .defaultValue("4zet")
        .build()
    );

    private final Setting<String> frontLine2 = sgFront.add(new StringSetting.Builder()
        .name("2nd line")
        .description("Second line of the front side of the sign.")
        .defaultValue("is")
        .build()
    );

    private final Setting<String> frontLine3 = sgFront.add(new StringSetting.Builder()
        .name("3rd line")
        .description("Third line of the front side of the sign.")
        .defaultValue("the")
        .build()
    );

    private final Setting<String> frontLine4 = sgFront.add(new StringSetting.Builder()
        .name("4th line")
        .description("Fourth line of the front side of the sign.")
        .defaultValue("best as always.")
        .build()
    );

    private final Setting<String> backLine1 = sgBack.add(new StringSetting.Builder()
        .name("1st line")
        .description("First line of the back side of the sign.")
        .defaultValue("4zet")
        .build()
    );

    private final Setting<String> backLine2 = sgBack.add(new StringSetting.Builder()
        .name("2nd line")
        .description("Second line of the back side of the sign.")
        .defaultValue("is")
        .build()
    );

    private final Setting<String> backLine3 = sgBack.add(new StringSetting.Builder()
        .name("3rd line")
        .description("Third line of the back side of the sign.")
        .defaultValue("the")
        .build()
    );

    private final Setting<String> backLine4 = sgBack.add(new StringSetting.Builder()
        .name("4th line")
        .description("Fourth line of the back side of the sign.")
        .defaultValue("best as always.")
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("Delay")
        .description("Delay in ticks. If 0 than instant")
        .defaultValue(0)
        .build()
    );

    private final Setting<Boolean> editPlaced = sgGeneral.add(new BoolSetting.Builder()
        .name("Edit placed")
        .description("Should edit placed signs (above 1.20)")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> bothSide = sgGeneral.add(new BoolSetting.Builder()
        .name("Both side")
        .description("Edits both side of the sign (above 1.20)")
        .defaultValue(true)
        .build()
    );

    public AutoSignPlus() {
        super(Categories.DEFAULT, "AutoSignPlus", "Extension for AutoSign");
    }

    @Override
    public void onActivate() {
        reinteract.clear();
        interacted.clear();
    }

    public static BlockHitResult blockHitResultOf(BlockPos bp) {
        return new BlockHitResult(Vec3d.ofBottomCenter(bp), Direction.DOWN, bp, false);
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        BlockEntityIterator iterator = new BlockEntityIterator();

        while (iterator.hasNext()) {
            BlockEntity blockEntity = iterator.next();
            if (!(blockEntity instanceof SignBlockEntity sign) || sign.isWaxed()) {
                continue;
            }

            if (isTheSame(sign)) {
                continue;
            }

            if (reinteract.contains(blockEntity.getPos())) continue;

            if (editPlaced.get() && mc.player.age % (delay.get()+1) == 0 && PlayerUtils.isWithinReach(sign.getPos())) {
                interactWithSign(sign.getPos());
                if (delay.get() > 0) break;
            }
        }
    }

    private boolean isCompleted(BlockPos pos) {
        return interacted.contains(pos) && !reinteract.contains(pos);
    }

    @EventHandler()
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (isCompleted(event.pos) && event.oldState.isIn(BlockTags.ALL_SIGNS) && (!event.newState.isOf(event.oldState.getBlock()) || !isTheSame(mc.world.getBlockEntity(event.pos)))) {
            interacted.remove(event.pos);
        }
    }

    @EventHandler(priority = HIGHEST+10)
    public void onOpenScreen(PacketEvent.Receive event) {
        if (event.packet instanceof SignEditorOpenS2CPacket packet) {
            if (!editPlaced.get() && isCompleted(packet.getPos())) return;
            boolean isFront = !reinteract.remove(packet.getPos());
            sendEditSignPacket(packet.getPos(), isFront);
            if (bothSide.get() && isFront) {
                interactWithSign(packet.getPos());
                reinteract.add(packet.getPos());
            }
            event.cancel();
        }
    }

    private boolean isTheSame(BlockEntity be) {
        SignBlockEntity sign = (SignBlockEntity) be;
        Text[] textArray = sign.getBackText().getMessages(false);
        boolean back = Objects.equals(textArray[0].getString(), backLine1.get()) &&
            Objects.equals(textArray[1].getString(), backLine2.get()) &&
            Objects.equals(textArray[2].getString(), backLine3.get()) &&
            Objects.equals(textArray[3].getString(), backLine4.get());

        textArray = sign.getFrontText().getMessages(false);
        boolean front = Objects.equals(textArray[0].getString(), frontLine1.get()) &&
            Objects.equals(textArray[1].getString(), frontLine2.get()) &&
            Objects.equals(textArray[2].getString(), frontLine3.get()) &&
            Objects.equals(textArray[3].getString(), frontLine4.get());

        return front && back;
    }

    private void interactWithSign(BlockPos pos) {
        mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResultOf(pos), 0));
        mc.player.swingHand(Hand.MAIN_HAND);
        interacted.add(pos);
    }

    private void sendEditSignPacket(BlockPos pos, boolean isFront) {
        String line1, line2, line3, line4;
        if (isFront) {
            line1 = frontLine1.get();
            line2 = frontLine2.get();
            line3 = frontLine3.get();
            line4 = frontLine4.get();
        } else {
            line1 = backLine1.get();
            line2 = backLine2.get();
            line3 = backLine3.get();
            line4 = backLine4.get();
        }
        mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(pos, isFront, line1, line2, line3, line4));
    }
}
