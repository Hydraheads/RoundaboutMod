package net.hydra.jojomod;

import net.hydra.jojomod.advancement.criteria.ModCriteria;
import net.zetalasis.networking.packet.impl.ModNetworking;
import net.hydra.jojomod.platform.Services;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.Networking;
import net.zetalasis.world.DynamicWorld;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Roundabout {

    public static int worldInFog = 0;
    public static final String MOD_ID = "roundabout";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Random RANDOM = new Random();

    public static float gasDamage = 0.83F;


    public static void init() {
        //LOGGER.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        //LOGGER.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded("roundabout")) {
            LOGGER.info("Hello to roundabout");
        }

        ModCriteria.bootstrap();
        ModNetworking.bootstrap();
    }

    public static void onServerStarted(MinecraftServer server)
    {
        Networking.setServer(server);
        ConfigManager.loadStandArrowPool();
        DynamicWorld.loadDynamicWorlds(server);
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}