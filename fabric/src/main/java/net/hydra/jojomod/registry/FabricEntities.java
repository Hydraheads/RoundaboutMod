package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.GasolineSplatterEntity;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

public class FabricEntities {

        public static final EntityType<TerrierEntity> TERRIER_DOG =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "terrier"),
                        EntityType.Builder.of(TerrierEntity::new, MobCategory.CREATURE).
                                sized(0.6f, 0.55f).clientTrackingRange(10).build(Roundabout.MOD_ID+":terrier")
                );

        public static final EntityType<TheWorldEntity> THE_WORLD =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "the_world"),
                        EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).
                                sized(0.001F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":the_world")
                );

        public static final EntityType<TheWorldEntity> STAR_PLATINUM =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "star_platinum"),
                        EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).
                                sized(0.001F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":star_platinum")
                );
        public static final EntityType<KnifeEntity> THROWN_KNIFE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "knife"),
                        EntityType.Builder.<KnifeEntity>of(KnifeEntity::new, MobCategory.MISC).
                                sized(0.5f, 0.5f).clientTrackingRange(10).build(Roundabout.MOD_ID+":knife")
                );
        public static final EntityType<MatchEntity> THROWN_MATCH =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "match"),
                        EntityType.Builder.<MatchEntity>of(MatchEntity::new, MobCategory.MISC).
                                sized(0.5f, 0.5f).clientTrackingRange(10).build(Roundabout.MOD_ID+":match")
                );
        public static final EntityType<GasolineCanEntity> GASOLINE_CAN =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "gasoline_can"),
                        EntityType.Builder.<GasolineCanEntity>of(GasolineCanEntity::new, MobCategory.MISC).
                                sized(0.8f, 0.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":gasoline_can")
                );
        public static final EntityType<GasolineSplatterEntity> GASOLINE_SPLATTER =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "gasoline_splatter"),
                        EntityType.Builder.<GasolineSplatterEntity>of(GasolineSplatterEntity::new, MobCategory.MISC).
                                sized(0.8f, 0.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":gasoline_splatter")
                );

        public static void register() {
                /*Common Code Bridge*/
                ModEntities.THE_WORLD = THE_WORLD;
                ModEntities.TERRIER_DOG = TERRIER_DOG;
                ModEntities.STAR_PLATINUM = STAR_PLATINUM;
                ModEntities.THROWN_KNIFE = THROWN_KNIFE;
                ModEntities.THROWN_MATCH = THROWN_MATCH;
                ModEntities.GASOLINE_CAN = GASOLINE_CAN;
                ModEntities.GASOLINE_SPLATTER = GASOLINE_SPLATTER;

                /*Attributes*/
                FabricDefaultAttributeRegistry.register(TERRIER_DOG, Wolf.createAttributes());
                FabricDefaultAttributeRegistry.register(THE_WORLD, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(STAR_PLATINUM, StandEntity.createStandAttributes());

                /*Spawn Weights and Biomes*/
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.DESERT), MobCategory.CREATURE,
                        ModEntities.TERRIER_DOG, 1, 1, 1);
                 BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.TAIGA), MobCategory.CREATURE,
                         ModEntities.TERRIER_DOG, 2, 1, 3);

                /*Spawn Placement rules*/
                 SpawnPlacements.register(ModEntities.TERRIER_DOG, SpawnPlacements.Type.ON_GROUND,
                         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TerrierEntity::canSpawn);
        }
}
