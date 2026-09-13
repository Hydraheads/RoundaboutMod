package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PurpleHazeSmokeParticle extends SimpleAnimatedParticle {
    protected PurpleHazeSmokeParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet, 1f);
        this.xd = 0;
        this.yd = h * 0.03f;
        this.zd = 0;
        this.gravity = 0;
        this.quadSize *= 2f + ((float) this.random.nextInt(5) / 10.0f);
        this.lifetime = 8 + this.random.nextInt(12);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<HazeColorParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(HazeColorParticleOptions options, ClientLevel level,
                                       double x, double y, double z, double xd, double yd, double zd) {
            PurpleHazeSmokeParticle particle = new PurpleHazeSmokeParticle(level, x, y, z, xd, yd, zd, sprites);
            particle.setColor(options.getR(), options.getG(), options.getB());
            particle.setFadeColor(0x1A1A1A);
            return particle;
        }
    }
}