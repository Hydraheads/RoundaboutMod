package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSelfDiscoOverlay;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSounds;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoModel;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoSlimModel;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoFeatureRenderer;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoGridLabelRenderer;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoGridRenderer;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ChocolateDiscoForgeClientEvents {
    private ChocolateDiscoForgeClientEvents() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ForgeEntities.CHOCOLATE_DISCO.get(), ChocolateDiscoRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ChocolateDiscoClient.CHOCOLATE_DISCO_LAYER, ChocolateDiscoModel::getTexturedModelData);
        event.registerLayerDefinition(ChocolateDiscoClient.CHOCOLATE_DISCO_SLIM_LAYER, ChocolateDiscoSlimModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            LivingEntityRenderer<?, ?> renderer = event.getSkin(skin);
            if (renderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new ChocolateDiscoFeatureRenderer(playerRenderer, event.getEntityModels()));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBusEvents {
        @SubscribeEvent
        public static void renderLevel(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            ChocolateDiscoGridRenderer.render(event.getPoseStack(), mc.level);
            ChocolateDiscoGridLabelRenderer.render(event.getPoseStack(), buffers, mc.level);
        }

        @SubscribeEvent
        public static void renderHud(RenderGuiOverlayEvent.Post event) {
            ChocolateDiscoSelfDiscoOverlay.render(event.getGuiGraphics(), event.getPartialTick());
        }
    }
}
