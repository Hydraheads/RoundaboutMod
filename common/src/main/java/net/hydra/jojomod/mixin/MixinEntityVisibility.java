package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntityVisibility {

    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    private void roundabout$checkInvis(CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof Player player) {
            if (((StandUser)player).roundabout$getStandPowers() instanceof PowersMetallica pm && pm.isInvisible()) {
                cir.setReturnValue(true);
            }
        }
    }
}