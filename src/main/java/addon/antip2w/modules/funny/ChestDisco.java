package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StorageBlockListSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.List;

public class ChestDisco extends Module {
    public SettingGroup sgGeneral = this.settings.getDefaultGroup();
    public SettingGroup sgParty = this.settings.createGroup("Party Mode");

    private final Setting<Integer> partyModeMin = sgGeneral.add(new IntSetting.Builder()
            .name("party-min-radius")
            .description("The minimum radius party mode will change to.")
            .defaultValue(0)
            .min(0)
            .max(128)
            .sliderMax(128)
            .build()
    );

    private final Setting<Integer> partyModeMax = sgGeneral.add(new IntSetting.Builder()
            .name("party-max-radius")
            .description("The maximum radius party mode will change to.")
            .defaultValue(64)
            .min(1)
            .max(128)
            .sliderMax(128)
            .build()
    );

    public final Setting<List<BlockEntityType<?>>> storageblocks = sgGeneral.add(new StorageBlockListSetting.Builder()
            .name("Storage-Blocks")
            .description("Storage blocks to unrender")
            .defaultValue(StorageBlockListSetting.STORAGE_BLOCKS)
            .build()
    );

    public ChestDisco() {
        super(Categories.FUNNY, "Chest-disco", "does idk");
    }

    public boolean retreat = false;
    private int chestdiscoradius = 0;

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (this.partyModeMin.get() > this.partyModeMax.get()) {
            info("The party mode minimum can't be bigger than the party mode maximum, setting the minimum to 0...");
            this.partyModeMin.set(0);
            return;
        }
        if (this.retreat) {
            if (chestdiscoradius == this.partyModeMin.get()) {
                this.retreat = false;
                return;
            }
            chestdiscoradius--;
        } else if (chestdiscoradius >= this.partyModeMax.get()) {
            this.retreat = true;
        } else {
            chestdiscoradius++;
        }
    }

    @EventHandler
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        BlockEntity block = event.blockEntity;
        for (BlockEntityType<?> blockType : storageblocks.get()) {
            if (blockType.equals(block.getType()) && PlayerUtils.distanceTo(block.getPos()) > chestdiscoradius) {
                event.cancel();
            }
        }
    }
}
