package addon.antip2w.mixin;

import addon.antip2w.modules.VanillaFlight;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "checkFallFlying", at = @At("HEAD"), cancellable = true)
    void onCheckFallFlying(CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().isActive(VanillaFlight.class)) cir.setReturnValue(false);
    }
}
