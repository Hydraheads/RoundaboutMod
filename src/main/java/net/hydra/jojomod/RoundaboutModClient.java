package net.hydra.jojomod;

import net.fabricmc.api.ClientModInitializer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.client.KeyInputHandler;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.particles.ModParticlesClient;

public class RoundaboutModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        ModMessages.registerS2CPackets();
        ModParticlesClient.registerClientParticles();
        ModEntityRendererClient.register();
    }
}
