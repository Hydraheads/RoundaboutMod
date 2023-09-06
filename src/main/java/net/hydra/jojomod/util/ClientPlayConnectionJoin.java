package net.hydra.jojomod.util;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.networking.ModMessages;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class ClientPlayConnectionJoin implements ClientPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        PacketByteBuf buffer = PacketByteBufs.create();
        assert client.player != null;
        boolean npbool = ((IEntityDataSaver) client.player).getPersistentData().getBoolean("active_stand");
        buffer.writeBoolean(npbool);
        ClientPlayNetworking.send(ModMessages.STAND_SYNC_ID, buffer);
        //boolean npbool = ((IEntityDataSaver) client.player).getPersistentData().getBoolean("active_stand");
        //StandData.syncStandActive(npbool,client.player);
    }
}