package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class FateEntityMixin {

    /**vampires cannot drown*/
    @Inject(method = "isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void rdbt$isInvulnerableTo(DamageSource $$0, CallbackInfoReturnable<Boolean> cir) {
        if (FateTypes.hasBloodHunger((LivingEntity) (Object) this)){
            if ($$0.is(DamageTypes.DROWN) || $$0.is(DamageTypes.FREEZE) || $$0.is(DamageTypes.STARVE))
                cir.setReturnValue(true);
        }
    }
}
