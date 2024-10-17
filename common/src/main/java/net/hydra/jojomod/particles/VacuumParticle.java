package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class VacuumParticle extends SimpleAnimatedParticle {
    private final SpriteSet sprites;

    protected VacuumParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet $$7) {
        super($$0, $$1, $$2, $$3, $$7, 1f);
        this.age = 0;
        this.lifetime = 6+ this.random.nextInt(2);
        this.friction = 0.96F;
        this.xd = this.xd * 0.01F + $$4;
        this.yd = this.yd * 0.01F + $$5;
        this.zd = this.zd * 0.01F + $$6;
        this.quadSize *= 1.7f;
        this.sprites = $$7;
        this.gravity = 0;
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
            VacuumParticle part = new VacuumParticle($$1, $$2, $$3, $$4, $$5,$$6,$$7, this.sprites);
            part.setColor(0.9F, 0.9F, 0.9F);
            part.setAlpha(0.7F);
            return part;
        }
    }
}
