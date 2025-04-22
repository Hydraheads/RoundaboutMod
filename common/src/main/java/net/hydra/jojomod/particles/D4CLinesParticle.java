package net.hydra.jojomod.particles;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class D4CLinesParticle extends SimpleAnimatedParticle {
    D4CLinesParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet, 1f);
        this.quadSize *= 1.8f;
        this.friction = 0.96F;
        this.xd = this.xd * 0.01F + g;
        this.yd = Math.abs(this.yd * 0.01F + h);
        this.zd = this.zd * 0.01F + i;
        this.gravity = -0.23F;
        this.x = this.x + (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.02F);
        this.y = this.y + (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.02F);
        this.z = this.z + (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.02F);
        this.lifetime = (int)(3.0 / (Math.random() * 0.8 + 0.2)) + 8;
        this.setFadeColor(15916745);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    public static class Provider
            implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) {
            this.sprites = $$0;
        }

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new D4CLinesParticle($$1, $$2, $$3, $$4, $$5, $$6, $$7, this.sprites);
        }
    }
}


