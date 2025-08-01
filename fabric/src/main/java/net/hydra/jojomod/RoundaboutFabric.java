package net.hydra.jojomod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.hydra.jojomod.event.commands.RoundaboutCom;
import net.hydra.jojomod.item.DispenserRegistry;
import net.hydra.jojomod.particles.FabricParticles;
import net.hydra.jojomod.registry.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.world.FabricGamerules;
import net.hydra.jojomod.world.gen.ModWorldGeneration;

public class RoundaboutFabric implements ModInitializer {


    @Override
    public void onInitialize() {
        Roundabout.LOGGER.info("Loading Roundabout (Fabric)");

        ServerLifecycleEvents.SERVER_STARTED.register(RoundaboutLoadServer::onServerStarted);
        ConfigManager.loadConfigs(FabricLoader.getInstance()
                .getConfigDir()
                .resolve(Roundabout.MOD_ID + ".json"),
                //FabricLoader.getInstance()
                //.getConfigDir()
                //.resolve(Roundabout.MOD_ID + "-server.json"),
                FabricLoader.getInstance()
                        .getConfigDir()
                        .resolve(Roundabout.MOD_ID + "-advanced.json"),
                FabricLoader.getInstance()
                        .getConfigDir()
                        .resolve(Roundabout.MOD_ID + "-clientOnly.json"));

        /**
        BlockBlacklist.load(
                FabricLoader.getInstance().getConfigDir(),
                "roundabout-block_blacklist"
        );
         **/

        FabricLootTables.modifyLootTables();

        FabricEffects.register();
        FabricEntities.register();
        FabricBlocks.register();
        FabricItems.register();
        FabricSounds.register();
        FabricParticles.registerParticles();
        FabricGamerules.registerGamerules();
        CommandRegistryFabric.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RoundaboutCom.register(dispatcher);
        });
        ModWorldGeneration.generateWorldGen();
        DispenserRegistry.init();
        Roundabout.init(false);
        ModSounds.registerSoundEvents();
    }

    
}
