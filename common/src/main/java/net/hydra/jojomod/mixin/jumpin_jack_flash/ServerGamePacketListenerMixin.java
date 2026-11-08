package net.hydra.jojomod.mixin.jumpin_jack_flash;

import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityAccess;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @Shadow
    private int aboveGroundTickCount;

    @Shadow
    private int aboveGroundVehicleTickCount;

    @Shadow
    private boolean clientIsFloating;

    @Shadow
    private boolean clientVehicleIsFloating;

    @Unique
    private void jjf$clearVanillaFlightTracking() {
        if (player instanceof ZeroGravityAccess access
                && access.jjf$getZeroGravityState().isZeroGravity()) {
            aboveGroundTickCount = 0;
            aboveGroundVehicleTickCount = 0;
            clientIsFloating = false;
            clientVehicleIsFloating = false;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void jjf$beforeConnectionTick(CallbackInfo ci) {
        jjf$clearVanillaFlightTracking();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void jjf$afterConnectionTick(CallbackInfo ci) {
        jjf$clearVanillaFlightTracking();
    }

    @Inject(method = "handleMovePlayer", at = @At("TAIL"))
    private void jjf$afterPlayerMovement(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        jjf$clearVanillaFlightTracking();
    }

    @Inject(method = "handleMoveVehicle", at = @At("TAIL"))
    private void jjf$afterVehicleMovement(ServerboundMoveVehiclePacket packet, CallbackInfo ci) {
        jjf$clearVanillaFlightTracking();
    }
}
