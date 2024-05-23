package net.hydra.jojomod.client;

import net.fabricmc.api.ClientModInitializer;
import net.hydra.jojomod.networking.FabricPackets;
import net.hydra.jojomod.particles.ModParticlesClient;

public class RoundaboutFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        FabricPackets.registerS2CPackets();
        ModParticlesClient.registerClientParticles();
    }
}
