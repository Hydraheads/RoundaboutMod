package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UltravioletProjectile extends RoundaboutGeneralProjectile{
    public UltravioletProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public UltravioletProjectile(LivingEntity $$1, Level $$2) {
        this(ModEntities.ULTRAVIOLET_PROJECTILE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    protected UltravioletProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    public int getMaxLifeSpan(){
        return 20;
    }
    protected UltravioletProjectile(EntityType<RoundaboutGeneralProjectile> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    public final AnimationState ripperEyes = new AnimationState();

    @Override
    public void tick() {
        boolean client = this.level().isClientSide();

        if (!client) {

            // Projectile movement direction
            Vec3 velocity = this.getDeltaMovement();

            // Prevent NaN if entity is barely moving
            if (velocity.lengthSqr() > 0.0001) {

                Vec3 forward = velocity.normalize();

                // Build perpendicular axes
                Vec3 arbitrary = Math.abs(forward.y) < 0.99
                        ? new Vec3(0, 1, 0)
                        : new Vec3(1, 0, 0);

                Vec3 right = forward.cross(arbitrary).normalize();
                Vec3 up = forward.cross(right).normalize();

                // Ring settings
                int particles = 24;
                double radius = 0.6;

                for (int i = 0; i < particles; i++) {

                    double angle = (Math.PI * 2.0 * i) / particles;

                    double x = Math.cos(angle) * radius;
                    double y = Math.sin(angle) * radius;

                    // Position on the ring
                    Vec3 offset = right.scale(x).add(up.scale(y));

                    // Particle world position
                    double px = this.getX() + offset.x;
                    double py = this.getY() + offset.y;
                    double pz = this.getZ() + offset.z;

                    // Outward particle motion
                    Vec3 particleMotion = offset.normalize().scale(0.15);

                    ((ServerLevel)this.level()).sendParticles(
                            ModParticles.UV_SPARKLE,
                            px, py, pz,
                            1,
                            particleMotion.x,
                            particleMotion.y,
                            particleMotion.z,
                            0.0
                    );
                }
            }
        }

        super.tick();
    }

    @Override
    public boolean needsStandUser(){
        return false;
    }
    @Override
    public boolean killAtZero(){
        return false;
    }
}
