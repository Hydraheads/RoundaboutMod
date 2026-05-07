package net.hydra.jojomod.mixin.networking;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.gui.FogInventoryScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.zetalasis.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.zetalasis.networking.packet.api.args.s2c.PacketArgsS2C;
import net.zetalasis.networking.packet.impl.ModNetworking;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ZClientGamePacketListener {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void stopErrorLogging(ClientboundCustomPayloadPacket packet, CallbackInfo ci)
    {
        if (packet.getIdentifier().equals(Roundabout.location("messages2c")))
        {
            AbstractBaseS2CPacket p = ModNetworking.getS2C(packet.getIdentifier());

            if (p != null)
            {
                PacketArgsS2C args = new PacketArgsS2C(Minecraft.getInstance(), ((ClientPacketListener)(Object)this), ModNetworking.decodeBufferToVArgs(packet.getData()));
                p.handle(args, packet.getData());
            }

            ci.cancel();
        }
    }
}