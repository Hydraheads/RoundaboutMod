package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class MoldDustParticle extends TextureSheetParticle {
    MoldDustParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, boolean bl) {
        super(clientLevel, d, e, f);
        this.scale(3.0F);
        this.setSize(0.25F, 0.25F);
        if (bl) {
            this.lifetime = this.random.nextInt(50) + 280;
        } else {
            this.lifetime = this.random.nextInt(50) + 80;
        }

        this.gravity = 3.0E-6F;
        this.xd = g;
        this.yd = h + (double)(this.random.nextFloat() / 500.0F);
        this.zd = i;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
            this.xd += (double)(this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1));
            this.zd += (double)(this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1));
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }

        } else {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            super.render($$0, $$1, $$2);
        }
    }
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    // public static class CosyProvider implements ParticleProvider<SimpleParticleType> {
    //     private final SpriteSet sprites;
    //
    //     public CosyProvider(SpriteSet spriteSet) {
    //         this.sprites = spriteSet;
    //     }
    //
    //     public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
    //         MoldDustParticle campfireSmokeParticle = new MoldDustParticle(clientLevel, d, e, f, g, h, i, false);
    //         campfireSmokeParticle.setAlpha(0.6F);
    //         campfireSmokeParticle.pickSprite(this.sprites);
    //         return campfireSmokeParticle;
    //     }
    // }

    public static class Provider implements ParticleProvider<ColorParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ColorParticleOptions options, ClientLevel level,
                double x, double y, double z, double xd, double yd, double zd) {
            MoldDustParticle particle = new MoldDustParticle(level, x, y, z, xd, yd, zd, false);
            particle.setAlpha(0.6f);
            particle.pickSprite(this.sprites);
            particle.setColor(options.getR(), options.getG(), options.getB());
            return particle;
        }
    }
}
