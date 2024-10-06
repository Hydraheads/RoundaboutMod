package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedCrossbowAttackGoal.class)
public class ZCrossbowAtk<T extends Monster & CrossbowAttackMob> {
    /**Minor code preventing crossbow charging on barrage*/
    @Final
    @Shadow
    private T mob;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(CallbackInfo ci) {
        if (((StandUser)this.mob).roundabout$isDazed() ||
                (!((StandUser)this.mob).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this.mob).roundabout$getStandPowers().disableMobAiAttack())){
            ci.cancel();
        }
    }
}
