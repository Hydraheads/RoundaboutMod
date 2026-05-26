package net.hydra.jojomod.mixin.achtung;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public class AchtungMinecart {
    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void roundabout$tick(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$universalTick();
        ((IEntityAndData)this).roundabout$tickQVec();
    }
}
