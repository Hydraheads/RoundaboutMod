package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class BlackSabbathRangedAttackGoal<T extends BlackSabbathEntity & RangedAttackMob> extends Goal {
    private final T mob;
    private final double speedModifier;
    private final float attackRadiusSqr;
    private int seeTime;
    private int seeTime2;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public BlackSabbathRangedAttackGoal(T $$0, double $$1, int $$2, float $$3) {
        this.mob = $$0;
        this.speedModifier = $$1;
        this.attackRadiusSqr = $$3 * $$3;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.targetSabbath() == null ? false : mob.getThrowable() && ((mob.getUser() != null && ((StandUser)mob.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs && pbs.moveMode == 3)) && !mob.isBlackSabbathUnderLight() && mob.targetSabbath() != null && MainUtil.cheapDistanceTo2(mob.getX(), mob.getZ(), mob.targetSabbath().getX(), mob.targetSabbath().getZ()) < 7.5;
    }

    @Override
    public boolean canContinueToUse() {
        return mob.targetSabbath() == null  ? false : !mob.isBlackSabbathUnderLight() && (this.canUse() || mob.getThrowable() && (MainUtil.cheapDistanceTo2(mob.getX(), mob.getZ(), mob.targetSabbath().getX(), mob.targetSabbath().getZ()) < 12.5 && mob.seeTime > 0) && !mob.getHeldItemSabbath().is(ItemStack.EMPTY.getItem()));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        super.start();
        this.mob.setStrafing(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setStrafing(false);
        this.seeTime = 0;
        this.seeTime2 = 0;
        mob.seeTime = 0;
    }

    @Override
    public void tick() {
        LivingEntity $$0 = this.mob.targetSabbath();
        if ($$0 != null) {
            double $$1 = this.mob.distanceToSqr($$0.getX(), $$0.getY(), $$0.getZ());
            boolean $$2 = this.mob.getSensing().hasLineOfSight($$0);
            boolean $$3 = this.seeTime > 0;
            if ($$2 != $$3) {
                this.seeTime = 0;
            }
            if (mob.hasLineOfSight($$0)) {
                this.seeTime++;
                    if (mob.seeTime != 60) {
                        mob.seeTime = 60;
                    }
            } else {
                this.seeTime--;
                if(mob.seeTime >= -1) {
                    mob.seeTime--;
                }
            }

            if (!($$1 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                this.strafingTime++;
            } else {
                this.mob.getNavigation().moveTo($$0, this.speedModifier);
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
                    if ($$1 > (double) (this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if ($$1 < (double) (this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    if (this.mob.getControlledVehicle() instanceof Mob $$4) {
                        $$4.lookAt($$0, 30.0F, 30.0F);
                    }
                    this.mob.lookAt($$0, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt($$0, 30.0F, 30.0F);
            }
        }
    }

}
