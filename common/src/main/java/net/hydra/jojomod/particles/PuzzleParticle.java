package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;

public class PuzzleParticle extends TextureSheetParticle {

    protected PuzzleParticle(ClientLevel level, double xPos, double yPos, double zPos, SpriteSet set,
                             double xd, double yd, double zd) {
        super(level, xPos, yPos, zPos, xd, yd, zd);
        int ticleInterval = ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingParticleSpawnInterval;

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = Math.max(ticleInterval, 10);
        setSpriteFromAge(set);
    }

    @Override
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            super.render($$0, $$1, $$2);
        }
    }

    @Override
    public void setSpriteFromAge(SpriteSet $$0) {
        if (!this.removed) {
            this.setSprite($$0.get(this.random));
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<ColorParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ColorParticleOptions options, ClientLevel level,
                                       double x, double y, double z, double xd, double yd, double zd) {
            PuzzleParticle particle = new PuzzleParticle(level, x, y, z, sprites, xd, yd, zd);
            particle.setColor(options.getR(), options.getG(), options.getB());
            return particle;
        }
    }
}
