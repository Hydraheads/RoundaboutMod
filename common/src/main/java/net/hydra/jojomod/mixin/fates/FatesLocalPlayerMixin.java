package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class FatesLocalPlayerMixin {
    /**You cannot spawn sprint particles while transforming*/
    @Inject(method = "canSpawnSprintParticle", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$applyEffectTick(CallbackInfoReturnable<Boolean> cir) {
            if (FateTypes.isTransforming(((LocalPlayer)(Object)this))) {
                cir.setReturnValue(false);
            }
    }
}
