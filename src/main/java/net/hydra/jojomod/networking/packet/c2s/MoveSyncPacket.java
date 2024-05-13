package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.event.powers.StandUser;
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
        byte forward = buf.readByte();
        byte strafe = buf.readByte();

        server.execute(() -> {
            ((StandUser) player).setDI(forward, strafe);
        });


    }
}
