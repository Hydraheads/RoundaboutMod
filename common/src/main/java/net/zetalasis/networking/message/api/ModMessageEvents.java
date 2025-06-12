package net.zetalasis.networking.message.api;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.ClientToServerPackets;
import net.minecraft.server.MinecraftServer;
import net.zetalasis.networking.message.impl.IMessageEvent;
import net.hydra.jojomod.util.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.zetalasis.networking.packet.impl.ModNetworking;
import net.zetalasis.networking.packet.impl.packet.MessageC2S;
import net.zetalasis.networking.packet.impl.packet.MessageS2C;
import net.zetalasis.world.DynamicWorld;

import java.util.HashSet;

/** Message Events API for Roundabout */
public class ModMessageEvents {
    public static HashSet<IMessageEvent> REGISTRAR = new HashSet<>();

    public static void bootstrap()
    {
        register(new DynamicWorld.DynamicWorldNetMessages());
        register(new ClientToServerPackets.StandPowerPackets());
    }

    private static <T extends IMessageEvent> void register(T event)
    {
        if (REGISTRAR.contains(event)) {
            Roundabout.LOGGER.warn("Attempted to register a duplicate IMessageEvent!");
            return;
        }

        REGISTRAR.add(event);
    }

    static {
        bootstrap();
    }

    /** C2S */
    public static void sendToServer(String name, Object... vargs)
    {
        Object[] args = new Object[vargs.length + 1];
        args[0] = name;
        System.arraycopy(vargs, 0, args, 1, vargs.length);

        ModNetworking.send(new MessageC2S(), args);
    }

    /** S2C send to all players */
    public static void sendToAll(String name, Object... vargs)
    {
        MinecraftServer server = Networking.getServer();
        if (server == null)
            return;

        Object[] args = new Object[vargs.length + 1];
        args[0] = name;
        System.arraycopy(vargs, 0, args, 1, vargs.length);

        for (ServerPlayer player : server.getPlayerList().getPlayers())
        {
            ModNetworking.send(new MessageS2C(), player, args);
        }
    }

    public static void sendToPlayer(ServerPlayer player, String name, Object... vargs)
    {
        Object[] args = new Object[vargs.length + 1];
        args[0] = name;
        System.arraycopy(vargs, 0, args, 1, vargs.length);

        ModNetworking.send(new MessageS2C(), player, args);
    }
}
