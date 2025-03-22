package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class RequestDynamicWorldC2S {
    public static void createNewWorld(MinecraftServer server, ServerPlayer player,
                                      ServerGamePacketListenerImpl handler,
                                      FriendlyByteBuf buf, PacketSender responseSender)
    {
        DynamicWorld.generateD4CWorld(server, player);
    }
}
