package net.hydra.jojomod.mixin.barrage;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwellGoal.class)
public abstract class DazeCreeperIgnite extends Goal {
    /**This mixin is in relation to barrages disabling mobs from attacking or doing things.
     * The daze that barrages inflict prevent creepers from swelling up and exploding.
     *
     * This mixin also makes it so when the creeper is using barrages or attacks
     * that it shouldn't explode during, it stops. Complements the DazeCreeper Mixin.*/

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(CallbackInfo ci) {
        if (((StandUser)this.creeper).roundabout$isDazed() ||
                (!((StandUser)this.creeper).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this.creeper).roundabout$getStandPowers().disableMobAiAttack())){
           this.creeper.setSwellDir(-1);
           ci.cancel();
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Final
    @Shadow
    private Creeper creeper;
}
