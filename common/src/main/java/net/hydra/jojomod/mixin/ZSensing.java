package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.Sensing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sensing.class)
public class ZSensing {
    @Inject(method = "hasLineOfSight", at = @At("HEAD"), cancellable = true)
    private void roundabout$hasLineOfSight(Entity entity, CallbackInfoReturnable<Boolean> cir)
    {
        if (entity instanceof LivingEntity livingEntity)
        {
            if (((StandUser)livingEntity).roundabout$isParallelRunning())
            {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
