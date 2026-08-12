package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class ToothParticle extends TextureSheetParticle {
    protected ToothParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f);
        this.scale(0.3F);
        this.setSize(0.1F, 0.1F);
        this.quadSize *= 0.75f;
        this.lifetime = this.random.nextInt(20) + 40;
        this.gravity = 2f;
        this.setSpriteFromAge(spriteSet);
        this.xd = g;
        this.yd = h;
        this.zd = i;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>{
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {this.spriteSet = spriteSet;}

        @Override
        public @Nullable Particle createParticle(SimpleParticleType $$0, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new ToothParticle(clientLevel, d, e, f, g, h, i, this.spriteSet);
        }
    }
}
