package net.hydra.jojomod.mixin.soft_and_wet;


import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetGoal.class)
public class SoftAndWetTargetGoal {

    /**Soft and Wet Plunder sight*/
    @Inject(method = "getFollowDistance", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$getFollowDistance(CallbackInfoReturnable<Double> cir) {
        if (((IMob)this.mob).sba$getConfusionTicks() > 0){
            cir.setReturnValue((double) 0);
            return;
        }
        if (((StandUser)this.mob).sba$getEyeSightTaken() != null && mob.getLastHurtByMob() == null){
            cir.setReturnValue((this.mob.getAttributeValue(Attributes.FOLLOW_RANGE)*0.07));
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    @Final
    protected Mob mob;
}
