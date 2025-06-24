package net.hydra.jojomod.mixin.networking;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.zetalasis.networking.packet.api.IClientNetworking;
import net.zetalasis.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.zetalasis.networking.packet.api.args.c2s.PacketArgsC2S;
import net.zetalasis.networking.packet.impl.ModNetworking;
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

// C2S Networking
@Mixin(ServerGamePacketListenerImpl.class)
public class ZServerGamePacketListenerImpl implements IClientNetworking {
    @Shadow @Final private Connection connection;
    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    private void roundabout$handlePlayerAction(ServerboundPlayerActionPacket $$0, CallbackInfo ci) {
        ServerboundPlayerActionPacket.Action serverboundplayeractionpacket$action = $$0.getAction();
        switch (serverboundplayeractionpacket$action) {
            case SWAP_ITEM_WITH_OFFHAND:
                if (((StandUser)this.player).roundabout$getEffectiveCombatMode()){
                    ci.cancel();
                }
        }
    }
    @Inject(method = "handleCustomPayload", at = @At("HEAD"))
    private void roundabout$handlePayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci)
    {
        if (!packet.getIdentifier().getNamespace().equals("roundabout"))
            return;

        AbstractBaseC2SPacket p = ModNetworking.getC2S(packet.getIdentifier());

        if (p != null)
        {
            ServerGamePacketListenerImpl handler = (ServerGamePacketListenerImpl) (Object) this;
            ServerPlayer sender = handler.player;
            MinecraftServer server = sender.server;

            PacketArgsC2S args = new PacketArgsC2S(server, sender, handler, ModNetworking.decodeBufferToVArgs(packet.getData()));
            p.handle(args, packet.getData());
        }
    }

    @Override
    public @Nullable Connection roundabout$getServer() {
        return this.connection;
    }
}
