package net.hydra.jojomod.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.TheWorldModel;
import net.hydra.jojomod.entity.stand.TheWorldRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class RegisterModLayers {

    public static void register(){
        EntityRendererRegistry.register(ModEntities.TERRIER_DOG, TerrierEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.THE_WORLD, TheWorldRenderer::new);
        //EntityModelLayerRegistry.registerModelLayer(WOLF_LAYER, WolfEntityModel::getTexturedModelData);
        //EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        //EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
    }
}
