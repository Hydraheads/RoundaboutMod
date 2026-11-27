package net.hydra.jojomod.particles;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class RainShardParticle extends SimpleAnimatedParticle {
    protected RainShardParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet, 1f);
        this.age = 0;
        this.xd = this.xd * 0.01F + g;
        this.yd = this.yd * 0.01F + h;
        this.zd = this.zd * 0.01F + i;
        this.gravity = 0;
        this.quadSize = 0.15f;
        this.lifetime = 40;
        this.setAlpha(Roundabout.RANDOM.nextFloat(0.3f, 0.65f));
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
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
        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new RainShardParticle($$1, $$2, $$3, $$4, $$5, $$6, $$7, this.sprites);
        }
    }
}