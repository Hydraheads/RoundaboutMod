package net.hydra.jojomod.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public abstract class Networking {
    private static MinecraftServer server;

    public static void init() {
    }

    private static void sendConfigToAllOtherPlayers(MinecraftServer server, ServerPlayer player) {
    }

    private static void sendConfigToPlayer(ServerPlayer player) {
    }

    public static void setServer(MinecraftServer server) {
        Networking.server = server;
    }

    public static boolean isDedicated() {
        if (server == null) return false;
        return server.isDedicatedServer();
    }
}
