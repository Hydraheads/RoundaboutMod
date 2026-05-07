package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenSkeleton;
import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;

import java.util.EnumSet;

public class FallenRangedAttackGoal<T extends FallenSkeleton & RangedAttackMob> extends Goal {
        private final T mob;
        private final double speedModifier;
        private int attackIntervalMin;
        private final float attackRadiusSqr;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

    public FallenRangedAttackGoal(T $$0, double $$1, int $$2, float $$3) {
            this.mob = $$0;
            this.speedModifier = $$1;
            this.attackIntervalMin = $$2;
            this.attackRadiusSqr = $$3 * $$3;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public void setMinAttackInterval(int $$0) {
            this.attackIntervalMin = $$0;
        }

        @Override
        public boolean canUse() {
            return this.mob.getTarget() == null ? false : this.isHoldingBow();
        }

        protected boolean isHoldingBow() {
            return this.mob.isHolding(Items.BOW);
        }

        @Override
        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingBow();
        }

        @Override
        public void start() {
            super.start();
            this.mob.setAggressive(true);
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
            this.seeTime = 0;
            this.attackTime = -1;
            this.mob.getMoveControl().strafe(0,0);
            this.mob.stopUsingItem();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity $$0 = this.mob.getTarget();
            if ($$0 != null) {
                double $$1 = this.mob.distanceToSqr($$0.getX(), $$0.getY(), $$0.getZ());
                boolean $$2 = this.mob.getSensing().hasLineOfSight($$0);
                boolean $$3 = this.seeTime > 0;
                if ($$2 != $$3) {
                    this.seeTime = 0;
                }

                if ($$2) {
                    this.seeTime++;
                } else {
                    this.seeTime--;
                }

                if (!($$1 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    if(this.mob.getMovementTactic() != Tactics.HOLD.id) {
                        this.mob.getNavigation().stop();
                    }
                    this.strafingTime++;
                } else {
                    if(this.mob.getMovementTactic() != Tactics.HOLD.id) {
                        this.mob.getNavigation().moveTo($$0, this.speedModifier);
                    }
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if ($$1 > (double)(this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if ($$1 < (double)(this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    if(this.mob.getMovementTactic() != Tactics.HOLD.id) {
                        this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    } else{
                        this.mob.getMoveControl().strafe(0,0);
                    }
                    if (this.mob.getControlledVehicle() instanceof Mob $$4) {
                        $$4.lookAt($$0, 30.0F, 30.0F);
                    }

                    this.mob.lookAt($$0, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt($$0, 30.0F, 30.0F);
                }

                if (this.mob.isUsingItem()) {
                    if (!$$2 && this.seeTime < -60) {
                        this.mob.stopUsingItem();
                    } else if ($$2) {
                        int $$5 = this.mob.getTicksUsingItem();
                        if ($$5 >= 20 && mob.controller !=null && mob.controller.tickCount % ClientNetworking.getAppropriateConfig().justiceSettings.skeletonFireInterval == 0) {
                            this.mob.stopUsingItem();
                            this.mob.performRangedAttack($$0, BowItem.getPowerForTime($$5));
                            this.attackTime = this.attackIntervalMin;
                        }
                    }
                } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                    this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.BOW));
                }
            } else{
                this.mob.getMoveControl().strafe(0,0);
            }
        }
}
