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

public class PWBlastwaveExplosionParticle extends SimpleAnimatedParticle {
    protected PWBlastwaveExplosionParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
    super(clientLevel, d, e, f, spriteSet, 1f);
    this.xd = 0;
    this.yd = h*0.03f;
    this.zd = 0;
    //this.friction = 0.6F;
    this.gravity = 0;
    this.quadSize *= 32f + ((float)this.random.nextInt(5) / 10.0f);
    this.lifetime = 8 + this.random.nextInt(12);
    this.setFadeColor(15916745);
    this.setSpriteFromAge(spriteSet);
}

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }
    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Vec3 cam = camera.getPosition();

        float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - cam.x());
        float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - cam.y());
        float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - cam.z());

        float size = this.getQuadSize(partialTick);

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        int light = this.getLightColor(partialTick);



        Vector3f[] corners = new Vector3f[] {
                new Vector3f(-size, 0, -size),
                new Vector3f(-size, 0, size),
                new Vector3f(size, 0, size),
                new Vector3f(size, 0, -size)
        };


        for (Vector3f corner : corners) {
            corner.add(x, y, z);
        }

        buffer.vertex(corners[0].x(), corners[0].y(), corners[0].z())
                .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();

        buffer.vertex(corners[1].x(), corners[1].y(), corners[1].z())
                .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();

        buffer.vertex(corners[2].x(), corners[2].y(), corners[2].z())
                .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();

        buffer.vertex(corners[3].x(), corners[3].y(), corners[3].z())
                .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) { this.sprites = $$0;}

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new PWBlastwaveExplosionParticle($$1, $$2, $$3, $$4, $$5, $$6, $$7, this.sprites);
        }
    }

}
