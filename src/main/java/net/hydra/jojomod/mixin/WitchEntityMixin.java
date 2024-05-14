package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WitchEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitchEntity.class)
public class WitchEntityMixin {

    @Shadow
    private static final TrackedData<Boolean> DRINKING = DataTracker.registerData(WitchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "isDrinking", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutIsDrinking(CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser) this).isDazed()) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "setDrinking", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutSetDrinking(CallbackInfo ci) {
        if (((StandUser) this).isDazed()) {
            ((WitchEntity) (Object) this).getDataTracker().set(DRINKING, false);
            ci.cancel();
        }
    }
}
