package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AnubisAttackGoal extends MeleeAttackGoal {
    public AnubisAttackGoal(AbstractIllager $$0, double $$1, boolean $$2) {
        super($$0, $$1, $$2);
    }


    private ItemStack crossbow;
    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
        crossbow = this.mob.getItemInHand(InteractionHand.MAIN_HAND);
        this.mob.setItemInHand(InteractionHand.MAIN_HAND, Items.AIR.getDefaultInstance());
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.mob.setItemInHand(InteractionHand.MAIN_HAND,crossbow);
    }

    @Override
    public boolean canUse() {
        boolean bl = true;
        if (this.mob instanceof Pillager P) {
            bl = !P.isChargingCrossbow();
        }
        return super.canUse() && bl && ((StandUser)this.mob).roundabout$getStandPowers() instanceof PowersAnubis;
    }
}
