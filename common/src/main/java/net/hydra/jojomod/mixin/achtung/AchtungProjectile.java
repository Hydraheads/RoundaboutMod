package net.hydra.jojomod.mixin.achtung;


import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Projectile.class)
public class AchtungProjectile {

    /**Turn shot projectiles invisible.*/
    @Inject(method = "setOwner", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$SetOwner(@Nullable Entity $$0, CallbackInfo ci) {
        if ($$0 != null) {
            if (MainUtil.getEntityIsTrulyInvisible($$0) && ClientNetworking.getAppropriateConfig().achtungSettings.hidesShotProjectiles){
                ((IEntityAndData)this).roundabout$setTrueInvisibility(MainUtil.getEntityTrulyInvisibleTicks($$0));
            }
        }
    }
}
