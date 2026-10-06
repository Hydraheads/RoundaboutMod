package net.hydra.jojomod.chocolatedisco.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoModel;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoSlimModel;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoFeatureRenderer;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoGridLabelRenderer;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoGridRenderer;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoRenderer;
import net.hydra.jojomod.registry.FabricEntities;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class RoundaboutChocolateDiscoFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChocolateDiscoSounds.register();

        EntityModelLayerRegistry.registerModelLayer(
                ChocolateDiscoClient.CHOCOLATE_DISCO_LAYER,
                ChocolateDiscoModel::getTexturedModelData
        );
        EntityModelLayerRegistry.registerModelLayer(
                ChocolateDiscoClient.CHOCOLATE_DISCO_SLIM_LAYER,
                ChocolateDiscoSlimModel::getTexturedModelData
        );
        EntityRendererRegistry.register(FabricEntities.CHOCOLATE_DISCO, ChocolateDiscoRenderer::new);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityRenderer instanceof PlayerRenderer playerRenderer) {
                        registrationHelper.register(
                                new ChocolateDiscoFeatureRenderer(
                                        (LivingEntityRenderer) playerRenderer,
                                        context
                                )
                        );
                    }
                }
        );

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (context.matrixStack() != null && context.world() != null) {
                ChocolateDiscoGridRenderer.render(context.matrixStack(), context.world());
                if (context.consumers() != null) {
                    ChocolateDiscoGridLabelRenderer.render(
                            context.matrixStack(), context.consumers(), context.world()
                    );
                }
            }
        });

        HudRenderCallback.EVENT.register(ChocolateDiscoSelfDiscoOverlay::render);
    }
}
