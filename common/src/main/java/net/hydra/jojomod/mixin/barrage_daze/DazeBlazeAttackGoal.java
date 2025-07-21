package net.hydra.jojomod.mixin.barrage_daze;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Blaze;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/world/entity/monster/Blaze$BlazeAttackGoal")
public abstract class DazeBlazeAttackGoal extends Goal {
    /**This mixin is in relation to barrages disabling mobs from attacking or doing things.
     * The daze that barrages inflict prevent blazes from shooting fire.
     *
     * This mixin also makes it so when the blaze is barraging, it doesn't also use its other attacks.*/

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(CallbackInfo ci) {
        if (((StandUser)blaze).roundabout$isDazed() ||
                (!((StandUser)blaze).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)blaze).roundabout$getStandPowers().disableMobAiAttack())) {
            super.tick();
            ci.cancel();
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Final
    @Shadow
    private Blaze blaze;
}
