package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class RoadRollerSmokeParticle extends TextureSheetParticle {
    protected RoadRollerSmokeParticle(ClientLevel level, double x, double y, double z,
                                      double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);

        this.pickSprite(sprites);
        this.gravity = 0.8F;
        this.lifetime = (int)(16 + random.nextFloat() * 8);
        this.quadSize = 0.6F;
        this.setSize(1.0F, 1.0F);

        this.xd = (random.nextDouble() - 0.5D) * 2.4D;
        this.yd = 0.5D + random.nextDouble() * 0.9D;
        this.zd = (random.nextDouble() - 0.5D) * 2.4D;
    }

    @Override
    public void tick() {
        super.tick();

        this.xd *= 0.97;
        this.yd -= 0.04D * this.gravity;
        this.zd *= 0.97;

        float ageRatio = (float) this.age / (float) this.lifetime;
        this.alpha = ageRatio < 0.75F ? 1.0F : 1.0F - (ageRatio - 0.75F) / 0.25F;

        if (this.onGround) {
            this.remove();
        }
    }

    @Override
    public net.minecraft.client.particle.ParticleRenderType getRenderType() {
        return net.minecraft.client.particle.ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            return new RoadRollerSmokeParticle(level, x, y, z, vx, vy, vz, sprites);
        }
    }
}
