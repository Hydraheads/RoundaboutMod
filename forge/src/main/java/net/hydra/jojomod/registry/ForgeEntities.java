package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ForgeEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Roundabout.MOD_ID);
    public static final RegistryObject<EntityType<TerrierEntity>> TERRIER_DOG =
            ENTITY_TYPES.register("terrier", () ->
                    EntityType.Builder.of(TerrierEntity::new, MobCategory.CREATURE).sized(0.6f, 0.55f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "terrier").toString())
            );
    public static final RegistryObject<EntityType<TheWorldEntity>> THE_WORLD =
            ENTITY_TYPES.register("the_world", () ->
                    EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "the_world").toString())
            );
    public static final RegistryObject<EntityType<StarPlatinumEntity>> STAR_PLATINUM =
            ENTITY_TYPES.register("star_platinum", () ->
                    EntityType.Builder.of(StarPlatinumEntity::new, MobCategory.MISC).sized(0.001F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "star_platinum").toString())
            );
    public static final RegistryObject<EntityType<KnifeEntity>> THROWN_KNIFE =
            ENTITY_TYPES.register("knife", () ->
                    EntityType.Builder.<KnifeEntity>of(KnifeEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "knife").toString())
            );
    public static final RegistryObject<EntityType<HarpoonEntity>> THROWN_HARPOON =
            ENTITY_TYPES.register("harpoon", () ->
                    EntityType.Builder.<HarpoonEntity>of(HarpoonEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "harpoon").toString())
            );
    public static final RegistryObject<EntityType<MatchEntity>> THROWN_MATCH =
            ENTITY_TYPES.register("match", () ->
                    EntityType.Builder.<MatchEntity>of(MatchEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "match").toString())
            );
    public static final RegistryObject<EntityType<GasolineCanEntity>> GASOLINE_CAN =
            ENTITY_TYPES.register("gasoline_can", () ->
                    EntityType.Builder.<GasolineCanEntity>of(GasolineCanEntity::new, MobCategory.MISC).sized(0.8f, 0.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "gasoline_can").toString())
            );
    public static final RegistryObject<EntityType<GasolineSplatterEntity>> GASOLINE_SPLATTER =
            ENTITY_TYPES.register("gasoline_splatter", () ->
                    EntityType.Builder.<GasolineSplatterEntity>of(GasolineSplatterEntity::new, MobCategory.MISC).sized(0.8f, 0.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "gasoline_splatter").toString())
            );
    public static final RegistryObject<EntityType<ThrownObjectEntity>> THROWN_OBJECT =
            ENTITY_TYPES.register("thrown_object", () ->
                    EntityType.Builder.<ThrownObjectEntity>of(ThrownObjectEntity::new, MobCategory.MISC).sized(1f, 1f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "thrown_object").toString())
            );
}
