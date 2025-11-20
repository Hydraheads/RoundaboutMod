package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
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

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("SuperThrown", this.entityData.get(ROUNDABOUT$SUPER_THROWN));
        tag.putByte("AmmoType", this.entityData.get(AMMO_TYPE));
        tag.putBoolean("TimeStopShot", this.timeStopShot);
        tag.putInt("OutsideTimeStop", this.outsideOfTimeStop);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, tag.getBoolean("SuperThrown"));
        this.entityData.set(AMMO_TYPE, tag.getByte("AmmoType"));
        this.timeStopShot = tag.getBoolean("TimeStopShot");
        this.outsideOfTimeStop = tag.getInt("OutsideTimeStop");
    }


    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(RoundaboutBulletEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> AMMO_TYPE = SynchedEntityData.defineId(RoundaboutBulletEntity.class, EntityDataSerializers.BYTE);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ROUNDABOUT$SUPER_THROWN, true);
        this.entityData.define(AMMO_TYPE, (byte) 0);
    }

    public final void setAmmoType(byte ammoType) {
        this.entityData.set(AMMO_TYPE, ammoType);
    }
    public byte getAmmoType() {
        return this.entityData.get(AMMO_TYPE);
    }

    public static final byte NONE = 0;
    public static final byte REVOLVER = 1;
    public static final byte TOMMY_GUN = 2;
    public static final byte SNIPER = 3;

    public ItemStack getBulletItemStack() {
        ItemStack itemStack;
        if (getAmmoType() == NONE) {
            return ItemStack.EMPTY;
        } else if (getAmmoType() == REVOLVER) {
            itemStack = ModItems.SNUBNOSE_AMMO.getDefaultInstance();
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    public void applyEffect(LivingEntity target) {
        if (!MainUtil.isBossMob(target) && !(target instanceof RoadRollerEntity)) {
            if (MainUtil.getMobBleed(target)) {
                ((StandUser) target).roundabout$setBleedLevel(1);
                target.addEffect(new MobEffectInstance(ModEffects.BLEED, 400, 0), this);
            }
        }
    }

    boolean timeStopShot = false;
    int outsideOfTimeStop = 0;

    @Override
    public void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (entity instanceof LivingEntity $$3) {
            StandPowers entityPowers = ((StandUser) $$3).roundabout$getStandPowers();
            if (entityPowers != null ) {
                if (entityPowers.dealWithProjectile(this, result)) {
                    return;
                }
            }
        }

        if (entity instanceof LivingEntity livingEntity) {
            if ((livingEntity.hurtTime > 0 || livingEntity.invulnerableTime > 0) && outsideOfTimeStop == 0) {
                return;
            } else if (livingEntity.isInvulnerable()) {
                return;
            } else if (outsideOfTimeStop > 0) {
                entity.invulnerableTime = 0;
            }

            boolean didDamage;
            float bulletDamage = timeStopShot ? 3.7F : 4.0F;

            didDamage = livingEntity.hurt(ModDamageTypes.of(level(), ModDamageTypes.BULLET, this, this.getOwner()), bulletDamage);

            if (didDamage) {
                applyEffect(livingEntity);
            }
        }

        Entity $$2 = this.getOwner();
        DamageSource damageSource = ModDamageTypes.of(level(), ModDamageTypes.BULLET, this, $$2);

        if ($$2 instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(entity);
        }

        this.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.BULLET_PENTRATION_EVENT, this.getSoundSource(), 1.0F, 1.0F);
        this.discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.BULLET_PENTRATION_EVENT;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
    }

    @Override
    public void tick() {
        Vec3 delta = this.getDeltaMovement();

        if (((IProjectileAccess) this).roundabout$getRoundaboutIsTimeStopCreated()) {
            timeStopShot = true;
        }

        if (timeStopShot) {
            if (outsideOfTimeStop < 5) {
                outsideOfTimeStop++;
            } else if (outsideOfTimeStop >= 5) {
                timeStopShot = false;
                outsideOfTimeStop = 0;
            }
        }

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

    public void blockBreakParticles(Block block, Vec3 pos){
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                            block.defaultBlockState()),
                    pos.x, pos.y, pos.z,
                    100, 0, 0, 0, 0.5);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (!level().isClientSide) {
            Block blkk = this.level().getBlockState($$0.getBlockPos()).getBlock();
            if (blkk instanceof AbstractGlassBlock || blkk instanceof StainedGlassPaneBlock
                    || blkk.defaultBlockState().is(Blocks.GLASS_PANE)){
                if (this.level().removeBlock($$0.getBlockPos(),false)){
                    blockBreakParticles(blkk,
                            new Vec3($$0.getBlockPos().getX()+0.5,
                                    $$0.getBlockPos().getY()+0.5,
                                    $$0.getBlockPos().getZ()+0.5));
                    this.playSound(blkk.defaultBlockState().getSoundType().getBreakSound(), 1.0F, 0.9F);
                    return;
                }
            }
        }

        super.onHitBlock($$0);
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
