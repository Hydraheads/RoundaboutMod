package net.hydra.jojomod.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Inject(method = "tick", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/EntityHitResult;getEntity()Lnet/minecraft/world/entity/Entity;"))
    private void roundabout$tickGetEntity(CallbackInfo ci)
    {

    }

    @Inject(method = "getPierceLevel", at= @At(value = "HEAD"), cancellable = true)
    private void roundabout$tickPierceLevel(CallbackInfoReturnable<Byte> cir)
    {

    }
}
