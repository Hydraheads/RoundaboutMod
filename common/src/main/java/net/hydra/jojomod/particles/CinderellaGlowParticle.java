package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class CinderellaGlowParticle extends TextureSheetParticle {
    static final RandomSource RANDOM = RandomSource.create();
    private final SpriteSet sprites;

    CinderellaGlowParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet $$7) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6);
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.sprites = $$7;
        this.quadSize *= 0.75F;
        this.hasPhysics = false;
        this.setSpriteFromAge($$7);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float $$0) {
        float $$1 = ((float)this.age + $$0) / (float)this.lifetime;
        $$1 = Mth.clamp($$1, 0.0F, 1.0F);
        int $$2 = super.getLightColor($$0);
        int $$3 = $$2 & 255;
        int $$4 = $$2 >> 16 & 255;
        $$3 += (int)($$1 * 15.0F * 16.0F);
        if ($$3 > 240) {
            $$3 = 240;
        }

        return $$3 | $$4 << 16;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }


    public static class CinderellaGlowProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public CinderellaGlowProvider(SpriteSet $$0) {
            this.sprite = $$0;
        }

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            CinderellaGlowParticle $$8 = new CinderellaGlowParticle($$1, $$2, $$3, $$4, 0.5 - CinderellaGlowParticle.RANDOM.nextDouble(), $$6, 0.5 - CinderellaGlowParticle.RANDOM.nextDouble(), this.sprite);
            if ($$1.random.nextBoolean()) {
                $$8.setColor(1F, 1.0F, 0.8F);
            } else {
                $$8.setColor(1F, 0.9F, 0.3F);
            }

            $$8.yd *= 0.20000000298023224;
            if ($$5 == 0.0 && $$7 == 0.0) {
                $$8.xd *= 0.10000000149011612;
                $$8.zd *= 0.10000000149011612;
            }

            $$8.setLifetime((int)(8.0 / ($$1.random.nextDouble() * 0.8 + 0.2)));
            return $$8;
        }
    }
}

