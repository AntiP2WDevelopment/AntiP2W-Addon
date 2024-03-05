package addon.antip2w.mixin;

import addon.antip2w.modules.AnvilUnescape;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.apache.commons.lang3.StringEscapeUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @Shadow private TextFieldWidget nameField;

    @Inject(method = "setup", at = @At("TAIL"))
    void afterSetup(CallbackInfo ci) {
        if(Modules.get().isActive(AnvilUnescape.class)) nameField.setMaxLength(512);
    }

    @ModifyVariable(method = "onRenamed", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    String modifyName(String original) {
        try {
            return StringEscapeUtils.unescapeJava(original);
        } catch (Exception e) {
            return original;
        }
    }

}
