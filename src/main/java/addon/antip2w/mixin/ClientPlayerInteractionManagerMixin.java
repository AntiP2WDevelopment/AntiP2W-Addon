package addon.antip2w.mixin;

import addon.antip2w.modules.funny.SuperHeroFX;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow private GameMode gameMode;

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (this.gameMode == GameMode.SPECTATOR) return;
        SuperHeroFX module = Modules.get().get(SuperHeroFX.class);
        if (!module.isActive()) return;
        Random random = target.getWorld().random;
        for (int i = 0; i < module.amount.get(); i++) {
            Vec3d pos = target.getPos();
            SuperHeroFX.SuperHeroFXParticle.SuperHeroFXEffect effect = new SuperHeroFX.SuperHeroFXParticle.SuperHeroFXEffect(
                module.words.get().get(random.nextInt(module.words.get().size())),
                module.customColor.get() ? SuperHeroFX.packColor(module.color.get()) : SuperHeroFX.randomColor(random),
                module.shadow.get(),
                module.scale.get().floatValue(),
                module.lifetime.get());
            target.getWorld().addParticle(effect, true, pos.x, target.getBodyY(random.nextFloat()), pos.z, 0, 0, 0);
        }
    }
}
