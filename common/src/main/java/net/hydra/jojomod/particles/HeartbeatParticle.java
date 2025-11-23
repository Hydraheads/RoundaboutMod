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


public class HeartbeatParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected HeartbeatParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet $$7) {
        super($$0, $$1, $$2, $$3, 0.0, 0.0, 0.0);
        this.age = 0;
        this.lifetime = 8;
        this.quadSize = 0.55F;
        this.sprites = $$7;
        this.setSpriteFromAge($$7);
    }
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
        Vec3 $$3 = $$1.getPosition();
        float $$4 = (float)(Mth.lerp((double)$$2, this.xo, this.x) - $$3.x());
        float $$5 = (float)(Mth.lerp((double)$$2, this.yo, this.y) - $$3.y());
        float $$6 = (float)(Mth.lerp((double)$$2, this.zo, this.z) - $$3.z());
        Quaternionf $$7;
        if (this.roll == 0.0F) {
            $$7 = $$1.rotation();
        } else {
            $$7 = new Quaternionf($$1.rotation());
            $$7.rotateZ(Mth.lerp($$2, this.oRoll, this.roll));
        }

        Vector3f[] $$9 = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float $$10 = this.getQuadSize($$2);

        for (int $$11 = 0; $$11 < 4; $$11++) {
            Vector3f $$12 = $$9[$$11];
            $$12.rotate($$7);
            $$12.mul($$10);
            $$12.add($$4, $$5, $$6);
        }

        float $$13 = this.getU0();
        float $$14 = this.getU1();
        float $$15 = this.getV0();
        float $$16 = this.getV1();
        int $$17 = this.getLightColor($$2);

        $$0.vertex((double)$$9[0].x(), (double)$$9[0].y(), (double)$$9[0].z())
                .uv($$14, $$16)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();

        $$0.vertex((double)$$9[1].x(), (double)$$9[1].y(), (double)$$9[1].z())
                .uv($$14, $$15)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();

        $$0.vertex((double)$$9[2].x(), (double)$$9[2].y(), (double)$$9[2].z())
                .uv($$13, $$15)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();

        $$0.vertex((double)$$9[3].x(), (double)$$9[3].y(), (double)$$9[3].z())
                .uv($$13, $$16)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
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
            HeartbeatParticle part = new HeartbeatParticle($$1, $$2, $$3, $$4, $$5,$$6,$$7, this.sprites);
            part.setColor(0.99F, 0.99F, 0.99F);
            part.setAlpha(0.7F);
            return part;
        }
    }
}
