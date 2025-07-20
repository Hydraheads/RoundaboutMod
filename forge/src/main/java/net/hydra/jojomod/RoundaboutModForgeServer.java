package net.hydra.jojomod;

import net.minecraftforge.event.server.ServerStartedEvent;

public class RoundaboutModForgeServer {

    public static void entityLifeCycle(ServerStartedEvent event) {
        RoundaboutLoadServer.onServerStarted(event.getServer());
    }
}
