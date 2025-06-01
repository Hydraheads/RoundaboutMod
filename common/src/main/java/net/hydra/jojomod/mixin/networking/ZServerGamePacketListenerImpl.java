package net.hydra.jojomod.mixin.networking;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.packet.api.IClientNetworking;
import net.hydra.jojomod.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.hydra.jojomod.networking.packet.api.args.c2s.PacketArgsC2S;
import net.hydra.jojomod.networking.packet.impl.ModNetworking;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ZServerGamePacketListenerImpl implements IClientNetworking {
    @Shadow @Final private Connection connection;

    @Inject(method = "handleCustomPayload", at = @At("HEAD"))
    private void roundabout$handlePayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci)
    {
        if (!packet.getIdentifier().getNamespace().equals("roundabout"))
            return;

        Roundabout.LOGGER.info("Got packet \"{}\"", packet.getIdentifier());

        AbstractBaseC2SPacket p = ModNetworking.getC2S(packet.getIdentifier());

        if (p != null)
        {
            Roundabout.LOGGER.info("packet not null, handling");

            ServerGamePacketListenerImpl handler = (ServerGamePacketListenerImpl) (Object) this;
            ServerPlayer sender = handler.player;
            MinecraftServer server = sender.server;

            PacketArgsC2S args = new PacketArgsC2S(server, sender, handler, new FriendlyByteBuf(packet.getData()));
            p.handle(args);
        }
    }

    @Override
    public @Nullable Connection roundabout$getServer() {
        return this.connection;
    }
}
