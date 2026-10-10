package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnergyRippleSurfaceParticle extends SimpleAnimatedParticle {
    public static final Map<ParticleKey, EnergyRippleSurfaceParticle> ACTIVE_RIPPLES = new ConcurrentHashMap<>();

    private final SpriteSet sprites;
    private final Direction face;
    public record ParticleKey(BlockPos pos, Direction face) {}

    public EnergyRippleSurfaceParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet, 1f);
        this.sprites = spriteSet;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.friction = 1.0F;
        this.gravity = 0;
        this.quadSize = 0.95F;
        this.hasPhysics = false;
        this.lifetime = 14;
        this.setAlpha(0.35F);

        // for orientation
        double fracX = Math.abs(d - (Math.floor(d) + 0.5));
        double fracY = Math.abs(e - (Math.floor(e) + 0.5));
        double fracZ = Math.abs(f - (Math.floor(f) + 0.5));

        if (fracY > fracX && fracY > fracZ) {
            this.face = (e > Math.floor(e) + 0.5) ? Direction.UP : Direction.DOWN;
        } else if (fracX > fracY && fracX > fracZ) {
            this.face = (d > Math.floor(d) + 0.5) ? Direction.EAST : Direction.WEST;
        } else {
            this.face = (f > Math.floor(f) + 0.5) ? Direction.SOUTH : Direction.NORTH;
        }

        this.setSprite(this.sprites.get(0, 6));
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();
        float lerpX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x());
        float lerpY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y());
        float lerpZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z());

        Vector3f[] uvList = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
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

        for (int i = 0; i < 4; i++) {
            Vector3f uv = uvList[i];
            uv.mul(quadSize);
            uv.mul(0.5f,0.5f,0.5f);
            uv.rotate(rotation);
            uv.add(lerpX, lerpY, lerpZ);
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
            return;
        }

        int ticksPerFrame = 1;
        int step = this.age / ticksPerFrame;
        int holdSteps = 1;

        int frameIndex;
        if (step <= 6) {
            frameIndex = step;
        } else if (step <= 6 + holdSteps) {
            frameIndex = 6;
        } else {
            frameIndex = Math.max(0, 6 - (step - (6 + holdSteps)));
        }

        this.setSprite(this.sprites.get(frameIndex, 6));
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new EnergyRippleSurfaceParticle(level, x, y, z, xd, yd, zd, this.sprites);
        }
    }
}
