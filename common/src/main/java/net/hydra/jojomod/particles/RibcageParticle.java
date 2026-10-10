package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RibcageParticle extends SimpleAnimatedParticle {
    protected RibcageParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet, 1f);
        this.age = 0;
        this.lifetime = 9;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        float progress = ((float) this.age + partialTicks) / (float) this.lifetime;
        progress = Mth.clamp(progress, 0.0F, 1.0F);

        float startSize = 0.4F;
        float endSize = 0.8F;

        return Mth.lerp(progress, startSize, endSize);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.alpha = Math.max(0.10F, this.alpha - 0.10F);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new RibcageParticle(level, x, y, z, xd, yd, zd, this.sprites);
        }
    }
}
