package net.hydra.jojomod.registry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.projectile.KnifeModel;
import net.hydra.jojomod.entity.projectile.KnifeRenderer;
import net.hydra.jojomod.entity.stand.TheWorldModel;
import net.hydra.jojomod.entity.stand.TheWorldRenderer;
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
        EntityRendererRegistry.register(FabricEntities.THE_WORLD, TheWorldRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_KNIFE, KnifeRenderer::new);
        /*Models*/
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.KNIFE_LAYER, KnifeModel::createBodyLayer);
    }
}