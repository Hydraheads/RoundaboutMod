package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MemoryMovementPackets {
    @Inject(method = "handleMovePlayer", at = @At("HEAD"), cancellable = true)
    private void roundabout$blockControlledMovement(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        ServerGamePacketListenerImpl listener = (ServerGamePacketListenerImpl) (Object) this;
        if (!DiscItemData.hasPlayerControl(listener.player)
                && !DiscItemData.isLobotomized(listener.player)) ci.cancel();
    }
}
