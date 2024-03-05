package addon.antip2w.modules.funny;

import java.util.List;
import java.util.Objects;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class ChestDisco extends Module {

    public SettingGroup sgGeneral = this.settings.getDefaultGroup();
    public SettingGroup sgParty = this.settings.createGroup("Party Mode");

    public ChestDisco() {
        super(Categories.FUNNY, "Chest-disco", "does idk");
    }

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

    public boolean retreat = false;
    int chestdiscoradius = 0;

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (((Integer)this.partyModeMin.get()).intValue() > ((Integer)this.partyModeMax.get()).intValue()) {
            info("The party mode minimum can't be bigger than the party mode maximum, setting the minimum to 0...", new Object[0]);
            this.partyModeMin.set(Integer.valueOf(0));
            return;
        }
        if (this.retreat) {
            if (Objects.equals(chestdiscoradius, this.partyModeMin.get())) {
                this.retreat = false;
                return;
            }
            chestdiscoradius--;
        } else if (chestdiscoradius >= ((Integer)this.partyModeMax.get()).intValue()) {
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
