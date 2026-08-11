package net.hydra.jojomod.particles;

import net.hydra.jojomod.client.HallucinatoryAcidColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public final class HallucinatoryAcidDripParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    private HallucinatoryAcidDripParticle(ClientLevel level, double x, double y, double z,
                                          SpriteSet sprites, int skin) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.gravity = 0.06F;
        this.lifetime = 60;
        this.quadSize = 0.06F;
        int color = HallucinatoryAcidColors.displayColor(skin);
        float red = ((color >> 16) & 255) / 255.0F;
        float green = ((color >> 8) & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        setColor(red, green, blue);
        setSprite(sprites.get(0, 2));
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime) {
            remove();
            return;
        }
        if (age > 10) yd -= gravity;
        move(xd, yd, zd);
        xd *= 0.98D;
        yd *= 0.98D;
        zd *= 0.98D;
        setSprite(sprites.get(age <= 10 ? 0 : 1, 2));
        if (onGround) remove();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static final class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new HallucinatoryAcidDripParticle(level, x, y, z, sprites,
                    (int) Math.round(velocityX));
        }
    }
}
