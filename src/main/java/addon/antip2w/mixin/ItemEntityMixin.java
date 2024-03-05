package addon.antip2w.mixin;

import addon.antip2w.modules.NoItem;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onItemTick(CallbackInfo ci) {
        NoItem noItem = Modules.get().get(NoItem.class);
        if (noItem.isActive() && noItem.items.get().contains(getStack().getItem())) ci.cancel();
    }
}
