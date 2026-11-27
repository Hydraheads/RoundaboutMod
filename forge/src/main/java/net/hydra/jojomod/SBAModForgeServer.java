package net.hydra.jojomod;

import net.minecraftforge.event.server.ServerStartedEvent;

public class SBAModForgeServer {

    public static void entityLifeCycle(ServerStartedEvent event) {
        SBALoadServer.onServerStarted(event.getServer());
    }
}
