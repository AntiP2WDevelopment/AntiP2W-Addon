package addon.antip2w.hud;

import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BrandHud extends HudElement {
    public static final HudElementInfo<BrandHud> INFO = new HudElementInfo<>(Groups.DEFAULT, "brand-hud", "cool hud", BrandHud::new);

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("The scale.")
        .defaultValue(1)
        .min(1)
        .sliderRange(1, 5)
        .onChanged(aDouble -> onScaleChanged())
        .build()
    );

    private final Setting<Double> spinSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("spin-speed")
        .description("The spinning speed.")
         .defaultValue(1)
        .range(0.1, 10)
        .sliderRange(0.1, 10)
        .build()
    );

    private float spinProgress = -180;

    public BrandHud() {
        super(INFO);
        setSize(100, 100);
    }

    private void onScaleChanged() {
        setSize(100 * scale.get(), 100 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        spinProgress = (float) MathHelper.wrapDegrees(spinProgress + 9 * spinSpeed.get().floatValue() * renderer.delta * 20);
        double magicnumber = Math.sin(Math.toRadians(spinProgress));
        renderer.texture(new Identifier("antip2w", "icon.png"), getX() + (double) getWidth() / 2 + (double) -getWidth() / 2 * magicnumber, getY(), getWidth() * magicnumber, getHeight(), Color.WHITE);
    }
}
