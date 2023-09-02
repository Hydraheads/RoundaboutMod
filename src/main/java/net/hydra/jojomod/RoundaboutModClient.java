package net.hydra.jojomod;

import net.fabricmc.api.ClientModInitializer;
import net.hydra.jojomod.event.KeyInputHandler;

public class RoundaboutModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }
}
