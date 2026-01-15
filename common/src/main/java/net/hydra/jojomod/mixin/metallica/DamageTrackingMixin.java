package net.hydra.jojomod.mixin.metallica;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class DamageTrackingMixin implements IEntityAndData {

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    private void roundabout$trackSignificantDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        boolean isDoT = source.is(DamageTypes.ON_FIRE) ||
                source.is(DamageTypes.IN_FIRE) ||
                source.is(DamageTypes.LAVA) ||
                source.is(DamageTypes.HOT_FLOOR) ||
                source.is(DamageTypes.CACTUS) ||
                source.is(DamageTypes.MAGIC) ||
                source.is(DamageTypes.WITHER) ||
                source.is(DamageTypes.DROWN) ||
                source.is(DamageTypes.STARVE) ||
                source.is(DamageTypes.FALL) ||
                source.is(DamageTypes.CRAMMING);


        boolean isTinyDamage = amount < 1.0f && isDoT;

        if (!isDoT && !isTinyDamage) {
            this.roundabout$setLastDamageTaken(amount);
        }
    }
}