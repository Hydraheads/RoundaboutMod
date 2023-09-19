package net.hydra.jojomod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.hydra.jojomod.client.ModEntityRendererClient;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.event.KeyInputHandler;
import net.hydra.jojomod.networking.ModMessages;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class RoundaboutModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        ModMessages.registerS2CPackets();

        //HudRenderCallback.EVENT.register(new StandHudRender());
        //ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionJoin());
        ModEntityRendererClient.register();
    }
}
