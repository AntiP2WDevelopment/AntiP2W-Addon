package addon.antip2w.modules;

import addon.antip2w.utils.TimerUtils;
import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class DubCounter extends Module {
    public HashSet<BlockPos> coords = new HashSet<>();
    public int shulkerCount = 0; // Add this line to declare shulkerCount

    public int chestCount = 0;

    public TimerUtils timer = new TimerUtils();

    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> loadTime = sgGeneral.add(new IntSetting.Builder()
            .name("load-time")
            .description("How much time it's going to take to load all the dubs. (useful if there are dubs out of render distance so you can load them or if you have a performance mod)")
            .defaultValue(Integer.valueOf(1))
            .min(1)
            .sliderMax(60)
            .build()
    );
    public DubCounter() {
        super(Categories.DEFAULT, "dub-counter", "Counts how many dubs are in render distance.");
    }

    public void onActivate() {
        this.timer.reset();
        info("Please wait " + Formatting.WHITE + this.loadTime.get() + Formatting.GRAY + " second(s)...", new Object[0]);
    }

    public void onDeactivate() {
        this.coords.clear();
        this.shulkerCount = 0;
        this.chestCount = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (this.timer.hasReached(this.loadTime.get() * 1000L)) {

            info("There are roughly " + Formatting.WHITE + chestCount/2 + Formatting.GRAY + " (" + chestCount + " normal chests) rendered double chests.");
            info("There are roughly " + Formatting.WHITE + this.shulkerCount + Formatting.GRAY + " rendered Shulker Boxes.");

            toggle();
            this.timer.reset();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1000000000)
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        BlockEntity block = event.blockEntity;

        if (!(block instanceof ChestBlockEntity) && !(block instanceof ShulkerBoxBlockEntity) || !this.coords.add(block.getPos())) return;

        if (block instanceof ShulkerBoxBlockEntity) {
            this.shulkerCount++;
        }

        if (block instanceof ChestBlockEntity) {
            this.chestCount++;
        }
    }
}
