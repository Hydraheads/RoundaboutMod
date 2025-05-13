package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class FrictionlessParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    FrictionlessParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet $$7) {
        super($$0, $$1, $$2, $$3);
        this.sprites = $$7;
        this.lifetime = 4;
        this.gravity = 0.008F;
        this.setAlpha(0.5F);
        this.xd = $$4;
        this.yd = $$5;
        this.zd = $$6;
        this.setSpriteFromAge($$7);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd = this.yd - (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.setSpriteFromAge(this.sprites);
        }
    }
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            super.render($$0, $$1, $$2);
        }
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) {
            this.sprites = $$0;
        }

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new FrictionlessParticle($$1, $$2, $$3, $$4, $$5, $$6, $$7, this.sprites);
        }
    }
}
