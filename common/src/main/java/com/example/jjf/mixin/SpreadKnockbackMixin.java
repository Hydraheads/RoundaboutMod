package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class SpreadKnockbackMixin {

    @Inject(
            method = "knockback",
            at = @At("TAIL")
    )
    private void jjf$amplifySpreadKnockback(
            double strength,
            double x,
            double z,
            CallbackInfo ci
    ) {
        LivingEntity self =
                (LivingEntity) (Object) this;

        ZeroGravitySystem.amplifySpreadKnockback(
                self
        );
    }
}