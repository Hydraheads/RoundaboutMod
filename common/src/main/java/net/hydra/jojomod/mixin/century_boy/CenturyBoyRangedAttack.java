package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedAttackGoal.class)
public abstract class CenturyBoyRangedAttack extends Goal {

    @Shadow @Final private Mob mob;


    @Inject(method = "tick", at=@At(value = "HEAD"), cancellable = true)
    public void roundabout$tick(CallbackInfo ci) {
        if (((StandUser)mob).roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB && PCB.invincibleState) {
            ci.cancel();
            return;
        }
    }

}
