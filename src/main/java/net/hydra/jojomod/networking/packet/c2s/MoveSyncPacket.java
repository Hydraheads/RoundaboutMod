package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class MoveSyncPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerWorld world = (ServerWorld) player.getWorld();
        //public MoveSyncPacket() {
        //}
        int forward = 0;
        int strafe = 0;
        if (buf.readBoolean()) forward++;
        if (buf.readBoolean()) forward--;
        if (buf.readBoolean()) strafe++;
        if (buf.readBoolean()) strafe--;

        int finalForward = forward;
        int finalStrafe = strafe;
        server.execute(() -> {
            ((IStandUser) player).setDI(finalForward, finalStrafe);
        });


    }
}
