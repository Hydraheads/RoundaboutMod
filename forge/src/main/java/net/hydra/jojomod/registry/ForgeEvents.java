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
import net.hydra.jojomod.entity.pathfinding.GroundBubbleEntity;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
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
        event.put(ForgeEntities.SOFT_AND_WET.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_KING.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_DROWNED.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_DEBUT.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.SOFT_AND_WET_KILLER_QUEEN.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.KILLER_QUEEN.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.CINDERELLA.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.MAGICIANS_RED.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.MAGICIANS_RED_OVA.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.DARK_MIRAGE.get(), StandEntity.createStandAttributes().build());
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
        event.put(ForgeEntities.POCOLOCO.get(), PocolocoNPC.createAttributes().build());
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
        event.put(ForgeEntities.GROUND_HURRICANE.get(), GroundHurricaneEntity.createStandAttributes().build());
        event.put(ForgeEntities.GROUND_BUBBLE.get(), GroundBubbleEntity.createStandAttributes().build());
        event.put(ForgeEntities.LIFE_TRACKER.get(), LifeTrackerEntity.createStandAttributes().build());
        event.put(ForgeEntities.D4C_CLONE.get(), D4CCloneEntity.createAttributes().build());
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
        ModBlocks.REGAL_FLOOR = ForgeBlocks.REGAL_FLOOR.get();
        ModBlocks.WOODEN_MANOR_TABLE = ForgeBlocks.WOODEN_MANOR_TABLE.get();
        ModBlocks.REGAL_WALL = ForgeBlocks.REGAL_WALL.get();
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
        ModBlocks.FOG_DIRT = ForgeBlocks.FOG_DIRT.get();
        ModBlocks.FOG_DIRT_COATING = ForgeBlocks.FOG_DIRT_COATING.get();
        ModBlocks.FOG_CLAY = ForgeBlocks.FOG_CLAY.get();
        ModBlocks.FOG_CLAY_COATING = ForgeBlocks.FOG_CLAY_COATING.get();
        ModBlocks.FOG_GRAVEL = ForgeBlocks.FOG_GRAVEL.get();
        ModBlocks.FOG_GRAVEL_COATING = ForgeBlocks.FOG_GRAVEL_COATING.get();
        ModBlocks.FOG_SAND = ForgeBlocks.FOG_SAND.get();
        ModBlocks.FOG_SAND_COATING = ForgeBlocks.FOG_SAND_COATING.get();
        ModBlocks.FOG_OAK_PLANKS = ForgeBlocks.FOG_OAK_PLANKS.get();
        ModBlocks.FOG_OAK_PLANKS_COATING = ForgeBlocks.FOG_OAK_PLANKS_COATING.get();
        ModBlocks.FOG_SPRUCE_PLANKS = ForgeBlocks.FOG_SPRUCE_PLANKS.get();
        ModBlocks.FOG_SPRUCE_PLANKS_COATING = ForgeBlocks.FOG_SPRUCE_PLANKS_COATING.get();
        ModBlocks.FOG_BIRCH_PLANKS = ForgeBlocks.FOG_BIRCH_PLANKS.get();
        ModBlocks.FOG_BIRCH_PLANKS_COATING = ForgeBlocks.FOG_BIRCH_PLANKS_COATING.get();
        ModBlocks.FOG_JUNGLE_PLANKS = ForgeBlocks.FOG_JUNGLE_PLANKS.get();
        ModBlocks.FOG_JUNGLE_PLANKS_COATING = ForgeBlocks.FOG_JUNGLE_PLANKS_COATING.get();
        ModBlocks.FOG_ACACIA_PLANKS = ForgeBlocks.FOG_ACACIA_PLANKS.get();
        ModBlocks.FOG_ACACIA_PLANKS_COATING = ForgeBlocks.FOG_ACACIA_PLANKS_COATING.get();
        ModBlocks.FOG_DARK_OAK_PLANKS = ForgeBlocks.FOG_DARK_OAK_PLANKS.get();
        ModBlocks.FOG_DARK_OAK_PLANKS_COATING = ForgeBlocks.FOG_DARK_OAK_PLANKS_COATING.get();
        ModBlocks.FOG_MANGROVE_PLANKS = ForgeBlocks.FOG_MANGROVE_PLANKS.get();
        ModBlocks.FOG_MANGROVE_PLANKS_COATING = ForgeBlocks.FOG_MANGROVE_PLANKS_COATING.get();
        ModBlocks.FOG_CHERRY_PLANKS = ForgeBlocks.FOG_CHERRY_PLANKS.get();
        ModBlocks.FOG_CHERRY_PLANKS_COATING = ForgeBlocks.FOG_CHERRY_PLANKS_COATING.get();
        ModBlocks.FOG_STONE = ForgeBlocks.FOG_STONE.get();
        ModBlocks.FOG_STONE_COATING = ForgeBlocks.FOG_STONE_COATING.get();
        ModBlocks.FOG_COBBLESTONE = ForgeBlocks.FOG_COBBLESTONE.get();
        ModBlocks.FOG_COBBLESTONE_COATING = ForgeBlocks.FOG_COBBLESTONE_COATING.get();
        ModBlocks.FOG_MOSSY_COBBLESTONE = ForgeBlocks.FOG_MOSSY_COBBLESTONE.get();
        ModBlocks.FOG_MOSSY_COBBLESTONE_COATING = ForgeBlocks.FOG_MOSSY_COBBLESTONE_COATING.get();
        ModBlocks.FOG_COAL_ORE = ForgeBlocks.FOG_COAL_ORE.get();
        ModBlocks.FOG_IRON_ORE = ForgeBlocks.FOG_IRON_ORE.get();
        ModBlocks.FOG_GOLD_ORE = ForgeBlocks.FOG_GOLD_ORE.get();
        ModBlocks.FOG_LAPIS_ORE = ForgeBlocks.FOG_LAPIS_ORE.get();
        ModBlocks.FOG_DIAMOND_ORE = ForgeBlocks.FOG_DIAMOND_ORE.get();
        ModBlocks.FOG_STONE_BRICKS = ForgeBlocks.FOG_STONE_BRICKS.get();
        ModBlocks.FOG_STONE_BRICKS_COATING = ForgeBlocks.FOG_STONE_BRICKS_COATING.get();
        ModBlocks.FOG_DEEPSLATE = ForgeBlocks.FOG_DEEPSLATE.get();
        ModBlocks.FOG_DEEPSLATE_COATING = ForgeBlocks.FOG_DEEPSLATE_COATING.get();
        ModBlocks.FOG_NETHERRACK = ForgeBlocks.FOG_NETHERRACK.get();
        ModBlocks.FOG_NETHERRACK_COATING = ForgeBlocks.FOG_NETHERRACK_COATING.get();
        ModBlocks.FOG_NETHER_BRICKS_COATING = ForgeBlocks.FOG_NETHER_BRICKS_COATING.get();
        ModBlocks.FOG_NETHER_BRICKS = ForgeBlocks.FOG_NETHER_BRICKS.get();
        ModBlocks.FOG_NETHER_BRICKS_COATING = ForgeBlocks.FOG_NETHER_BRICKS_COATING.get();
        ModBlocks.STEREO = ForgeBlocks.STEREO.get();
        ModBlocks.STAND_FIRE = ForgeBlocks.STAND_FIRE.get();
        ModBlocks.BUBBLE_SCAFFOLD = ForgeBlocks.BUBBLE_SCAFFOLD.get();
        ModBlocks.D4C_LIGHT_BLOCK = ForgeBlocks.D4C_LIGHT_BLOCK.get();

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
        ModBlocks.D4C_LIGHT_BLOCK_ENTITY = ForgeBlocks.D4C_LIGHT_BLOCK_ENTITY.get();

        ModItems.STAND_ARROW = ForgeItems.STAND_ARROW.get();
        ModItems.WORTHY_ARROW = ForgeItems.WORTHY_ARROW.get();
        ModItems.STAND_BEETLE_ARROW = ForgeItems.STAND_BEETLE_ARROW.get();
        ModItems.LUCK_UPGRADE = ForgeItems.LUCK_UPGRADE.get();
        ModItems.EXECUTION_UPGRADE = ForgeItems.EXECUTION_UPGRADE.get();
        ModItems.LUCK_SWORD = ForgeItems.LUCK_SWORD.get();
        ModItems.WOODEN_GLAIVE = ForgeItems.WOODEN_GLAIVE.get();
        ModItems.STONE_GLAIVE = ForgeItems.STONE_GLAIVE.get();
        ModItems.IRON_GLAIVE = ForgeItems.IRON_GLAIVE.get();
        ModItems.GOLDEN_GLAIVE = ForgeItems.GOLDEN_GLAIVE.get();
        ModItems.DIAMOND_GLAIVE = ForgeItems.DIAMOND_GLAIVE.get();
        ModItems.NETHERITE_GLAIVE = ForgeItems.NETHERITE_GLAIVE.get();
        ModItems.SCISSORS = ForgeItems.SCISSORS.get();
        ModItems.EXECUTIONER_AXE = ForgeItems.EXECUTIONER_AXE.get();
        ModItems.BODY_BAG = ForgeItems.BODY_BAG.get();
        ModItems.CREATIVE_BODY_BAG = ForgeItems.CREATIVE_BODY_BAG.get();
        ModItems.HARPOON = ForgeItems.HARPOON.get();
        ModItems.KNIFE = ForgeItems.KNIFE.get();
        ModItems.KNIFE_BUNDLE = ForgeItems.KNIFE_BUNDLE.get();
        ModItems.MATCH = ForgeItems.MATCH.get();
        ModItems.MATCH_BUNDLE = ForgeItems.MATCH_BUNDLE.get();
        ModItems.GASOLINE_CAN = ForgeItems.GASOLINE_CAN.get();
        ModItems.GASOLINE_BUCKET = ForgeItems.GASOLINE_BUCKET.get();
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
        ModItems.STAND_DISC_SOFT_AND_WET = ForgeItems.STAND_DISC_SOFT_AND_WET.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_SOFT_AND_WET.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_SOFT_AND_WET.get());
        ModItems.MAX_STAND_DISC_SOFT_AND_WET = ForgeItems.MAX_STAND_DISC_SOFT_AND_WET.get();
        ModItems.STAND_DISC_CINDERELLA = ForgeItems.STAND_DISC_CINDERELLA.get();
        ModItems.STAND_DISC_HEY_YA = ForgeItems.STAND_DISC_HEY_YA.get();
        ModItems.STAND_DISC_MANDOM = ForgeItems.STAND_DISC_MANDOM.get();
        ModItems.COFFEE_GUM = ForgeItems.COFFEE_GUM.get();

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
        ModItems.JOTARO_MASK = ForgeItems.JOTARO_MASK.get();
        ModItems.DIO_MASK = ForgeItems.DIO_MASK.get();
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
        ModItems.DIEGO_MASK = ForgeItems.DIEGO_MASK.get();
        ModItems.RINGO_MASK = ForgeItems.RINGO_MASK.get();
        ModItems.POCOLOCO_MASK = ForgeItems.POCOLOCO_MASK.get();
        ModItems.TERRIER_SPAWN_EGG = ForgeItems.TERRIER_SPAWN_EGG.get();
        ModItems.AESTHETICIAN_SPAWN_EGG = ForgeItems.AESTHETICIAN_SPAWN_EGG.get();
        ModItems.ZOMBIE_AESTHETICIAN_SPAWN_EGG = ForgeItems.ZOMBIE_AESTHETICIAN_SPAWN_EGG.get();
        ModItems.MUSIC_DISC_TORTURE_DANCE = ForgeItems.MUSIC_DISC_TORTURE_DANCE.get();
        ModItems.MUSIC_DISC_HALLELUJAH = ForgeItems.MUSIC_DISC_HALLELUJAH.get();
        ModItems.FOG_BLOCK_ITEMS = ForgeCreativeTab.FOG_GROUP.get();

        ModItems.INTERDIMENSIONAL_KEY = ForgeItems.INTERDIMENSIONAL_KEY.get();

        ModItems.initializeVisageStore();

        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(Potions.AWKWARD, ModItems.LOCACACA_PIT, ForgeItems.HEX_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.REDSTONE, ForgeItems.HEX_POTION_EXTENDED.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.GLOWSTONE, ForgeItems.HEX_POTION_STRONG.get()));


        ModParticles.HIT_IMPACT = ForgeParticles.HIT_IMPACT.get();
        ModParticles.BLOOD = ForgeParticles.BLOOD.get();
        ModParticles.BLUE_BLOOD = ForgeParticles.BLUE_BLOOD.get();
        ModParticles.ENDER_BLOOD = ForgeParticles.ENDER_BLOOD.get();
        ModParticles.POINTER = ForgeParticles.POINTER.get();
        ModParticles.POINTER_SOFT = ForgeParticles.POINTER_SOFT.get();
        ModParticles.AIR_CRACKLE = ForgeParticles.AIR_CRACKLE.get();
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
        ModParticles.BUBBLE_TRAIL = ForgeParticles.BUBBLE_TRAIL.get();
        ModParticles.WARDEN_CLOCK = ForgeParticles.WARDEN_CLOCK.get();
        ModParticles.CLOCK = ForgeParticles.CLOCK.get();
        ModParticles.RED_CLOCK = ForgeParticles.RED_CLOCK.get();
        ModParticles.GREEN_CLOCK = ForgeParticles.GREEN_CLOCK.get();
        ModParticles.BLUE_CLOCK = ForgeParticles.BLUE_CLOCK.get();
        ModParticles.CINDERELLA_GLOW = ForgeParticles.CINDERELLA_GLOW.get();
        ModParticles.PINK_SMOKE = ForgeParticles.PINK_SMOKE.get();
        ModParticles.BUBBLE_POP = ForgeParticles.BUBBLE_POP.get();
        ModParticles.PLUNDER = ForgeParticles.PLUNDER.get();
        ModParticles.FRICTIONLESS = ForgeParticles.FRICTIONLESS.get();
        ModParticles.EXCLAMATION = ForgeParticles.EXCLAMATION.get();
        ModParticles.D4C_LINES = ForgeParticles.D4C_LINES.get();
        ModParticles.STITCH = ForgeParticles.STITCH.get();
        ModParticles.MOLD_DUST = ForgeParticles.MOLD_DUST.get();
        ModParticles.MOLD = ForgeParticles.MOLD.get();

        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();

        ModEffects.BLEED = ForgeEffects.BLEED.get();
        ModEffects.HEX = ForgeEffects.HEX.get();
        ModEffects.STAND_VIRUS = ForgeEffects.STAND_VIRUS.get();
        ModEffects.CAPTURING_LOVE = ForgeEffects.CAPTURING_LOVE.get();
        ModEffects.FACELESS = ForgeEffects.FACELESS.get();

        ModEntities.THE_WORLD = ForgeEntities.THE_WORLD.get();
        ModEntities.THE_WORLD_ULTIMATE = ForgeEntities.THE_WORLD_ULTIMATE.get();
        ModEntities.TERRIER_DOG = ForgeEntities.TERRIER_DOG.get();
        ModEntities.STAR_PLATINUM = ForgeEntities.STAR_PLATINUM.get();
        ModEntities.JUSTICE = ForgeEntities.JUSTICE.get();
        ModEntities.MAGICIANS_RED = ForgeEntities.MAGICIANS_RED.get();
        ModEntities.MAGICIANS_RED_OVA = ForgeEntities.MAGICIANS_RED_OVA.get();
        ModEntities.D4C = ForgeEntities.D4C.get();
        ModEntities.GREEN_DAY = ForgeEntities.GREEN_DAY.get();
        ModEntities.SOFT_AND_WET = ForgeEntities.SOFT_AND_WET.get();
        ModEntities.SOFT_AND_WET_KING = ForgeEntities.SOFT_AND_WET_KING.get();
        ModEntities.SOFT_AND_WET_DROWNED = ForgeEntities.SOFT_AND_WET_DROWNED.get();
        ModEntities.SOFT_AND_WET_DEBUT = ForgeEntities.SOFT_AND_WET_DEBUT.get();
        ModEntities.SOFT_AND_WET_KILLER_QUEEN = ForgeEntities.SOFT_AND_WET_KILLER_QUEEN.get();
        ModEntities.KILLER_QUEEN = ForgeEntities.KILLER_QUEEN.get();
        ModEntities.CINDERELLA = ForgeEntities.CINDERELLA.get();
        ModEntities.JUSTICE_PIRATE = ForgeEntities.JUSTICE_PIRATE.get();
        ModEntities.DARK_MIRAGE = ForgeEntities.DARK_MIRAGE.get();
        ModEntities.STAR_PLATINUM_BASEBALL = ForgeEntities.STAR_PLATINUM_BASEBALL.get();
        ModEntities.THROWN_HARPOON = ForgeEntities.THROWN_HARPOON.get();
        ModEntities.THROWN_KNIFE = ForgeEntities.THROWN_KNIFE.get();
        ModEntities.THROWN_MATCH = ForgeEntities.THROWN_MATCH.get();
        ModEntities.CROSSFIRE_HURRICANE = ForgeEntities.CROSSFIRE_HURRICANE.get();
        ModEntities.LIFE_TRACKER = ForgeEntities.LIFE_TRACKER.get();
        ModEntities.STAND_FIREBALL = ForgeEntities.STAND_FIREBALL.get();
        ModEntities.GASOLINE_CAN = ForgeEntities.GASOLINE_CAN.get();
        ModEntities.GASOLINE_SPLATTER = ForgeEntities.GASOLINE_SPLATTER.get();
        ModEntities.STAND_ARROW = ForgeEntities.STAND_ARROW.get();

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
        ModEntities.FALLEN_ZOMBIE = ForgeEntities.FALLEN_ZOMBIE.get();
        ModEntities.FALLEN_SKELETON = ForgeEntities.FALLEN_SKELETON.get();
        ModEntities.FALLEN_SPIDER = ForgeEntities.FALLEN_SPIDER.get();
        ModEntities.FALLEN_VILLAGER = ForgeEntities.FALLEN_VILLAGER.get();
        ModEntities.FALLEN_CREEPER = ForgeEntities.FALLEN_CREEPER.get();

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
        ModEntities.RINGO = ForgeEntities.RINGO.get();
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
