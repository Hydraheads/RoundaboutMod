package net.hydra.jojomod.particles;

import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class DiscStealHitParticle extends PunchImpactParticle {
    protected DiscStealHitParticle(ClientLevel level, double x, double y, double z,
                                   double velocityX, double velocityY, double velocityZ, SpriteSet sprites) {
        super(level, x, y, z, velocityX, velocityY, velocityZ, sprites);
        this.quadSize *= 0.6F;
        this.lifetime = 10;
        this.setSpriteFromAge(sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            DiscStealHitParticle particle = new DiscStealHitParticle(level, x, y, z,
                    velocityX, velocityY, velocityZ, sprites);
            particle.setColor(0.9F, 0.9F, 0.9F);
            if (ConfigManager.getClientConfig() != null
                    && ConfigManager.getClientConfig().particleSettings != null) {
                particle.setAlpha(ConfigManager.getClientConfig().particleSettings.punchImpactOpacity);
            }
            return particle;
        }
    }
}
