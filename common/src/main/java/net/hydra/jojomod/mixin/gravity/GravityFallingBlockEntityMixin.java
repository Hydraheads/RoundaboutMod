package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public class GravityFallingBlockEntityMixin {
    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void roundabout$tick(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$universalTick();
        ((IEntityAndData)this).roundabout$tickQVec();
    }
}
