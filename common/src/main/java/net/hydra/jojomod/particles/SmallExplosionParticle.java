package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class SmallExplosionParticle extends HugeExplosionParticle {
    protected SmallExplosionParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, SpriteSet $$5) {
        super($$0, $$1, $$2, $$3, $$4, $$5);
        this.quadSize = 0.9F;
        this.setAlpha(0.6f);
    }
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_106925_) {
            this.sprites = p_106925_;
        }

        public Particle createParticle(SimpleParticleType p_106936_, ClientLevel p_106937_, double p_106938_, double p_106939_, double p_106940_, double p_106941_, double p_106942_, double p_106943_) {
            return new SmallExplosionParticle(p_106937_, p_106938_, p_106939_, p_106940_, p_106941_, this.sprites);
        }
    }
}
