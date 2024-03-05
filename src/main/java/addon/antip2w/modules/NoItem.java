package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.render.RenderItemEntityEvent;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;

public class NoItem extends Module {

    public SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
        .name("items")
        .description("The items not to render.")
        .defaultValue(
            Items.BAMBOO
        )
        .build()
    );

    public NoItem() {
        super(Categories.DEFAULT, "NoItem", "Disables specified items from ticking and rendering");
    }

    @EventHandler
    public void onRender(RenderItemEntityEvent event) {
        if (items.get().contains(event.itemEntity.getStack().getItem())) event.cancel();
    }

}
