package net.hydra.jojomod;

import net.hydra.jojomod.util.Networking;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.server.MinecraftServer;
import net.zetalasis.world.DynamicWorld;

public class RoundaboutLoadServer {

    public static void onServerStarted(MinecraftServer server)
    {
        Networking.setServer(server);
        ConfigManager.loadStandArrowPool();
        ConfigManager.loadBlacklists();
        DynamicWorld.loadDynamicWorlds(server);
    }
}
