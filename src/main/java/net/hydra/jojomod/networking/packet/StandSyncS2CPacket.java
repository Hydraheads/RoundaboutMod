package net.hydra.jojomod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.access.IStandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

public class StandSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((IEntityDataSaver) client.player).getPersistentData().copyFrom(buf.readNbt());
            ((IEntityDataSaver) client.player).syncPersistentData();
        }
    }
    public static void riding(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        RoundaboutMod.LOGGER.info("step 1");
        if (client.player != null && client.world != null) {
            RoundaboutMod.LOGGER.info("step 2");
            Entity Stando = client.world.getEntityById(buf.readInt());
            if (Stando != null && Stando.isAlive()){
                RoundaboutMod.LOGGER.info("Packet Wins");
                ((IStandUser) client.player).startStandRiding(Stando, true);
            }
        }
    }
}
