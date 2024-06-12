package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class MoveSyncPacket {
    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                              FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        //public MoveSyncPacket() {
        //}
        byte forward = buf.readByte();
        byte strafe = buf.readByte();

        server.execute(() -> {
            ((StandUser) player).setDI(forward, strafe);
        });


    }

    public static void updateTSJump(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                               FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        //public MoveSyncPacket() {
        //}
        boolean TSJump = buf.readBoolean();

        server.execute(() -> {
            ((StandUser) player).roundaboutSetTSJump(TSJump);
        });


    }
}
