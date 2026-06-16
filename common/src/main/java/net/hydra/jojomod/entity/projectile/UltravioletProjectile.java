package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Zombiefish;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

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

    public boolean alreadyHitEntity(Entity entity){
        return alreadyHitEntities.contains(entity);
    }

    public List<Entity> alreadyHitEntities = new ArrayList<>();

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!level().isClientSide()) {
            Entity ent = $$0.getEntity();
            if (ent != null && ent.isAlive()) {
                if (!alreadyHitEntity($$0.getEntity())) {
                    blastEntity(ent);
                }
            }
        }
    }
    public void blastEntity(Entity entity){
        //Add hurt code here
        //Roundabout.LOGGER.info("charge-> "+charge+" power-> "+power);
        if (entity instanceof LivingEntity lv) {
            if (entity instanceof BaseMinion || entity instanceof Zombiefish  ||
                    FateTypes.isVampire(lv)  ||
                    FateTypes.isZombie(lv)) {
                float power = 10;

                boolean isFullVampire = entity instanceof Player pl && (((IFatePlayer)pl).rdbt$getFatePowers() instanceof
                        VampireFate vf && vf.getVampireData().freezeLevel > 0) &&
                        PowerTypes.hasPowerActivelyEquipped(pl) &&
                        ((IPowersPlayer)pl).rdbt$getPowers() instanceof VampireGeneralPowers vgp;
                if (!isFullVampire){
                    if (DamageHandler.UVDamage(entity, power, getOwner())) {
                        lv.addEffect(new MobEffectInstance(ModEffects.SINGE, 200, 0));
                    }
                } else {
                    lv.addEffect(new MobEffectInstance(ModEffects.SINGE, 100, 0));
                }
            } else if ((lv instanceof Zombie zb && !(zb instanceof Husk)) || lv instanceof Phantom || lv instanceof Skeleton){
                lv.setSecondsOnFire(8);

            }
        }
        alreadyHitEntities.add(entity);
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!level().isClientSide()) {
            discard();
        }
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
