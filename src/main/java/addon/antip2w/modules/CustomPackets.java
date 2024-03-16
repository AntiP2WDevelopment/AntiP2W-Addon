package addon.antip2w.modules;

import addon.antip2w.utils.MCUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Direction;

public class CustomPackets extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final SettingGroup sgOptions = settings.createGroup("Options");
    //Modes

    private final Setting<CustomPackets.PacketType> mode = sgGeneral.add(new EnumSetting.Builder<CustomPackets.PacketType>()
        .name("mode")
        .description("the mode")
        .defaultValue(PacketType.ClickSlotC2SPacket)
        .build()
    );
    //ClickSlotC2SPacket

    private final Setting<SlotActionType> SlotActionTypeTypes = sgOptions.add(new EnumSetting.Builder<SlotActionType>()
        .name("mode")
        .defaultValue(SlotActionType.SWAP)
        .visible(() -> mode.get() == PacketType.ClickSlotC2SPacket)
        .build()
    );
    private final Setting<Integer> ClickSlotC2SPacketslot = sgOptions.add(new IntSetting.Builder()
        .name("slot")
        .defaultValue(1)
        .sliderRange(Integer.MIN_VALUE, Integer.MAX_VALUE)
        .visible(() -> mode.get() == PacketType.ClickSlotC2SPacket)
        .build()
    );

    private final Setting<Integer> ClickSlotC2SPacketbutton = sgOptions.add(new IntSetting.Builder()
        .name("button")
        .defaultValue(1)
        .sliderRange(Integer.MIN_VALUE, Integer.MAX_VALUE)
        .visible(() -> mode.get() == PacketType.ClickSlotC2SPacket)
        .build()
    );

    //PlayerActionC2SPacket

    private final Setting<PlayerActionC2SPacket.Action> ActionTypes = sgOptions.add(new EnumSetting.Builder<PlayerActionC2SPacket.Action>()
        .name("mode")
        .defaultValue(PlayerActionC2SPacket.Action.DROP_ALL_ITEMS)
        .visible(() -> mode.get() == PacketType.PlayerActionC2SPacket)
        .build()
    );
    //CommandExecutionC2SPacket

    private final Setting<String> CommandExecutionC2SPacketCommand = sgOptions.add(new StringSetting.Builder()
        .name("Command to run")
        .defaultValue("change me")
        .build()
    );
    public CustomPackets() {
        super(Categories.WIP, "Custom packets", "");
    }

    public void onActivate() {
        switch (mode.get()) {
            case ClickSlotC2SPacket:
                ScreenHandler handler = mc.player.currentScreenHandler;
                Int2ObjectMap<ItemStack> map = new Int2ObjectArrayMap<>();
                MCUtil.sendPacket(new ClickSlotC2SPacket(
                    handler.syncId,
                    handler.getRevision(),
                    ClickSlotC2SPacketslot.get(),
                    ClickSlotC2SPacketbutton.get(),
                    SlotActionTypeTypes.get(),
                    handler.getCursorStack().copy(),
                    map
                ));
                break;

            case PlayerActionC2SPacket:
                MCUtil.sendPacket(new PlayerActionC2SPacket(
                    //action, @NotNull pos, direction, sequence(optional)
                    ActionTypes.get(),
                    mc.player.getBlockPos(),
                    Direction.UP
                ));
                break;

            case CommandExecutionC2SPacket:
                ChatUtils.sendPlayerMsg("/" + CommandExecutionC2SPacketCommand);
        }
        toggle();
    }

    public enum PacketType {
        ClickSlotC2SPacket, PlayerActionC2SPacket, CommandExecutionC2SPacket
    }

}

