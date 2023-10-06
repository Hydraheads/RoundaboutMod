package net.hydra.jojomod.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.stand.StandStarPlatinumRenderer;
import net.hydra.jojomod.entity.stand.StandTheWorldRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
@Environment(EnvType.CLIENT)
public class ModEntityRendererClient {
    /** Registers the visual component of mobs. Note: Renderers render models, but models are different files*/
    public static final EntityModelLayer WOLF_LAYER = new EntityModelLayer(new Identifier(RoundaboutMod.MOD_ID, "wolf"), "main");

    public static void register(){
        EntityRendererRegistry.register(ModEntities.TERRIER_DOG, TerrierEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.THE_WORLD, StandTheWorldRenderer::new);
        EntityRendererRegistry.register(ModEntities.STAR_PLATINUM, StandStarPlatinumRenderer::new);
        //EntityModelLayerRegistry.registerModelLayer(WOLF_LAYER, WolfEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WOLF_LAYER, TerrierEntityModel::getTexturedModelData);
    }
}
