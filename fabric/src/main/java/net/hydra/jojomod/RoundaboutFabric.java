package net.hydra.jojomod;

import net.fabricmc.api.ModInitializer;
import net.hydra.jojomod.networking.FabricPacketManager;
import net.hydra.jojomod.networking.FabricPackets;
import net.hydra.jojomod.particles.ModParticles;
import net.hydra.jojomod.registry.*;
import net.hydra.jojomod.world.gen.ModWorldGeneration;
import net.minecraft.data.DataGenerator;

public class RoundaboutFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Roundabout.LOGGER.info("Hello Fabric world!");
        FabricLootTables.modifyLootTables();

        FabricEntities.register();
        FabricItems.register();
        FabricBlocks.register();
        FabricSounds.register();
        FabricPacketManager.registerC2SPackets();
        ModParticles.registerParticles();
        CommandRegistryFabric.register();
        ModWorldGeneration.generateWorldGen();
        Roundabout.init();
    }
    
}
