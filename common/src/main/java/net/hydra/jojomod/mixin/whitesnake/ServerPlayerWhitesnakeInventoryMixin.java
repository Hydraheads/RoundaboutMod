package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.access.WhitesnakeInventoryAccess;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerWhitesnakeInventoryMixin {
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void roundaboutWhitesnake$copyInventory(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        WhitesnakeInventoryAccess oldAccess = (WhitesnakeInventoryAccess) oldPlayer;
        ((WhitesnakeInventoryAccess) this).roundaboutWhitesnake$setInventory(oldAccess.roundaboutWhitesnake$getInventory());
    }
}
