package net.hydra.jojomod.mixin;

import net.zetalasis.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.zetalasis.networking.packet.api.args.s2c.PacketArgsS2C;
import net.zetalasis.networking.packet.impl.ModNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// S2C Networking
@Mixin(ClientPacketListener.class)
public class ZClientPacketListener {
    @Inject(method = "handleCustomPayload", at = @At("HEAD"))
    private void roundabout$handlePayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci)
    {
        //see zclientgamepacketlistener instead
    }
}