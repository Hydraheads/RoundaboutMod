package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.entity.corpses.FallenMob;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public class JusticeDamageSourceMixin {

    @Shadow @Final @Nullable private Entity causingEntity;

    @Shadow @Final @Nullable private Entity directEntity;

    @Inject(method = "scalesWithDifficulty()Z", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$scalesWithDifficulty(CallbackInfoReturnable<Boolean> cir) {
        if (this.causingEntity instanceof FallenMob || this.directEntity instanceof FallenMob){
            cir.setReturnValue(false);
        }
    }
}
