package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class ZMob {
    /**Minor code, mobs in a barrage should not be attacking*/
    @Inject(method = "doHurtTarget", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutTryAttack(Entity $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser) this).isDazed()) {
            ci.setReturnValue(false);
        }
    }

}
