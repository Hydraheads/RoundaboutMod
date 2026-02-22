package net.hydra.jojomod.entity;


import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.entity.mobs.TerrierEntity;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.paintings.BirthOfVenusPainting;
import net.hydra.jojomod.entity.paintings.MonaLisaPainting;
import net.hydra.jojomod.entity.paintings.VanGoughPainting;
import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.entity.pathfinding.GroundBubbleEntity;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.substand.EncasementBubbleEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.substand.SeperatedArmEntity;
import net.hydra.jojomod.entity.substand.SeperatedLegsEntity;
import net.hydra.jojomod.entity.visages.mobs.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.joml.Vector2f;

public class ModEntities {
    /** Entities are referenced in these files, but forge and fabric need to update
     * these with their registration data separately.*/

    public static float justiceHeight = 0.65f;
    public static float justiceWidth = 0.65F;
    public static EntityType<TerrierEntity> TERRIER_DOG;
    public static EntityType<AnubisGuardian> ANUBIS_GUARDIAN;
    public static EntityType<TheWorldEntity> THE_WORLD;
    public static EntityType<TheWorldEntity> THE_WORLD_ULTIMATE;
    public static EntityType<StarPlatinumEntity> STAR_PLATINUM;
    public static EntityType<JusticeEntity> JUSTICE;
    public static EntityType<MagiciansRedEntity> MAGICIANS_RED;
    public static EntityType<MagiciansRedEntity> MAGICIANS_RED_OVA;
    public static EntityType<RattEntity> RATT;
    public static EntityType<ReddEntity> REDD;
    public static EntityType<ChairRattEntity> CHAIR_RATT;
    public static EntityType<FleshPileEntity> FLESH_PILE;
    public static EntityType<D4CEntity> D4C;
    public static EntityType<CreamEntity> CREAM;
    public static EntityType<SurvivorEntity> SURVIVOR;
    public static EntityType<GreenDayEntity> GREEN_DAY;
    public static EntityType<SeperatedLegsEntity> SEPERATED_LEGS;
    public static EntityType<SeperatedArmEntity> SEPERATED_ARM;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET_KING;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET_DROWNED;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET_DEBUT;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET_KILLER_QUEEN;
    public static EntityType<KillerQueenEntity> KILLER_QUEEN;
    public static EntityType<CinderellaEntity> CINDERELLA;
    public static EntityType<WalkingHeartEntity> WALKING_HEART;
    public static EntityType<JusticePirateEntity> JUSTICE_PIRATE;
    public static EntityType<DarkMirageEntity> DARK_MIRAGE;
    public static EntityType<StarPlatinumBaseballEntity> STAR_PLATINUM_BASEBALL;
    public static EntityType<DiverDownEntity> DIVER_DOWN;
    public static EntityType<HarpoonEntity> THROWN_HARPOON;
    public static EntityType<BladedBowlerHatEntity> BLADED_BOWLER_HAT;
    public static EntityType<RoundaboutBulletEntity> ROUNDABOUT_BULLET_ENTITY;
    public static EntityType<RattDartEntity> RATT_DART;
    public static EntityType<KnifeEntity> THROWN_KNIFE;
    public static EntityType<MetallicaKnifeEntity> METALLICA_KNIFE;
    public static EntityType<MatchEntity> THROWN_MATCH;
    public static EntityType<ThrownWaterBottleEntity> THROWN_WATER_BOTTLE;
    public static EntityType<CrossfireHurricaneEntity> CROSSFIRE_HURRICANE;
    public static EntityType<LifeTrackerEntity> LIFE_TRACKER;
    public static EntityType<StandFireballEntity> STAND_FIREBALL;
    public static EntityType<EvilAuraProjectile> EVIL_AURA_PROJECTILE;
    public static EntityType<RipperEyesProjectile> RIPPER_EYES_PROJECTILE;

    public static EntityType<GasolineCanEntity> GASOLINE_CAN;
    public static EntityType<RoadRollerEntity> ROAD_ROLLER_ENTITY;

    public static EntityType<GasolineSplatterEntity> GASOLINE_SPLATTER;
    public static EntityType<BloodSplatterEntity> BLOOD_SPLATTER;

    public static EntityType<StandArrowEntity> STAND_ARROW;
    public static EntityType<ThrownObjectEntity> THROWN_OBJECT;
    public static EntityType<ThrownAnubisEntity> THROWN_ANUBIS;
    public static EntityType<CinderellaVisageDisplayEntity> CINDERELLA_VISAGE_DISPLAY;
    public static EntityType<ConcealedFlameObjectEntity> CONCEALED_FLAME_OBJECT;
    public static EntityType<GroundHurricaneEntity> GROUND_HURRICANE;
    public static EntityType<GroundBubbleEntity> GROUND_BUBBLE;
    public static EntityType<SoftAndWetPlunderBubbleEntity> PLUNDER_BUBBLE;
    public static EntityType<SoftAndWetExplosiveBubbleEntity> EXPLOSIVE_BUBBLE;
    public static EntityType<SoftAndWetItemLaunchingBubbleEntity> ITEM_LAUNCHING_BUBBLE_ENTITY;
    public static EntityType<AnubisPossessorEntity> ANUBIS_POSSESSOR;
    public static EntityType<AnubisSlipstreamEntity> ANUBIS_SLIPSTREAM;

    public static EntityType<GoBeyondEntity> GO_BEYOND;
    public static EntityType<EncasementBubbleEntity> ENCASEMENT_BUBBLE;
    public static EntityType<OVAEnyaNPC> OVA_ENYA;
    public static EntityType<EnyaNPC> ENYA;

    public static EntityType<JotaroNPC> JOTARO;

    public static EntityType<VanGoughPainting> VAN_GOUGH_PAINTING;
    public static EntityType<MonaLisaPainting> MONA_LISA_PAINTING;
    public static EntityType<BirthOfVenusPainting> BIRTH_OF_VENUS_PAINTING;

    public static EntityType<DIONPC> DIO;
    public static EntityType<AvdolNPC> AVDOL;
    public static EntityType<ValentineNPC> VALENTINE;
    public static EntityType<ParallelDiegoNPC> PARALLEL_DIEGO;
    public static EntityType<JosukePartEightNPC> JOSUKE_PART_EIGHT;
    public static EntityType<AyaNPC> AYA;
    public static EntityType<Aesthetician> AESTHETICIAN;
    public static EntityType<ZombieAesthetician> ZOMBIE_AESTHETICIAN;
    public static EntityType<PocolocoNPC> POCOLOCO;
    public static EntityType<GuccioNPC> GUCCIO;
    public static EntityType<RingoNPC> RINGO;
    public static EntityType<HatoNPC> HATO;
    public static EntityType<ShizukaNPC> SHIZUKA;
    public static EntityType<PlayerSteveNPC> STEVE_NPC;
    public static EntityType<PlayerAlexNPC> ALEX_NPC;
    public static EntityType<PlayerModifiedNPC> MODIFIED_NPC;
    public static EntityType<FogCloneEntity> FOG_CLONE;
    public static EntityType<D4CCloneEntity> D4C_CLONE;
    public static EntityType<FallenZombie> FALLEN_ZOMBIE;
    public static EntityType<FallenSkeleton> FALLEN_SKELETON;
    public static EntityType<FallenSpider> FALLEN_SPIDER;
    public static EntityType<FallenVillager> FALLEN_VILLAGER;
    public static EntityType<FallenPhantom> FALLEN_PHANTOM;
    public static EntityType<FallenCreeper> FALLEN_CREEPER;
    public static final ResourceLocation HARPOON_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_harpoon.png");
    public static final ResourceLocation SNUBNOSE_IN_HAND_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/item/snubnose_revolver.png");
    public static final ResourceLocation TOMMY_GUN_IN_HAND_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/item/tommy_gun.png");
    public static final ResourceLocation COLT_REVOLVER_IN_HAND_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/item/colt_revolver.png");
    public static final ResourceLocation JACKAL_RIFLE_IN_HAND_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/item/jackal_rifle.png");
    public static final ResourceLocation BLADED_BOWLER_HAT_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/bladed_bowler_hat.png");
    public static final ResourceLocation ROUNDABOUT_BULLET_ENTITY_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/roundabout_bullet.png");
    public static final ResourceLocation KNIFE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_knife.png");
    public static final ResourceLocation RATT_DART_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/ratt_dart.png");
    public static final ResourceLocation GASOLINE_CAN_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_gasoline_can.png");

    public static final ResourceLocation ROAD_ROLLER_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/road_roller.png");
    public static final ResourceLocation ROAD_ROLLER_TEXTURE_CRACKED_MEDIUM = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/road_roller_cracked_medium.png");
    public static final ResourceLocation ROAD_ROLLER_TEXTURE_CRACKED_HIGH = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/road_roller_cracked_high.png");


    /// Creates and registers the stand entity
    /// @param location The resource location to register the entity at (support for addons)
    /// @param factory The entity constructor. Usually entity::new
    /// @param size The entity size. Usually <code>new Vector2f(0.75f, 2.05f)</code>
    /// @return The stand entity
    public static <T extends StandEntity> EntityType<T> registerStandEntity(ResourceLocation location, EntityType.EntityFactory<T> factory, Vector2f size)
    {
        return Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                location,
                EntityType.Builder.of(factory, MobCategory.MISC).
                        sized(size.x(), size.y()).clientTrackingRange(14).build(location.toString())
        );
    }
}
