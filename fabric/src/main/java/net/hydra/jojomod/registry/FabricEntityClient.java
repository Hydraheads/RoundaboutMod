package net.hydra.jojomod.registry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class FabricEntityClient {

    public static <T extends Entity> EntityType<T> register(BuiltInRegistries entityType, String name, EntityType<T> builder) {
        return Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                new ResourceLocation(Roundabout.MOD_ID, "terrier"),
                builder);
    }

    public static void register() {
        /*Renderers*/
        EntityRendererRegistry.register(FabricEntities.TERRIER_DOG, TerrierEntityRenderer::new);
        EntityRendererRegistry.register(FabricEntities.STAR_PLATINUM, StarPlatinumRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THE_WORLD, TheWorldRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_HARPOON, HarpoonRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_KNIFE, KnifeRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_MATCH, ThrownItemRenderer::new);
        EntityRendererRegistry.register(FabricEntities.GASOLINE_SPLATTER, ThrownItemRenderer::new);
        EntityRendererRegistry.register(FabricEntities.GASOLINE_CAN, GasolineCanRenderer::new);
        EntityRendererRegistry.register(FabricEntities.STAND_ARROW, StandArrowRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_OBJECT, ThrownObjectRenderer::new);
        /*Models*/
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.STAR_PLATINUM_LAYER, StarPlatinumModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.HARPOON_LAYER, HarpoonModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.KNIFE_LAYER, KnifeModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.GASOLINE_LAYER, GasolineCanModel::createBodyLayer);
    }
}