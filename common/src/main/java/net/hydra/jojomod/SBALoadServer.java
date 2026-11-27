package net.hydra.jojomod;

import net.hydra.jojomod.util.Networking;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.server.MinecraftServer;

public class SBALoadServer {

    public static void onServerStarted(MinecraftServer server)
    {
        Networking.setServer(server);
        ConfigManager.loadStandArrowPool();
        //DynamicWorld.loadDynamicWorlds(server);
    }
}
