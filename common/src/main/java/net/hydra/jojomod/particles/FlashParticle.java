package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class FlashParticle extends SimpleAnimatedParticle {
    private final SpriteSet sprites;

    protected FlashParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed,
                            double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, sprites, 1.0f);
        this.age = 0;
        this.lifetime = 4;
        this.friction = 0.96F;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize *= 1.7f;
        this.sprites = sprites;
        this.gravity = 0;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
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

    @Override
    public int getLightColor(float partialTick) {
        return net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z,
                                       double R, double G, double B) {
            FlashParticle particle = new FlashParticle(level, x, y, z, R, G, B, this.sprites);
            particle.setColor((float) (R / 255), (float) (G / 255), (float) (B / 255));
            particle.setAlpha(1);
            return particle;
        }
    }
}