package net.hydra.jojomod.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.stand.StandStarPlatinumRenderer;
import net.hydra.jojomod.entity.stand.TheWorldModel;
import net.hydra.jojomod.entity.stand.TheWorldRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
@Environment(EnvType.CLIENT)
public class ModEntityRendererClient {
    /** Registers the visual component of mobs. Note: Renderers render models, but models are different files*/
    public static final EntityModelLayer WOLF_LAYER = new EntityModelLayer(new Identifier(RoundaboutMod.MOD_ID, "wolf"), "main");
    public static final EntityModelLayer THE_WORLD_LAYER = new EntityModelLayer(new Identifier(RoundaboutMod.MOD_ID, "the_world"), "main");

    public static void register(){
        EntityRendererRegistry.register(ModEntities.TERRIER_DOG, TerrierEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.THE_WORLD, TheWorldRenderer::new);
        //EntityModelLayerRegistry.registerModelLayer(WOLF_LAYER, WolfEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WOLF_LAYER, TerrierEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
    }
}
