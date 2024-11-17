package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class ConfigSyncPacket {

    public static void readConfig(Minecraft client, ClientPacketListener handler,
                                             FriendlyByteBuf buf, PacketSender responseSender) {

        ClientNetworking.initialize(buf.readBoolean(),buf.readUtf());
    }
}
