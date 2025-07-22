package net.hydra.jojomod.mixin.star_platinum;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public class StarPlatinumTridentEntity {

    /**Star Platinum's super item throw ability is canceled on hit, otherwise the returning trident...
     * will be a little rebellious until super throw wears off and keep careening forward*/

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHitEntity(EntityHitResult $$0, CallbackInfo ci) {
        ((IAbstractArrowAccess)this).roundabout$cancelSuperThrow();
    }
}
