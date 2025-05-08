package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.visages.mobs.*;
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
    public static final RegistryObject<EntityType<OVAEnyaNPC>> OVA_ENYA =
            ENTITY_TYPES.register("jojo_npc_ova_enya", () ->
                    EntityType.Builder.of(OVAEnyaNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_ova_enya").toString())
            );
    public static final RegistryObject<EntityType<EnyaNPC>> ENYA =
            ENTITY_TYPES.register("jojo_npc_enya", () ->
                    EntityType.Builder.of(EnyaNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_enya").toString())
            );
    public static final RegistryObject<EntityType<JotaroNPC>> JOTARO =
            ENTITY_TYPES.register("jojo_npc_jotaro", () ->
                    EntityType.Builder.of(JotaroNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_jotaro").toString())
            );
    public static final RegistryObject<EntityType<AvdolNPC>> AVDOL =
            ENTITY_TYPES.register("jojo_npc_avdol", () ->
                    EntityType.Builder.of(AvdolNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_avdol").toString())
            );
    public static final RegistryObject<EntityType<DIONPC>> DIO =
            ENTITY_TYPES.register("jojo_npc_dio", () ->
                    EntityType.Builder.of(DIONPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_dio").toString())
            );
    public static final RegistryObject<EntityType<ParallelDiegoNPC>> PARALLEL_DIEGO =
            ENTITY_TYPES.register("jojo_npc_parallel_diego", () ->
                    EntityType.Builder.of(ParallelDiegoNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_parallel_diego").toString())
            );
    public static final RegistryObject<EntityType<JosukePartEightNPC>> JOSUKE_PART_EIGHT =
            ENTITY_TYPES.register("jojo_npc_josuke_part_eight", () ->
                    EntityType.Builder.of(JosukePartEightNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_josuke_part_eight").toString())
            );
    public static final RegistryObject<EntityType<AyaNPC>> AYA =
            ENTITY_TYPES.register("jojo_npc_aya", () ->
                    EntityType.Builder.of(AyaNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_aya").toString())
            );
    public static final RegistryObject<EntityType<Aesthetician>> AESTHETICIAN =
            ENTITY_TYPES.register("aesthetician", () ->
                    EntityType.Builder.of(Aesthetician::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "aesthetician").toString())
            );
    public static final RegistryObject<EntityType<ZombieAesthetician>> ZOMBIE_AESTHETICIAN =
            ENTITY_TYPES.register("zombie_aesthetician", () ->
                    EntityType.Builder.of(ZombieAesthetician::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "zombie_aesthetician").toString())
            );
    public static final RegistryObject<EntityType<ValentineNPC>> VALENTINE =
            ENTITY_TYPES.register("jojo_npc_valentine", () ->
                    EntityType.Builder.of(ValentineNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_valentine").toString())
            );
    public static final RegistryObject<EntityType<PlayerSteveNPC>> STEVE_NPC =
            ENTITY_TYPES.register("jojo_npc_steve", () ->
                    EntityType.Builder.of(PlayerSteveNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_steve").toString())
            );
    public static final RegistryObject<EntityType<PlayerAlexNPC>> ALEX_NPC =
            ENTITY_TYPES.register("jojo_npc_alex", () ->
                    EntityType.Builder.of(PlayerAlexNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_alex").toString())
            );
    public static final RegistryObject<EntityType<PlayerModifiedNPC>> MODIFIED_NPC =
            ENTITY_TYPES.register("jojo_npc_modified", () ->
                    EntityType.Builder.of(PlayerModifiedNPC::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "jojo_npc_modified").toString())
            );
    public static final RegistryObject<EntityType<FogCloneEntity>> FOG_CLONE =
            ENTITY_TYPES.register("fog_clone", () ->
                    EntityType.Builder.of(FogCloneEntity::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "fog_clone").toString())
            );
    public static final RegistryObject<EntityType<D4CCloneEntity>> D4C_CLONE =
            ENTITY_TYPES.register("d4c_clone", () ->
                    EntityType.Builder.of(D4CCloneEntity::new, MobCategory.MISC).sized(0.6f, 1.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "d4c_clone").toString())
            );
    public static final RegistryObject<EntityType<FallenZombie>> FALLEN_ZOMBIE =
            ENTITY_TYPES.register("fallen_zombie", () ->
                    EntityType.Builder.of(FallenZombie::new, MobCategory.MISC).sized(0.6F, 1.95F).clientTrackingRange(8).
                            build(new ResourceLocation(Roundabout.MOD_ID, "fallen_zombie").toString())
            );
    public static final RegistryObject<EntityType<FallenSkeleton>> FALLEN_SKELETON =
            ENTITY_TYPES.register("fallen_skeleton", () ->
                    EntityType.Builder.of(FallenSkeleton::new, MobCategory.MISC).sized(0.6F, 1.99F).clientTrackingRange(8).
                            build(new ResourceLocation(Roundabout.MOD_ID, "fallen_skeleton").toString())
            );
    public static final RegistryObject<EntityType<FallenSpider>> FALLEN_SPIDER =
            ENTITY_TYPES.register("fallen_spider", () ->
                    EntityType.Builder.of(FallenSpider::new, MobCategory.MISC).sized(1.4f, 0.9f).clientTrackingRange(8).
                            build(new ResourceLocation(Roundabout.MOD_ID, "fallen_spider").toString())
            );
    public static final RegistryObject<EntityType<FallenVillager>> FALLEN_VILLAGER =
            ENTITY_TYPES.register("fallen_villager", () ->
                    EntityType.Builder.of(FallenVillager::new, MobCategory.MISC).sized(0.6F, 1.95F).clientTrackingRange(8).
                            build(new ResourceLocation(Roundabout.MOD_ID, "fallen_villager").toString())
            );
    public static final RegistryObject<EntityType<FallenCreeper>> FALLEN_CREEPER =
            ENTITY_TYPES.register("fallen_creeper", () ->
                    EntityType.Builder.of(FallenCreeper::new, MobCategory.MISC).sized(0.6F, 1.7F).clientTrackingRange(8).
                            build(new ResourceLocation(Roundabout.MOD_ID, "fallen_creeper").toString())
            );
    public static final RegistryObject<EntityType<TheWorldEntity>> THE_WORLD =
            ENTITY_TYPES.register("the_world", () ->
                    EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "the_world").toString())
            );
    public static final RegistryObject<EntityType<StarPlatinumEntity>> STAR_PLATINUM =
            ENTITY_TYPES.register("star_platinum", () ->
                    EntityType.Builder.of(StarPlatinumEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "star_platinum").toString())
            );
    public static final RegistryObject<EntityType<JusticeEntity>> JUSTICE =
            ENTITY_TYPES.register("justice", () ->
                    EntityType.Builder.of(JusticeEntity::new, MobCategory.MISC).sized(ModEntities.justiceWidth, ModEntities.justiceHeight).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "justice").toString())
            );
    public static final RegistryObject<EntityType<MagiciansRedEntity>> MAGICIANS_RED =
            ENTITY_TYPES.register("magicians_red", () ->
                    EntityType.Builder.of(MagiciansRedEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "magicians_red").toString())
            );
    public static final RegistryObject<EntityType<MagiciansRedEntity>> MAGICIANS_RED_OVA =
            ENTITY_TYPES.register("magicians_red_ova", () ->
                    EntityType.Builder.of(MagiciansRedEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "magicians_red_ova").toString())
            );
    public static final RegistryObject<EntityType<D4CEntity>> D4C =
            ENTITY_TYPES.register("d4c", () ->
                    EntityType.Builder.of(D4CEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "d4c").toString())
            );
    public static final RegistryObject<EntityType<SoftAndWetEntity>> SOFT_AND_WET =
            ENTITY_TYPES.register("soft_and_wet", () ->
                    EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "soft_and_wet").toString())
            );
    public static final RegistryObject<EntityType<SoftAndWetEntity>> SOFT_AND_WET_DROWNED =
            ENTITY_TYPES.register("soft_and_wet_drowned", () ->
                    EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "soft_and_wet_drowned").toString())
            );
    public static final RegistryObject<EntityType<SoftAndWetEntity>> SOFT_AND_WET_KING =
            ENTITY_TYPES.register("soft_and_wet_king", () ->
                    EntityType.Builder.of(SoftAndWetEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "soft_and_wet_king").toString())
            );
    public static final RegistryObject<EntityType<KillerQueenEntity>> KILLER_QUEEN =
            ENTITY_TYPES.register("killer_queen", () ->
                    EntityType.Builder.of(KillerQueenEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "d4c").toString())
            );
    public static final RegistryObject<EntityType<CinderellaEntity>> CINDERELLA =
            ENTITY_TYPES.register("cinderella", () ->
                    EntityType.Builder.of(CinderellaEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "cinderella").toString())
            );
    public static final RegistryObject<EntityType<JusticePirateEntity>> JUSTICE_PIRATE =
            ENTITY_TYPES.register("justice_pirate", () ->
                    EntityType.Builder.of(JusticePirateEntity::new, MobCategory.MISC).sized(ModEntities.justiceWidth, ModEntities.justiceHeight).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "justice_pirate").toString())
            );
    public static final RegistryObject<EntityType<DarkMirageEntity>> DARK_MIRAGE =
            ENTITY_TYPES.register("dark_mirage", () ->
                    EntityType.Builder.of(DarkMirageEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "dark_mirage").toString())
            );
    public static final RegistryObject<EntityType<StarPlatinumBaseballEntity>> STAR_PLATINUM_BASEBALL =
            ENTITY_TYPES.register("star_platinum_baseball", () ->
                    EntityType.Builder.of(StarPlatinumBaseballEntity::new, MobCategory.MISC).sized(0.75F, 2.05f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "star_platinum_baseball").toString())
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
    public static final RegistryObject<EntityType<CrossfireHurricaneEntity>> CROSSFIRE_HURRICANE =
            ENTITY_TYPES.register("crossfire_hurricane", () ->
                    EntityType.Builder.<CrossfireHurricaneEntity>of(CrossfireHurricaneEntity::new, MobCategory.MISC).sized(1.5f, 1.5f).
                            clientTrackingRange(15).
                            build(new ResourceLocation(Roundabout.MOD_ID, "crossfire_hurricane").toString())
            );
    public static final RegistryObject<EntityType<LifeTrackerEntity>> LIFE_TRACKER =
            ENTITY_TYPES.register("life_tracker", () ->
                    EntityType.Builder.<LifeTrackerEntity>of(LifeTrackerEntity::new, MobCategory.MISC).sized(1f, 1f).
                            clientTrackingRange(15).
                            build(new ResourceLocation(Roundabout.MOD_ID, "life_tracker").toString())
            );
    public static final RegistryObject<EntityType<StandFireballEntity>> STAND_FIREBALL =
            ENTITY_TYPES.register("stand_fireball", () ->
                    EntityType.Builder.<StandFireballEntity>of(StandFireballEntity::new, MobCategory.MISC).sized(0.9F, 0.9F).
                            clientTrackingRange(15).
                            build(new ResourceLocation(Roundabout.MOD_ID, "stand_fireball").toString())
            );
    public static final RegistryObject<EntityType<GasolineSplatterEntity>> GASOLINE_SPLATTER =
            ENTITY_TYPES.register("gasoline_splatter", () ->
                    EntityType.Builder.<GasolineSplatterEntity>of(GasolineSplatterEntity::new, MobCategory.MISC).sized(0.8f, 0.8f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "gasoline_splatter").toString())
            );
    public static final RegistryObject<EntityType<StandArrowEntity>> STAND_ARROW =
            ENTITY_TYPES.register("stand_arrow", () ->
                    EntityType.Builder.<StandArrowEntity>of(StandArrowEntity::new, MobCategory.MISC).sized(0.7f, 0.7f).
                            clientTrackingRange(6).
                            build(new ResourceLocation(Roundabout.MOD_ID, "stand_arrow").toString())
            );
    public static final RegistryObject<EntityType<ThrownObjectEntity>> THROWN_OBJECT =
            ENTITY_TYPES.register("thrown_object", () ->
                    EntityType.Builder.<ThrownObjectEntity>of(ThrownObjectEntity::new, MobCategory.MISC).sized(1f, 1f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "thrown_object").toString())
            );
    public static final RegistryObject<EntityType<ConcealedFlameObjectEntity>> CONCEALED_FLAME_OBJECT =
            ENTITY_TYPES.register("concealed_flame_object", () ->
                    EntityType.Builder.<ConcealedFlameObjectEntity>of(ConcealedFlameObjectEntity::new, MobCategory.MISC).sized(1f, 1f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "concealed_flame_object").toString())
            );
    public static final RegistryObject<EntityType<CinderellaVisageDisplayEntity>> CINDERELLA_VISAGE_DISPLAY =
            ENTITY_TYPES.register("visage_display", () ->
                    EntityType.Builder.<CinderellaVisageDisplayEntity>of(CinderellaVisageDisplayEntity::new, MobCategory.MISC).sized(1f, 1f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "visage_display").toString())
            );
    public static final RegistryObject<EntityType<GroundHurricaneEntity>> GROUND_HURRICANE =
            ENTITY_TYPES.register("ground_hurricane", () ->
                    EntityType.Builder.<GroundHurricaneEntity>of(GroundHurricaneEntity::new, MobCategory.MISC).sized(0.2f, 0.2f).
                            clientTrackingRange(10).
                            build(new ResourceLocation(Roundabout.MOD_ID, "ground_hurricane").toString())
            );
}
