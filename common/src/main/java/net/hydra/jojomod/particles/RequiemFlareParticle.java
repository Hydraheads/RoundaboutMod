package net.hydra.jojomod.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class RequiemFlareParticle extends TextureSheetParticle {
    private final float maxScale;
    private final float rotSpeed;

    public RequiemFlareParticle(ClientLevel level, double x, double y, double z,
                                double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.pickSprite(sprites);

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.gravity = 0.0F;
        this.hasPhysics = false;

        this.lifetime = 38;
        this.maxScale = 0.42F + this.random.nextFloat() * 0.25F;
        this.quadSize = 0.02F;

        this.roll = (this.random.nextFloat() - 0.5F) * 0.35F;
        this.oRoll = this.roll;
        this.rotSpeed = (this.random.nextFloat() - 0.5F) * 0.03F;

        this.rCol = 1.0F;
        this.gCol = 0.78F;
        this.bCol = 0.28F;
        this.alpha = 1.0F;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        float progress = Mth.clamp((this.age + partialTicks) / (float) this.lifetime, 0.0F, 1.0F);
        if (progress < 0.35F) {
            float grow = progress / 0.35F;
            return this.maxScale * (1.0F - (1.0F - grow) * (1.0F - grow));
        } else if (progress < 0.85F) {
            return this.maxScale;
        } else {
            float shrink = (progress - 0.85F) / 0.85F;
            return this.maxScale * (1.0F - shrink);
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        this.roll += this.rotSpeed;

        float progress = (float) this.age / (float) this.lifetime;
        this.alpha = progress > 0.85F ? Mth.clamp((1.0F - progress) / 0.15F, 0.0F, 1.0F) : 1.0F;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        super.render(builder, camera, partialTicks);
        tesselator.end();

        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new RequiemFlareParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}