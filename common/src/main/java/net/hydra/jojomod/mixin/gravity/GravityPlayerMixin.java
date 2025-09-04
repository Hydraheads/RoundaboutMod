package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@SuppressWarnings("resource")
@Mixin(value = Player.class, priority = 1001)
public abstract class GravityPlayerMixin extends LivingEntity {
    @Shadow
    @Final
    private Abilities abilities;

    @Shadow
    public abstract EntityDimensions getDimensions(Pose pose);

    @Shadow
    protected abstract boolean isStayingOnGroundSurface();

    @Shadow
    protected abstract boolean isAboveGround();

    @Shadow public abstract void checkMovementStatistics(double d, double e, double f);

    @Shadow public abstract void awardStat(Stat<?> stat);

    @Shadow public abstract void causeFoodExhaustion(float f);

    @Shadow public abstract void awardStat(Stat<?> stat, int i);

    @Shadow public abstract void awardStat(ResourceLocation resourceLocation, int i);

    protected GravityPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Unique
    public Player rdbt$this(){
        return ((Player)(Object)this);
    }

    @Inject(
            method = "checkMovementStatistics(DDD)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$checkMovementStatistics(double $$0, double $$1, double $$2, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(rdbt$this());
        if (gravityDirection == Direction.DOWN || gravityDirection == Direction.UP)
            return;
        ci.cancel();
        if (gravityDirection == Direction.EAST || gravityDirection == Direction.WEST)
            rdbt$checkMovementStatistics2($$1,$$0,$$2);
        else
            rdbt$checkMovementStatistics2($$0,$$2,$$1);
    }

    @Unique
    public void rdbt$checkMovementStatistics2(double $$0, double $$1, double $$2) {
        if (!this.isPassenger()) {
            if (this.isSwimming()) {
                int $$3 = Math.round((float)Math.sqrt($$0 * $$0 + $$1 * $$1 + $$2 * $$2) * 100.0F);
                if ($$3 > 0) {
                    this.awardStat(Stats.SWIM_ONE_CM, $$3);
                    this.causeFoodExhaustion(0.01F * (float)$$3 * 0.01F);
                }
            } else if (this.isEyeInFluid(FluidTags.WATER)) {
                int $$4 = Math.round((float)Math.sqrt($$0 * $$0 + $$1 * $$1 + $$2 * $$2) * 100.0F);
                if ($$4 > 0) {
                    this.awardStat(Stats.WALK_UNDER_WATER_ONE_CM, $$4);
                    this.causeFoodExhaustion(0.01F * (float)$$4 * 0.01F);
                }
            } else if (this.isInWater()) {
                int $$5 = Math.round((float)Math.sqrt($$0 * $$0 + $$2 * $$2) * 100.0F);
                if ($$5 > 0) {
                    this.awardStat(Stats.WALK_ON_WATER_ONE_CM, $$5);
                    this.causeFoodExhaustion(0.01F * (float)$$5 * 0.01F);
                }
            } else if (this.onClimbable()) {
                if ($$1 > 0.0) {
                    this.awardStat(Stats.CLIMB_ONE_CM, (int)Math.round($$1 * 100.0));
                }
            } else if (this.onGround()) {
                int $$6 = Math.round((float)Math.sqrt($$0 * $$0 + $$2 * $$2) * 100.0F);
                if ($$6 > 0) {
                    if (this.isSprinting()) {
                        this.awardStat(Stats.SPRINT_ONE_CM, $$6);
                        this.causeFoodExhaustion(0.1F * (float)$$6 * 0.01F);
                    } else if (this.isCrouching()) {
                        this.awardStat(Stats.CROUCH_ONE_CM, $$6);
                        this.causeFoodExhaustion(0.0F * (float)$$6 * 0.01F);
                    } else {
                        this.awardStat(Stats.WALK_ONE_CM, $$6);
                        this.causeFoodExhaustion(0.0F * (float)$$6 * 0.01F);
                    }
                }
            } else if (this.isFallFlying()) {
                int $$7 = Math.round((float)Math.sqrt($$0 * $$0 + $$1 * $$1 + $$2 * $$2) * 100.0F);
                this.awardStat(Stats.AVIATE_ONE_CM, $$7);
            } else {
                int $$8 = Math.round((float)Math.sqrt($$0 * $$0 + $$2 * $$2) * 100.0F);
                if ($$8 > 25) {
                    this.awardStat(Stats.FLY_ONE_CM, $$8);
                }
            }
        }
    }

    @Inject(
            method = "travel",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$gravityTravel(Vec3 $$0, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(rdbt$this());
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        double $$1 = this.getX();
        double $$2 = this.getY();
        double $$3 = this.getZ();
        if (this.isSwimming() && !this.isPassenger()) {
            double $$4 = RotationUtil.vecWorldToPlayer(this.getLookAngle(), gravityDirection).y;
            double $$5 = $$4 < -0.2 ? 0.085 : 0.06;
            Vec3 rotate = new Vec3(0.0D, 1.0D - 0.1D, 0.0D);
            rotate = RotationUtil.vecPlayerToWorld(rotate, GravityAPI.getGravityDirection(this));
            if ($$4 <= 0.0
                    || this.jumping
                    || !this.level().getBlockState(BlockPos.containing(
                    (double) this.getX() - rotate.x,
                    (double) (this.getY() + 1.0 - 0.1) - rotate.y + (1.0D - 0.1D),
                    (double) this.getZ() - rotate.z)
            ).getFluidState().isEmpty()) {
                Vec3 $$6 = this.getDeltaMovement();
                this.setDeltaMovement($$6.add(0.0, ($$4 - $$6.y) * $$5, 0.0));
            }
        }

        if (this.abilities.flying && !this.isPassenger()) {
            double $$7 = this.getDeltaMovement().y;
            super.travel($$0);
            Vec3 $$8 = this.getDeltaMovement();
            this.setDeltaMovement($$8.x, $$7 * 0.6, $$8.z);
            this.resetFallDistance();
            this.setSharedFlag(7, false);
        } else {
            super.travel($$0);
        }

        this.checkMovementStatistics(this.getX() - $$1, this.getY() - $$2, this.getZ() - $$3);

    }


    @Inject(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$dropGravity(
            ItemStack $$0, boolean $$1, boolean $$2, CallbackInfoReturnable<ItemEntity> cir
    ) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN)
            return;

        if ($$0.isEmpty()) {
            cir.setReturnValue(null);
        } else {
            if (this.level().isClientSide) {
                this.swing(InteractionHand.MAIN_HAND);
            }

            Vec3 vec3d = this.getEyePosition()
                    .subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.3D, 0.0D, gravityDirection));

            ItemEntity $$4 = new ItemEntity(this.level(), vec3d.x, vec3d.y, vec3d.z, $$0);
            $$4.setPickUpDelay(40);
            if ($$2) {
                $$4.setThrower(this.getUUID());
            }

            if ($$1) {
                float $$5 = this.random.nextFloat() * 0.5F;
                float $$6 = this.random.nextFloat() * (float) (Math.PI * 2);

                Vec3 world = RotationUtil.vecPlayerToWorld((double)(-Mth.sin($$6) * $$5), 0.2F, (double)(Mth.cos($$6) * $$5), gravityDirection);
                GravityAPI.setWorldVelocity($$4, world);
            } else {
                float $$7 = 0.3F;
                float $$8 = Mth.sin(this.getXRot() * (float) (Math.PI / 180.0));
                float $$9 = Mth.cos(this.getXRot() * (float) (Math.PI / 180.0));
                float $$10 = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0));
                float $$11 = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0));
                float $$12 = this.random.nextFloat() * (float) (Math.PI * 2);
                float $$13 = 0.02F * this.random.nextFloat();
                Vec3 world = RotationUtil.vecPlayerToWorld((double)(-$$10 * $$9 * 0.3F) + Math.cos((double)$$12) * (double)$$13,
                        (double)(-$$8 * 0.3F + 0.1F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F),
                        (double)($$11 * $$9 * 0.3F) + Math.sin((double)$$12) * (double)$$13, gravityDirection);
                GravityAPI.setWorldVelocity($$4, world);
            }

            cir.setReturnValue($$4);
        }
    }

    @Inject(
            method = "maybeBackOffFromEdge(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/MoverType;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$inject_adjustMovementForSneaking(Vec3 movement, MoverType type, CallbackInfoReturnable<Vec3> cir) {
        Entity this_ = (Entity) (Object) this;
        Direction gravityDirection = GravityAPI.getGravityDirection(this_);
        if (gravityDirection == Direction.DOWN) return;

        Vec3 playerMovement = RotationUtil.vecWorldToPlayer(movement, gravityDirection);

        if (!this.abilities.flying && (type == MoverType.SELF || type == MoverType.PLAYER) && this.isStayingOnGroundSurface() && this.isAboveGround()) {
            double d = playerMovement.x;
            double e = playerMovement.z;
            double var7 = 0.05D;

            while (d != 0.0D && this_.level().noCollision(this, this.getBoundingBox().move(RotationUtil.vecPlayerToWorld(d, (double) (-this.maxUpStep()), 0.0D, gravityDirection)))) {
                if (d < 0.05D && d >= -0.05D) {
                    d = 0.0D;
                }
                else if (d > 0.0D) {
                    d -= 0.05D;
                }
                else {
                    d += 0.05D;
                }
            }

            while (e != 0.0D && this_.level().noCollision(this, this.getBoundingBox().move(RotationUtil.vecPlayerToWorld(0.0D, (double) (-this.maxUpStep()), e, gravityDirection)))) {
                if (e < 0.05D && e >= -0.05D) {
                    e = 0.0D;
                }
                else if (e > 0.0D) {
                    e -= 0.05D;
                }
                else {
                    e += 0.05D;
                }
            }

            while (d != 0.0D && e != 0.0D && this_.level().noCollision(this, this.getBoundingBox().move(RotationUtil.vecPlayerToWorld(d, (double) (-this.maxUpStep()), e, gravityDirection)))) {
                if (d < 0.05D && d >= -0.05D) {
                    d = 0.0D;
                }
                else if (d > 0.0D) {
                    d -= 0.05D;
                }
                else {
                    d += 0.05D;
                }

                if (e < 0.05D && e >= -0.05D) {
                    e = 0.0D;
                }
                else if (e > 0.0D) {
                    e -= 0.05D;
                }
                else {
                    e += 0.05D;
                }
            }

            cir.setReturnValue(RotationUtil.vecPlayerToWorld(d, playerMovement.y, e, gravityDirection));
        }
        else {
            cir.setReturnValue(movement);
        }
    }

    @Inject(
            method = "isAboveGround",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$isAboveGround(CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN)
            return;


        Vec3 world = RotationUtil.vecPlayerToWorld(0.0, (double)(this.fallDistance - this.maxUpStep()), 0.0, gravityDirection);

        cir.setReturnValue(this.onGround()
                || this.fallDistance < this.maxUpStep()
                && !this.level().noCollision(this, this.getBoundingBox().move(world.x, world.y, world.z)));
        //Roundabout.LOGGER.info(""+cir.getReturnValue());
    }

    @Unique
    private float roundabout$tempStoreYRot = 0;
    @Unique
    private boolean roundabout$tempStoreYRotShifted = false;

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"
            )
    )
    private void roundabout$attackY1(Entity target, CallbackInfo ci) {
        Direction targetGravityDirection = GravityAPI.getGravityDirection(target);
        Direction attackerGravityDirection = GravityAPI.getGravityDirection(this);
        if (targetGravityDirection == attackerGravityDirection)
            return;

        roundabout$tempStoreYRot = getYRot();
        roundabout$tempStoreYRotShifted = true;
        setYRot(RotationUtil.rotWorldToPlayer(RotationUtil.rotPlayerToWorld(getYRot(), getXRot(), attackerGravityDirection), targetGravityDirection).x);
    }
    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V",
                    shift = At.Shift.AFTER
            )
    )
    private void roundabout$attackY2(Entity target, CallbackInfo ci) {
        if (roundabout$tempStoreYRotShifted){
            roundabout$tempStoreYRotShifted = false;
            setYRot(roundabout$tempStoreYRot);
        }
    }


    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;push(DDD)V"
            )
    )
    private void roundabout$attackY1push(Entity target, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN)
            return;

        roundabout$tempStoreYRot = getYRot();
        roundabout$tempStoreYRotShifted = true;
        setYRot(RotationUtil.rotPlayerToWorld(getYRot(), getXRot(), gravityDirection).x);
    }
    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;push(DDD)V",
                    shift = At.Shift.AFTER
            )
    )
    private void roundabout$attackY2push(Entity target, CallbackInfo ci) {
        if (roundabout$tempStoreYRotShifted){
            roundabout$tempStoreYRotShifted = false;
            setYRot(roundabout$tempStoreYRot);
        }
    }

    @Inject(
            method = "addParticlesAroundSelf",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$addParticlesAroundSelf(ParticleOptions $$0, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        ci.cancel();
        for (int $$1 = 0; $$1 < 5; $$1++) {
            double $$2 = this.random.nextGaussian() * 0.02;
            double $$3 = this.random.nextGaussian() * 0.02;
            double $$4 = this.random.nextGaussian() * 0.02;
            Vec3 vec3d = RotationUtil.maskPlayerToWorld(this.getRandomX(1.0),
                    this.getRandomY() + 1.0,
                    this.getRandomZ(1.0),
                    gravityDirection);
            this.level().addParticle($$0, vec3d.x,vec3d.y,vec3d.z, $$2, $$3, $$4);
        }
    }

}
