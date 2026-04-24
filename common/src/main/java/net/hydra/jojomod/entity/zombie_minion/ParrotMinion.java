package net.hydra.jojomod.entity.zombie_minion;

import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ParrotMinion extends BaseMinion{
    public ParrotMinion(EntityType<? extends ParrotMinion> $$0, Level $$1) {
        super($$0, $$1);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FallenZombieAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
        this.goalSelector.addGoal(7, new ParrotMinion.ParrotWanderGoal(this, (double)1.0F));
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.27).add(Attributes.MAX_HEALTH, 24)
                .add(Attributes.ATTACK_DAMAGE, 7).add(Attributes.FLYING_SPEED, (double)0.4F).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
    protected PathNavigation createNavigation(Level $$0) {
        FlyingPathNavigation $$1 = new FlyingPathNavigation(this, $$0);
        $$1.setCanOpenDoors(false);
        $$1.setCanFloat(true);
        $$1.setCanPassDoors(true);
        return $$1;
    }

    protected void checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3) {
    }
    public boolean isFlying() {
        return !this.onGround();
    }
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }
    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 $$0 = this.getDeltaMovement();
        if (!this.onGround() && $$0.y < (double)0.0F) {
            this.setDeltaMovement($$0.multiply((double)1.0F, 0.6, (double)1.0F));
        }

        this.flap += this.flapping * 2.0F;
    }

    static class ParrotWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public ParrotWanderGoal(PathfinderMob $$0, double $$1) {
            super($$0, $$1);
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 $$0 = null;
            if (this.mob.isInWater()) {
                $$0 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                $$0 = this.getTreePos();
            }

            return $$0 == null ? super.getPosition() : $$0;
        }

        @Override
        public boolean canUse() {
            if (this.mob instanceof BaseMinion FM && FM.getMovementTactic() == Tactics.ROAM.id) {
                return super.canUse();
            } else {
                return false;
            }
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos $$0 = this.mob.blockPosition();
            BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos $$2 = new BlockPos.MutableBlockPos();

            for(BlockPos $$4 : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - (double)3.0F), Mth.floor(this.mob.getY() - (double)6.0F), Mth.floor(this.mob.getZ() - (double)3.0F), Mth.floor(this.mob.getX() + (double)3.0F), Mth.floor(this.mob.getY() + (double)6.0F), Mth.floor(this.mob.getZ() + (double)3.0F))) {
                if (!$$0.equals($$4)) {
                    BlockState $$5 = this.mob.level().getBlockState($$2.setWithOffset($$4, Direction.DOWN));
                    boolean $$6 = $$5.getBlock() instanceof LeavesBlock || $$5.is(BlockTags.LOGS);
                    if ($$6 && this.mob.level().isEmptyBlock($$4) && this.mob.level().isEmptyBlock($$1.setWithOffset($$4, Direction.UP))) {
                        return Vec3.atBottomCenterOf($$4);
                    }
                }
            }

            return null;
        }
    }
}
