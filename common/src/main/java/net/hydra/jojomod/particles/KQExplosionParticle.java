package net.hydra.jojomod.particles;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;


public class KQExplosionParticle extends SimpleAnimatedParticle {
	protected KQExplosionParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
            super(clientLevel, d, e, f, spriteSet, 1f);
            this.xd = 0;
            this.yd = h*0.06f;
            this.zd = 0;
            this.friction = 0.96F;
            this.gravity = 0;
            this.quadSize *= 0.75f;
            this.lifetime = 10 + this.random.nextInt(12);
            this.setFadeColor(15916745);
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

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) { this.sprites = $$0;}

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new KQExplosionParticle($$1, $$2, $$3, $$4, $$5, $$6, $$7, this.sprites);
        }
    }
}