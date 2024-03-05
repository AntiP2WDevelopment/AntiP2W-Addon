package addon.antip2w.mixin;

import addon.antip2w.ClientWatchdog;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
