package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class RoundaboutBulletEntity extends AbstractArrow {

    public RoundaboutBulletEntity(EntityType<? extends RoundaboutBulletEntity> type, Level level) {
        super(type, level);
    }

    public RoundaboutBulletEntity(Level level, LivingEntity livingEntity) {
        super(ModEntities.ROUNDABOUT_BULLET_ENTITY, livingEntity, level);
    }
    public RoundaboutBulletEntity(Level $$0, LivingEntity $$1, ItemStack $$2, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.ROUNDABOUT_BULLET_ENTITY, p_36862_, p_36863_, p_36864_, $$0);
    }

    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.BOOLEAN);

    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.getEntityData().hasItem(ROUNDABOUT$SUPER_THROWN)) {
            this.getEntityData().define(ROUNDABOUT$SUPER_THROWN, true);
        }
    }

    @Override
    public void tick() {
        Vec3 delta = this.getDeltaMovement();

        if (inGroundTime >= 160) {
            this.remove(RemovalReason.DISCARDED);
        }

        if(this.isInWater()) {
            this.entityData.set(ROUNDABOUT$SUPER_THROWN,false);
        }

        super.tick();

        if (this.getEntityData().get(ROUNDABOUT$SUPER_THROWN)) {
            this.setDeltaMovement(delta);
        }

        if (level().isClientSide) {
            boolean isFlying = getDeltaMovement().lengthSqr() > 0.01;

            if (isFlying) {
                if (this.tickCount%80 ==1) {
                    if (!((TimeStop) this.level()).inTimeStopRange(this)) {
                        // ClientUtil.handleBowlerHatFlySound(this);
                    }
                }
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
    }

    @Override
    protected boolean tryPickup(Player $$0) {
        return false;
    }

    @Override
    public void tickDespawn() {
        int $$0 = 1;
        if (this.pickup != Pickup.ALLOWED || $$0 <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double $$0, double $$1, double $$2) {
        return true;
    }
}
