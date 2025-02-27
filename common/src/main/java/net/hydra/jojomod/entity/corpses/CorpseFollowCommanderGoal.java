package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class CorpseFollowCommanderGoal extends Goal {
    public static final int TELEPORT_WHEN_DISTANCE_IS = 12;
    private static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;
    private static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;
    private static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;
    private final FallenMob fallenMob;
    private LivingEntity owner;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;
    private final boolean canFly;

    public CorpseFollowCommanderGoal(FallenMob $$0, double $$1, float $$2, float $$3, boolean $$4) {
        this.fallenMob = $$0;
        this.level = $$0.level();
        this.speedModifier = $$1;
        this.navigation = $$0.getNavigation();
        this.startDistance = $$2;
        this.stopDistance = $$3;
        this.canFly = $$4;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!($$0.getNavigation() instanceof GroundPathNavigation) && !($$0.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity $$0 = null;
        if (this.fallenMob.controller instanceof LivingEntity LE){
            $$0 = LE;
        }
        if ($$0 == null) {
            return false;
        } else if ($$0.isSpectator()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else if (this.fallenMob.distanceToSqr($$0) < (double)(this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = $$0;
            if (this.fallenMob.getMovementTactic() == Tactics.FOLLOW.id){
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return this.unableToMove() ? false : !(this.fallenMob.distanceToSqr(this.owner) <= (double)(this.stopDistance * this.stopDistance));
        }
    }

    private boolean unableToMove() {
        return this.fallenMob.isPassenger() || this.fallenMob.isLeashed();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.fallenMob.getPathfindingMalus(BlockPathTypes.WATER);
        this.fallenMob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.fallenMob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        Roundabout.LOGGER.info("1");
        this.fallenMob.getLookControl().setLookAt(this.owner, 10.0F, (float)this.fallenMob.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            Roundabout.LOGGER.info("2");
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.navigation.moveTo(this.owner, this.speedModifier);
        }
    }

    private void teleportToOwner() {
        BlockPos $$0 = this.owner.blockPosition();

        for (int $$1 = 0; $$1 < 10; $$1++) {
            int $$2 = this.randomIntInclusive(-3, 3);
            int $$3 = this.randomIntInclusive(-1, 1);
            int $$4 = this.randomIntInclusive(-3, 3);
            boolean $$5 = this.maybeTeleportTo($$0.getX() + $$2, $$0.getY() + $$3, $$0.getZ() + $$4);
            if ($$5) {
                return;
            }
        }
    }

    private boolean maybeTeleportTo(int $$0, int $$1, int $$2) {
        if (Math.abs((double)$$0 - this.owner.getX()) < 2.0 && Math.abs((double)$$2 - this.owner.getZ()) < 2.0) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos($$0, $$1, $$2))) {
            return false;
        } else {
            this.fallenMob.moveTo((double)$$0 + 0.5, (double)$$1, (double)$$2 + 0.5, this.fallenMob.getYRot(), this.fallenMob.getXRot());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos $$0) {
        BlockPathTypes $$1 = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, $$0.mutable());
        if ($$1 != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState $$2 = this.level.getBlockState($$0.below());
            if (!this.canFly && $$2.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos $$3 = $$0.subtract(this.fallenMob.blockPosition());
                return this.level.noCollision(this.fallenMob, this.fallenMob.getBoundingBox().move($$3));
            }
        }
    }

    private int randomIntInclusive(int $$0, int $$1) {
        return this.fallenMob.getRandom().nextInt($$1 - $$0 + 1) + $$0;
    }
}
