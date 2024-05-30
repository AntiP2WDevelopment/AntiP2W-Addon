package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

public class Creeper_Alert extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public Creeper_Alert() {
        super(Categories.FUNNY, "Creeper Alert", "Alerts when a creeper is in range.");
    }

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
            .name("range")
            .sliderRange(1, 20)
            .defaultValue(15)
            .build()
    );

    //TODO: custom soundevent, custom pitch, custom volume, custom soundfile even (maybe), custom pitch, custom soundcategory

    @EventHandler
    private void onTick(TickEvent.Post event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof CreeperEntity && entity.distanceTo(mc.player) <= range.get()) {
                mc.player.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0F, 1.0F);
                break;
            }
        }
    }
}
