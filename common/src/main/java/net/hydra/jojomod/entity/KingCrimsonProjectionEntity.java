package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.navigation.ActiveCloneManager;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class KingCrimsonProjectionEntity extends CloneEntity {
    public KingCrimsonProjectionEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public int fadeInTick = 0;
    public int maxFadeInTick = 10;

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (!this.level().isClientSide()){
            if ($$0.getEntity() != null && this.tickCount >= 10) {
                spawnDeathParticles();
                pkc.addEXP(7);
                discard();
            }
        }
        return false;
    }

    public boolean isPickable(){
        return false;
    }
    public boolean contains = false;
    @Override
    public void tick() {
        if (!level().isClientSide()) {
            if (lifespan > 0) {
                lifespan--;
                if (lifespan == 0) {
                    spawnDeathParticles();
                    discard();
                }
            }
            if (!isRemoved()){
                if (pkc != null && (pkc.isErasingTime() || pkc.isUsingEpitaph())){
                    spawnDeathParticles();
                    discard();
                } else if (user == null || pkc == null || !user.isAlive()){
                    spawnDeathParticles();
                    discard();
                }
            }
            if (!isRemoved()){
                for (Projectile projectile : level().getEntitiesOfClass(
                        Projectile.class,
                        this.getBoundingBox().inflate(3.0))) {

                    Vec3 from = projectile.position().subtract(projectile.getDeltaMovement());
                    Vec3 to = projectile.position();

                    if (this.getBoundingBox().inflate(0.2).clip(from, to).isPresent()) {
                        spawnDeathParticles();
                        discard();
                        return;
                    }
                }
            }
            if (!contains) {
                contains = true;
                ActiveCloneManager.add(this);
            }
        } else {
            if (fadeInTick < maxFadeInTick){
            fadeInTick++;
            }
        }
        super.tick();
    }


    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void doPush(Entity $$0) {
    }
    @Override
    public void push(Entity $$0) {
    }
    @Override
    public boolean canCollideWith(Entity $$0) {
        return false;
    }

    public Player user = null;


    public PowersKingCrimson pkc = null;
    public void spawnDeathParticles() {
        if (!level().isClientSide()) {
            if (!isRemoved()) {
                if (pkc != null) {
                    pkc.playStandUserOnlySoundsIfNearby(PowersKingCrimson.EPITAPH_PROJECTION_2, 40, true, false);
                }
                ServerLevel level = (ServerLevel) this.level();

                float radius = this.getBbWidth() * 0.65F;
                float height = this.getBbHeight();

                int rings = 14;
                int particlesPerRing = 18;

                for (int ring = 0; ring < rings; ring++) {

                    double y = this.getY() + (height * ring / (rings - 1));

                    // Twist every ring a little
                    double offset = (ring * Math.PI / particlesPerRing);

                    for (int i = 0; i < particlesPerRing; i++) {

                        double angle = offset + (Math.PI * 2.0 * i / particlesPerRing);

                        double x = this.getX() + Math.cos(angle) * radius;
                        double z = this.getZ() + Math.sin(angle) * radius;

                        level.sendParticles(
                                DustParticleOptions.REDSTONE,
                                x, y, z,
                                1,
                                0, 0, 0,
                                0
                        );
                    }
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("lifespan",lifespan);
    }
    public int lifespan = -1;
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("lifespan")) {
            lifespan = tag.getInt("lifespan");
        }
    }

}
