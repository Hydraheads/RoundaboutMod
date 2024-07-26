package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.TheWorldModel;
import net.hydra.jojomod.entity.stand.TheWorldRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ForgeEntities.TERRIER_DOG.get(), TerrierEntityRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THE_WORLD.get(), TheWorldRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_HARPOON.get(), HarpoonRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_KNIFE.get(), KnifeRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_MATCH.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_SPLATTER.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_CAN.get(), GasolineCanRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        event.registerLayerDefinition(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.KNIFE_LAYER, KnifeModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.HARPOON_LAYER, HarpoonModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.GASOLINE_LAYER, GasolineCanModel::createBodyLayer);
    }
}
