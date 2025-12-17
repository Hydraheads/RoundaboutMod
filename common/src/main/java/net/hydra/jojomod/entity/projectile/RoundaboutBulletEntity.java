package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

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
        tag.putInt("SuperThrownTimer", this.superThrownTimer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, tag.getBoolean("SuperThrown"));
        this.entityData.set(AMMO_TYPE, tag.getByte("AmmoType"));
        this.timeStopShot = tag.getBoolean("TimeStopShot");
        this.outsideOfTimeStop = tag.getInt("OutsideTimeStop");
        this.superThrownTimer = tag.getInt("SuperThrownTimer");
    }


    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(RoundaboutBulletEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> AMMO_TYPE = SynchedEntityData.defineId(RoundaboutBulletEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DEFLECTED = SynchedEntityData.defineId(RoundaboutBulletEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ROUNDABOUT$SUPER_THROWN, true);
        this.entityData.define(AMMO_TYPE, (byte) 0);
        this.entityData.define(DEFLECTED, false);
    }

    public boolean getSuperThrown() {
        return this.entityData.get(ROUNDABOUT$SUPER_THROWN);
    }

    public void setSuperThrown(boolean value) {
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, value);
    }

    public boolean getDeflected() {
        return this.entityData.get(DEFLECTED);
    }

    public void setDeflected(boolean value) {
        this.entityData.set(DEFLECTED, value);
    }

    public final void setAmmoType(byte ammoType) {
        this.entityData.set(AMMO_TYPE, ammoType);
    }
    public byte getAmmoType() {
        return this.entityData.get(AMMO_TYPE);
    }

    public static final byte NONE = 0;
    public static final byte SNUBNOSE = 1;
    public static final byte TOMMY_GUN = 2;
    public static final byte SNIPER = 3;
    public static final byte COLT = 4;

    public ItemStack getBulletItemStack() {
        ItemStack itemStack;
        if (getAmmoType() == NONE) {
            return ItemStack.EMPTY;
        } else if (getAmmoType() == SNUBNOSE) {
            itemStack = ModItems.SNUBNOSE_AMMO.getDefaultInstance();
            return itemStack;
        } else if (getAmmoType() == TOMMY_GUN) {
            itemStack = ModItems.TOMMY_AMMO.getDefaultInstance();
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

    private int superThrownTimer = 0;

    private float getBulletDamage() {
        return switch (getAmmoType()) {
            case SNUBNOSE -> timeStopShot ? 3.7F : 4.0F;
            case TOMMY_GUN -> timeStopShot ? 0.74F : 0.82F;
            case SNIPER -> timeStopShot ? 3.7F : 4.0F;
            case COLT -> timeStopShot ? 3.8F : 4.7F;
            default -> 0.0F;
        };
    }



    @Override
    public void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (!level().isClientSide && entity instanceof EnderMan em) {

            if (((IEnderMan) em).roundabout$teleport()) return;

            for (int i = 0; i < 64; i++) {
                if (((IEnderMan) em).roundabout$teleport()) {
                    return;
                }
            }
        }

        if (entity instanceof LivingEntity $$3) {
            StandPowers entityPowers = ((StandUser) $$3).roundabout$getStandPowers();
            if (entityPowers != null ) {
                if (entityPowers.dealWithProjectile(this, result)) {
                    return;
                }
            }
        }

        if (entity instanceof LivingEntity livingEntity) {

            boolean hadIFrames = livingEntity.invulnerableTime > 0 || livingEntity.hurtTime > 0;

            if (livingEntity.isInvulnerable()) {
                return;
            }

            if (outsideOfTimeStop > 0 || getAmmoType() == TOMMY_GUN) {
                livingEntity.hurtTime = 0;
                livingEntity.invulnerableTime = 0;
            }

            if (getAmmoType() == SNUBNOSE && hadIFrames) {
                livingEntity.invulnerableTime = 0;
                livingEntity.hurtTime = 0;
            }


            float damage = getBulletDamage();

            if (getAmmoType() == SNUBNOSE && !hadIFrames) {
                damage += 1.0F;
            }

            boolean didDamage = livingEntity.hurt(ModDamageTypes.of(level(), ModDamageTypes.BULLET, this, this.getOwner()), damage);

            if (didDamage) {
                applyEffect(livingEntity);
            }

            if (didDamage && getAmmoType() == SNUBNOSE && outsideOfTimeStop == 0) {
                livingEntity.invulnerableTime = 10;
                livingEntity.hurtTime = 10;
            }

            if (didDamage && getAmmoType() == COLT && outsideOfTimeStop == 0) {
                livingEntity.invulnerableTime = 15;
                livingEntity.hurtTime = 15;
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

    protected void onHitBlock2(BlockHitResult $$0) {
        ((IAbstractArrowAccess)this).roundabout$setLastState(this.level().getBlockState($$0.getBlockPos()));
        BlockState BSS = this.level().getBlockState($$0.getBlockPos());
        BSS.onProjectileHit(this.level(), BSS, $$0, this);
        Vec3 $$1 = $$0.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement($$1);
        Vec3 $$2 = $$1.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - $$2.x, this.getY() - $$2.y, this.getZ() - $$2.z);
        this.playSound(ModSounds.BULLET_PENTRATION_EVENT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);
        this.setSoundEvent(SoundEvents.ARROW_HIT);
        this.setShotFromCrossbow(false);
        ((IAbstractArrowAccess)this).roundabout$resetPiercedEntities();
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
    }

    boolean deflectSoundPlayed = false;

    @Override
    public void tick() {
        Vec3 delta = this.getDeltaMovement();

        if (((IProjectileAccess) this).roundabout$getRoundaboutIsTimeStopCreated()) {
            timeStopShot = true;
        }

        if (this.getSuperThrown() && this.getAmmoType() != SNIPER) {
            superThrownTimer++;
            if (superThrownTimer >= 20) {
                this.setSuperThrown(false);
                superThrownTimer = 0;
            }
        } else {
            superThrownTimer = 0;
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

//        if (getDeflected()) {
//            this.flipTrajectory();
//            return;
//        }

        super.tick();

        if (this.getSuperThrown()) {
            this.setDeltaMovement(delta);
        }

        if (!level().isClientSide && !this.inGround) {
            boolean isFlying = getDeltaMovement().lengthSqr() > 1;

            if (isFlying) {
                ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.2F, 0.2F, 0.2F), 1f), this.getX(), this.getY(), this.getZ(), 0, 0, 0, 0, 0);
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
        this.setSuperThrown(false);
        this.setDeltaMovement(Vec3.ZERO);
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

        onHitBlock2($$0);
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
