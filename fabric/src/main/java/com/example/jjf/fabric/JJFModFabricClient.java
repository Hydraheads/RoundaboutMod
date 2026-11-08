package com.example.jjf.fabric;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.JumpinJackFlashModel;
import net.hydra.jojomod.client.models.projectile.renderers.JJFSpitRenderer;
import net.hydra.jojomod.client.models.stand.renderers.JumpinJackFlashRenderer;

import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySync;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public final class JJFModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() { /*
        EntityModelLayerRegistry.registerModelLayer(
                ModEntityRendererClient.JUMPIN_JACK_FLASH_LAYER,
                JumpinJackFlashModel::createBodyLayer
        );

        EntityRendererRegistry.register(
                ModEntities.JUMPIN_JACK_FLASH,
                JumpinJackFlashRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities.JJF_PROJECTILE,
                ThrownItemRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities.JJF_GUNPOWDER_CLOUD,
                NoopRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities.JJF_SPIT,
                JJFSpitRenderer::new
        );
*/
        ClientPlayNetworking.registerGlobalReceiver(ZeroGravitySync.CHANNEL,
                (client, handler, buffer, responseSender) -> {
                    ZeroGravitySync.Snapshot snapshot = ZeroGravitySync.read(buffer);
                    client.execute(() -> {
                        if (client.player != null) {
                            ZeroGravitySync.applyClient(client.player, snapshot);
                        }
                    });
                });
    }
}
