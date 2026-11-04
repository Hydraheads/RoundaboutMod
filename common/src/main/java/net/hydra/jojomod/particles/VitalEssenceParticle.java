package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class VitalEssenceParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final int frameOneDuration;

    VitalEssenceParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(clientLevel, x, y, z);

        this.sprites = spriteSet;

        // size is randomized
        this.quadSize = 0.1F + (this.random.nextFloat() * 0.2F);

        // colors are slightly randomized
        this.rCol = 0.8F + this.random.nextFloat() * 0.2F;
        this.gCol = 0.8F + this.random.nextFloat() * 0.2F;
        this.bCol = 0.8F + this.random.nextFloat() * 0.2F;

        // for the gravity
        this.gravity = -0.07F;
        this.friction = 0.96F;

        // velocity is randomized
        this.xd = xSpeed + (Math.random() - 0.5D) * 0.1D;
        this.yd = ySpeed + (Math.random() - 0.5D) * 0.1D;
        this.zd = zSpeed + (Math.random() - 0.5D) * 0.1D;

        // animations are randomized
        this.frameOneDuration = 10 + this.random.nextInt(20);
        this.lifetime = this.frameOneDuration + 6;

        this.setSprite(this.sprites.get(0, 4));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.removed) {
            int frameIndex;

            // particles have staggered update times for the first frame, then rapidly advance through the rest
            if (this.age < this.frameOneDuration) {
                frameIndex = 0;
            } else {
                int fastAge = this.age - this.frameOneDuration;
                frameIndex = 1 + (fastAge / 2);

                if (frameIndex > 3) {
                    frameIndex = 3;
                }

                float currentAlpha = 1.0F - ((float) fastAge / 6.0F);
                this.setAlpha(Math.max(0.0F, currentAlpha));
            }

            this.setSprite(this.sprites.get(frameIndex, 4));
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
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

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VitalEssenceParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}