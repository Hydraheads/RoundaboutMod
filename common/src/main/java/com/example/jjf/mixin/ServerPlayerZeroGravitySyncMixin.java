package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravityState;
import com.example.jjf.zero_gravity.ZeroGravitySync;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerZeroGravitySyncMixin {

    @Unique
    private boolean jjf$wasSyncingGravity;

    @Inject(method = "tick", at = @At("TAIL"))
    private void jjf$syncGravityWithPlayer(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (player.connection == null || !(player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();
        boolean isActive = state.isZeroGravity() || state.isRestoringWeight();

        if (isActive != jjf$wasSyncingGravity || (isActive && player.tickCount % 2 == 0)) {
            ZeroGravitySync.send(player, state);
        }

        jjf$wasSyncingGravity = isActive;
    }
}
