package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class OldLocalPlayerSprintMixin {
    @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$disableOldSprinting(CallbackInfoReturnable<Boolean> cir) {
        if (((LocalPlayer) (Object) this).hasEffect(ModEffects.OLD)) {
            cir.setReturnValue(false);
        }
    }
}
