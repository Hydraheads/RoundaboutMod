package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.visages.mobs.JotaroNPC;
import net.hydra.jojomod.entity.visages.mobs.OVAEnyaNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerAlexNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerSteveNPC;
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
        public static final EntityType<OVAEnyaNPC> OVA_ENYA =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jnpc_ova_enya"),
                        EntityType.Builder.of(OVAEnyaNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jnpc_ova_enya")
                );
        public static final EntityType<JotaroNPC> JOTARO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jnpc_jotaro"),
                        EntityType.Builder.of(JotaroNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jnpc_jotaro")
                );
        public static final EntityType<PlayerSteveNPC> STEVE_NPC =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jnpc_steve"),
                        EntityType.Builder.of(PlayerSteveNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jnpc_steve")
                );
        public static final EntityType<PlayerAlexNPC> ALEX_NPC =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jnpc_alex"),
                        EntityType.Builder.of(PlayerAlexNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jnpc_alex")
                );

        public static final EntityType<TheWorldEntity> THE_WORLD =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "the_world"),
                        EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":the_world")
                );

        public static final EntityType<StarPlatinumEntity> STAR_PLATINUM =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "star_platinum"),
                        EntityType.Builder.of(StarPlatinumEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":star_platinum")
                );

        public static final EntityType<StarPlatinumBaseballEntity> STAR_PLATINUM_BASEBALL =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "star_platinum_baseball"),
                        EntityType.Builder.of(StarPlatinumBaseballEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":star_platinum_baseball")
                );
        public static final EntityType<JusticeEntity> JUSTICE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "justice"),
                        EntityType.Builder.of(JusticeEntity::new, MobCategory.MISC).
                                sized(ModEntities.justiceWidth, ModEntities.justiceHeight).clientTrackingRange(12).build(Roundabout.MOD_ID+":justice")
                );
        public static final EntityType<JusticePirateEntity> JUSTICE_PIRATE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "justice_pirate"),
                        EntityType.Builder.of(JusticePirateEntity::new, MobCategory.MISC).
                                sized(ModEntities.justiceWidth, ModEntities.justiceHeight).clientTrackingRange(10).build(Roundabout.MOD_ID+":justice_pirate")
                );
        public static final EntityType<DarkMirageEntity> DARK_MIRAGE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "dark_mirage"),
                        EntityType.Builder.of(DarkMirageEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":dark_mirage")
                );
        public static final EntityType<HarpoonEntity> THROWN_HARPOON =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "harpoon"),
                        EntityType.Builder.<HarpoonEntity>of(HarpoonEntity::new, MobCategory.MISC).
                                sized(0.5f, 0.5f).clientTrackingRange(10).build(Roundabout.MOD_ID+":harpoon")
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
        public static final EntityType<StandArrowEntity> STAND_ARROW =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "stand_arrow"),
                        EntityType.Builder.<StandArrowEntity>of(StandArrowEntity::new, MobCategory.MISC).
                                sized(0.7f, 0.7f).clientTrackingRange(6).build(Roundabout.MOD_ID+":stand_arrow")
                );
        public static final EntityType<ThrownObjectEntity> THROWN_OBJECT =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "thrown_object"),
                        EntityType.Builder.<ThrownObjectEntity>of(ThrownObjectEntity::new, MobCategory.MISC).
                                sized(1f, 1f).clientTrackingRange(10).build(Roundabout.MOD_ID+":thrown_object")
                );

        public static void register() {
                /*Common Code Bridge*/
                ModEntities.THE_WORLD = THE_WORLD;
                ModEntities.TERRIER_DOG = TERRIER_DOG;
                ModEntities.STAR_PLATINUM = STAR_PLATINUM;
                ModEntities.STAR_PLATINUM_BASEBALL = STAR_PLATINUM_BASEBALL;
                ModEntities.JUSTICE = JUSTICE;
                ModEntities.JUSTICE_PIRATE = JUSTICE_PIRATE;
                ModEntities.DARK_MIRAGE = DARK_MIRAGE;
                ModEntities.THROWN_HARPOON = THROWN_HARPOON;
                ModEntities.THROWN_KNIFE = THROWN_KNIFE;
                ModEntities.THROWN_MATCH = THROWN_MATCH;
                ModEntities.GASOLINE_CAN = GASOLINE_CAN;
                ModEntities.GASOLINE_SPLATTER = GASOLINE_SPLATTER;
                ModEntities.STAND_ARROW = STAND_ARROW;

                ModEntities.THROWN_OBJECT = THROWN_OBJECT;

                ModEntities.OVA_ENYA = OVA_ENYA;
                ModEntities.JOTARO = JOTARO;
                ModEntities.STEVE_NPC = STEVE_NPC;
                ModEntities.ALEX_NPC = ALEX_NPC;

                /*Attributes*/
                FabricDefaultAttributeRegistry.register(TERRIER_DOG, Wolf.createAttributes());

                FabricDefaultAttributeRegistry.register(OVA_ENYA, OVAEnyaNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(JOTARO, JotaroNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(STEVE_NPC, PlayerSteveNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(ALEX_NPC, PlayerAlexNPC.createAttributes());

                FabricDefaultAttributeRegistry.register(THE_WORLD, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(STAR_PLATINUM, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(STAR_PLATINUM_BASEBALL, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(JUSTICE, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(JUSTICE_PIRATE, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(DARK_MIRAGE, StandEntity.createStandAttributes());

                /*Spawn Weights and Biomes*/
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.DESERT), MobCategory.CREATURE,
                        ModEntities.TERRIER_DOG, ClientNetworking.getAppropriateConfig().fabricTerrierSpawnWeightInDesertUseDatapackForForge, 1, 1);
                 BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.TAIGA), MobCategory.CREATURE,
                         ModEntities.TERRIER_DOG, ClientNetworking.getAppropriateConfig().fabricTerrierSpawnWeightInTaigaUseDatapackForForge, 1, 3);

                /*Spawn Placement rules*/
                 SpawnPlacements.register(ModEntities.TERRIER_DOG, SpawnPlacements.Type.ON_GROUND,
                         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TerrierEntity::canSpawn);
        }
}
