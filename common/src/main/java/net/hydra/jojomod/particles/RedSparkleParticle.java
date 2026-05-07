package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class RedSparkleParticle extends SimpleAnimatedParticle {
    private final SpriteSet sprites;

    protected RedSparkleParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet $$7) {
        super($$0, $$1, $$2, $$3, $$7, 1f);
        this.age = 0;
        this.lifetime = 6;
        this.xd = this.xd * 0.01F + $$4;
        this.yd = this.yd * 0.01F + $$5;
        this.zd = this.zd * 0.01F + $$6;
        this.quadSize *= 1f;
        this.sprites = $$7;
        this.gravity = 0;
        this.scale(2.7F);
        //this.setFadeColor(15916745);
        this.setSpriteFromAge($$7);
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

    @Override
    protected float getU0() {
        return this.sprite.getU0();
    }

    @Override
    protected float getU1() {
        return this.sprite.getU1();
    }

    @Override
    protected float getV0() {
        return this.sprite.getV0();
    }

    @Override
    protected float getV1() {
        return this.sprite.getV1();
    }


    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) {
            this.sprites = $$0;
        }

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            RedSparkleParticle part = new RedSparkleParticle($$1, $$2, $$3, $$4, $$5,$$6,$$7, this.sprites);
            part.setColor(0.9F, 0.9F, 0.9F);
            part.setAlpha(0.7F);
            return part;
        }
    }
    @Override
    public int getLightColor(float $$0) {
        float $$1 = ((float)this.age + $$0) / (float)this.lifetime;
        $$1 = Mth.clamp($$1, 0.0F, 1.0F);
        int $$2 = super.getLightColor($$0);
        int $$3 = $$2 & 0xFF;
        int $$4 = $$2 >> 16 & 0xFF;
        $$3 += (int)($$1 * 15.0F * 16.0F);
        if ($$3 > 240) {
            $$3 = 240;
        }

        return $$3 | $$4 << 16;
    }
}


