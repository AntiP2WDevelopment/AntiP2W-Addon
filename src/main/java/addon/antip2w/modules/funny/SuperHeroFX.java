package addon.antip2w.modules.funny;

import addon.antip2w.AntiP2W;
import addon.antip2w.modules.Categories;
import com.mojang.brigadier.StringReader;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SuperHeroFX extends Module {
    public static final ParticleType<SuperHeroFXParticle.SuperHeroFXEffect> PARTICLE = FabricParticleTypes.complex(SuperHeroFXParticle.SuperHeroFXEffect.FACTORY);

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<List<String>> words = sgGeneral.add(new StringListSetting.Builder()
        .name("words")
        .description("the words")
        .defaultValue("boom", "pow", "wham", "smash", "kapow", "bam", "kaboom", "thwack")
        .build()
    );

    public final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("the amount of particles to spawn")
        .range(1, 200)
        .sliderRange(1, 200)
        .defaultValue(3)
        .build()
    );

    public final Setting<Boolean> customColor = sgGeneral.add(new BoolSetting.Builder()
        .name("CustomColor")
        .description("use a custom color or not")
        .defaultValue(false)
        .build()
    );

    public final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
        .name("color")
        .description("the color (alpha is ignored)")
        .defaultValue(new SettingColor(255, 255, 255, 255))
        .visible(customColor::get)
        .build()
    );

    public final Setting<Boolean> shadow = sgGeneral.add(new BoolSetting.Builder()
        .name("shadow")
        .description("render the text with a shadow or not")
        .defaultValue(true)
        .build()
    );

    public final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("the scale of the particles")
        .range(0.1, 10)
        .sliderRange(0.1, 10)
        .defaultValue(1)
        .build()
    );

    public final Setting<Integer> lifetime = sgGeneral.add(new IntSetting.Builder()
        .name("lifetime")
        .description("how long to display the particles for (ticks)")
        .range(0, 200)
        .sliderRange(0, 200)
        .defaultValue(20)
        .build()
    );

    public SuperHeroFX() {
        super(Categories.FUNNY, "SuperHeroFX", "wham bam");
        ParticleFactoryRegistry.getInstance().register(PARTICLE, SuperHeroFXParticle.Factory::new);
    }

    public static int packColor(Color color) {
        return (color.r & 0xFF) << 16 | (color.g & 0xFF) << 8 | (color.b & 0xFF);
    }

    public static int randomColor(Random random) {
        return random.nextInt() & 0x00FFFFFF;
    }

    public static class SuperHeroFXParticle extends Particle {
        public final String text;
        public final int color;
        public final boolean shadow;
        public final float scale;

        public SuperHeroFXParticle(ClientWorld world, double x, double y, double z, String text, int color, boolean shadow, float scale, int lifetime) {
            super(world, x, y, z);
            this.maxAge = lifetime;
            this.text = String.valueOf(text);
            this.color = color;
            this.shadow = shadow;
            this.scale = scale;

            this.velocityX = (Math.random() * 2.0 - 1.0) * 0.8;
            this.velocityY = Math.random();
            this.velocityZ = (Math.random() * 2.0 - 1.0) * 0.8;
            double d = (Math.random() + Math.random() + 1.0) * 0.15;
            double e = Math.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY + this.velocityZ * this.velocityZ);
            this.velocityX = this.velocityX / e * d * 0.4;
            this.velocityY = this.velocityY / e * d * 0.4 + 0.1;
            this.velocityZ = this.velocityZ / e * d * 0.4;
        }

        @Override
        public void tick() {
            super.tick();
            if(world.getCollisions(null, getBoundingBox()).iterator().hasNext()) markDead();
            this.velocityY -= 0.03;
        }

        @Override
        public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
            Vec3d vec3d = camera.getPos();
            float x = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
            float y = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
            float z = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

            MatrixStack matStack = new MatrixStack();
            matStack.push();
            float shrink = 1 - (age + tickDelta) / maxAge;
            matStack.translate(x, y + 0.35f * scale * shrink, z);
            matStack.multiply(camera.getRotation());
            matStack.scale(-0.04f * scale * shrink, -0.04f * scale * shrink, -1f);
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            AntiP2W.MC.textRenderer.draw(text,
                - (float) AntiP2W.MC.textRenderer.getWidth(text) / 2,
                0,
                color,
                shadow,
                matStack.peek().getPositionMatrix(),
                immediate,
                TextRenderer.TextLayerType.NORMAL,
                0x00000000,
                LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE);
            immediate.draw();
            matStack.pop();
        }

        @Override
        public ParticleTextureSheet getType() {
            return ParticleTextureSheet.CUSTOM;
        }

        public static class Factory implements ParticleFactory<SuperHeroFXEffect> {
            public Factory(SpriteProvider spriteProvider) {
            }

            @Nullable
            @Override
            public Particle createParticle(SuperHeroFXEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
                return new SuperHeroFXParticle(world, x, y, z, parameters.text, parameters.color, parameters.shadow, parameters.scale, parameters.lifetime);
            }
        }

        public record SuperHeroFXEffect(String text, int color, boolean shadow, float scale, int lifetime) implements ParticleEffect {
            public static final Factory<SuperHeroFXEffect> FACTORY = new Factory<>() {
                @Override
                public SuperHeroFXEffect read(ParticleType<SuperHeroFXEffect> type, StringReader reader) {
                    return null;
                }

                @Override
                public SuperHeroFXEffect read(ParticleType<SuperHeroFXEffect> type, PacketByteBuf buf) {
                    return null;
                }
            };

            @Override
            public ParticleType<?> getType() {
                return SuperHeroFX.PARTICLE;
            }

            @Override
            public void write(PacketByteBuf buf) {
            }

            @Override
            public String asString() {
                return toString();
            }
        }
    }
}
