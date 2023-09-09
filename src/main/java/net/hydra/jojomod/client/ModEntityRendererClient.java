package net.hydra.jojomod.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.TerrierEntityModel;
import net.hydra.jojomod.entity.TerrierEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.util.Identifier;
@Environment(EnvType.CLIENT)
public class ModEntityRendererClient {
    public static final EntityModelLayer WOLF_LAYER = new EntityModelLayer(new Identifier(RoundaboutMod.MOD_ID, "wolf"), "main");

    public static void register(){
        EntityRendererRegistry.register(ModEntities.TERRIER_DOG, TerrierEntityRenderer::new);
        //EntityModelLayerRegistry.registerModelLayer(WOLF_LAYER, WolfEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WOLF_LAYER, TerrierEntityModel::getTexturedModelData);
    }
}
