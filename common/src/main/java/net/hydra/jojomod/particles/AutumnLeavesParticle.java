package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class AutumnLeavesParticle extends TextureSheetParticle {

    protected AutumnLeavesParticle(ClientLevel level,
                                   double x, double y, double z,
                                   double xd, double yd, double zd,
                                   SpriteSet sprites) {

        super(level, x, y, z);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.gravity = 0.0F;
        this.friction = 0.96F;

        this.quadSize = 0.15F;
        this.lifetime = 30 + this.random.nextInt(20);

        this.hasPhysics = false;

        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        this.alpha = 1.0F - (float) this.age / this.lifetime;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider
            implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {

            return new AutumnLeavesParticle(
                    level, x, y, z,
                    xd, yd, zd,
                    this.sprites
            );
        }
    }
}