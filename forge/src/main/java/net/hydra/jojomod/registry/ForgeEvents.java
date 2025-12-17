package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.Utils.ForgeBrewingRecipes;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StreetSignBlock;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.TerrierEntity;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.entity.pathfinding.GroundBubbleEntity;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.stand.CreamEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.substand.SeperatedLegsEntity;
import net.hydra.jojomod.entity.visages.mobs.*;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeEvents {

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ForgeEntities.TERRIER_DOG.get(), Wolf.createAttributes().build());
        event.put(ForgeEntities.THE_WORLD.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.THE_WORLD_ULTIMATE.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.STAR_PLATINUM.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.STAR_PLATINUM_BASEBALL.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.JUSTICE.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.JUSTICE_PIRATE.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.D4C.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.GREEN_DAY.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.RATT.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.REDD.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.CHAIR_RAT.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_KING.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_DROWNED.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_DEBUT.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_KILLER_QUEEN.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.KILLER_QUEEN.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.CINDERELLA.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.WALKING_HEART.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.CREAM.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.MAGICIANS_RED.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.MAGICIANS_RED_OVA.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SURVIVOR.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.DARK_MIRAGE.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.DIVER_DOWN.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.OVA_ENYA.get(), OVAEnyaNPC.createAttributes().build());
        event.put(ForgeEntities.ENYA.get(), OVAEnyaNPC.createAttributes().build());
        event.put(ForgeEntities.JOTARO.get(), JotaroNPC.createAttributes().build());
        event.put(ForgeEntities.AVDOL.get(), AvdolNPC.createAttributes().build());
        event.put(ForgeEntities.DIO.get(), DIONPC.createAttributes().build());
        event.put(ForgeEntities.PARALLEL_DIEGO.get(), ParallelDiegoNPC.createAttributes().build());
        event.put(ForgeEntities.JOSUKE_PART_EIGHT.get(), JosukePartEightNPC.createAttributes().build());
        event.put(ForgeEntities.AYA.get(), AyaNPC.createAttributes().build());
        event.put(ForgeEntities.AESTHETICIAN.get(), Aesthetician.createAttributes().build());
        event.put(ForgeEntities.ZOMBIE_AESTHETICIAN.get(), ZombieAesthetician.createAttributes().build());
        event.put(ForgeEntities.RINGO.get(), RingoNPC.createAttributes().build());
        event.put(ForgeEntities.HATO.get(), HatoNPC.createAttributes().build());
        event.put(ForgeEntities.SHIZUKA.get(), ShizukaNPC.createAttributes().build());
        event.put(ForgeEntities.POCOLOCO.get(), PocolocoNPC.createAttributes().build());
        event.put(ForgeEntities.GUCCIO.get(), GuccioNPC.createAttributes().build());
        event.put(ForgeEntities.VALENTINE.get(), ValentineNPC.createAttributes().build());
        event.put(ForgeEntities.STEVE_NPC.get(), PlayerSteveNPC.createAttributes().build());
        event.put(ForgeEntities.ALEX_NPC.get(), PlayerAlexNPC.createAttributes().build());
        event.put(ForgeEntities.MODIFIED_NPC.get(), PlayerAlexNPC.createAttributes().build());
        event.put(ForgeEntities.FOG_CLONE.get(), PlayerAlexNPC.createAttributes().build());
        event.put(ForgeEntities.FALLEN_ZOMBIE.get(), FallenZombie.createAttributes().build());
        event.put(ForgeEntities.FALLEN_SKELETON.get(), FallenSkeleton.createAttributes().build());
        event.put(ForgeEntities.FALLEN_SPIDER.get(), FallenSpider.createAttributes().build());
        event.put(ForgeEntities.FALLEN_VILLAGER.get(), FallenVillager.createAttributes().build());
        event.put(ForgeEntities.FALLEN_CREEPER.get(), FallenCreeper.createAttributes().build());
        event.put(ForgeEntities.FALLEN_PHANTOM.get(), FallenPhantom.createAttributes().build());
        event.put(ForgeEntities.GROUND_HURRICANE.get(), GroundHurricaneEntity.createStandAttributes().build());
        event.put(ForgeEntities.ANUBIS_POSSESSOR.get(), AnubisPossessorEntity.createStandAttributes().build());
        event.put(ForgeEntities.GROUND_BUBBLE.get(), GroundBubbleEntity.createStandAttributes().build());
        event.put(ForgeEntities.LIFE_TRACKER.get(), LifeTrackerEntity.createStandAttributes().build());
        event.put(ForgeEntities.D4C_CLONE.get(), D4CCloneEntity.createAttributes().build());
        event.put(ForgeEntities.ROAD_ROLLER_ENTITY.get(), RoadRollerEntity.createAttributes().build());
        event.put(ForgeEntities.SEPERATED_LEGS.get(), SeperatedLegsEntity.createStandAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                ForgeEntities.TERRIER_DOG.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                TerrierEntity::canSpawn,
                SpawnPlacementRegisterEvent.Operation.OR
        );
    }

    @SubscribeEvent
    public static void registerRoundaboutBridge(FMLCommonSetupEvent event){
        /*Common Code Bridge*/

        ModBlocks.METEOR_BLOCK = ForgeBlocks.METEOR_BLOCK.get();
        ModBlocks.IMPACT_MOUND = ForgeBlocks.IMPACT_MOUND.get();
        ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK = ForgeBlocks.EQUIPPABLE_STONE_MASK_BLOCK.get();
        ModBlocks.BLOODY_STONE_MASK_BLOCK = ForgeBlocks.BLOODY_STONE_MASK_BLOCK.get();
        ModBlocks.REGAL_FLOOR = ForgeBlocks.REGAL_FLOOR.get();
        ModBlocks.WOODEN_MANOR_TABLE = ForgeBlocks.WOODEN_MANOR_TABLE.get();
        ModBlocks.WOODEN_MANOR_CHAIR = ForgeBlocks.WOODEN_MANOR_CHAIR.get();
        ModBlocks.WOOL_SLAB_WHITE = ForgeBlocks.WOOL_SLAB_WHITE.get();
        ModBlocks.WOOL_STAIRS_WHITE = ForgeBlocks.WOOL_STAIRS_WHITE.get();
        ModBlocks.WOOL_SLAB_BLACK = ForgeBlocks.WOOL_SLAB_BLACK.get();
        ModBlocks.WOOL_STAIRS_BLACK = ForgeBlocks.WOOL_STAIRS_BLACK.get();
        ModBlocks.WOOL_SLAB_BLUE = ForgeBlocks.WOOL_SLAB_BLUE.get();
        ModBlocks.WOOL_STAIRS_BLUE = ForgeBlocks.WOOL_STAIRS_BLUE.get();
        ModBlocks.WOOL_SLAB_BROWN = ForgeBlocks.WOOL_SLAB_BROWN.get();
        ModBlocks.WOOL_STAIRS_BROWN = ForgeBlocks.WOOL_STAIRS_BROWN.get();
        ModBlocks.WOOL_SLAB_CYAN = ForgeBlocks.WOOL_SLAB_CYAN.get();
        ModBlocks.WOOL_STAIRS_CYAN = ForgeBlocks.WOOL_STAIRS_CYAN.get();
        ModBlocks.WOOL_SLAB_DARK_GREEN = ForgeBlocks.WOOL_SLAB_DARK_GREEN.get();
        ModBlocks.WOOL_STAIRS_DARK_GREEN = ForgeBlocks.WOOL_STAIRS_DARK_GREEN.get();
        ModBlocks.WOOL_SLAB_DARK_GREY = ForgeBlocks.WOOL_SLAB_DARK_GREY.get();
        ModBlocks.WOOL_STAIRS_DARK_GREY = ForgeBlocks.WOOL_STAIRS_DARK_GREY.get();
        ModBlocks.WOOL_SLAB_GREEN = ForgeBlocks.WOOL_SLAB_GREEN.get();
        ModBlocks.WOOL_STAIRS_GREEN = ForgeBlocks.WOOL_STAIRS_GREEN.get();
        ModBlocks.WOOL_SLAB_LIGHT_BLUE = ForgeBlocks.WOOL_SLAB_LIGHT_BLUE.get();
        ModBlocks.WOOL_STAIRS_LIGHT_BLUE = ForgeBlocks.WOOL_STAIRS_LIGHT_BLUE.get();
        ModBlocks.WOOL_SLAB_LIGHT_GREY = ForgeBlocks.WOOL_SLAB_LIGHT_GREY.get();
        ModBlocks.WOOL_STAIRS_LIGHT_GREY = ForgeBlocks.WOOL_STAIRS_LIGHT_GREY.get();
        ModBlocks.WOOL_SLAB_MAGENTA = ForgeBlocks.WOOL_SLAB_MAGENTA.get();
        ModBlocks.WOOL_STAIRS_MAGENTA = ForgeBlocks.WOOL_STAIRS_MAGENTA.get();
        ModBlocks.WOOL_SLAB_ORANGE = ForgeBlocks.WOOL_SLAB_ORANGE.get();
        ModBlocks.WOOL_STAIRS_ORANGE = ForgeBlocks.WOOL_STAIRS_ORANGE.get();
        ModBlocks.WOOL_SLAB_PURPLE = ForgeBlocks.WOOL_SLAB_PURPLE.get();
        ModBlocks.WOOL_STAIRS_PURPLE = ForgeBlocks.WOOL_STAIRS_PURPLE.get();
        ModBlocks.WOOL_SLAB_PINK = ForgeBlocks.WOOL_SLAB_PINK.get();
        ModBlocks.WOOL_STAIRS_PINK = ForgeBlocks.WOOL_STAIRS_PINK.get();
        ModBlocks.WOOL_SLAB_RED = ForgeBlocks.WOOL_SLAB_RED.get();
        ModBlocks.WOOL_STAIRS_RED = ForgeBlocks.WOOL_STAIRS_RED.get();
        ModBlocks.WOOL_SLAB_YELLOW = ForgeBlocks.WOOL_SLAB_YELLOW.get();
        ModBlocks.WOOL_STAIRS_YELLOW = ForgeBlocks.WOOL_STAIRS_YELLOW.get();
        ModBlocks.GLASS_DOOR = ForgeBlocks.GLASS_DOOR.get();
        ModBlocks.WALL_LANTERN = ForgeBlocks.WALL_LANTERN.get();
        ModBlocks.REGAL_WALL = ForgeBlocks.REGAL_WALL.get();
        ModBlocks.CULTIVATION_POT = ForgeBlocks.CULTIVATION_POT.get();
        ModBlocks.CULTIVATED_CHERRY_SAPLING = ForgeBlocks.CULTIVATED_CHERRY_SAPLING.get();
        ModBlocks.CULTIVATED_OAK_SAPLING = ForgeBlocks.CULTIVATED_OAK_SAPLING.get();
        ModBlocks.CULTIVATED_LOCACACA = ForgeBlocks.CULTIVATED_LOCACACA.get();
        ModBlocks.ANCIENT_METEOR = ForgeBlocks.ANCIENT_METEOR.get();
        ModBlocks.LOCACACA_CACTUS = ForgeBlocks.LOCACACA_CACTUS.get();
        ModBlocks.LOCACACA_BLOCK = ForgeBlocks.LOCACACA_BLOCK.get();
        ModBlocks.NEW_LOCACACA_BLOCK = ForgeBlocks.NEW_LOCACACA_BLOCK.get();
        ModBlocks.GASOLINE_SPLATTER = ForgeBlocks.GASOLINE_SPLATTER.get();
        ModBlocks.BLOOD_SPLATTER = ForgeBlocks.BLOOD_SPLATTER.get();
        ModBlocks.BLUE_BLOOD_SPLATTER = ForgeBlocks.BLUE_BLOOD_SPLATTER.get();
        ModBlocks.ENDER_BLOOD_SPLATTER = ForgeBlocks.ENDER_BLOOD_SPLATTER.get();
        ModBlocks.WIRE_TRAP = ForgeBlocks.WIRE_TRAP.get();
        ModBlocks.BARBED_WIRE = ForgeBlocks.BARBED_WIRE.get();
        ModBlocks.BARBED_WIRE_BUNDLE = ForgeBlocks.BARBED_WIRE_BUNDLE.get();
        ModBlocks.GODDESS_STATUE_BLOCK = ForgeBlocks.GODDESS_STATUE_BLOCK.get();
        ModBlocks.STREET_SIGN_DIO = ForgeBlocks.STREET_SIGN_DIO.get();
        ModBlocks.STREET_SIGN_RIGHT = ForgeBlocks.STREET_SIGN_RIGHT.get();
        ModBlocks.STREET_SIGN_STOP = ForgeBlocks.STREET_SIGN_STOP.get();
        ModBlocks.STREET_SIGN_YIELD = ForgeBlocks.STREET_SIGN_YIELD.get();
        ModBlocks.STREET_SIGN_DANGER = ForgeBlocks.STREET_SIGN_DANGER.get();
        ModBlocks.WALL_STREET_SIGN_DIO = ForgeBlocks.WALL_STREET_SIGN_DIO.get();
        ModBlocks.WALL_STREET_SIGN_RIGHT = ForgeBlocks.WALL_STREET_SIGN_RIGHT.get();
        ModBlocks.WALL_STREET_SIGN_STOP = ForgeBlocks.WALL_STREET_SIGN_STOP.get();
        ModBlocks.WALL_STREET_SIGN_YIELD = ForgeBlocks.WALL_STREET_SIGN_YIELD.get();
        ModBlocks.WALL_STREET_SIGN_DANGER = ForgeBlocks.WALL_STREET_SIGN_DANGER.get();
        ModBlocks.CEILING_LIGHT = ForgeBlocks.CEILING_LIGHT.get();
        ModBlocks.MIRROR = ForgeBlocks.MIRROR.get();
        ModBlocks.MINING_ALERT_BLOCK = ForgeBlocks.MINING_ALERT_BLOCK.get();
        ModBlocks.FLESH_BLOCK = ForgeBlocks.FLESH_BLOCK.get();
        ModBlocks.FOG_DIRT_COATING = ForgeBlocks.FOG_DIRT_COATING.get();
        ModBlocks.FOG_DIRT = ForgeBlocks.FOG_DIRT.get();
        ModBlocks.FOG_TRAP = ForgeBlocks.FOG_TRAP.get();
        ModBlocks.STEREO = ForgeBlocks.STEREO.get();
        ModBlocks.STAND_FIRE = ForgeBlocks.STAND_FIRE.get();
        ModBlocks.BUBBLE_SCAFFOLD = ForgeBlocks.BUBBLE_SCAFFOLD.get();
        ModBlocks.INVISIBLOCK = ForgeBlocks.INVISIBLOCK.get();
        ModBlocks.D4C_LIGHT_BLOCK = ForgeBlocks.D4C_LIGHT_BLOCK.get();
        ModBlocks.MELON_PARFAIT = ForgeBlocks.MELON_PARFAIT.get();

        ModBlocks.ORANGE_FIRE = ForgeBlocks.ORANGE_FIRE.get();
        ModBlocks.BLUE_FIRE = ForgeBlocks.BLUE_FIRE.get();
        ModBlocks.PURPLE_FIRE = ForgeBlocks.PURPLE_FIRE.get();
        ModBlocks.GREEN_FIRE = ForgeBlocks.GREEN_FIRE.get();
        ModBlocks.DREAD_FIRE = ForgeBlocks.DREAD_FIRE.get();
        ModBlocks.CREAM_FIRE = ForgeBlocks.CREAM_FIRE.get();

        ModBlocks.STEREO_BLOCK_ENTITY = ForgeBlocks.STEREO_BLOCK_ENTITY.get();
        ModBlocks.MIRROR_BLOCK_ENTITY = ForgeBlocks.MIRROR_BLOCK_ENTITY.get();
        ModBlocks.STAND_FIRE_BLOCK_ENTITY = ForgeBlocks.STAND_FIRE_BLOCK_ENTITY.get();
        ModBlocks.BUBBLE_SCAFFOLD_BLOCK_ENTITY = ForgeBlocks.BUBBLE_SCAFFOLD_BLOCK_ENTITY.get();
        ModBlocks.INVISIBLE_BLOCK_ENTITY = ForgeBlocks.INVISIBLE_BLOCK_ENTITY.get();
        ModBlocks.D4C_LIGHT_BLOCK_ENTITY = ForgeBlocks.D4C_LIGHT_BLOCK_ENTITY.get();
        ModBlocks.FOG_TRAP_BLOCK_ENTITY = ForgeBlocks.FOG_TRAP_BLOCK_ENTITY.get();

        ModItems.STAND_ARROW = ForgeItems.STAND_ARROW.get();
        ModItems.WORTHY_ARROW = ForgeItems.WORTHY_ARROW.get();
        ModItems.STAND_BEETLE_ARROW = ForgeItems.STAND_BEETLE_ARROW.get();
        ModItems.LUCK_UPGRADE = ForgeItems.LUCK_UPGRADE.get();
        ModItems.EXECUTION_UPGRADE = ForgeItems.EXECUTION_UPGRADE.get();
        ModItems.HAIRSPRAY = ForgeItems.HAIRSPRAY.get();
        ModItems.LUCK_SWORD = ForgeItems.LUCK_SWORD.get();
        ModItems.WOODEN_GLAIVE = ForgeItems.WOODEN_GLAIVE.get();
        ModItems.STONE_GLAIVE = ForgeItems.STONE_GLAIVE.get();
        ModItems.IRON_GLAIVE = ForgeItems.IRON_GLAIVE.get();
        ModItems.GOLDEN_GLAIVE = ForgeItems.GOLDEN_GLAIVE.get();
        ModItems.DIAMOND_GLAIVE = ForgeItems.DIAMOND_GLAIVE.get();
        ModItems.NETHERITE_GLAIVE = ForgeItems.NETHERITE_GLAIVE.get();
        ModItems.SCISSORS = ForgeItems.SCISSORS.get();
        ModItems.SACRIFICIAL_DAGGER = ForgeItems.SACRIFICIAL_DAGGER.get();
        ModItems.EXECUTIONER_AXE = ForgeItems.EXECUTIONER_AXE.get();
        ModItems.BOWLER_HAT = ForgeItems.BOWLER_HAT.get();
        ModItems.BODY_BAG = ForgeItems.BODY_BAG.get();
        ModItems.CREATIVE_BODY_BAG = ForgeItems.CREATIVE_BODY_BAG.get();
        ModItems.HARPOON = ForgeItems.HARPOON.get();
        ModItems.KNIFE = ForgeItems.KNIFE.get();
        ModItems.KNIFE_BUNDLE = ForgeItems.KNIFE_BUNDLE.get();
        ModItems.MATCH = ForgeItems.MATCH.get();
        ModItems.MATCH_BUNDLE = ForgeItems.MATCH_BUNDLE.get();
        ModItems.GASOLINE_CAN = ForgeItems.GASOLINE_CAN.get();
        ModItems.GASOLINE_BUCKET = ForgeItems.GASOLINE_BUCKET.get();
        ModItems.ROAD_ROLLER = ForgeItems.ROAD_ROLLER.get();
        ModItems.STAND_DISC = ForgeItems.STAND_DISC.get();

        ModItems.STAND_DISC_THE_WORLD = ForgeItems.STAND_DISC_THE_WORLD.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_THE_WORLD.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_THE_WORLD.get());
        ModItems.MAX_STAND_DISC_THE_WORLD = ForgeItems.MAX_STAND_DISC_THE_WORLD.get();

        ModItems.STAND_DISC_STAR_PLATINUM = ForgeItems.STAND_DISC_STAR_PLATINUM.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_STAR_PLATINUM.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_STAR_PLATINUM.get());
        ModItems.MAX_STAND_DISC_STAR_PLATINUM = ForgeItems.MAX_STAND_DISC_STAR_PLATINUM.get();

        ModItems.STAND_DISC_JUSTICE = ForgeItems.STAND_DISC_JUSTICE.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_JUSTICE.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_JUSTICE.get());
        ModItems.MAX_STAND_DISC_JUSTICE = ForgeItems.MAX_STAND_DISC_JUSTICE.get();

        ModItems.STAND_DISC_MAGICIANS_RED = ForgeItems.STAND_DISC_MAGICIANS_RED.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_MAGICIANS_RED.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_MAGICIANS_RED.get());
        ModItems.MAX_STAND_DISC_MAGICIANS_RED = ForgeItems.MAX_STAND_DISC_MAGICIANS_RED.get();

        ModItems.STAND_DISC_KILLER_QUEEN = ForgeItems.STAND_DISC_KILLER_QUEEN.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_KILLER_QUEEN.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_KILLER_QUEEN.get());
        ModItems.MAX_STAND_DISC_KILLER_QUEEN = ForgeItems.MAX_STAND_DISC_KILLER_QUEEN.get();

        ModItems.STAND_DISC_D4C = ForgeItems.STAND_DISC_D4C.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_D4C.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_D4C.get());
        ModItems.MAX_STAND_DISC_D4C = ForgeItems.MAX_STAND_DISC_D4C.get();

        ModItems.STAND_DISC_GREEN_DAY = ForgeItems.STAND_DISC_GREEN_DAY.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_GREEN_DAY.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_GREEN_DAY.get());
        ModItems.MAX_STAND_DISC_D4C = ForgeItems.MAX_STAND_DISC_GREEN_DAY.get();

        ModItems.STAND_DISC_RATT = ForgeItems.STAND_DISC_RATT.get();
        ModItems.MAX_STAND_DISC_RATT = ForgeItems.MAX_STAND_DISC_RATT.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_RATT.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_RATT.get());

        ModItems.STAND_DISC_SOFT_AND_WET = ForgeItems.STAND_DISC_SOFT_AND_WET.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_SOFT_AND_WET.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_SOFT_AND_WET.get());
        ModItems.MAX_STAND_DISC_SOFT_AND_WET = ForgeItems.MAX_STAND_DISC_SOFT_AND_WET.get();

        ModItems.STAND_DISC_CINDERELLA = ForgeItems.STAND_DISC_CINDERELLA.get();
        ModItems.STAND_DISC_HEY_YA = ForgeItems.STAND_DISC_HEY_YA.get();
        ModItems.STAND_DISC_MANDOM = ForgeItems.STAND_DISC_MANDOM.get();
        ModItems.STAND_DISC_SURVIVOR = ForgeItems.STAND_DISC_SURVIVOR.get();
        ModItems.STAND_DISC_ACHTUNG = ForgeItems.STAND_DISC_ACHTUNG.get();

        ModItems.STAND_DISC_WALKING_HEART = ForgeItems.STAND_DISC_WALKING_HEART.get();
        ModItems.MAX_STAND_DISC_WALKING_HEART = ForgeItems.MAX_STAND_DISC_WALKING_HEART.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_WALKING_HEART.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_WALKING_HEART.get());

        ModItems.STAND_DISC_DIVER_DOWN = ForgeItems.STAND_DISC_DIVER_DOWN.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_DIVER_DOWN.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_DIVER_DOWN.get());
        ModItems.MAX_STAND_DISC_DIVER_DOWN = ForgeItems.MAX_STAND_DISC_DIVER_DOWN.get();

        ModItems.STAND_DISC_CREAM = ForgeItems.STAND_DISC_CREAM.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_CREAM.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_CREAM.get());
        ModItems.MAX_STAND_DISC_CREAM = ForgeItems.MAX_STAND_DISC_CREAM.get();

        ModItems.STAND_DISC_ANUBIS = ForgeItems.STAND_DISC_ANUBIS.get();
        ModItems.MAX_STAND_DISC_ANUBIS = ForgeItems.MAX_STAND_DISC_ANUBIS.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_ANUBIS.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_ANUBIS.get());

        ModItems.COFFEE_GUM = ForgeItems.COFFEE_GUM.get();
        ModItems.CHERRIES = ForgeItems.CHERRIES.get();

        ModItems.ANUBIS_ITEM = ForgeItems.ANUBIS_ITEM.get();

        ModItems.SNUBNOSE_REVOLVER = ForgeItems.SNUBNOSE_REVOLVER.get();
        ModItems.SNUBNOSE_AMMO = ForgeItems.SNUBNOSE_AMMO.get();

        ModItems.TOMMY_GUN = ForgeItems.TOMMY_GUN.get();
        ModItems.TOMMY_AMMO = ForgeItems.TOMMY_AMMO.get();

        ModItems.COLT_REVOLVER = ForgeItems.COLT_REVOLVER.get();

        ((StreetSignBlock)ForgeBlocks.STREET_SIGN_DIO.get()).referenceItem = ForgeItems.STREET_SIGN_DIO_BLOCK_ITEM.get().getDefaultInstance();
        ((StreetSignBlock)ForgeBlocks.STREET_SIGN_RIGHT.get()).referenceItem = ForgeItems.STREET_SIGN_RIGHT_BLOCK_ITEM.get().getDefaultInstance();
        ((StreetSignBlock)ForgeBlocks.STREET_SIGN_STOP.get()).referenceItem = ForgeItems.STREET_SIGN_STOP_BLOCK_ITEM.get().getDefaultInstance();
        ((StreetSignBlock)ForgeBlocks.STREET_SIGN_YIELD.get()).referenceItem = ForgeItems.STREET_SIGN_YIELD_BLOCK_ITEM.get().getDefaultInstance();
        ((StreetSignBlock)ForgeBlocks.STREET_SIGN_DANGER.get()).referenceItem = ForgeItems.STREET_SIGN_DANGER_BLOCK_ITEM.get().getDefaultInstance();
        ModItems.STREET_SIGN_DIO_BLOCK_ITEM = ForgeItems.STREET_SIGN_DIO_BLOCK_ITEM.get();
        ModItems.STREET_SIGN_RIGHT_BLOCK_ITEM = ForgeItems.STREET_SIGN_RIGHT_BLOCK_ITEM.get();
        ModItems.STREET_SIGN_STOP_BLOCK_ITEM = ForgeItems.STREET_SIGN_STOP_BLOCK_ITEM.get();
        ModItems.STREET_SIGN_YIELD_BLOCK_ITEM = ForgeItems.STREET_SIGN_YIELD_BLOCK_ITEM.get();
        ModItems.STREET_SIGN_DANGER_BLOCK_ITEM = ForgeItems.STREET_SIGN_DANGER_BLOCK_ITEM.get();
        ModItems.METEORITE = ForgeItems.METEORITE.get();
        ModItems.METEORITE_INGOT = ForgeItems.METEORITE_INGOT.get();
        ModItems.LOCACACA_PIT = ForgeItems.LOCACACA_PIT.get();
        ModItems.LIGHT_BULB = ForgeItems.LIGHT_BULB.get();
        ModItems.LOCACACA = ForgeItems.LOCACACA.get();
        ModItems.LOCACACA_BRANCH = ForgeItems.LOCACACA_BRANCH.get();
        ModItems.NEW_LOCACACA = ForgeItems.NEW_LOCACACA.get();
        ModItems.LUCKY_LIPSTICK = ForgeItems.LUCKY_LIPSTICK.get();
        ModItems.BLANK_MASK = ForgeItems.BLANK_MASK.get();
        ModItems.MODIFICATION_MASK = ForgeItems.MODIFICATION_MASK.get();
        ModItems.SPEEDWAGON_MASK = ForgeItems.SPEEDWAGON_MASK.get();
        ModItems.SPEEDWAGON_FOUNDATION_MASK = ForgeItems.SPEEDWAGON_FOUNDATION_MASK.get();
        ModItems.JOTARO_MASK = ForgeItems.JOTARO_MASK.get();
        ModItems.JOTARO_4_MASK = ForgeItems.JOTARO_4_MASK.get();
        ModItems.JOTARO_6_MASK = ForgeItems.JOTARO_6_MASK.get();
        ModItems.DIO_MASK = ForgeItems.DIO_MASK.get();
        ModItems.DIO_VAMPIRE_MASK = ForgeItems.DIO_VAMPIRE_MASK.get();
        ModItems.GUCCIO_MASK = ForgeItems.GUCCIO_MASK.get();
        ModItems.ENYA_MASK = ForgeItems.ENYA_MASK.get();
        ModItems.ENYA_OVA_MASK = ForgeItems.ENYA_OVA_MASK.get();
        ModItems.VALENTINE_MASK = ForgeItems.VALENTINE_MASK.get();
        ModItems.JOSUKE_PART_EIGHT_MASK = ForgeItems.JOSUKE_PART_EIGHT_MASK.get();
        ModItems.AYA_MASK = ForgeItems.AYA_MASK.get();
        ModItems.AESTHETICIAN_MASK_1 = ForgeItems.AESTHETICIAN_MASK_1.get();
        ModItems.AESTHETICIAN_MASK_2 = ForgeItems.AESTHETICIAN_MASK_2.get();
        ModItems.AESTHETICIAN_MASK_3 = ForgeItems.AESTHETICIAN_MASK_3.get();
        ModItems.AESTHETICIAN_MASK_4 = ForgeItems.AESTHETICIAN_MASK_4.get();
        ModItems.AESTHETICIAN_MASK_5 = ForgeItems.AESTHETICIAN_MASK_5.get();
        ModItems.AESTHETICIAN_MASK_ZOMBIE = ForgeItems.AESTHETICIAN_MASK_ZOMBIE.get();
        ModItems.AVDOL_MASK = ForgeItems.AVDOL_MASK.get();
        ModItems.KAKYOIN_MASK = ForgeItems.KAKYOIN_MASK.get();
        ModItems.DIEGO_MASK = ForgeItems.DIEGO_MASK.get();
        ModItems.RINGO_MASK = ForgeItems.RINGO_MASK.get();
        ModItems.HATO_MASK = ForgeItems.HATO_MASK.get();
        ModItems.SHIZUKA_MASK = ForgeItems.SHIZUKA_MASK.get();
        ModItems.PAINTING_VAN_GOUGH = ForgeItems.PAINTING_VAN_GOUGH.get();
        ModItems.PAINTING_MONA_LISA = ForgeItems.PAINTING_MONA_LISA.get();
        ModItems.PAINTING_BIRTH_OF_VENUS = ForgeItems.PAINTING_BIRTH_OF_VENUS.get();
        ModItems.POCOLOCO_MASK = ForgeItems.POCOLOCO_MASK.get();
        ModItems.TERRIER_SPAWN_EGG = ForgeItems.TERRIER_SPAWN_EGG.get();
        ModItems.AESTHETICIAN_SPAWN_EGG = ForgeItems.AESTHETICIAN_SPAWN_EGG.get();
        ModItems.ZOMBIE_AESTHETICIAN_SPAWN_EGG = ForgeItems.ZOMBIE_AESTHETICIAN_SPAWN_EGG.get();
        ModItems.MUSIC_DISC_TORTURE_DANCE = ForgeItems.MUSIC_DISC_TORTURE_DANCE.get();
        ModItems.MUSIC_DISC_HALLELUJAH = ForgeItems.MUSIC_DISC_HALLELUJAH.get();
        ModItems.FOG_BLOCK_ITEMS = ForgeCreativeTab.FOG_GROUP.get();

        ModItems.INTERDIMENSIONAL_KEY = ForgeItems.INTERDIMENSIONAL_KEY.get();

        ModItems.FLESH_BUCKET = ForgeItems.FLESH_BUCKET.get();

        ModItems.initializeVisageStore();

        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(Potions.AWKWARD, ModItems.LOCACACA_PIT, ForgeItems.HEX_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.REDSTONE, ForgeItems.HEX_POTION_EXTENDED.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.GLOWSTONE, ForgeItems.HEX_POTION_STRONG.get()));


        ModParticles.HIT_IMPACT = ForgeParticles.HIT_IMPACT.get();
        ModParticles.MELTING = ForgeParticles.MELTING.get();
        ModParticles.BLOOD = ForgeParticles.BLOOD.get();
        ModParticles.BLUE_BLOOD = ForgeParticles.BLUE_BLOOD.get();
        ModParticles.ENDER_BLOOD = ForgeParticles.ENDER_BLOOD.get();
        ModParticles.POINTER = ForgeParticles.POINTER.get();
        ModParticles.POINTER_SOFT = ForgeParticles.POINTER_SOFT.get();
        ModParticles.AIR_CRACKLE = ForgeParticles.AIR_CRACKLE.get();
        ModParticles.HEARTBEAT = ForgeParticles.HEARTBEAT.get();
        ModParticles.MENACING = ForgeParticles.MENACING.get();
        ModParticles.VACUUM = ForgeParticles.VACUUM.get();
        ModParticles.ORANGE_FLAME = ForgeParticles.ORANGE_FLAME.get();
        ModParticles.BLUE_FLAME = ForgeParticles.BLUE_FLAME.get();
        ModParticles.PURPLE_FLAME = ForgeParticles.PURPLE_FLAME.get();
        ModParticles.GREEN_FLAME = ForgeParticles.GREEN_FLAME.get();
        ModParticles.DREAD_FLAME = ForgeParticles.DREAD_FLAME.get();
        ModParticles.CREAM_FLAME = ForgeParticles.CREAM_FLAME.get();
        ModParticles.FOG_CHAIN = ForgeParticles.FOG_CHAIN.get();
        ModParticles.STAR = ForgeParticles.STAR.get();
        ModParticles.HEART_ATTACK_MINI = ForgeParticles.HEART_ATTACK_MINI.get();
        ModParticles.ENERGY_DISTORTION = ForgeParticles.ENERGY_DISTORTION.get();
        ModParticles.PURPLE_STAR = ForgeParticles.PURPLE_STAR.get();
        ModParticles.BLUE_SPARKLE = ForgeParticles.BLUE_SPARKLE.get();
        ModParticles.PUNCH_IMPACT_A = ForgeParticles.PUNCH_IMPACT_A.get();
        ModParticles.PUNCH_IMPACT_B = ForgeParticles.PUNCH_IMPACT_B.get();
        ModParticles.PUNCH_IMPACT_C = ForgeParticles.PUNCH_IMPACT_C.get();
        ModParticles.PUNCH_MISS = ForgeParticles.PUNCH_MISS.get();
        ModParticles.BUBBLE_TRAIL = ForgeParticles.BUBBLE_TRAIL.get();
        ModParticles.WARDEN_CLOCK = ForgeParticles.WARDEN_CLOCK.get();
        ModParticles.CLOCK = ForgeParticles.CLOCK.get();
        ModParticles.RED_CLOCK = ForgeParticles.RED_CLOCK.get();
        ModParticles.GREEN_CLOCK = ForgeParticles.GREEN_CLOCK.get();
        ModParticles.ORANGE_CLOCK = ForgeParticles.ORANGE_CLOCK.get();
        ModParticles.TIME_EMBER = ForgeParticles.TIME_EMBER.get();
        ModParticles.ZAP = ForgeParticles.ZAP.get();
        ModParticles.BLUE_CLOCK = ForgeParticles.BLUE_CLOCK.get();
        ModParticles.CINDERELLA_GLOW = ForgeParticles.CINDERELLA_GLOW.get();
        ModParticles.PINK_SMOKE = ForgeParticles.PINK_SMOKE.get();
        ModParticles.BLOOD_MIST = ForgeParticles.BLOOD_MIST.get();
        ModParticles.BUBBLE_POP = ForgeParticles.BUBBLE_POP.get();
        ModParticles.PLUNDER = ForgeParticles.PLUNDER.get();
        ModParticles.FRICTIONLESS = ForgeParticles.FRICTIONLESS.get();
        ModParticles.EXCLAMATION = ForgeParticles.EXCLAMATION.get();
        ModParticles.BABY_CRACKLE = ForgeParticles.BABY_CRACKLE.get();
        ModParticles.MAGIC_DUST = ForgeParticles.MAGIC_DUST.get();
        ModParticles.BRIEF_MAGIC_DUST = ForgeParticles.BRIEF_MAGIC_DUST.get();
        ModParticles.D4C_LINES = ForgeParticles.D4C_LINES.get();
        ModParticles.STITCH = ForgeParticles.STITCH.get();
        ModParticles.HYPNO_SWIRL = ForgeParticles.HYPNO_SWIRL.get();
        ModParticles.MOLD_DUST = ForgeParticles.MOLD_DUST.get();
        ModParticles.MOLD = ForgeParticles.MOLD.get();
        ModParticles.RAGING_LIGHT = ForgeParticles.RAGING_LIGHT.get();
        ModParticles.ALLURING_LIGHT = ForgeParticles.ALLURING_LIGHT.get();
        ModParticles.DUST_CRUMBLE = ForgeParticles.DUST_CRUMBLE.get();
        ModParticles.FIRE_CRUMBLE = ForgeParticles.FIRE_CRUMBLE.get();
        ModParticles.SOUL_FIRE_CRUMBLE = ForgeParticles.SOUL_FIRE_CRUMBLE.get();
        ModParticles.ROAD_ROLLER_SCRAP = ForgeParticles.ROAD_ROLLER_SCRAP.get();
        ModParticles.ROAD_ROLLER_EXPLOSION = ForgeParticles.ROAD_ROLLER_EXPLOSION.get();
        ModParticles.ROAD_ROLLER_SMOKE = ForgeParticles.ROAD_ROLLER_SMOKE.get();

        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();

        ModEffects.BLEED = ForgeEffects.BLEED.get();
        ModEffects.HEX = ForgeEffects.HEX.get();
        ModEffects.STAND_VIRUS = ForgeEffects.STAND_VIRUS.get();
        ModEffects.CAPTURING_LOVE = ForgeEffects.CAPTURING_LOVE.get();
        ModEffects.FACELESS = ForgeEffects.FACELESS.get();
        ModEffects.MELTING = ForgeEffects.MELTING.get();
        ModEffects.GRAVITY_FLIP = ForgeEffects.GRAVITY_FLIP.get();
        ModEffects.WARDING = ForgeEffects.WARDING.get();
        ModEffects.VAMPIRE_BLOOD = ForgeEffects.VAMPIRE_BLOOD.get();



        ModEntities.VAN_GOUGH_PAINTING = ForgeEntities.VAN_GOUGH_PAINTING.get();
        ModEntities.MONA_LISA_PAINTING = ForgeEntities.MONA_LISA_PAINTING.get();
        ModEntities.BIRTH_OF_VENUS_PAINTING = ForgeEntities.BIRTH_OF_VENUS_PAINTING.get();
        ModEntities.THE_WORLD = ForgeEntities.THE_WORLD.get();
        ModEntities.THE_WORLD_ULTIMATE = ForgeEntities.THE_WORLD_ULTIMATE.get();
        ModEntities.TERRIER_DOG = ForgeEntities.TERRIER_DOG.get();
        ModEntities.STAR_PLATINUM = ForgeEntities.STAR_PLATINUM.get();
        ModEntities.JUSTICE = ForgeEntities.JUSTICE.get();
        ModEntities.MAGICIANS_RED = ForgeEntities.MAGICIANS_RED.get();
        ModEntities.MAGICIANS_RED_OVA = ForgeEntities.MAGICIANS_RED_OVA.get();
        ModEntities.SURVIVOR = ForgeEntities.SURVIVOR.get();
        ModEntities.D4C = ForgeEntities.D4C.get();
        ModEntities.CREAM = ForgeEntities.CREAM.get();
        ModEntities.GREEN_DAY = ForgeEntities.GREEN_DAY.get();
        ModEntities.RATT = ForgeEntities.RATT.get();
        ModEntities.REDD = ForgeEntities.REDD.get();
        ModEntities.CHAIR_RATT = ForgeEntities.CHAIR_RAT.get();
        ModEntities.FLESH_PILE = ForgeEntities.FLESH_PILE.get();
        ModEntities.SOFT_AND_WET = ForgeEntities.SOFT_AND_WET.get();
        ModEntities.SOFT_AND_WET_KING = ForgeEntities.SOFT_AND_WET_KING.get();
        ModEntities.SOFT_AND_WET_DROWNED = ForgeEntities.SOFT_AND_WET_DROWNED.get();
        ModEntities.SOFT_AND_WET_DEBUT = ForgeEntities.SOFT_AND_WET_DEBUT.get();
        ModEntities.SOFT_AND_WET_KILLER_QUEEN = ForgeEntities.SOFT_AND_WET_KILLER_QUEEN.get();
        ModEntities.KILLER_QUEEN = ForgeEntities.KILLER_QUEEN.get();
        ModEntities.CINDERELLA = ForgeEntities.CINDERELLA.get();
        ModEntities.WALKING_HEART = ForgeEntities.WALKING_HEART.get();
        ModEntities.JUSTICE_PIRATE = ForgeEntities.JUSTICE_PIRATE.get();
        ModEntities.DARK_MIRAGE = ForgeEntities.DARK_MIRAGE.get();
        ModEntities.STAR_PLATINUM_BASEBALL = ForgeEntities.STAR_PLATINUM_BASEBALL.get();
        ModEntities.DIVER_DOWN = ForgeEntities.DIVER_DOWN.get();
        ModEntities.THROWN_HARPOON = ForgeEntities.THROWN_HARPOON.get();
        ModEntities.BLADED_BOWLER_HAT = ForgeEntities.BLADED_BOWLER_HAT.get();
        ModEntities.ROUNDABOUT_BULLET_ENTITY = ForgeEntities.ROUNDABOUT_BULLET_ENTITY.get();
        ModEntities.THROWN_KNIFE = ForgeEntities.THROWN_KNIFE.get();
        ModEntities.RATT_DART = ForgeEntities.RATT_DART.get();
        ModEntities.THROWN_MATCH = ForgeEntities.THROWN_MATCH.get();
        ModEntities.THROWN_WATER_BOTTLE = ForgeEntities.THROWN_WATER_BOTTLE.get();
        ModEntities.CROSSFIRE_HURRICANE = ForgeEntities.CROSSFIRE_HURRICANE.get();
        ModEntities.LIFE_TRACKER = ForgeEntities.LIFE_TRACKER.get();
        ModEntities.STAND_FIREBALL = ForgeEntities.STAND_FIREBALL.get();
        ModEntities.GASOLINE_CAN = ForgeEntities.GASOLINE_CAN.get();
        ModEntities.GASOLINE_SPLATTER = ForgeEntities.GASOLINE_SPLATTER.get();
        ModEntities.BLOOD_SPLATTER = ForgeEntities.BLOOD_SPLATTER.get();
        ModEntities.STAND_ARROW = ForgeEntities.STAND_ARROW.get();
        ModEntities.ROAD_ROLLER_ENTITY = ForgeEntities.ROAD_ROLLER_ENTITY.get();
        ModEntities.SEPERATED_LEGS = ForgeEntities.SEPERATED_LEGS.get();

        ModEntities.THROWN_OBJECT = ForgeEntities.THROWN_OBJECT.get();
        ModEntities.CONCEALED_FLAME_OBJECT = ForgeEntities.CONCEALED_FLAME_OBJECT.get();
        ModEntities.GROUND_HURRICANE = ForgeEntities.GROUND_HURRICANE.get();
        ModEntities.GROUND_BUBBLE = ForgeEntities.GROUND_BUBBLE.get();
        ModEntities.PLUNDER_BUBBLE = ForgeEntities.PLUNDER_BUBBLE.get();
        ModEntities.EXPLOSIVE_BUBBLE = ForgeEntities.EXPLOSIVE_BUBBLE.get();
        ModEntities.ITEM_LAUNCHING_BUBBLE_ENTITY = ForgeEntities.ITEM_LAUNCHING_BUBBLE_ENTITY.get();
        ModEntities.GO_BEYOND = ForgeEntities.GO_BEYOND.get();
        ModEntities.ENCASEMENT_BUBBLE = ForgeEntities.ENCASEMENT_BUBBLE.get();
        ModEntities.CINDERELLA_VISAGE_DISPLAY = ForgeEntities.CINDERELLA_VISAGE_DISPLAY.get();
        ModEntities.ANUBIS_POSSESSOR = ForgeEntities.ANUBIS_POSSESSOR.get();
        ModEntities.ANUBIS_SLIPSTREAM = ForgeEntities.ANUBIS_SLIPSTREAM.get();

        ModEntities.FALLEN_ZOMBIE = ForgeEntities.FALLEN_ZOMBIE.get();
        ModEntities.FALLEN_SKELETON = ForgeEntities.FALLEN_SKELETON.get();
        ModEntities.FALLEN_SPIDER = ForgeEntities.FALLEN_SPIDER.get();
        ModEntities.FALLEN_VILLAGER = ForgeEntities.FALLEN_VILLAGER.get();
        ModEntities.FALLEN_CREEPER = ForgeEntities.FALLEN_CREEPER.get();
        ModEntities.FALLEN_PHANTOM = ForgeEntities.FALLEN_PHANTOM.get();


        ModEntities.OVA_ENYA = ForgeEntities.OVA_ENYA.get();
        ModEntities.ENYA = ForgeEntities.ENYA.get();
        ModEntities.JOTARO = ForgeEntities.JOTARO.get();
        ModEntities.DIO = ForgeEntities.DIO.get();
        ModEntities.PARALLEL_DIEGO = ForgeEntities.PARALLEL_DIEGO.get();
        ModEntities.JOSUKE_PART_EIGHT = ForgeEntities.JOSUKE_PART_EIGHT.get();
        ModEntities.AVDOL = ForgeEntities.AVDOL.get();
        ModEntities.AYA = ForgeEntities.AYA.get();
        ModEntities.AESTHETICIAN = ForgeEntities.AESTHETICIAN.get();
        ModEntities.ZOMBIE_AESTHETICIAN = ForgeEntities.ZOMBIE_AESTHETICIAN.get();
        ModEntities.POCOLOCO = ForgeEntities.POCOLOCO.get();
        ModEntities.GUCCIO = ForgeEntities.GUCCIO.get();
        ModEntities.RINGO = ForgeEntities.RINGO.get();
        ModEntities.HATO = ForgeEntities.HATO.get();
        ModEntities.SHIZUKA = ForgeEntities.SHIZUKA.get();
        ModEntities.VALENTINE = ForgeEntities.VALENTINE.get();
        ModEntities.STEVE_NPC = ForgeEntities.STEVE_NPC.get();
        ModEntities.ALEX_NPC = ForgeEntities.ALEX_NPC.get();
        ModEntities.MODIFIED_NPC = ForgeEntities.MODIFIED_NPC.get();
        ModEntities.MODIFIED_NPC = ForgeEntities.MODIFIED_NPC.get();
        ModEntities.FOG_CLONE = ForgeEntities.FOG_CLONE.get();
        ModEntities.D4C_CLONE = ForgeEntities.D4C_CLONE.get();

        ForgeItems.assignStupidForge();
    }

    /**
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }
    */
}
