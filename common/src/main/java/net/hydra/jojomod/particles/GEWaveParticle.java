package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ModRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GEWaveParticle extends Particle {
    private static final float MAX_RADIUS = 7.0F;
    private static final float RING_WIDTH = 0.75F;
    private static final Vector3f START_COLOR = new Vector3f(1.0F, 210 / 255F, 17 / 255F);
    private static final Vector3f END_COLOR = new Vector3f(1.0F, 173 / 255F, 13 / 255F);

    protected GEWaveParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.lifetime = 10; // matches the bush expansion
        this.hasPhysics = false;
        this.setBoundingBox(this.getBoundingBox().inflate(MAX_RADIUS, 1.0, MAX_RADIUS));
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    //this is actually pretty similar to the bullet trail thing under "TrailRenderer".
    // You can read both of these for more info on how to make this
    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        float progress = Mth.clamp((this.age + partialTicks) / (float) this.lifetime, 0.0F, 1.0F);
        float eased = 1.0F - (1.0F - progress) * (1.0F - progress);

        float outerRadius = eased * MAX_RADIUS;
        float innerRadius = Math.max(0.0F, outerRadius - RING_WIDTH);
        float outerAlpha = (1.0F - progress) * 0.85F;

        float r = Mth.lerp(progress, START_COLOR.x, END_COLOR.x);
        float g = Mth.lerp(progress, START_COLOR.y, END_COLOR.y);
        float b = Mth.lerp(progress, START_COLOR.z, END_COLOR.z);

        Vec3 camPos = camera.getPosition();
        float cx = (float) (this.x - camPos.x);
        float cy = (float) (this.y + 0.04 - camPos.y);
        float cz = (float) (this.z - camPos.z);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(ModRenderTypes.TRAIL_RENDER_TYPE);

        int segments = 48;
        int light = LightTexture.FULL_BRIGHT;

        for (int i = 0; i < segments; i++) {
            double a0 = (2.0 * Math.PI * i) / segments;
            double a1 = (2.0 * Math.PI * (i + 1)) / segments;

            float cos0 = (float) Math.cos(a0), sin0 = (float) Math.sin(a0);
            float cos1 = (float) Math.cos(a1), sin1 = (float) Math.sin(a1);

            putVertex(consumer, cx + cos0 * innerRadius, cy, cz + sin0 * innerRadius, r, g, b, 0.0F, light);
            putVertex(consumer, cx + cos0 * outerRadius, cy, cz + sin0 * outerRadius, r, g, b, outerAlpha, light);
            putVertex(consumer, cx + cos1 * outerRadius, cy, cz + sin1 * outerRadius, r, g, b, outerAlpha, light);
            putVertex(consumer, cx + cos1 * innerRadius, cy, cz + sin1 * innerRadius, r, g, b, 0.0F, light);
        }

        bufferSource.endBatch(ModRenderTypes.TRAIL_RENDER_TYPE);
    }

    private static void putVertex(VertexConsumer consumer, float x, float y, float z,
                                  float r, float g, float b, float a, int light) {
        consumer.vertex(x, y, z)
                .color(r, g, b, a)
                .uv(0.0F, 0.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(SpriteSet sprites) {}

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new GEWaveParticle(level, x, y, z);
        }
    }
}