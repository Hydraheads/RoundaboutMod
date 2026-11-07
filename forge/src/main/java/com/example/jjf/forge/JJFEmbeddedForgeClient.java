package com.example.jjf.forge;

import com.example.jjf.client.models.stand.JumpinJackFlashModel;
import com.example.jjf.client.models.stand.renderers.JJFSpitRenderer;
import com.example.jjf.client.models.stand.renderers.JumpinJackFlashRenderer;
import com.example.jjf.forge.registry.ForgeJJFRegistries;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public final class JJFEmbeddedForgeClient {

    private JJFEmbeddedForgeClient() {
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ForgeJJFRegistries.JUMPIN_JACK_FLASH.get(),
                JumpinJackFlashRenderer::new
        );
        event.registerEntityRenderer(
                ForgeJJFRegistries.JJF_PROJECTILE.get(),
                ThrownItemRenderer::new
        );
        event.registerEntityRenderer(
                ForgeJJFRegistries.JJF_GUNPOWDER_CLOUD.get(),
                NoopRenderer::new
        );
        event.registerEntityRenderer(
                ForgeJJFRegistries.JJF_SPIT.get(),
                JJFSpitRenderer::new
        );
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                JumpinJackFlashModel.LAYER_LOCATION,
                JumpinJackFlashModel::createBodyLayer
        );
    }
}