package net.hydra.jojomod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.hydra.jojomod.client.StandHUDRender;
import net.hydra.jojomod.event.KeyInputHandler;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.util.ClientPlayConnectionJoin;

public class RoundaboutModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        ModMessages.registerS2CPackets();

        HudRenderCallback.EVENT.register(new StandHUDRender());
        ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionJoin());
    }
}
