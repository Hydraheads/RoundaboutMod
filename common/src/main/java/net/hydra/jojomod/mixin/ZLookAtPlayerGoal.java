package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LookAtPlayerGoal.class)
public class ZLookAtPlayerGoal {
    @Shadow @Nullable protected Entity lookAt;

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void roundabout$shouldContinue(CallbackInfoReturnable<Boolean> cir)
    {
        if (this.lookAt == null)
            return;

        if (((StandUser)this.lookAt).roundabout$isParallelRunning())
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
