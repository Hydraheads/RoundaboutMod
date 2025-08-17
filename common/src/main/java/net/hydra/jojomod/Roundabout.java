package net.hydra.jojomod;

import net.hydra.jojomod.advancement.criteria.ModCriteria;
import net.hydra.jojomod.util.MainUtil;
import net.zetalasis.networking.packet.impl.ModNetworking;
import net.hydra.jojomod.platform.Services;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Roundabout {

    public static int worldInFog = 0;
    public static final String MOD_ID = "roundabout";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Random RANDOM = new Random();
    private static boolean isForge = false;
    public static float gasDamage = 0.83F;

    public static void init(boolean isForge) {
        //LOGGER.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        //LOGGER.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.

        if (Services.PLATFORM.isModLoaded("roundabout")) {
            String client;
            if(MainUtil.isClient()){
                client = " client";
            } else{
                client = " server";
            }
            LOGGER.info("Hello to roundabout on" + client);
        }
        forgeSet(isForge);
        ModCriteria.bootstrap();
        ModNetworking.bootstrap();
    }
    public static void forgeSet(boolean b){
        isForge = b;
    }
    public static boolean getIsForge(){
        return isForge;
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}