package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class BriefMagicDustParticle extends SimpleAnimatedParticle {
    BriefMagicDustParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet, 1f);
        this.quadSize = 0.4f;
        this.friction = 0.96F;
        this.gravity = 0F;
        this.xd = this.xd * 0.01F + g;
        this.yd = this.yd * 0.01F + h;
        this.zd = this.zd * 0.01F + i;
        this.lifetime = 4;
        this.setAlpha(0.15f);
        this.setSpriteFromAge(spriteSet);
    }
    @Override
    public void tick() {
        super.tick();
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {

            float alphaThis = ConfigManager.getClientConfig().invisibleBlockDepthF;
            if (alphaThis > 0) {
                double maxdist = ConfigManager.getClientConfig().invisibleBlocksDistanceAwaySeenI;
                double distance = new Vec3(this.x, this.y, this.z).distanceTo(ClientUtil.getPlayer().position());
                distance = Mth.clamp(distance, 0, maxdist);
                distance /= maxdist;
                distance *= alphaThis;

                this.setAlpha(0+(float) (alphaThis - distance));
                super.render($$0, $$1, $$2);
            }
        }
    }


    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) {
            this.sprites = $$0;
        }

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new BriefMagicDustParticle($$1, $$2, $$3, $$4, $$5, $$6, $$7, this.sprites);
        }
    }
}
