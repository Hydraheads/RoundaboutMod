package net.hydra.jojomod.entity;


import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.substand.EncasementBubbleEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.visages.JojoNPCGoToClosestVillage;
import net.hydra.jojomod.entity.visages.mobs.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class ModEntities {
    /** Entities are referenced in these files, but forge and fabric need to update
     * these with their registration data separately.*/

    public static float justiceHeight = 2.05f;
    public static float justiceWidth = 0.65F;
    public static EntityType<TerrierEntity> TERRIER_DOG;
    public static EntityType<TheWorldEntity> THE_WORLD;
    public static EntityType<TheWorldEntity> THE_WORLD_ULTIMATE;
    public static EntityType<StarPlatinumEntity> STAR_PLATINUM;
    public static EntityType<JusticeEntity> JUSTICE;
    public static EntityType<MagiciansRedEntity> MAGICIANS_RED;
    public static EntityType<MagiciansRedEntity> MAGICIANS_RED_OVA;
    public static EntityType<D4CEntity> D4C;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET_KING;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET_DROWNED;
    public static EntityType<SoftAndWetEntity> SOFT_AND_WET_DEBUT;
    public static EntityType<KillerQueenEntity> KILLER_QUEEN;
    public static EntityType<CinderellaEntity> CINDERELLA;
    public static EntityType<JusticePirateEntity> JUSTICE_PIRATE;
    public static EntityType<DarkMirageEntity> DARK_MIRAGE;
    public static EntityType<StarPlatinumBaseballEntity> STAR_PLATINUM_BASEBALL;
    public static EntityType<HarpoonEntity> THROWN_HARPOON;
    public static EntityType<KnifeEntity> THROWN_KNIFE;
    public static EntityType<MatchEntity> THROWN_MATCH;
    public static EntityType<CrossfireHurricaneEntity> CROSSFIRE_HURRICANE;
    public static EntityType<LifeTrackerEntity> LIFE_TRACKER;
    public static EntityType<StandFireballEntity> STAND_FIREBALL;

    public static EntityType<GasolineCanEntity> GASOLINE_CAN;

    public static EntityType<GasolineSplatterEntity> GASOLINE_SPLATTER;

    public static EntityType<StandArrowEntity> STAND_ARROW;
    public static EntityType<ThrownObjectEntity> THROWN_OBJECT;
    public static EntityType<CinderellaVisageDisplayEntity> CINDERELLA_VISAGE_DISPLAY;
    public static EntityType<ConcealedFlameObjectEntity> CONCEALED_FLAME_OBJECT;
    public static EntityType<GroundHurricaneEntity> GROUND_HURRICANE;
    public static EntityType<SoftAndWetPlunderBubbleEntity> PLUNDER_BUBBLE;
    public static EntityType<SoftAndWetExplosiveBubbleEntity> EXPLOSIVE_BUBBLE;
    public static EntityType<EncasementBubbleEntity> ENCASEMENT_BUBBLE;
    public static EntityType<OVAEnyaNPC> OVA_ENYA;
    public static EntityType<EnyaNPC> ENYA;

    public static EntityType<JotaroNPC> JOTARO;

    public static EntityType<DIONPC> DIO;
    public static EntityType<AvdolNPC> AVDOL;
    public static EntityType<ValentineNPC> VALENTINE;
    public static EntityType<ParallelDiegoNPC> PARALLEL_DIEGO;
    public static EntityType<JosukePartEightNPC> JOSUKE_PART_EIGHT;
    public static EntityType<AyaNPC> AYA;
    public static EntityType<Aesthetician> AESTHETICIAN;
    public static EntityType<ZombieAesthetician> ZOMBIE_AESTHETICIAN;
    public static EntityType<PlayerSteveNPC> STEVE_NPC;
    public static EntityType<PlayerAlexNPC> ALEX_NPC;
    public static EntityType<PlayerModifiedNPC> MODIFIED_NPC;
    public static EntityType<FogCloneEntity> FOG_CLONE;
    public static EntityType<D4CCloneEntity> D4C_CLONE;
    public static EntityType<FallenZombie> FALLEN_ZOMBIE;
    public static EntityType<FallenSkeleton> FALLEN_SKELETON;
    public static EntityType<FallenSpider> FALLEN_SPIDER;
    public static EntityType<FallenVillager> FALLEN_VILLAGER;
    public static EntityType<FallenCreeper> FALLEN_CREEPER;
    public static final ResourceLocation HARPOON_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_harpoon.png");
    public static final ResourceLocation KNIFE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_knife.png");
    public static final ResourceLocation GASOLINE_CAN_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_gasoline_can.png");


}
