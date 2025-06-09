package net.hydra.jojomod.networking.message.api;

import com.google.common.graph.Network;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.message.impl.IMessageEvent;
import net.hydra.jojomod.util.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;

/** Message Events API for Roundabout */
public class ModMessageEvents {
    public static HashSet<IMessageEvent> REGISTRAR = new HashSet<>();

    public static void bootstrap()
    {}

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
        for (IMessageEvent event : REGISTRAR)
        {
            // TODO: send packet to server to let it know we have a message for it
        }
    }

    /** S2C send to all players */
    public static void sendToAll(String name, Object... vargs)
    {
        if (Networking.getServer() == null)
            return;

        if (Minecraft.getInstance().level != null)
            return;

        for (ServerPlayer player : Networking.getServer().getPlayerList().getPlayers())
        {
            for (IMessageEvent event : REGISTRAR)
            {
                // TODO: send packet to client to let them know we have a message for them
            }
        }
    }

    public static void sendToPlayer(ServerPlayer player, String name, Object... vargs)
    {

    }
}
