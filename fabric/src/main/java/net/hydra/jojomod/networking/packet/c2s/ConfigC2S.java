package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ConfigC2S {

    /**
     * A generalized packet for sending bytes to the server. Context is what to do with the data byte
     */
    public static void Handshake(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                 FriendlyByteBuf buf, PacketSender responseSender) {
        //Everything here is server only!
        server.execute(() -> {
            MainUtil.handShake(player);
        });

    }
}