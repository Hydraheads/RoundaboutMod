package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class UtilC2S {

    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/
    public static void UpdateByte(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                  FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        byte data = buf.readByte();
        byte context = buf.readByte();

        server.execute(() -> {
            MainUtil.handleBytePacketC2S(player, data, context);
        });


    }

    /**A generalized packet for sending floats to the server. Context is what to do with the data byte*/
    public static void UpdateFloat(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                  FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        float data = buf.readFloat();
        byte context = buf.readByte();

        server.execute(() -> {
            MainUtil.handleFloatPacketC2S(player, data, context);
        });


    }
}
