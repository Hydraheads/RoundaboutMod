package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.ZeroGravityAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @Shadow
    private int aboveGroundTickCount;

    @Inject(method = "tick", at = @At("HEAD"))
    private void jjf$allowZeroGravityWithoutFlightKick(CallbackInfo ci) {
        if (player instanceof ZeroGravityAccess access
                && access.jjf$getZeroGravityState().isZeroGravity()) {
            aboveGroundTickCount = 0;
        }
    }
}
