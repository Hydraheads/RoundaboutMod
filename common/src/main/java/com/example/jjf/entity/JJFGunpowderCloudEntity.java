package com.example.jjf.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class JJFGunpowderCloudEntity extends Entity {

    private static final int MAX_LIFE_TICKS = 100;
    private static final float EXPLOSION_POWER = 3.0F;

    private boolean exploded;

    public JJFGunpowderCloudEntity(
            EntityType<? extends JJFGunpowderCloudEntity> type,
            Level level
    ) {
        super(
                type,
                level
        );
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            for (int i = 0; i < 3; i++) {
                level().addParticle(
                        ParticleTypes.ASH,
                        getX()
                                + (random.nextDouble() - 0.5D)
                                * getBbWidth(),
                        getY()
                                + random.nextDouble()
                                * getBbHeight(),
                        getZ()
                                + (random.nextDouble() - 0.5D)
                                * getBbWidth(),
                        0.0D,
                        0.01D,
                        0.0D
                );
            }

            return;
        }

        if (tickCount >= MAX_LIFE_TICKS) {
            discard();
            return;
        }

        if (isOnFire()
                || isInLava()
                || level().getBlockState(
                blockPosition()
        ).is(Blocks.FIRE)
                || level().getBlockState(
                blockPosition()
        ).is(Blocks.SOUL_FIRE)) {
            explode();
        }
    }

    @Override
    public boolean hurt(
            DamageSource source,
            float amount
    ) {
        if (level().isClientSide()) {
            return true;
        }

        explode();

        return true;
    }

    private void explode() {
        if (exploded
                || level().isClientSide()) {
            return;
        }

        exploded = true;

        level().explode(
                this,
                getX(),
                getY(),
                getZ(),
                EXPLOSION_POWER,
                false,
                Level.ExplosionInteraction.TNT
        );

        discard();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(
            CompoundTag tag
    ) {
        exploded =
                tag.getBoolean(
                        "Exploded"
                );
    }

    @Override
    protected void addAdditionalSaveData(
            CompoundTag tag
    ) {
        tag.putBoolean(
                "Exploded",
                exploded
        );
    }
}