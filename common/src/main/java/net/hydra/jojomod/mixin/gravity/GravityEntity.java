package net.hydra.jojomod.mixin.gravity;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Entity.class)
public abstract class GravityEntity implements IGravityEntity {

    @Shadow public abstract SynchedEntityData getEntityData();

    @Shadow @Final protected SynchedEntityData entityData;

    /***
     * Gravity Direction for Entities. Note that only Living Entities use tracked/synched entitydata,
     * so regular entities use a function in IEntityAndData instead.
     */
    @Unique
    private static final EntityDataAccessor<Direction> ROUNDABOUT$GRAVITY_DIRECTION = SynchedEntityData.defineId(Entity.class,
            EntityDataSerializers.DIRECTION);

    @Unique
    @Override
    public Direction roundabout$getGravityDirection(){
        if (this.entityData.hasItem(ROUNDABOUT$GRAVITY_DIRECTION)) {
            return this.getEntityData().get(ROUNDABOUT$GRAVITY_DIRECTION);
        }
        return Direction.DOWN;
    }

    @Unique
    @Override
    public void roundabout$setGravityDirection(Direction direction){
        if (this.entityData.hasItem(ROUNDABOUT$GRAVITY_DIRECTION)) {
            this.getEntityData().set(ROUNDABOUT$GRAVITY_DIRECTION, direction);
        }
    }
    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
    public void roundabout$init(EntityType $$0, Level $$1, CallbackInfo ci){
        if (!((Entity)(Object)this).getEntityData().hasItem(ROUNDABOUT$GRAVITY_DIRECTION)) {
            ((Entity) (Object) this).getEntityData().define(ROUNDABOUT$GRAVITY_DIRECTION, Direction.DOWN);
        }
    }

    @Shadow public abstract float maxUpStep();

    @Shadow private boolean onGround;
    @Shadow private float maxUpStep;

    @Shadow
    public static Vec3 collideBoundingBox(@org.jetbrains.annotations.Nullable Entity entity, Vec3 vec3, AABB aABB, Level level, List<VoxelShape> list) {
        return null;
    }

    @Shadow public abstract Level level();

    @Shadow public abstract boolean touchingUnloadedChunk();

    @Shadow public abstract boolean isPushedByFluid();

    @Shadow protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;

    @Shadow public abstract void setDeltaMovement(Vec3 vec3);

    @Shadow protected abstract boolean isFree(AABB aABB);

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "makeBoundingBox()Lnet/minecraft/world/phys/AABB;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void roundabout$inject_calculateBoundingBox(CallbackInfoReturnable<AABB> cir) {
        Entity entity = ((Entity) (Object) this);
        if (entity instanceof Projectile) return;

        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        AABB box = cir.getReturnValue().move(this.position.reverse());
        if (gravityDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            box = box.move(0.0D, -1.0E-6D, 0.0D);
        }
        cir.setReturnValue(RotationUtil.boxPlayerToWorld(box, gravityDirection).move(this.position));
    }

    @Inject(
            method = "calculateViewVector(FF)Lnet/minecraft/world/phys/Vec3;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void roundabout$inject_getRotationVector(CallbackInfoReturnable<Vec3> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        cir.setReturnValue(RotationUtil.vecPlayerToWorld(cir.getReturnValue(), gravityDirection));
    }

    @Inject(
            method = "getBlockPosBelowThatAffectsMyMovement()Lnet/minecraft/core/BlockPos;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$inject_getVelocityAffectingPos(CallbackInfoReturnable<BlockPos> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        cir.setReturnValue(BlockPos.containing(this.position.add(Vec3.atLowerCornerOf(gravityDirection.getNormal()).scale(0.5000001D))));
    }

    @Inject(
            method = "getEyePosition()Lnet/minecraft/world/phys/Vec3;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$inject_getEyePos(CallbackInfoReturnable<Vec3> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        cir.setReturnValue(RotationUtil.vecPlayerToWorld(0.0D, this.eyeHeight, 0.0D, gravityDirection).add(this.position));
    }

    @Inject(
            method = "getEyePosition(F)Lnet/minecraft/world/phys/Vec3;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$inject_getCameraPosVec(float tickDelta, CallbackInfoReturnable<Vec3> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        Vec3 vec3d = RotationUtil.vecPlayerToWorld(0.0D, this.eyeHeight, 0.0D, gravityDirection);

        double d = Mth.lerp((double) tickDelta, this.xo, this.getX()) + vec3d.x;
        double e = Mth.lerp((double) tickDelta, this.yo, this.getY()) + vec3d.y;
        double f = Mth.lerp((double) tickDelta, this.zo, this.getZ()) + vec3d.z;
        cir.setReturnValue(new Vec3(d, e, f));
    }

    @Inject(
            method = "getLightLevelDependentMagicValue()F",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$inject_getBrightnessAtFEyes(CallbackInfoReturnable<Float> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        cir.setReturnValue(this.level.hasChunkAt(this.getBlockX(), this.getBlockZ()) ? this.level.getLightLevelDependentMagicValue(BlockPos.containing(this.getEyePosition())) : 0.0F);
    }

    // transform move vector from local to world (the velocity is local)
    @ModifyVariable(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private Vec3 roundabout$modify_move_Vec3d_0_0(Vec3 vec3d) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            return vec3d;
        }

        return RotationUtil.vecPlayerToWorld(vec3d, gravityDirection);
    }

    // transform the argument vector back to local coordinate
    @ModifyVariable(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V",
                    ordinal = 0
            ),
            ordinal = 0,
            argsOnly = true
    )
    private Vec3 roundabout$modify_move_Vec3d_0_1(Vec3 vec3d) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            return vec3d;
        }

        return RotationUtil.vecWorldToPlayer(vec3d, gravityDirection);
    }

    // transform the local variable (result from collide()) to local coordinate
    @ModifyVariable(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V",
                    ordinal = 0
            ),
            ordinal = 1
    )
    private Vec3 roundabout$modify_move_Vec3d_1(Vec3 vec3d) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            return vec3d;
        }

        return RotationUtil.vecWorldToPlayer(vec3d, gravityDirection);
    }

    @Inject(
            method = "getOnPosLegacy()Lnet/minecraft/core/BlockPos;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$inject_getLandingPos(CallbackInfoReturnable<BlockPos> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;
        BlockPos blockPos = BlockPos.containing(RotationUtil.vecPlayerToWorld(0.0D, -0.20000000298023224D, 0.0D, gravityDirection).add(this.position));
        cir.setReturnValue(blockPos);
    }

    // transform the argument to local coordinate
    @ModifyVariable(
            method = "collide",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    ordinal = 0
            ),
            ordinal = 0
    )
    private Vec3 roundabout$modify_adjustMovementForCollisions_Vec3d_0(Vec3 vec3d) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            return vec3d;
        }

        return RotationUtil.vecWorldToPlayer(vec3d, gravityDirection);
    }

    // transform the result to world coordinate
    // the input to Entity.collideBoundingBox will be in local coord
    @Inject(
            method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void roundabout$inject_adjustMovementForCollisions(CallbackInfoReturnable<Vec3> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        cir.setReturnValue(RotationUtil.vecPlayerToWorld(cir.getReturnValue(), gravityDirection));
    }

    // transform back to local coord
    @Inject(
            method = "collideBoundingBox(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/Level;Ljava/util/List;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void roundabout$inject_adjustMovementForCollisions(Entity entity, Vec3 movement, AABB entityBoundingBox, Level world, List<VoxelShape> collisions, CallbackInfoReturnable<Vec3> cir) {
        if (entity == null) return;

        Direction gravityDirection = GravityAPI.getGravityDirection(entity);
        if (gravityDirection == Direction.DOWN) return;

        cir.setReturnValue(RotationUtil.vecWorldToPlayer(cir.getReturnValue(), gravityDirection));
    }

    @Inject(
            method = "spawnSprintParticle()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$inject_spawnSprintingParticles(CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        ci.cancel();

        Vec3 floorPos = this.position().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.20000000298023224D, 0.0D, gravityDirection));

        BlockPos blockPos = BlockPos.containing(floorPos);
        BlockState blockState = this.level.getBlockState(blockPos);
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            Vec3 particlePos = this.position().add(RotationUtil.vecPlayerToWorld((this.random.nextDouble() - 0.5D) * (double) this.dimensions.width, 0.1D, (this.random.nextDouble() - 0.5D) * (double) this.dimensions.width, gravityDirection));
            Vec3 playerVelocity = this.getDeltaMovement();
            Vec3 particleVelocity = RotationUtil.vecPlayerToWorld(playerVelocity.x * -4.0D, 1.5D, playerVelocity.z * -4.0D, gravityDirection);
            this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), particlePos.x, particlePos.y, particlePos.z, particleVelocity.x, particleVelocity.y, particleVelocity.z);
        }
    }

    @ModifyVariable(
            method = "updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 0
            ),
            ordinal = 1
    )
    private Vec3 roundabout$modify_updateMovementInFluid_Vec3d_0(Vec3 vec3d) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            return vec3d;
        }

        return RotationUtil.vecPlayerToWorld(vec3d, gravityDirection);
    }

    @Inject(
            method = "push(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void inject_pushAwayFrom(Entity entity, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        Direction otherGravityDirection = GravityAPI.getGravityDirection(entity);

        if (gravityDirection == Direction.DOWN && otherGravityDirection == Direction.DOWN) return;

        ci.cancel();

        if (!this.isPassengerOfSameVehicle(entity)) {
            if (!entity.noPhysics && !this.noPhysics) {
                Vec3 entityOffset = entity.getBoundingBox().getCenter().subtract(this.getBoundingBox().getCenter());

                {
                    Vec3 playerEntityOffset = RotationUtil.vecWorldToPlayer(entityOffset, gravityDirection);
                    double dx = playerEntityOffset.x;
                    double dz = playerEntityOffset.z;
                    double f = Mth.absMax(dx, dz);
                    if (f >= 0.009999999776482582D) {
                        f = Math.sqrt(f);
                        dx /= f;
                        dz /= f;
                        double g = 1.0D / f;
                        if (g > 1.0D) {
                            g = 1.0D;
                        }

                        dx *= g;
                        dz *= g;
                        dx *= 0.05000000074505806D;
                        dz *= 0.05000000074505806D;
                        if (!this.isVehicle()) {
                            this.push(-dx, 0.0D, -dz);
                        }
                    }
                }

                {
                    Vec3 entityEntityOffset = RotationUtil.vecWorldToPlayer(entityOffset, otherGravityDirection);
                    double dx = entityEntityOffset.x;
                    double dz = entityEntityOffset.z;
                    double f = Mth.absMax(dx, dz);
                    if (f >= 0.009999999776482582D) {
                        f = Math.sqrt(f);
                        dx /= f;
                        dz /= f;
                        double g = 1.0D / f;
                        if (g > 1.0D) {
                            g = 1.0D;
                        }

                        dx *= g;
                        dz *= g;
                        dx *= 0.05000000074505806D;
                        dz *= 0.05000000074505806D;
                        if (!entity.isVehicle()) {
                            entity.push(dx, 0.0D, dz);
                        }
                    }
                }
            }
        }
    }

    @ModifyVariable(
            method = "updateFluidOnEyes()V",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private double roundabout$submergedInWaterEyeFix(double d) {
        d = this.getEyePosition().y();
        return d;
    }

    @ModifyVariable(
            method = "updateFluidOnEyes()V",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private BlockPos roundabout$submergedInWaterPosFix(BlockPos blockpos) {
        blockpos = BlockPos.containing(this.getEyePosition());
        return blockpos;
    }


    //@ModifyVariable(method = "isFree(DDD)Z", at = @At(value = "HEAD"), argsOnly = true)
    @Inject(
            method = "isFree(DDD)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$isFree(double $$0, double $$1, double $$2, CallbackInfoReturnable<Boolean> cir) {

        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        Vec3 rotate = new Vec3($$0, $$1, $$2);
        rotate = RotationUtil.vecPlayerToWorld(rotate, GravityAPI.getGravityDirection((Entity) (Object) this));
        cir.setReturnValue(this.isFree(this.getBoundingBox().move(rotate.x, rotate.y, rotate.z)));
    }

    @Inject(
            method = "updateFluidHeightAndDoFluidPushing",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$updateFluidHeightAndDoFluidPushing(TagKey<Fluid> $$0, double $$1, CallbackInfoReturnable<Boolean> cir) {

        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        if (this.touchingUnloadedChunk()) {
            cir.setReturnValue(false);
        } else {
            AABB $$2 = this.getBoundingBox().deflate(0.001);
            int $$3 = Mth.floor($$2.minX);
            int $$4 = Mth.ceil($$2.maxX);
            int $$5 = Mth.floor($$2.minY);
            int $$6 = Mth.ceil($$2.maxY);
            int $$7 = Mth.floor($$2.minZ);
            int $$8 = Mth.ceil($$2.maxZ);
            double $$9 = 0.0;
            boolean $$10 = this.isPushedByFluid();
            boolean $$11 = false;
            Vec3 $$12 = Vec3.ZERO;
            int $$13 = 0;
            BlockPos.MutableBlockPos $$14 = new BlockPos.MutableBlockPos();

            for (int $$15 = $$3; $$15 < $$4; $$15++) {
                for (int $$16 = $$5; $$16 < $$6; $$16++) {
                    for (int $$17 = $$7; $$17 < $$8; $$17++) {
                        $$14.set($$15, $$16, $$17);
                        FluidState $$18 = this.level().getFluidState($$14);
                        if ($$18.is($$0)) {
                            double $$19 = (double)((float)$$16 + $$18.getHeight(this.level(), $$14));
                            if ($$19 >= $$2.minY) {
                                $$11 = true;
                                $$9 = Math.max($$19 - $$2.minY, $$9);
                                if ($$10) {
                                    Vec3 $$20 = $$18.getFlow(this.level(), $$14);
                                    if ($$9 < 0.4) {
                                        $$20 = $$20.scale($$9);
                                    }

                                    $$12 = $$12.add($$20);
                                    $$13++;
                                }
                            }
                        }
                    }
                }
            }

            if ($$12.length() > 0.0) {
                if ($$13 > 0) {
                    $$12 = $$12.scale(1.0 / (double)$$13);
                }

                if (!(((Entity)(Object)this) instanceof Player)) {
                    $$12 = $$12.normalize();
                }

                Vec3 $$21 = this.getDeltaMovement();
                $$12 = $$12.scale($$1 * 1.0);
                double $$22 = 0.003;
                if (Math.abs($$21.x) < 0.003 && Math.abs($$21.z) < 0.003 && $$12.length() < 0.0045000000000000005) {
                    $$12 = $$12.normalize().scale(0.0045000000000000005);
                }

                //This is the main change made to the function
                this.setDeltaMovement(this.getDeltaMovement().add(RotationUtil.vecWorldToPlayer($$12, gravityDirection)));
            }

            this.fluidHeight.put($$0, $$9);
            cir.setReturnValue($$11);
        }
    }

    @Inject(
            method = "getDirection",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$getDirection(CallbackInfoReturnable<Direction> cir) {

        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        cir.setReturnValue(Direction.fromYRot(RotationUtil.rotPlayerToWorld((float) this.getYRot(), this.getXRot(), gravityDirection).x));
    }

    @Inject(
            method = "isInWall",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$isInWall(CallbackInfoReturnable<Boolean> cir) {

        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

       if (this.noPhysics) {
           cir.setReturnValue(false);
        } else {
            float $$0 = this.dimensions.width * 0.8F;

           Vec3 rotate = new Vec3((double)$$0, 1.0E-6, (double)$$0);
           rotate = RotationUtil.vecPlayerToWorld(rotate, GravityAPI.getGravityDirection((Entity) (Object) this));

            AABB $$1 = AABB.ofSize(this.getEyePosition(), rotate.x,rotate.y,rotate.z);
            cir.setReturnValue(BlockPos.betweenClosedStream($$1)
                    .anyMatch(
                            $$1x -> {
                                BlockState $$2 = this.level().getBlockState($$1x);
                                return !$$2.isAir()
                                        && $$2.isSuffocating(this.level(), $$1x)
                                        && Shapes.joinIsNotEmpty(
                                        $$2.getCollisionShape(this.level(), $$1x).move((double)$$1x.getX(), (double)$$1x.getY(), (double)$$1x.getZ()),
                                        Shapes.create($$1),
                                        BooleanOp.AND
                                );
                            }
                    )
            );
       }
    }

    @Inject(
            method = "collideBoundingBox(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/Level;Ljava/util/List;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void roundabout$collideBoundingBox(@Nullable Entity entity, Vec3 movement, AABB entityBoundingBox, Level $$3, List<VoxelShape> collisions, CallbackInfoReturnable<Vec3> cir) {
        if (entity != null) {
            Direction gravityDirection = GravityAPI.getGravityDirection(entity);
            if (gravityDirection == Direction.DOWN) return;

            ImmutableList.Builder<VoxelShape> $$5 = ImmutableList.builderWithExpectedSize(collisions.size() + 1);
            if (!collisions.isEmpty()) {
                $$5.addAll(collisions);
            }

            WorldBorder $$6 = $$3.getWorldBorder();
            boolean $$7 = $$6.isInsideCloseToBorder(entity, entityBoundingBox.expandTowards(movement));
            if ($$7) {
                $$5.add($$6.getCollisionShape());
            }

            $$5.addAll($$3.getBlockCollisions(entity, entityBoundingBox.expandTowards(movement)));

            Vec3 playerMovement = RotationUtil.vecWorldToPlayer(movement, gravityDirection);
            double playerMovementX = playerMovement.x;
            double playerMovementY = playerMovement.y;
            double playerMovementZ = playerMovement.z;
            Direction directionX = RotationUtil.dirPlayerToWorld(Direction.EAST, gravityDirection);
            Direction directionY = RotationUtil.dirPlayerToWorld(Direction.UP, gravityDirection);
            Direction directionZ = RotationUtil.dirPlayerToWorld(Direction.SOUTH, gravityDirection);
            if (playerMovementY != 0.0D) {
                playerMovementY = Shapes.collide(directionY.getAxis(), entityBoundingBox, collisions, playerMovementY * directionY.getAxisDirection().getStep()) * directionY.getAxisDirection().getStep();
                if (playerMovementY != 0.0D) {
                    entityBoundingBox = entityBoundingBox.move(RotationUtil.vecPlayerToWorld(0.0D, playerMovementY, 0.0D, gravityDirection));
                }
            }

            boolean isZLargerThanX = Math.abs(playerMovementX) < Math.abs(playerMovementZ);
            if (isZLargerThanX && playerMovementZ != 0.0D) {
                playerMovementZ = Shapes.collide(directionZ.getAxis(), entityBoundingBox, collisions, playerMovementZ * directionZ.getAxisDirection().getStep()) * directionZ.getAxisDirection().getStep();
                if (playerMovementZ != 0.0D) {
                    entityBoundingBox = entityBoundingBox.move(RotationUtil.vecPlayerToWorld(0.0D, 0.0D, playerMovementZ, gravityDirection));
                }
            }

            if (playerMovementX != 0.0D) {
                playerMovementX = Shapes.collide(directionX.getAxis(), entityBoundingBox, collisions, playerMovementX * directionX.getAxisDirection().getStep()) * directionX.getAxisDirection().getStep();
                if (!isZLargerThanX && playerMovementX != 0.0D) {
                    entityBoundingBox = entityBoundingBox.move(RotationUtil.vecPlayerToWorld(playerMovementX, 0.0D, 0.0D, gravityDirection));
                }
            }

            if (!isZLargerThanX && playerMovementZ != 0.0D) {
                playerMovementZ = Shapes.collide(directionZ.getAxis(), entityBoundingBox, collisions, playerMovementZ * directionZ.getAxisDirection().getStep()) * directionZ.getAxisDirection().getStep();
            }

            cir.setReturnValue(RotationUtil.vecPlayerToWorld(playerMovementX, playerMovementY, playerMovementZ, gravityDirection));
        }
    }


    // the argument was transformed to local coord,
    // but bounding box stretch needs world coord
    // the argument was transformed to local coord,
    // but bounding box move needs world coord
    @Inject(
            method = "collide",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$collide(Vec3 $$0,CallbackInfoReturnable<Vec3> cir) {

        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        Entity getThis = ((Entity) (Object)this);
        AABB $$1 = this.getBoundingBox();
        List<VoxelShape> $$2 = this.level().getEntityCollisions(getThis, $$1.expandTowards($$0));
        Vec3 $$3 = $$0.lengthSqr() == 0.0 ? $$0 : collideBoundingBox(getThis, $$0, $$1, this.level(), $$2);
        boolean $$4 = $$0.x != $$3.x;
        boolean $$5 = $$0.y != $$3.y;
        boolean $$6 = $$0.z != $$3.z;
        boolean $$7 = this.onGround || $$5 && $$0.y < 0.0;
        if (this.maxUpStep() > 0.0F && $$7 && ($$4 || $$6)) {
            Vec3 $$8 = collideBoundingBox(getThis, new Vec3($$0.x, (double)this.maxUpStep(), $$0.z), $$1, this.level(), $$2);
            Vec3 yeah = RotationUtil.vecPlayerToWorld(new Vec3($$0.x, 0.0, $$0.z), GravityAPI.getGravityDirection((Entity) (Object) this));
            Vec3 $$9 = collideBoundingBox(getThis, new Vec3(0.0, (double)this.maxUpStep(), 0.0), $$1.expandTowards(yeah.x,yeah.y,yeah.z), this.level(), $$2);
            if ($$9.y < (double)this.maxUpStep) {
                Vec3 $$10 = collideBoundingBox(getThis, new Vec3($$0.x, 0.0, $$0.z), $$1.move(RotationUtil.vecPlayerToWorld($$9, GravityAPI.getGravityDirection((Entity) (Object) this))), this.level(), $$2).add($$9);
                if ($$10.horizontalDistanceSqr() > $$8.horizontalDistanceSqr()) {
                    $$8 = $$10;
                }
            }

            if ($$8.horizontalDistanceSqr() > $$3.horizontalDistanceSqr()) {

                cir.setReturnValue($$8.add(collideBoundingBox(getThis, new Vec3(0.0, -$$8.y + $$0.y, 0.0), $$1.move(RotationUtil.vecPlayerToWorld($$8, GravityAPI.getGravityDirection((Entity) (Object) this))), this.level(), $$2)));
            }
        }

        cir.setReturnValue($$3);
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    private Vec3 position;

    @Shadow
    private EntityDimensions dimensions;

    @Shadow
    private float eyeHeight;

    @Shadow
    public double xo;

    @Shadow
    public double yo;

    @Shadow
    public double zo;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract Vec3 getEyePosition();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public Level level;

    @Shadow
    public abstract int getBlockX();

    @Shadow
    public abstract int getBlockZ();

    @Shadow
    public boolean noPhysics;

    @Shadow
    public abstract Vec3 getDeltaMovement();

    @Shadow
    public abstract boolean isVehicle();

    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow
    public static Vec3 collideWithShapes(Vec3 movement, AABB entityBoundingBox, List<VoxelShape> collisions) {
        return null;
    }

    @Shadow
    public abstract Vec3 position();


    @Shadow
    public abstract boolean isPassengerOfSameVehicle(Entity entity);

    @Shadow
    public abstract void push(double deltaX, double deltaY, double deltaZ);

    @Shadow
    protected abstract void onBelowWorld();

    @Shadow
    public abstract double getEyeY();

    @Shadow
    public abstract float getViewYRot(float tickDelta);

    @Shadow
    public abstract float getYRot();

    @Shadow
    public abstract float getXRot();

    @Shadow
    @Final
    protected RandomSource random;

    @Shadow
    public float fallDistance;
}
