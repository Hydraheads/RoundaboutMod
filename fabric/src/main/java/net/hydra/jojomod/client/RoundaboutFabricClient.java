package net.hydra.jojomod.client;

import net.fabricmc.api.ClientModInitializer;
import net.hydra.jojomod.networking.FabricPacketManager;
import net.hydra.jojomod.networking.FabricPackets;
import net.hydra.jojomod.particles.ModParticlesClient;
import net.hydra.jojomod.registry.FabricEntities;
import net.hydra.jojomod.registry.FabricEntityClient;
import net.hydra.jojomod.registry.FabricKeyInputs;

public class RoundaboutFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricKeyInputs.register();
        FabricPacketManager.registerS2CPackets();
        ModParticlesClient.registerClientParticles();
        FabricEntityClient.register();
    }
}
