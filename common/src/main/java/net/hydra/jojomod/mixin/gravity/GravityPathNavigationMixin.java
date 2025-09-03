package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(PathNavigation.class)
public abstract class GravityPathNavigationMixin {
//    @Shadow protected int tick;
//
//    @Shadow protected boolean hasDelayedRecomputation;
//
//    @Shadow public abstract void recomputePath();
//
//    @Shadow public abstract boolean isDone();
//
//    @Shadow protected abstract boolean canUpdatePath();
//
//    @Shadow protected abstract void followThePath();
//
//    @Shadow @Final protected Mob mob;
//
//    @Shadow @Nullable protected Path path;
//
//    @Shadow @Nullable public abstract Path getPath();
//
//    @Shadow protected abstract Vec3 getTempMobPos();
//
//    @Shadow @Final protected Level level;
//
//    @Shadow protected float maxDistanceToWaypoint;
//
//    @Shadow protected abstract double getGroundY(Vec3 vec3);
//
//    @Shadow protected double speedModifier;
//
//
//
//    @Shadow @Nullable private BlockPos targetPos;
//
//    @Shadow @Final private PathFinder pathFinder;
//
//    @Shadow private float maxVisitedNodesMultiplier;
//
//    @Shadow private int reachRange;
//
//    @Shadow protected abstract void resetStuckTimeout();
//
//    @Shadow public abstract boolean canCutCorner(BlockPathTypes blockPathTypes);
//
//    @Shadow protected abstract void doStuckDetection(Vec3 vec3);
//
//    @Shadow protected abstract boolean shouldTargetNextNodeInDirection(Vec3 vec3);
//
//    @Shadow protected int lastStuckCheck;
//
//    @Shadow protected Vec3 lastStuckCheckPos;
//
//    @Shadow private boolean isStuck;
//
//    @Shadow public abstract void stop();
//
//    @Shadow protected Vec3i timeoutCachedNode;
//
//    @Shadow protected long timeoutTimer;
//
//    @Shadow protected long lastTimeoutCheck;
//
//    @Shadow protected double timeoutLimit;
//
//    @Shadow protected abstract void timeoutPath();
//
//    @Inject(
//            method = "isClearForMovementBetween(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Z)Z",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    private static void rdbt$isClearForMovementBetween(Mob $$0, Vec3 $$1, Vec3 $$2, boolean $$3, CallbackInfoReturnable<Boolean> cir) {
//        if ($$0 != null) {
//            Direction dir = ((IGravityEntity) $$0).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//            Vec3 vec = new Vec3(0, (double)$$0.getBbHeight() * 0.5, 0);
//            vec = RotationUtil.vecPlayerToWorld(vec,dir);
//
//            Vec3 $$4 = new Vec3($$2.x + vec.x, $$2.y + vec.y, $$2.z + vec.z);
//
//            cir.setReturnValue($$0.level().clip(new ClipContext($$1, $$4, ClipContext.Block.COLLIDER, $$3 ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, $$0)).getType()
//                    == HitResult.Type.MISS);
//        }
//    }
//    @Inject(
//            method = "followThePath()V",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$followThePath(CallbackInfo ci) {
//        if (this.mob != null) {
//            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//
//            ci.cancel();
//
//            Vec3 $$0 = this.getTempMobPos();
//            this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
//            Vec3i $$1 = this.path.getNextNodePos();
//
//            Vec3 newVec = new Vec3(0.5,
//                    0,
//                    0.5);
//            newVec = RotationUtil.vecPlayerToWorld(newVec,dir);
//
//            double $$2 = Math.abs(this.mob.getX() - ((double)$$1.getX() + newVec.x));
//            double $$3 = Math.abs(this.mob.getY() - (double)$$1.getY()+ newVec.y);
//            double $$4 = Math.abs(this.mob.getZ() - ((double)$$1.getZ() + newVec.z));
//            boolean $$5 = $$2 < (double)this.maxDistanceToWaypoint && $$4 < (double)this.maxDistanceToWaypoint && $$3 < 1.0;
//            if ($$5 || this.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection($$0)) {
//                this.path.advance();
//            }
//
//            this.doStuckDetection($$0);
//        }
//    }
//
//            @Inject(
//            method = "createPath(Ljava/util/Set;IZIF)Lnet/minecraft/world/level/pathfinder/Path;",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$createPath(Set<BlockPos> $$0, int $$1, boolean $$2, int $$3, float $$4, CallbackInfoReturnable<Path> cir) {
//        if (this.mob != null) {
//            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//
//            if ($$0.isEmpty()) {
//                cir.setReturnValue(null);
//            } else if (this.mob.getY() < (double)this.level.getMinBuildHeight()) {
//                cir.setReturnValue(null);
//            } else if (!this.canUpdatePath()) {
//                cir.setReturnValue(null);
//            } else if (this.path != null && !this.path.isDone() && $$0.contains(this.targetPos)) {
//                cir.setReturnValue(this.path);
//            } else {
//                this.level.getProfiler().push("pathfind");
//                BlockPos $$5 = $$2 ? this.mob.blockPosition().relative(dir.getOpposite()) : this.mob.blockPosition();
//                int $$6 = (int)($$4 + (float)$$1);
//                PathNavigationRegion $$7 = new PathNavigationRegion(this.level, $$5.offset(-$$6, -$$6, -$$6), $$5.offset($$6, $$6, $$6));
//                Path $$8 = this.pathFinder.findPath($$7, this.mob, $$0, $$4, $$3, this.maxVisitedNodesMultiplier);
//                this.level.getProfiler().pop();
//                if ($$8 != null && $$8.getTarget() != null) {
//                    this.targetPos = $$8.getTarget();
//                    this.reachRange = $$3;
//                    this.resetStuckTimeout();
//                }
//                cir.setReturnValue($$8);
//            }
//        }
//    }
//
//    @Inject(
//            method = "isStableDestination(Lnet/minecraft/core/BlockPos;)Z",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//
//    public void rdbt$isStableDestination(BlockPos $$0, CallbackInfoReturnable<Boolean> cir) {
//        if (this.mob != null) {
//            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//            BlockPos $$1 = $$0.relative(dir);
//            cir.setReturnValue( this.level.getBlockState($$1).isSolidRender(this.level, $$1));
//        }
//    }
//
//            @Inject(
//            method = "tick",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$tickTrue(CallbackInfo ci) {
//        if (this.mob != null){
//            Direction dir = ((IGravityEntity)this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//
//            ci.cancel();
//
//            this.tick++;
//            if (this.hasDelayedRecomputation) {
//                this.recomputePath();
//            }
//
//            if (!this.isDone()) {
//                if (this.canUpdatePath()) {
//                    this.followThePath();
//                } else if (this.path != null && !this.path.isDone()) {
//                    Vec3 $$0 = this.getTempMobPos();
//                    Vec3 $$1 = this.path.getNextEntityPos(this.mob);
//                    if (dir == Direction.UP) {
//                        if ($$0.y < $$1.y && !this.mob.onGround() && Mth.floor($$0.x) == Mth.floor($$1.x) && Mth.floor($$0.z) == Mth.floor($$1.z)) {
//                            this.path.advance();
//                        }
//                    }
//                    if (dir == Direction.NORTH) {
//                        if ($$0.z > $$1.z && !this.mob.onGround() && Mth.floor($$0.x) == Mth.floor($$1.x) && Mth.floor($$0.y) == Mth.floor($$1.y)) {
//                            this.path.advance();
//                        }
//                    }
//                    if (dir == Direction.SOUTH) {
//                        if ($$0.z < $$1.z && !this.mob.onGround() && Mth.floor($$0.x) == Mth.floor($$1.x) && Mth.floor($$0.y) == Mth.floor($$1.y)) {
//                            this.path.advance();
//                        }
//                    }
//                    if (dir == Direction.WEST) {
//                        if ($$0.x > $$1.x && !this.mob.onGround() && Mth.floor($$0.z) == Mth.floor($$1.z) && Mth.floor($$0.y) == Mth.floor($$1.y)) {
//                            this.path.advance();
//                        }
//                    }
//                    if (dir == Direction.EAST) {
//                        if ($$0.x < $$1.x && !this.mob.onGround() && Mth.floor($$0.z) == Mth.floor($$1.z) && Mth.floor($$0.y) == Mth.floor($$1.y)) {
//                            this.path.advance();
//                        }
//                    }
//                }
//
//                DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
//                if (!this.isDone()) {
//                    Vec3 $$2 = this.path.getNextEntityPos(this.mob);
//                    if (dir == Direction.UP) {
//                        this.mob.getMoveControl().setWantedPosition($$2.x, rdbt$getGroundY2(dir, $$2), $$2.z, this.speedModifier);
//                    } if (dir == Direction.NORTH) {
//                        this.mob.getMoveControl().setWantedPosition($$2.x, $$2.y, rdbt$getGroundZ(dir, $$2), this.speedModifier);
//                    } if (dir == Direction.SOUTH) {
//                        this.mob.getMoveControl().setWantedPosition($$2.x, $$2.y, rdbt$getGroundZ2(dir, $$2), this.speedModifier);
//                    } if (dir == Direction.WEST) {
//                        this.mob.getMoveControl().setWantedPosition(rdbt$getGroundX(dir, $$2), $$2.y, $$2.z, this.speedModifier);
//                    } if (dir == Direction.EAST) {
//                        this.mob.getMoveControl().setWantedPosition(rdbt$getGroundX2(dir, $$2), $$2.y, $$2.z, this.speedModifier);
//                    }
//                }
//            }
//        }
//    }
//
//    @Unique
//    public double rdbt$getFloorLevelX2(Direction dir, BlockGetter $$0, BlockPos $$1) {
//        BlockPos $$2 = $$1.relative(dir);
//        VoxelShape $$3 = $$0.getBlockState($$2).getCollisionShape($$0, $$2);
//        return (double)$$2.getX() + ($$3.isEmpty() ? 0.0 : (-1*$$3.max(Direction.Axis.X)));
//    }
//
//    @Unique
//    public double rdbt$getGroundX(Direction dir, Vec3 $$0) {
//        BlockPos $$1 = BlockPos.containing($$0);
//        return this.level.getBlockState($$1.relative(dir)).isAir() ? $$0.x : rdbt$getFloorLevelX(dir, this.level, $$1);
//    }
//
//    @Unique
//    public double rdbt$getFloorLevelX(Direction dir, BlockGetter $$0, BlockPos $$1) {
//        BlockPos $$2 = $$1.relative(dir);
//        VoxelShape $$3 = $$0.getBlockState($$2).getCollisionShape($$0, $$2);
//        return (double)$$2.getX() + ($$3.isEmpty() ? 0.0 : (1*$$3.max(Direction.Axis.X)));
//    }
//
//    @Unique
//    public double rdbt$getGroundX2(Direction dir, Vec3 $$0) {
//        BlockPos $$1 = BlockPos.containing($$0);
//        return this.level.getBlockState($$1.relative(dir)).isAir() ? $$0.x : rdbt$getFloorLevelX2(dir, this.level, $$1);
//    }
//
//    @Unique
//    public double rdbt$getGroundZ2(Direction dir, Vec3 $$0) {
//        BlockPos $$1 = BlockPos.containing($$0);
//        return this.level.getBlockState($$1.relative(dir)).isAir() ? $$0.z : rdbt$getFloorLevelZ2(dir, this.level, $$1);
//    }
//    @Unique
//    public double rdbt$getFloorLevelZ2(Direction dir, BlockGetter $$0, BlockPos $$1) {
//        BlockPos $$2 = $$1.relative(dir);
//        VoxelShape $$3 = $$0.getBlockState($$2).getCollisionShape($$0, $$2);
//        return (double)$$2.getZ() + ($$3.isEmpty() ? 0.0 : (-1*$$3.max(Direction.Axis.Z)));
//    }
//
//    @Unique
//    public double rdbt$getGroundZ(Direction dir, Vec3 $$0) {
//        BlockPos $$1 = BlockPos.containing($$0);
//        return this.level.getBlockState($$1.relative(dir)).isAir() ? $$0.z : rdbt$getFloorLevelZ(dir, this.level, $$1);
//    }
//
//    @Unique
//    public double rdbt$getFloorLevelZ(Direction dir, BlockGetter $$0, BlockPos $$1) {
//        BlockPos $$2 = $$1.relative(dir);
//        VoxelShape $$3 = $$0.getBlockState($$2).getCollisionShape($$0, $$2);
//        return (double)$$2.getZ() + ($$3.isEmpty() ? 0.0 : (1*$$3.max(Direction.Axis.Z)));
//    }
//
//    @Unique
//    public double rdbt$getGroundY2(Direction dir, Vec3 $$0) {
//        BlockPos $$1 = BlockPos.containing($$0);
//        return this.level.getBlockState($$1.relative(dir)).isAir() ? $$0.y : rdbt$getFloorLevel(dir, this.level, $$1);
//    }
//
//    @Unique
//    public double rdbt$getFloorLevel(Direction dir, BlockGetter $$0, BlockPos $$1) {
//        BlockPos $$2 = $$1.relative(dir);
//        VoxelShape $$3 = $$0.getBlockState($$2).getCollisionShape($$0, $$2);
//        return (double)$$2.getY() + ($$3.isEmpty() ? 0.0 : (-1*$$3.max(Direction.Axis.Y)));
//    }
}
