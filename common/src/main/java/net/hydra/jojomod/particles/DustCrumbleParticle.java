package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class DustCrumbleParticle extends SimpleAnimatedParticle {
    DustCrumbleParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet, 1f);
        this.quadSize = 0.13f;
        this.friction = 0.96F;
        this.gravity = 0F;
        this.xd = this.xd * 0.01F + g;
        this.yd = this.yd * 0.01F + h;
        this.zd = this.zd * 0.01F + i;
        this.lifetime = 6;
        this.setFadeColor(15916745);
        this.setAlpha(0.3f);
        this.setSpriteFromAge(spriteSet);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
            super.render($$0, $$1, $$2);
    }
    public static class Provider
            implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) {
            this.sprites = $$0;
        }

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new DustCrumbleParticle($$1, $$2, $$3, $$4, $$5, $$6, $$7, this.sprites);
        }
    }
}



