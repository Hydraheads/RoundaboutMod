package net.hydra.jojomod.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class AirCrackleParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private static final Vector3f ROTATION_VECTOR = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private final Vector3d ROTVECTOR;
    private static final float MAGICAL_X_ROT = 1.0472F;

    protected AirCrackleParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet $$7) {
        super($$0, $$1, $$2, $$3, 0.0, 0.0, 0.0);
        this.lifetime = 6;
        this.quadSize = 0.45F;
        this.sprites = $$7;
        this.ROTVECTOR = new Vector3d(Math.toRadians($$4), Math.toRadians($$5), Math.toRadians($$6));
        this.setSpriteFromAge($$7);
    }

    /**
    @Override
    public void render(VertexConsumer p_233985_, Camera p_233986_, float p_233987_) {
        //this.alpha = 1.0F - Mth.clamp(((float) this.age + p_233987_) / (float) this.lifetime, 0.0F, 1.0F);
        super.render(p_233985_, p_233986_, p_233987_);
    }
     */
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
        this.alpha = 1.0F - Mth.clamp(((float)this.age + $$2) / (float)this.lifetime, 0.0F, 1.0F);
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


    private void makeCornerVertex(VertexConsumer p_254493_, Vector3f p_253752_, float p_254250_, float p_254047_, int p_253814_) {
        p_254493_.vertex((double)p_253752_.x(), (double)p_253752_.y(), (double)p_253752_.z())
                .uv(p_254250_, p_254047_)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(p_253814_)
                .endVertex();
    }

    /***
    public void render(VertexConsumer p_233985_, Camera p_233986_, float p_233987_) {
        this.alpha = 1.0F - Mth.clamp(((float)this.age + p_233987_) / (float)this.lifetime, 0.0F, 1.0F);
        this.renderRotatedParticle(p_233985_, p_233986_, p_233987_, (p_253347_) -> {
            p_253347_.mul((new Quaternionf()).rotationX((float)ROTVECTOR.x));
        });
        this.renderRotatedParticle(p_233985_, p_233986_, p_233987_, (p_253346_) -> {
            p_253346_.mul((new Quaternionf()).rotationYXZ((float)ROTVECTOR.y,(float)ROTVECTOR.y,(float)ROTVECTOR.z));
        });
    }

    private void renderRotatedParticle(VertexConsumer p_233989_, Camera p_233990_, float p_233991_, Consumer<Quaternionf> p_233992_) {
        Vec3 vec3 = p_233990_.getPosition();
        float f = (float)(Mth.lerp((double)p_233991_, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)p_233991_, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)p_233991_, this.zo, this.z) - vec3.z());
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        p_233992_.accept(quaternionf);
        quaternionf.transform(TRANSFORM_VECTOR);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(p_233991_);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternionf);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        int j = this.getLightColor(p_233991_);
        this.makeCornerVertex(p_233989_, avector3f[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(p_233989_, avector3f[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(p_233989_, avector3f[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(p_233989_, avector3f[3], this.getU0(), this.getV1(), j);
    }
    */

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
            AirCrackleParticle part = new AirCrackleParticle($$1, $$2, $$3, $$4, $$5,$$6,$$7, this.sprites);
            part.setAlpha(0.9F);
            return part;
        }
    }
}
