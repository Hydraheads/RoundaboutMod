package net.hydra.jojomod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.hydra.jojomod.client.StandHUDRender;
import net.hydra.jojomod.event.KeyInputHandler;
import net.hydra.jojomod.networking.ModMessages;

public class RoundaboutModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        ModMessages.registerS2CPackets();

        HudRenderCallback.EVENT.register(new StandHUDRender());
    }
}
