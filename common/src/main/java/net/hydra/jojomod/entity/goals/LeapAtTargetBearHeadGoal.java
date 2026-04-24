package net.hydra.jojomod.entity.goals;

import java.util.EnumSet;

import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.Vec3;

public class LeapAtTargetBearHeadGoal extends Goal {
    private final Mob mob;
    private LivingEntity target;
    private final float yd;

    public LeapAtTargetBearHeadGoal(Mob $$0, float $$1) {
        this.mob = $$0;
        this.yd = $$1;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        } else {
            this.target = this.mob.getTarget();
            if (this.target == null || (mob instanceof BaseMinion bm &&
                    (!(bm.getHeadItem() != null && bm.getHeadItem().is(ModItems.POLAR_BEAR_REMAINS)) ||
                            !bm.canGetMadAt(this.target)))) {
                return false;
            } else {
                double $$0 = this.mob.distanceToSqr(this.target);
                if (!($$0 < (double)4.0F) && !($$0 > (double)16.0F)) {
                    if (!this.mob.onGround()) {
                        return false;
                    } else {
                        return this.mob.getRandom().nextInt(reducedTickDelay(5)) == 0;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.mob.onGround();
    }

    public void start() {
        Vec3 $$0 = this.mob.getDeltaMovement();
        Vec3 $$1 = new Vec3(this.target.getX() - this.mob.getX(), (double)0.0F, this.target.getZ() - this.mob.getZ());
        if ($$1.lengthSqr() > 1.0E-7) {
            $$1 = $$1.normalize().scale(0.4).add($$0.scale(0.2));
        }

        this.mob.setDeltaMovement($$1.x, (double)this.yd, $$1.z);
    }
}
