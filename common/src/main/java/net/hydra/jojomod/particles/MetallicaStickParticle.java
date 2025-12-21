package net.hydra.jojomod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.Minecraft;

public class MetallicaStickParticle extends TextureSheetParticle {

    private final Entity targetEntity;
    private final double relativeX, relativeY, relativeZ;
    private final SpriteSet spriteSet;

    protected MetallicaStickParticle(ClientLevel level, double x, double y, double z, Entity target, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.targetEntity = target;
        this.spriteSet = spriteSet;


        if (target != null) {
            this.relativeX = x - target.getX();
            this.relativeY = y - target.getY();
            this.relativeZ = z - target.getZ();
        } else {
            this.relativeX = 0; this.relativeY = 0; this.relativeZ = 0;
        }

        this.lifetime = 35;
        this.quadSize *= 0.75f;
        this.gravity = 0;
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
        this.alpha = 1.0f;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            if (targetEntity == null) {
                this.setSpriteFromAge(this.spriteSet);
                return;
            }

            if (targetEntity.isAlive()) {
                this.setPos(targetEntity.getX() + relativeX, targetEntity.getY() + relativeY, targetEntity.getZ() + relativeZ);

                this.setSpriteFromAge(this.spriteSet);

                Minecraft mc = Minecraft.getInstance();
                boolean isMeInFirstPerson = (targetEntity == mc.player && mc.options.getCameraType().isFirstPerson());
                if (isMeInFirstPerson) {
                    this.setAlpha(0.25f);
                } else {
                    this.setAlpha(1.0f);
                }
            } else {
                this.remove();
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            int entityId = (int) xSpeed;

            Entity target = null;
            if (entityId > 0) {
                target = level.getEntity(entityId);
            }

            return new MetallicaStickParticle(level, x, y, z, target, spriteSet);
        }
    }
}