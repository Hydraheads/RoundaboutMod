package net.hydra.jojomod;

import net.fabricmc.api.ModInitializer;
import net.hydra.jojomod.entity.ModEntityAttributes;
import net.hydra.jojomod.items.ModItemGroups;
import net.hydra.jojomod.networking.FabricPackets;
import net.hydra.jojomod.particles.ModParticles;
import net.hydra.jojomod.util.ModLootTableModifiers;
import net.hydra.jojomod.world.gen.ModWorldGeneration;

public class RoundaboutFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Roundabout.LOGGER.info("Hello Fabric world!");
        ModLootTableModifiers.modifyLootTables();
        FabricPackets.registerC2SPackets();
        ModParticles.registerParticles();
        CommandRegistryFabric.register();
        ModItemGroups.registerItemGroups();
        ModWorldGeneration.generateWorldGen();
        ModEntityAttributes.registerModEntities();
        Roundabout.init();
    }
    
}
