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


public class TuskHoleParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected TuskHoleParticle(ClientLevel $$0, double $$1, double $$2, double $$3, double $$4, double $$5, double $$6, SpriteSet $$7) {
        super($$0, $$1, $$2, $$3, 0.0, 0.0, 0.0);
        this.age = 0;
        this.lifetime = 8;
        this.quadSize = 0.55F;
        this.sprites = $$7;
        this.setSpriteFromAge($$7);
    }
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();
        float lerpX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float lerpY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float lerpZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());

        Vector3f[] uvList = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float quadSize = this.getQuadSize(partialTicks);

        for (int i = 0; i < 4; i++) {
            Vector3f uv = uvList[i];
            uv.mul(quadSize);
            uv.mul(0.5f,0.5f,0.5f);
            uv.rotate(new Quaternionf().fromAxisAngleDeg(1,0,0,90));
            uv.add(lerpX, lerpY, lerpZ);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(partialTicks);

        vertexConsumer.vertex(uvList[0].x(), uvList[0].y(), uvList[0].z())
                .uv(u1, v1)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
                .endVertex();

        vertexConsumer.vertex(uvList[1].x(), uvList[1].y(), uvList[1].z())
                .uv(u1, v0)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
                .endVertex();

        vertexConsumer.vertex(uvList[2].x(), uvList[2].y(), uvList[2].z())
                .uv(u0, v0)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
                .endVertex();

        vertexConsumer.vertex(uvList[3].x(), uvList[3].y(), uvList[3].z())
                .uv(u0, v1)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
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
            TuskHoleParticle part = new TuskHoleParticle($$1, $$2, $$3, $$4, $$5,$$6,$$7, this.sprites);
            part.setColor(0.99F, 0.99F, 0.99F);
            return part;
        }
    }
}
