package addon.antip2w.mixin;

import addon.antip2w.AntiP2W;
import addon.antip2w.ClientWatchdog;
import addon.antip2w.modules.MultiUse;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Unique
    private Thread watchDog;

    @Shadow public abstract void tick();

    @Inject(method = "render", at = @At("HEAD"))
    void onRender(boolean tick, CallbackInfo ci) {
        if(watchDog == null) {
            Thread thread = new Thread(new ClientWatchdog(MinecraftClient.getInstance()));
            thread.setName("Client Watchdog");
            thread.setDaemon(true);
            thread.start();
            watchDog = thread;
        }
        if(tick) ClientWatchdog.lastTickStartTime = Util.getMeasuringTimeMs();
    }

    @ModifyConstant(method = "getWindowTitle", constant = @Constant(stringValue = "Minecraft"))
    String modifyMinecraftConst(String constant) {
        return "AntiP2W-Tools";
    }

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void afterFirstIsUsingItemCall(CallbackInfo ci, boolean bl3) {
        if(Modules.get().isActive(MultiUse.class))
            while (AntiP2W.MC.options.attackKey.wasPressed()) {
                bl3 |= AntiP2W.MC.doAttack();
            }
    }

    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z"))
    private boolean redirectDoAttack(MinecraftClient instance) {
        if(Modules.get().isActive(MultiUse.class)) return false;
        else return instance.doAttack();
    }
}
