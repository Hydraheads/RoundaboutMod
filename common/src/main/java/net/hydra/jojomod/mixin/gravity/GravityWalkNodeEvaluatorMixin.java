package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public abstract class GravityWalkNodeEvaluatorMixin extends NodeEvaluator {
//    @Shadow protected abstract boolean canStartAt(BlockPos blockPos);
//
//    @Shadow protected abstract Node getStartNode(BlockPos blockPos);
//
//    @Shadow protected abstract BlockPathTypes getCachedBlockType(Mob mob, int i, int j, int k);
//
//    @Shadow
//    public static double getFloorLevel(BlockGetter blockGetter, BlockPos blockPos) {
//        return 0;
//    }
//
//    @Shadow protected abstract double getFloorLevel(BlockPos blockPos);
//
//    @Shadow @Nullable protected abstract Node findAcceptedNode(int i, int j, int k, int l, double d, Direction direction, BlockPathTypes blockPathTypes);
//
//    @Shadow protected abstract boolean isNeighborValid(@Nullable Node node, Node node2);
//
//    @Shadow protected abstract boolean isDiagonalValid(Node node, @Nullable Node node2, @Nullable Node node3, @Nullable Node node4);
//
//    @Shadow protected abstract boolean isAmphibious();
//
//    @Shadow protected abstract double getMobJumpHeight();
//
//    @Shadow protected abstract Node getNodeAndUpdateCostToMax(int i, int j, int k, BlockPathTypes blockPathTypes, float f);
//
//    @Shadow
//    protected static boolean doesBlockHavePartialCollision(BlockPathTypes blockPathTypes) {
//        return false;
//    }
//
//    @Shadow protected abstract boolean canReachWithoutCollision(Node node);
//
//    @Shadow protected abstract boolean hasCollisions(AABB aABB);
//
//    @Shadow protected abstract Node getBlockedNode(int i, int j, int k);
//
//    @Shadow
//    public static BlockPathTypes checkNeighbourBlocks(BlockGetter blockGetter, BlockPos.MutableBlockPos mutableBlockPos, BlockPathTypes blockPathTypes) {
//        return null;
//    }
//
//    @Shadow
//    protected static BlockPathTypes getBlockPathTypeRaw(BlockGetter blockGetter, BlockPos blockPos) {
//        return null;
//    }
//
//    @Inject(
//            method = "getStart()Lnet/minecraft/world/level/pathfinder/Node;",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$getStart(CallbackInfoReturnable<Node> cir) {
//        if (this.mob != null) {
//            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//
//            BlockPos.MutableBlockPos $$0 = new BlockPos.MutableBlockPos();
//            int $$1 = this.mob.getBlockY();
//            Vec3 bs= new Vec3(this.mob.getX(), (double)$$1, this.mob.getZ());
//
//            if (dir == Direction.NORTH || dir == Direction.SOUTH){
//                $$1 = this.mob.getBlockZ();
//                bs= new Vec3(this.mob.getX(), this.mob.getY(), (double)$$1);
//            }
//            if (dir == Direction.EAST || dir == Direction.WEST){
//                $$1 = this.mob.getBlockX();
//                bs= new Vec3((double)$$1, this.mob.getY(), this.mob.getZ());
//            }
//
//            BlockState $$2 = this.level.getBlockState($$0.set(bs.x, bs.y, bs.z));
//            if (!this.mob.canStandOnFluid($$2.getFluidState())) {
//                if (this.canFloat() && this.mob.isInWater()) {
//                    while (true) {
//                        if (!$$2.is(Blocks.WATER) && $$2.getFluidState() != Fluids.WATER.getSource(false)) {
//                            if (dir == Direction.EAST || dir == Direction.SOUTH  || dir == Direction.UP){
//                                $$1++;
//                            } else {
//                                $$1--;
//                            }
//                            break;
//                        }
//
//                        if (dir == Direction.UP){
//                            $$2 = this.level.getBlockState($$0.set(this.mob.getX(), (double)(--$$1), this.mob.getZ()));
//                        }
//                        if (dir == Direction.NORTH){
//                            $$2 = this.level.getBlockState($$0.set(this.mob.getX(), this.mob.getY(), (double)(++$$1)));
//                        }
//                        if (dir == Direction.SOUTH){
//                            $$2 = this.level.getBlockState($$0.set(this.mob.getX(), this.mob.getY(), (double)(--$$1)));
//                        }
//                        if (dir == Direction.EAST){
//                            $$2 = this.level.getBlockState($$0.set((double)(--$$1), this.mob.getY(), this.mob.getZ()));
//                        }
//                        if (dir == Direction.WEST){
//                            $$2 = this.level.getBlockState($$0.set((double)(++$$1), this.mob.getY(), this.mob.getZ()));
//                        }
//                    }
//                } else if (this.mob.onGround()) {
//                    $$1 = Mth.floor(this.mob.getY() + 0.5);
//                    if (dir == Direction.NORTH){
//                        $$1 = Mth.floor(this.mob.getZ() + 0.5);
//                    } if (dir == Direction.SOUTH){
//                        $$1 = Mth.floor(this.mob.getZ() - 0.5);
//                    }if (dir == Direction.WEST){
//                        $$1 = Mth.floor(this.mob.getX() + 0.5);
//                    } if (dir == Direction.EAST){
//                        $$1 = Mth.floor(this.mob.getX() - 0.5);
//                    } if (dir == Direction.UP){
//                        $$1 = Mth.floor(this.mob.getY() - 0.5);
//                    }
//                } else {
//                    BlockPos $$3 = this.mob.blockPosition();
//
//                    while (
//                            (this.level.getBlockState($$3).isAir() || this.level.getBlockState($$3).isPathfindable(this.level, $$3, PathComputationType.LAND))
//                                    && $$3.getY() > this.mob.level().getMinBuildHeight()
//                    ) {
//                        $$3 = $$3.relative(dir);
//                    }
//
//                    if (dir == Direction.NORTH){
//                        $$1 = $$3.relative(dir.getOpposite()).getZ();
//                    } if (dir == Direction.SOUTH){
//                        $$1 = $$3.relative(dir).getZ();
//                    }if (dir == Direction.WEST){
//                        $$1 = $$3.relative(dir.getOpposite()).getX();
//                    } if (dir == Direction.EAST){
//                        $$1 = $$3.relative(dir).getX();
//                    } if (dir == Direction.UP){
//                        $$1 = $$3.below().getY();
//                    }
//                }
//            } else {
//                while (this.mob.canStandOnFluid($$2.getFluidState())) {
//                    if (dir == Direction.UP){
//                        $$2 = this.level.getBlockState($$0.set(this.mob.getX(), (double)(--$$1), this.mob.getZ()));
//                    }
//                    if (dir == Direction.NORTH){
//                        $$2 = this.level.getBlockState($$0.set(this.mob.getX(), this.mob.getY(), (double)(++$$1)));
//                    }
//                    if (dir == Direction.SOUTH){
//                        $$2 = this.level.getBlockState($$0.set(this.mob.getX(), this.mob.getY(), (double)(--$$1)));
//                    }
//                    if (dir == Direction.EAST){
//                        $$2 = this.level.getBlockState($$0.set((double)(--$$1), this.mob.getY(), this.mob.getZ()));
//                    }
//                    if (dir == Direction.WEST){
//                        $$2 = this.level.getBlockState($$0.set((double)(++$$1), this.mob.getY(), this.mob.getZ()));
//                    }
//                }
//
//                if (dir == Direction.EAST || dir == Direction.SOUTH  || dir == Direction.UP){
//                    $$1++;
//                } else {
//                    $$1--;
//                }
//            }
//
//
//            BlockPos $$4 = this.mob.blockPosition();
//            if (dir == Direction.UP){
//                if (!this.canStartAt($$0.set($$4.getX(), $$1, $$4.getZ()))) {
//                    AABB $$5 = this.mob.getBoundingBox();
//                    if (this.canStartAt($$0.set($$5.minX, (double)$$1, $$5.minZ))
//                            || this.canStartAt($$0.set($$5.minX, (double)$$1, $$5.maxZ))
//                            || this.canStartAt($$0.set($$5.maxX, (double)$$1, $$5.minZ))
//                            || this.canStartAt($$0.set($$5.maxX, (double)$$1, $$5.maxZ))) {
//                        cir.setReturnValue(this.getStartNode($$0));
//                        return;
//                    }
//                }
//            }
//            if (dir == Direction.NORTH || dir == Direction.SOUTH){
//                if (!this.canStartAt($$0.set($$4.getX(), $$4.getY(), $$1))) {
//                    AABB $$5 = this.mob.getBoundingBox();
//                    if (this.canStartAt($$0.set($$5.minX, $$5.minY,(double)$$1))
//                            || this.canStartAt($$0.set($$5.minX, $$5.maxY, (double)$$1))
//                            || this.canStartAt($$0.set($$5.maxX, $$5.minY, (double)$$1))
//                            || this.canStartAt($$0.set($$5.maxX, $$5.maxY, (double)$$1))) {
//                        cir.setReturnValue(this.getStartNode($$0));
//                        return;
//                    }
//                }
//            }
//            if (dir == Direction.EAST || dir == Direction.WEST){
//                if (!this.canStartAt($$0.set($$1, $$4.getY(), $$4.getZ()))) {
//                    AABB $$5 = this.mob.getBoundingBox();
//                    if (this.canStartAt($$0.set((double)$$1, $$5.minY, $$5.minZ))
//                            || this.canStartAt($$0.set((double)$$1, $$5.maxY, $$5.maxZ))
//                            || this.canStartAt($$0.set((double)$$1, $$5.minY, $$5.minZ))
//                            || this.canStartAt($$0.set((double)$$1, $$5.maxY, $$5.maxZ))) {
//                        cir.setReturnValue(this.getStartNode($$0));
//                        return;
//                    }
//                }
//            }
//
//
//            if (dir == Direction.UP) {
//                cir.setReturnValue(getStartNode(new BlockPos($$4.getX(), $$1, $$4.getZ())));
//                return;
//            }
//            if (dir == Direction.NORTH || dir == Direction.SOUTH){
//                cir.setReturnValue(getStartNode(new BlockPos($$4.getX(), $$4.getY(), $$1)));
//                return;
//            }
//            if (dir == Direction.EAST || dir == Direction.WEST){
//                cir.setReturnValue(getStartNode(new BlockPos($$1, $$4.getY(), $$4.getZ())));
//                return;
//            }
//        }
//    }
//    @Inject(
//            method = "getNeighbors([Lnet/minecraft/world/level/pathfinder/Node;Lnet/minecraft/world/level/pathfinder/Node;)I",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$getNeighbors(Node[] $$0, Node $$1,CallbackInfoReturnable<Integer> cir) {
//        if (this.mob != null) {
//            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//
//            int $$2 = 0;
//            int $$3 = 0;
//            BlockPathTypes $$4 = this.getCachedBlockType(this.mob, $$1.x, $$1.y, $$1.z+ 1);
//            BlockPathTypes $$5 = this.getCachedBlockType(this.mob, $$1.x, $$1.y, $$1.z);
//            if (this.mob.getPathfindingMalus($$4) >= 0.0F && $$5 != BlockPathTypes.STICKY_HONEY) {
//                $$3 = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
//            }
//
//            double $$6 = this.getFloorLevel(new BlockPos($$1.x, $$1.y, $$1.z));
//
//            Vec3 rot = RotationUtil.vecPlayerToWorld(new Vec3(0,0,1),dir);
//            Node $$7 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.SOUTH,dir), $$5);
//            if (this.isNeighborValid($$7, $$1)) {
//                $$0[$$2++] = $$7;
//            }
//
//            rot = RotationUtil.vecPlayerToWorld(new Vec3(-1,0,0),dir);
//            Node $$8 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.WEST,dir), $$5);
//            if (this.isNeighborValid($$8, $$1)) {
//                $$0[$$2++] = $$8;
//            }
//
//            rot = RotationUtil.vecPlayerToWorld(new Vec3(1,0,0),dir);
//            Node $$9 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.EAST,dir), $$5);
//            if (this.isNeighborValid($$9, $$1)) {
//                $$0[$$2++] = $$9;
//            }
//
//            rot = RotationUtil.vecPlayerToWorld(new Vec3(0,0,-1),dir);
//            Node $$10 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.NORTH,dir), $$5);
//            if (this.isNeighborValid($$10, $$1)) {
//                $$0[$$2++] = $$10;
//            }
//
//            rot = RotationUtil.vecPlayerToWorld(new Vec3(-1,0,-1),dir);
//            Node $$11 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.NORTH,dir), $$5);
//            if (this.isDiagonalValid($$1, $$8, $$10, $$11)) {
//                $$0[$$2++] = $$11;
//            }
//
//            rot = RotationUtil.vecPlayerToWorld(new Vec3(1,0,-1),dir);
//            Node $$12 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.NORTH,dir), $$5);
//            if (this.isDiagonalValid($$1, $$9, $$10, $$12)) {
//                $$0[$$2++] = $$12;
//            }
//
//            rot = RotationUtil.vecPlayerToWorld(new Vec3(-1,0,1),dir);
//            Node $$13 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.SOUTH,dir), $$5);
//            if (this.isDiagonalValid($$1, $$8, $$7, $$13)) {
//                $$0[$$2++] = $$13;
//            }
//
//            rot = RotationUtil.vecPlayerToWorld(new Vec3(1,0,1),dir);
//            Node $$14 = this.findAcceptedNode((int) ($$1.x+rot.x), (int) ($$1.y+rot.y), (int) ($$1.z+rot.z), $$3, $$6, RotationUtil.dirPlayerToWorld(Direction.SOUTH,dir), $$5);
//            if (this.isDiagonalValid($$1, $$9, $$7, $$14)) {
//                $$0[$$2++] = $$14;
//            }
//
//            cir.setReturnValue($$2);
//            return;
//        }
//    }
//
//    @Inject(
//            method = "isDiagonalValid(Lnet/minecraft/world/level/pathfinder/Node;Lnet/minecraft/world/level/pathfinder/Node;Lnet/minecraft/world/level/pathfinder/Node;Lnet/minecraft/world/level/pathfinder/Node;)Z",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$isDiagonalValid(Node $$0, Node $$1, Node $$2, Node $$3, CallbackInfoReturnable<Boolean> cir){
//        if (this.mob != null) {
//            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//
//            if ($$3 == null || $$2 == null || $$1 == null) {
//                cir.setReturnValue(false);
//                return;
//            } else if ($$3.closed) {
//                cir.setReturnValue(false);
//                return;
//            }
//
//
//            boolean check1 = $$2.y > $$0.y;
//            boolean check2 = $$1.y > $$0.y;
//            if (dir == Direction.UP){
//                check1 = $$2.y < $$0.y;
//                check2 = $$1.y < $$0.y;
//            }if (dir == Direction.NORTH){
//                check1 = $$2.z > $$0.z;
//                check2 = $$1.z > $$0.z;
//            }if (dir == Direction.SOUTH){
//                check1 = $$2.z < $$0.z;
//                check2 = $$1.z < $$0.z;
//            }if (dir == Direction.WEST){
//                check1 = $$2.x > $$0.x;
//                check2 = $$1.x > $$0.x;
//            }if (dir == Direction.EAST){
//                check1 = $$2.x < $$0.x;
//                check2 = $$1.x < $$0.x;
//            }
//
//            if (check1 || check2) {
//                cir.setReturnValue(false);
//            } else if ($$1.type != BlockPathTypes.WALKABLE_DOOR && $$2.type != BlockPathTypes.WALKABLE_DOOR && $$3.type != BlockPathTypes.WALKABLE_DOOR) {
//                boolean $$4 = $$2.type == BlockPathTypes.FENCE && $$1.type == BlockPathTypes.FENCE && (double)this.mob.getBbWidth() < 0.5;
//                cir.setReturnValue($$3.costMalus >= 0.0F && (check1 || $$2.costMalus >= 0.0F || $$4) && (check2 || $$1.costMalus >= 0.0F || $$4));
//            } else {
//                cir.setReturnValue(false);
//            }
//
//        }
//    }
//
//
//    @Inject(
//            method = "getFloorLevel(Lnet/minecraft/core/BlockPos;)D",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$getFloorLevel(BlockPos $$0, CallbackInfoReturnable<Double> cir){
//        if (this.mob != null) {
//            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//            if (dir == Direction.DOWN)
//                return;
//
//            double coordval = (double)$$0.getY() + 0.5;
//            if (dir == Direction.UP)
//                coordval = (double)$$0.getY() - 0.5;
//            if (dir == Direction.NORTH)
//                coordval = (double)$$0.getZ() + 0.5;
//            if (dir == Direction.SOUTH)
//                coordval = (double)$$0.getZ() - 0.5;
//            if (dir == Direction.WEST)
//                coordval = (double)$$0.getX() + 0.5;
//            if (dir == Direction.EAST)
//                coordval = (double)$$0.getX() - 0.5;
//            cir.setReturnValue((this.canFloat() || this.isAmphibious()) && this.level.getFluidState($$0).is(FluidTags.WATER)
//                    ? coordval
//                    : rdbt$getFloorLevelX(dir,this.level, $$0));
//        }
//    }
//
//    @Unique
//    private static double rdbt$getFloorLevelX(Direction dir, BlockGetter $$0, BlockPos $$1) {
//        BlockPos $$2 = $$1.relative(dir);
//        VoxelShape $$3 = $$0.getBlockState($$2).getCollisionShape($$0, $$2);
//        if (dir == Direction.UP){
//            return (double)$$2.getY() - ($$3.isEmpty() ? 0.0 : $$3.max(Direction.Axis.Y));
//        }
//        if (dir == Direction.NORTH){
//            return (double)$$2.getZ() + ($$3.isEmpty() ? 0.0 : $$3.max(Direction.Axis.Z));
//        }
//        if (dir == Direction.SOUTH){
//            return (double)$$2.getZ() - ($$3.isEmpty() ? 0.0 : $$3.max(Direction.Axis.Z));
//        }
//        if (dir == Direction.WEST){
//            return (double)$$2.getX() + ($$3.isEmpty() ? 0.0 : $$3.max(Direction.Axis.X));
//        }
//        if (dir == Direction.EAST){
//            return (double)$$2.getX() - ($$3.isEmpty() ? 0.0 : $$3.max(Direction.Axis.X));
//        }
//        return 0;
//    }
//
//
//    @Inject(
//            method = "findAcceptedNode(IIIIDLnet/minecraft/core/Direction;Lnet/minecraft/world/level/pathfinder/BlockPathTypes;)Lnet/minecraft/world/level/pathfinder/Node;",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$findAcceptedNode(int $$0, int $$1, int $$2, int $$3, double $$4, Direction $$5, BlockPathTypes $$6, CallbackInfoReturnable<Node> cir){
//        Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//        if (dir == Direction.DOWN)
//            return;
//
//        Node $$7 = null;
//        BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos();
//        double $$9 = this.getFloorLevel($$8.set($$0, $$1, $$2));
//        if ($$9 - $$4 > this.getMobJumpHeight()) {
//            cir.setReturnValue(null);
//            return;
//        } else {
//            BlockPathTypes $$10 = this.getCachedBlockType(this.mob, $$0, $$1, $$2);
//            float $$11 = this.mob.getPathfindingMalus($$10);
//            double $$12 = (double)this.mob.getBbWidth() / 2.0;
//            if ($$11 >= 0.0F) {
//                $$7 = this.getNodeAndUpdateCostToMax($$0, $$1, $$2, $$10, $$11);
//            }
//
//            if (doesBlockHavePartialCollision($$6) && $$7 != null && $$7.costMalus >= 0.0F && !this.canReachWithoutCollision($$7)) {
//                $$7 = null;
//            }
//
//            if ($$10 != BlockPathTypes.WALKABLE && (!this.isAmphibious() || $$10 != BlockPathTypes.WATER)) {
//                if (($$7 == null || $$7.costMalus < 0.0F)
//                        && $$3 > 0
//                        && ($$10 != BlockPathTypes.FENCE || this.canWalkOverFences())
//                        && $$10 != BlockPathTypes.UNPASSABLE_RAIL
//                        && $$10 != BlockPathTypes.TRAPDOOR
//                        && $$10 != BlockPathTypes.POWDER_SNOW) {
//                    $$7 = this.findAcceptedNode($$0, $$1 + 1, $$2, $$3 - 1, $$4, $$5, $$6);
//                    if ($$7 != null && ($$7.type == BlockPathTypes.OPEN || $$7.type == BlockPathTypes.WALKABLE) && this.mob.getBbWidth() < 1.0F) {
//                        double stpX = (double)($$0 - $$5.getStepX()) + 0.5;
//                        double stpZ = (double)($$2 - $$5.getStepZ()) + 0.5;
//                        AABB $$15= new AABB(
//                                stpX - $$12,
//                                this.getFloorLevel($$8.set(stpX, (double)($$1 + 1), stpZ)) + 0.001,
//                                stpZ - $$12,
//                                stpX + $$12,
//                                (double)this.mob.getBbHeight() + this.getFloorLevel($$8.set((double)$$7.x, (double)$$7.y, (double)$$7.z)) - 0.002,
//                                stpZ + $$12);
//                        $$15 = RotationUtil.boxPlayerToWorld($$15,dir);
//
//                        if (this.hasCollisions($$15)) {
//                            $$7 = null;
//                        }
//                    }
//                }
//
//                if (!this.isAmphibious() && $$10 == BlockPathTypes.WATER && !this.canFloat()) {
//                    if (this.getCachedBlockType(this.mob, $$0, $$1 - 1, $$2) != BlockPathTypes.WATER) {
//                        cir.setReturnValue($$7);
//                        return;
//                    }
//
//                    while ($$1 > this.mob.level().getMinBuildHeight()) {
//                        $$10 = this.getCachedBlockType(this.mob, $$0, --$$1, $$2);
//                        if ($$10 != BlockPathTypes.WATER) {
//                            cir.setReturnValue($$7);
//                            return;
//                        }
//
//                        $$7 = this.getNodeAndUpdateCostToMax($$0, $$1, $$2, $$10, this.mob.getPathfindingMalus($$10));
//                    }
//                }
//
//                if ($$10 == BlockPathTypes.OPEN) {
//                    int $$16 = 0;
//                    int $$17 = $$1;
//                    int cap = 300;
//
//                    while ($$10 == BlockPathTypes.OPEN) {
//                        if (dir == Direction.UP){
//                            ++$$1;
//                            if ($$1 > this.mob.level().getMaxBuildHeight()) {
//                                cir.setReturnValue(this.getBlockedNode($$0, $$17, $$2));
//                                return;
//                            }
//                        } if (dir == Direction.NORTH){
//                            --$$2;
//                            cap--;
//                        } if (dir == Direction.SOUTH){
//                            ++$$2;
//                            cap--;
//                        } if (dir == Direction.WEST){
//                            --$$0;
//                            cap--;
//                        } if (dir == Direction.EAST){
//                            ++$$0;
//                            cap--;
//                        }
//
//                        if (cap < 0){
//                            cir.setReturnValue(this.getBlockedNode($$0, $$17, $$2));
//                            return;
//                        }
//
//                        if ($$16++ >= this.mob.getMaxFallDistance()) {
//                            cir.setReturnValue(this.getBlockedNode($$0, $$1, $$2));
//                            return;
//                        }
//
//                        $$10 = this.getCachedBlockType(this.mob, $$0, $$1, $$2);
//                        $$11 = this.mob.getPathfindingMalus($$10);
//                        if ($$10 != BlockPathTypes.OPEN && $$11 >= 0.0F) {
//                            $$7 = this.getNodeAndUpdateCostToMax($$0, $$1, $$2, $$10, $$11);
//                            break;
//                        }
//
//                        if ($$11 < 0.0F) {
//                            cir.setReturnValue(this.getBlockedNode($$0, $$1, $$2));
//                            return;
//                        }
//                    }
//                }
//
//                if (doesBlockHavePartialCollision($$10) && $$7 == null) {
//                    $$7 = this.getNode($$0, $$1, $$2);
//                    $$7.closed = true;
//                    $$7.type = $$10;
//                    $$7.costMalus = $$10.getMalus();
//                }
//
//                cir.setReturnValue($$7);
//                return;
//            } else {
//                cir.setReturnValue($$7);
//                return;
//            }
//        }
//    }
//
//    @Inject(
//            method = "getBlockPathType(Lnet/minecraft/world/level/BlockGetter;III)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void rdbt$getBlockPathType(BlockGetter $$0, int $$1, int $$2, int $$3, CallbackInfoReturnable<BlockPathTypes> cir) {
//        Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
//        if (dir == Direction.DOWN)
//            return;
//
//        cir.setReturnValue(rdbt$getBlockPathTypeStatic(dir, $$0, new BlockPos.MutableBlockPos($$1, $$2, $$3)));
//
//    }
//
//    @Unique
//    private static BlockPathTypes rdbt$getBlockPathTypeStatic(Direction dir, BlockGetter $$0, BlockPos.MutableBlockPos $$1) {
//        int $$2 = $$1.getX();
//        int $$3 = $$1.getY();
//        int $$4 = $$1.getZ();
//        BlockPathTypes $$5 = getBlockPathTypeRaw($$0, $$1);
//        if ($$5 == BlockPathTypes.OPEN) {
//            BlockPathTypes $$6 = null;
//
//            if (dir == Direction.UP){
//                getBlockPathTypeRaw($$0, $$1.set($$2, $$3 + 1, $$4));
//            } if (dir == Direction.NORTH){
//                getBlockPathTypeRaw($$0, $$1.set($$2, $$3, $$4 - 1));
//            } if (dir == Direction.SOUTH){
//                getBlockPathTypeRaw($$0, $$1.set($$2, $$3, $$4 + 1));
//            } if (dir == Direction.WEST){
//                getBlockPathTypeRaw($$0, $$1.set($$2 - 1, $$3, $$4));
//            } if (dir == Direction.EAST){
//                getBlockPathTypeRaw($$0, $$1.set($$2 + 1, $$3, $$4));
//            }
//            $$5 = $$6 != BlockPathTypes.WALKABLE && $$6 != BlockPathTypes.OPEN && $$6 != BlockPathTypes.WATER && $$6 != BlockPathTypes.LAVA
//                    ? BlockPathTypes.WALKABLE
//                    : BlockPathTypes.OPEN;
//            if ($$6 == BlockPathTypes.DAMAGE_FIRE) {
//                $$5 = BlockPathTypes.DAMAGE_FIRE;
//            }
//
//            if ($$6 == BlockPathTypes.DAMAGE_OTHER) {
//                $$5 = BlockPathTypes.DAMAGE_OTHER;
//            }
//
//            if ($$6 == BlockPathTypes.STICKY_HONEY) {
//                $$5 = BlockPathTypes.STICKY_HONEY;
//            }
//
//            if ($$6 == BlockPathTypes.POWDER_SNOW) {
//                $$5 = BlockPathTypes.DANGER_POWDER_SNOW;
//            }
//
//            if ($$6 == BlockPathTypes.DAMAGE_CAUTIOUS) {
//                $$5 = BlockPathTypes.DAMAGE_CAUTIOUS;
//            }
//        }
//
//        if ($$5 == BlockPathTypes.WALKABLE) {
//            $$5 = checkNeighbourBlocks($$0, $$1.set($$2, $$3, $$4), $$5);
//        }
//
//        return $$5;
//    }

}
