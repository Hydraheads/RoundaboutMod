package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class OldSprintMixin {
    @Inject(method = "setSprinting", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$blockOldSprint(boolean sprinting, CallbackInfo ci) {
        if (sprinting && ModEffects.OLD != null
                && (Object) this instanceof LivingEntity living
                && living.hasEffect(ModEffects.OLD)) {
            ci.cancel();
        }
    }
}
