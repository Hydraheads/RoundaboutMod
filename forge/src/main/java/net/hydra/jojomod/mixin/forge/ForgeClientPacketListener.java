package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.client.ClientNetworking;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ForgeClientPacketListener {
    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void handleServerPlayReady(ClientboundLoginPacket packet, CallbackInfo ci) {
        ClientNetworking.sendHandshake();
    }
}
