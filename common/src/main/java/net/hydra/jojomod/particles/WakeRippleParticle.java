package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class WakeRippleParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final Direction face;

    protected WakeRippleParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.sprites = spriteSet;
        this.age = 0;
        this.lifetime = 6;
        this.quadSize = 1.5F;
        this.setSpriteFromAge(spriteSet);

        // Detect surface normal from xd, yd, zd, or default to UP (horizontal ground)
        if (Math.abs(yd) > 0.5) {
            this.face = yd > 0 ? Direction.UP : Direction.DOWN;
        } else if (Math.abs(xd) > 0.5) {
            this.face = xd > 0 ? Direction.EAST : Direction.WEST;
        } else if (Math.abs(zd) > 0.5) {
            this.face = zd > 0 ? Direction.SOUTH : Direction.NORTH;
        } else {
            this.face = Direction.UP;
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();
        float lerpX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float lerpY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float lerpZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());

        Vector3f[] uvList = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float quadSize = this.getQuadSize(partialTicks);

        Quaternionf rotation = switch (this.face) {
            case UP -> new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90);
            case DOWN -> new Quaternionf().fromAxisAngleDeg(1, 0, 0, -90);
            case NORTH -> new Quaternionf().fromAxisAngleDeg(0, 1, 0, 180);
            case SOUTH -> new Quaternionf();
            case WEST -> new Quaternionf().fromAxisAngleDeg(0, 1, 0, -90);
            case EAST -> new Quaternionf().fromAxisAngleDeg(0, 1, 0, 90);
        };

        float ageOffset = (this.age + partialTicks) * -0.002F;
        float normalX = this.face.getStepX() * ageOffset;
        float normalY = this.face.getStepY() * ageOffset;
        float normalZ = this.face.getStepZ() * ageOffset;

        for (int i = 0; i < 4; i++) {
            Vector3f uv = uvList[i];
            uv.mul(quadSize);
            uv.mul(0.5f, 0.5f, 0.5f);
            uv.rotate(rotation);
            uv.add(lerpX + normalX, lerpY + normalY, lerpZ + normalZ);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(partialTicks);

        // Front face
        vertexConsumer.vertex(uvList[0].x(), uvList[0].y(), uvList[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(uvList[1].x(), uvList[1].y(), uvList[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(uvList[2].x(), uvList[2].y(), uvList[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(uvList[3].x(), uvList[3].y(), uvList[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();

        // Back face (so ripples are visible from any angle)
        vertexConsumer.vertex(uvList[3].x(), uvList[3].y(), uvList[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(uvList[2].x(), uvList[2].y(), uvList[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(uvList[1].x(), uvList[1].y(), uvList[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(uvList[0].x(), uvList[0].y(), uvList[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
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
        this.alpha = Math.max(0.10F, this.alpha - 0.15F);
    }

    @Override
    public int getLightColor(float partialTick) {
        float factor = ((float)this.age + partialTick) / (float)this.lifetime;
        factor = Mth.clamp(factor, 0.0F, 1.0F);
        int light = super.getLightColor(partialTick);
        int blockLight = light & 0xFF;
        int skyLight = light >> 16 & 0xFF;
        blockLight += (int)(factor * 15.0F * 16.0F);
        if (blockLight > 240) {
            blockLight = 240;
        }
        return blockLight | skyLight << 16;
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

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            WakeRippleParticle part = new WakeRippleParticle(level, x, y, z, xd, yd, zd, this.sprites);
            part.setColor(0.99F, 0.99F, 0.99F);
            return part;
        }
    }
}