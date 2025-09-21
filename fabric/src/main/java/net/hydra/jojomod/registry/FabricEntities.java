package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.TerrierEntity;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.pathfinding.GroundBubbleEntity;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.substand.EncasementBubbleEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.visages.mobs.*;
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
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_ova_enya"),
                        EntityType.Builder.of(OVAEnyaNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_ova_enya")
                );
        public static final EntityType<EnyaNPC> ENYA =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_enya"),
                        EntityType.Builder.of(EnyaNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_enya")
                );
        public static final EntityType<JotaroNPC> JOTARO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_jotaro"),
                        EntityType.Builder.of(JotaroNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_jotaro")
                );
        public static final EntityType<AvdolNPC> AVDOL =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_avdol"),
                        EntityType.Builder.of(AvdolNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_avdol")
                );
        public static final EntityType<ValentineNPC> VALENTINE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_valentine"),
                        EntityType.Builder.of(ValentineNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_valentine")
                );
        public static final EntityType<DIONPC> DIO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_dio"),
                        EntityType.Builder.of(DIONPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_dio")
                );
        public static final EntityType<ParallelDiegoNPC> PARALLEL_DIEGO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_parallel_diego"),
                        EntityType.Builder.of(ParallelDiegoNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_parallel_diego")
                );
        public static final EntityType<JosukePartEightNPC> JOSUKE_PART_EIGHT =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_josuke_part_eight"),
                        EntityType.Builder.of(JosukePartEightNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_josuke_part_eight")
                );
        public static final EntityType<AyaNPC> AYA =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_aya"),
                        EntityType.Builder.of(AyaNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_aya")
                );
        public static final EntityType<Aesthetician> AESTHETICIAN =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "aesthetician"),
                        EntityType.Builder.of(Aesthetician::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":aesthetician")
                );
        public static final EntityType<ZombieAesthetician> ZOMBIE_AESTHETICIAN =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "zombie_aesthetician"),
                        EntityType.Builder.of(ZombieAesthetician::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":zombie_aesthetician")
                );
        public static final EntityType<PocolocoNPC> POCOLOCO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_pocoloco"),
                        EntityType.Builder.of(PocolocoNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_pocoloco")
                );
        public static final EntityType<GuccioNPC> GUCCIO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_guccio"),
                        EntityType.Builder.of(GuccioNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_guccio")
                );
        public static final EntityType<RingoNPC> RINGO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_ringo"),
                        EntityType.Builder.of(RingoNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_ringo")
                );
        public static final EntityType<HatoNPC> HATO =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_hato"),
                        EntityType.Builder.of(HatoNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_hato")
                );
        public static final EntityType<ShizukaNPC> SHIZUKA =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_shizuka"),
                        EntityType.Builder.of(ShizukaNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_shizuka")
                );
        public static final EntityType<PlayerSteveNPC> STEVE_NPC =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_steve"),
                        EntityType.Builder.of(PlayerSteveNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_steve")
                );
        public static final EntityType<PlayerAlexNPC> ALEX_NPC =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_alex"),
                        EntityType.Builder.of(PlayerAlexNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_alex")
                );
        public static final EntityType<PlayerModifiedNPC> MODIFIED_NPC =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_modified"),
                        EntityType.Builder.of(PlayerModifiedNPC::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":jojo_npc_modified")
                );
        public static final EntityType<FogCloneEntity> FOG_CLONE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "fog_clone"),
                        EntityType.Builder.of(FogCloneEntity::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":fog_clone")
                );
        public static final EntityType<D4CCloneEntity> D4C_CLONE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "d4c_clone"),
                        EntityType.Builder.of(D4CCloneEntity::new, MobCategory.MISC).
                                sized(0.6f, 1.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":d4c_clone")
                );
        public static final EntityType<FallenZombie> FALLEN_ZOMBIE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "fallen_zombie"),
                        EntityType.Builder.of(FallenZombie::new, MobCategory.MISC).sized(0.6F, 1.95F).clientTrackingRange(8).build(Roundabout.MOD_ID+":fallen_zombie")
                );
        public static final EntityType<FallenSkeleton> FALLEN_SKELETON =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "fallen_skeleton"),
                        EntityType.Builder.of(FallenSkeleton::new, MobCategory.MISC).sized(0.6F, 1.99F).clientTrackingRange(8).build(Roundabout.MOD_ID+":fallen_skeleton")
                );
        public static final EntityType<FallenSpider> FALLEN_SPIDER =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "fallen_spider"),
                        EntityType.Builder.of(FallenSpider::new, MobCategory.MISC).sized(1.4f, 0.9f).clientTrackingRange(8).build(Roundabout.MOD_ID+":fallen_skeleton")
                );
        public static final EntityType<FallenVillager> FALLEN_VILLAGER =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "fallen_villager"),
                        EntityType.Builder.of(FallenVillager::new, MobCategory.MISC).sized(0.6F, 1.95F).clientTrackingRange(8).build(Roundabout.MOD_ID+":fallen_skeleton")
                );
        public static final EntityType<FallenCreeper> FALLEN_CREEPER =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "fallen_creeper"),
                        EntityType.Builder.of(FallenCreeper::new, MobCategory.MISC).sized(0.6F, 1.7F).clientTrackingRange(8).build(Roundabout.MOD_ID+":fallen_skeleton")
                );

        public static final EntityType<FallenPhantom> FALLEN_PHANTOM =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "fallen_phantom"),
                        EntityType.Builder.of(FallenPhantom::new, MobCategory.MISC).sized(0.8F, 0.5F).clientTrackingRange(8).build(Roundabout.MOD_ID+":fallen_skeleton")
                );

        public static final EntityType<TheWorldEntity> THE_WORLD =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "the_world"),
                        EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":the_world")
                );

        public static final EntityType<TheWorldEntity> THE_WORLD_ULTIMATE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "the_world_ultimate"),
                        EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":the_world_ultimate")
                );

        public static final EntityType<StarPlatinumEntity> STAR_PLATINUM =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "star_platinum"),
                        EntityType.Builder.of(StarPlatinumEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":star_platinum")
                );

        public static final EntityType<StarPlatinumBaseballEntity> STAR_PLATINUM_BASEBALL =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "star_platinum_baseball"),
                        EntityType.Builder.of(StarPlatinumBaseballEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":star_platinum_baseball")
                );
        public static final EntityType<JusticeEntity> JUSTICE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "justice"),
                        EntityType.Builder.of(JusticeEntity::new, MobCategory.MISC).
                                sized(ModEntities.justiceWidth, ModEntities.justiceHeight).clientTrackingRange(14).build(Roundabout.MOD_ID+":justice")
                );
        public static final EntityType<MagiciansRedEntity> MAGICIANS_RED =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "magicians_red"),
                        EntityType.Builder.of(MagiciansRedEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":magicians_red")
                );
        public static final EntityType<MagiciansRedEntity> MAGICIANS_RED_OVA =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "magicians_red_ova"),
                        EntityType.Builder.of(MagiciansRedEntity::new, MobCategory.MISC).
                                sized(0.75F, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":magicians_red")
                );
        public static final EntityType<D4CEntity> D4C =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("d4c"),
                        EntityType.Builder.of(D4CEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":d4c")
                );

        public static final EntityType<SoftAndWetEntity> SOFT_AND_WET =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("soft_and_wet"),
                        EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":soft_and_wet")
                );
        public static final EntityType<RattEntity> RATT =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("ratt"),
                        EntityType.Builder.of(RattEntity::new, MobCategory.MISC).
                                sized(0.75f, 1.2f).clientTrackingRange(14).build(Roundabout.MOD_ID+":ratt")
                );
        public static final EntityType<FleshPileEntity> FLESH_PILE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "flesh_pile"),
                        EntityType.Builder.<FleshPileEntity>of(FleshPileEntity::new, MobCategory.MISC).
                                sized(0.8f, 0.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":flesh_pile")
                );
        public static final EntityType<GreenDayEntity> GREEN_DAY =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("green_day"),
                        EntityType.Builder.of(GreenDayEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":green_day")
                );
        public static final EntityType<SurvivorEntity> SURVIVOR =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("survivor"),
                        EntityType.Builder.of(SurvivorEntity::new, MobCategory.MISC).
                                sized(0.5f, 0.3f).clientTrackingRange(14).build(Roundabout.MOD_ID+":survivor")
                );
        public static final EntityType<SoftAndWetEntity> SOFT_AND_WET_DROWNED =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("soft_and_wet_drowned"),
                        EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":soft_and_wet_drowned")
                );
        public static final EntityType<SoftAndWetEntity> SOFT_AND_WET_DEBUT =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("soft_and_wet_debut"),
                        EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":soft_and_wet_debut")
                );
        public static final EntityType<SoftAndWetEntity> SOFT_AND_WET_KING =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("soft_and_wet_king"),
                        EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":soft_and_wet_king")
                );
        public static final EntityType<SoftAndWetEntity> SOFT_AND_WET_KILLER_QUEEN =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("soft_and_wet_killer_queen"),
                        EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":soft_and_wet_killer_queen")
                );
        public static final EntityType<KillerQueenEntity> KILLER_QUEEN =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("killer_queen"),
                        EntityType.Builder.of(KillerQueenEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":killer_queen")
                );
        public static final EntityType<CinderellaEntity> CINDERELLA =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("cinderella"),
                        EntityType.Builder.of(CinderellaEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":cinderella")
                );
        public static final EntityType<WalkingHeartEntity> WALKING_HEART =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("walking_heart"),
                        EntityType.Builder.of(WalkingHeartEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":walking_heart")
                );
        public static final EntityType<JusticePirateEntity> JUSTICE_PIRATE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "justice_pirate"),
                        EntityType.Builder.of(JusticePirateEntity::new, MobCategory.MISC).
                                sized(ModEntities.justiceWidth, ModEntities.justiceHeight).clientTrackingRange(14).build(Roundabout.MOD_ID+":justice_pirate")
                );
        public static final EntityType<DarkMirageEntity> DARK_MIRAGE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "dark_mirage"),
                        EntityType.Builder.of(DarkMirageEntity::new, MobCategory.MISC).
                                sized(ModEntities.justiceWidth, ModEntities.justiceHeight).clientTrackingRange(14).build(Roundabout.MOD_ID+":dark_mirage")
                );
        public static final EntityType<DiverDownEntity> DIVER_DOWN =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("diver_down"),
                        EntityType.Builder.of(DiverDownEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":diver_down")
                );
        public static final EntityType<TheGratefulDeadEntity> THE_GRATEFUL_DEAD =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        Roundabout.location("the_grateful_dead"),
                        EntityType.Builder.of(TheGratefulDeadEntity::new, MobCategory.MISC).
                                sized(0.75f, 2.05f).clientTrackingRange(14).build(Roundabout.MOD_ID+":the_grateful_dead")
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
        public static final EntityType<RattDartEntity> RATT_DART =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "ratt_dart"),
                        EntityType.Builder.<RattDartEntity>of(RattDartEntity::new, MobCategory.MISC).
                                sized(0.5f, 0.5f).clientTrackingRange(10).build(Roundabout.MOD_ID+":ratt_dart")
                );
        public static final EntityType<MatchEntity> THROWN_MATCH =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "match"),
                        EntityType.Builder.<MatchEntity>of(MatchEntity::new, MobCategory.MISC).
                                sized(0.5f, 0.5f).clientTrackingRange(10).build(Roundabout.MOD_ID+":match")
                );
        public static final EntityType<ThrownWaterBottleEntity> THROWN_WATER_BOTTLE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "water_bottle"),
                        EntityType.Builder.<ThrownWaterBottleEntity>of(ThrownWaterBottleEntity::new, MobCategory.MISC).
                                sized(0.5f, 0.5f).clientTrackingRange(12).build(Roundabout.MOD_ID+":water_bottle")
                );
        public static final EntityType<GasolineCanEntity> GASOLINE_CAN =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "gasoline_can"),
                        EntityType.Builder.<GasolineCanEntity>of(GasolineCanEntity::new, MobCategory.MISC).
                                sized(0.8f, 0.8f).clientTrackingRange(10).build(Roundabout.MOD_ID+":gasoline_can")
                );
        public static final EntityType<CrossfireHurricaneEntity> CROSSFIRE_HURRICANE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "crossfire_hurricane"),
                        EntityType.Builder.<CrossfireHurricaneEntity>of(CrossfireHurricaneEntity::new, MobCategory.MISC).
                                sized(1.5f, 1.5f).clientTrackingRange(15).build(Roundabout.MOD_ID+":crossfire_hurricane")
                );
        public static final EntityType<LifeTrackerEntity> LIFE_TRACKER =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "life_tracker"),
                        EntityType.Builder.<LifeTrackerEntity>of(LifeTrackerEntity::new, MobCategory.MISC).
                                sized(1, 1).clientTrackingRange(15).build(Roundabout.MOD_ID+":life_tracker")
                );
        public static final EntityType<StandFireballEntity> STAND_FIREBALL =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "stand_fireball"),
                        EntityType.Builder.<StandFireballEntity>of(StandFireballEntity::new, MobCategory.MISC).
                                sized(0.9F, 0.9F).clientTrackingRange(15).build(Roundabout.MOD_ID+":stand_fireball")
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
        public static final EntityType<ConcealedFlameObjectEntity> CONCEALED_FLAME_OBJECT =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "concealed_flame_object"),
                        EntityType.Builder.<ConcealedFlameObjectEntity>of(ConcealedFlameObjectEntity::new, MobCategory.MISC).
                                sized(1f, 1f).clientTrackingRange(10).build(Roundabout.MOD_ID+":concealed_flame_object")
                );
        public static final EntityType<CinderellaVisageDisplayEntity> CINDERELLA_VISAGE_DISPLAY =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "visage_display"),
                        EntityType.Builder.<CinderellaVisageDisplayEntity>of(CinderellaVisageDisplayEntity::new, MobCategory.MISC).
                                sized(1f, 1f).clientTrackingRange(10).build(Roundabout.MOD_ID+":visage_display")
                );
        public static final EntityType<GroundHurricaneEntity> GROUND_HURRICANE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "ground_hurricane"),
                        EntityType.Builder.<GroundHurricaneEntity>of(GroundHurricaneEntity::new, MobCategory.MISC).
                                sized(0.2f, 0.2f).clientTrackingRange(10).build(Roundabout.MOD_ID+":ground_hurricane")
                );
        public static final EntityType<GroundBubbleEntity> GROUND_BUBBLE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "ground_bubble"),
                        EntityType.Builder.<GroundBubbleEntity>of(GroundBubbleEntity::new, MobCategory.MISC).
                                sized(0.2f, 0.2f).clientTrackingRange(10).build(Roundabout.MOD_ID+":ground_bubble")
                );
        public static final EntityType<SoftAndWetPlunderBubbleEntity> PLUNDER_BUBBLE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "plunder_bubble"),
                        EntityType.Builder.<SoftAndWetPlunderBubbleEntity>of(SoftAndWetPlunderBubbleEntity::new, MobCategory.MISC).
                                sized(SoftAndWetBubbleEntity.eWidth, SoftAndWetBubbleEntity.eHeight).
                                clientTrackingRange(10).build(Roundabout.MOD_ID+":plunder_bubble")
                );
        public static final EntityType<SoftAndWetExplosiveBubbleEntity> EXPLOSIVE_BUBBLE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "explosive_bubble"),
                        EntityType.Builder.<SoftAndWetExplosiveBubbleEntity>of(SoftAndWetExplosiveBubbleEntity::new, MobCategory.MISC).
                                sized(SoftAndWetBubbleEntity.eWidth, SoftAndWetBubbleEntity.eHeight).
                                clientTrackingRange(10).build(Roundabout.MOD_ID+":explosive_bubble")
                );
        public static final EntityType<SoftAndWetItemLaunchingBubbleEntity> ITEM_LAUNCHING_BUBBLE_ENTITY =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "item_launching_bubble"),
                        EntityType.Builder.<SoftAndWetItemLaunchingBubbleEntity>of(SoftAndWetItemLaunchingBubbleEntity::new, MobCategory.MISC).
                                sized(SoftAndWetBubbleEntity.eWidth, SoftAndWetBubbleEntity.eHeight).
                                clientTrackingRange(10).build(Roundabout.MOD_ID+":item_launching_bubble")
                );
        public static final EntityType<GoBeyondEntity> GO_BEYOND =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "go_beyond"),
                        EntityType.Builder.<GoBeyondEntity>of(GoBeyondEntity::new, MobCategory.MISC).
                                sized(GoBeyondEntity.eWidth, GoBeyondEntity.eHeight).
                                clientTrackingRange(10).build(Roundabout.MOD_ID+":go_beyond")
                );
        public static final EntityType<EncasementBubbleEntity> ENCASEMENT_BUBBLE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        new ResourceLocation(Roundabout.MOD_ID, "encasement_bubble"),
                        EntityType.Builder.<EncasementBubbleEntity>of(EncasementBubbleEntity::new, MobCategory.MISC).
                                sized(EncasementBubbleEntity.eWidth, EncasementBubbleEntity.eHeight).
                                clientTrackingRange(10).build(Roundabout.MOD_ID+":encasement_bubble")
                );

        public static void register() {
                /*Common Code Bridge*/
                ModEntities.THE_WORLD = THE_WORLD;
                ModEntities.THE_WORLD_ULTIMATE = THE_WORLD_ULTIMATE;
                ModEntities.TERRIER_DOG = TERRIER_DOG;
                ModEntities.STAR_PLATINUM = STAR_PLATINUM;
                ModEntities.STAR_PLATINUM_BASEBALL = STAR_PLATINUM_BASEBALL;
                ModEntities.JUSTICE = JUSTICE;
                ModEntities.MAGICIANS_RED = MAGICIANS_RED;
                ModEntities.MAGICIANS_RED_OVA = MAGICIANS_RED_OVA;
                ModEntities.D4C = D4C;
                ModEntities.GREEN_DAY = GREEN_DAY;
                ModEntities.RATT = RATT;
                ModEntities.FLESH_PILE = FLESH_PILE;
                ModEntities.SURVIVOR = SURVIVOR;
                ModEntities.SOFT_AND_WET = SOFT_AND_WET;
                ModEntities.SOFT_AND_WET_DROWNED = SOFT_AND_WET_DROWNED;
                ModEntities.SOFT_AND_WET_DEBUT = SOFT_AND_WET_DEBUT;
                ModEntities.SOFT_AND_WET_KING = SOFT_AND_WET_KING;
                ModEntities.SOFT_AND_WET_KILLER_QUEEN = SOFT_AND_WET_KILLER_QUEEN;
                ModEntities.KILLER_QUEEN = KILLER_QUEEN;
                ModEntities.CINDERELLA = CINDERELLA;
                ModEntities.WALKING_HEART = WALKING_HEART;
                ModEntities.JUSTICE_PIRATE = JUSTICE_PIRATE;
                ModEntities.DARK_MIRAGE = DARK_MIRAGE;
                ModEntities.DIVER_DOWN = DIVER_DOWN;
                ModEntities.THE_GRATEFUL_DEAD = THE_GRATEFUL_DEAD;
                ModEntities.THROWN_HARPOON = THROWN_HARPOON;
                ModEntities.THROWN_KNIFE = THROWN_KNIFE;
                ModEntities.RATT_DART = RATT_DART;
                ModEntities.THROWN_MATCH = THROWN_MATCH;
                ModEntities.THROWN_WATER_BOTTLE = THROWN_WATER_BOTTLE;
                ModEntities.CROSSFIRE_HURRICANE = CROSSFIRE_HURRICANE;
                ModEntities.LIFE_TRACKER = LIFE_TRACKER;
                ModEntities.STAND_FIREBALL = STAND_FIREBALL;
                ModEntities.GASOLINE_CAN = GASOLINE_CAN;
                ModEntities.GASOLINE_SPLATTER = GASOLINE_SPLATTER;
                ModEntities.STAND_ARROW = STAND_ARROW;

                ModEntities.THROWN_OBJECT = THROWN_OBJECT;
                ModEntities.CONCEALED_FLAME_OBJECT = CONCEALED_FLAME_OBJECT;
                ModEntities.GROUND_HURRICANE = GROUND_HURRICANE;
                ModEntities.GROUND_BUBBLE = GROUND_BUBBLE;
                ModEntities.PLUNDER_BUBBLE = PLUNDER_BUBBLE;
                ModEntities.EXPLOSIVE_BUBBLE = EXPLOSIVE_BUBBLE;
                ModEntities.ITEM_LAUNCHING_BUBBLE_ENTITY = ITEM_LAUNCHING_BUBBLE_ENTITY;
                ModEntities.GO_BEYOND = GO_BEYOND;
                ModEntities.ENCASEMENT_BUBBLE = ENCASEMENT_BUBBLE;
                ModEntities.CINDERELLA_VISAGE_DISPLAY = CINDERELLA_VISAGE_DISPLAY;

                ModEntities.FALLEN_ZOMBIE = FALLEN_ZOMBIE;
                ModEntities.FALLEN_SKELETON = FALLEN_SKELETON;
                ModEntities.FALLEN_SPIDER = FALLEN_SPIDER;
                ModEntities.FALLEN_VILLAGER = FALLEN_VILLAGER;
                ModEntities.FALLEN_CREEPER = FALLEN_CREEPER;
                ModEntities.FALLEN_PHANTOM = FALLEN_PHANTOM;

                ModEntities.OVA_ENYA = OVA_ENYA;
                ModEntities.ENYA = ENYA;
                ModEntities.JOTARO = JOTARO;
                ModEntities.AVDOL = AVDOL;
                ModEntities.VALENTINE = VALENTINE;
                ModEntities.DIO = DIO;
                ModEntities.PARALLEL_DIEGO = PARALLEL_DIEGO;
                ModEntities.JOSUKE_PART_EIGHT = JOSUKE_PART_EIGHT;
                ModEntities.AYA = AYA;
                ModEntities.AESTHETICIAN = AESTHETICIAN;
                ModEntities.ZOMBIE_AESTHETICIAN = ZOMBIE_AESTHETICIAN;
                ModEntities.POCOLOCO = POCOLOCO;
                ModEntities.GUCCIO = GUCCIO;
                ModEntities.RINGO = RINGO;
                ModEntities.HATO = HATO;
                ModEntities.SHIZUKA = SHIZUKA;
                ModEntities.STEVE_NPC = STEVE_NPC;
                ModEntities.ALEX_NPC = ALEX_NPC;
                ModEntities.MODIFIED_NPC = MODIFIED_NPC;
                ModEntities.FOG_CLONE = FOG_CLONE;
                ModEntities.D4C_CLONE = D4C_CLONE;

                /*Attributes*/
                FabricDefaultAttributeRegistry.register(TERRIER_DOG, Wolf.createAttributes());

                FabricDefaultAttributeRegistry.register(OVA_ENYA, OVAEnyaNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(ENYA, OVAEnyaNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(JOTARO, JotaroNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(AVDOL, AvdolNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(VALENTINE, ValentineNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(DIO, DIONPC.createAttributes());
                FabricDefaultAttributeRegistry.register(PARALLEL_DIEGO, ParallelDiegoNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(JOSUKE_PART_EIGHT, JosukePartEightNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(AYA, AyaNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(AESTHETICIAN, Aesthetician.createAttributes());
                FabricDefaultAttributeRegistry.register(RINGO, RingoNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(HATO, HatoNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(SHIZUKA, ShizukaNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(POCOLOCO, PocolocoNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(GUCCIO, GuccioNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(ZOMBIE_AESTHETICIAN, ZombieAesthetician.createAttributes());
                FabricDefaultAttributeRegistry.register(STEVE_NPC, PlayerSteveNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(ALEX_NPC, PlayerAlexNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(MODIFIED_NPC, PlayerModifiedNPC.createAttributes());
                FabricDefaultAttributeRegistry.register(FOG_CLONE, PlayerAlexNPC.createAttributes());

                FabricDefaultAttributeRegistry.register(FALLEN_ZOMBIE, FallenZombie.createAttributes());
                FabricDefaultAttributeRegistry.register(FALLEN_SKELETON, FallenSkeleton.createAttributes());
                FabricDefaultAttributeRegistry.register(FALLEN_SPIDER, FallenSpider.createAttributes());
                FabricDefaultAttributeRegistry.register(FALLEN_VILLAGER, FallenVillager.createAttributes());
                FabricDefaultAttributeRegistry.register(FALLEN_CREEPER, FallenCreeper.createAttributes());
                FabricDefaultAttributeRegistry.register(FALLEN_PHANTOM,FallenPhantom.createAttributes());

                FabricDefaultAttributeRegistry.register(THE_WORLD, StandEntity.createStandAttributes());

                FabricDefaultAttributeRegistry.register(THE_WORLD_ULTIMATE, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(STAR_PLATINUM, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(STAR_PLATINUM_BASEBALL, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(MAGICIANS_RED, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(MAGICIANS_RED_OVA, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(D4C, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(GREEN_DAY, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(RATT, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(SURVIVOR, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(SOFT_AND_WET, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(SOFT_AND_WET_DROWNED, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(SOFT_AND_WET_DEBUT, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(SOFT_AND_WET_KING, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(SOFT_AND_WET_KILLER_QUEEN, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(KILLER_QUEEN, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(JUSTICE, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(JUSTICE_PIRATE, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(DARK_MIRAGE, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(CINDERELLA, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(WALKING_HEART, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(DIVER_DOWN, StandEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(THE_GRATEFUL_DEAD, StandEntity.createStandAttributes());


                FabricDefaultAttributeRegistry.register(GROUND_HURRICANE, GroundHurricaneEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(GROUND_BUBBLE, GroundHurricaneEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(LIFE_TRACKER, LifeTrackerEntity.createStandAttributes());
                FabricDefaultAttributeRegistry.register(D4C_CLONE, D4CCloneEntity.createAttributes());

                /*Spawn Weights and Biomes*/
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.DESERT), MobCategory.CREATURE,
                        ModEntities.TERRIER_DOG, 2, 1, 1);
                 BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.TAIGA), MobCategory.CREATURE,
                         ModEntities.TERRIER_DOG, 1, 1, 3);

                /*Spawn Placement rules*/
                 SpawnPlacements.register(ModEntities.TERRIER_DOG, SpawnPlacements.Type.ON_GROUND,
                         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TerrierEntity::canSpawn);
        }
}
