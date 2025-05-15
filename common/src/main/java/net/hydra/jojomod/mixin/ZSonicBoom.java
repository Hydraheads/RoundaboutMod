package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.ILevelAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SonicBoom.class)
public class ZSonicBoom {

    @Inject(method = "start(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/warden/Warden;J)V", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$start(ServerLevel $$0, Warden $$1, long $$2, CallbackInfo ci) {
        if ($$1 != null) {
            //if (((ILevelAccess) $$1.level()).roundabout$isSoundPlunderedEntity())
        }
    }
}
