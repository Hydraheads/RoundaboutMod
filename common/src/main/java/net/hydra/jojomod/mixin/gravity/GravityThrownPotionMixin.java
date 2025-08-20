package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrownPotion.class)
public class GravityThrownPotionMixin {
    @Inject(method = "getGravity", at = @At("HEAD"), cancellable = true)
    private void rdbt$multiplyGravity(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.05F * (float) GravityAPI.getGravityStrength(((Entity) (Object) this)));
    }
}
