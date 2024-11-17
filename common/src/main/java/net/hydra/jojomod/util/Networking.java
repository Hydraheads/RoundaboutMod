package net.hydra.jojomod.util;

import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public abstract class Networking {
    private static MinecraftServer server;
    public static void init() {
    }


    private static void sendConfigToAllOtherPlayers(MinecraftServer server, ServerPlayer player) {
        for (int j = 0; j < server.getPlayerList().getPlayers().size(); ++j) {
            sendConfigToPlayer(server.getPlayerList().getPlayers().get(j));
        }
    }

    public static void sendConfigToPlayer(ServerPlayer player) {
        ModPacketHandler.PACKET_ACCESS.sendConfig(((ServerPlayer) player));
    }

    public static void setServer(MinecraftServer server) {
        Networking.server = server;
    }

    public static boolean isDedicated() {
        if (server == null) return false;
        return server.isDedicatedServer();
    }
}
