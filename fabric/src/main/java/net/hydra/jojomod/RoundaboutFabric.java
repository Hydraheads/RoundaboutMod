package net.hydra.jojomod;

import net.fabricmc.api.ModInitializer;
import net.hydra.jojomod.item.DispenserRegistry;
import net.hydra.jojomod.networking.FabricPacketManager;
import net.hydra.jojomod.particles.FabricParticles;
import net.hydra.jojomod.registry.*;
import net.hydra.jojomod.world.gen.ModWorldGeneration;

public class RoundaboutFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Roundabout.LOGGER.info("Loading Roundabout (Fabric)");



        FabricLootTables.modifyLootTables();

        FabricEffects.register();
        FabricEntities.register();
        FabricBlocks.register();
        FabricItems.register();
        FabricSounds.register();
        FabricPacketManager.registerC2SPackets();
        FabricParticles.registerParticles();
        CommandRegistryFabric.register();
        ModWorldGeneration.generateWorldGen();
        DispenserRegistry.init();
        Roundabout.init();
    }
    
}
