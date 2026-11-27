package net.hydra.jojomod.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.hydra.jojomod.client.models.stand.renderers.GoldExperienceBaseRenderer;
import net.hydra.jojomod.particles.GESkinCrackParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class GESkinCrackParticle extends SingleQuadParticle {
    private static final float MIN_U = 0.125F;
    private static final float MAX_U = 0.205F;
    private static final float MIN_V = 0.010F;
    private static final float MAX_V = 0.105F;
    private static final float PATCH_SIZE = 0.045F;

    private final ResourceLocation skinTexture;
    private final float u0;
    private final float u1;
    private final float v0;
    private final float v1;

    public GESkinCrackParticle(ClientLevel level, double x, double y, double z,
                               double xSpeed, double ySpeed, double zSpeed, byte skin) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.skinTexture = GoldExperienceBaseRenderer.getSkin(skin);

        this.gravity = 1.0F;
        this.quadSize /= 2.0F;

        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
        double speedScale = (Math.random() + Math.random() + 1.0) * 0.15F;
        double len = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
        if (len > 1.0E-5) {
            this.xd = this.xd / len * speedScale * 0.4F;
            this.yd = this.yd / len * speedScale * 0.4F + 0.1F;
            this.zd = this.zd / len * speedScale * 0.4F;
        }

        // crops a random square from the texture
        this.u0 = MIN_U + this.random.nextFloat() * (MAX_U - MIN_U - PATCH_SIZE);
        this.u1 = this.u0 + PATCH_SIZE;
        this.v0 = MIN_V + this.random.nextFloat() * (MAX_V - MIN_V - PATCH_SIZE);
        this.v1 = this.v0 + PATCH_SIZE;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderTexture(0, this.skinTexture);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        super.render(builder, camera, partialTicks);
        tesselator.end();
    }

    @Override
    protected float getU0() {
        return this.u0;
    }

    @Override
    protected float getU1() {
        return this.u1;
    }

    @Override
    protected float getV0() {
        return this.v0;
    }

    @Override
    protected float getV1() {
        return this.v1;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Provider implements ParticleProvider<GESkinCrackParticleOptions> {
        public Provider() {
        }

        public Provider(SpriteSet sprites) {
        }

        @Override
        public Particle createParticle(GESkinCrackParticleOptions options, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new GESkinCrackParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.skin());
        }
    }
}