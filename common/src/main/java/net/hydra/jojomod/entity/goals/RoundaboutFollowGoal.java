package net.hydra.jojomod.entity.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

public class RoundaboutFollowGoal extends Goal {
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10.0).ignoreLineOfSight();
    protected final PathfinderMob mob;
    private final double speedModifier;
    private double px;
    private double py;
    private double pz;
    private double pRotX;
    private double pRotY;
    @Nullable
    private int calmDown;
    private boolean isRunning;

    public RoundaboutFollowGoal(PathfinderMob $$0, double $$1) {
        this.mob = $$0;
        if (mob instanceof AbstractVillager){
            this.speedModifier = $$1/2;
        } else {
            this.speedModifier = $$1;
        }
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.calmDown > 0) {
            this.calmDown--;
            return false;
        }
        return getHypnotizer() != null;
    }

    public LivingEntity getHypnotizer(){
        return ((IMob)this.mob).roundabout$getHypnotizedBy();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        this.px = this.getHypnotizer().getX();
        this.py = this.getHypnotizer().getY();
        this.pz = this.getHypnotizer().getZ();
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.calmDown = reducedTickDelay(100);
        this.isRunning = false;
    }

    @Override
    public void tick() {
        Roundabout.LOGGER.info("yup");
        this.mob.getLookControl().setLookAt(this.getHypnotizer(), (float)(this.mob.getMaxHeadYRot() + 20), (float)this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.getHypnotizer()) < 6.25) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.getHypnotizer(), this.speedModifier);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}