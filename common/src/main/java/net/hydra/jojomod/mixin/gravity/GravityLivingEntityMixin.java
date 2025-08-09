package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;

@Mixin(value= LivingEntity.class)
public abstract class GravityLivingEntityMixin extends Entity {

    public GravityLivingEntityMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    public abstract void readAdditionalSaveData(CompoundTag nbt);

    @Shadow
    public abstract EntityDimensions getDimensions(Pose pose);

    @Shadow
    public abstract float getViewYRot(float tickDelta);

    @Shadow public abstract boolean hasEffect(MobEffect mobEffect);

    @Shadow protected abstract boolean isAffectedByFluids();

    @Shadow public abstract boolean canStandOnFluid(FluidState fluidState);

    @Shadow protected abstract float getWaterSlowDown();


    @Shadow public abstract float getSpeed();

    @Shadow public abstract boolean onClimbable();

    @Shadow public abstract Vec3 getFluidFallingAdjustedMovement(double d, boolean bl, Vec3 vec3);

    @Shadow public abstract boolean isFallFlying();

    @Shadow protected abstract SoundEvent getFallDamageSound(int i);

    @Shadow public abstract Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec3, float f);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect mobEffect);

    @Shadow public abstract boolean shouldDiscardFriction();

    @Shadow public abstract void calculateEntityAnimation(boolean bl);

    @Shadow public abstract void knockback(double d, double e, double f);

    @Shadow public abstract boolean isBlocking();

    @Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;

    @Shadow protected abstract void onEffectUpdated(MobEffectInstance mobEffectInstance, boolean bl, @Nullable Entity entity);

    @Shadow protected abstract void onEffectRemoved(MobEffectInstance mobEffectInstance);

    @Shadow private boolean effectsDirty;

    @Shadow protected abstract void updateInvisibilityStatus();

    @Shadow protected abstract void updateGlowingStatus();

    @Shadow @Final private static EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID;

    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;

    public LivingEntity rdbt$this(){
        return ((LivingEntity)(Object)this);
    }

    @Inject(
            method = "travel(Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void roundabout$travelWithGravity(Vec3 $$0, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(rdbt$this());
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();
        if (this.isControlledByLocalInstance()) {
            double $$1 = 0.08* (float) Math.sqrt(GravityAPI.getGravityStrength(this));
            boolean $$2 = this.getDeltaMovement().y <= 0.0;
            if ($$2 && this.hasEffect(MobEffects.SLOW_FALLING)) {
                $$1 = 0.01* (float) Math.sqrt(GravityAPI.getGravityStrength(this));
            }

            FluidState $$3 = this.level().getFluidState(this.blockPosition());
            if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid($$3)) {
                double $$4 = RotationUtil.vecWorldToPlayer(position(), gravityDirection).y;;
                float $$5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                float $$6 = 0.02F;
                float $$7 = (float) EnchantmentHelper.getDepthStrider(rdbt$this());
                if ($$7 > 3.0F) {
                    $$7 = 3.0F;
                }

                if (!this.onGround()) {
                    $$7 *= 0.5F;
                }

                if ($$7 > 0.0F) {
                    $$5 += (0.54600006F - $$5) * $$7 / 3.0F;
                    $$6 += (this.getSpeed() - $$6) * $$7 / 3.0F;
                }

                if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                    $$5 = 0.96F;
                }

                this.moveRelative($$6, $$0);
                this.move(MoverType.SELF, this.getDeltaMovement());
                Vec3 $$8 = this.getDeltaMovement();
                if (this.horizontalCollision && this.onClimbable()) {
                    $$8 = new Vec3($$8.x, 0.2, $$8.z);
                }

                this.setDeltaMovement($$8.multiply((double)$$5, 0.8F, (double)$$5));
                Vec3 $$9 = this.getFluidFallingAdjustedMovement($$1, $$2, this.getDeltaMovement());
                this.setDeltaMovement($$9);
                if (this.horizontalCollision && this.isFree($$9.x, $$9.y + 0.6F - RotationUtil.vecWorldToPlayer(position(), gravityDirection).y + $$4, $$9.z)) {
                    this.setDeltaMovement($$9.x, 0.3F, $$9.z);
                }
            } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid($$3)) {
                double $$10 = RotationUtil.vecWorldToPlayer(position(), gravityDirection).y;
                this.moveRelative(0.02F, $$0);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
                    Vec3 $$11 = this.getFluidFallingAdjustedMovement($$1, $$2, this.getDeltaMovement());
                    this.setDeltaMovement($$11);
                } else {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
                }

                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, -$$1 / 4.0, 0.0));
                }

                Vec3 $$12 = this.getDeltaMovement();
                if (this.horizontalCollision && this.isFree($$12.x, $$12.y + 0.6F - RotationUtil.vecWorldToPlayer(position(), gravityDirection).y + $$10, $$12.z)) {
                    this.setDeltaMovement($$12.x, 0.3F, $$12.z);
                }
            } else if (this.isFallFlying()) {
                this.checkSlowFallDistance();
                Vec3 $$13 = this.getDeltaMovement();
                Vec3 $$14 = RotationUtil.vecWorldToPlayer(this.getLookAngle(), gravityDirection);
                float $$15 = this.getXRot() * (float) (Math.PI / 180.0);
                double $$16 = Math.sqrt($$14.x * $$14.x + $$14.z * $$14.z);
                double $$17 = $$13.horizontalDistance();
                double $$18 = $$14.length();
                double $$19 = Math.cos((double)$$15);
                $$19 = $$19 * $$19 * Math.min(1.0, $$18 / 0.4);
                $$13 = this.getDeltaMovement().add(0.0, $$1 * (-1.0 + $$19 * 0.75), 0.0);
                if ($$13.y < 0.0 && $$16 > 0.0) {
                    double $$20 = $$13.y * -0.1 * $$19;
                    $$13 = $$13.add($$14.x * $$20 / $$16, $$20, $$14.z * $$20 / $$16);
                }

                if ($$15 < 0.0F && $$16 > 0.0) {
                    double $$21 = $$17 * (double)(-Mth.sin($$15)) * 0.04;
                    $$13 = $$13.add(-$$14.x * $$21 / $$16, $$21 * 3.2, -$$14.z * $$21 / $$16);
                }

                if ($$16 > 0.0) {
                    $$13 = $$13.add(($$14.x / $$16 * $$17 - $$13.x) * 0.1, 0.0, ($$14.z / $$16 * $$17 - $$13.z) * 0.1);
                }

                this.setDeltaMovement($$13.multiply(0.99F, 0.98F, 0.99F));
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.horizontalCollision && !this.level().isClientSide) {
                    double $$22 = this.getDeltaMovement().horizontalDistance();
                    double $$23 = $$17 - $$22;
                    float $$24 = (float)($$23 * 10.0 - 3.0);
                    if ($$24 > 0.0F) {
                        this.playSound(this.getFallDamageSound((int)$$24), 1.0F, 1.0F);
                        this.hurt(this.damageSources().flyIntoWall(), $$24);
                    }
                }

                if (this.onGround() && !this.level().isClientSide) {
                    this.setSharedFlag(7, false);
                }
            } else {
                BlockPos $$25 = this.getBlockPosBelowThatAffectsMyMovement();
                float $$26 = this.level().getBlockState($$25).getBlock().getFriction();
                float $$27 = this.onGround() ? $$26 * 0.91F : 0.91F;
                Vec3 $$28 = this.handleRelativeFrictionAndCalculateMovement($$0, $$26);
                double $$29 = $$28.y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    $$29 += (0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - $$28.y) * 0.2;
                } else if (this.level().isClientSide && !this.level().hasChunkAt($$25)) {
                    if (this.getY() > (double)this.level().getMinBuildHeight()) {
                        $$29 = -0.1;
                    } else {
                        $$29 = 0.0;
                    }
                } else if (!this.isNoGravity()) {
                    $$29 -= $$1;
                }

                if (this.shouldDiscardFriction()) {
                    this.setDeltaMovement($$28.x, $$29, $$28.z);
                } else {
                    this.setDeltaMovement($$28.x * (double)$$27, $$29 * 0.98F, $$28.z * (double)$$27);
                }
            }
        }

        this.calculateEntityAnimation(this instanceof FlyingAnimal);
    }


    @Inject(
            method = "playBlockFallSound",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$modify_playBlockFallSound_getBlockState_0(CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();
        if (!this.isSilent()) {
            BlockPos flipPos = BlockPos.containing(this.position().add(RotationUtil.vecPlayerToWorld(0, -0.20000000298023224D, 0, gravityDirection)));
            BlockState $$3 = this.level().getBlockState(new BlockPos(flipPos.getX(), flipPos.getY(), flipPos.getZ()));
            if (!$$3.isAir()) {
                SoundType $$4 = $$3.getSoundType();
                this.playSound($$4.getFallSound(), $$4.getVolume() * 0.5F, $$4.getPitch() * 0.75F);
            }
        }
    }

    @Inject(
            method = "hasLineOfSight(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(
                    value = "NEW",
                    target = "(DDD)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 0
            ),
            cancellable = true)
    private void roundabout$redirect_canSee_new_0(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);

        Direction gravityDirectioEent = GravityAPI.getGravityDirection(entity);
        if (gravityDirection == Direction.DOWN && gravityDirectioEent == Direction.DOWN)
            return;
        if (entity.level() != this.level()) {
            cir.setReturnValue(false);
        } else {
            Vec3 $$1 = this.getEyePosition();
            Vec3 $$2 = entity.getEyePosition();
            cir.setReturnValue($$2.distanceTo($$1) > 128.0
                    ? false
                    : this.level().clip(new ClipContext($$1, $$2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS);
        }
    }


    @Inject(
            method = "getLocalBoundsForPose(Lnet/minecraft/world/entity/Pose;)Lnet/minecraft/world/phys/AABB;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void roundabout$inject_getBoundingBox(Pose pose, CallbackInfoReturnable<AABB> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        AABB box = cir.getReturnValue();
        if (gravityDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            box = box.move(0.0D, -1.0E-6D, 0.0D);
        }
        cir.setReturnValue(RotationUtil.boxPlayerToWorld(box, gravityDirection));
    }
    /**Modifies the gravity influence*/
    @ModifyVariable(method = "tick", at = @At(value = "STORE"),ordinal = 0)
    private double roundabout$tickGravity(double $$1) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return $$1;

        return RotationUtil.vecWorldToPlayer($$1 - xo, getY() - yo, getZ() - zo, gravityDirection).x + xo;
    }
    @ModifyVariable(method = "tick", at = @At(value = "STORE"),ordinal = 1)
    private double roundabout$tickGravity2(double $$1) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return $$1;

        return RotationUtil.vecWorldToPlayer(getX() - xo, getY() - yo, $$1 - zo, gravityDirection).z + zo;
    }


    @Unique
    public boolean roundabout$knockbackGravityAugmentation = false;
    @Unique
    public Entity roundabout$augmentSource = null;

    @Inject(
            method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"
            )
    )
    private void roundabout$augmentHurt(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> cir){
        roundabout$knockbackGravityAugmentation = true;
        roundabout$augmentSource = $$0.getEntity();
    }
    @Inject(
            method = "knockback(DDD)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$augmentHurtKnockaback(double $$0, double $$1, double $$2, CallbackInfo ci){
        if (roundabout$knockbackGravityAugmentation){
            roundabout$knockbackGravityAugmentation = false;
            if (roundabout$augmentSource != null) {
                Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
                if (gravityDirection == Direction.DOWN)
                    return;
                double $$13 = roundabout$redirect_damage_getX_0(roundabout$augmentSource) -
                        roundabout$redirect_damage_getX_1(rdbt$this());

                double $$14;
                for ($$14 = roundabout$redirect_damage_getZ_0(roundabout$augmentSource) -
                        roundabout$redirect_damage_getZ_1(rdbt$this()); $$13 * $$13 + $$14 * $$14 < 1.0E-4; $$14 = (Math.random() - Math.random()) * 0.01) {
                    $$13 = (Math.random() - Math.random()) * 0.01;
                }
                knockback(0.4F,$$13,$$14);
                ci.cancel();
            }
        }
    }

    @Unique
    private double roundabout$redirect_damage_getX_0(Entity attacker) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            if (GravityAPI.getGravityDirection(attacker) == Direction.DOWN) {
                return attacker.getX();
            }
            else {
                return attacker.getEyePosition().x;
            }
        }

        return RotationUtil.vecWorldToPlayer(attacker.getEyePosition(), gravityDirection).x;
    }

    @Unique
    private double roundabout$redirect_damage_getZ_0(Entity attacker) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            if (GravityAPI.getGravityDirection(attacker) == Direction.DOWN) {
                return attacker.getZ();
            }
            else {
                return attacker.getEyePosition().z;
            }
        }

        return RotationUtil.vecWorldToPlayer(attacker.getEyePosition(), gravityDirection).z;
    }


    @Unique
    private double roundabout$redirect_damage_getX_1(LivingEntity target) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN) {
            return target.getX();
        }

        return RotationUtil.vecWorldToPlayer(target.position(), gravityDirection).x;
    }

    @Unique
    private double roundabout$redirect_damage_getZ_1(LivingEntity target) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN) {
            return target.getZ();
        }

        return RotationUtil.vecWorldToPlayer(target.position(), gravityDirection).z;
    }

    @Inject(
            method = "blockedByShield(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void roundabout$blocked(LivingEntity target, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        Direction gravityDirection2 = GravityAPI.getGravityDirection(rdbt$this());
        if (gravityDirection == Direction.DOWN && gravityDirection2 == Direction.DOWN)
            return;
        ci.cancel();

        target.knockback(0.5, RotationUtil.vecWorldToPlayer(target.position(), gravityDirection).x
                - roundabout$redirect_knockback_getX_1(rdbt$this(),target),
                RotationUtil.vecWorldToPlayer(target.position(), gravityDirection).z
                        - roundabout$redirect_knockback_getZ_1(rdbt$this(),target));
        return ;
    }

    @Unique
    private double roundabout$redirect_knockback_getX_1(LivingEntity attacker, LivingEntity target) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN) {
            if (GravityAPI.getGravityDirection(attacker) == Direction.DOWN) {
                return attacker.getX();
            }
            else {
                return attacker.getEyePosition().x;
            }
        }

        return RotationUtil.vecWorldToPlayer(attacker.getEyePosition(), gravityDirection).x;
    }

    @Unique
    private double roundabout$redirect_knockback_getZ_1(LivingEntity attacker, LivingEntity target) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN) {
            if (GravityAPI.getGravityDirection(attacker) == Direction.DOWN) {
                return attacker.getZ();
            }
            else {
                return attacker.getEyePosition().z;
            }
        }

        return RotationUtil.vecWorldToPlayer(attacker.getEyePosition(), gravityDirection).z;
    }

    @Inject(
            method = "spawnItemParticles",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void roundabout$spawnItemParticles(ItemStack $$0, int $$1, CallbackInfo ci){
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            Vec3 $$3 = new Vec3(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            $$3 = $$3.xRot(-this.getXRot() * (float) (Math.PI / 180.0));
            $$3 = RotationUtil.vecPlayerToWorld($$3.yRot(-this.getYRot() * (float) (Math.PI / 180.0)), gravityDirection);
            double $$4 = (double)(-this.random.nextFloat()) * 0.6 - 0.3;
            Vec3 $$5 = new Vec3(((double)this.random.nextFloat() - 0.5) * 0.3, $$4, 0.6);
            $$5 = $$5.xRot(-this.getXRot() * (float) (Math.PI / 180.0));
            $$5 = $$5.yRot(-this.getYRot() * (float) (Math.PI / 180.0));
            Vec3 rotated = RotationUtil.vecPlayerToWorld($$5, gravityDirection);
            $$5 = this.getEyePosition().add(rotated.x, rotated.y, rotated.z);
            this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, $$0), $$5.x, $$5.y, $$5.z, $$3.x, $$3.y + 0.05, $$3.z);
        }

    }

    @Inject(
            method = "tickEffects",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$tickEffects(CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();
        Iterator<MobEffect> $$0 = this.activeEffects.keySet().iterator();

        try {
            while ($$0.hasNext()) {
                MobEffect $$1 = $$0.next();
                MobEffectInstance $$2 = this.activeEffects.get($$1);
                if (!$$2.tick(rdbt$this(), () -> this.onEffectUpdated($$2, true, null))) {
                    if (!this.level().isClientSide) {
                        $$0.remove();
                        this.onEffectRemoved($$2);
                    }
                } else if ($$2.getDuration() % 600 == 0) {
                    this.onEffectUpdated($$2, false, null);
                }
            }
        } catch (ConcurrentModificationException var11) {
        }

        if (this.effectsDirty) {
            if (!this.level().isClientSide) {
                this.updateInvisibilityStatus();
                this.updateGlowingStatus();
            }

            this.effectsDirty = false;
        }

        int $$3 = this.entityData.get(DATA_EFFECT_COLOR_ID);
        boolean $$4 = this.entityData.get(DATA_EFFECT_AMBIENCE_ID);
        if ($$3 > 0) {
            boolean $$5;
            if (this.isInvisible()) {
                $$5 = this.random.nextInt(15) == 0;
            } else {
                $$5 = this.random.nextBoolean();
            }

            if ($$4) {
                $$5 &= this.random.nextInt(5) == 0;
            }

            if ($$5 && $$3 > 0) {
                double $$7 = (double)($$3 >> 16 & 0xFF) / 255.0;
                double $$8 = (double)($$3 >> 8 & 0xFF) / 255.0;
                double $$9 = (double)($$3 >> 0 & 0xFF) / 255.0;
                Vec3 vec3d = this.position().subtract(RotationUtil.vecPlayerToWorld(this.position().subtract(this.getRandomX(0.5),
                        this.getRandomY(),
                        this.getRandomZ(0.5)), gravityDirection));
                this.level()
                        .addParticle(
                                $$4 ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT,
                                vec3d.x,vec3d.y,vec3d.z,
                                $$7,
                                $$8,
                                $$9
                        );
            }
        }
    }


    @Inject(
            method = "makePoofParticles",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$modify_addDeathParticless_addParticle_0(CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN)
            return;
        for (int $$0 = 0; $$0 < 20; $$0++) {
            double $$1 = this.random.nextGaussian() * 0.02;
            double $$2 = this.random.nextGaussian() * 0.02;
            double $$3 = this.random.nextGaussian() * 0.02;
            Vec3 vec3d = this.position().subtract(RotationUtil.vecPlayerToWorld(this.position().subtract(this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0)), gravityDirection));

            this.level().addParticle(ParticleTypes.POOF, vec3d.x, vec3d.y, vec3d.z, $$1, $$2, $$3);
        }
        ci.cancel();
    }

    @Inject(
            method = "isDamageSourceBlocked(Lnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$modify_blockedByShield_Vec3d_1(DamageSource $$0, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN)
            return;
        Entity $$1 = $$0.getDirectEntity();
        boolean $$2 = false;
        if ($$1 instanceof AbstractArrow $$3 && $$3.getPierceLevel() > 0) {
            $$2 = true;
        }

        if (!$$0.is(DamageTypeTags.BYPASSES_SHIELD) && this.isBlocking() && !$$2) {
            Vec3 $$4 = $$0.getSourcePosition();
            if ($$4 != null) {
                Vec3 $$5 = RotationUtil.vecWorldToPlayer(this.getViewVector(1.0F), gravityDirection);
                Vec3 $$6 = $$4.vectorTo(this.position()).normalize();
                $$6 = new Vec3($$6.x, 0.0, $$6.z);
                if ($$6.dot($$5) < 0.0) {
                    cir.setReturnValue(true);
                }
            }
        }

        cir.setReturnValue(false);

        return ;
    }



    @ModifyVariable(method = "calculateFallDamage(FF)I", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float roundabout$diminishFallDamage(float value) {
        return value * (float) Math.sqrt(GravityAPI.getGravityStrength(this));
    }

}
