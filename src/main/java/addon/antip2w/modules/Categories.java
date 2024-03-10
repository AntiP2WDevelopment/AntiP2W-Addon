package addon.antip2w.modules;

import meteordevelopment.meteorclient.systems.modules.Category;
import net.minecraft.item.Items;

public final class Categories {
    public static final Category DEFAULT = new Category("AntiP2W Tools", Items.BARRIER.getDefaultStack());
    public static final Category FUNNY = new Category("AntiP2W Funny", Items.DIRT.getDefaultStack());
    public static final Category GRIEF = new Category("AntiP2W Griefing", Items.TNT.getDefaultStack());
    public static final Category WIP = new Category("AntiP2W WIP", Items.LIGHT.getDefaultStack());
}
