package net.hydra.jojomod.entity.goals.badCompanyGoals;

import net.hydra.jojomod.entity.badCompanyTroops.BCTroop;
import net.hydra.jojomod.event.index.BadCompanyTactics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class UserHurtTargetGoal extends TargetGoal {
    private final BCTroop troop;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public UserHurtTargetGoal(BCTroop $$0) {
        super($$0, false);
        this.troop = $$0;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (troop.getCurrentTactic() != BadCompanyTactics.WARD.id) {
        //if (troop.getCurrentTactic() == BadCompanyTactics.RETURN.id || troop.getCurrentTactic() == BadCompanyTactics.STOP.id) {
            return false;
        }

        LivingEntity $$0 = this.troop.getUser();
        if ($$0 == null) {
            return false;
        } else {
            this.ownerLastHurt = $$0.getLastHurtMob();
            int $$1 = $$0.getLastHurtMobTimestamp();
            return $$1 != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && this.troop.wantsToAttack(this.ownerLastHurt, $$0);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.troop.wantsToAttack(this.ownerLastHurt, this.troop.getUser()) && troop.getCurrentTactic() == BadCompanyTactics.WARD.id;
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity $$0 = this.troop.getUser();
        if ($$0 != null) {
            this.timestamp = $$0.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
